/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class DataTracker {

    public static Logger logger = Logger.getLogger(DataTracker.class);

    public void setclickdata(HttpServletRequest request1, String reportId) throws SQLException {
        StringBuffer sqlstr = new StringBuffer();
        Statement st = null;
//        ProgenConnection pg = new ProgenConnection();
        Connection con = null;
        HttpSession session = null;
        String userId = null;
        try {
            session = request1.getSession(false);
            userId = String.valueOf(session.getAttribute("USERID"));
            sqlstr.append("insert into prg_user_hitcalc ");
            sqlstr.append(" (GETCONTEXTPATH , ");
            sqlstr.append(" GETREQUESTURL , ");
            sqlstr.append(" GETSESSION , ");
            sqlstr.append(" GETDATE , ");
            sqlstr.append(" getRequestURI ,");
            sqlstr.append(" getQueryString ,");
            sqlstr.append(" getRemoteUser , ");
            sqlstr.append(" getRemoteAddr , ");
            sqlstr.append(" getRemoteHost , ");
            sqlstr.append("  user_id,report_id) ");
            sqlstr.append(" values(' ");
            sqlstr.append(request1.getContextPath() + "','");
            sqlstr.append(request1.getRequestURL().toString() + "','");
            sqlstr.append(request1.getSession().getId());
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                sqlstr.append("',GETDATE()");
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                sqlstr.append("',curdate()");
            } else {
                sqlstr.append("',sysdate");
            }
            sqlstr.append(", '" + request1.getRequestURI() + "' ");
            sqlstr.append(", '" + request1.getQueryString() + "' ");
            sqlstr.append(", '" + request1.getRemoteUser() + "' ");
            sqlstr.append(", '" + request1.getRemoteAddr() + "' ");
            sqlstr.append(", '" + request1.getRemoteHost() + "' ");
            sqlstr.append(", '" + userId + "' ");
            sqlstr.append(", '" + reportId + "' ");
            sqlstr.append(" )");




            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();
            st.executeUpdate(sqlstr.toString());
        } catch (SQLException e) {
            logger.error("Exception:", e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
    }

    public void setclickdata(HttpServletRequest request1, String reportId, double seconds) throws SQLException {
        StringBuilder sqlstr = new StringBuilder();
        Statement st = null;
//        ProgenConnection pg = new ProgenConnection();
        Connection con = null;
        HttpSession session = null;
        String userId = null;
        try {
            session = request1.getSession(false);
            userId = String.valueOf(session.getAttribute("USERID"));
            sqlstr.append("insert into prg_user_hitcalc ");
            sqlstr.append(" (GETCONTEXTPATH , ");
            sqlstr.append(" GETREQUESTURL , ");
            sqlstr.append(" GETSESSION , ");
            sqlstr.append(" GETDATE , ");
            sqlstr.append(" getRequestURI ,");
            sqlstr.append(" getQueryString ,");
            sqlstr.append(" getRemoteUser , ");
            sqlstr.append(" getRemoteAddr , ");
            sqlstr.append(" getRemoteHost , ");
            sqlstr.append("  user_id,report_id, ");
            sqlstr.append(" LOAD_TIME_SEC)");
            sqlstr.append(" values(' ");
            sqlstr.append(request1.getContextPath()).append("','");
            sqlstr.append(request1.getRequestURL().toString()).append("','");
            sqlstr.append(request1.getSession().getId());
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                sqlstr.append("',getdate()");
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                sqlstr.append("',curdate()");
            } else {
                sqlstr.append("',sysdate");
            }

            sqlstr.append(", '").append(request1.getRequestURI()).append("' ");
            sqlstr.append(", '").append(request1.getQueryString()).append("' ");
            sqlstr.append(", '").append(request1.getRemoteUser()).append("' ");
            sqlstr.append(", '").append(request1.getRemoteAddr()).append("' ");
            sqlstr.append(", '").append(request1.getRemoteHost()).append("' ");
            sqlstr.append(", '").append(userId).append("' ");
            sqlstr.append(", '").append(reportId).append("' ");
            sqlstr.append(", '").append(seconds).append("' ");
            sqlstr.append(" )");
            PbDb pbdb = new PbDb();
            int result = pbdb.execUpdateSQL(sqlstr.toString());
        } catch (SQLException e) {
            logger.error("Exception:", e);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void setLoginInformation(HttpServletRequest request, String userName) {
        StringBuilder str = new StringBuilder();
        HttpSession session = null;
        String userId = null;
        Statement st = null;
        //        ProgenConnection pg = new ProgenConnection();
        Connection con = null;
        try {
            session = request.getSession(false);
            /// System.out.println("request.getSession().getId()"+request.getSession().getId());
            System.out.println("session" + session.getId());
            userId = String.valueOf(session.getAttribute("USERID"));
            str.append("insert into HIT_CALC ");
            str.append(" (USER_ID , ");
            str.append(" SESSION_ID , ");
            str.append(" STATUS , ");
            str.append(" LOGIN_DATE , ");
            str.append(" LOGIN_TIME ,");
            str.append(" LOGOUT_DATE ,");
            str.append(" LOGOUT_TIME , ");
            str.append(" BROWSER_TYPE , ");
            str.append(" SCREEN_RESOLUTION , ");
            str.append(" IP_ADDRESS, ");
            str.append(" USER_NAME ) ");
            str.append(" values(' ");
            str.append(userId).append("','");
            str.append(request.getSession().getId());
            str.append("','Y'");
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                str.append(",getdate()");
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                str.append(",curdate()");
            } else {
                str.append(",sysdate");
            }
            //     int second, minute, hour;
            GregorianCalendar date = new GregorianCalendar();
            //     second = date.get(Calendar.SECOND);
            //     minute = date.get(Calendar.MINUTE);
            //     hour = date.get(Calendar.HOUR);
            String loginTime = date.get(Calendar.HOUR) + " : " + date.get(Calendar.MINUTE) + " : " + date.get(Calendar.SECOND);
            str.append(",'").append(loginTime).append("',");
            str.append("null,null");
            str.append(",'").append(request.getHeader("User-Agent").toUpperCase()).append("'");
            // str.append(",'" + Toolkit.getDefaultToolkit().getScreenResolution() + "'");
            str.append(",null");
            str.append(",'").append(request.getRemoteAddr()).append("'");
            str.append(",'").append(userName).append("'");
            str.append(" )");
//            System.out.println("LooginInformation"+str.toString());
            PbDb pbdb = new PbDb();
            int result = pbdb.execUpdateSQL(str.toString());
        } catch (SQLException e) {
            logger.error("Exception:", e);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
}
