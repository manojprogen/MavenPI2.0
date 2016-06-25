package org.querydesigner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SaveDimTablesList extends org.apache.struts.action.Action {

    public static Logger logger = Logger.getLogger(SaveDimTablesList.class);
    Connection con = null;
    Statement st = null;
    Statement st1 = null;
    Statement st2 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String Q1, Q2, Q3;
    private final static String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            String tables = request.getParameter("tables");
            //String[] tabids =  request.getParameterValues("chk1");
            String[] tabids = tables.split(",");
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tab1 "+tabids[0]);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tab2 "+tabids[1]);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("tab3 "+tabids[2]);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("IN ACTION-->"+tabids.length);
            int dim_id = Integer.parseInt((String) request.getSession().getAttribute("dimId"));
            con = ProgenConnection.getInstance().getConnection();
            st = con.createStatement();


            for (int i = 0; i < tabids.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    Q1 = "insert into prg_qry_dim_tables (dim_id,tab_id) values (" + dim_id + "," + tabids[i] + ")";
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("DIM MASTER TABLE-->"+Q1);
                    st.executeUpdate(Q1);
                    Q2 = "select column_id from prg_db_master_table_details where table_id in (" + tabids[i] + ") and is_active='Y'";

                } else {
                    Q1 = "insert into prg_qry_dim_tables (dim_tab_id,dim_id,tab_id) values (prg_qry_dim_tables_seq.nextval," + dim_id + "," + tabids[i] + ")";
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("DIM MASTER TABLE-->"+Q1);
                    st.executeUpdate(Q1);
                    Q2 = "select column_id from prg_db_master_table_details where table_id in (" + tabids[i] + ") and is_active='Y'";

                }

                st1 = con.createStatement();
                rs = st1.executeQuery(Q2);
                while (rs.next()) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        Q3 = "insert into prg_qry_dim_tab_details (dim_tab_id,col_id,is_available,is_pk_key) values (ident_current('PRG_QRY_DIM_TABLES')," + rs.getString(1) + ",'Y','N')";

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        Q3 = "insert into prg_qry_dim_tab_details (dim_tab_id,col_id,is_available,is_pk_key) values ((select LAST_INSERT_ID(dim_tab_id) FROM PRG_QRY_DIM_TABLES ORDER BY 1 DESC LIMIT 1)," + rs.getString(1) + ",'Y','N')";

                    } else {
                        Q3 = "insert into prg_qry_dim_tab_details (dim_tab_col_id,dim_tab_id,col_id,is_available,is_pk_key) values (prg_qry_dim_tab_details_seq.nextval,prg_qry_dim_tables_seq.currval," + rs.getString(1) + ",'Y','N')";

                    }

                    st2 = con.createStatement();
                    st2.executeUpdate(Q3);

                }

            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            st2.close();
            rs.close();
            st1.close();
            st.close();
            con.close();
        }
        return mapping.findForward(SUCCESS);

    }
}
