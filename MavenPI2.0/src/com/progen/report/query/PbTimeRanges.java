/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

/**
 * @filename PbTimeRanges
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 25, 2009, 4:41:59 PM
 */
import com.progen.contypes.GetConnectionType;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

public class PbTimeRanges extends PbDb {

    public static Logger logger = Logger.getLogger(PbTimeRanges.class);
    public String d_clu = "";
    public String pd_clu = "";
    public String date_type = "";
    String ft_clu = "";
    public String st_d = "";
    public String ed_d = "";
    public String p_st_d = "";
    public String p_ed_d = "";
    String ft_st_d = "";
    String ft_ed_d = "";
    public String st_d1 = "";
    public String ed_d1 = "";
    public String p_st_d1 = "";
    public String p_ed_d1 = "";
    java.util.Date dst_d = new Date(0);
    java.util.Date ded_d = new Date(0);
    java.util.Date dp_st_d = new Date(0);
    java.util.Date dp_ed_d = new Date(0);
    java.util.Date dft_st_d = new Date(0);
    java.util.Date dft_ed_d = new Date(0);
    ProgenConnection pg = null;
    PbReturnObject pbretObj = null;
    public String elementID;
    public String timeTableName = "pr_day_denom";
    //added by susheela 07-12-09
    String d_tar_clu = "";
    String p_tar_clu = "";
    //added by susheela 12-01-10
    String qtr_tar_clu = "";
    String qtr_tar_clu2 = "";
    String timeStamp = null;
    String connType = null;
    public int daysDiff = 0;//changed by sruthi for manual budget
    String rollingLevel = "Day";
    Map<String, Integer> trendNoOfDays;
    String daySql = " SELECT DDATE,  ST_DATE,  END_DATE,  P_ST_DATE,N_ST_DATE,  DAYSOFYEAR,  PDAYSOFYEAR,  NDAYSOFYEAR,  CWEEK,  CWYEAR,  CW_ST_DATE,                      "
            + "   CW_END_DATE,  CW_ST_MON_DATE,  CW_END_MON_DATE,  CW_CUST_NAME,  PW_DAY,  PW_ST_DATE,  PW_END_DATE,  PW_ST_MON_DATE,  PW_END_MON_DATE,              "
            + "   PW_CUST_NAME,  NW_DAY,  NW_ST_DATE,  NW_END_DATE,  NW_ST_MON_DATE,  NW_END_MON_DATE,  NW_CUST_NAME,  LYW_DAY,  LYW_ST_DATE,  LYW_END_DATE,         "
            + "   LYW_ST_MON_DATE, LYW_END_MON_DATE,   LYW_CUST_NAME,  NYW_DAY,  NYW_ST_DATE,  NYW_END_DATE,  NYW_ST_MON_DATE,  NYW_END_MON_DATE,  NYW_CUST_NAME,    "
            + "   CMONTH,  CM_YEAR,  CM_ST_DATE,  CM_END_DATE,  CM_CUST_NAME,  PM_DAY,  PM_ST_DATE,  PM_END_DATE,  PM_CUST_NAME,  NM_DAY,  NM_ST_DATE,               "
            + "   NM_END_DATE,  NM_CUST_NAME,  PYM_DAY,  PYM_ST_DATE,  PYM_END_DATE,  PYM_CUST_NAME,  NYM_DAY,  NYM_ST_DATE,  NYM_END_DATE,  NYM_CUST_NAME,          "
            + "   CQTR,  CQ_YEAR,  CQ_ST_DATE,  CQ_END_DATE, CQ_CUST_NAME,  LQ_DAY,  LQ_ST_DATE,  LQ_END_DATE,  LQ_CUST_NAME,  NQ_DAY,  NQ_ST_DATE,                  "
            + "   NQ_END_DATE,  NQ_CUST_NAME,  LYQ_DAY,  LYQ_ST_DATE,  LYQ_END_DATE,  LYQ_CUST_NAME,  NYQ_DAY,  NYQ_ST_DATE,  NYQ_END_DATE,  NYQ_CUST_NAME,          "
            + "   CYEAR,  CY_ST_DATE,  CY_END_DATE,  CY_CUST_NAME,  LY_DAY,  LY_ST_DATE,  LY_END_DATE,  LY_CUST_NAME,  NY_DAY,  NY_ST_DATE,  NY_END_DATE,            "
            + "   NY_CUST_NAME,  NYEAR,  LYEAR FROM PR_DAY_DENOM where ddate =?";
    public String connId;

    public PbTimeRanges() {
    }

    //added by susheela 07-12-09 start
    //added by susheela 07-12-09 start
    public String summTableCalculation(String st_date, String end_date, boolean istrend, String Timelevel, String SummLevel) {
        GetConnectionType gettypeofconn = new GetConnectionType();
        connType = gettypeofconn.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("SqlServer")) {
            st_date = "   convert(datetime,'" + st_date + "',120) ";
            ////.println("d_clu   -- --  " + d_clu);
            end_date = "   convert(datetime,'" + end_date + "',120) ";

        } else if (connType.equalsIgnoreCase("Oracle")) {
            st_date = "   to_date('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";
            ////.println("d_clu   -- --  " + d_clu);
            end_date = "   to_date('" + end_date + "','mm/dd/yyyy hh24:mi:ss ') ";

        } else if (connType.equalsIgnoreCase("Postgres")) {
            st_date = "   to_timestamp('" + st_date + "','mm/dd/yyyy hh24:mi:ss ') ";
            ////.println("d_clu   -- --  " + d_clu);
            end_date = "   to_timestamp('" + end_date + "','mm/dd/yyyy hh24:mi:ss ') ";

        }

        String sqlstr = "";
        if (!istrend) {
            sqlstr += " select   distinct                                       ";
            sqlstr += " case                                            ";
            sqlstr += " when type1 ='DB' then 'D'                       ";
            sqlstr += " when type1 ='DA' then 'D'                       ";
            sqlstr += " else type1 end time_summ_flag ,                 ";
            sqlstr += " case                                            ";
            sqlstr += " when type1 ='DB' then d_st_date                 ";
            sqlstr += " when type1 ='DA' then month_after               ";
            sqlstr += " when type1 ='M' then A1                         ";
            sqlstr += " when type1 ='Q' then A2                         ";
            sqlstr += " else A3 end T001_st_date,                            ";
            sqlstr += " case                                            ";
            sqlstr += " when type1 ='DB' then month_before              ";
            sqlstr += " when type1 ='DA' then ed_st_date                ";
            sqlstr += " when type1 ='M' then A4                         ";
            sqlstr += " when type1 ='Q' then A5                         ";
            sqlstr += " else A6  end T001_end_date                           ";
            sqlstr += "                                                 ";
            sqlstr += "                                                 ";
            sqlstr += " from (                                          ";
            sqlstr += " select                                          ";
            sqlstr += " distinct                                        ";
            sqlstr += " (case when month_before is not null and A1 is null  then 'DB'   ";
            sqlstr += " when month_after is not null and A1 is null then 'DA'          ";
            sqlstr += " when A2 is null and A3 is null then 'M'         ";
            sqlstr += " when A3 is null then 'Q'                        ";
            sqlstr += " else 'Y' end                                    ";
            sqlstr += " ) type1,                                        ";
            sqlstr += "                                                 ";
            sqlstr += " case when month_before is not null then s1 else null end  d_st_date,              ";
            sqlstr += " case when month_before is not null then month_before else null end  month_before, ";
            sqlstr += " case when A2 is null and A3 is null then A1 else null end A1,                     ";
            sqlstr += " case when A3 is null then A2 else null end A2,                                    ";
            sqlstr += " A3,                                                                               ";
            sqlstr += " case when A5 is null and A6 is null then A4 else null end A4,                     ";
            sqlstr += " case when A6 is null then A5 else null end A5,                                    ";
            sqlstr += " A6,                                                                               ";
            sqlstr += "                                                                                   ";
            sqlstr += "                                                                                   ";
            sqlstr += " case when month_after is not null then  e2 else null end  ed_st_date,             ";
            sqlstr += " case when month_after is not null then month_after else null end  month_after     ";
            sqlstr += " from           ";
            sqlstr += " (              ";
            sqlstr += "                ";
            sqlstr += "                ";
            sqlstr += "                ";
            sqlstr += " select         ";
            sqlstr += "                ";
            sqlstr += " distinct       ";
            sqlstr += " " + st_date + "  s1,        ";
            sqlstr += " " + end_date + "  e2,        ";
            sqlstr += " (case when cm_st_date<= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_end_date else null end  ) month_before,  ";
            sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_st_date else null end  ) A1,             ";
            sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + "  then cq_st_date else null end  ) A2,             ";
            sqlstr += " (case when cy_st_date>= " + st_date + "  and cy_end_date <=" + end_date + "  then cy_st_date else null end  ) A3,             ";
            sqlstr += "                                                                                                    ";
            sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_end_date else null end  ) A4,            ";
            sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + "  then cq_end_date else null end ) A5,             ";
            sqlstr += " (case when cy_st_date>= " + st_date + "  and cy_end_date <=" + end_date + "  then cy_end_date else null end  ) A6,            ";
            sqlstr += " (case when cm_st_date<= " + end_date + "  and cm_end_date >=" + end_date + "  then cm_st_date else null end  ) month_After     ";
            sqlstr += "                                        ";
            sqlstr += "                                        ";
            sqlstr += " from pr_day_denom                      ";
            sqlstr += " where ddate between (" + st_date + " ) and (" + end_date + " )    ";
            sqlstr += " ) QA1 ) QA2                            ";
            sqlstr += "                         ";
        } else {
            if (Timelevel.equalsIgnoreCase("Day") || Timelevel.equalsIgnoreCase("Week")) {
                sqlstr = null;
            } else if (Timelevel.equalsIgnoreCase("Month") || Timelevel.equalsIgnoreCase("Mon")) {
                sqlstr += " select     distinct                          ";
                sqlstr += " case                                 ";
                sqlstr += " when type1 ='DB' then 'D'            ";
                sqlstr += " when type1 ='DA' then 'D'            ";
                sqlstr += " else type1 end time_summ_flag ,      ";
                sqlstr += " case                                 ";
                sqlstr += " when type1 ='DB' then d_st_date      ";
                sqlstr += " when type1 ='DA' then month_after    ";
                sqlstr += " else A1 end T001_st_date,                 ";
                sqlstr += " case                                 ";
                sqlstr += " when type1 ='DB' then month_before   ";
                sqlstr += " when type1 ='DA' then ed_st_date     ";
                sqlstr += " else A4  end T001_end_date                ";
                sqlstr += "                                      ";
                sqlstr += "                                      ";
                sqlstr += " from (                               ";
                sqlstr += " select                               ";
                sqlstr += " distinct                             ";
                sqlstr += " (case when month_before is not null and A1 is null then 'DB'        ";
                sqlstr += " when month_after is not null and A1 is null then 'DA'               ";
                sqlstr += " else 'M' end                                                        ";
                sqlstr += " ) type1,                                                            ";
                sqlstr += "                                                                     ";
                sqlstr += "                                                                     ";
                sqlstr += " case when month_before is not null then s1 else null end  d_st_date, ";
                sqlstr += " case when month_before is not null then month_before else null end  month_before,  ";
                sqlstr += " A1,                                                                                ";
                sqlstr += " A4,                                                                                ";
                sqlstr += "                                                                                    ";
                sqlstr += "                                                                                    ";
                sqlstr += " case when month_after is not null then  e2 else null end  ed_st_date,              ";
                sqlstr += " case when month_after is not null then month_after else null end  month_after      ";
                sqlstr += " from                                                                               ";
                sqlstr += " (                                                                                  ";
                sqlstr += "                                                                                    ";
                sqlstr += "                                                                                    ";
                sqlstr += "                                                                                    ";
                sqlstr += " select                                                                             ";
                sqlstr += "                                                                                    ";
                sqlstr += " distinct                                                                           ";
                sqlstr += " " + st_date + "  s1,                                                     ";
                sqlstr += " " + end_date + " e2,                                                     ";
                sqlstr += " (case when cm_st_date<= " + st_date + "  and cm_end_date <=" + end_date + " then cm_end_date else null end  ) month_before, ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + " then cm_st_date else null end  ) A1,            ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + " then cm_end_date else null end  ) A4,           ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += " (case when cm_st_date<= " + end_date + " and cm_end_date >=" + end_date + " then cm_st_date else null end  ) month_After    ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += " from pr_day_denom                                                                                 ";
                sqlstr += " where ddate between (" + st_date + " ) and (" + end_date + " )                                                                 ";
                sqlstr += " ) QA1 ) QA2                                                                                       ";
            } else if (Timelevel.equalsIgnoreCase("QUARTER") || Timelevel.equalsIgnoreCase("QTR")) {
                sqlstr += " select   distinct                             ";
                sqlstr += " case                                  ";
                sqlstr += " when type1 ='DB' then 'D'             ";
                sqlstr += " when type1 ='DA' then 'D'             ";
                sqlstr += " else type1 end time_summ_flag ,       ";
                sqlstr += " case                                  ";
                sqlstr += " when type1 ='DB' then d_st_date       ";
                sqlstr += " when type1 ='DA' then month_after     ";
                sqlstr += " when type1 ='M' then A1               ";
                sqlstr += " else A2 end T001_st_date,                  ";
                sqlstr += " case                                  ";
                sqlstr += " when type1 ='DB' then month_before    ";
                sqlstr += " when type1 ='DA' then ed_st_date      ";
                sqlstr += " when type1 ='M' then A4               ";
                sqlstr += " else A5  end T001_end_date                 ";
                sqlstr += "                                       ";
                sqlstr += "                                       ";
                sqlstr += " from (                                ";
                sqlstr += " select                                ";
                sqlstr += " distinct                              ";
                sqlstr += " (case when month_before is not null and A1 is null then 'DB' ";
                sqlstr += " when month_after is not null and A1 is null then 'DA'        ";
                sqlstr += " when A2 is null  then 'M'                                    ";
                sqlstr += " else 'Q' end                                                 ";
                sqlstr += " ) type1,                                                     ";
                sqlstr += "                                                              ";
                sqlstr += "                                                              ";
                sqlstr += " case when month_before is not null then s1 else null end  d_st_date,               ";
                sqlstr += " case when month_before is not null then month_before else null end  month_before,  ";
                sqlstr += " case when A2 is null  then A1 else null end A1,                                    ";
                sqlstr += " A2,                                                                                ";
                sqlstr += " case when A5 is null  then A4 else null end A4,                                    ";
                sqlstr += " A5,                                                                                ";
                sqlstr += "                                                                                    ";
                sqlstr += "                                                                                    ";
                sqlstr += " case when month_after is not null then  e2 else null end  ed_st_date,              ";
                sqlstr += " case when month_after is not null then month_after else null end  month_after      ";
                sqlstr += " from           ";
                sqlstr += " (              ";
                sqlstr += "                ";
                sqlstr += "                ";
                sqlstr += "                ";
                sqlstr += " select         ";
                sqlstr += "                ";
                sqlstr += " distinct       ";
                sqlstr += " " + st_date + "  s1,        ";
                sqlstr += " " + end_date + " e2,        ";
                sqlstr += " (case when cm_st_date<= " + st_date + "  and cm_end_date <=" + end_date + " then cm_end_date else null end  ) month_before, ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + " then cm_st_date else null end  ) A1,            ";
                sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + " then cq_st_date else null end  ) A2,            ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + " then cm_end_date else null end  ) A4,           ";
                sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + " then cq_end_date else null end ) A5,            ";
                sqlstr += "                                                                                                   ";
                sqlstr += " (case when cm_st_date<= " + end_date + " and cm_end_date >=" + end_date + " then cm_st_date else null end  ) month_After    ";
                sqlstr += "                                                                                                   ";
                sqlstr += "                                                                                                   ";
                sqlstr += " from pr_day_denom                                                                                 ";
                sqlstr += " where ddate between (" + st_date + " ) and (" + end_date + " )                                                                ";
                sqlstr += " ) QA1 ) QA2                                                                                       ";
                sqlstr += "                                                                                      ";

            } else if (Timelevel.equalsIgnoreCase("Year") || Timelevel.equalsIgnoreCase("Yr")) {
                sqlstr += " select   distinct                                       ";
                sqlstr += " case                                            ";
                sqlstr += " when type1 ='DB' then 'D'                       ";
                sqlstr += " when type1 ='DA' then 'D'                       ";
                sqlstr += " when type1 ='DA' then 'D'                       ";
                sqlstr += " else type1 end time_summ_flag ,                 ";
                sqlstr += " case                                            ";
                sqlstr += " when type1 ='DB' then d_st_date                 ";
                sqlstr += " when type1 ='DA' then month_after               ";
                sqlstr += " when type1 ='M' then A1                         ";
                sqlstr += " when type1 ='Q' then A2                         ";
                sqlstr += " else A3 end T001_st_date,                            ";
                sqlstr += " case                                            ";
                sqlstr += " when type1 ='DB' then month_before              ";
                sqlstr += " when type1 ='DA' then ed_st_date                ";
                sqlstr += " when type1 ='M' then A4                         ";
                sqlstr += " when type1 ='Q' then A5                         ";
                sqlstr += " else A6  end T001_end_date                           ";
                sqlstr += "                                                 ";
                sqlstr += "                                                 ";
                sqlstr += " from (                                          ";
                sqlstr += " select                                          ";
                sqlstr += " distinct                                        ";
                sqlstr += " (case when month_before is not null and A1 is null  then 'DB'   ";
                sqlstr += " when month_after is not null and A1 is null then 'DA'          ";
                sqlstr += " when A2 is null and A3 is null then 'M'         ";
                sqlstr += " when A3 is null then 'Q'                        ";
                sqlstr += " else 'Y' end                                    ";
                sqlstr += " ) type1,                                        ";
                sqlstr += "                                                 ";
                sqlstr += " case when month_before is not null then s1 else null end  d_st_date,              ";
                sqlstr += " case when month_before is not null then month_before else null end  month_before, ";
                sqlstr += " case when A2 is null and A3 is null then A1 else null end A1,                     ";
                sqlstr += " case when A3 is null then A2 else null end A2,                                    ";
                sqlstr += " A3,                                                                               ";
                sqlstr += " case when A5 is null and A6 is null then A4 else null end A4,                     ";
                sqlstr += " case when A6 is null then A5 else null end A5,                                    ";
                sqlstr += " A6,                                                                               ";
                sqlstr += "                                                                                   ";
                sqlstr += "                                                                                   ";
                sqlstr += " case when month_after is not null then  e2 else null end  ed_st_date,             ";
                sqlstr += " case when month_after is not null then month_after else null end  month_after     ";
                sqlstr += " from           ";
                sqlstr += " (              ";
                sqlstr += "                ";
                sqlstr += "                ";
                sqlstr += "                ";
                sqlstr += " select         ";
                sqlstr += "                ";
                sqlstr += " distinct       ";
                sqlstr += " " + st_date + "  s1,        ";
                sqlstr += " " + end_date + "  e2,        ";
                sqlstr += " (case when cm_st_date<= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_end_date else null end  ) month_before,  ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_st_date else null end  ) A1,             ";
                sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + "  then cq_st_date else null end  ) A2,             ";
                sqlstr += " (case when cy_st_date>= " + st_date + "  and cy_end_date <=" + end_date + "  then cy_st_date else null end  ) A3,             ";
                sqlstr += "                                                                                                    ";
                sqlstr += " (case when cm_st_date>= " + st_date + "  and cm_end_date <=" + end_date + "  then cm_end_date else null end  ) A4,            ";
                sqlstr += " (case when cq_st_date>= " + st_date + "  and cq_end_date <=" + end_date + "  then cq_end_date else null end ) A5,             ";
                sqlstr += " (case when cy_st_date>= " + st_date + "  and cy_end_date <=" + end_date + "  then cy_end_date else null end  ) A6,            ";
                sqlstr += " (case when cm_st_date<= " + end_date + "  and cm_end_date >=" + end_date + "  then cm_st_date else null end  ) month_After     ";
                sqlstr += "                                        ";
                sqlstr += "                                        ";
                sqlstr += " from pr_day_denom                      ";
                sqlstr += " where ddate between (" + st_date + " ) and (" + end_date + " )    ";
                sqlstr += " ) QA1 ) QA2                            ";
                sqlstr += "                            ";

            }
        }

        return (sqlstr);
    }

    public void setTargetRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        String startC = "";
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null;
        String dateType = null, dateFormat = null, onlyDate = null, dateConversion = null;

        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        GetConnectionType gettypeofconn = new GetConnectionType();
        connType = gettypeofconn.getConTypeByElementId(elementID);
        if (connType == null) {
            return;
        }
        ////.println("conntype is" + connType);
        if (connType.equalsIgnoreCase("PostgreSQL")) {
            interval = "";
        } else if (connType.equalsIgnoreCase("Mysql")) {
            dateType = "date_format";
            dateConversion = "str_to_date";
            dateFormat = "%m/%d/%Y %H:%i:%s";
            interval = "+.99999";
            onlyDate = "%m/%d/%Y";
        } else {
            interval = "+.99999";
        }

        if (connType.equalsIgnoreCase("Mysql")) {
            try {
                str2 = "select " + dateType + "(end_date ,'" + dateFormat + " '), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(date_sub((end_date),interval 1 day),'" + dateFormat + " ') , " + dateType + "(date_sub((ST_DATE),interval 1 day) ,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
//                        modified by Nazneen
//                        str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY    ,'" + dateFormat + " ') , " + dateType + "(LYW_DAY -1 ,'" + dateFormat + " ') ";
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY    ,'" + dateFormat + " ') , " + dateType + "(DATE_SUB(LYW_DAY,INTERVAL 1 DAY) ,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY    ,'" + dateFormat + " ') , " + dateType + "(DATE_SUB(LYW_DAY,INTERVAL 1 DAY) ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY    ,'" + dateFormat + " ') , " + dateType + "(DATE_SUB(LYW_DAY,INTERVAL 1 DAY)  ,'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PW_DAY  ,'" + dateFormat + " ') , " + dateType + "(PW_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY   ,'" + dateFormat + " ') , " + dateType + "(LYW_ST_DATE,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PW_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PW_ST_DATE ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LYW_ST_DATE  ,'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        startC = "T_MONTH=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ')  , " + dateType + "(PM_DAY  ,'" + dateFormat + " ') , " + dateType + "(PM_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PYM_DAY  ,'" + dateFormat + " ') ,  " + dateType + "(PYM_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PM_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PM_ST_DATE  ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PYM_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PYM_ST_DATE  ,'" + dateFormat + " ') ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        startC = "T_QTR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') ,  " + dateType + "(LQ_DAY  ,'" + dateFormat + " ') , " + dateType + "(LQ_ST_DATE,'" + dateFormat + " '),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_DAY   ,'" + dateFormat + " '),  " + dateType + "(LYQ_ST_DATE,'" + dateFormat + " '),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LQ_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LQ_ST_DATE  ,'" + dateFormat + " '),cqtr,cm_year ";
                        } else {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LYQ_ST_DATE  ,'" + dateFormat + " '),cqtr,cm_year ";
                        }

                    } else {
                        startC = "T_YEAR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + onlyDate + "') , " + dateType + "(LY_DAY  ,'" + onlyDate + "') , " + dateType + "(LY_ST_DATE,'" + onlyDate + " ')   ";
                        } else {
                            str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + onlyDate + " ') , " + dateType + "(LY_END_DATE  ,'" + onlyDate + " ') , " + dateType + "(LY_ST_DATE,'" + onlyDate + " ')   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = " + dateConversion + "('" + d1 + "','" + onlyDate + "') ";

                con = getConnection(elementID);

                if (connType.equalsIgnoreCase("mysql")) {
                    if (str2.contains("mm/dd/yyyy")) {
                        str2 = str2.replaceAll("mm/dd/yyyy", "%m/%d/%Y");
                    }
                }

                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);

                    if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        qtr_tar_clu = rs2.getString(5);
                        qtr_tar_clu2 = rs2.getString(6);
                    }


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                logger.error("Exception:", e);
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            if (PRG_PERIOD_TYPE.equalsIgnoreCase("YEAR")) {
                d_tar_clu = startC + " (select " + dateType + "(to_date('" + st_d + "','mm-dd-yyyy'),'yyyy') from dual)";
                p_tar_clu = startC + " (select " + dateType + "(to_date('" + p_st_d + "','mm-dd-yyyy'),'yyyy') from dual)";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                d_tar_clu = startC + " (select " + dateType + "(to_date('" + st_d + "','mm-dd-yyyy'),'MON-yy') from dual)";
                p_tar_clu = startC + " (select " + dateType + "(to_date('" + p_st_d + "','mm-dd-yyyy'),'MON-yy') from dual)";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("Quarter")) {

                d_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
                p_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
            } else {
                //d_tar_clu = startC+" =(select "+dateType+"(to_date('"+st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                // p_tar_clu = startC+" =(select "+dateType+"(to_date('"+p_st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                d_tar_clu = "T_DATE between  " + dateConversion + "('" + st_d + "','" + dateFormat + " ') and  " + dateConversion + "('" + ed_d + "','" + dateFormat + " ') ";
                p_tar_clu = "T_DATE between  " + dateConversion + "('" + p_st_d + "','" + dateFormat + " ') and  " + dateConversion + "('" + p_ed_d + "','" + dateFormat + " ') ";
            }
        } else if (connType.equalsIgnoreCase("SqlServer")) {

            try {


                str2 = "select convert(varchar,end_date ,120)  A0, ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,end_date -1,120) A2 , convert(varchar,ST_DATE -1 ,120) A3  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LYW_DAY    ,120)  A2 , convert(varchar,LYW_DAY -1 ,120)  A3 ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LYW_DAY    ,120)  A2 , convert(varchar,LYW_DAY -1 ,120)  A3 ";
                        } else {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LYW_DAY    ,120)  A2 , convert(varchar,LYW_DAY -1 ,120)  A3 ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,PW_DAY  ,120) A3 , convert(varchar,PW_ST_DATE,120) A4  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,LYW_DAY   ,120) A3 , convert(varchar,LYW_ST_DATE,120) A4 ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,PW_END_DATE   ,120)  A3  , convert(varchar,PW_ST_DATE ,120)  A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,LYW_END_DATE   ,120)  A3  , convert(varchar,LYW_ST_DATE  ,120) A4 ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        startC = "T_MONTH=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) A2  , convert(varchar,PM_DAY " + interval + ",120) A3 , convert(varchar,PM_ST_DATE,120)  A4 ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) A2 , convert(varchar,PYM_DAY " + interval + " ,120) A3 ,  convert(varchar,PYM_ST_DATE,120) A4  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) A2 , convert(varchar,PM_END_DATE   ,120) A3 , convert(varchar,PM_ST_DATE  ,120) A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) A2 , convert(varchar,PYM_END_DATE   ,120) A3 , convert(varchar,PYM_ST_DATE  ,120) A4 ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        startC = "T_QTR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) ,  convert(varchar,LQ_DAY  ,120) , convert(varchar,LQ_ST_DATE,120),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LYQ_DAY   ,120),  convert(varchar,LYQ_ST_DATE,120),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LQ_END_DATE   ,120) , convert(varchar,LQ_ST_DATE  ,120),cqtr,cm_year ";
                        } else {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LYQ_END_DATE   ,120) , convert(varchar,LYQ_ST_DATE  ,120),cqtr,cm_year ";
                        }

                    } else {
                        startC = "T_YEAR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " convert(varchar,CY_ST_DATE,120) , convert(varchar,LY_DAY  ,120) , convert(varchar,LY_ST_DATE,120)   ";
                        } else {
                            str2 = str2 + " convert(varchar,CY_ST_DATE,120) , convert(varchar,LY_END_DATE  ,120) , convert(varchar,LY_ST_DATE,120)   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";

                con = getConnection(elementID);
                ////.println("str2 "+ str2);
                st = con.createStatement();
                rs2 = st.executeQuery(str2);
                //st2 = con.prepareCall(str2);;

                //rs2 = st2.executeQuery();

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);

                    if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        qtr_tar_clu = rs2.getString(5);
                        qtr_tar_clu2 = rs2.getString(6);
                    }


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                logger.error("Exception:", e);
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (st != null) {
                    st.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            if (PRG_PERIOD_TYPE.equalsIgnoreCase("YEAR")) {
                d_tar_clu = startC + " (select distinct CY_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + st_d + "',120)) )";
                p_tar_clu = startC + " (select distinct LY_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + p_st_d + "',120)) )";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                d_tar_clu = startC + " (select distinct CM_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + st_d + "',120)) )";
                p_tar_clu = startC + " (select distinct PM_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + p_st_d + "',120)) )";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("Quarter")) {
                d_tar_clu = startC + " (select distinct CQ_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + st_d + "',120)) )";
                p_tar_clu = startC + " (select distinct LQ_CUST_NAME from pr_day_denom  where ddate = (convert(datetime,'" + p_st_d + "',120)) )";
