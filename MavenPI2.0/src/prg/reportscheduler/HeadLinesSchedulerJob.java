package prg.reportscheduler;

import com.progen.datasnapshots.DataSnapshotDAO;
import com.progen.scheduler.ReportSchedule;
import com.progen.scheduler.db.SchedulerDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import prg.util.PbMail;
import prg.util.PbMailParams;

public class HeadLinesSchedulerJob implements Job {

    public static Logger logger = Logger.getLogger(HeadLinesSchedulerJob.class);
    PbMailParams params = null;
    PbMail mailer = null;

    public void execute(JobExecutionContext jec) {

        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        String headlineId = (String) dataMap.get("headlineId");
        String toAddress = (String) dataMap.get("toAddress");
        String scheduleTime = (String) dataMap.get("scheduleTime");
        String headlinesNames = (String) dataMap.get("headlinesNames");
        String description = (String) dataMap.get("description");
        try {
            sendSchedulerMail(headlineId, headlinesNames, toAddress, scheduleTime, description);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        } catch (IOException ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void sendSchedulerMail(String headlineId, String headlinesNames, String toAddress, String scheduleTime, String description) throws SQLException, IOException {

        String[] toAddresss = toAddress.split(",");
        DataSnapshotDAO dataSnapshotDAO = new DataSnapshotDAO();
        Clob clob = dataSnapshotDAO.getHeadlineData(headlineId);
        String headlinename = dataSnapshotDAO.getHeadlineName(headlineId);

        StringBuffer str = new StringBuffer();
        // str.append(description+"<br>");
        str.append("<h2>" + headlinesNames + "</h2><br><br>");
        String strng;
        BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

        while ((strng = bufferRead.readLine()) != null) {
            str.append(strng);
        }

        for (int i = 0; i < toAddresss.length; i++) {

            params = new PbMailParams();
            params.setBodyText(str.toString());
            params.setToAddr(toAddresss[i]);
            params.setSubject(headlinesNames);
            params.setHasAttach(false);

            try {
                mailer = new PbMail(params);
                mailer.sendMail();

            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        }
    }

//        public void sendSchedulerMail(ReportSchedule schedule)
//        {
//            String subscribers = schedule.getSubscribers();
//            String reportId = String.valueOf(schedule.getReportId());
//            String contentType = schedule.getContenType();
//            String userId = schedule.getUserId();
//            String[] reportIds = new String[1];
//            reportIds[0] = reportId;
//            DataSnapshotBD snapshotBD = new DataSnapshotBD();
//            ArrayList<String> fileNames = snapshotBD.downloadDataSnapShotHtml(reportIds, userId, true);
//            String dir = System.getProperty("java.io.tmpdir") + "/";
//
//            if (fileNames != null && !fileNames.isEmpty()){
//
//                String subject="Report Scheduler ";
//                String toAddress=subscribers;
//
//                params=new PbMailParams();
//                params.setBodyText("");
// }               params.setToAddr(toAddress);
//                params.setSubject(schedule.getSchedulerName());
//                params.setHasAttach(true);
//
//                if (fileNames.size() == 1){
//                    params.setAttachFile(dir+fileNames.get(0));
//                }
//                else{
//                    try {
//                        String zipFileName = ServletUtilities.makeAndStoreZip(fileNames);
//                        zipFileName = dir + zipFileName;
//                        params.setAttachFile(zipFileName);
//                    } catch (IOException ex) {
//                        logger.error("Exception: ",ex);
//                    }
//                }
//
//                try{
//                    mailer=new PbMail(params);
//                    mailer.sendMail();
//                }
//                catch(Exception e){
//                    logger.error("Exception: ",e);
//                }
//            }
//            this.deleteFiles(fileNames);
//        }
    private void deleteFiles(ArrayList<String> fileNames) {
        Iterator iter = fileNames.listIterator();
        while (iter.hasNext()) {
            String filename = (String) iter.next();
            File file = new File(
                    System.getProperty("java.io.tmpdir"), filename);
            if (file.exists()) {
                file.delete();
            }
        }
        return;
    }

    public static void main(String args[]) {
        SchedulerDAO dao = new SchedulerDAO();
        ReportSchedule schedule = dao.getReportScheduleDetails(String.valueOf(445), false);
        ReportSchedulerJob job = new ReportSchedulerJob();
        try {
            job.sendSchedulerMail(schedule);
        } catch (ParseException ex) {
            logger.error("Exception: ", ex);
        }
    }
}