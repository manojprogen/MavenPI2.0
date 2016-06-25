package prg.tracker.scheduler;

import com.progen.connection.ConnectionMetadata;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.SchedulerBD;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.scheduler.tracker.Tracker;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbEmailConfig;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class TrackerStartUpServlet extends HttpServlet {

    public static Logger logger = Logger.getLogger(TrackerStartUpServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();

        PbReturnObject pbretObj = null;
        PbDb pbdb = new PbDb();

        Properties connProps = new Properties();
        InputStream servletStream = getServletContext().getResourceAsStream("WEB-INF/MetadataConn.xml");
        ConnectionMetadata metadata = null;
        SchedulerBD bd = new SchedulerBD();
        SchedulerDAO dao = new SchedulerDAO();

        if (servletStream != null) {
            try {
                connProps.loadFromXML(servletStream);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
            metadata = new ConnectionMetadata(connProps);
            ProgenConnection.setConnectionMetadata(metadata);
        }
        PbReportViewerDAO pbrv = new PbReportViewerDAO();
        PbReturnObject returnObj = new PbReturnObject();
        returnObj = pbrv.getEmailConfigDetails();
        if (returnObj.getRowCount() > 0) {
            PbEmailConfig emailConfigObj = PbEmailConfig.getPbEmailConfig();

            if (emailConfigObj == null) {
                PbEmailConfig.createEmailConfigfrmDB(returnObj);
//                       PbEmailConfig emailConfig = new PbEmailConfig();
//                       emailConfig.setSmtpHostName(returnObj.getFieldValueString(0,0));
//                       emailConfig.setSmtpPortNo(returnObj.getFieldValueString(0,1));
//                       emailConfig.setFromAddr(returnObj.getFieldValueString(0,2));
//                       emailConfig.setDebug(returnObj.getFieldValueString(0,3));
//                       emailConfig.setAuthUser(returnObj.getFieldValueString(0,4));
//                       String pswd = returnObj.getFieldValueString(0,5);
//                       String decryptPwd = DeEncrypter.getInstance().decrypt(pswd); 
//                       emailConfig.setAuthPwd(decryptPwd);
            }
        }

//        PbEmailConfig emailConfig = PbEmailConfig.getPbEmailConfig();
//
//        if (emailConfig == null){
//            Properties emailProps=new Properties();
//            servletStream = getServletContext().getResourceAsStream("WEB-INF/EmailConfig.xml");
//            if( servletStream != null ){
//                try{
//                    emailProps.loadFromXML(servletStream);
//                    PbEmailConfig.createEmailConfig(emailProps);
//                }
//                catch(Exception e){
//                    logger.error("Exception: ",e);
//                }
//            }
//        }

        try {

            //Schedule the reports

            String reportSQL = "select REPORT_SCHEDULE_ID from prg_report_scheduler";
            PbReturnObject retObj = pbdb.execSelectSQL(reportSQL);

            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    String reportScheduleId = retObj.getFieldValueString(i, "REPORT_SCHEDULE_ID");
                    ReportSchedule schedule = dao.getReportScheduleDetails(reportScheduleId, false);
                    bd.scheduleReport(schedule, true);
                }
            }

            //Schedule the trackers

            String sqlstring = "select Tracker_id from PRG_TRACKER_MASTER";

            pbretObj = pbdb.execSelectSQL(sqlstring);
            if (pbretObj != null && pbretObj.getRowCount() != 0) {
                for (int i = 0; i < pbretObj.getRowCount(); i++) {
                    int trackerId = pbretObj.getFieldValueInt(i, "TRACKER_ID");
                    Tracker tracker = dao.getTracker(trackerId, false);
                    bd.scheduleTracker(tracker, true);
                }
            }
        } catch (Exception exception) {
            logger.error("Exception: ", exception);
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            out = response.getWriter();
        } catch (IOException ex) {
        }

        try {
            /*
             * TODO output your page here out.println("<html>");
             * out.println("<head>"); out.println("<title>Servlet
             * TrackerStartUpServlet</title>"); out.println("</head>");
             * out.println("<body>"); out.println("<h1>Servlet
             * TrackerStartUpServlet at " + request.getContextPath () +
             * "</h1>"); out.println("</body>"); out.println("</html>");
             */
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}