//
//                d_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
//                p_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
            } else {
                //d_tar_clu = startC+" =(select convert(varchar,convert(datetime,'"+st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                // p_tar_clu = startC+" =(select convert(varchar,convert(datetime,'"+p_st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                d_tar_clu = "T_DATE between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
                p_tar_clu = "T_DATE between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) ";

            }
        } else {
            try {

                str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date -1,'mm/dd/yyyy hh24:mi:ss ') , to_char(ST_DATE -1 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -1 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -1 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -1 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        startC = "T_MONTH=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy')  , to_char(PM_DAY " + interval + ",'mm/dd/yyyy') , to_char(PM_ST_DATE,'mm/dd/yyyy')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PYM_DAY " + interval + " ,'mm/dd/yyyy') ,  to_char(PYM_ST_DATE,'mm/dd/yyyy')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PM_END_DATE   ,'mm/dd/yyyy') , to_char(PM_ST_DATE  ,'mm/dd/yyyy') ";
                        } else {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PYM_END_DATE   ,'mm/dd/yyyy') , to_char(PYM_ST_DATE  ,'mm/dd/yyyy') ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        startC = "T_QTR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(LQ_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                        } else {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                        }

                    } else {
                        startC = "T_YEAR=";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy') , to_char(LY_DAY  ,'mm/dd/yyyy') , to_char(LY_ST_DATE,'mm/dd/yyyy ')   ";
                        } else {
                            str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy ') , to_char(LY_END_DATE  ,'mm/dd/yyyy ') , to_char(LY_ST_DATE,'mm/dd/yyyy ')   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";

                con = getConnection(elementID);

                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);

                    if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        qtr_tar_clu = rs2.getString(5);
                        qtr_tar_clu2 = rs2.getString(6);
                    }


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                logger.error("Exception:", e);
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            if (PRG_PERIOD_TYPE.equalsIgnoreCase("YEAR")) {
                d_tar_clu = startC + " (select to_char(to_date('" + st_d + "','mm-dd-yyyy'),'yyyy') from dual)";
                p_tar_clu = startC + " (select to_char(to_date('" + p_st_d + "','mm-dd-yyyy'),'yyyy') from dual)";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                d_tar_clu = startC + " (select to_char(to_date('" + st_d + "','mm-dd-yyyy'),'MON-yy') from dual)";
                p_tar_clu = startC + " (select to_char(to_date('" + p_st_d + "','mm-dd-yyyy'),'MON-yy') from dual)";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("Quarter")) {

                d_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
                p_tar_clu = startC + " '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
            } else {
                //d_tar_clu = startC+" =(select to_char(to_date('"+st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                // p_tar_clu = startC+" =(select to_char(to_date('"+p_st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                d_tar_clu = "T_DATE between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                p_tar_clu = "T_DATE between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";

            }


        }




    }
    //added by susheela 04-12-09 over

    public void setTargetTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        ////.println("Target trend is Called ................");
        Connection con = null;
        CallableStatement st2 = null;
        String startC = "";
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null;

        GetConnectionType conectiontype = new GetConnectionType();
        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        if (connType == null) {
            return;
        }
        if (connType.equalsIgnoreCase("Sqlserver")) {
            try {
                str2 = "select convert(varchar,end_date ,120), ";



                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " convert(varchar,ST_DATE -15 ,120)  , convert(varchar,LYW_DAY   ,120) , convert(varchar,LYW_DAY -15 ,120)  ";
                        } else {
                            str2 = str2 + " convert(varchar,ST_DATE -15 ,120)  , convert(varchar,LYW_DAY   ,120) , convert(varchar,LYW_DAY -15 ,120)  ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) , convert(varchar,LYW_DAY   ,120) , convert(varchar,LYW_ST_DATE -91,120) ";
                        } else {
                            str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) , convert(varchar,LYW_END_DATE   ,120) , convert(varchar,LYW_ST_DATE -91,120) ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE -365,120) , convert(varchar,PYM_DAY +.99999 ,120) ,  convert(varchar,PYM_ST_DATE -365,120)  ";
                        } else {
                            str2 = str2 + " convert(varchar,CM_ST_DATE -365,120) , convert(varchar,PYM_END_DATE +.99999 ,120) ,  convert(varchar,PYM_ST_DATE -365,120)  ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) , convert(varchar,LYQ_DAY   ,120),  convert(varchar,LYQ_ST_DATE-730,120) ";
                        } else {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) , convert(varchar,LYQ_END_DATE   ,120),  convert(varchar,LYQ_ST_DATE-730,120) ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) , convert(varchar,LY_DAY  ,120) , convert(varchar,LY_ST_DATE -1460,120)   ";
                        } else {
                            str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) , convert(varchar,LY_END_DATE  ,120) , convert(varchar,LY_ST_DATE -1460,120)   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',110) ";

                con = getConnection(elementID);

                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);

                    if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        qtr_tar_clu = rs2.getString(5);
                        qtr_tar_clu2 = rs2.getString(6);
                    }


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (st != null) {
                    st.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            if (PRG_PERIOD_TYPE.equalsIgnoreCase("YEAR")) {
                d_tar_clu = "T_YEAR = PROGEN_TIME.CY_CUST_NAME AND PROGEN_TIME.ddate = CY_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + st_d + "',120) and convert(datetime,'" + ed_d + "',120) ";
                p_tar_clu = "T_YEAR = PROGEN_TIME.LY_CUST_NAME AND PROGEN_TIME.ddate = LY_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + p_st_d + "',120) and convert(datetime,'" + p_ed_d + "',120) ";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                d_tar_clu = "T_MONTH = CM_CUST_NAME AND PROGEN_TIME.ddate = CM_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + st_d + "',120) and convert(datetime,'" + ed_d + "',120) ";
                p_tar_clu = "T_MONTH = PM_CUST_NAME AND PROGEN_TIME.ddate = PM_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + p_st_d + "',120) and convert(datetime,'" + p_ed_d + "',120) ";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("Quarter")) {

                d_tar_clu = "T_QTR = CQ_CUST_NAME AND PROGEN_TIME.ddate = CQ_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + st_d + "',120) and convert(datetime,'" + ed_d + "',120) ";
                p_tar_clu = "T_QTR = LQ_CUST_NAME AND PROGEN_TIME.ddate = LQ_ST_DATE and PROGEN_TIME.ddate between convert(datetime,'" + p_st_d + "',120) and convert(datetime,'" + p_ed_d + "',120) ";


            } else {
                //d_tar_clu = startC+" =(select convert(varchar,convert(datetime,'"+st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                // p_tar_clu = startC+" =(select convert(varchar,convert(datetime,'"+p_st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                d_tar_clu = "T_DATE between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
                p_tar_clu = "T_DATE between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) ";

            }

        } else {
            try {
                str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                /*
                 * {
                 * if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) { if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")) { str2 = str2 +
                 * " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(end_date -1,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(ST_DATE -1 ,'mm/dd/yyyy hh24:mi:ss ') "; } else if
                 * (PRG_COMPARE.equalsIgnoreCase("YEAR") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) { str2 = str2 + "
                 * to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY
                 * ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -1 ,'mm/dd/yyyy
                 * hh24:mi:ss ') "; } else if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) { str2
                 * = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LYW_DAY ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY
                 * -1 ,'mm/dd/yyyy hh24:mi:ss ') "; } else { str2 = str2 + "
                 * to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY
                 * ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -1 ,'mm/dd/yyyy
                 * hh24:mi:ss ') "; }
                 *
                 * } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) { if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")) { str2 = str2 +
                 * " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(PW_DAY ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') "; } else if
                 * (PRG_COMPARE.equalsIgnoreCase("YEAR") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) { str2 = str2 + "
                 * to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LYW_DAY ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LYW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') "; } else if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) { str2
                 * = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(PW_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(PW_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') "; } else {
                 * str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')
                 * , to_char(LYW_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LYW_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') "; }
                 *
                 * } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                 * startC = "T_MONTH="; if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")) { str2 = str2 +
                 * " to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PM_DAY
                 * +.99999,'mm/dd/yyyy') , to_char(PM_ST_DATE,'mm/dd/yyyy') "; }
                 * else if (PRG_COMPARE.equalsIgnoreCase("YEAR") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) { str2 = str2 + "
                 * to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PYM_DAY +.99999
                 * ,'mm/dd/yyyy') , to_char(PYM_ST_DATE,'mm/dd/yyyy') "; } else
                 * if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) { str2
                 * = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy') ,
                 * to_char(PM_END_DATE ,'mm/dd/yyyy') , to_char(PM_ST_DATE
                 * ,'mm/dd/yyyy') "; } else { str2 = str2 + "
                 * to_char(CM_ST_DATE,'mm/dd/yyyy') , to_char(PYM_END_DATE
                 * ,'mm/dd/yyyy') , to_char(PYM_ST_DATE ,'mm/dd/yyyy') "; }
                 *
                 *
                 * } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") ||
                 * PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) { startC =
                 * "T_QTR="; if (PRG_COMPARE.equalsIgnoreCase("PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")) { str2 = str2 +
                 * " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LQ_DAY ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                 * } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) { str2 = str2 + "
                 * to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LYQ_DAY ,'mm/dd/yyyy hh24:mi:ss '),
                 * to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                 * } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) { str2
                 * = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LQ_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') ,
                 * to_char(LQ_ST_DATE ,'mm/dd/yyyy hh24:mi:ss '),cqtr,cm_year ";
                 * } else { str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy
                 * hh24:mi:ss ') , to_char(LYQ_END_DATE ,'mm/dd/yyyy hh24:mi:ss
                 * ') , to_char(LYQ_ST_DATE ,'mm/dd/yyyy hh24:mi:ss
                 * '),cqtr,cm_year "; }
                 *
                 * } else { startC = "T_YEAR="; if
                 * (PRG_COMPARE.equalsIgnoreCase("PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") ||
                 * PRG_COMPARE.equalsIgnoreCase("YEAR") ||
                 * PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) { str2 = str2 + "
                 * to_char(CY_ST_DATE,'mm/dd/yyyy') , to_char(LY_DAY
                 * ,'mm/dd/yyyy') , to_char(LY_ST_DATE,'mm/dd/yyyy ') "; } else
                 * { str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy ') ,
                 * to_char(LY_END_DATE ,'mm/dd/yyyy ') ,
                 * to_char(LY_ST_DATE,'mm/dd/yyyy ') "; } }
            }
                 */

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " to_char(ST_DATE -15 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -15 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else {
                            str2 = str2 + " to_char(ST_DATE -15 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -15 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_DAY +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else {
                            str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                        } else {
                            str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";

                con = getConnection(elementID);

                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);

                    if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        qtr_tar_clu = rs2.getString(5);
                        qtr_tar_clu2 = rs2.getString(6);
                    }


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (st != null) {
                    st.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            if (PRG_PERIOD_TYPE.equalsIgnoreCase("YEAR")) {
                d_tar_clu = "T_YEAR = PROGEN_TIME.CY_CUST_NAME AND PROGEN_TIME.ddate = to_date(PROGEN_TIME.CY_CUST_NAME,'YYYY') and PROGEN_TIME.ddate between to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                p_tar_clu = "T_YEAR = PROGEN_TIME.LY_CUST_NAME AND PROGEN_TIME.ddate = to_date(PROGEN_TIME.LY_CUST_NAME,'YYYY') and PROGEN_TIME.ddate between to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                d_tar_clu = "T_MONTH = to_char(to_date(PROGEN_TIME.CM_CUST_NAME,'MON-YYYY'),'MON-YY') AND PROGEN_TIME.ddate = to_date(PROGEN_TIME.CM_CUST_NAME,'MON-YYYY') and PROGEN_TIME.ddate between to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                p_tar_clu = "T_MONTH = to_char(to_date(PROGEN_TIME.PM_CUST_NAME,'MON-YYYY'),'MON-YY') AND PROGEN_TIME.ddate = to_date(PROGEN_TIME.PM_CUST_NAME,'MON-YYYY') and PROGEN_TIME.ddate between to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("Quarter")) {

                d_tar_clu = "T_QTR = '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
                p_tar_clu = "T_QTR = '" + qtr_tar_clu + "-" + qtr_tar_clu2 + "'";
            } else {
                //d_tar_clu = startC+" =(select to_char(to_date('"+st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                // p_tar_clu = startC+" =(select to_char(to_date('"+p_st_d+"','mm-dd-yyyy'),'mm-yyyy') from dual)";
                d_tar_clu = "T_DATE between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                p_tar_clu = "T_DATE between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";

            }



        }


    }

    //added by susheela 04-12-09 over
    public void setRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null;
        String dateType = null;
        String dateFormat = null;
        String dateConversion = null;

        GetConnectionType conectiontype = new GetConnectionType();
        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        try {
            GetConnectionType gettypeofconn = new GetConnectionType();
            if (connType == null || connType.equals("")) {
                if (elementID != null) {
                    connType = gettypeofconn.getConTypeByElementId(elementID);
                } else if (connId != null) {
                    connType = gettypeofconn.getConTypeByConnID(connId);
                }
            }


            ////.println("conntype is" + connType);
            if (connType.equalsIgnoreCase("PostgreSQL")) {
                interval = "";
            } else if (connType.equalsIgnoreCase("mysql")) {
                dateType = "date_format";
                dateConversion = "date_format";
                dateFormat = "%m/%d/%Y %H:%i:%s";
                interval = "+.99999";
            } else {
                interval = "+.99999";
            }
            if (connType.equalsIgnoreCase("Mysql")) {


                str2 = "select " + dateType + "(end_date ,'" + dateFormat + "'), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                                || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(date_sub(end_date, INTERVAL 1 day),'" + dateFormat + "') , " + dateType + "(date_sub(ST_DATE, INTERVAL 1 day) ,'" + dateFormat + "')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("WEEK") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(PW_DAY    ,'" + dateFormat + " ') , " + dateType + "(PW_DAY    ,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(PM_DAY    ,'" + dateFormat + " ') , " + dateType + "(PM_DAY    ,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LQ_DAY    ,'" + dateFormat + " ') , " + dateType + "(LQ_DAY    ,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_DAY    ,'" + dateFormat + " ') , " + dateType + "(LY_DAY    ,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) {
                            str2 = str2 + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(date_sub(end_date, INTERVAL 1 day),'" + dateFormat + "') , " + dateType + "(date_sub(ST_DATE, INTERVAL 1 day) ,'" + dateFormat + "')  ";
                        } else {
                            str2 = str2 + " " + dateType + "(ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_DAY    ,'" + dateFormat + " ') , " + dateType + "(LY_DAY    ,'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase(" LAST WEEK")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PW_DAY  ,'" + dateFormat + " ') , " + dateType + "(PW_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_DAY   ,'" + dateFormat + " ') , " + dateType + "(LYW_ST_DATE,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PW_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PW_ST_DATE ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(CW_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYW_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LYW_ST_DATE  ,'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ')  , " + dateType + "(PM_DAY  ,'" + dateFormat + " ') , " + dateType + "(PM_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PYM_DAY  ,'" + dateFormat + " ') ,  " + dateType + "(PYM_ST_DATE,'" + dateFormat + " ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PM_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PM_ST_DATE  ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(CM_ST_DATE,'" + dateFormat + " ') , " + dateType + "(PYM_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(PYM_ST_DATE  ,'" + dateFormat + " ') ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') ,  " + dateType + "(LQ_DAY  ,'" + dateFormat + " ') , " + dateType + "(LQ_ST_DATE,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_DAY   ,'" + dateFormat + " '),  " + dateType + "(LYQ_ST_DATE,'" + dateFormat + " ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LQ_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LQ_ST_DATE  ,'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_END_DATE   ,'" + dateFormat + " ') , " + dateType + "(LYQ_ST_DATE  ,'" + dateFormat + " ') ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_DAY  ,'" + dateFormat + " ') , " + dateType + "(LY_ST_DATE,'" + dateFormat + " ')   ";
                        } else {
                            str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_END_DATE  ,'" + dateFormat + " ') , " + dateType + "(LY_ST_DATE,'" + dateFormat + " ')   ";
                        }
                    }
                }

                {
                    timeStamp = "str_to_date";
                    str2 = str2 + " from   " + timeTableName.toLowerCase() + "  " + timeTableName.toLowerCase() + " where ddate = " + timeStamp + "('" + d1 + "','%m/%d/%Y'); ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);
                }



            } else if (connType.equalsIgnoreCase("SqlServer")) {

                str2 = "select convert(varchar,end_date ,120)  A0, ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                                || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,end_date-1 ,120) , convert(varchar,ST_DATE -1 ,120) A3  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,PW_DAY ,120) , convert(varchar,PW_DAY  ,120) A3  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,PM_DAY ,120) , convert(varchar,PM_DAY ,120) A3  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LQ_DAY  ,120) , convert(varchar,LQ_DAY ,120) A3  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LY_DAY    ,120)  A2 , convert(varchar,LY_DAY  ,120)  A3 ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,end_date-1 ,120) , convert(varchar,ST_DATE -1 ,120) A3  ";
                        } else {
                            str2 = str2 + " convert(varchar,ST_DATE,120)  A1, convert(varchar,LY_DAY     ,120)  A2 , convert(varchar,LY_DAY -1  ,120) ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,PW_DAY  ,120) A3 , convert(varchar,PW_ST_DATE,120) A4  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,LYW_DAY   ,120) A3 , convert(varchar,LYW_ST_DATE,120) A4 ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,PW_END_DATE   ,120)  A3 , convert(varchar,PW_ST_DATE ,120)  A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,CW_ST_DATE,120) A2 , convert(varchar,LYW_END_DATE   ,120)  A3 , convert(varchar,LYW_ST_DATE  ,120) A4 ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120)  , convert(varchar,PM_DAY " + interval + ",120) , convert(varchar,PM_ST_DATE,120)  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) , convert(varchar,PYM_DAY " + interval + ",120) ,  convert(varchar,PYM_ST_DATE,120)  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) , convert(varchar,PM_END_DATE   ,120) , convert(varchar,PM_ST_DATE  ,120) ";
                        } else {
                            str2 = str2 + " convert(varchar,CM_ST_DATE,120) , convert(varchar,PYM_END_DATE   ,120) , convert(varchar,PYM_ST_DATE  ,120) ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) ,  convert(varchar,LQ_DAY  ,120) , convert(varchar,LQ_ST_DATE,120) ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LYQ_DAY   ,120),  convert(varchar,LYQ_ST_DATE,120) ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LQ_END_DATE   ,120) , convert(varchar,LQ_ST_DATE  ,120) ";
                        } else {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE,120) , convert(varchar,LYQ_END_DATE   ,120) , convert(varchar,LYQ_ST_DATE  ,120) ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " convert(varchar,CY_ST_DATE,120) , convert(varchar,LY_DAY  ,120) , convert(varchar,LY_ST_DATE,120)   ";
                        } else {
                            str2 = str2 + " convert(varchar,CY_ST_DATE,120) , convert(varchar,LY_END_DATE  ,120) , convert(varchar,LY_ST_DATE,120)   ";
                        }
                    }
                }

                {
                    timeStamp = "to_date";
                    str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                    ////.println("str2 non ps --" + str2);
                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);
                }

            } else {


                str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                                || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date -1,'mm/dd/yyyy hh24:mi:ss ') , to_char(ST_DATE -1 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date -1,'mm/dd/yyyy hh24:mi:ss ') , to_char(ST_DATE -1 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else {
                            str2 = str2 + " to_char(ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PM_DAY " + interval + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year") || PRG_COMPARE.equalsIgnoreCase("Same QTR Last Year")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_DAY " + interval + ",'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(LQ_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')   ";
                        } else {
                            str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')   ";
                        }
                    }
                }

                {
                    if (elementID != null) {
                        con = getConnection(elementID);
                        if (connType == null || connType.equals("")) {
                            connType = conectiontype.getConTypeByElementId(elementID);
                        }
                    } else if (connId != null) {
                        if (connType == null || connType.equals("")) {
                            connType = gettypeofconn.getConTypeByConnID(connId);
                        }
                        con = getConnectionByConnId(connId);
                    }
                }
                if (connType.equalsIgnoreCase("PostgreSQL")) {
                    timeStamp = "to_timestamp";
                    str2 = str2 + " from   " + timeTableName.toLowerCase() + "  " + timeTableName.toLowerCase() + " where ddate = " + timeStamp + "('" + d1 + "','MM/dd/yyyy'); ";
                    ////.println("str2" + str2);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                } else {
                    timeStamp = "to_date";
                    str2 = str2 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + d1 + "','MM/dd/yyyy') ";
                    System.out.println("str2 non ps --" + str2);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();
                }
            }


            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st != null) {
                st.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        GetConnectionType gettypeofconn = new GetConnectionType();
        if (elementID != null) {
            if (connType == null || connType.equals("")) {
                connType = gettypeofconn.getConTypeByElementId(elementID);
            }
        } else if (connId != null) {
            if (connType == null || connType.equals("")) {
                connType = gettypeofconn.getConTypeByConnID(connId);
            }
        }
        ////.println("conntype is" + connType);
        if (connType.equalsIgnoreCase("SqlServer")) {
             if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
                 d_clu = " =  convert(datetime,'" + ed_d + "',120) ";
                 pd_clu = " =  convert(datetime,'" + p_ed_d + "',120) ";
             }else{
                 
            d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
            pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) ";
             }    

        } else if (connType.equalsIgnoreCase("mysql")) {
          if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){ 
            d_clu = " = " + timeStamp + "('" + ed_d + "','" + dateFormat + " ') ";
            pd_clu = " = " + timeStamp + "('" + p_ed_d + "','" + dateFormat + " ') ";
          }   else{
            d_clu = " between  " + timeStamp + "('" + st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + ed_d + "','" + dateFormat + " ') ";
            pd_clu = " between  " + timeStamp + "('" + p_st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + p_ed_d + "','" + dateFormat + " ') ";
          }

        } else {
            //start of code by Nazneen on 4 April 2014
            String[] dateArrEdD = null;
            String[] dateArrPEdD = null;
            dateArrEdD = ed_d.split(" ");
            dateArrPEdD = p_ed_d.split(" ");
            if (dateArrEdD[0].contains("00:00:00")) {
                ed_d = dateArrEdD[0];
            } else {
                ed_d = dateArrEdD[0] + " " + "23:59:59 ";
            }
            if (dateArrPEdD[0].contains("00:00:00")) {
                p_ed_d = dateArrPEdD[0];
            } else {
                p_ed_d = dateArrPEdD[0] + " " + "23:59:59 ";
            }
//                ed_d = dateArrEdD[0] + " " + "23:59:59 ";
//                p_ed_d = dateArrPEdD[0] + " " + "23:59:59 ";
            //End of code by Nazneen on 4 April 2014
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){ 
            d_clu = " = " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            pd_clu = " = " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}else{
            d_clu = " between  " + timeStamp + "('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            ////.println("d_clu   -- --  " + d_clu);
            pd_clu = " between  " + timeStamp + "('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";

        }
        }
        setDateDiff(connType);


    }

    public String getPriorDate(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null;
        String dateType = null;
        String dateFormat = null;
        String dateConversion = null;
        String prior_date = null;

        GetConnectionType conectiontype = new GetConnectionType();
        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }

        GetConnectionType gettypeofconn = new GetConnectionType();
        connType = gettypeofconn.getConTypeByElementId(elementID);
        ////.println("conntype is" + connType);
        if (connType.equalsIgnoreCase("PostgreSQL")) {
            interval = "";
        } else if (connType.equalsIgnoreCase("mysql")) {
            dateType = "date_format";
            dateConversion = "date_format";
            dateFormat = "%m/%d/%Y %H:%i:%s";
            interval = "+.99999";
        } else {
            interval = "+.99999";
        }
        timeStamp = "to_date";

        str2 = " Select ";
        if (connType.equalsIgnoreCase("Oracle")) {
            str2 += "  to_Char(";

        } else if (connType.equalsIgnoreCase("SqlServer")) {
            str2 += "  convert(datetime,";

        } else if (connType.equalsIgnoreCase("mysql")) {
            str2 += "  date_format(";

        }


        if (PRG_PERIOD_TYPE.equalsIgnoreCase("Month")) {
            str2 += " PM_DAY ";
        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Qtr") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER")) {
            str2 += " LQ_DAY ";
        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Year")) {
            str2 += " LY_DAY ";
        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Week")) {
            str2 += " PW_DAY ";
        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Day")) {
            str2 += " dDATE ";
        }
        if (connType.equalsIgnoreCase("Oracle")) {
            str2 += " ,'mm/dd/yyyy')  A1 from " + timeTableName.toLowerCase() + "  " + timeTableName.toLowerCase() + " where  ddate = " + timeStamp + "('" + d1 + "','MM/dd/yyyy')";
        } else if (connType.equalsIgnoreCase("SqlServer")) {
            str2 += " ,101)  A1 from " + timeTableName.toLowerCase() + "  " + timeTableName.toLowerCase() + " where  ddate = convert(datetime,'" + d1 + "',101) ";
        }
        if (connType.equalsIgnoreCase("mysql")) {
            str2 += " ,'%m/%d/%Y')  A1 from " + timeTableName.toLowerCase() + "  " + timeTableName.toLowerCase() + " where  ddate = str_to_date('" + d1 + "','%m/%d/%Y')";
        }


        try {
            con = getConnection(elementID);
            st = con.createStatement();
            rs2 = st.executeQuery(str2);


            while (rs2.next()) {
                prior_date = rs2.getString(1);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }

            rs2.close();
            st.close();
            con.close();

            rs2 = null;
            st = null;
            con = null;
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }


        return (prior_date);
    }

    public void setDateDiff(String ConnType) {

        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "Select ";

        if (ConnType.equalsIgnoreCase("SqlServer")) {
            str2 += " datediff(DD,convert(datetime,'" + st_d + "',120), convert(datetime,'" + ed_d + "',120) )  A1 ";
        } else if (ConnType.equalsIgnoreCase("Oracle")) {
            str2 += " to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') - to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') A1 from dual ";
        } else if (ConnType.equalsIgnoreCase("mysql")) {
            str2 += " datediff(str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') , str_to_date('" + st_d + "','%m/%d/%Y %H:%i:%s')) A1  ";
        } else {
            str2 += " to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') - to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') A1  ";
        }
        try {


            //
            if (elementID != null) {
                con = getConnection(elementID);
            } else if (connId != null) {
                con = getConnectionByConnId(connId);
            }

            st = con.createStatement();
            rs2 = st.executeQuery(str2);
            while (rs2.next()) {
                daysDiff = rs2.getInt(1);
            }
            rs2.close();
            rs2 = null;
            st.close();
            st = null;
            con.close();
            con = null;

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        } finally {

            try {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
        }


    }

    public void setRange(ArrayList timeArray) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        ProgenParam param = new ProgenParam();
        String dateType = null, dateFormat = null;

        GetConnectionType gettypeofconn = new GetConnectionType();
        connType = gettypeofconn.getConTypeByElementId(elementID);
        ////.println("conntype is" + connType);

        if (connType.equalsIgnoreCase("mysql")) {
            dateType = "date_format";
            timeStamp = "str_to_date";
            dateFormat = "%m/%d/%Y %H:%i:%s";

            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                } else {
//                ed_d = param.getdateforpage();
                    if (param.getdateforpage().contains("00:00:00")) {
                        ed_d = param.getdateforpage();
                    } else {
                        ed_d = param.getdateforpage() + " " + "23:59:59 ";
                    }
//                ed_d = param.getdateforpage()+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                } else {
                    p_st_d = param.getdateforpage(60);
                }
            }
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                String PRG_ROLLING = "";
                String ref_date = "";
                String strpart2 = "";
                String strpart3 = "";
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "Last 30 Days";
                } else {
                    PRG_ROLLING = timeArray.get(3).toString();
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                str2 = "Select " + dateType + "(end_date,'" + dateFormat + " ') , " + dateType + "(date_sub(st_date , ";
                strpart2 = " , " + dateType + "(date_sub(end_date , ";
                strpart3 = " , " + dateType + "(date_sub(st_date , ";

                if (PRG_ROLLING.equalsIgnoreCase("Last 7 Days")) {
                    str2 += " INTERVAL 7  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 8 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 15  day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 15 Days")) {
                    str2 += " INTERVAL 15  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 16 day ) ,'" + dateFormat + " ')  ";
                    strpart3 += " INTERVAL 31 day ) ,'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 30 Days")) {
                    str2 += " INTERVAL 30  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 31 day ) ,'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 61  day ),'" + dateFormat + " ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 60 Days")) {
                    str2 += " INTERVAL 60  day ),'" + dateFormat + " ')  ";
                    strpart2 += " INTERVAL 61 day ) ,'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 121 day ) ,'" + dateFormat + " ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 90 Days")) {
                    str2 += " INTERVAL 90 day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 91 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 181 day ) ,'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 180 Days")) {
                    str2 += " INTERVAL 180 day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 181 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 361 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 365 Days")) {
                    str2 += " INTERVAL 365 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 366 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 721 day ),'" + dateFormat + " ') ";

                }
                GetConnectionType connectionType = new GetConnectionType();
                connType = connectionType.getConTypeByElementId(elementID);
                if (connType.equalsIgnoreCase("PostgreSQL")) {
                    timeStamp = "to_timestamp";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";
                } else if (connType.equalsIgnoreCase("mysql")) {
                    timeStamp = "str_to_date";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','%m/%d/%Y') ";
                } else {
                    timeStamp = "to_date";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";
                }



                try {
                    con = getConnection(elementID);

                    st2 = con.prepareCall(str2);

                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){ 
                d_clu = " =  " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
                pd_clu = " = " + timeStamp + "('" + p_ed_d + "','" + dateFormat + "') ";
}else{
                d_clu = " between  " + timeStamp + "('" + st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
                pd_clu = " between  " + timeStamp + "('" + p_st_d + "','" + dateFormat + "') and  " + timeStamp + "('" + p_ed_d + "','" + dateFormat + "') ";
}





            }
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_ROLLING")) {
                String PRG_ROLLING = "";
                String ref_date = "";
                String strpart2 = "";
                String strpart3 = "";
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "Last 1 Month";
                } else {
                    PRG_ROLLING = timeArray.get(3).toString();
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                str2 = "Select " + dateType + "(end_date,'" + dateFormat + " ') , " + dateType + "(date_sub(st_date , ";
                strpart2 = " , " + dateType + "(date_sub(end_date , ";
                strpart3 = " , " + dateType + "(date_sub(st_date , ";

                if (PRG_ROLLING.equalsIgnoreCase("Last 1 Months")) {
                    str2 += " INTERVAL 30  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 31 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 61  day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 2 Months")) {
                    str2 += " INTERVAL 60  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 61 day ) ,'" + dateFormat + " ')  ";
                    strpart3 += " INTERVAL 91 day ) ,'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 3 Months")) {
                    str2 += " INTERVAL 92  day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 93 day ) ,'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 121  day ),'" + dateFormat + " ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 4 Months")) {
                    str2 += " INTERVAL 120  day ),'" + dateFormat + " ')  ";
                    strpart2 += " INTERVAL 121 day ) ,'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 151 day ) ,'" + dateFormat + " ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 5 Months")) {
                    str2 += " INTERVAL 150 day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 151 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 181 day ) ,'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 6 Months")) {
                    str2 += " INTERVAL 180 day ),'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 181 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 211 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 7 Months")) {
                    str2 += " INTERVAL 210 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 211 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 241 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 8 Months")) {
                    str2 += " INTERVAL 240 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 241 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 271 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 9 Months")) {
                    str2 += " INTERVAL 270 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 271 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 301 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 10 Months")) {
                    str2 += " INTERVAL 300 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 301 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 331 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 11 Months")) {
                    str2 += " INTERVAL 330 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 331 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 361 day ),'" + dateFormat + " ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 12 Months")) {
                    str2 += " INTERVAL 360 day ) ,'" + dateFormat + " ') ";
                    strpart2 += " INTERVAL 361 day ),'" + dateFormat + " ') ";
                    strpart3 += " INTERVAL 391 day ),'" + dateFormat + " ') ";

                }
                GetConnectionType connectionType = new GetConnectionType();
                connType = connectionType.getConTypeByElementId(elementID);
                if (connType.equalsIgnoreCase("PostgreSQL")) {
                    timeStamp = "to_timestamp";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";
                } else if (connType.equalsIgnoreCase("mysql")) {
                    timeStamp = "str_to_date";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','%m/%d/%Y') ";
                } else {
                    timeStamp = "to_date";
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";

                }
                try {
                    con = getConnection(elementID);

                    st2 = con.prepareCall(str2);

                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(4);

                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){ 
                d_clu = " = " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
                pd_clu = " = " + timeStamp + "('" + p_ed_d + "','" + dateFormat + "') ";
}else{
                d_clu = " between  " + timeStamp + "('" + st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
                pd_clu = " between  " + timeStamp + "('" + p_st_d + "','" + dateFormat + "') and  " + timeStamp + "('" + p_ed_d + "','" + dateFormat + "') ";
}

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + "') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(60);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + "') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1).toString();
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0).toString();
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'" + dateFormat + " ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
            d_clu = " = " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
            pd_clu = " = " + timeStamp + "('" + p_ed_d + "','" + dateFormat + " ') ";
    }else{
            d_clu = " between  " + timeStamp + "('" + st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + ed_d + "','" + dateFormat + "') ";
            pd_clu = " between  " + timeStamp + "('" + p_st_d + "','" + dateFormat + " ') and  " + timeStamp + "('" + p_ed_d + "','" + dateFormat + " ') ";
 }



        } else if (connType.equalsIgnoreCase("SqlServer")) {

            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                } else {
//                    ed_d = param.getdateforpage();
                    if (param.getdateforpage().contains("00:00:00")) {
                        ed_d = param.getdateforpage();
                    } else {
                        ed_d = param.getdateforpage() + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage()+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                } else {
                    p_st_d = param.getdateforpage(60);
                }
            }
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                String PRG_ROLLING = "";
                String ref_date = "";
                String strpart2 = "";
                String strpart3 = "";
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "Last 30 Days";
                } else {
                    PRG_ROLLING = timeArray.get(3).toString();
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                ////.println("PRG ROLLOING DAYS " + PRG_ROLLING);
                str2 = "Select convert(varchar,end_date,120) , convert(varchar,st_date - ";
                strpart2 = " , convert(varchar,end_date - ";
                strpart3 = " , convert(varchar,st_date - ";

                if (PRG_ROLLING.equalsIgnoreCase("Last 7 Days")) {
                    str2 += " 7 ,120) ";
                    strpart2 += " 8,120) ";
                    strpart3 += " 15 ,120) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 15 Days")) {
                    str2 += " 15 ,120) ";
                    strpart2 += " 16 ,120)  ";
                    strpart3 += " 31 ,120) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 30 Days")) {
                    str2 += " 30 ,120) ";
                    strpart2 += " 31 ,120) ";
                    strpart3 += " 61 ,120)  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 60 Days")) {
                    str2 += " 60 ,120)  ";
                    strpart2 += " 61 ,120) ";
                    strpart3 += " 121 ,120)  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 90 Days")) {
                    str2 += " 90 ,120) ";
                    strpart2 += " 91 ,120) ";
                    strpart3 += " 181 ,120) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 180 Days")) {
                    str2 += " 180 ,120) ";
                    strpart2 += " 181 ,120) ";
                    strpart3 += " 361 ,120) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 365 Days")) {
                    str2 += " 365 ,120) ";
                    strpart2 += " 366 ,120) ";
                    strpart3 += " 721 ,120) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 366 Days")) {
                    str2 += " 366 ,120) ";
                    strpart2 += " 367 ,120) ";
                    strpart3 += " 723 ,120) ";

                }
                GetConnectionType connectionType = new GetConnectionType();
                connType = connectionType.getConTypeByElementId(elementID);

                str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + ref_date + "',101) ";

