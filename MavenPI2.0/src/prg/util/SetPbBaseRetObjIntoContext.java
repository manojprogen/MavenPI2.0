package prg.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author Prabal Pratap Singh @Use This class is used to execute all the select
 * queries in just creating one connection and set all data into context with
 * its table name @Date 05-Feb-2016
 *
 */
public class SetPbBaseRetObjIntoContext {

    public static Logger logger = Logger.getLogger(SetPbBaseRetObjIntoContext.class);
    public static PbDb pbdb = new PbDb();
    public static Connection con = null;
    public static PreparedStatement ps = null;
    public static PbReturnObject rs = null;
    private static int activeUserCount = 0;
    private static int purchasedUsers = 0;

    public static int setPbBaseRetObjIntoContext(int userId, ServletContext context, HttpSession session) throws SQLException {

//        long t1=System.currentTimeMillis();
        con = pbdb.getConnection();
        String companyId = "0000";
        String languageCountryCode = "en_us";
        Locale currLocale;
//       System.out.println("******************************************************************************");
        try {
            ps = con.prepareStatement("select * from prg_ar_users where pu_id=?" );
            ps.setInt(1, userId);
            ps.execute();
            rs = new PbReturnObject(ps.executeQuery());
            if (rs != null && rs.rowCount > 0) {
                context.setAttribute("prg_ar_users", rs);
                companyId = rs.getFieldValueString(0, "COMPANY_ID");

                session.setAttribute("companyName", rs.getFieldValueString(0, "PU_LOGIN_ID"));
                if (companyId == null || companyId.isEmpty() || companyId.equalsIgnoreCase(" ")) {
                    companyId = "0000";
                }
                session.setAttribute("companyId", companyId);// set companyId into session

                if (session.getAttribute("status") != null && session.getAttribute("status").toString().equalsIgnoreCase("ok")) {
                } else {
                    if (!rs.getFieldValueString(0, "LANGUAGE_COUNTRY_CODE").isEmpty()) {
                        languageCountryCode = rs.getFieldValueString(0, "LANGUAGE_COUNTRY_CODE");
                    }
                }
                String[] header = new String[3];

                if (rs.getFieldValueString(0, "HEADER_TAGS") != null && !rs.getFieldValueString(0, "HEADER_TAGS").isEmpty()) {
                    header = rs.getFieldValueString(0, "HEADER_TAGS").split(",");
                    session.setAttribute("HEADER_FONT", header[0]);
                    session.setAttribute("HEADER_LENGTH", header[1]);
                }
                session.setAttribute("languageCountryCode", languageCountryCode);
                session.setAttribute("startPage", rs.getFieldValueString(0, "START_PAGE"));
            }
            context.setAttribute("LANGUAGE_COUNTRY_CODE", languageCountryCode);
            if (languageCountryCode != null && !languageCountryCode.isEmpty() && !languageCountryCode.equalsIgnoreCase("null")) {
                String newLocale[] = languageCountryCode.split("_");
                currLocale = new Locale(newLocale[0], newLocale[1]);
            } else {
                currLocale = new Locale("en", "US");
            }
            session.setAttribute("UserLocaleFormat", currLocale);
            rs = null;
            ps = null;
            String sId = "3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,38,39,40,41,42";
            ps = con.prepareStatement("SELECT SETUP_KEY,SETUP_DATE_TYPE_VALUE,SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_ID in (" + sId + ")");
//            System.out.println("***********PRG_GBL_SETUP_VALUES==========================================**************");
            rs = new PbReturnObject(ps.executeQuery());
            if (rs != null && rs.rowCount > 0) {
                SetGBLParamIntoSession.setGBLParamIntoSession(rs, session);
                context.setAttribute("PRG_GBL_SETUP_VALUES", rs);
            }


//            rs=null;
//            ps = null;
//            ps = con.prepareStatement("select distinct multi_company from prg_usr_col_disp_label where User_Id='"+userId+"'");
////            System.out.println("***********prg_usr_col_disp_label==========================================**************");
//            rs= new PbReturnObject( ps.executeQuery());
//            if(rs!=null && rs.rowCount>0){
//                context.setAttribute("prg_usr_col_disp_label", rs);
//                   for(int k=0;k<rs.rowCount;k++){
//                       if(rs.getFieldValueString(k, "MULTI_COMPANY").equalsIgnoreCase("Y")){
//                session.setAttribute("isMultiCompany", true);
//                             break;
//                       }
//                   }
//               
////                
//            }else{
//                if(session.getAttribute("status")!=null && session.getAttribute("status").toString().equalsIgnoreCase("ok")){
//                     session.setAttribute("isMultiCompany", true);// for varection
//                }else{
//                session.setAttribute("isMultiCompany", false);
//            }
//            
//            }



            rs = null;
            ps = null;
            if (session.getAttribute("isCompLogo") != null && session.getAttribute("isCompLogo").toString().equalsIgnoreCase("YES")) {
                ps = con.prepareStatement("SELECT COMPANY_LOGO,BUSSINESS_LOGO,COMPANY_TITLE,BUSSINESS_TITLE,RIGHT_WEB_SITE_URL,LEFT_WEB_SITE_URL,COPYRIGTH_MSG,WELCOME_MSG,LEFT_SIDE_LOGO_HEIGHT ,LEFT_SIDE_LOGO_WIDTH ,RIGHT_SIDE_LOGO_HEIGHT,RIGHT_SIDE_LOGO_WIDTH,COMPANY_FEVICON,COMPANY_FEVICON_TITLE FROM PRG_COMPANY_LOGO WHERE COMPANY_ID=?");
                ps.setString(1, companyId);
                rs = new PbReturnObject(ps.executeQuery());
                if (rs != null && rs.rowCount > 0) {
                    context.setAttribute("PRG_COMPANY_LOGO", rs);
                    session.setAttribute("compLogo", rs.getFieldValueString(0, 0));
                    session.setAttribute("bussLogo", rs.getFieldValueString(0, 1));
                    session.setAttribute("compTitle", rs.getFieldValueString(0, 2));
                    session.setAttribute("bussTitle", rs.getFieldValueString(0, 3));
                    session.setAttribute("rightWebSiteUrl", rs.getFieldValueString(0, 4));
                    session.setAttribute("leftWebSiteUrl", rs.getFieldValueString(0, 5));
                    session.setAttribute("copyRightMsg", rs.getFieldValueString(0, 6));
                    session.setAttribute("welcomeMsg", rs.getFieldValueString(0, 7));
                    session.setAttribute("leftSideLogoHeight", rs.getFieldValueString(0, 8));
                    session.setAttribute("leftSideLogoWidth", rs.getFieldValueString(0, 9));
                    session.setAttribute("rightSideLogoHeight", rs.getFieldValueString(0, 10));
                    session.setAttribute("rightSideLogoWidth", rs.getFieldValueString(0, 11));
                    session.setAttribute("compFavicon", rs.getFieldValueString(0, 12));
                    session.setAttribute("compFavtitle", rs.getFieldValueString(0, 13));
                }
            }

            if (session.getAttribute("isMultiCompCal") != null && session.getAttribute("isMultiCompCal").toString().equalsIgnoreCase("YES")) {
                rs = null;
                ps = null;
                ps = con.prepareStatement(" SELECT A.DENOM_TABLE FROM PRG_CALENDER_SETUP A,PRG_COMPANY_CALANDER B WHERE A.CALENDER_ID = B.CALENDER_ID AND B.DEFAULT_VAL = 'Y' AND B.COMPANY_ID =?");
                ps.setString(1, companyId);
                rs = new PbReturnObject(ps.executeQuery());
                if (rs != null && rs.rowCount > 0) {
                    context.setAttribute("DENOM_TABLE", rs);
                    session.setAttribute("denomTable", rs.getFieldValueString(0, 0));
                }
            }   // end of multicompanycal if



            rs = null;
            ps = null;
            ps = con.prepareStatement("SELECT ANALYZERS from PRG_TEST_DATA");
            rs = new PbReturnObject(ps.executeQuery());
            if (rs != null) {
                purchasedUsers = Integer.parseInt(rs.getFieldValueString(0, 0));
            }

            rs = null;
            ps = null;
            ps = con.prepareStatement("select PU_ID from prg_ar_users where PU_ACTIVE='Y' AND PU_ID not in (41)");
            rs = new PbReturnObject(ps.executeQuery());
            if (rs != null) {
                activeUserCount = rs.getRowCount();
                context.setAttribute("activate", activeUserCount);
            }
            if (purchasedUsers > activeUserCount) {
                session.setAttribute("userActiveStatus", "Y");
            } else {
                session.setAttribute("userActiveStatus", "N");
            }




            rs = null;
            ps = null;
            String query="SELECT USER_ID, PRIVELEGE_ID FROM PRG_AR_USER_PRIVELEGES where USER_ID in(" + userId + ")";
            ps = con.prepareStatement(query);
            rs = new PbReturnObject(ps.executeQuery());
            if (rs != null) {
                activeUserCount = rs.getRowCount();
                context.setAttribute("startPagePrivilages", rs);
//                session.setAttribute(sId, t);
            }


        } catch (NumberFormatException ex) {
            logger.error("Exception: ", ex);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
            return 0;
        }

    }
}
