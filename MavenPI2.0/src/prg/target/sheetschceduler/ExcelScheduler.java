package prg.target.sheetschceduler;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class ExcelScheduler {

    private String targetId, userId, excelSheetName, FileName;
    private Scheduler shed;
    private SchedulerFactory sf;
    private JobDetail jd;
    public static Logger logger = Logger.getLogger(ExcelScheduler.class);

    public ExcelScheduler(String targetId, String userId, String excelSheetName, String FileName) {
        this.targetId = targetId;
        this.userId = userId;
        this.excelSheetName = excelSheetName;
        this.FileName = FileName;
    }

    public void readExcel(String targetId, String userId, String excelSheetName, String FileName) throws Exception {
        try {
            ////////////////////////////////////////////////////////////////////.println(" in read excel-----------------------------");
            //Correct code

            sf = new StdSchedulerFactory();
            shed = sf.getScheduler();

            shed.start();
            jd = new JobDetail("newtargetscheduler", "demotargetschedulergroupup", ExcelSheetJob.class);
            ////////////////////////////////////////////////////////////////////.println(jd+" jd "+jd.toString());
            jd.getJobDataMap().put("targetId", targetId);
            jd.getJobDataMap().put("userId", userId);
            jd.getJobDataMap().put("excelSheetName", excelSheetName);
            jd.getJobDataMap().put("FileName", FileName);
            SimpleTrigger st = new SimpleTrigger("targetscheduler", "targetschedulergroupup");
            shed.scheduleJob(jd, st);


        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }
}