//////////////////////////.println.println("time Q"+str2);
                try {
                    con = getConnection(elementID);

                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }


                    if (con != null) {
                        con.close();
                    }
                }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
     d_clu = " = convert(datetime,'" + ed_d + "',120) ";
     pd_clu = " =  convert(datetime,'" + p_ed_d + "',120) ";
}else{
                d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
   pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120)";
}






            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(60);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select convert(varchar,max(END_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select convert(varchar,min(ST_DATE) ,120) from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st = con.createStatement();
                    rs2 = st.executeQuery(str2);

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (st != null) {
                        st.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            }
            if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
                d_clu = " = convert(datetime,'" + ed_d + "',120) ";
            pd_clu = " =  convert(datetime,'" + p_ed_d + "',120) ";
            }else{
                
            d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
            pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) ";
            }





        } else {

            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                } else {
//                    ed_d = param.getdateforpage();
                    if (param.getdateforpage().contains("00:00:00")) {
                        ed_d = param.getdateforpage().toString();
                    } else {
                        ed_d = param.getdateforpage() + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage()+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(3).toString();
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(2).toString();
                } else {
                    p_st_d = param.getdateforpage(60);
                }



                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    ft_ed_d = timeArray.get(5).toString();
                } else {
                    ft_ed_d = param.getdateforpage(-365);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    ft_st_d = timeArray.get(4).toString();
                } else {
                    ft_st_d = param.getdateforpage(30);
                }
            }


            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                } else {
//                    ed_d = param.getdateforpage();
                    if (param.getdateforpage().contains("00:00:00")) {
                        ed_d = param.getdateforpage();
                    } else {
                        ed_d = param.getdateforpage() + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage()+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                } else {
                    p_st_d = param.getdateforpage(60);
                }
            }
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                String PRG_ROLLING = "";
                String ref_date = "";
                String strpart2 = "";
                String strpart3 = "";
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "Last 30 Days";
                } else {
                    PRG_ROLLING = timeArray.get(3).toString();
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }
                ////.println("PRG ROLLOING DAYS " + PRG_ROLLING);
                str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(st_date - ";
                strpart2 = " , to_char(end_date - ";
                strpart3 = " , to_char(st_date - ";

                if (PRG_ROLLING.equalsIgnoreCase("Last 7 Days")) {
                    str2 += " 7 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 8,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 15 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 15 Days")) {
                    str2 += " 15 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 16 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 += " 31 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 30 Days")) {
                    str2 += " 30 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 31 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 61 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 60 Days")) {
                    str2 += " 60 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 += " 61 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 121 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 90 Days")) {
                    str2 += " 90 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 91 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 181 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 180 Days")) {
                    str2 += " 180 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 181 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 361 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 365 Days")) {
                    str2 += " 365 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 366 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 721 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 366 Days")) {
                    str2 += " 366 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 367 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 723 ,'mm/dd/yyyy hh24:mi:ss ') ";

                }
                GetConnectionType connectionType = new GetConnectionType();
                connType = connectionType.getConTypeByElementId(elementID);
                if (connType.equalsIgnoreCase("PostgreSQL")) {
                    timeStamp = "to_timestamp";
                } else {
                    timeStamp = "to_date";
                }
                str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";

//////////////////////////.println.println("time Q"+str2);
                try {
                    con = getConnection(elementID);

                    st2 = con.prepareCall(str2);

                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
                d_clu = " =  " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " = " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}else{
                d_clu = " between  " + timeStamp + "('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " between  " + timeStamp + "('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}





            }
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_ROLLING")) {
                String PRG_ROLLING = "";
                String ref_date = "";
                String strpart2 = "";
                String strpart3 = "";
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "Last 1 Months";
                } else {
                    PRG_ROLLING = timeArray.get(3).toString();
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                ////.println("PRG ROLLOING DAYS " + PRG_ROLLING);
                str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(st_date - ";
                strpart2 = " , to_char(end_date - ";
                strpart3 = " , to_char(st_date - ";

                if (PRG_ROLLING.equalsIgnoreCase("Last 1 Months")) {
                    str2 += " 30 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 31,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 61 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 2 Months")) {
                    str2 += " 61 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 62 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 += " 92 ,'mm/dd/yyyy hh24:mi:ss ') ";


                } else if (PRG_ROLLING.equalsIgnoreCase("Last 3 Months")) {
                    str2 += " 91 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 92 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 122 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 4 Months")) {
                    str2 += " 121 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 += " 122 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 152 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 5 Months")) {
                    str2 += " 152 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 153 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 183 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 6 Months")) {
                    str2 += " 183 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 184 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 214 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 7 Months")) {
                    str2 += " 215 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 216 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 246 ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("Last 8 Months")) {
                    str2 += " 246 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " 246 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " 276 ,'mm/dd/yyyy hh24:mi:ss ') ";

                }
                GetConnectionType connectionType = new GetConnectionType();
                connType = connectionType.getConTypeByElementId(elementID);
                if (connType.equalsIgnoreCase("PostgreSQL")) {
                    timeStamp = "to_timestamp";
                } else {
                    timeStamp = "to_date";
                }
                str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = " + timeStamp + "('" + ref_date + "','MM/dd/yyyy') ";

                try {
                    con = getConnection(elementID);

                    st2 = con.prepareCall(str2);

                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
                d_clu = " =  " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " = " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}else{
                d_clu = " between  " + timeStamp + "('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " between  " + timeStamp + "('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  " + timeStamp + "('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";

}

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).toString().contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(30);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(31);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CM_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(60);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CQ_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                if (!(timeArray.get(3) == null || timeArray.get(3).toString().equalsIgnoreCase("NULL"))) {
//                    ed_d = timeArray.get(3).toString();
                    if (timeArray.get(3).toString().contains("00:00:00")) {
                        ed_d = timeArray.get(3).toString();
                    } else {
                        ed_d = timeArray.get(3).toString() + " " + "23:59:59 ";
                    }
//                    ed_d = timeArray.get(3).toString()+ " " + "23:59:59 ";
                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
//                        ed_d = rs2.getString(1);
                        if (rs2.getString(1).contains("00:00:00")) {
                            ed_d = rs2.getString(1);
                        } else {
                            ed_d = rs2.getString(1) + " " + "23:59:59 ";
                        }
//                        ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
//                    ed_d = param.getdateforpage(0);
                    if (param.getdateforpage(0).contains("00:00:00")) {
                        ed_d = param.getdateforpage(0);
                    } else {
                        ed_d = param.getdateforpage(0) + " " + "23:59:59 ";
                    }
//                    ed_d = param.getdateforpage(0)+ " " + "23:59:59 ";
                }

                if (!(timeArray.get(2) == null || timeArray.get(2).toString().equalsIgnoreCase("NULL"))) {
                    st_d = timeArray.get(2).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        st_d = rs2.getString(1);
                        //p_ed_d = rs2.getString(3);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    st_d = param.getdateforpage(365);
                }


                if (!(timeArray.get(5) == null || timeArray.get(5).toString().equalsIgnoreCase("NULL"))) {
                    p_ed_d = timeArray.get(5).toString();

                    str2 = "select to_char(max(END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_ed_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        p_ed_d = rs2.getString(1);
                        //p_st_d = rs2.getString(4);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_ed_d = param.getdateforpage(366);
                }


                if (!(timeArray.get(4) == null || timeArray.get(4).toString().equalsIgnoreCase("NULL"))) {
                    p_st_d = timeArray.get(4).toString();
                    str2 = "select to_char(min(ST_DATE) ,'mm/dd/yyyy hh24:mi:ss ') from  " + timeTableName + "  ";
                    str2 += " where CY_CUST_NAME ='" + p_st_d + "' ";

                    con = getConnection(elementID);
                    st2 = con.prepareCall(str2);
                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        //ed_d = rs2.getString(1);
                        //st_d = rs2.getString(2);
                        //p_ed_d = rs2.getString(3);
                        p_st_d = rs2.getString(1);


                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } else {
                    p_st_d = param.getdateforpage(734);
                }

            }
            //start of code by Nazneen on 4 April 2014
            String[] dateArrEdD = null;
            String[] dateArrPEdD = null;
            dateArrEdD = ed_d.split(" ");
            dateArrPEdD = p_ed_d.split(" ");
            if (dateArrEdD[0].contains("00:00:00")) {
                ed_d = dateArrEdD[0];
            } else {
                ed_d = dateArrEdD[0] + " " + "23:59:59 ";
            }
            if (dateArrPEdD[0].contains("00:00:00")) {
                p_ed_d = dateArrPEdD[0];
            } else {
                p_ed_d = dateArrPEdD[0] + " " + "23:59:59 ";
            }
