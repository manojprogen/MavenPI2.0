/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class SaveSearch {

    private HttpServletRequest request1;
    private String SearchText = "";
    public static Logger logger = Logger.getLogger(SaveSearch.class);

    public String getSearchText() {
        return SearchText;
    }

    public void setSearchText(String SearchText) {
        this.SearchText = SearchText;
    }

    public HttpServletRequest getRequest1() {
        return request1;
    }

    public void setRequest1(HttpServletRequest request1) {
        this.request1 = request1;
    }

    public void saveFavSearchData() {
        String userId = null;

    }

    public void saveSearchData() {
        StringBuffer sqlstr = new StringBuffer();
        Statement st = null;
//        ProgenConnection pg = new ProgenConnection();
        Connection con = null;
        HttpSession session = null;
        String userId = null;
        ////Code to be completed 

        try {
            session = request1.getSession(false);
            userId = String.valueOf(session.getAttribute("USERID"));
            sqlstr.append("insert into prg_user_search_details ");
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
            sqlstr.append(request1.getContextPath() + "','");
            sqlstr.append(request1.getRequestURL().toString() + "','");
            sqlstr.append(request1.getSession().getId());
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                sqlstr.append("',getdate()");
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                sqlstr.append("',sysdate");
            } else {
                sqlstr.append("',sysdate");
            }

            sqlstr.append(", '" + request1.getRequestURI() + "' ");
            sqlstr.append(", '" + request1.getQueryString() + "' ");
            sqlstr.append(", '" + request1.getRemoteUser() + "' ");
            sqlstr.append(", '" + request1.getRemoteAddr() + "' ");
            sqlstr.append(", '" + request1.getRemoteHost() + "' ");
//            sqlstr.append(", '" + userId + "' ");
//            sqlstr.append(", '" + reportId + "' ");
//            sqlstr.append(", '" + seconds + "' ");
//            sqlstr.append(" )");




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
}
