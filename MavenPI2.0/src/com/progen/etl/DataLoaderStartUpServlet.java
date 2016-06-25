package com.progen.etl;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import com.progen.connection.ConnectionMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utils.db.ProgenConnection;

public class DataLoaderStartUpServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        Properties connProps = new Properties();
        Properties timeProps = new Properties();
        InputStream servletStream = getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");

        InputStream timeStream = getServletContext().getResourceAsStream("/WEB-INF/EtlLoadTables.xml");
        ConnectionMetadata metadata = null;

        if (servletStream != null) {
            try {
                connProps.loadFromXML(servletStream);
                servletStream.close();
            } catch (IOException ex) {
            }
            metadata = new ConnectionMetadata(connProps);
            ProgenConnection.setConnectionMetadata(metadata);
        }

        if (timeStream != null) {
            try {
                timeProps.loadFromXML(timeStream);
                timeStream.close();
            } catch (IOException ex) {
            }
        }


        SchedulerFactory sf = new StdSchedulerFactory();

        String expression = this.buildExpression(timeProps);
        // expression="0 14 14 ? * 5 *";


        //String expression = "0 * * * * ?";//For Each Minute
        //String expression = "0 03 11 * * ? *";
        //0 45 23 * * ? * (Every day 11:45 pm)
        JobDetail job = new JobDetail("job1", "group1", com.progen.etl.DataLoaderTracker.class);
        job.getJobDataMap().put("context", getServletContext());
        Scheduler sched = null;
        CronTrigger ct;
        try {
            sched = sf.getScheduler();
            sched.start();
            CronExpression cex = new CronExpression(expression);
            ct = new CronTrigger("simple trigger", "simple group", expression);
            sched.scheduleJob(job, ct);
        } catch (SchedulerException ex) {
        } catch (ParseException ex) {
        }


    }

    private String buildExpression(Properties timeProps) {
        StringBuffer expression = new StringBuffer("");

        String hours = timeProps.getProperty("Hours");
        String minutes = timeProps.getProperty("Minutes");
        String seconds = timeProps.getProperty("Seconds");
        String type = timeProps.getProperty("type");
        if (type.equalsIgnoreCase("PM")) {
            if (hours.equalsIgnoreCase("12")) {
                hours = String.valueOf(12 + 0);
            } else {
                hours = String.valueOf(12 + Integer.parseInt(hours));
            }
        }
        String dayOfWeek[] = timeProps.getProperty("Day_Of_Week").split(",");
        String days = "";
        for (int i = 0; i < dayOfWeek.length; i++) {
            if (dayOfWeek[i].equalsIgnoreCase("Sunday")) {
                days = days + "1";
            } else if (dayOfWeek[i].equalsIgnoreCase("Monday")) {
                days = days + "2";
            } else if (dayOfWeek[i].equalsIgnoreCase("Tuesday")) {
                days = days + "3";
            } else if (dayOfWeek[i].equalsIgnoreCase("Wednesday")) {
                days = days + "4";
            } else if (dayOfWeek[i].equalsIgnoreCase("Thursday")) {
                days = days + "5";
            } else if (dayOfWeek[i].equalsIgnoreCase("Friday")) {
                days = days + "6";
            } else if (dayOfWeek[i].equalsIgnoreCase("SaturDay")) {
                days = days + "7";
            } else {
                days = days + "?";
            }

            // days=days+dayOfWeek[i];
            if (i != dayOfWeek.length - 1) {
                days = days + ",";
            }
        }
        expression.append(seconds + " " + minutes + " " + hours + " ? * " + days + " *");
        return expression.toString();
    }
}