//                ed_d = dateArrEdD[0] + " " + "23:59:59 ";
//                p_ed_d = dateArrPEdD[0] + " " + "23:59:59 ";
            //End of code by Nazneen on 4 April 2014
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
            d_clu = " = to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            pd_clu = " = to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}else{
            d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
}
            if (timeArray.get(1).toString().equalsIgnoreCase("PRG_COHORT")) {
                ft_clu = " between  to_date('" + ft_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ft_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            } else {
                ft_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            }


        }




        setDateDiff(connType);


    }

    public String setRange(ArrayList timeArray, String totDays) throws SQLException {
        //////////////////////////.println("totDays"+totDays+"--- ");
        totDays = totDays.trim();
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        String d_clu1 = "";
        String pd_clu1 = "";
//        String st_d1 = "";
//        String ed_d1 = "";
//        String p_st_d1 = "";
//        String p_ed_d1 = "";
        String PRG_ROLLING = "";
        String ref_date = "";
        String strpart2 = "";
        String strpart3 = "";
        String strpart4 = null;
        String clauseForce = "Between";
        String ActualClause = "";
        boolean executeQry = true;
        int trailCount = 0;
        boolean trailing = false;
        ProgenParam param = new ProgenParam();
        GetConnectionType getconType = new GetConnectionType();
        if (connType == null || connType.equals("")) {
            connType = getconType.getConTypeByElementId(elementID);
        }
        ////.println("connType is" + connType);


        if (connType.equalsIgnoreCase("Sqlserver")) {
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY")) {
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "30days(";
                } //added by Nazneen for Trailling As Period
                else if (totDays.contains("TPERIODS")) {
                    if (!timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        PRG_ROLLING = "TDAYS" + totDays.substring(8);
                    } else {
                        if (timeArray.get(3).toString().equalsIgnoreCase("MONTH")) {
                            PRG_ROLLING = "TMONTHS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("MONTH")) {
                            PRG_ROLLING = "TWMONTHS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("DAY")) {
                            PRG_ROLLING = "TDAYS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("YEAR")) {
                            PRG_ROLLING = "TYEARS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("QTR")) {
                            PRG_ROLLING = "TQTRS" + totDays.substring(8);
                        }
                    }
                } else {
                    PRG_ROLLING = totDays;
                }


                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }
                str2 = "Select convert(varchar,end_date,120) , convert(varchar,st_date - ";
                strpart2 = " , convert(varchar,end_date - ";
                strpart3 = " , convert(varchar,st_date - ";


                if (PRG_ROLLING.contains("RDAYS")
                        || PRG_ROLLING.contains("TDAYS")
                        || PRG_ROLLING.contains("TMONTHS")
                        || PRG_ROLLING.contains("TWMONTHS")
                        || PRG_ROLLING.contains("TQTRS")
                        || PRG_ROLLING.contains("TYEARS")
                        || PRG_ROLLING.contains("RMONTHS")
                        || PRG_ROLLING.contains("LMONTHS")
                        || PRG_ROLLING.contains("RQTRS")
                        || PRG_ROLLING.contains("LQTRS")
                        || PRG_ROLLING.contains("RYEARS")
                        || PRG_ROLLING.contains("LYEARS")
                        || PRG_ROLLING.contains("DRANGE")
                        || PRG_ROLLING.contains("MRANGE")
                        || PRG_ROLLING.contains("QRANGE")
                        || PRG_ROLLING.contains("YRANGE")
                        || PRG_ROLLING.contains("DDIS")
                        || PRG_ROLLING.contains("MDIS")
                        || PRG_ROLLING.contains("QDIS")
                        || PRG_ROLLING.contains("YDIS")) {
                    if (PRG_ROLLING.contains("DRANGE")
                            || PRG_ROLLING.contains("MRANGE")
                            || PRG_ROLLING.contains("QRANGE")
                            || PRG_ROLLING.contains("YRANGE")) {

                        rollingLevel = "Year";//Can pickup any range ;start date and end date are used

                        PRG_ROLLING = PRG_ROLLING.replace("DRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
                        if (a.length == 1) {
                            a[1] = a[0];
                        } else if (a.length == 0) {
                            a[0] = param.getdateforpage();
                            a[1] = a[0];
                        }
                        ed_d1 = a[1] + " 23:59:59  ";
                        st_d1 = a[0] + " 00:00:00 ";
                        p_ed_d1 = a[1] + " 23:59:59 ";
                        p_st_d1 = a[0] + " 00:00:00 ";
                        executeQry = false;

                    } else if (PRG_ROLLING.contains("DDIS")
                            || PRG_ROLLING.contains("MDIS")
                            || PRG_ROLLING.contains("QDIS")
                            || PRG_ROLLING.contains("YDIS")) {
//                       if(PRG_ROLLING.contains("DDIS") )
//                            rollingLevel="Day";
//                       else if(PRG_ROLLING.contains("MDIS") )
//                           rollingLevel="Month";
//                       else if(PRG_ROLLING.contains("QDIS") )
//                           rollingLevel="Qtr";
//                       else if(PRG_ROLLING.contains("YDIS") )
//                           rollingLevel="Year";
                        rollingLevel = "Day";//Summrization not supported



                        executeQry = false;
                        clauseForce = "in";

                        ActualClause = "  in ( Select distinct ddate from pr_day_denom where ";
                        if (PRG_ROLLING.contains("DDIS")) {
                            ActualClause += " ddate ";
                        } else if (PRG_ROLLING.contains("MDIS")) {
                            ActualClause += " convert(smalldatetime,cm_end_date) ";
                        } else if (PRG_ROLLING.contains("QDIS")) {
                            ActualClause += " convert(smalldatetime,cq_end_date) ";
                        } else if (PRG_ROLLING.contains("YDIS")) {
                            ActualClause += " convert(smalldatetime,cy_end_date) ";
                        } else {
                            ActualClause += " st_date ";
                        }

                        PRG_ROLLING = PRG_ROLLING.replace("DDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
                //        String Ddate1 = "";
                        StringBuilder Ddate1=new StringBuilder();
                        for (int loop = 0; loop < a.length; loop++) {
                            if (loop == 0) {
                   //            Ddate1 = " convert(datetime,'" + a[loop] + "',101) ";
                                Ddate1.append(" convert(datetime,'").append(a[loop]).append("',101) ");
                            } else {
                        //        Ddate1 += " ,  convert(datetime,'" + a[loop] + "',101)";
                                Ddate1.append(" ,  convert(datetime,'").append(a[loop]).append("',101)");
                            }
                        }



                        ActualClause = ActualClause + " in ( " + Ddate1 + " )) ";
                    } else if (PRG_ROLLING.contains("RDAYS")
                            || PRG_ROLLING.contains("RMONTHS")
                            || PRG_ROLLING.contains("RQTRS")
                            || PRG_ROLLING.contains("RYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("RDAYS")) {
                            PRG_ROLLING = PRG_ROLLING.replace("RDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[1] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                a[1] = a[0];
                            }

                            str2 = "Select convert(varchar,end_date- " + a[0] + ",120) , convert(varchar,end_date- " + a[1] + ",120)  ";
                            strpart2 = " ,convert(varchar,end_date- " + a[0] + ",120)  ";
                            strpart3 = " , convert(varchar,end_date- " + a[1] + ",120)  ";

                        }




                    } else if (PRG_ROLLING.contains("TDAYS")
                            || PRG_ROLLING.contains("TMONTHS")
                            || PRG_ROLLING.contains("TWMONTHS")
                            || PRG_ROLLING.contains("TQTRS")
                            || PRG_ROLLING.contains("TYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("TDAYS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
//                               a[1]=a[0];

                            } else if (a.length == 0) {
                                a[0] = "0";
//                               a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select convert(varchar,end_date- " + a[0] + ",120) , convert(varchar,end_date- " + a[0] + ",120)  ";
                            strpart2 = " ,convert(varchar,end_date- " + a[0] + ",120)  ";
                            strpart3 = " , convert(varchar,end_date- " + a[0] + ",120)  ,CM_END_DATE ";
                            strpart4 = " from   " + timeTableName + " where ddate <= convert(datetime,'" + ref_date + "',101)  order by CM_END_DATE desc";

                        } else if (PRG_ROLLING.contains("TMONTHS") || PRG_ROLLING.contains("TWMONTHS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("TWMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct convert(varchar,CM_END_DATE ,120) A1 , convert(varchar,CM_ST_DATE,120) A2 ";
                            strpart2 = " ,convert(varchar,CM_END_DATE ,120) A3 ";
                            strpart3 = " , convert(varchar,CM_ST_DATE ,120) A4 ,CM_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cm_st_date <= convert(datetime,'" + ref_date + "',101)  order by CM_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TQTRS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TQTRS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct convert(varchar,CQ_END_DATE ,120) A1 , convert(varchar,CQ_ST_DATE,120) A2 ";
                            strpart2 = " ,convert(varchar,CQ_END_DATE ,120) A3 ";
                            strpart3 = " , convert(varchar,CQ_ST_DATE ,120) A4 ,CQ_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cq_st_date <= convert(datetime,'" + ref_date + "',101)  order by CQ_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TYEARS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TYEARS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct convert(varchar,CY_END_DATE ,120) A1 , convert(varchar,CY_ST_DATE,120) A2 ";
                            strpart2 = " ,convert(varchar,CY_END_DATE ,120) A3 ";
                            strpart3 = " , convert(varchar,CY_ST_DATE ,120) A4 ,CY_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cY_st_date <= convert(datetime,'" + ref_date + "',101)  order by CY_END_DATE desc";
                        }




                    }

                } else {
                    rollingLevel = "Year";
                    if (PRG_ROLLING.equalsIgnoreCase("30days(")) {
                        str2 += " 30 ,120) ";
                        strpart2 += " 31 ,120) ";
                        strpart3 += " 61 ,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("60days(")) {
                        str2 += " 60 ,120)  ";
                        strpart2 += " 61 ,120) ";
                        strpart3 += " 121 ,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("90days(")) {
                        str2 += " 90 ,120) ";
                        strpart2 += " 91 ,120) ";
                        strpart3 += " 181 ,120) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("180days(")) {
                        str2 += " 180 ,120) ";
                        strpart2 += " 181 ,120) ";
                        strpart3 += " 361 ,120) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("365days(")) {
                        str2 += " 365 ,120) ";
                        strpart2 += " 366 ,120) ";
                        strpart3 += " 721 ,120) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("366days(")) {
                        str2 += " 366 ,120) ";
                        strpart2 += " 367 ,120) ";
                        strpart3 += " 723 ,120) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("45days(")) {
                        str2 += " 45 ,120) ";
                        strpart2 += " 46 ,120) ";
                        strpart3 += " 91 ,120) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("WTD(") || PRG_ROLLING.equalsIgnoreCase("WTD")) {
                        str2 = "Select convert(varchar,end_date,120) , convert(varchar,CW_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,PW_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PW_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PWTD(") || PRG_ROLLING.equalsIgnoreCase("PWTD")) {
                        str2 = "Select convert(varchar,PW_DAY,120) , convert(varchar,PW_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,PW_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PW_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYWTD(") || PRG_ROLLING.equalsIgnoreCase("PYWTD")) {
                        str2 = "Select convert(varchar,LYW_DAY,120) , convert(varchar,LYW_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,LYW_ST_DATE,120)  ";
                        strpart3 = " , convert(varchar,LYW_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTD(") || PRG_ROLLING.equalsIgnoreCase("MTD")) {
                        str2 = "Select convert(varchar,end_date,120) , convert(varchar,CM_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,PM_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PM_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTDW(") || PRG_ROLLING.equalsIgnoreCase("MTDW")) {
                        str2 = "Select convert(varchar,end_date,120) , convert(varchar,end_date,120)  ";
                        strpart2 = " ,convert(varchar,PM_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PM_END_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("QTD(") || PRG_ROLLING.equalsIgnoreCase("QTD")) {
                        str2 = "Select convert(varchar,end_date,120) , convert(varchar,CQ_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,LQ_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,LQ_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("YTD(") || PRG_ROLLING.equalsIgnoreCase("YTD")) {
                        str2 = "Select convert(varchar,end_date,120) , convert(varchar,CY_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,LY_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,LY_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("CYD(") || PRG_ROLLING.equalsIgnoreCase("CYD")) {
                        str2 = "Select convert(varchar,CY_END_DATE,120) , convert(varchar,CY_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,LY_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,LY_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("LTD(")) {
                        str2 = "Select convert(varchar,end_date,120)  , '1980-01-01 00:00:00' ";
                        strpart2 = " ,convert(varchar,LY_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,LY_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PMTD(") || PRG_ROLLING.equalsIgnoreCase("PMTD")) {
                        str2 = "Select convert(varchar,PM_END_DATE,120)  , convert(varchar,PM_ST_DATE,120)   ";
                        strpart2 = " ,convert(varchar,PM_END_DATE,120)   ";
                        strpart3 = " , convert(varchar,PM_ST_DATE,120)   ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PQTD(") || PRG_ROLLING.equalsIgnoreCase("PQTD")) {
                        str2 = "Select convert(varchar,LQ_END_DATE,120)  , convert(varchar,LQ_ST_DATE,120)   ";
                        strpart2 = " ,convert(varchar,LQ_END_DATE,120)   ";
                        strpart3 = " , convert(varchar,LQ_ST_DATE,120)   ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYTD(") || PRG_ROLLING.equalsIgnoreCase("PYTD")) {
                        str2 = "Select convert(varchar,LY_END_DATE,120)  , convert(varchar,LY_ST_DATE,120)   ";
                        strpart2 = " ,convert(varchar,LY_END_DATE,120)   ";
                        strpart3 = " , convert(varchar,LY_ST_DATE,120)   ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYMTD(") || PRG_ROLLING.equalsIgnoreCase("PYMTD")) {
                        str2 = "Select convert(varchar,PYM_END_DATE,120)  , convert(varchar,PYM_ST_DATE,120)   ";
                        strpart2 = " ,convert(varchar,PM_END_DATE,120)   ";
                        strpart3 = " , convert(varchar,PM_ST_DATE,120)   ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYQTD(") || PRG_ROLLING.equalsIgnoreCase("PYQTD")) {
                        str2 = "Select convert(varchar,LYQ_END_DATE,120)  , convert(varchar,LYQ_ST_DATE,120)   ";
                        strpart2 = " ,convert(varchar,LYQ_END_DATE,120)   ";
                        strpart3 = " , convert(varchar,LYQ_ST_DATE,120)   ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("1month(")) {
                        str2 = "Select convert(varchar,PM_END_DATE,120) , convert(varchar,PM_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,PM_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PM_ST_DATE,120)  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("2month(")) {
                        str2 = "Select convert(varchar,CM_END_DATE,120) , convert(varchar,PM_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,CM_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,PM_ST_DATE,120)  ";
                        strpart4 = " from   " + timeTableName + "  where ddate = (select PM_DAY from   " + timeTableName + "  where ddate = convert(datetime,'" + ref_date + "',101) ) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("3month(")) {
                        str2 = " select convert(varchar,max(end_date),120) end_date ,convert(varchar,min(st_date),120) st_date ,max (end_date) end_date1 ,min(st_date) st_date1   from( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  convert(datetime,'" + ref_date + "',101) -92  and convert(datetime,'" + ref_date + "',101)  )";
                        strpart4 = " a ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("6month(")) {
                        str2 = " select convert(varchar,max(end_date),120) end_date ,convert(varchar,min(st_date),120) st_date ,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  convert(datetime,'" + ref_date + "',101) -183 and convert(datetime,'" + ref_date + "',101) )";
                        strpart4 = " a ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("1year(")) {
                        str2 = " select convert(varchar,max(end_date),120) end_date ,convert(varchar,min(st_date),120) st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  convert(datetime,'" + ref_date + "',101) -365  and convert(datetime,'" + ref_date + "',101)  )";
                        strpart4 = " a ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("2year(")) {
                        str2 = " select convert(varchar,max(end_date),120) end_date ,convert(varchar,min(st_date),120) st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  convert(datetime,'" + ref_date + "',101) -700  and convert(datetime,'" + ref_date + "',101)  )";
                        strpart4 = " a ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("NYD(")) {
                        str2 = "Select convert(varchar,NY_END_DATE,120) , convert(varchar,NY_ST_DATE,120)  ";
                        strpart2 = " ,convert(varchar,LY_END_DATE,120)  ";
                        strpart3 = " , convert(varchar,LY_ST_DATE,120)  ";

                    }
                }


                ////////////////////////.println("strpart4 is "+ strpart4);
                if (strpart4 == null) {
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + ref_date + "',101) ";
                } else {
                    str2 = str2 + strpart2 + strpart3 + strpart4;
                }

                //////////////////////.println("time Q"+str2);
                if (executeQry) {
                    try {
                        con = getConnection(elementID);

                        st = con.createStatement();
                        rs2 = st.executeQuery(str2);

//                    while (rs2.next()) {
//                        ed_d1 = rs2.getString(1);
//                        st_d1 = rs2.getString(2);
//                        p_ed_d1 = rs2.getString(3);
//                        p_st_d1 = rs2.getString(4);
//
//
//                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
//                    }
                        if (!trailing) {
                            while (rs2.next()) {
                                ed_d1 = rs2.getString(1);
                                st_d1 = rs2.getString(2);
                                p_ed_d1 = rs2.getString(3);
                                p_st_d1 = rs2.getString(4);


                                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                            }
                        } else {
                            PbReturnObject pb = null;
                            if (rs2 != null) {
                                pb = new PbReturnObject(rs2);
                            }

                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (pb.rowCount >= trailCount) {
                                ed_d1 = pb.getFieldValueString(trailCount, 0);
                                st_d1 = pb.getFieldValueString(trailCount, 1);
                                p_ed_d1 = pb.getFieldValueString(trailCount, 2);
                                p_st_d1 = pb.getFieldValueString(trailCount, 3);
                            } else {
                                ed_d1 = ref_date + " 00:00:00";
                                st_d1 = ref_date + " 23:00:00";
                                p_ed_d1 = ref_date + " 00:00:00";
                                p_st_d1 = ref_date + " 23:00:00";
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                        logger.error("Exception:", e);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (st2 != null) {
                            st2.close();
                        }

                        if (con != null) {
                            con.close();
                        }
                    }

                }



                if (clauseForce.equalsIgnoreCase("BETWEEN")) {
                    ////////////////////////.println("st_d1"+st_d1);
                    if (PRG_ROLLING.equals("TWMONTHS")) {
                        d_clu1 = " between  convert(datetime,'" + ed_d1 + "',120) and  convert(datetime,'" + ed_d1 + "',120) ";
                        pd_clu1 = " between  convert(datetime,'" + p_ed_d1 + "',120) and  convert(datetime,'" + p_ed_d1 + "',120) ";
                    } else {
                        d_clu1 = " between  convert(datetime,'" + st_d1 + "',120) and  convert(datetime,'" + ed_d1 + "',120) ";
                        pd_clu1 = " between  convert(datetime,'" + p_st_d1 + "',120) and  convert(datetime,'" + p_ed_d1 + "',120) ";
                    }


                } else if (clauseForce.equalsIgnoreCase("in")) {
                    // d_clu1 = clauseForce + "   to_date('" + st_d1 + "','mm/dd/yyyy hh24:mi:ss ') , to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                    //pd_clu1 = clauseForce + "   to_date('" + p_st_d1 + "','mm/dd/yyyy hh24:mi:ss ') , to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";


                    d_clu1 = ActualClause;
                    pd_clu1 = ActualClause;

                } else {
                    ////////////////////////.println("st_d1"+st_d1);
                    if (PRG_ROLLING.equals("TWMONTHS")) {
                        d_clu1 = " between  convert(datetime,'" + ed_d1 + "',120) and  convert(datetime,'" + ed_d1 + "',120) ";
                        pd_clu1 = " between  convert(datetime,'" + p_ed_d1 + "',120) and  convert(datetime,'" + p_ed_d1 + "',120) ";
                    } else {
                        d_clu1 = " between  convert(datetime,'" + st_d1 + "',120) and  convert(datetime,'" + ed_d1 + "',120) ";
                        pd_clu1 = " between  convert(datetime,'" + p_st_d1 + "',120) and  convert(datetime,'" + p_ed_d1 + "',120) ";
                    }


                }





            }
        } else if (connType.equalsIgnoreCase("mysql")) {
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY")) {
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "30days(";
                } //added by Nazneen for Trailling As Period
                else if (totDays.contains("TPERIODS")) {
                    if (!timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        PRG_ROLLING = "TDAYS" + totDays.substring(8);
                    } else {
                        if (timeArray.get(3).toString().equalsIgnoreCase("MONTH")) {
                            PRG_ROLLING = "TMONTHS" + totDays.substring(8);

                        }

                        if (timeArray.get(3).toString().equalsIgnoreCase("DAY")) {
                            PRG_ROLLING = "TDAYS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("YEAR")) {
                            PRG_ROLLING = "TYEARS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("QTR")) {
                            PRG_ROLLING = "TQTRS" + totDays.substring(8);
                        }
                    }
                } else {
                    PRG_ROLLING = totDays;
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(st_date - ";
                strpart2 = " , date_format(date_sub( end_date , ";
                strpart3 = " , date_format(date_sub( st_date , ";
                if (PRG_ROLLING.contains("RDAYS")
                        || PRG_ROLLING.contains("TDAYS")
                        || PRG_ROLLING.contains("TMONTHS")
                        || PRG_ROLLING.contains("TWMONTHS")
                        || PRG_ROLLING.contains("TQTRS")
                        || PRG_ROLLING.contains("TYEARS")
                        || PRG_ROLLING.contains("RMONTHS")
                        || PRG_ROLLING.contains("LMONTHS")
                        || PRG_ROLLING.contains("RQTRS")
                        || PRG_ROLLING.contains("LQTRS")
                        || PRG_ROLLING.contains("RYEARS")
                        || PRG_ROLLING.contains("LYEARS")
                        || PRG_ROLLING.contains("DRANGE")
                        || PRG_ROLLING.contains("MRANGE")
                        || PRG_ROLLING.contains("QRANGE")
                        || PRG_ROLLING.contains("YRANGE")
                        || PRG_ROLLING.contains("DDIS")
                        || PRG_ROLLING.contains("MDIS")
                        || PRG_ROLLING.contains("QDIS")
                        || PRG_ROLLING.contains("YDIS")) {
                    if (PRG_ROLLING.contains("DRANGE")
                            || PRG_ROLLING.contains("MRANGE")
                            || PRG_ROLLING.contains("QRANGE")
                            || PRG_ROLLING.contains("YRANGE")) {

                        rollingLevel = "Year";//Can pickup any range ;start date and end date are used

                        PRG_ROLLING = PRG_ROLLING.replace("DRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
                        if (a.length == 1) {
                            a[1] = a[0];
                        } else if (a.length == 0) {
                            a[0] = param.getdateforpage();
                            a[1] = a[0];
                        }
                        ed_d1 = a[1] + " 23:59:59  ";
                        st_d1 = a[0] + " 00:00:00 ";
                        p_ed_d1 = a[1] + " 23:59:59 ";
                        p_st_d1 = a[0] + " 00:00:00 ";
                        executeQry = false;

                    } else if (PRG_ROLLING.contains("DDIS")
                            || PRG_ROLLING.contains("MDIS")
                            || PRG_ROLLING.contains("QDIS")
                            || PRG_ROLLING.contains("YDIS")) {
                        if (PRG_ROLLING.contains("DDIS")) {
                            rollingLevel = "Day";
                        } else if (PRG_ROLLING.contains("MDIS")) {
                            rollingLevel = "Month";
                        } else if (PRG_ROLLING.contains("QDIS")) {
                            rollingLevel = "Qtr";
                        } else if (PRG_ROLLING.contains("YDIS")) {
                            rollingLevel = "Year";
                        }


                        executeQry = false;
                        clauseForce = "in";

                        ActualClause = "  in ( Select distinct ddate from " + timeTableName + " where ";
                        if (PRG_ROLLING.contains("DDIS")) {
                            ActualClause += " ddate ";
                        } else if (PRG_ROLLING.contains("MDIS")) {
                            ActualClause += " date(cm_end_date) ";
                        } else if (PRG_ROLLING.contains("QDIS")) {
                            ActualClause += " date(cq_end_date) ";
                        } else if (PRG_ROLLING.contains("YDIS")) {
                            ActualClause += " date(cy_end_date) ";
                        } else {
                            ActualClause += " st_date ";
                        }

                        PRG_ROLLING = PRG_ROLLING.replace("DDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
                 //       String Ddate1 = "";
                        StringBuilder Ddate1=new StringBuilder();
                        for (int loop = 0; loop < a.length; loop++) {
                            if (loop == 0) {
                        //        Ddate1 = " str_to_date('" + a[loop] + "','%m/%d/%Y') ";
                                Ddate1.append(" str_to_date('").append(a[loop]).append("','%m/%d/%Y') ");
                            } else {
                         //       Ddate1 += " ,  str_to_date('" + a[loop] + "','%m/%d/%Y')";
                                Ddate1.append(" ,  str_to_date('").append(a[loop]).append("','%m/%d/%Y')");
                            }
                        }



                        ActualClause = ActualClause + " in ( " + Ddate1 + " )) ";
                    } else if (PRG_ROLLING.contains("RDAYS")
                            || PRG_ROLLING.contains("RMONTHS")
                            || PRG_ROLLING.contains("RQTRS")
                            || PRG_ROLLING.contains("RYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("RDAYS")) {
                            PRG_ROLLING = PRG_ROLLING.replace("RDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[1] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                a[1] = a[0];
                            }

                            str2 = "Select date_format(date_sub( end_date, INTERVAL  " + a[0] + " day ),'%m/%d/%Y %H:%i:%s') , date_format(end_date- " + a[1] + ",'%m/%d/%Y %H:%i:%s')  ";
                            strpart2 = " ,date_format(date_sub( end_date, INTERVAL " + a[0] + ", day)'%m/%d/%Y %H:%i:%s')  ";
                            strpart3 = " , date_format(date_sub( end_date, INTERVAL " + a[1] + " day) ,'%m/%d/%Y %H:%i:%s')  ";

                        }




                    } else if (PRG_ROLLING.contains("TDAYS")
                            || PRG_ROLLING.contains("TMONTHS")
                            || PRG_ROLLING.contains("TWMONTHS")
                            || PRG_ROLLING.contains("TQTRS")
                            || PRG_ROLLING.contains("TYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("TDAYS")) {
                            PRG_ROLLING = PRG_ROLLING.replace("TDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
//                               a[1]=a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
//                               a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select date_format(end_date- " + a[0] + ",'%m/%d/%Y %H:%i:%s ') , date_format(end_date- " + a[0] + ",'%m/%d/%Y %H:%i:%s ')  ";
                            strpart2 = " ,date_format(end_date- " + a[0] + ",'%m/%d/%Y %H:%i:%s ')  ";
                            strpart3 = " , date_format(end_date- " + a[0] + ",'%m/%d/%Y %H:%i:%s ')  ";
                            strpart4 = " from   " + timeTableName + " where ddate <= str_to_date('" + ref_date + "','%m/%d/%Y')  order by CM_END_DATE desc";

                        } else if (PRG_ROLLING.contains("TMONTHS") || PRG_ROLLING.contains("TWMONTHS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("TWMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct date_format(CM_END_DATE ,'%m/%d/%Y %H:%i:%s ') A1 , date_format(CM_ST_DATE,'%m/%d/%Y %H:%i:%s ') A2 ";
                            strpart2 = " ,date_format(CM_END_DATE ,'%m/%d/%Y %H:%i:%s ') A3 ";
                            strpart3 = " , date_format(CM_ST_DATE ,'%m/%d/%Y %H:%i:%s ') A4 ,CM_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cm_st_date <= str_to_date('" + ref_date + "','%m/%d/%Y')  order by CM_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TQTRS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TQTRS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct date_format(CQ_END_DATE ,'%m/%d/%Y %H:%i:%s ') A1 , date_format(CQ_ST_DATE,'%m/%d/%Y %H:%i:%s ') A2 ";
                            strpart2 = " ,date_format(CQ_END_DATE ,'%m/%d/%Y %H:%i:%s ') A3 ";
                            strpart3 = " , date_format(CQ_ST_DATE ,'%m/%d/%Y %H:%i:%s ') A4 ,CQ_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cq_st_date <= str_to_date('" + ref_date + "','%m/%d/%Y')  order by CQ_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TYEARS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TYEARS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct date_format(CY_END_DATE ,'%m/%d/%Y %H:%i:%s ') A1 , date_format(CY_ST_DATE,'%m/%d/%Y %H:%i:%s ') A2 ";
                            strpart2 = " ,date_format(CY_END_DATE ,'%m/%d/%Y %H:%i:%s ') A3 ";
                            strpart3 = " , date_format(CY_ST_DATE ,'%m/%d/%Y %H:%i:%s ') A4 ,CY_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cY_st_date <= str_to_date('" + ref_date + "','%m/%d/%Y')  order by CY_END_DATE desc";
                        }




                    }


                } else {
                    rollingLevel = "Year"; //can take from any slice
                    if (PRG_ROLLING.equalsIgnoreCase("30days(")) {
                        str2 += " INTERVAL 30 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL  31 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 61 day ),'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("60days(")) {
                        str2 += " INTERVAL 60 day ),'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 += " INTERVAL 61 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 121 day ),'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("90days(")) {
                        str2 += " INTERVAL 90 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL 91 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 181 day ),'%m/%d/%Y %H:%i:%s') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("180days(")) {
                        str2 += " INTERVAL 180 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL 181 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 361 day ),'%m/%d/%Y %H:%i:%s') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("365days(")) {
                        str2 += " INTERVAL 365 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL 366 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 721 day ),'%m/%d/%Y %H:%i:%s') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("366days(")) {
                        str2 += " INTERVAL 366 day ) ,'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL 367 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 723 day ),'%m/%d/%Y %H:%i:%s') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("45days(")) {
                        str2 += " INTERVAL 45 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart2 += " INTERVAL 46 day ),'%m/%d/%Y %H:%i:%s') ";
                        strpart3 += " INTERVAL 91 day ),'%m/%d/%Y %H:%i:%s') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("WTD(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(CW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PW_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PWTD(")) {
                        str2 = "Select date_format(PW_DAY,'%m/%d/%Y %H:%i:%s') , date_format(PW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PW_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYWTD(")) {
                        str2 = "Select date_format(LYW_DAY,'%m/%d/%Y %H:%i:%s') , date_format(LYW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LYW_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LYW_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTD(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(CM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTDW(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(end_date,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("QTD(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(CQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LQ_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("YTD(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , date_format(CY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LY_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("CYD(")) {
                        str2 = "Select date_format(CY_END_DATE,'%m/%d/%Y %H:%i:%s') , date_format(CY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LY_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("LTD(")) {
                        str2 = "Select date_format(end_date,'%m/%d/%Y %H:%i:%s') , '01/01/1980 00:00:00' A2  ";
                        strpart2 = " ,date_format(LY_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PMTD(")) {
                        str2 = "Select date_format(PM_DAY,'%m/%d/%Y %H:%i:%s') , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PQTD(")) {
                        str2 = "Select date_format(LQ_DAY,'%m/%d/%Y %H:%i:%s') , date_format(LQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LQ_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYTD(")) {
                        str2 = "Select date_format(LY_DAY,'%m/%d/%Y %H:%i:%s') , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LY_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYMTD(")) {
                        str2 = "Select date_format(PYM_DAY,'%m/%d/%Y %H:%i:%s') , date_format(PYM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYQTD(")) {
                        str2 = "Select date_format(LYQ_DAY,'%m/%d/%Y %H:%i:%s') , date_format(LYQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LYQ_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LYQ_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("1month(")) {
                        str2 = "Select date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s') , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(PM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("2month(")) {
                        str2 = "Select date_format(CM_END_DATE,'%m/%d/%Y %H:%i:%s') , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(CM_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(PM_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart4 = " from   " + timeTableName + "  where ddate = (select PM_DAY from   " + timeTableName + "  where ddate = str_to_date('" + ref_date + "','%m/%d/%Y') ) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("3month(")) {
                        str2 = " select date_format(max(end_date),'%m/%d/%Y %H:%i:%s') end_date ,date_format(min(st_date),'%m/%d/%Y %H:%i:%s') st_date ,max (end_date) end_date1 ,min(st_date) st_date1   from( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  str_to_date('" + ref_date + "','%m/%d/%Y') -92  and str_to_date('" + ref_date + "','%m/%d/%Y')  )";
                        strpart4 = " ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("6month(")) {
                        str2 = " select date_format(max(end_date),'%m/%d/%Y %H:%i:%s') end_date ,date_format(min(st_date),'%m/%d/%Y %H:%i:%s') st_date ,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  str_to_date('" + ref_date + "','%m/%d/%Y') -183 and str_to_date('" + ref_date + "','%m/%d/%Y') )";
                        strpart4 = " ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("1year(")) {
                        str2 = " select date_format(max(end_date),'%m/%d/%Y %H:%i:%s') end_date ,date_format(min(st_date),'%m/%d/%Y %H:%i:%s') st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  str_to_date('" + ref_date + "','%m/%d/%Y') -365  and str_to_date('" + ref_date + "','%m/%d/%Y')  )";
                        strpart4 = " ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("2year(")) {
                        str2 = " select date_format(max(end_date),'%m/%d/%Y %H:%i:%s') end_date ,date_format(min(st_date),'%m/%d/%Y %H:%i:%s') st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  str_to_date('" + ref_date + "','%m/%d/%Y') -700  and str_to_date('" + ref_date + "','%m/%d/%Y')  )";
                        strpart4 = " ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("NYD(")) {
                        str2 = "Select date_format(NY_END_DATE,'%m/%d/%Y %H:%i:%s') , date_format(NY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart2 = " ,date_format(LY_END_DATE,'%m/%d/%Y %H:%i:%s')  ";
                        strpart3 = " , date_format(LY_ST_DATE,'%m/%d/%Y %H:%i:%s')  ";

                    }

                }

                ////////////////////////.println("strpart4 is "+ strpart4);
                if (strpart4 == null) {
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = str_to_date('" + ref_date + "','%m/%d/%Y') ";
                } else {
                    str2 = str2 + strpart2 + strpart3 + strpart4;
                }

                if (executeQry) {
                    try {
                        con = getConnection(elementID);

                        st2 = con.prepareCall(str2);

                        rs2 = st2.executeQuery();

//                    while (rs2.next()) {
//                        ed_d1 = rs2.getString(1);
//                        st_d1 = rs2.getString(2);
//                        p_ed_d1 = rs2.getString(3);
//                        p_st_d1 = rs2.getString(4);
//
//
//                        //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
//                    }

                        if (!trailing) {
                            while (rs2.next()) {
                                ed_d1 = rs2.getString(1);
                                st_d1 = rs2.getString(2);
                                p_ed_d1 = rs2.getString(3);
                                p_st_d1 = rs2.getString(4);


                                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                            }
                        } else {
                            PbReturnObject pb = null;
                            if (rs2 != null) {
                                pb = new PbReturnObject(rs2);
                            }

                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (pb.rowCount >= trailCount) {
                                ed_d1 = pb.getFieldValueString(trailCount, 0);
                                st_d1 = pb.getFieldValueString(trailCount, 1);
                                p_ed_d1 = pb.getFieldValueString(trailCount, 2);
                                p_st_d1 = pb.getFieldValueString(trailCount, 3);
                            } else {
                                ed_d1 = ref_date + " 00:00:00";
                                st_d1 = ref_date + " 23:00:00";
                                p_ed_d1 = ref_date + " 00:00:00";
                                p_st_d1 = ref_date + " 23:00:00";
                            }
                        }
                    } catch (SQLException e) {
//                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
//                    logger.error("Exception:",e);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (st2 != null) {
                            st2.close();
                        }

                        if (con != null) {
                            con.close();
                        }
                    }

                }

                ////////////////////////.println("st_d1"+st_d1);
                if (clauseForce.equalsIgnoreCase("BETWEEN")) {
                    if (PRG_ROLLING.contains("TWMONTHS")) {
                        d_clu1 = "  " + clauseForce + "   str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                        pd_clu1 = "  " + clauseForce + "   str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') ";

                    } else {
                        d_clu1 = "  " + clauseForce + "   str_to_date('" + st_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                        pd_clu1 = "  " + clauseForce + "   str_to_date('" + p_st_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') ";

                    }

                } else if (clauseForce.equalsIgnoreCase("in")) {
                    // d_clu1 = clauseForce + "   str_to_date('" + st_d1 + "','%m/%d/%Y %H:%i:%s') , str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                    //pd_clu1 = clauseForce + "   str_to_date('" + p_st_d1 + "','%m/%d/%Y %H:%i:%s') , str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                    d_clu1 = ActualClause;
                    pd_clu1 = ActualClause;

                } else {
                    if (PRG_ROLLING.contains("TWMONTHS")) {
                        d_clu1 = "  " + clauseForce + "   str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                        pd_clu1 = "  " + clauseForce + "   str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') ";

                    } else {
                        d_clu1 = "  " + clauseForce + "   str_to_date('" + st_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + ed_d1 + "','%m/%d/%Y %H:%i:%s') ";
                        pd_clu1 = "  " + clauseForce + "   str_to_date('" + p_st_d1 + "','%m/%d/%Y %H:%i:%s') and  str_to_date('" + p_ed_d1 + "','%m/%d/%Y %H:%i:%s') ";

                    }

                }

            }
        } else if (connType.equalsIgnoreCase("PostgreSQL")) {
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY")) {

                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "30days(";
                } //added by Nazneen for Trailling As Period
                else if (totDays.contains("TPERIODS")) {
                    if (!timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        PRG_ROLLING = "TDAYS" + totDays.substring(8);
                    } else {
                        if (timeArray.get(3).toString().equalsIgnoreCase("MONTH")) {
                            PRG_ROLLING = "TMONTHS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("DAY")) {
                            PRG_ROLLING = "TDAYS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("YEAR")) {
                            PRG_ROLLING = "TYEARS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("QTR")) {
                            PRG_ROLLING = "TQTRS" + totDays.substring(8);
                        }
                    }
                } else {
                    PRG_ROLLING = totDays;
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(st_date - ";
                strpart2 = " , to_char(end_date - ";
                strpart3 = " , to_char(st_date - ";

                if (PRG_ROLLING.equalsIgnoreCase("30days(")) {
                    str2 += " interval '30 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '31 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '61 days','mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("60days(")) {
                    str2 += " interval '60 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '61 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '121 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("90days(")) {
                    str2 += " interval '90 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '91 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '181 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("180days(")) {
                    str2 += " interval '180 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '181 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '361 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("365days(")) {
                    str2 += " interval '365 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '366 days','mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '721 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("366days(")) {
                    str2 += " interval '366 days','mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '367 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '723 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("45days(")) {
                    str2 += " interval '45 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 += " interval '46 days' ,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 += " interval '91 days' ,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("WTD(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(PW_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PWTD(")) {
                    str2 = "Select to_char(PW_DAY,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(PW_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PYWTD(")) {
                    str2 = "Select to_char(LYW_DAY,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(LYW_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LYW_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("MTD(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("MTDW(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("QTD(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("YTD(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("CYD(")) {
//                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    str2 = "Select to_char(CY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("LTD(")) {
                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , '01/01/1980 00:00:00' A2 ";
                    strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PMTD(")) {
                    str2 = "Select to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PQTD(")) {
                    str2 = "Select to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 = " ,to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 = " , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PYTD(")) {
                    str2 = "Select to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PYMTD(")) {
                    str2 = "Select to_char(PYM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("PYQTD(")) {
                    str2 = "Select to_char(LYQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart2 = " ,to_char(LYQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    strpart3 = " , to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                } else if (PRG_ROLLING.equalsIgnoreCase("1month(")) {
                    str2 = "Select to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                } else if (PRG_ROLLING.equalsIgnoreCase("2month(")) {
                    str2 = "Select to_char(CM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(CM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart4 = " from " + timeTableName + " where ddate = (select PM_DAY from " + timeTableName + " where ddate = to_date('" + ref_date + "','MM/dd/yyyy') ) ";

                } else if (PRG_ROLLING.equalsIgnoreCase("3month(")) {
                    str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date ,max (end_date) end_date1 ,min(st_date) st_date1 from( ";
                    strpart2 = " select distinct CM_END_DATE end_date, CM_ST_DATE st_date from " + timeTableName + " where CM_END_DATE between ";
                    strpart3 = " to_date('" + ref_date + "','MM/dd/yyyy') -interval '92 days' and to_date('" + ref_date + "','MM/dd/yyyy') )";
                    strpart4 = " ";
                } else if (PRG_ROLLING.equalsIgnoreCase("6month(")) {
                    str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date ,max (end_date) end_date1 ,min(st_date) st_date1 from ( ";
                    strpart2 = " select distinct CM_END_DATE end_date, CM_ST_DATE st_date from " + timeTableName + " where CM_END_DATE between ";
                    strpart3 = " to_date('" + ref_date + "','MM/dd/yyyy') -interval '183 days' and to_date('" + ref_date + "','MM/dd/yyyy') )";
                    strpart4 = " ";
                } else if (PRG_ROLLING.equalsIgnoreCase("1year(")) {
                    str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date,max (end_date) end_date1 ,min(st_date) st_date1 from ( ";
                    strpart2 = " select distinct CM_END_DATE end_date, CM_ST_DATE st_date from " + timeTableName + " where CM_END_DATE between ";
                    strpart3 = " to_date('" + ref_date + "','MM/dd/yyyy') -interval '365 days' and to_date('" + ref_date + "','MM/dd/yyyy') )";
                    strpart4 = " ";
                } else if (PRG_ROLLING.equalsIgnoreCase("2year(")) {
                    str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date,max (end_date) end_date1 ,min(st_date) st_date1 from ( ";
                    strpart2 = " select distinct CM_END_DATE end_date, CM_ST_DATE st_date from " + timeTableName + " where CM_END_DATE between ";
                    strpart3 = " to_date('" + ref_date + "','MM/dd/yyyy') -interval '700 days' and to_date('" + ref_date + "','MM/dd/yyyy') )";
                    strpart4 = " ";

                } else if (PRG_ROLLING.equalsIgnoreCase("NYD(")) {
//                    str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    str2 = "Select to_char(NY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(NY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";

                }
////////////////.println("strpart4 is "+ strpart4);
                if (strpart4 == null) {
                    str2 = str2 + strpart2 + strpart3 + " from " + timeTableName + " where ddate = to_timestamp('" + ref_date + "','MM/dd/yyyy') ";
                } else {
                    str2 = str2 + strpart2 + strpart3 + strpart4;
                }

                //////.println("time Q" + str2);
                try {
                    con = getConnection(elementID);

                    st2 = con.prepareCall(str2);

                    rs2 = st2.executeQuery();

                    while (rs2.next()) {
                        ed_d1 = rs2.getString(1);
                        st_d1 = rs2.getString(2);
                        p_ed_d1 = rs2.getString(3);
                        p_st_d1 = rs2.getString(4);
//out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                    }
                } catch (SQLException e) {
                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                    logger.error("Exception:", e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st2 != null) {
                        st2.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }

////////////////.println("st_d1"+st_d1);
if(date_type!=null && !date_type.equalsIgnoreCase("") && date_type.equalsIgnoreCase("snap")){
                d_clu1 = " = to_timestamp('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu1 = " = to_timestamp('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
}else{
                d_clu1 = " between to_timestamp('" + st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and to_timestamp('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu1 = " between to_timestamp('" + p_st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and to_timestamp('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
            }

            }
        } else {
            if (timeArray.get(0).toString().equalsIgnoreCase("DAY")) {
                if (timeArray.get(3) == null) {
                    PRG_ROLLING = "30days(";
                } //added by Nazneen for Trailling As Period
                else if (totDays.contains("TPERIODS")) {
                    if (!timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                        PRG_ROLLING = "TDAYS" + totDays.substring(8);
                    } else {
                        if (timeArray.get(3).toString().equalsIgnoreCase("MONTH")) {
                            PRG_ROLLING = "TMONTHS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("DAY")) {
                            PRG_ROLLING = "TDAYS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("YEAR")) {
                            PRG_ROLLING = "TYEARS" + totDays.substring(8);
                        }
                        if (timeArray.get(3).toString().equalsIgnoreCase("QTR")) {
                            PRG_ROLLING = "TQTRS" + totDays.substring(8);
                        }
                    }
                } else {
                    PRG_ROLLING = totDays;
                }
                if (timeArray.get(2) == null) {
                    ref_date = param.getdateforpage();
                } else if (timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    ref_date = timeArray.get(3).toString();
                } else {
                    ref_date = timeArray.get(2).toString();
                }

                str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(st_date - ";
                strpart2 = " , to_char(end_date - ";
                strpart3 = " , to_char(st_date - ";
                if (PRG_ROLLING.contains("RDAYS")
                        || PRG_ROLLING.contains("TDAYS")
                        || PRG_ROLLING.contains("TMONTHS")
                        || PRG_ROLLING.contains("TWMONTHS")
                        || PRG_ROLLING.contains("TQTRS")
                        || PRG_ROLLING.contains("TYEARS")
                        || PRG_ROLLING.contains("RMONTHS")
                        || PRG_ROLLING.contains("LMONTHS")
                        || PRG_ROLLING.contains("RQTRS")
                        || PRG_ROLLING.contains("LQTRS")
                        || PRG_ROLLING.contains("RYEARS")
                        || PRG_ROLLING.contains("LYEARS")
                        || PRG_ROLLING.contains("DRANGE")
                        || PRG_ROLLING.contains("MRANGE")
                        || PRG_ROLLING.contains("QRANGE")
                        || PRG_ROLLING.contains("YRANGE")
                        || PRG_ROLLING.contains("DDIS")
                        || PRG_ROLLING.contains("MDIS")
                        || PRG_ROLLING.contains("QDIS")
                        || PRG_ROLLING.contains("YDIS")) {
                    if (PRG_ROLLING.contains("DRANGE")
                            || PRG_ROLLING.contains("MRANGE")
                            || PRG_ROLLING.contains("QRANGE")
                            || PRG_ROLLING.contains("YRANGE")) {

                        rollingLevel = "Year";//Can pickup any range ;start date and end date are used

                        PRG_ROLLING = PRG_ROLLING.replace("DRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YRANGE", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
                        if (a.length == 1) {
                            a[1] = a[0];
                        } else if (a.length == 0) {
                            a[0] = param.getdateforpage();
                            a[1] = a[0];
                        }
                        ed_d1 = a[1] + " 23:59:59  ";
                        st_d1 = a[0] + " 00:00:00 ";
                        p_ed_d1 = a[1] + " 23:59:59 ";
                        p_st_d1 = a[0] + " 00:00:00 ";
                        executeQry = false;

                    } else if (PRG_ROLLING.contains("DDIS")
                            || PRG_ROLLING.contains("MDIS")
                            || PRG_ROLLING.contains("QDIS")
                            || PRG_ROLLING.contains("YDIS")) {
                        if (PRG_ROLLING.contains("DDIS")) {
                            rollingLevel = "Day";
                        } else if (PRG_ROLLING.contains("MDIS")) {
                            rollingLevel = "Month";
                        } else if (PRG_ROLLING.contains("QDIS")) {
                            rollingLevel = "Qtr";
                        } else if (PRG_ROLLING.contains("YDIS")) {
                            rollingLevel = "Year";
                        }


                        executeQry = false;
                        clauseForce = "in";

                        ActualClause = "  in ( Select distinct ddate from pr_day_denom where ";
                        if (PRG_ROLLING.contains("DDIS")) {
                            ActualClause += " ddate ";
                        } else if (PRG_ROLLING.contains("MDIS")) {
                            ActualClause += " trunc(cm_end_date) ";
                        } else if (PRG_ROLLING.contains("QDIS")) {
                            ActualClause += " trunc(cq_end_date) ";
                        } else if (PRG_ROLLING.contains("YDIS")) {
                            ActualClause += " trunc(cy_end_date) ";
                        } else {
                            ActualClause += " st_date ";
                        }

                        PRG_ROLLING = PRG_ROLLING.replace("DDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("MDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("QDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("YDIS", "");
                        PRG_ROLLING = PRG_ROLLING.replace("(", "");
                        String a[] = PRG_ROLLING.split(",");
       //                 String Ddate1 = "";
                        StringBuilder Ddate1=new StringBuilder();
                        for (int loop = 0; loop < a.length; loop++) {
                            if (loop == 0) {
                                Ddate1.append(" to_date('").append(a[loop]).append( "','mm/dd/yyyy') ");
                            } else {
                                Ddate1.append(" ,  to_date('").append(a[loop]).append( "','mm/dd/yyyy')");
                            }
                        }



                        ActualClause = ActualClause + " in ( " + Ddate1 + " )) ";
                    } else if (PRG_ROLLING.contains("RDAYS")
                            || PRG_ROLLING.contains("RMONTHS")
                            || PRG_ROLLING.contains("RQTRS")
                            || PRG_ROLLING.contains("RYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("RDAYS")) {
                            PRG_ROLLING = PRG_ROLLING.replace("RDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[1] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                a[1] = a[0];
                            }

                            str2 = "Select to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date- " + a[1] + ",'mm/dd/yyyy hh24:mi:ss ')  ";
                            strpart2 = " ,to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ')  ";
                            strpart3 = " , to_char(end_date- " + a[1] + ",'mm/dd/yyyy hh24:mi:ss ')  ";

                        }
                    } else if (PRG_ROLLING.contains("TDAYS")
                            || PRG_ROLLING.contains("TMONTHS")
                            || PRG_ROLLING.contains("TWMONTHS")
                            || PRG_ROLLING.contains("TQTRS")
                            || PRG_ROLLING.contains("TYEARS")) {


                        rollingLevel = "Year"; //Can take from any slice



//                       PRG_ROLLING= PRG_ROLLING.replace("MRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("QRANGE", "");
//                       PRG_ROLLING= PRG_ROLLING.replace("YRANGE", "");


                        if (PRG_ROLLING.contains("TDAYS")) {
                            PRG_ROLLING = PRG_ROLLING.replace("TDAYS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
//                               a[1]=a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
//                               a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ')  ";
                            strpart2 = " ,to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ')  ";
                            strpart3 = " , to_char(end_date- " + a[0] + ",'mm/dd/yyyy hh24:mi:ss ') ,end_date ";
                            strpart4 = " from   " + timeTableName + " where ddate <= to_date('" + ref_date + "','MM/dd/yyyy')  order by end_date desc";

                        } else if (PRG_ROLLING.contains("TMONTHS") || PRG_ROLLING.contains("TWMONTHS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("TWMONTHS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct to_char(CM_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A1 , to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') A2 ";
                            strpart2 = " ,to_char(CM_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A3 ";
                            strpart3 = " , to_char(CM_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') A4 ,CM_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cm_st_date <= to_date('" + ref_date + "','MM/dd/yyyy')  order by CM_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TQTRS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TQTRS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct to_char(CQ_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A1 , to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') A2 ";
                            strpart2 = " ,to_char(CQ_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A3 ";
                            strpart3 = " , to_char(CQ_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') A4 ,CQ_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cq_st_date <= to_date('" + ref_date + "','MM/dd/yyyy')  order by CQ_END_DATE desc";
                        } else if (PRG_ROLLING.contains("TYEARS")) {

                            PRG_ROLLING = PRG_ROLLING.replace("TYEARS", "");
                            PRG_ROLLING = PRG_ROLLING.replace("(", "");
                            String a[] = PRG_ROLLING.split(",");
                            if (a.length == 1) {
                                a[0] = a[0];
                            } else if (a.length == 0) {
                                a[0] = "0";
                                //a[1]=a[0];
                            }
                            trailing = true;
                            trailCount = Integer.parseInt(a[0]);
                            str2 = "Select distinct to_char(CY_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A1 , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') A2 ";
                            strpart2 = " ,to_char(CY_END_DATE ,'mm/dd/yyyy hh24:mi:ss ') A3 ";
                            strpart3 = " , to_char(CY_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') A4 ,CY_END_DATE  ";
                            strpart4 = " from   " + timeTableName + " where cY_st_date <= to_date('" + ref_date + "','MM/dd/yyyy')  order by CY_END_DATE desc";
                        }
                    }

                } else {
                    rollingLevel = "Year"; //can take from any slice
                    if (PRG_ROLLING.equalsIgnoreCase("30days(")) {
                        str2 += " 30 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 31 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 61 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("60days(")) {
                        str2 += " 60 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 += " 61 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 121 ,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("90days(")) {
                        str2 += " 90 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 91 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 181 ,'mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("180days(")) {
                        str2 += " 180 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 181 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 361 ,'mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("365days(")) {
                        str2 += " 365 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 366 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 721 ,'mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("366days(")) {
                        str2 += " 366 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 367 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 723 ,'mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("45days(")) {
                        str2 += " 45 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart2 += " 46 ,'mm/dd/yyyy hh24:mi:ss ') ";
                        strpart3 += " 91 ,'mm/dd/yyyy hh24:mi:ss ') ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTD(")) {
                        str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("MTDW(")) {
                        str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("QTD(")) {
                        str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PMTD(")) {
                        str2 = "Select to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PQTD(")) {
                        str2 = "Select to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYTD(")) {
                        str2 = "Select to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYMTD(")) {
                        str2 = "Select to_char(PYM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("PYQTD(")) {
                        str2 = "Select to_char(LYQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LYQ_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("YTD(")) {
                        str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("CYD(")) {
                        str2 = "Select to_char(CY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("LTD(")) {
                        str2 = "Select to_char(end_date,'mm/dd/yyyy hh24:mi:ss ') , '01/01/1980 00:00:00' A2 ";
                        strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("1month(")) {
                        str2 = "Select to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(PM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("2month(")) {
                        str2 = "Select to_char(CM_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(CM_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart4 = " from   " + timeTableName + "  where ddate = (select PM_DAY from   " + timeTableName + "  where ddate = to_date('" + ref_date + "','MM/dd/yyyy') ) ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("3month(")) {
                        str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date ,max (end_date) end_date1 ,min(st_date) st_date1   from( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  to_date('" + ref_date + "','MM/dd/yyyy') -92  and to_date('" + ref_date + "','MM/dd/yyyy')  )";
                        strpart4 = " ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("6month(")) {
                        str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date ,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  to_date('" + ref_date + "','MM/dd/yyyy') -183 and to_date('" + ref_date + "','MM/dd/yyyy') )";
                        strpart4 = " ";
                    } else if (PRG_ROLLING.equalsIgnoreCase("1year(")) {
                        str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  to_date('" + ref_date + "','MM/dd/yyyy') -341  and to_date('" + ref_date + "','MM/dd/yyyy')  )";
                        strpart4 = " ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("2year(")) {
                        str2 = " select to_char(max(end_date),'mm/dd/yyyy hh24:mi:ss ') end_date ,to_char(min(st_date),'mm/dd/yyyy hh24:mi:ss ') st_date,max (end_date) end_date1 ,min(st_date) st_date1  from ( ";
                        strpart2 = "  select distinct CM_END_DATE end_date, CM_ST_DATE st_date  from  " + timeTableName + "  where CM_END_DATE between  ";
                        strpart3 = "  to_date('" + ref_date + "','MM/dd/yyyy') -724  and to_date('" + ref_date + "','MM/dd/yyyy')  )";
                        strpart4 = " ";

                    } else if (PRG_ROLLING.equalsIgnoreCase("NYD(")) {
                        str2 = "Select to_char(NY_END_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(NY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart2 = " ,to_char(LY_END_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                        strpart3 = " , to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    }

                }

                ////////////////////////.println("strpart4 is "+ strpart4);
                if (strpart4 == null) {
                    str2 = str2 + strpart2 + strpart3 + " from   " + timeTableName + "  where ddate = to_date('" + ref_date + "','MM/dd/yyyy') ";
                } else {
                    str2 = str2 + strpart2 + strpart3 + strpart4;
                }

                if (executeQry) {
                    try {
                        con = getConnection(elementID);

                        st2 = con.prepareCall(str2);

                        rs2 = st2.executeQuery();


                        if (!trailing) {
                            while (rs2.next()) {
                                ed_d1 = rs2.getString(1);
                                st_d1 = rs2.getString(2);
                                p_ed_d1 = rs2.getString(3);
                                p_st_d1 = rs2.getString(4);


                                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                            }
                        } else {
                            PbReturnObject pb = null;
                            if (rs2 != null) {
                                pb = new PbReturnObject(rs2);
                            }

                            if (rs2 != null) {
                                rs2.close();
                            }
                            if (pb.rowCount >= trailCount) {
                                ed_d1 = pb.getFieldValueString(trailCount, 0);
                                st_d1 = pb.getFieldValueString(trailCount, 1);
                                p_ed_d1 = pb.getFieldValueString(trailCount, 2);
                                p_st_d1 = pb.getFieldValueString(trailCount, 3);
                            } else {
                                ed_d1 = ref_date + " 00:00:00";
                                st_d1 = ref_date + " 23:00:00";
                                p_ed_d1 = ref_date + " 00:00:00";
                                p_st_d1 = ref_date + " 23:00:00";
                            }
                        }
                    } catch (SQLException e) {
//                    System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
//                    logger.error("Exception:",e);
                    } finally {
                        if (rs2 != null) {
                            rs2.close();
                        }
                        if (st2 != null) {
                            st2.close();
                        }

                        if (con != null) {
                            con.close();
                        }
                    }

                }
//                //start of code by Nazneen on 4 April 2014
//                String[] dateArrEdD = null;
//                String[] dateArrPEdD = null;
//                dateArrEdD = ed_d.split(" ");
//                dateArrPEdD = p_ed_d.split(" ");
//                ed_d = dateArrEdD[0] + " " + "23:59:59 ";
//                p_ed_d = dateArrPEdD[0] + " " + "23:59:59 ";
//            //End of code by Nazneen on 4 April 2014
                ////////////////////////.println("st_d1"+st_d1);
                if (clauseForce.equalsIgnoreCase("BETWEEN")) {
                    if (PRG_ROLLING.contains("TWMONTHS")) {
                        d_clu1 = "  " + clauseForce + "   to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                        pd_clu1 = "  " + clauseForce + "   to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";

                    } else {
                        d_clu1 = "  " + clauseForce + "   to_date('" + st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                        pd_clu1 = "  " + clauseForce + "   to_date('" + p_st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";

                    }

                } else if (clauseForce.equalsIgnoreCase("in")) {
                    // d_clu1 = clauseForce + "   to_date('" + st_d1 + "','mm/dd/yyyy hh24:mi:ss ') , to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                    //pd_clu1 = clauseForce + "   to_date('" + p_st_d1 + "','mm/dd/yyyy hh24:mi:ss ') , to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                    d_clu1 = ActualClause;
                    pd_clu1 = ActualClause;

                } else {
                    if (PRG_ROLLING.contains("TWMONTHS")) {
                        d_clu1 = "  " + clauseForce + "   to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                        pd_clu1 = "  " + clauseForce + "   to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";

                    } else {
                        d_clu1 = "  " + clauseForce + "   to_date('" + st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";
                        pd_clu1 = "  " + clauseForce + "   to_date('" + p_st_d1 + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d1 + "','mm/dd/yyyy hh24:mi:ss ') ";

                    }

                }

            }
        } ////End of orcale else
////////////////.println("d_clu1 "+d_clu1);

        return (d_clu1);

    }

    public void setMonthRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String getmonth) throws SQLException {



        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null;
        String timeInterval = null;
        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        try {
            GetConnectionType getconntype = new GetConnectionType();
            connType = getconntype.getConTypeByElementId(elementID);
            if (connType.equalsIgnoreCase("PostgreSQL")) {
                interval = "";
                timeInterval = "to_timestamp";
            } else {
                interval = "+.99999";
                timeInterval = "to_date";
            }
            //con = DriverManager.getConnection (URL, username, password);
            //   Class.forName("oracle.jdbc.driver.OracleDriver");
            ////.println("trend range is called");
            str2 = "select to_char(CM_END_DATE ,'mm/dd/yyyy hh24:mi:ss '), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                        str2 = str2 + " to_char(CM_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PM_END_DATE) " + interval + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PYM_END_DATE) " + interval + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                        str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PM_END_DATE) " + interval + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else {
                        str2 = str2 + " to_char(CM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PYY_END_DATE) " + interval + ",'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PM_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(PMYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else {
                        str2 = str2 + " to_char(CMQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMYQ_ST_DATE  ,'mm/dd/yyyy hh24:mi:ss ') ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " to_char(CMY_START_DATE ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE,'mm/dd/yyyy hh24:mi:ss ')   ";
                    } else {
                        str2 = str2 + " to_char(CMY_START_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE,'mm/dd/yyyy hh24:mi:ss ')   ";
                    }
                }
            }
            str2 = str2 + " from  prg_acn_mon_denom where MON_code = '" + getmonth + "' ";

            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
        pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";







    }
    /////////////////////////

    public void setTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {

        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "", interval = null, timeInterval = null, toChar = null, dateFormat = null, dateType = null,
                onlyDate = null, onlymysql = null, periodType = null, mysqlInterval = null;
        String addClause = " ";
        GetConnectionType getconntype = new GetConnectionType();
        connType = getconntype.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("PostgreSQL")) {
            onlyDate = "MM/dd/yyyy";
            dateType = "to_date";
            toChar = "to_char";
            dateFormat = "mm/dd/yyyy hh24:mi:ss";
            interval = "";
            onlymysql = "";
            periodType = "-";
            mysqlInterval = "";
            timeInterval = "to_timestamp";
        } else if (connType.equalsIgnoreCase("mysql")) {

            onlyDate = "%m/%d/%Y";
            dateType = "str_to_date";
            toChar = "date_format";
            onlymysql = "(DATE_SUB";
            mysqlInterval = "DAY)";
            periodType = ",interval";
            interval = "";
            timeInterval = "str_to_date";
            dateFormat = "%m/%d/%Y %H:%i:%s";
        } else {
            interval = "+.99999";
            timeInterval = "to_date";
        }
        if (connType.equalsIgnoreCase("MySql") || connType.equalsIgnoreCase("Postgres")) {
            try {
                str2 = "select " + toChar + "(end_date ,'" + dateFormat + " '), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " " + toChar + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + toChar + "(LYW_DAY ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LYW_DAY " + periodType + "30" + mysqlInterval + ",'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + toChar + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + toChar + "(LYW_DAY ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LYW_DAY " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " " + toChar + onlymysql + "(CW_ST_DATE " + periodType + "91" + mysqlInterval + " ,'" + dateFormat + " ') , " + toChar + "(LYW_DAY ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LYW_ST_DATE " + periodType + "91" + mysqlInterval + ",'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + toChar + onlymysql + "(CW_ST_DATE " + periodType + "91" + mysqlInterval + " ,'" + dateFormat + " ') , " + toChar + "(LYW_END_DATE ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LYW_ST_DATE " + periodType + "91" + mysqlInterval + ",'" + dateFormat + " ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " " + toChar + onlymysql + "(CM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(PYM_DAY " + interval + " ,'" + dateFormat + " ') , " + toChar + onlymysql + "(PYM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') ";

                            if (connType.equalsIgnoreCase("MySql")) {
                                addClause = " and date_format(PROGEN_TIME.ddate,'%d') *1 <= " + d1.substring(3, 5) + " ";
                            } else if (connType.equalsIgnoreCase("Postgres")) {
                                addClause = " and to_char(PROGEN_TIME.ddate,'dd') *1 <= " + d1.substring(3, 5) + " ";
                            }

                        } else {
                            str2 = str2 + " " + toChar + onlymysql + "(CM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(PYM_END_DATE " + interval + " ,'" + dateFormat + " ') , " + toChar + onlymysql + "(PYM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " " + toChar + onlymysql + "(CQ_ST_DATE " + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(LYQ_DAY ,'" + dateFormat + " '), " + toChar + onlymysql + "(LYQ_ST_DATE" + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + toChar + onlymysql + "(CQ_ST_DATE " + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(LYQ_END_DATE ,'" + dateFormat + " '), " + toChar + onlymysql + "(LYQ_ST_DATE" + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " " + toChar + onlymysql + "(CY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(LY_DAY ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                        } else {
                            str2 = str2 + " " + toChar + onlymysql + "(CY_ST_DATE" + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + toChar + "(LY_END_DATE ,'" + dateFormat + " ') , " + toChar + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                        }
                    }
                }
                str2 = str2 + " from " + timeTableName + " where ddate = " + dateType + "('" + d1 + "','" + onlyDate + "') ";
                //str2 = str2 + addClause;


                con = getConnection(elementID);
                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


//out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                logger.error("Exception:", e);
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between " + timeInterval + "('" + st_d + "','" + dateFormat + " ') and " + timeInterval + "('" + ed_d + "','" + dateFormat + " ')   " + addClause;
            pd_clu = " between " + timeInterval + "('" + p_st_d + "','" + dateFormat + " ') and " + timeInterval + "('" + p_ed_d + "','" + dateFormat + " ')  " + addClause;




        }
        if (connType.equalsIgnoreCase("Sqlserver")) {
            try {
                str2 = "select convert(varchar,end_date ,120)  A0, ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                            str2 = str2 + " convert(varchar,ST_DATE -15 ,120)  A2 , convert(varchar,LYW_DAY   ,120) A3 , convert(varchar,LYW_DAY -15 ,120) A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,ST_DATE -15 ,120)  A2, convert(varchar,LYW_DAY   ,120) A3 , convert(varchar,LYW_DAY -15 ,120)  A4";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) A2, convert(varchar,LYW_DAY   ,120) A3 , convert(varchar,LYW_ST_DATE -91,120) A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) A2, convert(varchar,LYW_END_DATE   ,120)  A3 , convert(varchar,LYW_ST_DATE -91,120) A4 ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " convert(varchar,CM_ST_DATE -365,120) A2, convert(varchar,PYM_DAY " + interval + " ,120) A3,  convert(varchar,PYM_ST_DATE -365,120) A4 ";
                            addClause = " and DATEPART(day,PROGEN_TIME.ddate) <= " + d1.substring(3, 5) + " ";

                        } else {
                            str2 = str2 + " convert(varchar,CM_ST_DATE -365,120) A2, convert(varchar,PYM_END_DATE " + interval + " ,120) A3,  convert(varchar,PYM_ST_DATE -365,120) A4 ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) A2, convert(varchar,LYQ_DAY   ,120) A3,  convert(varchar,LYQ_ST_DATE-730,120) A4 ";
                        } else {
                            str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) A2, convert(varchar,LYQ_END_DATE   ,120) A3,  convert(varchar,LYQ_ST_DATE-730,120) A4 ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A2, convert(varchar,LY_DAY  ,120) A3, convert(varchar,LY_ST_DATE -1460,120)  A4  ";
                        } else {
                            str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A2, convert(varchar,LY_END_DATE  ,120) A3, convert(varchar,LY_ST_DATE -1460,120) A4  ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                // str2 = str2 + addClause;

                con = getConnection(elementID);
                st = con.createStatement();
                rs2 = st.executeQuery(str2);

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) " + addClause;
            pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) " + addClause;





        } else {


            try {
                str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(P_ST_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(P_ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        } else {
                            str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                            addClause = " and to_char(ddate,'dd') *1 <= " + d1.substring(3, 5) + " ";
                        } else {
                            str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                        } else {
                            str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                        } else {
                            str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                str2 = str2 + addClause;

                con = getConnection(elementID);
                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";


        }

        if (periodType.equalsIgnoreCase("MONTH")) {
            daysDiff = 30;
        } else if (periodType.equalsIgnoreCase("DAY")) {
            daysDiff = 1;
        } else if (periodType.equalsIgnoreCase("QTR")) {
            daysDiff = 90;
        } else if (periodType.equalsIgnoreCase("WEEK")) {
            daysDiff = 7;
        } else {
            daysDiff = 365;
        }
        ////.println("d_clu"+d_clu);
        ////.println("pd_clu"+pd_clu);

    }

    public void setTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1, boolean drilled) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        String str2 = "";
        String interval = null, time = null, timeInterval = null, toChar = null, dateFormat = null, dateType = null,
                onlyDate = null, onlymysql = null, periodType = null, mysqlInterval = null;

        String addClause = " ";
        boolean callJavaTime = false;

        GetConnectionType getconntype = new GetConnectionType();
        connType = getconntype.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("PostgreSQL")) {
            onlyDate = "MM/dd/yyyy";
            dateType = "to_char";
            toChar = "to_char";
            dateFormat = "mm/dd/yyyy hh24:mi:ss";
            interval = "";
            onlymysql = "";
            periodType = "- INTERVAL '";
            mysqlInterval = " DAYS' ";
            timeInterval = "to_timestamp";
            time = "day";
        } else if (connType.equalsIgnoreCase("mysql")) {

            onlyDate = "%m/%d/%Y";
            dateType = "date_format";
            toChar = "date_format";
            onlymysql = "(DATE_SUB";
            mysqlInterval = " DAY)";
            periodType = ",interval ";
            interval = "";
            timeInterval = "str_to_date";
            dateFormat = "%m/%d/%Y %H:%i:%s";
            time = "";
        } else {
            interval = "";
            time = "";
        }
        if (connType.equalsIgnoreCase("Mysql") || connType.equalsIgnoreCase("PostgreSQL")) {
            try {
                if (drilled) {
                    str2 = "select " + dateType + "(end_date ,'" + dateFormat + " '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " " + dateType + "(cm_st_date ,'" + dateFormat + " ') , " + dateType + onlymysql + "(end_date " + periodType + "1" + mysqlInterval + " ,'" + dateFormat + " ')  , " + dateType + onlymysql + "(cm_st_date " + periodType + "1" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " " + dateType + "(cm_st_date ,'" + dateFormat + " ') , " + dateType + onlymysql + "(end_date " + periodType + "7" + mysqlInterval + " ,'" + dateFormat + " ')  , " + dateType + onlymysql + "(cm_st_date " + periodType + "7" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " " + dateType + "(cm_st_date ,'" + dateFormat + " ') , " + dateType + "(PM_END_DATE,'" + dateFormat + " ')  , " + dateType + "(PM_ST_DATE,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " " + dateType + "(cm_st_date ,'" + dateFormat + " ') , " + dateType + onlymysql + "(end_date " + periodType + "91" + mysqlInterval + " ,'" + dateFormat + " ')  , " + dateType + onlymysql + "(cm_st_date " + periodType + "91" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + "(cm_st_date ,'" + dateFormat + " ') , " + dateType + "(PYM_END_DATE ,'" + dateFormat + " ') , " + dateType + "(PYM_ST_DATE,'" + dateFormat + " ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CW_ST_DATE " + periodType + "91" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(LYW_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LYW_ST_DATE " + periodType + "91" + mysqlInterval + ",'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CW_ST_DATE " + periodType + "91" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LYW_END_DATE ,'" + dateFormat + " ') , " + dateType + "(LYW_ST_DATE " + periodType + "91" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_END_DATE +.99999 ,'" + dateFormat + " ') , " + dateType + "(LYQ_ST_DATE,'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + "(CQ_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LYQ_END_DATE +.99999 ,'" + dateFormat + " ') , " + dateType + "(LYQ_ST_DATE,'" + dateFormat + " ') ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_END_DATE ,'" + dateFormat + " '), " + dateType + "(LY_ST_DATE,'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + "(CY_ST_DATE,'" + dateFormat + " ') , " + dateType + "(LY_END_DATE ,'" + dateFormat + " '), " + dateType + "(LY_ST_DATE,'" + dateFormat + " ') ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LY_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LY_END_DATE ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }
                        }
                    }
                    str2 = str2 + " from " + timeTableName + " where ddate = " + timeInterval + "('" + d1 + "','" + onlyDate + "') ";

                    //out.println(str2);

                } else {
                    str2 = "select " + dateType + "(end_date ,'" + dateFormat + " '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " " + dateType + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(P_ST_DATE ,'" + dateFormat + " ') , " + dateType + onlymysql + "(P_ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " " + dateType + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(PW_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(PW_DAY " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " " + dateType + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(PM_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(PM_DAY " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " " + dateType + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(LQ_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LQ_DAY " + periodType + "30" + mysqlInterval + " ,'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(ST_DATE " + periodType + "30" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LY_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LY_DAY" + periodType + "30" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CW_ST_DATE" + periodType + "49" + mysqlInterval + " ,'" + dateFormat + " ') , " + dateType + "(LYW_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LYW_ST_DATE " + periodType + "49" + mysqlInterval + ",'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CW_ST_DATE " + periodType + "49" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LYW_END_DATE ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LYW_ST_DATE " + periodType + "49" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(PM_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(PM_ST_DATE " + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                    if (connType.equalsIgnoreCase("MySql")) {
                                        addClause = " and date_format(PROGEN_TIME.ddate,'%d') *1 <= " + d1.substring(3, 5) + " ";
                                    } else if (connType.equalsIgnoreCase("Postgres")) {
                                        addClause = " and to_char(PROGEN_TIME.ddate,'dd') *1 <= " + d1.substring(3, 5) + " ";
                                    }
                                }
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CM_ST_DATE" + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(PYM_END_DATE ,'" + dateFormat + " ') , " + dateType + onlymysql + "(PYM_ST_DATE" + periodType + "365" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CQ_ST_DATE " + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LYQ_DAY ,'" + dateFormat + " '), " + dateType + onlymysql + "(LYQ_ST_DATE" + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CQ_ST_DATE " + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LYQ_END_DATE ,'" + dateFormat + " '), " + dateType + onlymysql + "(LYQ_ST_DATE" + periodType + "730" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " " + dateType + onlymysql + "(CY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LY_DAY ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                            } else {
                                str2 = str2 + " " + dateType + onlymysql + "(CY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') , " + dateType + "(LY_END_DATE ,'" + dateFormat + " ') , " + dateType + onlymysql + "(LY_ST_DATE " + periodType + "1460" + mysqlInterval + ",'" + dateFormat + " ') ";
                            }
                        }
                    }
//str2 = str2 + " from " + timeTableName + " where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                    if (timeInterval != null) {
                        str2 = str2 + " from " + timeTableName + " PROGEN_TIME where ddate = " + timeInterval + " ('" + d1 + "','" + onlyDate + "') ";
                    } else {
                        str2 = str2 + " from " + timeTableName + " PROGEN_TIME where ddate = str_to_date ('" + d1 + "','" + onlyDate + "') ";
                    }
//out.println(str2);

                }

                str2 = str2;
                con = getConnection(elementID);

                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                ed_d = rs2.getString(1
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


//out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
                logger.error("Exception:", e);
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between " + timeInterval + "('" + st_d + "','" + dateFormat + " ') and " + timeInterval + "('" + ed_d + "','" + dateFormat + " ')  " + addClause;
            pd_clu = " between " + timeInterval + "('" + p_st_d + "','" + dateFormat + " ') and " + timeInterval + "('" + p_ed_d + "','" + dateFormat + " ')  " + addClause;


        } else if (connType.equalsIgnoreCase("Sqlserver")) {
            try {
                if (drilled) {
                    str2 = "select convert(varchar,end_date ,120)  A0, ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " convert(varchar,cm_st_date ,120)  A2, convert(varchar,end_date-1   ,120) A3, convert(varchar,cm_st_date-1 ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " convert(varchar,cm_st_date ,120)  A2, convert(varchar,end_date-7   ,120) A3, convert(varchar,cm_st_date-7  ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " convert(varchar,cm_st_date ,120)  A2, convert(varchar,PM_END_DATE   ,120) A3, convert(varchar,PM_ST_DATE ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " convert(varchar,cm_st_date ,120)  A2, convert(varchar,end_date-91   ,120) A3, convert(varchar,cm_st_date-91 ,120) A4 ";
                            } else {
                                str2 = str2 + " convert(varchar,cm_st_date ,120)  A2, convert(varchar,PYM_END_DATE   ,120) A3, convert(varchar,PYM_ST_DATE ,120) A4 ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) A2, convert(varchar,LW_DAY   ,120) A3 , convert(varchar,LW_ST_DATE -91,120) A4 ";
                            } else {
                                str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) A2, convert(varchar,LYW_END_DATE   ,120)  A3 , convert(varchar,LYW_ST_DATE -91,120) A4 ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " convert(varchar,CQ_ST_DATE,120) A2, convert(varchar,LQ_END_DATE +.99999 ,120) A3,  convert(varchar,LQ_ST_DATE,120) A4  ";
                            } else {
                                str2 = str2 + " convert(varchar,CQ_ST_DATE,120) A2, convert(varchar,LYQ_END_DATE +.99999 ,120) A3,  convert(varchar,LYQ_ST_DATE,120) A4 ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " convert(varchar,CY_ST_DATE,120) A2, convert(varchar,LY_END_DATE   ,120) A3,  convert(varchar,LY_ST_DATE,120) A4 ";
                            } else {
                                str2 = str2 + " convert(varchar,CY_ST_DATE,120) A2, convert(varchar,LY_END_DATE   ,120) A3,  convert(varchar,LY_ST_DATE,120) A4 ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A2, convert(varchar,LY_DAY  ,120) A3, convert(varchar,LY_ST_DATE -1460,120)  A4 ";
                            } else {
                                str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A2, convert(varchar,LY_END_DATE  ,120) A3, convert(varchar,LY_ST_DATE -1460,120)  A4 ";
                            }
                        }
                    }
                    str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                    //out.println(str2);

                } else {
                    str2 = "select convert(varchar,end_date ,120)  A0, ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " convert(varchar,ST_DATE -" + interval + " 30 " + time + " ,120)  A2, convert(varchar,P_ST_DATE   ,120) A3 , convert(varchar,P_ST_DATE -" + interval + " 30 " + time + " ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " convert(varchar,ST_DATE -" + interval + " 30 " + time + " ,120)  A2, convert(varchar,PW_DAY   ,120) A3 , convert(varchar,PW_DAY -" + interval + " 30 " + time + " ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " convert(varchar,ST_DATE -" + interval + " 30 " + time + " ,120)  A2, convert(varchar,PM_DAY   ,120) A3 , convert(varchar,PM_DAY -" + interval + " 30 " + time + " ,120) A4 ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " convert(varchar,ST_DATE -" + interval + " 30 " + time + " ,120)  A2, convert(varchar,LQ_DAY   ,120) A3 , convert(varchar,LQ_DAY -" + interval + " 30 " + time + " ,120) A4 ";
                            } else {
                                str2 = str2 + " convert(varchar,ST_DATE -" + interval + " 30 " + time + " ,120)  A2, convert(varchar,LY_DAY   ,120) A3 , convert(varchar,LY_DAY -" + interval + " 30 " + time + " ,120) A4  ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " convert(varchar,CW_ST_DATE -" + interval + " '91 " + time + "' ,120) A2, convert(varchar,LW_DAY   ,120) A3 , convert(varchar,LW_ST_DATE -" + interval + " '91 " + time + "',120) A4 ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                                    addClause = " DATEPART(day,PROGEN_TIME.ddate) <= " + d1.substring(3, 5) + " ";
                                }
                            } else {
                                str2 = str2 + " convert(varchar,CW_ST_DATE -91 ,120) A2, convert(varchar,LYW_END_DATE   ,120)  A3 , convert(varchar,LYW_ST_DATE -91,120) A4 ";
                            }
                            callJavaTime = true;
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " convert(varchar,CM_ST_DATE -335,120) A1, convert(varchar,PM_DAY +.99999 ,120) A2,  convert(varchar,PM_ST_DATE -365,120) A3 ";
                            } else {
                                str2 = str2 + " convert(varchar,CM_ST_DATE -335,120) A1, convert(varchar,PYM_END_DATE +.99999 ,120) A2,  convert(varchar,PYM_ST_DATE -365,120) A3 ";
                            }
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                if(d1.length()==9){
                                addClause = " and DATEPART(dd,PROGEN_TIME.ddate)*1 <= " + d1.substring(3, 4) + " ";
                                }
 else{
                                addClause = " and DATEPART(dd,PROGEN_TIME.ddate)*1 <= " + d1.substring(3, 5) + " ";
                            }
                            }

                            callJavaTime = true;
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) A1, convert(varchar,LQ_DAY   ,120) A2,  convert(varchar,LQ_ST_DATE-730,120) A3 ";
                            } else {
                                str2 = str2 + " convert(varchar,CQ_ST_DATE -730,120) A1, convert(varchar,LYQ_END_DATE   ,120) A2,  convert(varchar,LYQ_ST_DATE-730,120) A3 ";
                            }
                            callJavaTime = true;
                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A1, convert(varchar,LY_DAY  ,120) A2, convert(varchar,LY_ST_DATE -1460,120)  A3 ";
                                addClause = " and PROGEN_TIME.daysofyear <= (select max(daysofyear)  from  " + timeTableName + "  where ddate= convert(datetime,'" + d1 + "',101) )";
                            } else {
                                str2 = str2 + " convert(varchar,CY_ST_DATE -1460,120) A1, convert(varchar,LY_END_DATE  ,120) A2, convert(varchar,LY_ST_DATE -1460,120) A3  ";
                            }


                            callJavaTime = true;
                        }
                    }
                    //str2 = str2 + " from   " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                    if (timeStamp != null) {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                    } else {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = convert(datetime,'" + d1 + "',101) ";
                    }
                    //out.println(str2);

                }

                str2 = str2;
                ////.println("str2"+str2);
                con = getConnection(elementID);

                st = con.createStatement();
                rs2 = st.executeQuery(str2);

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) " + addClause;
            pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) " + addClause;


        } else if (connType.equalsIgnoreCase("PostgreSQL")) {


            try {
                if (drilled) {
                    str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date-1   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date-1 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date-7   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date-7 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date- 91   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date- 91 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PYM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date-7    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_END_DATE +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                            }
                        }
                    }
                    str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                    //out.println(str2);

                } else {
                    str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(P_ST_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(P_ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PM_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LQ_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LY_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                    addClause = " and to_char(PROGEN_TIME.ddate,'dd') *1 <= " + d1.substring(3, 5) + " ";
                                }
                            } else {
                                str2 = str2 + " to_char(CM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                    addClause = " and PROGEN_TIME.end_date - PROGEN_TIME.CY_ST_DATE  <= daysofyear";
                                }
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                                if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                    addClause = " and PROGEN_TIME.end_date - PROGEN_TIME.CY_ST_DATE  <= daysofyear";
                                }
                            }
                        }
                    }
                    //str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                    if (timeStamp != null) {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = " + this.timeStamp + " ('" + d1 + "','MM/dd/yyyy') ";
                    } else {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = to_timestamp ('" + d1 + "','MM/dd/yyyy') ";
                    }
                    //out.println(str2);

                }
                str2 = str2;

                con = getConnection(elementID);
                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') " + addClause;
            pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') " + addClause;


        } else {


            try {
                if (drilled) {
                    str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date-1   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date-1 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date-7   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date-7 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(end_date- 91   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(cm_st_date- 91 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(cm_st_date ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PYM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(end_date-7    ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_END_DATE +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(CQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE +.99999 ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(LYQ_ST_DATE,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LY_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') ";
                            }

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                            }
                        }
                    }
                    str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                    //out.println(str2);

                } else {
                    str2 = "select to_char(end_date ,'mm/dd/yyyy hh24:mi:ss '), ";

                    {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(P_ST_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(P_ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("PRIOR WEEK")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("PRIOR MONTH")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(PM_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")) {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LQ_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            } else {
                                str2 = str2 + " to_char(ST_DATE -30 ,'mm/dd/yyyy hh24:mi:ss ')  , to_char(LY_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_DAY -30 ,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_DAY   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CW_ST_DATE -91 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYW_ST_DATE -91,'mm/dd/yyyy hh24:mi:ss ') ";
                            }
                            callJavaTime = true;

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                str2 = str2 + " to_char(CM_ST_DATE -335,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_DAY " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                    addClause = " and to_char(PROGEN_TIME.ddate,'dd') *1 <= " + d1.substring(3, 5) + " ";
                                }
                            } else {
                                str2 = str2 + " to_char(CM_ST_DATE -335,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE " + interval + " ,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_ST_DATE -365,'mm/dd/yyyy hh24:mi:ss ')  ";
                            }
                            callJavaTime = true;

                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LQ_DAY   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                            } else {
                                str2 = str2 + " to_char(CQ_ST_DATE -730,'mm/dd/yyyy hh24:mi:ss ') , to_char(LYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(LYQ_ST_DATE-730,'mm/dd/yyyy hh24:mi:ss ') ";
                            }
                            callJavaTime = true;

                        } else {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                    addClause = " and PROGEN_TIME.daysofyear <= (select max(daysofyear)  from  " + timeTableName + "  where ddate= to_date('" + d1 + "','MM/dd/yyyy'))";
                                    callJavaTime = true;
                                }
                            } else {
                                str2 = str2 + " to_char(CY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(LY_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                                if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                    addClause = " and PROGEN_TIME.daysofyear <= (select max(daysofyear)  from  " + timeTableName + "  where ddate= to_date('" + d1 + "','MM/dd/yyyy'))";
                                    callJavaTime = true;
                                }
                            }
                        }
                    }
                    //str2 = str2 + " from   " + timeTableName + "  where ddate = to_date('" + d1 + "','MM/dd/yyyy') ";
                    if (timeStamp != null) {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = " + this.timeStamp + " ('" + d1 + "','MM/dd/yyyy') ";
                    } else {
                        str2 = str2 + " from    " + timeTableName + "  where ddate = to_timestamp ('" + d1 + "','MM/dd/yyyy') ";
                    }
                    //out.println(str2);

                }
                str2 = str2;
                //
                con = getConnection(elementID);
                st2 = con.prepareCall(str2);
                rs2 = st2.executeQuery();

                while (rs2.next()) {
//                    ed_d = rs2.getString(1);
                    if (rs2.getString(1).contains("00:00:00")) {
                        ed_d = rs2.getString(1);
                    } else {
                        ed_d = rs2.getString(1) + " " + "23:59:59 ";
                    }
//                    ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                    st_d = rs2.getString(2);
                    p_ed_d = rs2.getString(3);
                    p_st_d = rs2.getString(4);


                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }
            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }

            d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') " + addClause;
            pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') " + addClause;

            if (callJavaTime) {
                setJavaTrendRange(PRG_PERIOD_TYPE, PRG_COMPARE, d1);
                d_clu += addClause;
                pd_clu += addClause;
            }

        }

        ////.println("d_clu"+d_clu);
        ////.println("pd_clu"+pd_clu);
        if (callJavaTime) {
            setJavaTrendRange(PRG_PERIOD_TYPE, PRG_COMPARE, d1);
            d_clu += addClause;
            pd_clu += addClause;
        }
        getTrendDateRanges(PRG_PERIOD_TYPE);

    }

    public void getTrendDateRanges(String periodType) {
        String str = null;
        GetConnectionType getcontype = new GetConnectionType();
        connType = getcontype.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("Sqlserver")) {

            if ("Month".equalsIgnoreCase(periodType)) {
                str = " select cm_cust_name , cm_st_date, datediff(dd,cm_end_date,cm_st_date) day_diff from "
                        + " ( select distinct  cm_cust_name , cm_st_date, case when cm_end_date < CONVERT(DATETIME,'" + ed_d + "',120) "
                        + " then cm_end_date else CONVERT(DATETIME,'" + ed_d + "',120) end cm_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")A";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Qtr".equalsIgnoreCase(periodType)) {
                str = " select cq_cust_name , cq_st_date, cq_end_date-cq_st_date day_diff from "
                        + " ( select distinct  cq_cust_name , cq_st_date, case when cq_end_date < CONVERT(DATETIME,'" + ed_d + "',120) "
                        + " then cq_end_date else CONVERT(DATETIME,'" + ed_d + "',120) end cq_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")A";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Year".equalsIgnoreCase(periodType)) {
                str = " select cy_cust_name , cy_st_date, cy_end_date-cy_st_date day_diff from "
                        + " ( select distinct  cy_cust_name , cy_st_date, case when cy_end_date <CONVERT(DATETIME,'" + ed_d + "',120) "
                        + " then cy_end_date else CONVERT(DATETIME,'" + ed_d + "',120) end cy_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")A";  // convert(datetime,'" + ed_d + "',120)
            }

        } else if (connType.equalsIgnoreCase("mysql")) {
            if ("Month".equalsIgnoreCase(periodType)) {
                str = " select cm_cust_name , cm_st_date, cm_end_date-cm_st_date day_diff from "
                        + " ( select distinct  cm_cust_name , cm_st_date, case when cm_end_date < str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') "
                        + " then cm_end_date else str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') end cm_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ") A ";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Qtr".equalsIgnoreCase(periodType)) {
                str = " select cq_cust_name , cq_st_date, cq_end_date-cq_st_date day_diff from "
                        + " ( select distinct  cq_cust_name , cq_st_date, case when cq_end_date < str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') "
                        + " then cq_end_date else str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') end cq_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ") A ";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Year".equalsIgnoreCase(periodType)) {
                str = " select cy_cust_name , cy_st_date, cy_end_date-cy_st_date day_diff from "
                        + " ( select distinct  cy_cust_name , cy_st_date, case when cy_end_date < str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') "
                        + " then cy_end_date else str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s') end cy_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ") A ";  // convert(datetime,'" + ed_d + "',120)
            }

        } else {
            if ("Month".equalsIgnoreCase(periodType)) {
                str = " select cm_cust_name , cm_st_date, cm_end_date-cm_st_date day_diff from "
                        + " ( select distinct  cm_cust_name , cm_st_date, case when cm_end_date < to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') "
                        + " then cm_end_date else to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') end cm_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Qtr".equalsIgnoreCase(periodType)) {
                str = " select cq_cust_name , cq_st_date, cq_end_date-cq_st_date day_diff from "
                        + " ( select distinct  cq_cust_name , cq_st_date, case when cq_end_date < to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') "
                        + " then cq_end_date else to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') end cq_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")";  // convert(datetime,'" + ed_d + "',120)
            } else if ("Year".equalsIgnoreCase(periodType)) {
                str = " select cy_cust_name , cy_st_date, cy_end_date-cy_st_date day_diff from "
                        + " ( select distinct  cy_cust_name , cy_st_date, case when cy_end_date < to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') "
                        + " then cy_end_date else to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') end cy_end_date "
                        + "from pr_day_denom PROGEN_TIME where ddate " + d_clu + ")";  // convert(datetime,'" + ed_d + "',120)
            }



        }

        if (str != null) {
            Connection con = null;
//            CallableStatement st2 = null;
//            ResultSet rs2 = null;
            PbReturnObject pbReturnObject = new PbReturnObject();
            try {
                con = getConnection(elementID);

                pbReturnObject = super.execSelectSQL(str, con);
                //                st2 = con.prepareCall(str);
                //                rs2 = st2.executeQuery();

//                st2 = con.prepareCall(str);
//                rs2 = st2.executeQuery();

                trendNoOfDays = new HashMap<String, Integer>();
                for (int row = 0; row < pbReturnObject.getRowCount(); row++) {
                    String period = pbReturnObject.getFieldValueString(row, 0);
                    int days = pbReturnObject.getFieldValueInt(row, 2);
                    trendNoOfDays.put(period, days);
                }
//                while(rs2.next()){
//                    String period = rs2.getString(1);
//                    int days = rs2.getInt(3);
//                    trendNoOfDays.put(period, days);
//                }

//                rs2.close();
//                st2.close();
//                con.close();
//
//                rs2 = null;
//                st2 = null;
//                con = null;

            } catch (SQLException e) {
                logger.error("Exception:", e);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
//            finally {
//                try{
//                    if (rs2 != null) {
//                        rs2.close();
//                    }
//                    if (st2 != null) {
//                        st2.close();
//                    }
//
//                    if (con != null) {
//                        con.close();
//                    }
//                }
//                catch(SQLException e){
//                    logger.error("Exception:",e);
//                }
//            }
        }
    }
    ////////////////////////////

    /////////////////////////
    public void setMonthTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String getmonth) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {

            //con = DriverManager.getConnection (URL, username, password);
            //   Class.forName("oracle.jdbc.driver.OracleDriver");

            str2 = "select to_char(CM_END_DATE ,'mm/dd/yyyy hh24:mi:ss '), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                        str2 = str2 + " to_char(CM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PM_END_DATE) ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " to_char(CM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PM_END_DATE) +.99999,'mm/dd/yyyy hh24:mi:ss ') , to_char(PM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                        str2 = str2 + " to_char(CM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PYY_END_DATE) +.99999,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  ";
                    } else {
                        str2 = str2 + " to_char(CM_ST_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  , to_char(trunc(PYY_END_DATE) +.99999,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE -185,'mm/dd/yyyy hh24:mi:ss ')  ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') ,  to_char(PYM_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMYQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE   ,'mm/dd/yyyy hh24:mi:ss '),  to_char(PMYQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                        str2 = str2 + " to_char(CMQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMQ_ST_DATE -740 ,'mm/dd/yyyy hh24:mi:ss ') ";
                    } else {
                        str2 = str2 + " to_char(CMQ_ST_DATE -740,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMYQ_END_DATE   ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PMYQ_ST_DATE  -740,'mm/dd/yyyy hh24:mi:ss ') ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " to_char(CMY_START_DATE-1460 ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYM_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE-1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                    } else {
                        str2 = str2 + " to_char(CMY_START_DATE-1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(PYY_START_DATE-1460,'mm/dd/yyyy hh24:mi:ss ')   ";
                    }
                }
            }
            str2 = str2 + " from  prg_acn_mon_denom where MON_code = '" + getmonth + "' ";
            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
        pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";


    }

    ////////////////////////////
    public void setStdMonthRange(String month1, String month2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        ProgenParam param = new ProgenParam();
        param.elementId = this.elementID;
        if (month1 == null || month1.equalsIgnoreCase("NULL") || month1.equalsIgnoreCase("")) {
            //month1 = param.getYearforpage();
            month1 = param.getmonthforpage();
        }
        if (month2 == null || month2.equalsIgnoreCase("NULL") || month2.equalsIgnoreCase("")) {
            //month2 = (Integer.parseInt(param.getYearforpage()) -1 )+"";
            month2 = param.getmonthforpage();
        }

        d_clu = " = '" + month1 + "' ";
        pd_clu = " = '" + month2 + "' ";


    }

    public void setStdMonthTrendRange(String month1, String month2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        ProgenParam param = new ProgenParam();
        String str2 = "select to_char(PYM_ST_DATE,'mm/dd/yyyy') , to_char(CM_ST_DATE,'mm/dd/yyyy') from  " + timeTableName + "  where CM_CUST_NAME =" + month1;
        String str3 = "select to_char(PYM_ST_DATE,'mm/dd/yyyy') , to_char(PYM_ST_DATE,'mm/dd/yyyy') from  " + timeTableName + "  where CM_CUST_NAME =" + month2;
        if (month1 == null || month1.equalsIgnoreCase("NULL")) {
            month1 = param.getmonthforpage();
        }
        if (month2 == null || month2.equalsIgnoreCase("NULL")) {
            month2 = param.getmonthforpage();
        }


        try {
            str2 = "select to_char(CM_END_DATE,'mm/dd/yyyy') , to_char(PYM_ST_DATE,'mm/dd/yyyy') from  " + timeTableName + "  where CM_CUST_NAME ='" + month1 + "' ";




            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                //p_ed_d = rs2.getString(3);
                //p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }


        try {
            str2 = "select to_char(CM_END_DATE,'mm/dd/yyyy') , to_char(PYM_ST_DATE,'mm/dd/yyyy') from  " + timeTableName + "  where CM_CUST_NAME = '" + month2 + "' ";





            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
                //ed_d = rs2.getString(1);
                //st_d = rs2.getString(2);
                p_ed_d = rs2.getString(1);
                p_st_d = rs2.getString(2);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
        pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";




    }

    /////////////////////////
    public void setYearRange(String PRG_PERIOD_TYPE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            str2 = "select to_char(c_end_date ,'mm/dd/yyyy hh24:mi:ss '), ";
            str2 = str2 + " to_char(C_ST_DATE,'mm/dd/yyyy hh24:mi:ss ') , to_char(p_end_date  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(p_st_date,'mm/dd/yyyy hh24:mi:ss ')   ";


            str2 = str2 + " from  pr_year_denom where  to_date('" + d1 + "','MM/dd/yyyy') between c_st_date  and c_end_date ";

            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }

        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
        pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";


    }

    public void setYearTrendRange(String PRG_PERIOD_TYPE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            str2 = "select to_char(c_end_date ,'mm/dd/yyyy hh24:mi:ss '), ";
            str2 = str2 + " to_char(C_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ') , to_char(P_END_DATE  ,'mm/dd/yyyy hh24:mi:ss ') , to_char(p_ST_DATE -1460,'mm/dd/yyyy hh24:mi:ss ')   ";


            str2 = str2 + " from  pr_year_denom where  to_date('" + d1 + "','MM/dd/yyyy') between c_st_date  and c_end_date ";

            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
        pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";


    }
    /////////////

    public void setStdYearRange(String Year1, String Year2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        ProgenParam param = new ProgenParam();
        param.elementId = this.elementID;
        if (Year1 == null || Year1.equalsIgnoreCase("NULL") || Year1.equalsIgnoreCase("")) {
            Year1 = param.getYearforpage();
//            Year1="2008";
        }
        if (Year2 == null || Year2.equalsIgnoreCase("NULL") || Year2.equalsIgnoreCase("")) {
            //Year2 = (Integer.parseInt(param.getYearforpage()) -1 )+"";
            Year2 = (Integer.parseInt(param.getYearforpage()) - 1) + "";
        }

        d_clu = " = " + Year1;
        pd_clu = " = " + Year2;


    }

    public void setStdYearTrendRange(String Year1, String Year2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        ProgenParam param = new ProgenParam();
        String str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year1;
        String str3 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year2;
        if (Year1 == null || Year1.equalsIgnoreCase("NULL")) {
            Year1 = param.getYearforpage();
        }
        if (Year2 == null || Year2.equalsIgnoreCase("NULL")) {
            Year2 = (Integer.parseInt(param.getYearforpage()) - 1) + "";
        }


        try {
            str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year1;




            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                //p_ed_d = rs2.getString(3);
                //p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }


        try {
            str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year2;




            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
                //ed_d = rs2.getString(1);
                //st_d = rs2.getString(2);
                p_ed_d = rs2.getString(1);
                p_st_d = rs2.getString(2);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  " + st_d + " and  " + ed_d + " ";
        pd_clu = " between  " + p_st_d + " and  " + p_ed_d + " ";



    }

    public String getTimeViewbyQuery(ArrayList timeArray) {
        String viewOrderBy = "";
        if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
            viewOrderBy = ("Select distinct CY_CUST_NAME , CY_CUST_NAME, CYEAR from  " + timeTableName + "  order by CYEAR  ");
        } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
            viewOrderBy = ("Select distinct CQ_CUST_NAME , CQ_CUST_NAME , CYEAR  , CQTR from  " + timeTableName + "  order by CYEAR desc , CQTR  ");

        } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY")) {
            viewOrderBy = ("Select * from  " + timeTableName + "  order by ST_DATE  ");
        }

        return (viewOrderBy);
    }

    public String getTimeViewbyQuery(String timeLevel, String PRG_PERIOD_TYPE, String PRG_COMPARE, String Currval) throws SQLException {
        String viewOrderBy = "";
        this.setTrendRange(PRG_PERIOD_TYPE, PRG_COMPARE, Currval, false);
        if (timeLevel.equalsIgnoreCase("Day")) {
            if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                GetConnectionType getcontype = new GetConnectionType();
                connType = getcontype.getConTypeByElementId(elementID);
                if (connType.equalsIgnoreCase("Sqlserver")) {
                    viewOrderBy = ("Select distinct convert(varchar,PROGEN_TIME.ST_DATE,101) , convert(varchar,PROGEN_TIME.ST_DATE,101) , trunc(PROGEN_TIME.ST_DATE)  "
                            + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by trunc(PROGEN_TIME.ST_DATE)  ");

                } else {
                    viewOrderBy = ("Select distinct to_Char(PROGEN_TIME.ST_DATE,'dd-mm-yy') , to_Char(PROGEN_TIME.ST_DATE,'dd-mm-yy') , trunc(PROGEN_TIME.ST_DATE)  "
                            + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by trunc(PROGEN_TIME.ST_DATE)  ");

                }


            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                viewOrderBy = ("Select distinct CW_CUST_NAME A1, CW_CUST_NAME A2, CWYEAR A3, CWEEK A4 "
                        + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by CWYEAR, CWEEK ");

            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                viewOrderBy = ("Select distinct CM_CUST_NAME A1, CM_CUST_NAME A2, CM_ST_DATE A3 "
                        + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by CM_ST_DATE  ");

            } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                viewOrderBy = ("Select distinct CQ_CUST_NAME A1, CQ_CUST_NAME A2 , CQ_YEAR A3, CQTR  A4"
                        + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by CQ_YEAR, CQTR ");

            } else {
                viewOrderBy = ("Select distinct CY_CUST_NAME A1, CY_CUST_NAME A2,  CYEAR A3, CYEAR A4"
                        + " from  " + timeTableName + "  PROGEN_TIME where ST_DATE " + d_clu + "  order by CYEAR, CYEAR ");
            }



        }

        return (viewOrderBy);
    }

    public ArrayList getTimeViewbycols(ArrayList timeArray, boolean isTimeSeries) {
        ArrayList viewOrderBy = new ArrayList();
        String truncType = "trunc";
        String dateFormat = "mm-dd-yyyy";
        String truncDateType = "";
        String tochar = "to_char";
        String mntYear = "YYYY-MM";


        GetConnectionType getcontype = new GetConnectionType();
        connType = getcontype.getConTypeByElementId(elementID);

        if (connType.equalsIgnoreCase("mysql")) {
            truncType = "date_format";
            dateFormat = "%m-%d-%Y";
            truncDateType = ", '%m-%d-%Y'";
            tochar = "date_format";
            mntYear = "%Y-%m";
            if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("MONTH")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.CM_ST_DATE,'" + mntYear + "') ");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.CM_ST_DATE,'" + mntYear + "') ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && isTimeSeries) {
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.CM_ST_DATE,'" + mntYear + "') ");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(tochar + "(PROGEN_TIME.CM_ST_DATE,'" + mntYear + "') ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE   ");
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            }

        } else if (connType.equalsIgnoreCase("Sqlserver")) {
            if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("MONTH")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && isTimeSeries) {
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE  ");
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE   ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            }

        } else {


            if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("MONTH")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && isTimeSeries) {
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QTR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE ");
                viewOrderBy.add("PROGEN_TIME.CQ_CUST_NAME ");
                viewOrderBy.add("  PROGEN_TIME.CQ_ST_DATE ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
                viewOrderBy.add("PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
            } else if (timeArray.get(0).toString().equalsIgnoreCase("DAY") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_ROLLING")) {
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE");
                viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
            }
        }


        return (viewOrderBy);
    }
//Proc to Get the column in trend

    public ArrayList getTimeViewbycols(String timeLevel, String PRG_PERIOD_TYPE, String PRG_COMPARE, String Currval) {
        ArrayList viewOrderBy = new ArrayList();
        String truncType = "trunc";
        String dateFormat = "mm-dd-yyyy";
        String truncDateType = "";
        String tochar = "to_cahr";
        String mntYear = "YYYY-MM";
        GetConnectionType getcontype = new GetConnectionType();
        connType = getcontype.getConTypeByElementId(elementID);

        if (timeLevel.equalsIgnoreCase("Day")
                && (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")
                || PRG_PERIOD_TYPE.equalsIgnoreCase("Month")
                || PRG_PERIOD_TYPE.equalsIgnoreCase("Qtr")
                || PRG_PERIOD_TYPE.equalsIgnoreCase("Week")
                || PRG_PERIOD_TYPE.equalsIgnoreCase("Year")
                || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
            if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.N_ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.N_ST_DATE");

                } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NW_DAY");
                    viewOrderBy.add("PROGEN_TIME.NW_DAY");

                } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NM_DAY");
                    viewOrderBy.add("PROGEN_TIME.NM_DAY");

                } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR")
                        || PRG_COMPARE.equalsIgnoreCase("Same Day Last Qtr")) {
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NQ_DAY");
                    viewOrderBy.add("PROGEN_TIME.NQ_DAY");

                } else {
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NY_DAY");
                    viewOrderBy.add("PROGEN_TIME.NY_DAY");

                }

            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")
                        || PRG_COMPARE.equalsIgnoreCase("LAST WEEK COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST Month")
                        || PRG_COMPARE.equalsIgnoreCase("LAST Month COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                    viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                    viewOrderBy.add(" PROGEN_TIME.CW_ST_DATE ");
                    viewOrderBy.add(" PROGEN_TIME.NW_CUST_NAME ");
                    viewOrderBy.add(" PROGEN_TIME.NW_ST_DATE ");

                } else {
                    viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                    viewOrderBy.add(" PROGEN_TIME.CW_ST_DATE ");
                    viewOrderBy.add(" PROGEN_TIME.NYW_CUST_NAME ");
                    viewOrderBy.add(" PROGEN_TIME.NYW_ST_DATE ");
                }



            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
///// Code for Month
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")
                        || PRG_COMPARE.equalsIgnoreCase("LAST MONTH COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("COMPLETE LAST MONTH")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                    viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.CM_ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.NM_ST_DATE");

                } else if (PRG_COMPARE.equalsIgnoreCase("LAST YEAR")
                        || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")
                        || PRG_COMPARE.equalsIgnoreCase("LAST YEAR COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")
                        || PRG_COMPARE.equalsIgnoreCase("Complete Same Month Last Year")
                        || PRG_COMPARE.equalsIgnoreCase("Same QTR Last Year")) {
                    viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.CM_ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NYM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.NYM_ST_DATE");

                } else {
                    viewOrderBy.add("PROGEN_TIME.CM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.CM_ST_DATE");
                    viewOrderBy.add("PROGEN_TIME.NYM_CUST_NAME");
                    viewOrderBy.add("PROGEN_TIME.NYM_ST_DATE");

                }
            } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                        || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST QTR")
                        || PRG_COMPARE.equalsIgnoreCase("LAST QTR COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER")
                        || PRG_COMPARE.equalsIgnoreCase("LAST QUARTER COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("COMPLETE LAST QUARTER")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                    viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                    viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                    viewOrderBy.add(" PROGEN_TIME.NQ_CUST_NAME");
                    viewOrderBy.add(" PROGEN_TIME.NQ_ST_DATE ");
                } else {
                    viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                    viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                    viewOrderBy.add(" PROGEN_TIME.NYQ_CUST_NAME");
                    viewOrderBy.add(" PROGEN_TIME.NYQ_ST_DATE ");
                }

            } else {
                viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CY_ST_DATE ");
                viewOrderBy.add(" PROGEN_TIME.NY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.NY_ST_DATE ");
            }


        } else {



            if (connType.equalsIgnoreCase("mysql")) {
                truncType = "date_format";
                dateFormat = "%m-%d-%Y";
                truncDateType = ", '%m-%d-%Y'";
                tochar = "date_format";
                mntYear = "%Y-%m";


                if (timeLevel.equalsIgnoreCase("Day")) {

                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                            || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")
                            || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("COMPLETE LAST MONTH")) {

                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || " + tochar + "(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                            viewOrderBy.add(tochar + "(PROGEN_TIME.N_ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || " + tochar + "(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.N_ST_DATE " + truncDateType + ") ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                                viewOrderBy.add(tochar + "(PROGEN_TIME.N_ST_DATE,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.N_ST_DATE " + truncDateType + ") ");

                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                                viewOrderBy.add(tochar + "(PROGEN_TIME.NW_DAY,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.NW_DAY " + truncDateType + ") ");

                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                                viewOrderBy.add(tochar + "(PROGEN_TIME.NM_DAY,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.NM_DAY " + truncDateType + ") ");

                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                                viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                                viewOrderBy.add(tochar + "(PROGEN_TIME.NQ_DAY,'" + dateFormat + "')");
                                viewOrderBy.add(truncType + "(PROGEN_TIME.NQ_DAY " + truncDateType + ") ");

                            }


                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CW_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NW_ST_DATE ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NM_ST_DATE ");
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.NQ_ST_DATE ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CY_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NY_ST_DATE ");
                        }


                    } else {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || " + tochar + "(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                            viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || " + tochar + "(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                            viewOrderBy.add(tochar + "(PROGEN_TIME.ST_DATE,'" + dateFormat + "')");
                            viewOrderBy.add(truncType + "(PROGEN_TIME.ST_DATE " + truncDateType + ") ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add("PROGEN_TIME.ST_DATE");
                            viewOrderBy.add(" PROGEN_TIME.NYM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NYM_ST_DATE ");

                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        }
                    }




                }
                if (timeLevel.equalsIgnoreCase("Month")) {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(tochar + " (PROGEN_TIME.ST_DATE,'" + mntYear + "') ");
                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(tochar + " (PROGEN_TIME.ST_DATE,'" + mntYear + "') ");
                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                    } else {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                    }



                }
                if (timeLevel.equalsIgnoreCase("Year")) {
                    {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                    }



                }
            }




            if (connType.equalsIgnoreCase("Sqlserver")) {
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                    if (timeLevel.equalsIgnoreCase("Day")) {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || convert(varchar,PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.N_ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || convert(varchar,PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" (PROGEN_TIME.N_ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                                viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.N_ST_DATE,101)");
                                viewOrderBy.add(" (PROGEN_TIME.N_ST_DATE) ");

                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                                viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.NW_DAY,101)");
                                viewOrderBy.add(" (PROGEN_TIME.NW_DAY) ");


                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                                viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.NM_DAY,101)");
                                viewOrderBy.add(" (PROGEN_TIME.NM_DAY) ");



                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                                viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("convert(varchar,PROGEN_TIME.NQ_DAY,101)");
                                viewOrderBy.add(" (PROGEN_TIME.NQ_DAY) ");



                            }


                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CW_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NW_ST_DATE ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NM_ST_DATE ");
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.NQ_ST_DATE ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CY_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NY_ST_DATE ");
                        }



                    }

                } else {
                    if (timeLevel.equalsIgnoreCase("Day")) {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || convert(varchar,PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || convert(varchar,PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                            viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("convert(varchar,PROGEN_TIME.ST_DATE,101)");
                            viewOrderBy.add(" (PROGEN_TIME.ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        }



                    }

                }

                if (timeLevel.equalsIgnoreCase("Month")) {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(" convert(varchar,ST_DATE,'YYYY-MM') ");
                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(" convert(varchar,ST_DATE,'YYYY-MM') ");
                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                    } else {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                    }



                }
                if (timeLevel.equalsIgnoreCase("Year")) {
                    {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                    }



                }

            } else { /////////////////ORACLE
                if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                        || PRG_COMPARE.equalsIgnoreCase("Previous Day") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                    if (timeLevel.equalsIgnoreCase("Day")) {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || to_Char(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("to_Char(PROGEN_TIME.N_ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || to_Char(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" trunc(PROGEN_TIME.N_ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {

                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE")
                                    || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {

                                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("to_Char(PROGEN_TIME.N_ST_DATE,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.N_ST_DATE) ");


                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {

                                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("to_Char(PROGEN_TIME.NW_DAY,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.NW_DAY) ");



                            } else if (PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {

                                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("to_Char(PROGEN_TIME.NM_DAY,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.NM_DAY) ");





                            } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {

                                viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                                viewOrderBy.add("to_Char(PROGEN_TIME.NQ_DAY,'mm-dd-yyyy')");
                                viewOrderBy.add(" trunc(PROGEN_TIME.NQ_DAY) ");




                            }



                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CW_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NW_ST_DATE ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CM_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NM_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NM_ST_DATE ");
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.NQ_ST_DATE ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CY_ST_DATE ");
                            viewOrderBy.add(" PROGEN_TIME.NY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.NY_ST_DATE ");
                        }

                    }
                } else {
                    if (timeLevel.equalsIgnoreCase("Day")) {
                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || to_Char(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy HH24') || ':00:00 to ' || to_Char(PROGEN_TIME.ST_DATE,'HH24') || ':59:59'");
                            viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                            viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                            viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                            viewOrderBy.add("to_Char(PROGEN_TIME.ST_DATE,'mm-dd-yyyy')");
                            viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                            viewOrderBy.add(" PROGEN_TIME.CW_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CWYEAR || '-' || PROGEN_TIME.CWEEK ");
                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" TO_CHAR(PROGEN_TIME.ST_DATE,'YYYY-MM') ");
                            viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                            viewOrderBy.add(" TO_CHAR(PROGEN_TIME.ST_DATE,'YYYY-MM') ");
                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                            viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        } else {
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                            viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                            viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                        }

                    }

                }


                if (timeLevel.equalsIgnoreCase("Month")) {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(" TO_CHAR(ST_DATE,'YYYY-MM') ");
                        viewOrderBy.add(" PROGEN_TIME.CM_CUST_NAME ");
                        viewOrderBy.add(" TO_CHAR(ST_DATE,'YYYY-MM') ");
                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                        viewOrderBy.add(" PROGEN_TIME.CQ_CUST_NAME");
                        viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || '-' || PROGEN_TIME.CQTR ");
                    } else {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CY_ST_DATE ");
                        viewOrderBy.add(" PROGEN_TIME.NY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.NY_ST_DATE ");
                    }



                }
                if (timeLevel.equalsIgnoreCase("Year")) {
                    {
                        viewOrderBy.add(" PROGEN_TIME.CY_CUST_NAME ");
                        viewOrderBy.add(" PROGEN_TIME.CYEAR ");
                    }



                }


            }
        }

        return (viewOrderBy);
    }

    ///////////////////////////
    public void setExcelRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        try {
            str2 = "select TEXT(end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                        str2 = str2 + " TEXT(ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(end_date -1,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(ST_DATE -1 ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                        str2 = str2 + " TEXT(ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY    ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -1 ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                        str2 = str2 + " TEXT(ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY    ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -1 ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY    ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -1 ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                        str2 = str2 + " TEXT(CW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PW_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                        str2 = str2 + " TEXT(CW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                        str2 = str2 + " TEXT(CW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PW_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PW_ST_DATE ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CW_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(PM_DAY +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_DAY +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PM_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                        str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(LQ_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_DAY   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LYQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                        str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LQ_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " TEXT(CY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    } else {
                        str2 = str2 + " TEXT(CY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    }
                }
            }
            str2 = str2 + " from   " + timeTableName + "  where ddate = DATE(\"" + d1 + "\",\"MM/dd/yyyy\") ";
            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }

    public void setExcelMonthRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String getmonth) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";



        if (PRG_COMPARE == null) {
            PRG_COMPARE = "PERIOD";
        }
        if (PRG_PERIOD_TYPE == null) {
            PRG_PERIOD_TYPE = "Month";
        }
        try {


            str2 = "select TEXT(CM_END_DATE ,\"mm/dd/yyyy hh24:mi:ss \"), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                        str2 = str2 + " TEXT(CM_ST_DATE ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PM_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYM_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PM_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else {
                        str2 = str2 + " TEXT(CM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYY_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PM_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(PMYQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMQ_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CMQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMYQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMYQ_ST_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " TEXT(CMY_START_DATE ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    } else {
                        str2 = str2 + " TEXT(CMY_START_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    }
                }
            }
            str2 = str2 + " from  prg_acn_mon_denom where MON_code = \"" + getmonth + "\" ";

            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";




    }
    /////////////////////////

    public void setExcelTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            str2 = "select TEXT(end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                        str2 = str2 + " TEXT(ST_DATE -15 ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -15 ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else {
                        str2 = str2 + " TEXT(ST_DATE -15 ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -15 ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    }

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                        str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " TEXT(CM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_DAY +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else {
                        str2 = str2 + " TEXT(CM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " TEXT(CQ_ST_DATE -730,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_DAY   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LYQ_ST_DATE-730,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CQ_ST_DATE -730,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LYQ_ST_DATE-730,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    } else {
                        str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    }
                }
            }
            str2 = str2 + " from   " + timeTableName + "  where ddate = DATE(\"" + d1 + "\",\"MM/dd/yyyy\") ";
            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }

    public void setExcelTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1, boolean drilled) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            if (drilled) {
                str2 = "select TEXT(end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                            str2 = str2 + " TEXT(cm_st_date ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(PYM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        } else {
                            str2 = str2 + " TEXT(cm_st_date ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(PYM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                        } else {
                            str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_END_DATE +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(LYQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        } else {
                            str2 = str2 + " TEXT(CQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_END_DATE +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(LYQ_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " TEXT(CY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                        } else {
                            str2 = str2 + " TEXT(CY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LY_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                        } else {
                            str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = DATE(\"" + d1 + "\",\"MM/dd/yyyy\") ";
                //out.println(str2);

            } else {
                str2 = "select TEXT(end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";

                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year")) {
                            str2 = str2 + " TEXT(ST_DATE -15 ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -15 ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        } else {
                            str2 = str2 + " TEXT(ST_DATE -15 ,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY -15 ,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_DAY   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                        } else {
                            str2 = str2 + " TEXT(CW_ST_DATE -91 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYW_ST_DATE -91,\"mm/dd/yyyy hh24:mi:ss \") ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                            str2 = str2 + " TEXT(CM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_DAY +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        } else {
                            str2 = str2 + " TEXT(CM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE +.99999 ,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_ST_DATE -365,\"mm/dd/yyyy hh24:mi:ss \")  ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST QTR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                            str2 = str2 + " TEXT(CQ_ST_DATE -730,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_DAY   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LYQ_ST_DATE-730,\"mm/dd/yyyy hh24:mi:ss \") ";
                        } else {
                            str2 = str2 + " TEXT(CQ_ST_DATE -730,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LYQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(LYQ_ST_DATE-730,\"mm/dd/yyyy hh24:mi:ss \") ";
                        }

                    } else {
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_DAY  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                        } else {
                            str2 = str2 + " TEXT(CY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(LY_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate = DATE(\"" + d1 + "\",\"MM/dd/yyyy\") ";
                //out.println(str2);

            }

            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                ed_d = rs2.getString(1);
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }
    ////////////////////////////

    /////////////////////////
    public void setExcelMonthTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String getmonth) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {


            str2 = "select TEXT(CM_END_DATE ,\"mm/dd/yyyy hh24:mi:ss \"), ";

            {
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                        str2 = str2 + " TEXT(CM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYM_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                        str2 = str2 + " TEXT(CM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYM_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                        str2 = str2 + " TEXT(CM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYY_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    } else {
                        str2 = str2 + " TEXT(CM_ST_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  , TEXT(trunc(PYY_END_DATE) +.99999,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE -370,\"mm/dd/yyyy hh24:mi:ss \")  ";
                    }


                } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") ,  TEXT(PYM_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMYQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \"),  TEXT(PMYQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                        str2 = str2 + " TEXT(CMQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMQ_ST_DATE -740 ,\"mm/dd/yyyy hh24:mi:ss \") ";
                    } else {
                        str2 = str2 + " TEXT(CMQ_ST_DATE -740,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMYQ_END_DATE   ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PMYQ_ST_DATE  -740,\"mm/dd/yyyy hh24:mi:ss \") ";
                    }

                } else {
                    if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                        str2 = str2 + " TEXT(CMY_START_DATE-1460 ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYM_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE-1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    } else {
                        str2 = str2 + " TEXT(CMY_START_DATE-1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(PYY_START_DATE-1460,\"mm/dd/yyyy hh24:mi:ss \")   ";
                    }
                }
            }
            str2 = str2 + " from  prg_acn_mon_denom where MON_code = \"" + getmonth + "\" ";
            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }

    ////////////////////////////
    public void setExcelYearRange(String PRG_PERIOD_TYPE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            str2 = "select TEXT(c_end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";
            str2 = str2 + " TEXT(C_ST_DATE,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(p_end_date  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(p_st_date,\"mm/dd/yyyy hh24:mi:ss \")   ";


            str2 = str2 + " from  pr_year_denom where  DATE(\"" + d1 + "\",\"MM/dd/yyyy\") between c_st_date  and c_end_date ";

            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }

        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }

    public void setExcelYearTrendRange(String PRG_PERIOD_TYPE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        try {
            str2 = "select TEXT(c_end_date ,\"mm/dd/yyyy hh24:mi:ss \"), ";
            str2 = str2 + " TEXT(C_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(P_END_DATE  ,\"mm/dd/yyyy hh24:mi:ss \") , TEXT(p_ST_DATE -1460,\"mm/dd/yyyy hh24:mi:ss \")   ";


            str2 = str2 + " from  pr_year_denom where  DATE(\"" + d1 + "\",\"MM/dd/yyyy\") between c_st_date  and c_end_date ";

            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                p_ed_d = rs2.getString(3);
                p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  DATE(\"" + st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";
        pd_clu = " between  DATE(\"" + p_st_d + "\",\"mm/dd/yyyy hh24:mi:ss \") and  DATE(\"" + p_ed_d + "\",\"mm/dd/yyyy hh24:mi:ss \") ";


    }
    /////////////

    public void setExcelStdYearRange(String Year1, String Year2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        String str2 = "";
        ProgenParam param = new ProgenParam();
        param.elementId = this.elementID;
        if (Year1 == null || Year1.equalsIgnoreCase("NULL") || Year1.equalsIgnoreCase("")) {
            //Year1 = param.getYearforpage();
            Year1 = "2008";
        }
        if (Year2 == null || Year2.equalsIgnoreCase("NULL") || Year2.equalsIgnoreCase("")) {
            //Year2 = (Integer.parseInt(param.getYearforpage()) -1 )+"";
            Year2 = "2007";
        }

        d_clu = " = " + Year1;
        pd_clu = " = " + Year2;


    }

    public void setExcelStdYearTrendRange(String Year1, String Year2) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        ResultSet rs2 = null;
        ProgenParam param = new ProgenParam();

        String str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year1;
        String str3 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year2;
        if (Year1 == null || Year1.equalsIgnoreCase("NULL")) {
            Year1 = param.getYearforpage();
        }
        if (Year2 == null || Year2.equalsIgnoreCase("NULL")) {
            Year2 = (Integer.parseInt(param.getYearforpage()) - 1) + "";
        }


        try {
            str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year1;




            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
//                ed_d = rs2.getString(1);
                if (rs2.getString(1).contains("00:00:00")) {
                    ed_d = rs2.getString(1);
                } else {
                    ed_d = rs2.getString(1) + " " + "23:59:59 ";
                }
//                ed_d = rs2.getString(1)+ " " + "23:59:59 ";
                st_d = rs2.getString(2);
                //p_ed_d = rs2.getString(3);
                //p_st_d = rs2.getString(4);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }


        try {
            str2 = "select cyear , cyear-4 from  " + timeTableName + "  where CY_CUST_NAME =" + Year2;

            //out.println(str2);
            con = getConnection(elementID);
            st2 = con.prepareCall(str2);
            rs2 = st2.executeQuery();

            while (rs2.next()) {
                //ed_d = rs2.getString(1);
                //st_d = rs2.getString(2);
                p_ed_d = rs2.getString(1);
                p_st_d = rs2.getString(2);


                //out.println("insert into Item_master values(" + rs.getString(1)+ ",\""+ rs.getString(2) + "\",\"" + rs.getString(3) +"\",\"" + rs.getString(4) +"\"," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception:", e);
        } finally {
            if (rs2 != null) {
                rs2.close();
            }
            if (st2 != null) {
                st2.close();
            }

            if (con != null) {
                con.close();
            }
        }

        d_clu = " between  " + st_d + " and  " + ed_d + " ";
        pd_clu = " between  " + p_st_d + " and  " + p_ed_d + " ";
    }

    ////////////////////////////
    public ArrayList getExcelTimeViewbycols(ArrayList timeArray) {
        ArrayList viewOrderBy = new ArrayList();
        if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
            viewOrderBy.add("CY_CUST_NAME ");
            viewOrderBy.add(" PROGEN_TIME.CYEAR  ");
        } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
            viewOrderBy.add(" CQ_CUST_NAME");
            viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || \"-\" || PROGEN_TIME.CQTR ");
        }

        return (viewOrderBy);
    }

    public String getExcelTimeViewbyQuery(ArrayList timeArray) {
        String viewOrderBy = "";
        if (timeArray.get(0).toString().equalsIgnoreCase("YEAR")) {
            viewOrderBy = ("Select distinct CY_CUST_NAME , CY_CUST_NAME from  " + timeTableName + "  order by CYEAR  ");
        } else if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") || timeArray.get(0).toString().equalsIgnoreCase("QTR")) {
            viewOrderBy = ("Select distinct CQ_CUST_NAME , CQ_CUST_NAME from  " + timeTableName + "  order by CYEAR desc , CQTR  ");

        }

        return (viewOrderBy);
    }

    public ArrayList getExcelTimeViewbycols(String timeLevel, String PRG_PERIOD_TYPE, String PRG_COMPARE, String Currval) {
        ArrayList viewOrderBy = new ArrayList();
        if (timeLevel.equalsIgnoreCase("Day")) {
            if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                viewOrderBy.add("TEXT(PROGEN_TIME.ST_DATE,\"dd-mm-yy\")");
                viewOrderBy.add(" trunc(PROGEN_TIME.ST_DATE) ");
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {

                viewOrderBy.add(" CW_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CWYEAR || \"-\" || PROGEN_TIME.CWEEK ");
            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                viewOrderBy.add(" CM_CUST_NAME ");
                viewOrderBy.add(" TEXT(ST_DATE,\"YYYY-MM\") ");
            } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                viewOrderBy.add(" CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || \"-\" || PROGEN_TIME.CQTR ");
            } else {
                viewOrderBy.add(" CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR ");
            }



        }
        if (timeLevel.equalsIgnoreCase("Month")) {
            if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {

                viewOrderBy.add(" CM_CUST_NAME ");
                viewOrderBy.add(" TEXT(ST_DATE,\"YYYY-MM\") ");
            } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                viewOrderBy.add(" CQ_CUST_NAME");
                viewOrderBy.add(" PROGEN_TIME.CQ_YEAR || \"-\" || PROGEN_TIME.CQTR ");
            } else {
                viewOrderBy.add(" CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR ");
            }



        }
        if (timeLevel.equalsIgnoreCase("Year")) {
            {
                viewOrderBy.add(" CY_CUST_NAME ");
                viewOrderBy.add(" PROGEN_TIME.CYEAR ");
            }



        }
        return (viewOrderBy);
    }

    public String getExcelTimeViewbyQuery(String timeLevel, String PRG_PERIOD_TYPE, String PRG_COMPARE, String Currval) {
        String viewOrderBy = "";
        if (timeLevel.equalsIgnoreCase("Day")) {
            if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                viewOrderBy = ("Select distinct TEXT(PROGEN_TIME.ST_DATE,\"dd-mm-yy\") , TEXT(PROGEN_TIME.ST_DATE,\"dd-mm-yy\") "
                        + " from  " + timeTableName + "  order by trunc(PROGEN_TIME.ST_DATE)  ");

            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                viewOrderBy = ("Select distinct CW_CUST_NAME , CW_CUST_NAME "
                        + " from  " + timeTableName + "  order by CWYEAR, CWEEK ");

            } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                viewOrderBy = ("Select distinct CM_CUST_NAME , CM_CUST_NAME "
                        + " from  " + timeTableName + "  order by TEXT(ST_DATE,\"YYYY-MM\") ");
            } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                viewOrderBy = ("Select distinct CQ_CUST_NAME , CQ_CUST_NAME "
                        + " from  " + timeTableName + "  order by CQ_YEAR, CQTR ");

            } else {
                viewOrderBy = ("Select distinct CY_CUST_NAME , CY_CUST_NAME "
                        + " from  " + timeTableName + "  order by CYEAR, CYEAR ");
            }



        }

        return (viewOrderBy);
    }

    ////////////////////////////
    public Connection getConnection(String elementId) {
        Connection connection = null;
        /*
         * PbReturnObject retObj = null; //String
         * getConnectionIdByElementIdQuery =
         * resBundle.getString("getConnectionIdByElementId"); String finalQuery
         * = "select DISTINCT a.USER_NAME, a.PASSWORD, a.SERVER, a.SERVICE_ID,
         * a.SERVICE_NAME, a.PORT, a.DSN_NAME, a.DB_CONNECTION_TYPE " + "from
         * PRG_USER_CONNECTIONS a,PRG_USER_ALL_INFO_DETAILS b where
         * a.connection_id=b.connection_id and b.element_id= "+elementId+" ";
         *
         * String serverName = ""; String dsnName = ""; String connType = "";
         * String serverPort = ""; String serviceId = ""; String userName = "";
         * String password = ""; String server = ""; String dbName = "";
         *
         * Object[] Obj = new Object[1]; Obj[0] = elementId; String[]
         * tableColumnNames = null;
         *
         * try { //finalQuery = buildQuery(getConnectionIdByElementIdQuery,
         * Obj);
         *
         * retObj = execSelectSQL(finalQuery); tableColumnNames =
         * retObj.getColumnNames();
         *
         * serverName = retObj.getFieldValueString(0, tableColumnNames[4]);
         * serverPort = retObj.getFieldValueString(0, tableColumnNames[5]);
         * serviceId = retObj.getFieldValueString(0, tableColumnNames[3]);
         * userName = retObj.getFieldValueString(0, tableColumnNames[0]);
         * password = retObj.getFieldValueString(0, tableColumnNames[1]); server
         * = retObj.getFieldValueString(0, tableColumnNames[2]); dsnName =
         * retObj.getFieldValueString(0, tableColumnNames[6]); connType =
         * retObj.getFieldValueString(0, tableColumnNames[7]); dbName =
         * retObj.getFieldValueString(0, tableColumnNames[8]);
         *
         * if (connType != null && connType.equalsIgnoreCase("oracle")) {
         * Class.forName("oracle.jdbc.driver.OracleDriver");
         *
         * connection = DriverManager.getConnection("jdbc:oracle:thin:@" +
         * server + ":" + serverPort + ":" + serviceId + "", userName,
         * password); } else if (connType != null &&
         * connType.equalsIgnoreCase("excel")) { } else if (connType != null &&
         * connType.equalsIgnoreCase("mysql")) {
         * Class.forName("com.mysql.jdbc.Driver"); connection =
         * DriverManager.getConnection("jdbc:mysql://" + server + ":" +
         * serverPort + "/" + dbName, userName, password); } else if (connType
         * != null && connType.equalsIgnoreCase("PostgreSQL")) { connection =
         * ProgenConnection.postgresqlConnection(server, serverPort, dbName,
         * userName, password); } return connection; } catch (Exception exp) {
         * logger.error("Exception:",exp); return connection;
        }
         */

        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public Connection getConnectionByConnId(String connId) {
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionByConId(connId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    public String getTimeWhereClause() {
        return ("");

    }

    public void setElementID(String elementId) {
        this.elementID = elementId;
    }

    public String getCurrentPeriodName(String date, String timeLevel) {
        String periodName = null;
        String query = null;

        GetConnectionType gettypeofconn = new GetConnectionType();
        connType = gettypeofconn.getConTypeByElementId(elementID);
        if (connType.equalsIgnoreCase("Oracle")) {
            if ("Month".equalsIgnoreCase(timeLevel)) {
                query = "select CM_CUST_NAME from pr_day_denom where ddate=to_date('" + date + "','MM/DD/YYYY')";
            } else if ("Quarter".equalsIgnoreCase(timeLevel) || "Qtr".equalsIgnoreCase(timeLevel)) {
                query = "select CQ_CUST_NAME from pr_day_denom where ddate=to_date('" + date + "','MM/DD/YYYY')";
            } else if ("Year".equalsIgnoreCase(timeLevel)) {
                query = "select CY_CUST_NAME from pr_day_denom where ddate=to_date('" + date + "','MM/DD/YYYY')";
            }
        }

        if (query != null) {
            Connection con = null;
            Statement st = null;
            ResultSet rs2 = null;
            try {
                con = getConnection(elementID);
                st = con.createStatement();
                rs2 = st.executeQuery(query);

                if (rs2 != null) {
                    if (rs2.next()) {
                        periodName = rs2.getString(1);
                    }
                }

                rs2.close();
                st.close();
                con.close();

                rs2 = null;
                st = null;
                con = null;
            } catch (Exception e) {
                logger.error("Exception:", e);
            } finally {
                try {
                    if (rs2 != null) {
                        rs2.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
        return periodName;
    }

    public ArrayList<Integer> breakDateNumber(String d1) {
        ArrayList yearMonthDay = new ArrayList<Integer>();
        if(d1.length()==9)
        {
          yearMonthDay.add(Integer.parseInt(d1.substring(5, 9)));
          yearMonthDay.add(Integer.parseInt(d1.substring(0, 1)));
          yearMonthDay.add(Integer.parseInt(d1.substring(2, 4)));
        }
 else{
        yearMonthDay.add(Integer.parseInt(d1.substring(6, 10)));
        yearMonthDay.add(Integer.parseInt(d1.substring(0, 2)) - 1);
        yearMonthDay.add(Integer.parseInt(d1.substring(3, 5)));
//        return (yearMonthDay);
    }
        return(yearMonthDay);
    }

    public void setDateJava(ArrayList timeDetails) {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs1 = null;
        PreparedStatement prest = null;

        String PRG_PERIOD_TYPE = "Month";
        String PRG_COMPARE = "Period";
        String d1;

        /*
         * First Handling Day Std
         */
        if (timeDetails.get(0).toString().equalsIgnoreCase("Day")) {
            if (timeDetails.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                PRG_PERIOD_TYPE = timeDetails.get(3).toString();
                PRG_COMPARE = timeDetails.get(4).toString();
                d1 = timeDetails.get(2).toString();

                if (PRG_COMPARE == null) {
                    PRG_COMPARE = "PERIOD";
                }
                if (PRG_PERIOD_TYPE == null) {
                    PRG_PERIOD_TYPE = "Month";
                }

                ArrayList<Integer> breakdate = new ArrayList<Integer>();
                breakdate = breakDateNumber(d1);

                //java.util.Calendar newCalDate =  Calendar.getInstance();
                java.sql.Date currDate = new Date(breakdate.get(0), breakdate.get(1), breakdate.get(2));

                try {
                    con = getConnection(elementID);
                    prest = con.prepareStatement(daySql);
                    prest.setDate(1, currDate);

                    rs1 = prest.executeQuery();


                    while (rs1.next()) {

                        ded_d = rs1.getDate("END_DATE");

                        if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY") || PRG_PERIOD_TYPE.equalsIgnoreCase("HOURS")) {
                            dst_d = rs1.getDate("ST_DATE");
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                                dp_ed_d = rs1.getDate("P_ST_DATE");
                                dp_st_d = rs1.getDate("P_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Year") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Week")) {
                                dp_ed_d = rs1.getDate("LY_DAY");
                                dp_st_d = rs1.getDate("LY_DAY");
                            } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Same Day Last Month")) {
                                dp_ed_d = rs1.getDate("P_ST_DATE");
                                dp_st_d = rs1.getDate("P_ST_DATE");
                            } else {
                                dp_ed_d = rs1.getDate("LY_DAY");
                                dp_st_d = rs1.getDate("LY_DAY");
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                            dst_d = rs1.getDate("CW_ST_DATE");
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK")) {
                                dp_ed_d = rs1.getDate("PW_DAY");
                                dp_st_d = rs1.getDate("PW_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                                dp_ed_d = rs1.getDate("LYW_DAY");
                                dp_st_d = rs1.getDate("LYW_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Week")) {
                                dp_ed_d = rs1.getDate("PW_END_DATE");
                                dp_st_d = rs1.getDate("PW_ST_DATE");
                            } else {
                                dp_ed_d = rs1.getDate("LYW_END_DATE");
                                dp_st_d = rs1.getDate("LYW_ST_DATE");
                            }

                        } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                            dst_d = rs1.getDate("CM_ST_DATE");
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                                dp_ed_d = rs1.getDate("PM_DAY");
                                dp_st_d = rs1.getDate("PM_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Month Last Year")) {
                                dp_ed_d = rs1.getDate("PYM_DAY");
                                dp_st_d = rs1.getDate("PYM_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Month")) {
                                dp_ed_d = rs1.getDate("PYM_ST_DATE");
                                dp_st_d = rs1.getDate("PM_ST_DATE");
                            } else {
                                dp_ed_d = rs1.getDate("PYM_END_DATE");
                                dp_st_d = rs1.getDate("PYM_ST_DATE");
                            }


                        } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR") || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER"))) {
                            dst_d = rs1.getDate("CQ_ST_DATE");
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                                dp_ed_d = rs1.getDate("LQ_DAY");
                                dp_st_d = rs1.getDate("LQ_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("Same Qtr Last Year")) {
                                dp_ed_d = rs1.getDate("LYQ_DAY");
                                dp_st_d = rs1.getDate("LYQ_ST_DATE");
                            } else if (PRG_COMPARE.equalsIgnoreCase("PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD COMPLETE") || PRG_COMPARE.equalsIgnoreCase("Complete Last Qtr")) {
                                dp_ed_d = rs1.getDate("LQ_END_DATE");
                                dp_st_d = rs1.getDate("LQ_ST_DATE");
                            } else {
                                dp_ed_d = rs1.getDate("LYQ_END_DATE");
                                dp_st_d = rs1.getDate("LYQ_ST_DATE");
                            }

                        } else {
                            dp_ed_d = rs1.getDate("CY_ST_DATE");
                            if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                                dp_ed_d = rs1.getDate("LY_DAY");
                                dp_st_d = rs1.getDate("LY_ST_DATE");
                            } else {
                                dp_ed_d = rs1.getDate("LY_END_DATE");
                                dp_st_d = rs1.getDate("LY_ST_DATE");
                            }
                        }

                    }
                    Calendar newCal = GregorianCalendar.getInstance();
                    newCal.setTimeInMillis(ded_d.getTime());







                    /// Got the param in date format





//                    ded_d.setHours(23);
//                    ded_d.setMinutes(59);
//                    ded_d.setSeconds(59);






                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }



            }

        }



    }

    public void setJavaTrendRange(String PRG_PERIOD_TYPE, String PRG_COMPARE, String d1) throws SQLException {
        Connection con = null;
        CallableStatement st2 = null;
        Statement st = null;
        ResultSet rs2 = null;
        PreparedStatement prest = null;
        String str2 = "", interval = null, timeInterval = null, toChar = null, dateFormat = null, dateType = null,
                onlyDate = null, onlymysql = null, periodType = null, mysqlInterval = null;

        GetConnectionType getconntype = new GetConnectionType();
        connType = getconntype.getConTypeByElementId(elementID);

        {
            ArrayList<Integer> breakdate = new ArrayList<Integer>();
            breakdate = breakDateNumber(d1);

            //java.util.Calendar newCalDate =  Calendar.getInstance();
            java.sql.Date currDate = new Date(breakdate.get(0) - 1900, breakdate.get(1), breakdate.get(2));


            try {


                {
                    if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                        str2 = "select distinct end_date , ";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("Previous Day")) {
                            str2 = str2 + " ST_DATE   , P_ST_DATE , P_ST_DATE  ";
                        } else {
                            str2 = str2 + " ST_DATE   , LYW_DAY    , LYW_DAY   ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                        str2 = "select distinct END_DATE , ";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR") || PRG_COMPARE.equalsIgnoreCase("LAST WEEK") || PRG_COMPARE.equalsIgnoreCase("Same Week Last Year")) {
                            str2 = str2 + " CW_ST_DATE  , LYW_DAY   , LYW_ST_DATE  ";
                        } else {
                            str2 = str2 + " CW_ST_DATE  , LYW_END_DATE    , LYW_ST_DATE ";
                        }

                    } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("MONTH")) {
                        str2 = "select distinct end_date , ";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD")
                                || PRG_COMPARE.equalsIgnoreCase("LAST MONTH")) {
                            str2 = str2 + " CM_ST_DATE  , PM_DAY  ,  PM_ST_DATE  ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("LAST MONTH COMPLETE")
                                || PRG_COMPARE.equalsIgnoreCase("COMPLETE LAST MONTH")) {
                            str2 = str2 + " CM_ST_DATE  , PM_END_DATE  ,  PM_ST_DATE  ";
                        } else {
                            str2 = str2 + " CM_ST_DATE  , PYM_END_DATE  ,  PYM_ST_DATE   ";
                        }


                    } else if ((PRG_PERIOD_TYPE.equalsIgnoreCase("QTR")
                            || PRG_PERIOD_TYPE.equalsIgnoreCase("QUARTER")
                            || PRG_PERIOD_TYPE.equalsIgnoreCase("LAST QUARTER"))) {
                        str2 = "select distinct end_date , ";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST QTR")) {
                            str2 = str2 + " CQ_ST_DATE  , LQ_DAY   ,  LQ_ST_DATE ";
                        } else if (PRG_COMPARE.equalsIgnoreCase("LAST QTR COMPLETE")
                                || PRG_COMPARE.equalsIgnoreCase("COMPLETE QTR MONTH")) {
                            str2 = str2 + " CQ_ST_DATE  , LQ_END_DATE   ,  LQ_ST_DATE ";
                        } else {

                            str2 = str2 + " CQ_ST_DATE  , LYQ_END_DATE  ,  LYQ_ST_DATE ";
                        }

                    } else {
                        str2 = "select distinct end_date , ";
                        if (PRG_COMPARE.equalsIgnoreCase("PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST PERIOD") || PRG_COMPARE.equalsIgnoreCase("LAST YEAR")) {
                            str2 = str2 + " CY_ST_DATE  , LY_DAY   , LY_ST_DATE    ";
                        } else {
                            str2 = str2 + " CY_ST_DATE  , LY_END_DATE , LY_ST_DATE    ";
                        }
                    }
                }
                str2 = str2 + " from   " + timeTableName + "  where ddate <= ? order by 1 desc ";

                con = getConnection(elementID);
                prest = con.prepareStatement(str2);
                prest.setDate(1, currDate);

//

                rs2 = prest.executeQuery();
                PbReturnObject retObj = new PbReturnObject(rs2);
                if (rs2 != null) {
                    rs2.close();
                }
                if (retObj == null || retObj.rowCount == 0) {
                    ded_d = currDate;
                    dst_d = currDate;
                    dp_ed_d = currDate;
                    dp_st_d = currDate;

                }
                if (PRG_PERIOD_TYPE.equalsIgnoreCase("DAY")) {
                    ded_d = currDate;
                    dst_d = retObj.getFieldValueDate(0, 1);
                    int finalRow = 30;
                    if (finalRow >= retObj.rowCount) {
                        finalRow = retObj.rowCount;
                    }
                    dst_d = retObj.getFieldValueDate(finalRow, 1);
                    dp_ed_d = retObj.getFieldValueDate(0, 2);
                    dp_st_d = retObj.getFieldValueDate(finalRow, 3);

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("WEEK")) {
                    ded_d = currDate;
                    //dst_d = retObj.getFieldValueDate(0, 1);
                    int finalRow = 49;
                    if (finalRow >= retObj.rowCount) {
                        finalRow = retObj.rowCount;
                    }
                    dst_d = retObj.getFieldValueDate(finalRow, 1);
                    dp_ed_d = retObj.getFieldValueDate(0, 2);
                    dp_st_d = retObj.getFieldValueDate(finalRow, 3);

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Month")) {
                    ded_d = currDate;
                    //dst_d = retObj.getFieldValueDate(0, 1);
                    int finalRow = 364;
                    if (finalRow >= retObj.rowCount) {
                        finalRow = retObj.rowCount;
                    }
                    dst_d = retObj.getFieldValueDate(finalRow, 1);
                    dp_ed_d = retObj.getFieldValueDate(0, 2);
                    dp_st_d = retObj.getFieldValueDate(finalRow, 3);

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("Qtr")) {
                    ded_d = currDate;
                    //dst_d = retObj.getFieldValueDate(0, 1);
                    int finalRow = 731;
                    if (finalRow >= retObj.rowCount) {
                        finalRow = retObj.rowCount;
                    }
                    dst_d = retObj.getFieldValueDate(finalRow, 1);
                    dp_ed_d = retObj.getFieldValueDate(0, 2);
                    dp_st_d = retObj.getFieldValueDate(finalRow, 3);

                } else if (PRG_PERIOD_TYPE.equalsIgnoreCase("year")) {
                    ded_d = currDate;

                    int finalRow = 1462;
                    if (finalRow >= retObj.rowCount) {
                        finalRow = retObj.rowCount;
                    }
                    dst_d = retObj.getFieldValueDate(finalRow, 1);
                    dp_ed_d = retObj.getFieldValueDate(0, 2);
                    dp_st_d = retObj.getFieldValueDate(finalRow, 3);

                }



            } catch (SQLException e) {
                System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (rs2 != null) {
                    rs2.close();
                }
                if (st2 != null) {
                    st2.close();
                }

                if (con != null) {
                    con.close();
                }
            }






//           st_d = (dst_d.getMonth()+1) + "/" + (dst_d.getDate()) + "/" + (dst_d.getYear()+1900) + " 00:00:00";
////           ed_d = (ded_d.getMonth()+1) + "/" + (ded_d.getDate()) + "/" + (ded_d.getYear()+1900) + " 00:00:00";;
//          ed_d = (ded_d.getMonth()+1) + "/" + (ded_d.getDate()) + "/" + (ded_d.getYear()+1900) + " 23:59:59";;
//           p_st_d =(dp_st_d.getMonth()+1) + "/" + (dp_st_d.getDate()) + "/" + (dp_st_d.getYear()+1900) + " 23:59:59";
//           p_ed_d =(dp_ed_d.getMonth()+1)+ "/" + (dp_ed_d.getDate()) + "/" + (dp_ed_d.getYear()+1900) + " 23:59:59";

            st_d = (dst_d.getMonth() + 1) + "/" + (dst_d.getDate()) + "/" + (dst_d.getYear() + 1900);
//           ed_d = (ded_d.getMonth()+1) + "/" + (ded_d.getDate()) + "/" + (ded_d.getYear()+1900) + " 00:00:00";;
            ed_d = (ded_d.getMonth() + 1) + "/" + (ded_d.getDate()) + "/" + (ded_d.getYear() + 1900);
            p_st_d = (dp_st_d.getMonth() + 1) + "/" + (dp_st_d.getDate()) + "/" + (dp_st_d.getYear() + 1900);
            p_ed_d = (dp_ed_d.getMonth() + 1) + "/" + (dp_ed_d.getDate()) + "/" + (dp_ed_d.getYear() + 1900);

            if (st_d.contains("00:00:00.0")) {
                st_d = st_d.replace("00:00:00.0", "") + " 00:00:00";
            } else if (!st_d.contains("00:00:00")) {
                st_d = st_d + " 00:00:00";
            }
            if (!p_st_d.contains("00:00:00.0")) {
                p_st_d = p_st_d.replace("00:00:00.0", "") + " 00:00:00";
            } else if (!p_st_d.contains("00:00:00")) {
                p_st_d = p_st_d + " 00:00:00";
            }


            ed_d = ed_d.replace("00:00:00", "") + " 23:59:59";
            p_ed_d = p_ed_d.replace("00:00:00", "") + " 23:59:59";








            if (connType.equalsIgnoreCase("Oracle")) {
                d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
            } else if (connType.equalsIgnoreCase("Sqlserver")) {
                d_clu = " between  convert(datetime,'" + st_d + "',120) and  convert(datetime,'" + ed_d + "',120) ";
                pd_clu = " between  convert(datetime,'" + p_st_d + "',120) and  convert(datetime,'" + p_ed_d + "',120) ";

            } else if (connType.equalsIgnoreCase("Mysql")) {
                d_clu = " between str_to_date('" + st_d + "','%m/%d/%Y %H:%i:%s') and str_to_date('" + ed_d + "','%m/%d/%Y %H:%i:%s')  ";
                pd_clu = " between str_to_date('" + p_st_d + "','%m/%d/%Y %H:%i:%s') and str_to_date('" + p_ed_d + "','%m/%d/%Y %H:%i:%s')  ";

            } else {
                d_clu = " between  to_date('" + st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";
                pd_clu = " between  to_date('" + p_st_d + "','mm/dd/yyyy hh24:mi:ss ') and  to_date('" + p_ed_d + "','mm/dd/yyyy hh24:mi:ss ') ";

            }

        }

        ////.println("d_clu"+d_clu);
        ////.println("pd_clu"+pd_clu);

    }
}
