/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.tracker;

import com.progen.report.display.util.NumberFormatter;
import com.progen.report.scorecard.Score;
import com.progen.report.scorecard.ScoreCard;
import com.progen.report.scorecard.bd.ScorecardBuilder;
import com.progen.report.scorecard.bd.ScorecardViewerBD;
import com.progen.report.scorecard.db.ScorecardDAO;
import com.progen.report.scorecard.query.ScorecardQueryBuilder;
import com.progen.report.scorecard.query.ScorecardQueryDetails;
import com.progen.report.scorecard.query.ScorecardQueryResult;
import com.progen.scheduler.ScheduleLogger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.util.PbMail;
import prg.util.PbMailParams;

/**
 *
 * @author progen
 */
public class ScorecardTrackerJob implements Job {

    public static Logger logger = Logger.getLogger(ScorecardTrackerJob.class);

    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        ScorecardTracker tracker = (ScorecardTracker) dataMap.get("ScorecardTracker");
        sendScorecardDetails(tracker);
    }

    public void sendScorecardDetails(ScorecardTracker tracker) {
        int scorecardId = tracker.getsCardId();
        int userId = tracker.getCreatedBy();
        Calendar asOfDateCalendar = Calendar.getInstance();
        asOfDateCalendar.setTime(tracker.getAsOfDate());
        String dateValue = (asOfDateCalendar.get(Calendar.MONTH) + 1) + "/" + asOfDateCalendar.get(Calendar.DATE) + "/" + asOfDateCalendar.get(Calendar.YEAR);
        String compareValue = tracker.getCompareWith();
        String duration = tracker.getPeriod();
        List<String> idList = new ArrayList<String>();
        idList.add(String.valueOf(tracker.getsCardId()));
        ScorecardDAO dao = new ScorecardDAO();
        List<ScoreCard> scardList = dao.getScoreCards(idList);
        ScoreCard scard = null;
        if (!scardList.isEmpty()) {
            scard = scardList.get(0);
            String folderId = scard.getFolderId();
            ScorecardBuilder builder = new ScorecardBuilder();
            ArrayList<String> timeDetails = ScorecardViewerBD.buildTimeInfo(dateValue, compareValue, duration);
            Iterable<ScorecardQueryDetails> queryDetails = scard.getQueryDetails();
            ScorecardQueryBuilder queryBuilder = new ScorecardQueryBuilder();
            Iterable<ScorecardQueryResult> queryResult = queryBuilder.buildScorecardResultset(queryDetails, String.valueOf(userId), folderId, timeDetails);

            // Build the score object
            builder.buildScoreObject(scard, queryResult);

        }
        sendMail(scard, tracker);
    }

    public String buildHtml(ScoreCard scoreCard) {
        Score score = scoreCard.getScore();
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("<table border='1'>");
        builder.append("<thead><tr><th style='background-color:#B4D9EE;color:#3A457C'>Scorecard Name</th><th style='background-color:#B4D9EE;color:#3A457C'>Prior Score</th><th style='background-color:#B4D9EE;color:#3A457C'>Current Score</th></tr></thead>");
        builder.append("<tbody>");
        builder.append("<tr><td>").append(scoreCard.getScoreCardName()).append("</td>");
        builder.append("<td>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getPriorScore()), "", -1)).append("</td>");
        builder.append("<td>").append(NumberFormatter.getModifiedNumber(new BigDecimal(score.getCurrentScore()), "", -1)).append("</td></tr>");
        builder.append("</tbody>");
        builder.append("</html>");
        return builder.toString();
    }

    public void sendMail(ScoreCard scoreCard, ScorecardTracker tracker) {
        String subject = "Scorecard Tracker Alert : " + tracker.getTrackerName();
        String htmlBody = buildHtml(scoreCard);
        List<ScorecardTrackerRule> ruleLst = tracker.getRuleLst();
        Score score = scoreCard.getScore();
        Double scoreValue = score.getCurrentScore();
        PbMailParams params = null;
        PbMail mailer = null;
        for (ScorecardTrackerRule rule : ruleLst) {
            if (isRuleApplicable(scoreValue, rule)) {
                params = new PbMailParams();
                params.setBodyText(htmlBody);
                params.setToAddr(rule.getEmail());
                params.setSubject(subject);
                params.setHasAttach(true);
                mailer = new PbMail(params);
                try {
                    boolean status = mailer.sendMail();
                    ScheduleLogger.addLogEntry(tracker.getsCardShedId(), "Scorecard", "Success");
                } catch (AddressException ex) {
                    logger.error("Exception:", ex);
                    ScheduleLogger.addLogEntry(tracker.getsCardShedId(), "Scorecard", "Failed");
                } catch (MessagingException ex) {
                    logger.error("Exception:", ex);
                    ScheduleLogger.addLogEntry(tracker.getsCardShedId(), "Scorecard", "Failed");
                }
            }
        }



    }

    private boolean isRuleApplicable(Double score, ScorecardTrackerRule rule) {
        if (rule.getOperator().equalsIgnoreCase("<")) {
            return score < rule.getStartValue();
        }
        if (rule.getOperator().equalsIgnoreCase("<=")) {
            return score <= rule.getStartValue();
        }
        if (rule.getOperator().equalsIgnoreCase(">")) {
            return score > rule.getStartValue();
        }
        if (rule.getOperator().equalsIgnoreCase(">=")) {
            return score >= rule.getStartValue();
        }
        if (rule.getOperator().equalsIgnoreCase("<>")) {
            return (score > rule.getStartValue() && score < rule.getEndValue());
        }
        if (rule.getOperator().equalsIgnoreCase("=")) {
            return score == rule.getStartValue();
        }
        if (rule.getOperator().equalsIgnoreCase("!=")) {
            return score != rule.getStartValue();
        }

        return false;
    }
}
