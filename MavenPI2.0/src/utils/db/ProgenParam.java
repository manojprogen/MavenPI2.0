/*
 * ProgenParam.java
 *
 * Created on June 26, 2008, 9:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package utils.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.contypes.GetConnectionType;
import com.progen.report.PbReportCollection;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author achandak
 */
public class ProgenParam extends PbDb {

    Connection con;
    CallableStatement st;
    Statement st2;
    ResultSet rs;
    CallableStatement st1;
    ResultSet rs1;
    public String elementId;
    public String SYTM = "";
    private String[] filtertype = {"NOTIN", "LIKE", "NOTLIKE"};
    static HashMap<String, ArrayList<String>> initialDefaultFilter = new HashMap<String, ArrayList<String>>();
    //ProgenConnection pg;

    /**
     * Creates a new instance of ProgenParam
     */
    public ProgenParam() {
        this.con = null;
        this.st = null;
        this.rs = null;
        this.st1 = null;
        this.rs1 = null;
        this.st2 = null;

        // Creates a new instan
    }

    public String getDefaultSystemTime(String reqType, String reqFormat, int diff) {
        String dTime = "";
        String str1 = "";
        if (reqType == null) {
            reqType = "DATE";
        }
        if (reqFormat == null) {
            if (reqType.equalsIgnoreCase("DATE")) {
                reqFormat = "MM/DD/YYYY";
            } else if (reqType.equalsIgnoreCase("MONTH")) {
                reqFormat = "MON-YYYY";
            } else if (reqType.equalsIgnoreCase("QTR")) {
                reqFormat = "MON-YYYY";
            } else {
                reqFormat = "YYYY";
            }
        }


        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                PbReturnObject pbRet = null;
                //str1 = "select to_char(sysdate,'MM/dd/yyyy') as D1 from dual";
                //str1 = "select '10/31/2009' as D1 from dual";

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    str1 = "select convert(varchar,isnull(case when setup_date_value is null then  case when Upper(setup_char_value) = 'YESTERDAY' then (getdate() -1) when Upper(setup_char_value) = 'TOMORROW' then (getdate() +1) else null end else setup_date_value  end- " + diff + ",getdate()- " + diff + "),101) as D1 from "
                            + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                    Statement str = con.createStatement();
                    ResultSet rs = str.executeQuery(str1);
                    pbRet = new PbReturnObject(rs);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
//                    str1 = "select convert(varchar,isnull(case when setup_date_value is null then  case when Upper(setup_char_value) = 'YESTERDAY' then (getdate() -1) when Upper(setup_char_value) = 'TOMORROW' then (getdate() +1) else null end else setup_date_value  end- " + diff + ",getdate()- " + diff + "),101) as D1 from "
//                            + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                    str1 = "select DATE_FORMAT(CASE WHEN setup_date_value IS NULL then case when Upper(setup_char_value) = 'YESTERDAY' then DATE_SUB(CURDATE(), INTERVAL 1 DAY) when Upper(setup_char_value) = 'TOMORROW' then DATE_ADD(CURDATE(), INTERVAL 1 DAY) else null end Else Setup_Date_Value end, '%m/%d/%Y ')from "
                            + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";

                    Statement str = con.createStatement();
                    pbRet = execSelectSQL(str1);
                } else {
                    str1 = "select to_char(nvl(case when setup_date_value is null then case when Upper(setup_char_value) = 'YESTERDAY' then (sysdate -1) when Upper(setup_char_value) = 'TOMORROW' then (sysdate +1) else null end else setup_date_value  end - " + diff + ",sysdate- " + diff + "),'" + reqFormat + "') as D1 from "
                            + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";

                    this.st1 = this.con.prepareCall(str1);
                    this.rs1 = this.st1.executeQuery();
                    pbRet = new PbReturnObject(rs1);
                }


                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (con != null) {
                    con.close();
                }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    dTime = pbRet.getFieldValueString(0, 0);
                } else {
                    dTime = pbRet.getFieldValueString(0, "D1");
                }
                //out.println(sqlstr);

            } catch (SQLException e) {
                logger.error("Exception in getDefaultSystemTime() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getDefaultSystemTime() !", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getDefaultSystemTime() Info Message!", ex);
                    }
                }
            }
        }
        return (dTime);
    }

    private String getCurrentSystemTime(String reqType, String reqFormat, String Value) {
        String dTime = "";
        String str1 = "";
        String[] vals = Value.split(",");
        if (reqType == null) {
            reqType = "DATE";
        }
        if (reqFormat == null) {
            if (reqType.equalsIgnoreCase("DATE")) {
                reqFormat = "MM/DD/YYYY";
            } else if (reqType.equalsIgnoreCase("MONTH")) {
                reqFormat = "MON-YYYY";
            } else if (reqType.equalsIgnoreCase("QTR")) {
                reqFormat = "MON-YYYY";
            } else {
                reqFormat = "YYYY";
            }
        }


        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                PbReturnObject pbRet = null;
                //str1 = "select to_char(sysdate,'MM/dd/yyyy') as D1 from dual";
                //str1 = "select '10/31/2009' as D1 from dual";
                if (vals[0].equalsIgnoreCase("newSysDate") || vals[0].equalsIgnoreCase("fromSysDate") || vals[0].equalsIgnoreCase("toSystDate") || vals[0].equalsIgnoreCase("CmpFrmSysDate") || vals[0].equalsIgnoreCase("cmptoSysDate")) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        str1 = "select convert(varchar,(getdate()" + vals[1] + vals[2] + "),101) as D1 ";
                        Statement str = con.createStatement();
                        ResultSet rs = str.executeQuery(str1);
                        pbRet = new PbReturnObject(rs);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        str1 = "select date_format(sysdate()" + vals[1] + " interval " + vals[2] + " day,'%m/%d/%Y') as D1 from dual";
                        //str1 = select date_format(sysdate() + interval 1 day,'%m/%d/%Y');
                        //added by mohit for mysql
                        this.st1 = this.con.prepareCall(str1);
                        this.rs1 = this.st1.executeQuery();
                        pbRet = new PbReturnObject(rs1);
                    } else {
                        str1 = "select to_char((sysdate" + vals[1] + vals[2] + "),'" + reqFormat + "') as D1 from dual";

                        this.st1 = this.con.prepareCall(str1);
                        this.rs1 = this.st1.executeQuery();
                        pbRet = new PbReturnObject(rs1);
                    }
                } else {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        str1 = "select convert(varchar,isnull(setup_date_value " + vals[1] + vals[2] + ",getdate()" + vals[1] + vals[2] + "),101) as D1 from "
                                + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                        Statement str = con.createStatement();
                        ResultSet rs = str.executeQuery(str1);
                        pbRet = new PbReturnObject(rs);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        str1 = "select date_format(coalesce(setup_date_value " + vals[1] + " interval " + vals[2] + " day,sysdate()" + vals[1] + " interval " + vals[2] + " day),'%m/%d/%Y') as D1 from "
                                + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";
                        //added by mohit for mysql
                        //str1 = "select date_format(sysdate()" + vals[1]+" interval "+vals[2]+" day,'%m/%d/%Y') as D1 from dual";
                        this.st1 = this.con.prepareCall(str1);
                        this.rs1 = this.st1.executeQuery();
                        pbRet = new PbReturnObject(rs1);
                    } else {
                        str1 = "select to_char(nvl(setup_date_value " + vals[1] + vals[2] + ",sysdate" + vals[1] + vals[2] + "),'" + reqFormat + "') as D1 from "
                                + " prg_gbl_setup_values where setup_key ='SYSTEM_DATE'";

                        this.st1 = this.con.prepareCall(str1);
                        this.rs1 = this.st1.executeQuery();
                        pbRet = new PbReturnObject(rs1);
                    }
                }


                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (con != null) {
                    con.close();
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);

                dTime = pbRet.getFieldValueString(0, "D1");
                //out.println(sqlstr);

            } catch (SQLException e) {
                logger.error("Exception in getCurrentSystemTime() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }
            }
        }
        return (dTime);
    }

    private String getCurrentSystemTime(String reqType, String reqFormat, int Value) {
        String dTime = "";
        String str1 = "";
        if (reqType == null) {
            reqType = "DATE";
        }
        if (reqFormat == null) {
            if (reqType.equalsIgnoreCase("DATE")) {
                reqFormat = "MM/DD/YYYY";
            } else if (reqType.equalsIgnoreCase("MONTH")) {
                reqFormat = "MON-YYYY";
            } else if (reqType.equalsIgnoreCase("QTR")) {
                reqFormat = "MON-YYYY";
            } else {
                reqFormat = "YYYY";
            }
        }
        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                PbReturnObject pbRet = null;
                //str1 = "select to_char(sysdate,'MM/dd/yyyy') as D1 from dual";
                //str1 = "select '10/31/2009' as D1 from dual";
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    str1 = "select convert(varchar,(getdate()- " + Value + "),101) as D1 ";
                    Statement str = con.createStatement();
                    ResultSet rs = str.executeQuery(str1);
                    pbRet = new PbReturnObject(rs);
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    str1 = "select  DATE_FORMAT(DATE_SUB(NOW(), INTERVAL " + Value + " DAY), '%m/%d/%Y') as D1";
                    Statement str = con.createStatement();
                    ResultSet rs = str.executeQuery(str1);
                    pbRet = new PbReturnObject(rs);
                } else {
                    str1 = "select to_char((sysdate- " + Value + "),'" + reqFormat + "') as D1 from dual";

                    this.st1 = this.con.prepareCall(str1);
                    this.rs1 = this.st1.executeQuery();
                    pbRet = new PbReturnObject(rs1);
                }


                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (st2 != null) {
                    st2.close();
                }
                if (con != null) {
                    con.close();
                }
                dTime = pbRet.getFieldValueString(0, "D1");

            } catch (SQLException e) {
                logger.error("Exception in getCurrentSystemTime() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getCurrentSystemTime() Info Message!", ex);
                    }
                }
            }
        }

        return (dTime);
    }

    public String getstdformHeader() {
        String sqlstr = "";
        sqlstr = "<form name=\"frmParameter\" action=\"trav_book_summ.jsp\" method=post >";
        return (sqlstr);
    }

    public String getstdformHeader(String frmName) {
        String sqlstr = "";
        sqlstr = "<form name=\"" + frmName + "\" action=\"trav_book_summ.jsp\" method=post >";
        return (sqlstr);
    }

    public String getstdformHeader(String frmName, String pAction) {
        String sqlstr = "";
        sqlstr = "<form name=\"" + frmName + "\" action=\"" + pAction + "\" method=post >";
        return (sqlstr);
    }

    public String getstdformfooter() {
        String sqlstr = "";
        sqlstr = "</form>";
        return (sqlstr);
    }

    public String getYearforpage() {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getDefaultSystemTime("YEAR", "YYYY", 0);
        //////.println("Get year for Page" + d1);
        return (d1);
    }

    public String getYearforpage(String diff) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getDefaultSystemTime("YEAR", "YYYY", Integer.parseInt(diff));
        //////.println("Get year for Page" + d1);

        return (d1);
    }

    public String getdateforpage() {
//        String sqlstr ="";
//        String str1 ="";
        String d1 = "";//"";
        d1 = getDefaultSystemTime("DATE", "MM/DD/YYYY", 0);



        return (d1);
    }

    public String getdateforpageNow(String value) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getDefaultSystemTime("DATE", "MM/DD/YYYY", 0);



        return (d1);
    }

    public String getdateforpage(int diff) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getDefaultSystemTime("DATE", "MM/DD/YYYY", diff);

        return (d1);
    }

    public String getcurrentdateforpage(String Value) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getCurrentSystemTime("DATE", "MM/DD/YYYY", Value);

        return (d1);
    }

    public String getcurrentdateforpage(int Value) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";
        d1 = getCurrentSystemTime("DATE", "MM/DD/YYYY", Value);

        return (d1);
    }
    /////////////

    public String getmonthforpage() {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";

        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                //str1 = "select to_char(sysdate,'MON-YY')  ";
                str1 = "select 'DEC-08' A1  ";
                str1 = str1 + " from dual ";


                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                PbReturnObject pbRet = new PbReturnObject(rs1);
                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                d1 = pbRet.getFieldValueString(0, 0);
            } catch (SQLException e) {
                logger.error("Exception In getmonthforpage() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }
            }



        }
        return (d1);
    }

    public String getmonthforpage(String diff) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";

        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = "select to_char(sysdate-" + diff + ",'MON-YY') A1 from dual ";
                // str1 = "select 'DEC-08' A1  ";
                //str1 = str1 + " from dual ";


                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                PbReturnObject pbRet = new PbReturnObject(rs1);
                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                d1 = pbRet.getFieldValueString(0, 0);
            } catch (SQLException e) {
                logger.error("Exception In getmonthforpage() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception In getmonthforpage()  Info Message!", ex);
                    }
                }
            }



        }
        return (d1);
    }

    public String getQtrforpage() {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";

        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                //str1 = "select to_char(sysdate,'MON-YY')  ";
                str1 = "select distinct  CQ_CUST_NAME A1  ";
                str1 = str1 + " from pr_day_denom where ST_DATE=sysdate ";


                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                PbReturnObject pbRet = new PbReturnObject(rs1);
                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                d1 = pbRet.getFieldValueString(0, 0);
            } catch (SQLException e) {
                logger.error("Exception in getQtrforpage() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }
            }



        }
        return (d1);
    }

    public String getqtrforpage(String diff) {
        String sqlstr = "";
        String str1 = "";
        String d1 = "";

        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = "select distinct  CQ_CUST_NAME A1  ";
                str1 = str1 + " from pr_day_denom where ST_DATE=(sysdate-" + diff + ") ";

                // str1 = "select 'DEC-08' A1  ";
                //str1 = str1 + " from dual ";


                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                PbReturnObject pbRet = new PbReturnObject(rs1);
                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
                //////////////////////////////////////////////////////////////////////////////////////////////////.println(sqlstr);
                d1 = pbRet.getFieldValueString(0, 0);
            } catch (SQLException e) {
                logger.error("Exception in getQtrforpage()  <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQtrforpage()  Info Message!", ex);
                    }
                }
            }



        }
        return (d1);
    }
    ///////////

    public String getTextAllb(String Name, String Label, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" class=\"myTextbox3\" value=\"" + CurrVal + "\" >";
        cbo = cbo + "<A  HREF=\"#2\" onClick=\"submitsearchp();\" ><img src=\"images/search.gif\" border=\"0\" width=\"10\" height=\"10\" /></A>";
        cbo = cbo + "</Td></Tr></Table>";

        /// end of query

        return (cbo);
    }
    /////////////

    ///////////
    public String getTextAllb(String Name, String Label, String CurrVal, String pJavaFun) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" class=\"myTextbox3\" value=\"" + CurrVal + "\" >";
        cbo = cbo + "<A  HREF=\"#2\" onClick=\"" + pJavaFun + "();\" ><img src=\"images/search.gif\" border=\"0\" width=\"10\" height=\"10\" /></A>";
        cbo = cbo + "</Td></Tr></Table>";

        /// end of query

        return (cbo);
    }
    /////////////

    public String getTextAllb2(String Name, String Label, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" class=\"myTextbox3\" value=\"" + CurrVal + "\" >";
        cbo = cbo + "<A  HREF=\"#2\" onClick=\"submitsearchprd();\" ><img src=\"images/search.gif\" border=\"0\" width=\"10\" height=\"10\" /></A>";
        cbo = cbo + "</Td></Tr></Table>";

        /// end of query

        return (cbo);
    }
    /////////////

    public String getdateforpage(String date1, String format1) {
        String sqlstr = "";
        String str1 = "";
        {
            try {

                this.con = getConnection(elementId);
                str1 = "select to_char(date1,format1) as D1 from dual";
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                PbReturnObject pbRet = new PbReturnObject(rs1);
                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }


                date1 = pbRet.getFieldValueString(0, 0);

            } catch (SQLException e) {
                logger.error("Exception in getdateforpage() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getdateforpage()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getdateforpage()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getdateforpage()  Info Message!", ex);
                    }
                }
            }



        }
        return (date1);

    }

    public String getQueryAllCombotb(String Name, String Label, String Query1, String CurrVal) throws Exception {
        String cbo = "";
        String str1 = "";
        String temp = "";
        PbReturnObject pbroisbucket = null;
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"1\" cellpadding=\"0\"  style=\"width:100%\"> ";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\"  class=\"myTextbox3\" > ";
        {
            try {
                //code to know IS bucket or not
               /*
                 * String isBucketQuery = "SELECT
                 * nvl(IS_BUCKET,'N'),buss_table_id,connection_id FROM
                 * PRG_USER_ALL_INFO_DETAILS where element_id in(" + elementId +
                 * ")"; pbroisbucket = execSelectSQL(isBucketQuery); String
                 * isbucket = pbroisbucket.getFieldValueString(0, 0); if
                 * (isbucket.equalsIgnoreCase("N")) { this.con =
                 * getConnection(elementId); str1 = Query1;
                 *
                 */
                /*
                 * } else if (isbucket.equalsIgnoreCase("Y")) { //String
                 * connection_id=String.valueOf(pbroisbucket.getFieldValueInt(0,2));
                 * // String str2="SELECT BUSS_TABLE_NAME, DB_QUERY FROM
                 * PRG_GRP_BUSS_TABLE where buss_table_id
                 * ="+pbroisbucket.getFieldValueInt(0, 1); String str2="SELECT
                 * DB_QUERY FROM PRG_GRP_BUSS_TABLE_SRC where buss_table_id
                 * ="+pbroisbucket.getFieldValueInt(0, 1);
                 * //////////////////////////////////////////////////////////.println("str2-
                 * 1- for bucket--"+str2); PbReturnObject str2pbro=
                 * execSelectSQL(str2); this.con = getConnection(elementId);
                 * str1="select BUCKET_NAME,BUCKET_DISP_VALUE from
                 * ("+str2pbro.getFieldValueClobString(0,"DB_QUERY")+")";
                 * //str1="select BUCKET_NAME,BUCKET_DISP_VALUE from
                 * ("+str2pbro.getFieldValueString(0,"DB_QUERY")+")";
                 * //////////////////////////////////////////////////////////.println("str1-
                 * 2- for bucket--"+str1);
                 *
                 * }
                 */ this.con = getConnection(elementId);
                GetConnectionType gettypeofconn = new GetConnectionType();
                ;
                str1 = Query1;
                if (gettypeofconn.getConTypeByElementId(elementId).equalsIgnoreCase("Sqlserver")) {
                    this.st2 = this.con.createStatement();
                    this.rs1 = this.st2.executeQuery(str1);
                } else {
                    this.st1 = this.con.prepareCall(str1);
                    this.rs1 = this.st1.executeQuery();
                }
                if (CurrVal.equalsIgnoreCase("All")) {
                    cbo = cbo + " <option selected value=\"All\" >All </option> ";
                } else {
                    //////////////////////////////////////////////////////////////.println("in all else");
                    cbo = cbo + " <option value=\"All\" >All</option> ";
                }



                int looper = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + " <option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option> ";
                    } else {
                        cbo = cbo + " <option value=\"" + temp + "\">" + rs1.getString(2) + "</option> ";
                    }
                    looper++;
                    if (looper > 100) {
                        break;
                    }
                }


                if (rs1 != null) {
                    rs1.close();
                    rs1 = null;
                }
                if (st1 != null) {
                    st1.close();
                    st1 = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
                logger.error("Exception in getQueryAllCombotb() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryAllCombotb()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryAllCombotb()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryAllCombotb()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";

        /// end of query

        return (cbo);
    }
    //added by uday start

    public String getQueryCombotb2(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        int i = 0;
        /// start of query

        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\" id=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = Query1;
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                i = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else {
                        cbo = cbo + ("<option value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    }
                    i++;
                }

                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Exception in getQueryCombotb2() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb2()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb2()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb2()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";

        /// end of query

        return (cbo);
    }

    public String getQueryCombotb4(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        int i = 0;
        /// start of query

        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select id=\"primaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = Query1;
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                i = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else {
                        cbo = cbo + ("<option value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    }
                    i++;
                }

                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Exception in getQueryCombotb4() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryCombotb4() Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb4()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb4()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";
        /// end of query

        return (cbo);
    }

    public String getQueryCombotb3(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        int i = 0;
        /// start of query

        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select id=\"secondaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = Query1;
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                i = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else {
                        cbo = cbo + ("<option value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    }
                    i++;
                }

                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Exception in getQueryCombotb3() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb3()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb3()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotb3()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</Td></Tr></Table>";
        // ////////////////////////////////////////////////////////////////////////////////////////.println("CBO--------" + cbo);

        /// end of query

        return (cbo);
    }

    //added by susheela start 12-01-10
    public String getQueryCombotbBasis(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        int i = 0;
        /// start of query

        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select id=\"tertiaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            try {

                this.con = ProgenConnection.getInstance().getConnection();
                str1 = Query1;
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                i = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else {
                        cbo = cbo + ("<option value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    }
                    i++;
                }

                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Exception in getQueryCombotbBasis() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotbBasis()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotbBasis()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error("Exception in getQueryCombotbBasis()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";
        /// end of query

        return (cbo);
    }
    //added by susheela over

    public ArrayList getRowEdges(String Query1, String currVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        ArrayList rowEdgeValues = new ArrayList();
        cbo = "";
        Statement st = null;
        rowEdgeValues.add(currVal);

        try {

            this.con = getConnection(elementId);
            str1 = Query1;
//            
            //this.st1 = this.con..prepareCall(str1);
            st = this.con.createStatement();

            this.rs1 = st.executeQuery(str1);


            while (rs1.next()) {
                temp = rs1.getString(1);
                // cbo += "<tr><Td style=\"width:50%\">"+temp+"</td></tr>";
                // rowEdgeValues.add(temp);
                if (temp != null) {
                    cbo += "<tr><Td style=\"width:50%\" align=\"right\">" + temp + "</td></tr>";
                    rowEdgeValues.add(temp);
                }

            }

            if (rs1 != null) {
                rs1.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
//           logger.error("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            logger.error("Exception in getRowEdges <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (this.rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdges  Info Message!", ex);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdges  Info Message!", ex);
                }
            }

            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdges  Info Message!", ex);
                }
            }
        }

        //   ////////////////////////////////////////////////////////////////////////////////////////.println(" roEdge cbo is::: "+cbo);
        return rowEdgeValues;
    }

    public PbReturnObject getRowEdgesRetObj(String Query1, String currVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        ArrayList rowEdgeValues = new ArrayList();
        cbo = "";
        PbReturnObject pbro = null;
        rowEdgeValues.add(currVal);

        try {

            this.con = getConnection(elementId);
            str1 = Query1;
            this.st1 = this.con.prepareCall(str1);
            this.rs1 = this.st1.executeQuery();
            pbro = new PbReturnObject(this.rs1);


            if (rs1 != null) {
                rs1.close();
            }
            if (st1 != null) {
                st1.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            logger.error("Exception in getRowEdgesRetObj()  <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (this.rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdgesRetObj()   Info Message!", ex);
                }
            }
            if (this.st1 != null) {
                try {
                    st1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdgesRetObj() Info Message!", ex);
                }
            }

            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getRowEdgesRetObj()  Info Message!", ex);
                }
            }
        }
        return pbro;
    }

    public PbReturnObject getColumnEdges(String Query1) {
        PbReturnObject pbro = new PbReturnObject();
        String str1 = "";
//        String temp ="";
//        ArrayList columnEdgeValues = new ArrayList();

        try {

            this.con = getConnection(elementId);
            str1 = Query1;
            this.st1 = this.con.prepareCall(str1);
            this.rs1 = this.st1.executeQuery();

            pbro = new PbReturnObject(this.rs1);

            if (rs1 != null) {
                rs1.close();
            }
            if (st1 != null) {
                st1.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            logger.error("Exception in getColumnEdges() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (this.rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getColumnEdges()  Info Message!", ex);
                }
            }
            if (this.st1 != null) {
                try {
                    st1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getColumnEdges()  Info Message!", ex);
                }
            }

            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getColumnEdges()  Info Message!", ex);
                }
            }
        }
        return pbro;
    }

    public String getStdStratDate(String Name, String Label, String CurrVal, String ide) {//Chnage this
        String cbo = "";
        if (CurrVal == null || CurrVal.equals("")) {
            CurrVal = getdateforpage();
        }

        String valu = "";
        String mont = "";
        String CurrValue = "";

        valu = CurrVal.substring(0, 2);
        mont = CurrVal.substring(3, 5);
        CurrValue = mont.concat("/").concat(valu).concat(CurrVal.substring(5));

        cbo = cbo + " <table border=\"0\" cellspacing=\"1\" cellpadding=\"0\"  style=\"width:100%\"> ";
        cbo = cbo + " <TR> ";
        cbo = cbo + " <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";
        //changed by anitha
        cbo = cbo + " <Td style=\"width:50%\" align=\"left\"> ";
        cbo = cbo + " <input type=\"hidden\" class=\"myTextbox3\" name=\"" + Name + "1\" ";
        cbo = cbo + " id=\"" + ide + "1\" value=\"" + CurrValue + "\" /> ";//onclick='shwDate()'
        cbo = cbo + " <input type=\"text\" class=\"myTextbox3\" name=\"" + Name + "\" ";
        cbo = cbo + " id=\"" + ide + "\" value=\"" + CurrValue + "\" onchange='copyDate(this.id)' /> ";
        //cbo = cbo + " <A HREF=\"#\" ";
        //cbo = cbo + " NAME=\"anchor1\" ID=\"anchor1\"><img src=\"cdp-10.gif\" border=\"0\" /></A> ";
        cbo = cbo + " </TD> ";
        cbo = cbo + " </TR> ";
        cbo = cbo + " </Table> ";

        /// end of query

        return (cbo);
    }

    public PbReturnObject getParentData(String Query1) {
        String str1 = "";
        PbReturnObject dataOb = new PbReturnObject();
        try {

            this.con = getConnection(elementId);
            str1 = Query1;
            this.st1 = this.con.prepareCall(str1);
            this.rs1 = this.st1.executeQuery();
            dataOb = new PbReturnObject(this.rs1);



            if (rs1 != null) {
                rs1.close();
            }
            if (st1 != null) {
                st1.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            logger.error(" Exception in getParentData() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (this.rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    logger.error(" Exception in getParentData()  Info Message!", ex);
                }
            }
            if (this.st1 != null) {
                try {
                    st1.close();
                } catch (SQLException ex) {
                    logger.error(" Exception in getParentData()  Info Message!", ex);
                }
            }

            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error(" Exception in getParentData()  Info Message!", ex);
                }
            }
        }


        return dataOb;
    }

    //added by uday over
    public String getQueryAllCombotr(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"1\" cellpadding=\"0\"  style=\"width:100%\"> ";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\"  class=\"myTextbox3\" onchange=\"javascript:submitform()\" > ";
        {
            try {

                this.con = getConnection(elementId);
                str1 = Query1;
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                if (CurrVal.equalsIgnoreCase("All")) {
                    cbo = cbo + " <option selected value=\"All\"  >All </option> ";
                } else {
                    cbo = cbo + " <option value=\"All\" >All</option> ";
                }


                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + " <option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option> ";
                    } else {
                        cbo = cbo + " <option value=\"" + temp + "\">" + rs1.getString(2) + "</option> ";
                    }
                }


                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error(" Exception in getQueryAllCombotr() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryAllCombotr()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryAllCombotr()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryAllCombotr()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";

        /// end of query

        return (cbo);
    }

    public String getQueryCombotb(String Name, String Label, String Query1, String CurrVal) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        int i = 0;
        /// start of query
        ////////////////////////////////////////////////////////////////////////////////////////////////////.println("in progen param " + str1);
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            try {

                this.con = getConnection(elementId);
                str1 = Query1;
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println("Got Conn"+ str1);
                this.st1 = this.con.prepareCall(str1);
                this.rs1 = this.st1.executeQuery();
                //out.println(sqlstr);
                i = 0;
                while (rs1.next()) {
                    temp = rs1.getString(1);
                    if (CurrVal.equalsIgnoreCase(temp)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                        cbo = cbo + ("<option selected value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    } else {
                        cbo = cbo + ("<option value=\"" + temp + "\">" + rs1.getString(2) + "</option>");
                    }
                    i++;
                }

                if (rs1 != null) {
                    rs1.close();
                    rs1 = null;
                }
                if (st1 != null) {
                    st1.close();
                    st1 = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
                logger.error(" Exception in getQueryCombotb() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    try {
                        rs1.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryCombotb()  Info Message!", ex);
                    }
                }
                if (this.st1 != null) {
                    try {
                        st1.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryCombotb()  Info Message!", ex);
                    }
                }

                if (this.con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        logger.error(" Exception in getQueryCombotb()  Info Message!", ex);
                    }
                }
            }



        }
        cbo = cbo + "</select></Td></Tr></Table>";
        /// end of query

        return (cbo);
    }

    public String getRollingPeriodLOV(String Name, String Label, String CurrVal) {
        ArrayList array = new ArrayList(0);
        array.add("Last 7 Days");
        array.add("Last 15 Days");
        array.add("Last 30 Days");
        array.add("Last 60 Days");
        array.add("Last 90 Days");
        array.add("Last 180 Days");
        array.add("Last 365 Days");
        return (getCombotbUsingArray(Name, Label, array, CurrVal));
    }

    public String getRollingPeriodLOV(String CurrVal) {
        ////////////////////////////.println("Lov called");
        String Name = "CBO_PRG_DAY_ROLLING";
        String Label = "Rolling Period";
        if (CurrVal == null) {
            CurrVal = "Last 30 Days";
        }
        ArrayList array = new ArrayList(0);
        array.add("Last 7 Days");
        array.add("Last 15 Days");
        array.add("Last 30 Days");
        array.add("Last 60 Days");
        array.add("Last 90 Days");
        array.add("Last 180 Days");
        array.add("Last 365 Days");
        return (getCombotbUsingArray(Name, Label, array, CurrVal));
    }

    public String getCombotbUsingArray(String Name, String Label, ArrayList array, String CurrVal) {
        ////////////////////////////.println("Display LOV called");
        String cbo = "";
        String str1 = "";
        String temp = "";
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\" class=\"myTextbox3\" >";
        {


            for (int i = 0; i < array.size(); i++) {
                temp = (String) array.get(i);
                if (CurrVal.equalsIgnoreCase(temp)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + temp + "</option>");
                } else {
                    cbo = cbo + ("<option value=\"" + temp + "\">" + temp + "</option>");
                }
            }



        }
        cbo = cbo + "</Td></Tr></Table>";
        ////////////////////////////.println("cbo"+cbo);

        /// end of query

        return (cbo);
    }

    public String getCombotbUsingArray(String Name, String Label, ArrayList array, ArrayList DispVal, String CurrVal, ArrayList sPramList) {
        String cbo = "";
        String str1 = "";
        String temp = "";
        String temp1 = "";
        /// start of query
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select name=\"" + Name + "\" class=\"myTextbox3\" >";
        {


            for (int i = 0; i < array.size(); i++) {
                temp = (String) array.get(i);
                temp1 = (String) DispVal.get(i);

                if (CurrVal.equalsIgnoreCase(temp)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + temp1 + "</option>");
                } else {
                    cbo = cbo + ("<option value=\"" + temp + "\">" + temp1 + "</option>");
                }
            }
        }
        cbo = cbo + "</select></Td></Tr></Table>";

        /// end of query

        return (cbo);
    }

    public ArrayList getArray(String str) {
        ArrayList arrayList1 = new ArrayList();
        String str1 = "";
        String key = null;
        HashMap hashMap = new HashMap();
        try {

            this.con = getConnection(elementId);
            str1 = str;
            this.st1 = this.con.prepareCall(str);
            this.rs1 = this.st1.executeQuery();

            while (rs1.next()) {
                ArrayList arrayList = new ArrayList();
                key = rs1.getString(1);
                arrayList.add(rs1.getString(2));
                arrayList.add(rs1.getString(3));
                arrayList.add(rs1.getString(4));
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println("rs1.getString(1) is: " + key);
                hashMap.put(key, arrayList);
                ////////////////////////////////////////////////////////////////////////////////////////////////////.println("HashMap is: " + hashMap);
            }

        } catch (SQLException e) {
            logger.error(" Exception in getArray() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        } finally {
            if (this.rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getArray()  Info Message!", ex);
                }
            }
            if (this.st1 != null) {
                try {
                    st1.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getArray()  Info Message!", ex);
                }
            }

            if (this.con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception in getArray()  Info Message!", ex);
                }
            }
        }


        return arrayList1;
    }

    public String getStdStratDate(String d1) {
        String cbo = "";

        cbo = cbo + " <Table  border=\"0\" cellspacing=\"1\" cellpadding=\"0\"  style=\"width:100%\">  ";
        cbo = cbo + " <Tr>            ";
        cbo = cbo + " <Td style=\"width:50%\" align=\"right\">Date</Td>   ";
        cbo = cbo + " <Td style=\"width:50%\">            ";
        cbo = cbo + "  <input type=\"text\" size=\"8\" class=\"myTextbox3\" name=\"StartDate\" onClick=\"displayDatePicker('StartDate');\"  value=\"" + d1 + "\" readonly/>    ";
        //cbo = cbo + " <A  HREF=\"#\"     ";
        //cbo = cbo + "  NAME=\"anchor1\" ID=\"anchor1\"><img src=\"cdp-10.gif\" border=\"0\" /></A>     ";
        cbo = cbo + " </Td>    ";
        cbo = cbo + " </Tr>    ";
        cbo = cbo + " </Table> ";


        return (cbo);
    }

    /*
     * public String getStdStratDate(String Name,String Label, String CurrVal){
     * String cbo ="";
     *
     *
     * cbo = cbo + " <Table border=\"0\" cellspacing=\"1\" cellpadding=\"0\"
     * style=\"width:100%\"> "; cbo = cbo + " <Tr> "; cbo = cbo + " <Td
     * style=\"width:50%\">"+ Label +"</Td> "; cbo = cbo + " <Td
     * style=\"width:50%\"> "; cbo = cbo + " <input type=\"text\" size=\"8\"
     * class=\"myTextbox3\" name=\""+ Name +"\" "; cbo = cbo + "
     * onClick=\"displayDatePicker('"+ Name +"');\" value=\""+ CurrVal+ "\"
     * readonly/> "; //cbo = cbo + " <A HREF=\"#\" "; //cbo = cbo + "
     * NAME=\"anchor1\" ID=\"anchor1\"><img src=\"cdp-10.gif\" border=\"0\"
     * /></A> "; cbo = cbo + " </Td> "; cbo = cbo + " </Tr> "; cbo = cbo + "
     * </Table> ";
     *
     * /// end of query
     *
     * return(cbo);
    }
     */
    public String getStdStratDate(String Name, String Label, String CurrVal) {//Chnage this
        String cbo = "";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("NULL")) {
            CurrVal = this.getdateforpage();
        }


        cbo = cbo + " <Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">  ";
        cbo = cbo + " <Tr>            ";
        cbo = cbo + " <Td style=\"width:50%\" align=\"right\">" + Label + "</Td>   ";
        cbo = cbo + " <Td style=\"width:50%\">       ";
        cbo = cbo + "  <input type=\"text\" class=\"myTextbox3\"  name=\"" + Name + "\"  ";
        cbo = cbo + " id=\"datepicker\"  value=\"" + CurrVal + "\" onchange='copyDate(this.id)'/>    ";
        //cbo = cbo + " <A  HREF=\"#\"     ";
        //cbo = cbo + "  NAME=\"anchor1\" ID=\"anchor1\"><img src=\"cdp-10.gif\" border=\"0\" /></A>     ";
        cbo = cbo + " </Td>    ";
        cbo = cbo + " </Tr>    ";
        cbo = cbo + " </Table> ";

        /// end of query

        return (cbo);
    }

    public String getStdStratDate() {
        String cbo = "";
        String d1 = "";

        d1 = getdateforpage();

        cbo = cbo + " <Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">  ";
        cbo = cbo + " <Tr>            ";
        cbo = cbo + " <Td style=\"width:50%\" align=\"right\">Date</Td>   ";
        cbo = cbo + " <Td style=\"width:50%\">            ";
        cbo = cbo + "  <input type=\"text\" size=\"8\" class=\"myTextbox3\" name=\"StartDate\" onClick=\"displayDatePicker('StartDate');\"  value=\"" + d1 + "\" readonly/>    ";
        //cbo = cbo + " <A  HREF=\"#\"     ";
        //cbo = cbo + "  NAME=\"anchor1\" ID=\"anchor1\"><img src=\"cdp-10.gif\" border=\"0\"/></A>     ";
        cbo = cbo + " </Td>    ";
        cbo = cbo + " </Tr>    ";
        cbo = cbo + " </Table> ";


        return (cbo);
    }

    public String getStdStringDuration(String CurrVal, String fixeddate) {
        String cbo = "";
//         String sytm="NO";
////           HttpSession session = request.getSession(false);
////    String sytm=(String.valueOf(session.getAttribute("isYearCal")));
//        try
//        {
//            this.con = ProgenConnection.getInstance().getConnection();
//        String query = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
//
//            Statement str = con.createStatement();
//            ResultSet rs = str.executeQuery(query);
//
//         if(rs.next())
//         {
//             String[] arr=rs.getString(1).split(";");
//             sytm=arr[4].substring(arr[4].indexOf("~") + 1);
//
//            }
//         }catch (SQLException e) {
//            logger.error("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
//         } finally {                //Added by amar to close open connections on 9 Dec 2014
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//        }
//            }
//            if (st != null) {
//                try {
//                    st.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//                }
//            }
//
//            if (this.con != null) {
//                try {
//                    con.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//                }
//            }
//        }
        //addded by Nazneen for Global Params
        PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String sytm = "No";
        try {
            String qery1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
            returnObject = execSelectSQL(qery1);
            if (returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
            }
            if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                String arrSetUpCharVal[] = setUpCharVal.split(";");
                for (int i = 0; i < arrSetUpCharVal.length; i++) {
                    String temp = arrSetUpCharVal[i];
                    if (temp.contains("isYearCal")) {
                        sytm = temp.substring(temp.indexOf("~") + 1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in getStdStringDuration() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        }
        //ended By Nazneen


        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        if (fixeddate != "fixdate") {
            cbo = cbo + "<Tr> <Td style=\"width:50%\" align=\"right\"> Duration </Td> ";//Label
        }
        cbo = cbo + " <Td style=\"width:50%\"><select style=\"width:60px;border: 1px solid black\" id=\"CBO_PRG_PERIOD_TYPE\" onchange=\"getChangeDuration()\" name=\"CBO_PRG_PERIOD_TYPE\" class=\"myTextbox3\" > ";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("NULL") || CurrVal.contains("/")) {

            CurrVal = "Month";
        }
        if (sytm.equalsIgnoreCase("Yes")) {
            SYTM = "Yes";
//               
            cbo = cbo + "<option selected value=\"Year\"> Year</option>";
        } else {
            if (CurrVal.equalsIgnoreCase("DAY")) {
                cbo = cbo + "<option selected value=\"Day\" > Day </option>";
                cbo = cbo + "<option value=\"Week\" > Week </option>";
                cbo = cbo + "<option value=\"Month\"> Month </option>";
                cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
                cbo = cbo + "<option value=\"Year\"> Year</option>";

            } else if (CurrVal.equalsIgnoreCase("WEEK")) {
                cbo = cbo + "<option value=\"Day\" > Day </option>";
                cbo = cbo + "<option selected value=\"Week\" > Week </option>";
                cbo = cbo + "<option value=\"Month\"> Month </option>";
                cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
                cbo = cbo + "<option value=\"Year\"> Year</option>";

            } else if (CurrVal.equalsIgnoreCase("MONTH")) {
                cbo = cbo + "<option  value=\"Day\" > Day </option>";
                cbo = cbo + "<option  value=\"Week\" > Week </option>";
                cbo = cbo + "<option selected value=\"Month\"> Month </option>";
                cbo = cbo + "<option value=\"Qtr\"> Qtr </option>";
                cbo = cbo + "<option value=\"Year\"> Year</option>";

            } else if (CurrVal.equalsIgnoreCase("QTR")) {
                cbo = cbo + "<option  value=\"Day\" > Day </option>";
                cbo = cbo + "<option  value=\"Week\" > Week </option>";
                cbo = cbo + "<option  value=\"Month\"> Month </option>";
                cbo = cbo + "<option selected value=\"Qtr\"> Qtr </option>";
                cbo = cbo + "<option value=\"Year\"> Year</option>";
            } else {
                cbo = cbo + "<option  value=\"Day\" > Day </option>";
                cbo = cbo + "<option  value=\"Week\" > Week </option>";
                cbo = cbo + "<option  value=\"Month\"> Month </option>";
                cbo = cbo + "<option  value=\"Qtr\"> Qtr </option>";
                cbo = cbo + "<option selected value=\"Year\"> Year</option>";
            }
        }
        cbo = cbo + "</select></Td></Tr></Table>";


        return (cbo);
    }

    public String getStdStringCompare(String CurrVal) {
        String cbo = "";


        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr> <Td style=\"width:50%\" align=\"right\"> Compare with </Td> ";//Label
        cbo = cbo + " <Td style=\"width:50%;border: 1px solid black\"><select id=\"CBO_PRG_COMPARE\" onchange=\"getChangeCompare()\"  name=\"CBO_PRG_COMPARE\" class=\"myTextbox3\" > ";

        if (CurrVal.equalsIgnoreCase("Last Day")) {
            cbo = cbo + "<option selected value=\"Last Day\" > Last Day </option>";
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option value=\"Last Month\"> Last Month </option>";
            cbo = cbo + "<option value=\"Last Year\"> Last Year</option>";
        } else if (CurrVal.equalsIgnoreCase("Last Week")) {
            cbo = cbo + "<option value=\"Last Day\" > Last Day </option>";
            cbo = cbo + "<option selected value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option value=\"Last Month\"> Last Month </option>";
            cbo = cbo + "<option value=\"Last Year\"> Last Year</option>";
        } else if (CurrVal.equalsIgnoreCase("Last Month")) {
            cbo = cbo + "<option value=\"Last Day\" > Last Day </option>";
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option selected value=\"Last Month\"> Last Month </option>";
            cbo = cbo + "<option value=\"Last Year\"> Last Year</option>";
        } else if (CurrVal.equalsIgnoreCase("Last Year")) {
            cbo = cbo + "<option value=\"Last Day\" > Last Day </option>";
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option selected value=\"Last Month\"> Last Month </option>";
            cbo = cbo + "<option value=\"Last Year\"> Last Year</option>";
        } else if (CurrVal.equalsIgnoreCase("Last Period")) {
            cbo = cbo + "<option selected value=\"Last Period\" > Last Period </option>";
            cbo = cbo + "<option value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option value=\"Period Complete\"> Period Complete </option>";
            cbo = cbo + "<option value=\"Year Complete\"> Year Complete</option>";


        } else if (CurrVal.equalsIgnoreCase("Last Year")) {
            cbo = cbo + "<option value=\"Last Period\" > Last Period </option>";
            cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option value=\"Period Complete\"> Period Complete </option>";
            cbo = cbo + "<option value=\"Year Complete\"> Year Complete</option>";


        } else if (CurrVal.equalsIgnoreCase("Period Complete")) {
            cbo = cbo + "<option  value=\"Last Period\" > Last Period </option>";
            cbo = cbo + "<option  value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option selected value=\"Period Complete\"> Period Complete </option>";
            cbo = cbo + "<option value=\"Year Complete\"> Year Complete </option>";


        } else {
            cbo = cbo + "<option  value=\"Last Period\" > Last Period </option>";
            cbo = cbo + "<option  value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option  value=\"Period Complete\"> Period Complete </option>";
            cbo = cbo + "<option selected value=\"Year Complete\"> Year Complete </option>";

        }
        cbo = cbo + "</select></Td></Tr></Table>";


        return (cbo);
    }

    public String getStdStringCompare(String CurrVal, String PeriodType, String fixed) {
        String cbo = "";
//        String sytm="No";
//        if(SYTM=="")
//        {
//
//
////           HttpSession session = request.getSession(false);
////    String sytm=(String.valueOf(session.getAttribute("isYearCal")));
//        try
//        {
//        this.con = ProgenConnection.getInstance().getConnection();
//        String query = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
//
//        Statement str = con.createStatement();
//         rs = str.executeQuery(query);
//
//         if(rs.next())
//         {
//             String[] arr=rs.getString(1).split(";");
//             sytm=arr[5].substring(arr[5].indexOf("~") + 1);
//
//         }
//         }catch (SQLException e) {
//                logger.error("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
//            } finally {                //Added by amar to close open connections on 9 Dec 2014
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//            }
//            }
//            if (st != null) {
//                try {
//                    st.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//                }
//            }
//
//            if (this.con != null) {
//                try {
//                    con.close();
//                } catch (SQLException ex) {
//                    logger.error(" Info Message!", ex);
//                }
//            }
//        }
//         }else
//        {
//             sytm=SYTM;
//         }
        //addded by Nazneen for Global Params
        PbReturnObject returnObject = null;
        String setUpCharVal = "";
        String sytm = "No";
        try {
            String qery1 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
            returnObject = execSelectSQL(qery1);
            if (returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
            }
            if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                String arrSetUpCharVal[] = setUpCharVal.split(";");
                for (int i = 0; i < arrSetUpCharVal.length; i++) {
                    String temp = arrSetUpCharVal[i];
                    if (temp.contains("isYearCal")) {
                        sytm = temp.substring(temp.indexOf("~") + 1);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in getStdStringCompare() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        }
        //ended By Nazneen

        if (CurrVal == null || CurrVal.equalsIgnoreCase("NULL") || CurrVal.contains("/")) {

            CurrVal = "Last Period";
        }
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        if (fixed != "fixdate") {
            cbo = cbo + "<Tr> <Td style=\"width:50%\" align=\"right\"> Compare with </Td> ";//Label
        } else {
            cbo = cbo + "<Tr> <Td style=\"width:30%\" align=\"right\"> COMPARISON </Td> ";//Label
        }
        if (sytm.equalsIgnoreCase("Yes")) {
//            
            cbo = cbo + " <Td style=\"width:100%\"><select style=\"width:100px;border: 1px solid black\" id=\"CBO_PRG_COMPARE\" onchange=\"getChangeCompare()\"  name=\"CBO_PRG_COMPARE\" class=\"myTextbox3\" > ";
            cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
        } else {
            cbo = cbo + " <Td style=\"width:100%\"><select style=\"width:180px;border: 1px solid black\" id=\"CBO_PRG_COMPARE\" onchange=\"getChangeCompare()\"  name=\"CBO_PRG_COMPARE\" class=\"myTextbox3\" > ";
            if (PeriodType.equalsIgnoreCase("Day") //options added by mohit
                    ) {
                if (CurrVal.equalsIgnoreCase("Previous Day") || CurrVal.equalsIgnoreCase("Day")
                        || CurrVal.equalsIgnoreCase("Last Period")
                        || CurrVal.equalsIgnoreCase("Period Complete")) {
                    cbo = cbo + "<option selected value=\"Previous Day\" > Previous Day </option>";
                    cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
                    cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
                    cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Same Day Last Week")) {
                    cbo = cbo + "<option value=\"Previous Day\" > Previous Day </option>";
                    cbo = cbo + "<option selected value=\"Same Day Last Week\" > Same Day Last Week </option>";
                    cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
                    cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Same Day Last Month")) {
                    cbo = cbo + "<option value=\"Previous Day\" > Previous Day </option>";
                    cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
                    cbo = cbo + "<option selected value=\"Same Day Last Month\"> Same Day Last Month </option>";
                    cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Same Day Last Year")
                        || CurrVal.equalsIgnoreCase("Year Complete")) {
                    cbo = cbo + "<option value=\"Previous Day\" > Previous Day </option>";
                    cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
                    cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
                    cbo = cbo + "<option  selected value=\"Same Day Last Year\"> Same Day Last Year </option>";
                } else {
                    cbo = cbo + "<option selected value=\"Previous Day\" > Previous Day </option>";
                    cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
                    cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
                    cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year </option>";
                }

            } else if (PeriodType.equalsIgnoreCase("Week")) {
                if (CurrVal.equalsIgnoreCase("Last Day") || CurrVal.equalsIgnoreCase("Last Week")
                        || CurrVal.equalsIgnoreCase("Last Period") || CurrVal.equalsIgnoreCase("Last Month")) {
                    cbo = cbo + "<option selected value=\"Last Week\" > Last Week </option>";
                    cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
                    cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Last Year") || CurrVal.equalsIgnoreCase("Same Week Last Year")) {
                    cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
                    cbo = cbo + "<option selected value=\"Same Week Last Year\" > Same Week Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
                    cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Period Complete") || CurrVal.equalsIgnoreCase("Complete Last Week")) {
                    cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
                    cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
                    cbo = cbo + "<option selected value=\"Complete Last Week\"> Complete Last Week </option>";
                    cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Year Complete") || CurrVal.equalsIgnoreCase("Complete Same Week Last Year")) {
                    cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
                    cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
                    cbo = cbo + "<option selected value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
                } else {
                    cbo = cbo + "<option selected value=\"Last Week\" > Last Week </option>";
                    cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
                    cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
                }
            } else if (PeriodType.equalsIgnoreCase("Month")) {
                if (CurrVal.equalsIgnoreCase("Last Day") || CurrVal.equalsIgnoreCase("Last Week")
                        || CurrVal.equalsIgnoreCase("Last Period") || CurrVal.equalsIgnoreCase("Last Month")) {
                    cbo = cbo + "<option selected value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Last Year") || CurrVal.equalsIgnoreCase("Same Month Last Year")) {
                    cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option selected value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Period Complete") || CurrVal.equalsIgnoreCase("Complete Last Month")) {
                    cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option selected value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Year Complete") || CurrVal.equalsIgnoreCase("Complete Same Month Last Year")) {
                    cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option selected value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                } else {
                    cbo = cbo + "<option selected value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                }
            } else if (PeriodType.equalsIgnoreCase("Qtr")) {
                if (CurrVal.equalsIgnoreCase("Last Day") || CurrVal.equalsIgnoreCase("Last Week")
                        || CurrVal.equalsIgnoreCase("Last Period") || CurrVal.equalsIgnoreCase("Last Month") || CurrVal.equalsIgnoreCase("Last Qtr")) {
                    cbo = cbo + "<option selected value=\"Last Qtr\" > Last Qtr </option>";
                    cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
                    cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Last Year") || CurrVal.equalsIgnoreCase("Same Qtr Last Year")) {
                    cbo = cbo + "<option value=\"Last Qtr\" > Last Qtr </option>";
                    cbo = cbo + "<option selected value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
                    cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Period Complete") || CurrVal.equalsIgnoreCase("Complete Last Qtr")) {
                    cbo = cbo + "<option value=\"Last Qtr\" > Last Qtr </option>";
                    cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
                    cbo = cbo + "<option selected value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
                    cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
                } else if (CurrVal.equalsIgnoreCase("Year Complete") || CurrVal.equalsIgnoreCase("Complete Same Qtr Last Year")) {
                    cbo = cbo + "<option value=\"Last Qtr\" > Last Qtr </option>";
                    cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
                    cbo = cbo + "<option selected value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
                } else {
                    cbo = cbo + "<option selected value=\"Last Qtr\" > Last Qtr </option>";
                    cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
                    cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
                }
            } else if (PeriodType.equalsIgnoreCase("Year")) {
                if (CurrVal.equalsIgnoreCase("Last Day") || CurrVal.equalsIgnoreCase("Last Week")
                        || CurrVal.equalsIgnoreCase("Last Period") || CurrVal.equalsIgnoreCase("Last Month") || CurrVal.equalsIgnoreCase("Last Year")) {
                    cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Year\"> Complete Last Year </option>";

                } else if (CurrVal.equalsIgnoreCase("Period Complete") || CurrVal.equalsIgnoreCase("Year Complete") || CurrVal.equalsIgnoreCase("Complete Last Year")) {
                    cbo = cbo + "<option value=\"Last Year\" > Last Year </option>";
                    cbo = cbo + "<option selected  value=\"Complete Last Year\"> Complete Last Year </option>";
                } else {
                    cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Year\"> Complete Last Year </option>";
                }
            } else {
                if (CurrVal.equalsIgnoreCase("Last Period")) {
                    cbo = cbo + "<option selected value=\"Last Month\" > Last Month </option>";
                    cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
                    cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
                    cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
                }
            }



        }
//      }  else{
//            if (CurrVal.equalsIgnoreCase("Last Period")
//                    || CurrVal.equalsIgnoreCase("Last Day")
//                    || CurrVal.equalsIgnoreCase("Last Week")
//                    || CurrVal.equalsIgnoreCase("Last Month")) {
//            cbo = cbo + "<option selected value=\"Last Period\" > Last Period </option>";
//            cbo = cbo + "<option value=\"Last Year\" > Last Year </option>";
//            cbo = cbo + "<option value=\"Period Complete\"> Period Complete </option>";
//            cbo = cbo + "<option value=\"Year Complete\"> Year Complete</option>";
//
//
//        } else if (CurrVal.equalsIgnoreCase("Last Year")) {
//            cbo = cbo + "<option value=\"Last Period\" > Last Period </option>";
//            cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
//            cbo = cbo + "<option value=\"Period Complete\"> Period Complete </option>";
//            cbo = cbo + "<option value=\"Year Complete\"> Year Complete</option>";
//
//
//        } else if (CurrVal.equalsIgnoreCase("Period Complete")) {
//            cbo = cbo + "<option  value=\"Last Period\" > Last Period </option>";
//            cbo = cbo + "<option  value=\"Last Year\" > Last Year </option>";
//            cbo = cbo + "<option selected value=\"Period Complete\"> Period Complete </option>";
//            cbo = cbo + "<option value=\"Year Complete\"> Year Complete </option>";
//
//
//        } else {
//            cbo = cbo + "<option  value=\"Last Period\" > Last Period </option>";
//            cbo = cbo + "<option  value=\"Last Year\" > Last Year </option>";
//            cbo = cbo + "<option  value=\"Period Complete\"> Period Complete </option>";
//            cbo = cbo + "<option selected value=\"Year Complete\"> Year Complete </option>";
//
//        }
//
//        }
//


        cbo = cbo + "</select></Td></Tr></Table>";


        return (cbo);
    }

    public String getStdDuration(String CurrVal) {
        String cbo = "";


        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr> <Td style=\"width:50%\" align=\"right\"> Duration </Td> ";//Label
        cbo = cbo + " <Td style=\"width:50%\"><select   name=\"CBOPRG_PERIOD_TYPE\" class=\"myTextbox3\" /> ";



        if (CurrVal.equalsIgnoreCase("1")) {
            cbo = cbo + "<option selected value=1 > Day </option>";
            cbo = cbo + "<option value=2 > Week </option>";
            cbo = cbo + "<option value=3> Month </option>";
            cbo = cbo + "<option value=4> Qtr</option>";
            cbo = cbo + "<option value=5> Year</option>";

        } else if (CurrVal.equalsIgnoreCase("2")) {
            cbo = cbo + "<option value=1 > Day </option>";
            cbo = cbo + "<option selected value=2 > Week </option>";
            cbo = cbo + "<option value=3> Month </option>";
            cbo = cbo + "<option value=4> Qtr</option>";
            cbo = cbo + "<option value=5> Year</option>";

        } else if (CurrVal.equalsIgnoreCase("3")) {
            cbo = cbo + "<option  value=1 > Day </option>";
            cbo = cbo + "<option  value=2 > Week </option>";
            cbo = cbo + "<option selected value=3> Month </option>";
            cbo = cbo + "<option value=4> Qtr </option>";
            cbo = cbo + "<option value=5> Year</option>";

        } else if (CurrVal.equalsIgnoreCase("4")) {
            cbo = cbo + "<option  value=1 > Day </option>";
            cbo = cbo + "<option  value=2 > Week </option>";
            cbo = cbo + "<option  value=3> Month </option>";
            cbo = cbo + "<option selected value=4> Qtr </option>";
            cbo = cbo + "<option value=5> Year</option>";
        } else {
            cbo = cbo + "<option  value=1 > Day </option>";
            cbo = cbo + "<option  value=2 > Week </option>";
            cbo = cbo + "<option  value=3> Month </option>";
            cbo = cbo + "<option  value=4> Qtr </option>";
            cbo = cbo + "<option selected value=5> Year</option>";
        }
        cbo = cbo + "</select></Td></Tr></Table>";


        return (cbo);
    }

    public String getNonStdDuration(String CurrVal) {
        String cbo = "";


        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr> <Td style=\"width:50%\" align=\"right\"> Duration </Td> ";//Label
        cbo = cbo + " <Td style=\"width:50%\"><select   name=\"cboperiod\" class=\"myTextbox3\" > ";


        if (CurrVal.equalsIgnoreCase("4")) {

            cbo = cbo + "<option  value=3> Month </option>";
            cbo = cbo + "<option selected value=4> Qtr </option>";
            cbo = cbo + "<option value=5> Year</option>";
        } else if (CurrVal.equalsIgnoreCase("5")) {

            cbo = cbo + "<option  value=3> Month </option>";
            cbo = cbo + "<option  value=4> Qtr </option>";
            cbo = cbo + "<option selected value=5> Year</option>";
        } else {

            cbo = cbo + "<option selected value=3> Month </option>";
            cbo = cbo + "<option value=4> Qtr </option>";
            cbo = cbo + "<option value=5> Year</option>";

        }

        cbo = cbo + "</Td></Tr></Table>";


        return (cbo);
    }

    public String getStdCompare(String CurrVal) {
        String cbo = "";


        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + " <Td style=\"width:50%\">&nbsp;Compare&nbsp; ::&nbsp; </Td> <Td style=\"width:50%\">&nbsp; Last :&nbsp;&nbsp;  </Td>  <Td style=\"width:50%\">";//Label





        if (CurrVal.equalsIgnoreCase("1")) {
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"1\" checked> Period";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"2\" >  Year ";

            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"3\"   > Period Complete ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"4\"  >  Year  Complete";


        } else if (CurrVal.equalsIgnoreCase("2")) {
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"1\" > Period Till Date ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"2\" checked >  Year  Till Date";

            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"3\"   > Period Complete ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"4\"   >  Year  Complete";

        } else if (CurrVal.equalsIgnoreCase("3")) {
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"1\" > Period Till Date ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"2\"  >  Year  Till Date";

            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"3\" checked  > Period Complete ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"4\"   >  Year  Complete";

        } else {
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"1\" > Period Till Date ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"2\"  >  Year  Till Date";

            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"3\"   > Period Complete ";
            cbo = cbo + "<input type=\"radio\" name=\"cboptype\" value=\"4\"  checked >  Year  Complete";

        }
        cbo = cbo + "</Td></Tr></Table>";

        return (cbo);
    }
    ///////////////////

    public String getdatefortrd(String ddrill, String ddrillv, String d1, String st_period) throws SQLException {
        String sqlstr = "";
        String str1 = "";



        if (d1 == null || ddrill.equalsIgnoreCase("Y")) {
            try {
                //String str1="";
                //con = DriverManager.getConnection (URL, username, password);
                ////   Class.forName("oracle.jdbc.driver.OracleDriver");

                this.con = getConnection(elementId);
                if (ddrill.equalsIgnoreCase("Y")) {
                    if (st_period.equalsIgnoreCase("4")) {
                        str1 = "select to_char(MAX(ddate) ,'MM/dd/yyyy')  from pr_day_denom T where T.CQ_YEAR = '" + ddrillv + "' ";

                    } else if (st_period.equalsIgnoreCase("3")) {
                        str1 = "select to_char(MAX(CQ_END_DATE) ,'MM/dd/yyyy')  from pr_day_denom T where   T.CQTR|| '-' ||T.CQ_YEAR  = '" + ddrillv + "' ";
                    } else if (st_period.equalsIgnoreCase("1")) {
                        str1 = "select to_char(MAX(CM_END_DATE) ,'MM/dd/yyyy')  from pr_day_denom T where  TO_CHAR(CM_END_DATE,'MON-RR')= '" + ddrillv + "' ";
                    } else {
                        str1 = "select to_char(sysdate,'MM/dd/yyyy') from dual";
                    }

                    ddrill = "N";
                } else {
                    str1 = "select to_char(sysdate,'MM/dd/yyyy') from dual";
                }
                //out.println("String bild <BR>");

                this.st1 = this.con.prepareCall(str1);
                //out.println("St bild <BR>");
                this.rs1 = this.st1.executeQuery();
                //out.println("RS bild <BR>");

                while (rs1.next()) {

                    d1 = rs1.getString(1);

                    //out.println("insert into Item_master values(" + rs.getString(1)+ ",'"+ rs.getString(2) + "','" + rs.getString(3) +"','" + rs.getString(4) +"'," +rs.getString(5) + ", " + rs.getString(6) + " ) ; <BR> <BR>" );
                }

                if (rs1 != null) {
                    rs1.close();
                }
                if (st1 != null) {
                    st1.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("Exception in getdatefortrd() <P> SQL error: <PRE> " + e + " </PRE> </P>\n");
            } finally {
                if (this.rs1 != null) {
                    this.rs1.close();
                }
                if (this.st1 != null) {
                    this.st1.close();
                }

                if (this.con != null) {
                    this.con.close();
                }
            }


        }


        return (d1);
    }

    public String getsingleTextBox(String Name, String Label, String CurrVal, String key) {

        String cbo = "";
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String function = "\"selId(" + boxName + "," + divName + "," + mKey + ")\"";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox3\" maxlength=\"4\" value=\"" + CurrVal + "\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        cbo = cbo + "</Table>";

/// end of query

        return (cbo);
    }

    public String getMultiTextBox(String Name, String Label, String CurrVal, String key) {

        String cbo = "";
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String function = "\"selId(" + boxName + "," + divName + "," + mKey + ")\"";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox3\"  value=\"" + CurrVal + "\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        cbo = cbo + "</Table>";

/// end of query

        return (cbo);
    }

    /////////////////////refresh
    public String getsingleTextBoxr(String Name, String Label, String CurrVal, String key) {

        String cbo = "";
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String function = "\"selId(" + boxName + "," + divName + "," + mKey + ")\"";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox3\" onblur=\"javascript:submitform()\" maxlength=\"4\" value=\"" + CurrVal + "\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        cbo = cbo + "</Table>";

/// end of query

        return (cbo);
    }

    public String getMultiTextBoxr(String Name, String Label, String CurrVal, String key) {

        String cbo = "";
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String function = "\"selId(" + boxName + "," + divName + "," + mKey + ")\"";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        /// start of query
        cbo = "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox3\" onblur=\"javascript:submitform()\" value=\"" + CurrVal + "\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        cbo = cbo + "</Table>";

/// end of query

        return (cbo);
    }

    //Added by Bharathi on 20 JULY
    public String getSingleAutoCompleteBox(String Name, String mKey) {
        String cbo = "";
        String divName = "'" + Name + "SuggestList1" + "'";
        String boxName = "'" + Name + "'";
        String divName1 = Name + "SuggestList1";
        String function = "\"selId(" + boxName + "," + divName + ",'" + mKey + "')\"";
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox5\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        return cbo;
    }

    public String getSingleAutoCompleteBox2d(String Name, String mKey) {
        String cbo = "";
        String divName = "'" + Name + "SuggestList2" + "'";
        String boxName = "'" + Name + "'";
        String divName1 = Name + "SuggestList2";
        String function = "\"selId(" + boxName + "," + divName + ",'" + mKey + "')\"";
        cbo = cbo + "<Td style=\"width:50%\"> <input name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox5\" onfocus=" + function + "></Td>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;width:200px;height:100px\"></div></Td></Tr>";
        return cbo;
    }

    public String getMultiTextBoxNew(String Name, String Label, String CurrVal, String key, String includeExclude) {

        String cbo = "";
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String imageName = Name + "-plus";
        String excludeImgId = Name + "-excimg";
        String iconsHtml;
        String function = "\"selId(" + boxName + "," + divName + "," + mKey + ")\"";
        String disableonkeyUpfunction = "\"disableonkeyselId(" + boxName + "," + divName + "," + mKey + ")\"";
        if (CurrVal == null || CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
            CurrVal = "All";
        }
        // 
        /// start of query
        cbo = "<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        //changed by anitha
        if (includeExclude.equalsIgnoreCase("LIKE") || includeExclude.equalsIgnoreCase("NOT LIKE")) {
            cbo = cbo + "<Td style=\"width:50%\" align=\"left\"><input  name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox6\" onblur=resetAllAjax() value=\"" + CurrVal + "\" onfocus=" + disableonkeyUpfunction + ">"
                    + "<input  name=\"" + Name + "_excbox\" id=\"" + Name + "_exc\" style='display:none'>";
        } else {
            cbo = cbo + "<Td style=\"width:50%\" align=\"left\"><input  name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox6\" onblur=resetAllAjax() value=\"" + CurrVal + "\" onfocus=" + function + ">"
                    + "<input  name=\"" + Name + "_excbox\" id=\"" + Name + "_exc\" style='display:none'>";
        }
        if (includeExclude.equalsIgnoreCase("NOT_SELECTED")) {
            iconsHtml = "<img id='" + imageName + "' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=includeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/include.png'>"
                    + "<img id='" + excludeImgId + "' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=excludeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/exclude.png'>";
        } //            iconsHtml = "<span id='" + imageName + "' class='ui-icon ui-icon-circle-plus' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=includeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")></span>"
        //                + "<span id='" + excludeImgId + "' class='ui-icon ui-icon-circle-minus' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=excludeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")></span>";
        else if (includeExclude.equalsIgnoreCase("INCLUDED") || includeExclude.equalsIgnoreCase("IN") || includeExclude.equalsIgnoreCase("LIKE")) {
            iconsHtml = "<img id='" + imageName + "' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=includeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/include.png'>"
                    + "<img id='" + excludeImgId + "' style='display:none' onmouseover='checkImg(this)' onclick=excludeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/exclude.png'>";
        } else {
            iconsHtml = "<img id='" + imageName + "' style='display:none' onmouseover='checkImg(this)' onclick=includeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/include.png'>"
                    + "<img id='" + excludeImgId + "' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer' onmouseover='checkImg(this)' onclick=excludeParameter(this,'" + Name + "'," + boxName + "," + divName + "," + mKey + ")  src='images/exclude.png'>";
        }

        cbo = cbo + iconsHtml;
        cbo = cbo + "</Td></Tr>";
// commented by manik       cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto\"></div></Td></Tr>";
        cbo = cbo + "<Tr><Td style=\"width:50%\"></Td><Td style=\"width:50%\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto;position: absolute;\"></div></Td></Tr>";
        cbo = cbo + "</Table>";


        return (cbo);
    }

    //#### santhosh modified file
    /*
     * public String getMultiTextBoxNew(String Name, String Label, String
     * CurrVal, String key) {
     *
     * StringBuffer cbo = new StringBuffer(); String str1 =""; String temp ="";
     * String mKey = "'" + key + "'"; String boxName = "'" + Name + "'"; String
     * divName = "'" + Name + "SuggestList" + "'"; String divName1 = Name +
     * "SuggestList"; String imageName = Name + "-plus"; //String function =
     * "selId(" + boxName + "," + divName + "," + mKey + ")"; if (CurrVal ==
     * null || CurrVal.equalsIgnoreCase("All") ||
     * CurrVal.equalsIgnoreCase("null")) { CurrVal = "All"; } /// start of query
     * // // cbo.append( "<Table border=\"0\" cellspacing=\"0\"
     * cellpadding=\"0\" style=\"width:100%\">"); // cbo.append( "<Tr>"); //
     * cbo.append( "<Td style=\"width:50%\" align=\"right\">" + Label + "</Td>
     * ");//Label // cbo.append( "<Td style=\"width:50%\"><input name=\"" + Name
     * + "\" id=\"" + Name + "\" autocomplete=\"off\" class=\"myTextbox6\"
     * onblur=resetAllAjax() value=\"" + CurrVal + "\" onFocus=\"" + function +
     * "\">" + // "<img id='" + imageName + "'
     * style='vertical-align:middle;display:inline;width:12px;height:15px'
     * onmouseover='checkImg(this)' onFocus=\"" + function + "\"
     * onClick=\"changeImage(this,'" + Name + "')\"
     * src='images/plus.gif'></Td></Tr>"); // cbo.append( "<Tr>"); //
     * cbo.append( "<Td style=\"width:50%\"></Td>"); // cbo.append("<Td
     * style=\"width:50%\"><div onscroll='onScrollDiv(this)'
     * class=\"ajaxboxstyle\" id=\"" + divName1 + "\"
     * style=\"display:none\"></div></Td>"); // cbo.append( "</Tr>"); //
     * cbo.append( "</Table>"); //
     *
     * //################ cbo.append("<Table border=\"0\" cellspacing=\"0\"
     * cellpadding=\"0\" style=\"width:100%\">"); cbo.append("<Tr>");
     * cbo.append("<Td style=\"width:50%\" align=\"right\">" + Label + "</Td>
     * ");//Label cbo.append("<Td style=\"width:50%\">"); cbo.append("<input
     * name=\"" + Name + "\" id=\"" + Name + "\" autocomplete=\"off\"
     * class=\"myTextbox6\" onblur=resetAllAjax() value=\"" + CurrVal + "\"
     * onFocus=\"selId(" + boxName + "," + divName + "," + mKey + ")\" >");
     * //cbo.append("<img id='" + imageName + "'
     * style='vertical-align:middle;display:inline;width:12px;height:15px'
     * onmouseover='checkImg(this)' onClick=\"changeImage(this," + Name + "," +
     * boxName + "," + divName + "," + mKey + ")\" src='images/plus.gif'>");
     * cbo.append("<img id='" + imageName + "'
     * style='vertical-align:middle;display:inline;width:12px;height:15px'
     * onmouseover='checkImg(this)' onClick=\"changeImage(this," + Name + ")\"
     * src='images/plus.gif'>"); cbo.append("</Td>"); cbo.append("</Tr>");
     * cbo.append("<Tr>"); cbo.append("<Td style=\"width:50%\"></Td>");
     * cbo.append("<Td style=\"width:50%\"><div onscroll='onScrollDiv(this)'
     * class=\"ajaxboxstyle\" id=\"" + divName1 + "\"
     * style=\"display:none\"></div></Td>"); //cbo.append("<Td
     * style=\"width:50%\"><div onscroll='onScrollDiv(this" + boxName + "," +
     * divName + "," + mKey + ")' class=\"ajaxboxstyle\" id=\"" + divName1 + "\"
     * style=\"display:none\"></div></Td>"); cbo.append("</Tr>");
     * cbo.append("</Table>");
     *
     * //################
     *
     *
     * //////////////////////////////////.println("Test");
     * //////////////////////////////////.println("<Td
     * style=\"width:50%\"><input name=\"" + Name + "\" id=\"" + Name + "\"
     * autocomplete=\"off\" class=\"myTextbox6\" onblur=resetAllAjax() value=\""
     * + CurrVal + "\" onfocus=\"" + function + "\"><img id='" + imageName + "'
     * style='vertical-align:middle;display:inline;width:12px;height:15px'
     * onmouseover='checkImg(this)' onclick=\"changeImage(this,'" + Name + "')\"
     * src='images/plus.gif'></Td>");
     *
     *
     * return (cbo.toString()); }
     */
    //####
    /*
     * public String getMultiTextBoxNew(String Name, String Label, String
     * CurrVal, String key) {
     *
     * String cbo =""; String str1 =""; String temp =""; String mKey = "'" + key
     * + "'"; String boxName = "'" + Name + "'"; String divName = "'" + Name +
     * "SuggestList" + "'"; String divName1 = Name + "SuggestList"; String
     * imageName = Name + "-plus"; String function = "\"selId(" + boxName + ","
     * + divName + "," + mKey + ")\""; if (CurrVal == null ||
     * CurrVal.equalsIgnoreCase("All") || CurrVal.equalsIgnoreCase("null")) {
     * CurrVal = "All"; } /// start of query cbo = "<Table border=\"0\"
     * cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">"; cbo = cbo +
     * "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" +
     * Label + "</Td> ";//Label cbo = cbo + "<Td style=\"width:50%\"><input
     * name=\"" + Name + "\" id=\"" + Name + "\" class=\"myTextbox6\" value=\""
     * + CurrVal + "\" />" ; cbo = cbo + "</Td></Tr>"; cbo = cbo + ""; cbo = cbo
     * + "</Table>";
     *
     *
     * return (cbo);
    }
     */
    public Connection getConnection(String elementId) {
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
        }
        return connection;
    }

    //added by uday
    public String getQueryCombotb4ForScenario(String Name, String Label, PbReturnObject data, String CurrVal, HashMap paramMap) {
//        String Query1 = "";
        String cbo = "";
//        String str1 ="";
        String temp = "";
        int i = 0;
        data.writeString();

        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        cbo = cbo + "<Td style=\"width:50%\"> <select id=\"primaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            ////////////////////////////.println("in progen param " + str1);

            // while (rs1.next()) {
            for (i = 0; i < data.getRowCount(); i++) {
                temp = data.getFieldValueString(i, 0);
                if (CurrVal.equalsIgnoreCase(temp)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                } else {
                    cbo = cbo + ("<option value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                }
            }

        }
        cbo = cbo + "</select></Td></Tr></Table>";
        ////////////////////////////.println("CBO--------vie w by " + cbo);

        /// end of query

        return (cbo);
    }

    public String getQueryCombotb3ForScenario(String Name, String Label, PbReturnObject data, String CurrVal) throws SQLException {
//        String Query1 = "";
        data.writeString();
        String cbo = "";
//        String str1 ="";
        String temp = "";
        int i = 0;
        ////////////////////////////.println(" in getQ com3 For Sce ");
        cbo = "<Table  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\">";
        cbo = cbo + "<Tr style=\"width:100%\"> <Td style=\"width:50%\" align=\"right\">" + Label + "</Td> ";//Label
        //cbo = cbo + "<Td style=\"width:50%\"> <select id=\"secondaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" onchange=\"return createCustomModel()\">";
        cbo = cbo + "<Td style=\"width:50%\"> <select id=\"secondaryViewBy\" name=\"" + Name + "\" class=\"myTextbox3\" >";
        {
            i = 0;
            for (i = 0; i < data.getRowCount(); i++) {
                temp = data.getFieldValueString(i, 0);
                if (CurrVal.equalsIgnoreCase(temp)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                } else if (i == 0 && (CurrVal.equalsIgnoreCase("null") || CurrVal == null)) {
                    cbo = cbo + ("<option selected value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                } else {
                    cbo = cbo + ("<option value=\"" + temp + "\">" + data.getFieldValueString(i, 1) + "</option>");
                }
            }
        }
        cbo = cbo + "</select></Td></Tr></Table>";
        ////////////////////////////.println("CBO--------" + cbo);

        /// end of query

        return (cbo);
    }
    // end of class
    //modified by Bhargavi for showing text box in diff color if not in filters are applied on 15 nov 14
//    public String getMultiTextAreaNew(String Name, String Label, List<String> currValList, String key, String includeExclude,String reportId,String dimid) {

    public String getMultiTextAreaNew(String Name, String Label, List<String> currValList, String key, String includeExclude, String reportId, String dimid, List<String> notIn, List<String> notLike, List<String> like, Container container) {

        StringBuilder resultStr = new StringBuilder();
        String str1 = "";
        String temp = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String textBoxName = "selFilter_" + Name + "";
        String divName = "'" + Name + "SuggestList" + "'";
        String divName1 = Name + "SuggestList";
        String imageName = Name + "-plus";
        String excludeImgId = Name + "-excimg";
        StringBuilder iconsHtml = new StringBuilder();
        String function = "\"selId('" + textBoxName + "'," + divName + "," + mKey + "," + boxName + ")\"";
        String disableonkeyUpfunction = "\"disableonkeyselId(" + textBoxName + "," + divName + "," + mKey + "," + boxName + ")\"";
//        String currVal="";
//        if (currValList == null || currValList.isEmpty()) {
//            currVal = "All";
//        }

        Gson gson = new Gson();
        String filterStr = "";
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
//       list.add(CurrVal);
        Type listOfTestObject = new TypeToken<List<String>>() {
        }.getType();
        filterStr = gson.toJson(currValList, listOfTestObject);

        //Added by Ram For Removing Inintialize Default Filter.
        Type tarType = new TypeToken<List<String>>() {
        }.getType();
        List<String> filterStrNew = gson.fromJson(filterStr, tarType);
        HashMap<String, ArrayList<String>> filterVal = container.getInitilizeFilterElement();
        if (!filterVal.isEmpty()) {
            initialDefaultFilter = filterVal;
        }
        if (initialDefaultFilter.containsKey(dimid)) {
            ArrayList<String> viewByElementIds = new ArrayList();
            for (Object keyVal : initialDefaultFilter.keySet()) {
                viewByElementIds.add(keyVal.toString());
            }
            ArrayList filterList = (ArrayList) initialDefaultFilter.get(viewByElementIds.get(0));
            String defaultFilterVal = filterList.get(0).toString();
            int index = 0;
            if (filterStrNew.contains(defaultFilterVal)) {
                index = filterStrNew.indexOf(defaultFilterVal);
                filterStrNew.set(index, "ALL");
            }
            if (container.isReportDrill() && currValList.contains(defaultFilterVal)) {
                index = currValList.indexOf(defaultFilterVal);
                currValList.set(index, "ALL");
            }
        }

        String jsonToStr = new Gson().toJson(filterStrNew);
        filterStr = jsonToStr;
        //End Ram Code

        resultStr.append("<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">");
        resultStr.append("<tr style=\"width:100%\">");
        resultStr.append("<td>");
        resultStr.append("<table>");
        resultStr.append("<tr style=\"width:100%\">");
        resultStr.append("<Td width=\"10px\"><a href=\"#\" class=\"ui-icon ui-icon-arrowthickstop-1-s\"  style=\"\" onclick=\"showParamDiv('content_" + Name + "')\"></a></td>");
//        resultStr.append("<Td style=\"width:50%\" align=\"right\">" + Label + "</Td>");
        resultStr.append("<td align=\"left\"><span class=\"paramNameNew\" align=\"left\">" + Label + "</span></Td>");
        resultStr.append("</tr>");
        resultStr.append("<tr>");
        resultStr.append("<td colspan=\"2\">");
        resultStr.append("<div id='content_" + Name + "' >");
        resultStr.append("<table width='100%'>");
        resultStr.append("<tr>");
        resultStr.append("<td width='90%'>");
        //modified by Nazneen on 18 Nov 14 for handling null
//        if(!notIn.isEmpty() || !notLike.isEmpty() || !like.isEmpty()){
//                resultStr.append("<div id=\"textAreaLike_"+Name+"\" style=\"border:2px solid #FD482F\" class=\"textareaLikeDiv\">");
//            } else {
//                resultStr.append("<div id=\"textAreaLike_"+Name+"\" class=\"textareaLikeDiv\">");
        if (notIn != null && notLike != null && like != null) {
            if (!notIn.isEmpty() || !notLike.isEmpty() || !like.isEmpty()) {
                resultStr.append("<div id=\"textAreaLike_" + Name + "\" style=\"border:2px solid #FD482F\" class=\"textareaLikeDiv\">");
            } else {
                resultStr.append("<div id=\"textAreaLike_" + Name + "\" class=\"textareaLikeDiv\">");
            }
        } else {
            resultStr.append("<div id=\"textAreaLike_" + Name + "\" class=\"textareaLikeDiv\">");
        }
        //end by Nazneen

        resultStr.append("<div id=\"ol_" + Name + "\" class=\"ulHorizantal\">");
        StringBuilder currValIcon = new StringBuilder();
        for (String CurrVal : currValList) {
            currValIcon.append("<div id='" + Name + "_" + CurrVal + "' class='newparamView' style='width:" + (10 * (CurrVal.length())) + ";float:left;'>");
            currValIcon.append("<table><tr>");
            currValIcon.append("<td><span class='newParamName' >" + CurrVal + "</span></td>");
            currValIcon.append("<td ><a href=\"javascript:deleteNewParam('" + Name + "','" + Name + "_" + CurrVal + "','" + CurrVal + "')\" style=\"float:left\" class=\"ui-icon ui-icon-close\" ></a></td>");
            currValIcon.append("</tr></table>");
            currValIcon.append("</div>");
        }

        resultStr.append(currValIcon.toString());
        resultStr.append("</div>");
        /*
         * if(includeExclude.equalsIgnoreCase("LIKE") ||
         * includeExclude.equalsIgnoreCase("NOT LIKE")){
         * resultStr.append("<input type='text' style='' name='" + textBoxName +
         * "' id='" + textBoxName + "' autocomplete='off'
         * class='newTextBoxStyle' onblur=resetAllAjax() onfocus=" +
         * disableonkeyUpfunction + " value='' >"); resultStr.append("<input
         * name='" + Name + "' id='" + Name + "' autocomplete='off'
         * class='myTextbox6' value=\"" + filterStr + "\" >" );
         *
         * }else{
         */
        resultStr.append("<input  type='text' style='' name='" + textBoxName + "' id='" + textBoxName + "' autocomplete='off' class='newTextBoxStyle' onblur=resetAllAjax() onfocus=" + function + " value='' >");

        resultStr.append("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='" + Name + "' id='" + Name + "' class='' value='" + filterStr + "'>");

        /*
         * }
         */
        resultStr.append("<input  name=\"" + Name + "_excbox\" id=\"" + Name + "_exc\" style='display:none' value='" + includeExclude + "' >");
        /*
         * if ( includeExclude.equalsIgnoreCase("NOT_SELECTED") ){ iconsHtml =
         * "<img id='" + imageName + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer;'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>" + "<img id='" +
         * excludeImgId + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=excludeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/exclude.png'>"; }else
         * if(includeExclude.equalsIgnoreCase("INCLUDED") ||
         * includeExclude.equalsIgnoreCase("IN") ||
         * includeExclude.equalsIgnoreCase("LIKE")){ iconsHtml = "<img id='" +
         * imageName + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>" + "<img id='" +
         * excludeImgId + "' style='display:none' onmouseover='checkImg(this)'
         * onclick=excludeParameter(this,'" + textBoxName + "','" + textBoxName
         * + "'," + divName + "," + mKey + ","+boxName+")
         * src='images/exclude.png'>"; }else{
         *
         * iconsHtml = "<img id='" + imageName + "' style='display:none'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" +textBoxName+ "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>"; + "<img id='" +
         * excludeImgId + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=excludeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/exclude.png'>";
        }
         */





        resultStr.append("</div>");
//         resultStr.append(iconsHtml);
        resultStr.append("</td>");
        resultStr.append("<td style='float:left;margin-top:15px;'>");
        iconsHtml.append("<table><tr>");
//           //added by prabal for fontawesome icon
//            iconsHtml.append("<td style=\"padding: 0px 3px;\" width=\"\" align=\"left\"><i class=\"fa fa-book fa-2x\"  id='"+imageName+"'  onmouseover='checkImg(this)' onclick=includeParameter(this,'" + textBoxName + "','" + textBoxName + "'," + divName + "," + mKey + ","+boxName+") ></i></td>");
//           iconsHtml.append("<td  style=\"padding: 0px 3px;\" width=\"1%\" align=\"left\"><i class=\"fa fa-search fa-2x\" onclick=\"filterBy('DimFilterBy_"+Name+"','"+Label+"','"+reportId+"','"+dimid+"')\"></i></td>");
//           iconsHtml.append("<td  style=\"padding: 0px 3px;\" id='order_"+Name+"' width=\"1%\" align=\"left\"><i class=\"fa fa-sort  fa-2x\" alt=\"Asc\" title=\"Ascending Order\" onclick=\"changeOrder('asc_"+Name+"','" + Name + "')\"></i><input type=\"hidden\" id='orderVal_"+Name+"' name='orderVal_"+Name+"' value=\"orderAsc_"+Name+"\"></td>");
//           // ended by prabal

        iconsHtml.append("<td width=\"\" align=\"left\"><img id='" + imageName + "' style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer;' onmouseover='checkImg(this)' onclick=includeParameter(this,'" + textBoxName + "','" + textBoxName + "'," + divName + "," + mKey + "," + boxName + ")  src='images/exclude.png'></td>");
        iconsHtml.append("<td  style=\"padding: 0px 3px;\" width=\"1%\" align=\"left\"><i class=\"fa fa-search \" onclick=\"filterBy('DimFilterBy_" + Name + "','" + Label + "','" + reportId + "','" + dimid + "')\"></i></td>");
        iconsHtml.append("<td id='order_" + Name + "' width=\"1%\" align=\"left\"><img alt=\"Asc\" title=\"Ascending Order\" src=\"images/doubleUpArrow.gif\" onclick=\"changeOrder('asc_" + Name + "','" + Name + "')\"><input type=\"hidden\" id='orderVal_" + Name + "' name='orderVal_" + Name + "' value=\"orderAsc_" + Name + "\"></td>");
        iconsHtml.append("</tr></table>");
        resultStr.append(iconsHtml);
        resultStr.append("</Td>");
        resultStr.append("</Tr>");
// eddited by manik       resultStr.append("<Tr><Td style=\"width:100%\" colspan=\"2\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto\"></div></Td></Tr>");
        resultStr.append("<Tr><Td style=\"width:100%\" colspan=\"2\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto;position: absolute;\"></div></Td></Tr>");
        resultStr.append("</Table>");
        resultStr.append("</div>");
        resultStr.append("</td>");
        resultStr.append("</tr>");
        resultStr.append("</table>");
        resultStr.append("</td></tr></table>");

        return (resultStr.toString());
    }
    //sandeep

    public String getMultiTextAreakpi(String Name, String Label, List<String> currValList, String key, String includeExclude, String reportId, String dimid, List<String> comboIds, List<String> comboValues, String currVal) {




        StringBuilder resultStr = new StringBuilder();
        String str1 = "";
        String temp = "";
        String temp1 = "";
        String mKey = "'" + key + "'";
        String boxName = "'" + Name + "'";
        String textBoxName = "selFilter_" + Name + "";
        String divName = "'" + Name + "SuggestListkpi" + "'";
        String divName1 = Name + "SuggestListkpi";
        String imageName = Name + "-plus";
        String excludeImgId = Name + "-excimg";
        StringBuilder iconsHtml = new StringBuilder();
        String function = "\"selId('" + textBoxName + "'," + divName + "," + mKey + "," + boxName + ")\"";
        String disableonkeyUpfunction = "\"disableonkeyselId(" + textBoxName + "," + divName + "," + mKey + "," + boxName + ")\"";
//        String currVal="";
//        if (currValList == null || currValList.isEmpty()) {
//            currVal = "All";
//        }

        Gson gson = new Gson();
        String filterStr = "";
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
//       list.add(CurrVal);
        Type listOfTestObject = new TypeToken<List<String>>() {
        }.getType();
        filterStr = gson.toJson(currValList, listOfTestObject);

//        resultStr.append("<Table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"  style=\"width:100%\">");
//        resultStr.append("<tr style=\"width:100%\">");
//        resultStr.append("<td>");
//        resultStr.append("<table>");
//        resultStr.append("<tr style=\"width:100%\">");
//        resultStr.append("<Td width=\"10px\"><a href=\"#\" class=\"ui-icon ui-icon-arrowthickstop-1-s\"  style=\"\" onclick=\"showParamDiv('content_"+Name+"')\"></a></td>");
////        resultStr.append("<Td style=\"width:50%\" align=\"right\">" + Label + "</Td>");
//        resultStr.append("<td align=\"left\"><span class=\"paramNameNew\" align=\"left\">" + Label + "</span></Td>");
//        resultStr.append("</tr>");
//        resultStr.append("<tr>");
//         resultStr.append("<Td style=\"width:40%; font-style:bold;\" align=\"right\">Global FIlter:</Td> ");//Label
//        resultStr.append("<Td style=\"width:10%\"> <select id='selectparam'  name=\"" + Name + "\" onchange=\"getparamvales('selectparam','" + Name + "')\" class=\"myTextbox3\" >");
//
//            for (int i1 = 0; i1< comboIds.size(); i1++) {
//                temp = (String) comboIds.get(i1);
//                temp1 = (String) comboValues.get(i1);
//
//                if (currVal.equalsIgnoreCase(temp)) {
//
//                     resultStr.append("<option selected value=\"" + temp + "\">" + temp1 + "</option>");
//                } else {
//                  resultStr.append("<option value=\"" + temp + "\">" + temp1 + "</option>");
//                }
//            }
//
//
//        resultStr.append("</select></Td>");


//        resultStr.append("<td colspan=\"2\" style=\"width:50%;\">");

        resultStr.append("<div id='contentkpi_" + Name + "' >");

        resultStr.append("<table width='100%'>");
        resultStr.append("<tr>");
        resultStr.append("<td  width='90%' style=\"overflow:auto;\" height='10%'>");

        resultStr.append("<div id=\"textAreaLikekpi_" + Name + "\"style=\"height:20px; overflow:auto; \" class=\"textareaLikeDivkpi\">");
        resultStr.append("<div id=\"olkpi_" + Name + "\" class=\"ulHorizantal\">");
        StringBuilder currValIcon = new StringBuilder();
        for (String CurrVal : currValList) {
            currValIcon.append("<div id='" + Name + "_kpi" + CurrVal + "' class='newparamView' style='width:" + (10 * (CurrVal.length())) + ";float:left;'>");
            currValIcon.append("<table><tr>");
            currValIcon.append("<td><span class='newParamName' >" + CurrVal + "</span></td>");
            currValIcon.append("<td ><a href=\"javascript:deleteNewParamkpi('" + Name + "','" + Name + "_kpi" + CurrVal + "','" + CurrVal + "')\" style=\"float:left\" class=\"ui-icon ui-icon-close\" ></a></td>");
            currValIcon.append("</tr></table>");
            currValIcon.append("</div>");
        }

        resultStr.append(currValIcon.toString());
        resultStr.append("</div>");



        /*
         * if(includeExclude.equalsIgnoreCase("LIKE") ||
         * includeExclude.equalsIgnoreCase("NOT LIKE")){
         * resultStr.append("<input type='text' style='' name='" + textBoxName +
         * "' id='" + textBoxName + "' autocomplete='off'
         * class='newTextBoxStyle' onblur=resetAllAjax() onfocus=" +
         * disableonkeyUpfunction + " value='' >"); resultStr.append("<input
         * name='" + Name + "' id='" + Name + "' autocomplete='off'
         * class='myTextbox6' value=\"" + filterStr + "\" >" );
         *
         * }else{
         */
        resultStr.append("<input  type='text' style='' name='" + textBoxName + "' id='" + textBoxName + "' autocomplete='off' class='newTextBoxStyle' onblur=resetAllAjax()  value='' >");
        resultStr.append("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='" + Name + "' id='" + Name + "' class='' value='" + filterStr + "'>");
        resultStr.append("<input  type='text' style='border:1px solid #B2B2B2;background:transparent;float:left;display:none;' name='kpitext" + Name + "' id='kpitext" + Name + "' class='' value='" + filterStr + "'>");

        /*
         * }
         */
        resultStr.append("<input  name=\"" + Name + "_excbox\" id=\"" + Name + "_exc\" style='display:none' value='" + includeExclude + "' >");
        /*
         * if ( includeExclude.equalsIgnoreCase("NOT_SELECTED") ){ iconsHtml =
         * "<img id='" + imageName + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer;'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>" + "<img id='" +
         * excludeImgId + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=excludeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/exclude.png'>"; }else
         * if(includeExclude.equalsIgnoreCase("INCLUDED") ||
         * includeExclude.equalsIgnoreCase("IN") ||
         * includeExclude.equalsIgnoreCase("LIKE")){ iconsHtml = "<img id='" +
         * imageName + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>" + "<img id='" +
         * excludeImgId + "' style='display:none' onmouseover='checkImg(this)'
         * onclick=excludeParameter(this,'" + textBoxName + "','" + textBoxName
         * + "'," + divName + "," + mKey + ","+boxName+")
         * src='images/exclude.png'>"; }else{
         *
         * iconsHtml = "<img id='" + imageName + "' style='display:none'
         * onmouseover='checkImg(this)' onclick=includeParameter(this,'" +
         * textBoxName + "','" +textBoxName+ "'," + divName + "," + mKey +
         * ","+boxName+") src='images/include.png'>"; + "<img id='" +
         * excludeImgId + "'
         * style='vertical-align:inline;display:inline;width:12px;height:15px;cursor:pointer'
         * onmouseover='checkImg(this)' onclick=excludeParameter(this,'" +
         * textBoxName + "','" + textBoxName + "'," + divName + "," + mKey +
         * ","+boxName+") src='images/exclude.png'>";
        }
         */





        resultStr.append("</div>");
//         resultStr.append(iconsHtml);
        resultStr.append("</td>");

        resultStr.append("<td style='float:left;margin-top:5px;'width=\"10%\">");
        iconsHtml.append("<table><tr>");
        iconsHtml.append("<td width=\"\" align=\"left\"><img id='" + imageName + "' style='vertical-align:inline;display:inline;width:20px;height:22px;cursor:pointer;' onmouseover='checkImg(this)' onclick=includeparamkpi(this,'" + textBoxName + "','" + textBoxName + "'," + divName + "," + mKey + "," + boxName + ")  src='images/include.png'></td>");
        //iconsHtml.append("<td width=\"1%\" align=\"left\"><a href=\"#\" class=\"ui-icon ui-icon-search\"  style=\"\" onclick=\"filterBy('DimFilterBy_"+Name+"','"+Label+"','"+reportId+"','"+dimid+"')\"></a></td>");
        iconsHtml.append("<td id='order_" + Name + "'style=\"display:none\" width=\"1%\" align=\"left\"><img alt=\"Asc\" title=\"Ascending Order\" src=\"images/doubleUpArrow.gif\" onclick=\"changeOrder('asc_" + Name + "','" + Name + "')\"><input type=\"hidden\" id='orderVal_" + Name + "' name='orderVal_" + Name + "' value=\"orderAsc_" + Name + "\"></td>");
        iconsHtml.append("</tr></table>");
        resultStr.append(iconsHtml);
        resultStr.append("</Td>");
        resultStr.append("</Tr>");
        resultStr.append("<Tr><Td style=\"width:100%\" colspan=\"2\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle1\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto;background-color:white;position:absolute;\"></div></Td></Tr>");
        resultStr.append("</Table>");
        resultStr.append("</div>");

//        resultStr.append("</td>");
//        resultStr.append("</tr>");
//        resultStr.append("</table>");
//        resultStr.append("</td></tr></table>");

        return (resultStr.toString());
    }

    public String getOperatorFiltersHtml(PbReportCollection collect, String dimId, String reportId, String theamcolour) { //changed by sruthi
        StringBuilder resultHtml = new StringBuilder();
        HashMap<String, HashMap<String, List>> operatorFilt = collect.operatorFilters;
        LinkedHashMap reportParametersValues = collect.reportParametersValues;
        LinkedHashMap<String, ArrayList<String>> reportParameters = collect.reportParameters;
        List<String> paraminfo = reportParameters.get(dimId);
        String formattedDim = "new_" + paraminfo.get(9);
        String includeExclude = paraminfo.get(10);
        String textBoxName = "selFilter_" + formattedDim + "";
        String divName = "'" + formattedDim + "SuggestList" + "'";
        String divName1 = formattedDim + "SuggestList";
        String imageName = formattedDim + "-plus";
        String boxName = "'" + formattedDim + "'";
        String function = "\"selId('" + textBoxName + "'," + divName + "," + dimId + "," + boxName + ")\"";
        String onkeyUpLikefun = "\"onkeyUpLikefun('" + formattedDim + "'," + dimId + ",'" + formattedDim + "_selecFil')\"";
        String onkeyUpNotLikefun = "\"onkeyUpNotLikefun('" + formattedDim + "'," + dimId + ",'" + formattedDim + "_selecFil')\"";
        Set keySet = operatorFilt.keySet();
        Iterator iter = keySet.iterator();
//        resultHtml.append("<form id=\"paramOperatorsForm\" method=\"POST\">");
        resultHtml.append("<div >");
        resultHtml.append("<table >");
        resultHtml.append("<tr >");
        resultHtml.append("<td><span class=\"paramNameNew\" align=\"left\">FormatType</span></td>");
        resultHtml.append("<td><select  name=\"" + formattedDim + "_selecFil\" id=\"" + formattedDim + "_selecFil\" onchange=\"changeOperator(this.id,'" + formattedDim + "','" + dimId + "')\">");
        String selected = "";
        if (includeExclude.equalsIgnoreCase("NOT LIKE")) {
            includeExclude = "NOTLIKE";
        }
        for (String filter : filtertype) {
            selected = "";
            if (filter.equalsIgnoreCase(includeExclude)) {
                selected = "selected";
            }
            resultHtml.append("<option " + selected + " value='" + filter + "'>" + filter + "</option>");
        }
        resultHtml.append("</select>");
        resultHtml.append("</td>");
        resultHtml.append("</tr>");
        resultHtml.append("<tr>");
        resultHtml.append("<td><span class=\"paramNameNew\" align=\"left\">Apply Filter</span></td>");

        resultHtml.append("<td>");
        String likedisplay = "none";
        String notLikedisplay = "none";
        String notIndisplay = "none";
        if (includeExclude.equalsIgnoreCase("LIKE")) {
            likedisplay = "";
        } else if (includeExclude.equalsIgnoreCase("NOTLIKE") || includeExclude.equalsIgnoreCase("NOT LIKE")) {
            notLikedisplay = "";
        } else {
            notIndisplay = "";
        }
        /*
         * Like Not Like TextBox
         */
        resultHtml.append("<input  type='text' style='display:" + likedisplay + "' name='" + textBoxName + "' id='LIKE_" + textBoxName + "' autocomplete='off' class='newTextBoxStyle' onClick=" + onkeyUpLikefun + " value='' >");
        resultHtml.append("<input  type='text' style='display:" + notLikedisplay + "' name='" + textBoxName + "' id='NOTLIKE_" + textBoxName + "' autocomplete='off' class='newTextBoxStyle'  onClick=" + onkeyUpNotLikefun + " value='' >");
        /*
         * Not In Text Box
         */
        //added by sruthi
        if (theamcolour.equalsIgnoreCase("orange")) {
            resultHtml.append("<input  type='text' style='display:" + notIndisplay + ";background-color:#FFFFFF;' name='" + textBoxName + "' id='" + textBoxName + "' autocomplete='off'  onblur=resetAllAjax() onfocus=" + function + " value='' >");
        } else {
            resultHtml.append("<input  type='text' style='display:" + notIndisplay + "' name='" + textBoxName + "' id='" + textBoxName + "' autocomplete='off'  onblur=resetAllAjax() onfocus=" + function + " value='' >");
        }
        //ended by sruthi
        resultHtml.append("</td>");
        resultHtml.append("</tr>");
        resultHtml.append("<Tr><Td style=\"width:100%\" colspan=\"2\"><div onscroll='onScrollDiv(this)' class=\"ajaxboxstyle\" id=\"" + divName1 + "\" style=\"display:none;overflow:auto\"></div></Td></Tr>");
        resultHtml.append("<tr>");
        resultHtml.append("<td colspan=\"2\">");
        resultHtml.append("<div style=\"\">");
        resultHtml.append("<table>");
        Gson gson = new Gson();
        String filterStr = "";
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
//       list.add(CurrVal);
        Type listOfTestObject = new TypeToken<List<String>>() {
        }.getType();

        for (String key : filtertype) {
            HashMap<String, List> valueMap = operatorFilt.get(key);
            List<String> valueList = valueMap.get(dimId);
            resultHtml.append("<tr>");
            resultHtml.append("<td><span class=\"paramNameNew\" align=\"left\">" + key + "</span></td>");
            resultHtml.append("<td>");
            resultHtml.append("<div id=\"textArea_" + key + "_" + formattedDim + "\" class=\"textareaLikeDiv\">");
            if (key.equalsIgnoreCase("NOTIN")) {
                resultHtml.append("<div id=\"ol_" + formattedDim + "\" class=\"ulHorizantal\">");
            } else {
                resultHtml.append("<div id=\"ol" + key + "_" + formattedDim + "\" class=\"ulHorizantal\">");
            }

            StringBuilder currValIcon = new StringBuilder();
            String removefiltId = "";
            if (key.equalsIgnoreCase("NOTIN")) {
                removefiltId = formattedDim;
            } else {
                removefiltId = key + "_" + formattedDim;
            }
            if (valueList != null) {
                for (String CurrVal : valueList) {
                    currValIcon.append("<div id='" + formattedDim + "_" + CurrVal.replace("%", "") + "' class='newparamView' style='width:" + (10 * (CurrVal.length())) + ";float:left;'>");
                    currValIcon.append("<table><tr>");
                    currValIcon.append("<td><span class='newParamName' >" + CurrVal + "</span></td>");

                    currValIcon.append("<td ><a href=\"javascript:removeFilter('" + removefiltId + "','" + formattedDim + "_" + CurrVal.replace("%", "") + "','" + CurrVal.replace("%", "") + "')\" style=\"float:left\" class=\"ui-icon ui-icon-close\" ></a></td>");
                    currValIcon.append("</tr></table>");
                    currValIcon.append("</div>");
                }
            }
            resultHtml.append(currValIcon.toString());
            resultHtml.append("</div>");
            resultHtml.append("</div>");
            if (key.equalsIgnoreCase("NOTIN")) {
                if (valueList == null) {
                    valueList = new ArrayList<String>();
//                   valueList.add("All");
                }
                filterStr = gson.toJson(valueList, listOfTestObject);
                resultHtml.append("<input  type='hidden' name='" + key + "_" + formattedDim + "' id='" + formattedDim + "' value='" + filterStr + "'>");
            } else {
                if (valueList == null) {
                    valueList = new ArrayList<String>();
                }
                filterStr = gson.toJson(valueList, listOfTestObject);
                resultHtml.append("<input  type='hidden' name='" + key + "_" + formattedDim + "' id='" + key + "_" + formattedDim + "' value='" + filterStr + "'>");
            }

            resultHtml.append("</td>");
            resultHtml.append("</tr>");
        }
        resultHtml.append("</table>");
        resultHtml.append("</td>");
        resultHtml.append("</tr>");
        resultHtml.append("<tr>");
        resultHtml.append("<td colspan=\"2\" align=\"center\"><input type='button' value='Done' class='navtitle-hover' onclick=\"closeFilterBy('" + dimId + "','" + formattedDim + "','" + formattedDim + "_selecFil','" + reportId + "')\"></td>");
        resultHtml.append("</tr></table>");
//        resultHtml.append("</form>");
        return resultHtml.toString();
    }
}
