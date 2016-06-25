/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.scheduler;

import org.apache.log4j.Logger;
import prg.db.PbDb;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public class ScheduleLogger {

    public static Logger logger = Logger.getLogger(ScheduleLogger.class);

    public static void addLogEntry(int schedulerId, String schedulerType, String status) {
        PbDb db = new PbDb();
        String query = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "insert into PRG_SCHEDULER_LOGS(scheduler_id,scheduler_type,execution_date,status) values(&,'&',getDate(),'&')";
        } else {
            query = "insert into PRG_SCHEDULER_LOGS(LOG_ID, scheduler_id,scheduler_type,execution_date,status) values(PRG_SCHEDULER_LOGS_SEQ.nextval, &,'&',sysdate,'&')";
        }

        Object[] objArr = new Object[3];
        objArr[0] = schedulerId;
        objArr[1] = schedulerType;
        objArr[2] = status;

        String finalQuery = db.buildQuery(query, objArr);

        query = "";
        if ("Scorecard".equalsIgnoreCase(schedulerType)) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "update PRG_AR_SCORECARD_TRACKER set last_executed=getDate() where scorecard_schedule_id=" + schedulerId;
            } else {
                query = "update PRG_AR_SCORECARD_TRACKER set last_executed=sysdate where scorecard_schedule_id=" + schedulerId;
            }
        } else if ("Scheduler".equalsIgnoreCase(schedulerType)) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "update PRG_REPORT_SCHEDULER set last_executed=getDate() where report_schedule_id=" + schedulerId;
            } else {
                query = "update PRG_REPORT_SCHEDULER set last_executed=sysdate where report_schedule_id=" + schedulerId;
            }
        } else if ("Tracker".equalsIgnoreCase(schedulerType)) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                query = "update PRG_TRACKER_MASTER set last_executed=getDate() where tracker_id=" + schedulerId;
            } else {
                query = "update PRG_TRACKER_MASTER set last_executed=sysdate where tracker_id=" + schedulerId;
            }
        }

        try {
            db.execUpdateSQL(finalQuery);
            db.execUpdateSQL(query);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
}
