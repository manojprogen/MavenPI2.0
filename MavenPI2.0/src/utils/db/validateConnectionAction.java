package utils.db;

import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class validateConnectionAction extends DispatchAction {

    public static Logger logger = Logger.getLogger(validateConnectionAction.class);

    public ActionForward testPostgreConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        String username = request.getParameter("usrename");
        String password = request.getParameter("password");
        String server = request.getParameter("server");
        String port = request.getParameter("port");
        String databasename = request.getParameter("pstgredbname");







        Connection con = ProgenConnection.postgresqlConnection(server, port, databasename, username, password);

        if (con == null) {
            out.println("Connection Failed.");
        } else {
            out.println("Connection Successfull.");
        }

        return null;
    }

    public ActionForward testMySqlConnection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try {
            PrintWriter out = response.getWriter();
            String username = request.getParameter("usrename");
            String password = request.getParameter("password");
            String server = request.getParameter("server");
            String port = request.getParameter("port");
            String databasename = request.getParameter("mysqldbname");







//        Connection con = ProgenConnection.getmySQLConnection(databasename, server, port, username, password);
            Connection con = ProgenConnection.getInstance().getConnection();

            if (con == null) {
                out.println("Connection Failed.");
            } else {
                out.println("Connection Successfull.");
            }

        } catch (Exception e) {
            logger.error("Exception:", e);

        }
        return null;
    }
}
