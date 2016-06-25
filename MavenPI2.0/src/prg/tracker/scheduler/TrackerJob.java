package prg.tracker.scheduler;

import com.google.common.collect.ArrayListMultimap;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.DataSet;
import com.progen.report.query.DataSetHelper;
import com.progen.report.query.KPIDataSet;
import com.progen.report.query.KPIDataSetHelper;
import com.progen.scheduler.ScheduleLogger;
import com.progen.scheduler.UserDimensionMap;
import com.progen.scheduler.db.SchedulerDAO;
import com.progen.scheduler.tracker.Tracker;
import com.progen.scheduler.tracker.TrackerCondition;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import prg.db.PbReturnObject;
import prg.util.PbMail;
import prg.util.PbMailParams;
import utils.db.ProgenParam;

public class TrackerJob implements Job {

    PbMailParams params = null;
    PbMail mailer = null;
    public static Logger logger = Logger.getLogger(TrackerJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        Tracker tracker = (Tracker) dataMap.get("Tracker");
        sendTrackerHtml(tracker);

    }

    public void sendTrackerHtml(Tracker tracker) {
        try {
//            int trackerId = tracker.getTrackerId();
            String userId = tracker.getUserId();
            String viewById = tracker.getViewById();
            String measId = tracker.getMeasureId();
            String suppMeasureIds = tracker.getSupportingMsr();
            String[] suppMsrArr = null;
            if (suppMeasureIds != null && !suppMeasureIds.equalsIgnoreCase("")) {
                suppMsrArr = suppMeasureIds.split(",");
            }
            boolean isAutoIdentifier = tracker.isAutoIdentify();
            SchedulerDAO dao = new SchedulerDAO();
            String[] viewByIds = viewById.split(",");
            List<UserDimensionMap> userMap = dao.getUserDetails(viewByIds[0]);
            Map<String, String> dimEmailMap = new HashMap<String, String>();
            ArrayListMultimap<String, TrackerCondition> trackerCond = null;
            boolean kpiQueryNeeded = false;
            Map<String, String> paramValues = new HashMap<String, String>();
            for (UserDimensionMap eachUserDimension : userMap) {
                dimEmailMap.put(eachUserDimension.getDimensionValue(), eachUserDimension.getMailId());
            }

            String folderId = tracker.getFolderId();

            if (isAutoIdentifier) {
                StringBuilder vals = new StringBuilder();
                Set paramKeys = dimEmailMap.keySet();
                Iterator paramItr = paramKeys.iterator();
                while (paramItr.hasNext()) {
                    String paramValue = (String) paramItr.next();
                    vals.append(",").append(paramValue);
                }
                if (vals.length() > 0) {
                    vals.replace(0, 1, "");
                }
                paramValues.put(viewById, vals.toString());
            } else {
                trackerCond = tracker.getTrackerConditions();
                String values = getParamValues(trackerCond);
                if (values != null && !values.isEmpty()) {
                    paramValues.put(viewById, values);
                }
                kpiQueryNeeded = isKpiQueryNeeded(trackerCond);
            }
            ArrayList<String> rowViewBys = new ArrayList<String>();
            rowViewBys.add(viewById);
            ArrayList<String> colViewBys = new ArrayList<String>();
            ArrayList<String> measIds = new ArrayList<String>();
            measIds.add(measId);
            if (suppMsrArr != null && suppMsrArr.length != 0) {
                Collections.addAll(measIds, suppMsrArr);
            }

            ArrayList<String> timeDetails = buildTimeInfo();
            String measName = tracker.getMeasureName();
            String dimName = tracker.getDimensionName();
            String measureString = "";
            Date measureDate = new Date();
            DateFormat formatter;
            BigDecimal measureVal = BigDecimal.ONE;
            trackerCond = tracker.getTrackerConditions();                 //for targetBasis
            List<TrackerCondition> conditions = trackerCond.get("All");
            TrackerCondition trckerCondition = conditions.get(0);
            boolean testTrack = trckerCondition.isFromFlagTarget();     //for targetBasis
            String mailSubject = "Tracker Alert : " + tracker.getTrackerName();
            StringBuilder newbodyText = new StringBuilder();
            String tdStyle = "font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt;font-weight:bold;color:rgb(51, 102, 153);padding-left:12px;background-color:gainsboro;";
            newbodyText.append("<html><body>");
            newbodyText.append("<table border='1'  width='100%' style='border-collapse:collapse;'>");
            newbodyText.append("<thead><tr>");
            newbodyText.append("<th width='12%' style=\"").append(tdStyle).append("\">").append(dimName).append("</th>");
            if (testTrack) {
                newbodyText.append("<th width='12%' style=\"").append(tdStyle).append("\">").append("Target Value").append("</th>");     //for targetBasis
                newbodyText.append("<th width='12%' style=\"").append(tdStyle).append("\">").append("Deviation Percentage").append("</th>");   //for targetBasis
            }
            newbodyText.append("<th width='12%' style=\"").append(tdStyle).append("\">").append(tracker.getMeasureName()).append("</th>");
            String suppMeasNames = tracker.getSupportingMsrNames();
            if (!suppMeasNames.equalsIgnoreCase((""))) {
                String[] namesArr = suppMeasNames.split(",");
                for (String name : namesArr) {
                    newbodyText.append("<th width='12%' style=\"").append(tdStyle).append("\">").append(name).append("</th>");
                }
            }
            newbodyText.append("</tr></thead><tbody>");

            if (kpiQueryNeeded && !isAutoIdentifier) {
                KPIDataSet dataSet = KPIDataSetHelper.buildKPIDataSet(measIds, timeDetails, userId, folderId);
                PbReturnObject retObj = dataSet.getKPIData();

                if (retObj != null && retObj.getRowCount() > 0) {
                    Object obj = retObj.getFieldValue(0, "A_" + measId);
                    BigDecimal val = retObj.getFieldValueBigDecimal(0, "A_" + measId);
                    double measVal = val.doubleValue();
                    ArrayListMultimap<String, TrackerCondition> trackCondMap = tracker.getTrackerConditions();
                    Set trackeCondSet = trackCondMap.keySet();
                    Iterator trackItr = trackeCondSet.iterator();
                    while (trackItr.hasNext()) {
                        String trackViewByVal = (String) trackItr.next();
                        if (trackViewByVal.equalsIgnoreCase("All")) {
                            List<TrackerCondition> trackerCondition = trackCondMap.get(trackViewByVal);
                            for (TrackerCondition eachCond : trackerCondition) {
                                boolean trackTest = eachCond.isFromFlagTarget();      //for targetBasis
                                boolean isSuccessed = false;
                                if (trackTest) {
                                    isSuccessed = this.isConditionSatisfied(eachCond);
                                } else {
                                    isSuccessed = this.isConditionSatisfy(eachCond, measVal);
                                }
                                if (isSuccessed) {
                                    StringBuilder completeKpiBody = new StringBuilder();
                                    StringBuilder singleRow = new StringBuilder();
                                    singleRow.append("<tr>");
                                    singleRow.append("<td align='center'>").append("All").append("</td>");
                                    if (trackTest) {             //for targetBasis
                                        double targetValue = eachCond.getTargetValue();
                                        BigDecimal bd = new BigDecimal(String.valueOf(targetValue));

                                        singleRow.append("<td align='right'>").append(NumberFormatter.getModifidNumber(bd)).append("</td>");    //Target Basis
                                        singleRow.append("<td align='right'>").append(eachCond.getDeviationPerVal()).append("</td>");
                                    }

                                    for (int m = 0; m < measIds.size(); m++) {
                                        BigDecimal eachMsrVal = retObj.getFieldValueBigDecimal(0, "A_" + measIds.get(m));
                                        singleRow.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(eachMsrVal, "", -1)).append("</td>");
                                    }
                                    singleRow.append("</tr>");
                                    completeKpiBody.append(newbodyText);
                                    completeKpiBody.append(singleRow);
                                    completeKpiBody.append("</tbody></table></body></html>");
                                    this.sendTrackerMail(tracker.getMode(), completeKpiBody.toString(), eachCond.getMailIds(), mailSubject);
                                }
                            }
                        }
                    }

                }
            }

            if (!paramValues.isEmpty() || isAutoIdentifier) {
                Map<String, String> mapCond = null;
                if (!tracker.isSendAnyWay() && !tracker.getCondOperator().equalsIgnoreCase("") && !tracker.getCondValue().equalsIgnoreCase("")) {
                    mapCond = new HashMap<String, String>();
                    mapCond.put("A_" + measIds.get(0), tracker.getCondOperator() + " " + tracker.getCondValue());
                }

                DataSetHelper helper = new DataSetHelper.DataSetHelperBuilder().measIds(measIds).rowViewBys(rowViewBys).colViewBys(colViewBys).paramValues(paramValues).timeDetails(timeDetails).userId(userId).bizRole(new String[]{folderId}).measConditions(mapCond).build();
                DataSet dataSet = helper.getDataSet();
                PbReturnObject retObj = dataSet.getData();

                for (int i = 0; i < retObj.getRowCount(); i++) {
                    String viewByVal = retObj.getFieldValueString(i, 0);
                    Object retobj = retObj.getFieldValue(0, "A_" + measId);
//                       Object obj=retObj.getFieldValue(0, "A_"+measId);
                    BigDecimal val = retObj.getFieldValueBigDecimal(0, "A_" + measId);
                    double measVal = val.doubleValue();
                    StringBuilder row = new StringBuilder();
                    if (isAutoIdentifier) {
                        String autoMailId = dimEmailMap.get(viewByVal);

                        StringBuilder autoMailBody = new StringBuilder();
                        if (retobj != null) {
                            row.append("<tr>");
                            row.append("<td>").append(viewByVal).append("</td>");
                            for (String meas : measIds) {
                                Object measrObj = retObj.getFieldValue(0, "A_" + meas);
                                if (measrObj instanceof String) {
                                    measureString = measrObj.toString();
                                    row.append("<td align='right'>").append(measureString).append("</td>");
                                } else if (measrObj instanceof Date) {
                                    formatter = new SimpleDateFormat("MM/dd/yyyy");
                                    measureDate = formatter.parse(measrObj.toString());
                                    row.append("<td align='right'>").append(measureDate).append("</td>");
                                } else {
                                    measureVal = retObj.getFieldValueBigDecimal(i, "A_" + meas);;
                                    row.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(measureVal, "", -1)).append("</td>");
                                }

                            }
                            row.append("</tr>");
                        }
                        autoMailBody.append(newbodyText);
                        autoMailBody.append(row);
                        autoMailBody.append("</tbody></table></body></html>");
                        this.sendTrackerMail(tracker.getMode(), autoMailBody.toString(), autoMailId, mailSubject);
                    }
//                        else
//                        {
                    ArrayListMultimap<String, TrackerCondition> trackCondMap = tracker.getTrackerConditions();
                    Set trackeCondSet = trackCondMap.keySet();
                    Iterator trackItr = trackeCondSet.iterator();
                    while (trackItr.hasNext()) {
                        String trackViewByVal = (String) trackItr.next();
                        if (trackViewByVal.equalsIgnoreCase(viewByVal)) {
                            if (trackViewByVal.equalsIgnoreCase(viewByVal)) {
                                List<TrackerCondition> trackerCondition = trackCondMap.get(trackViewByVal);
                                for (TrackerCondition eachCond : trackerCondition) {
                                    boolean isSuccess = this.isConditionSatisfy(eachCond, measVal);
                                    if (isSuccess || eachCond.isSendAnywayCheck()) {
                                        List<Double> measureValues = new ArrayList();
                                        for (int j = 0; j < measIds.size(); j++) {
                                            Object obj = retObj.getFieldValue(i, "A_" + measIds.get(j));
                                            if (obj instanceof String) {
                                                measureString = obj.toString();
                                            } else if (obj instanceof Date) {
                                                formatter = new SimpleDateFormat("MM/dd/yyyy");
                                                measureDate = formatter.parse(obj.toString());
                                            } else {
                                                measureVal = retObj.getFieldValueBigDecimal(i, "A_" + measIds.get(j));
                                            }

                                            measureValues.add(measureVal.doubleValue());

                                        }
                                        this.getTrackerConditionRow(tracker, viewByVal, eachCond.getMailIds(), (ArrayList<Double>) measureValues, newbodyText.toString());
                                    }
                                }
                            }
                        }
                    }
//                        }
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        ScheduleLogger.addLogEntry(tracker.getTrackerId(), "Tracker", "Success");
    }

    public static ArrayList<String> buildTimeInfo() {
        ArrayList<String> timeDetails = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();
        timeDetails.add("Day");
        timeDetails.add("PRG_STD");
        timeDetails.add(pParam.getdateforpage().toString());
        timeDetails.add("Month");
        timeDetails.add("Last Period");
        return timeDetails;
    }

    private boolean isKpiQueryNeeded(ArrayListMultimap<String, TrackerCondition> trackCondMap) {
        boolean kpiNeeded = false;
        Set condSet = trackCondMap.keySet();
        Iterator condItr = condSet.iterator();
        while (condItr.hasNext()) {
            String paramValue = (String) condItr.next();
            if ("All".equalsIgnoreCase(paramValue)) {
                kpiNeeded = true;
            }
        }
        return kpiNeeded;
    }

//    private boolean getTableRow(ArrayListMultimap<String,TrackerCondition> trackCondMap,String viewByVal, String measName, double measVal)
//    {
//        StringBuilder row = new StringBuilder();
//        Set trackeCondSet=trackCondMap.keySet();
//        Iterator trackItr=trackeCondSet.iterator();
//        boolean success = false;
//        while(trackItr.hasNext())
//        {
//            String trackViewByVal=(String) trackItr.next();
//            if(trackViewByVal.equalsIgnoreCase(viewByVal))
//            {
//                 List<TrackerCondition> trackerCondition= trackCondMap.get(trackViewByVal);
//                for(TrackerCondition trackerCond:trackerCondition)
//                {
//                    String operator = trackerCond.getOperator();
//                    double trackerVal = trackerCond.getMeasureStartValue();
//
//                    if (">".equals(operator)){
//                        if (measVal > trackerVal)
//                            success = true;
//                    }
//                    else if (">=".equalsIgnoreCase(operator)){
//                        if (measVal >= trackerVal)
//                            success = true;
//                    }
//                    else if ("<".equalsIgnoreCase(operator)){
//                        if (measVal < trackerVal)
//                            success = true;
//                    }
//                    else if ("<=".equalsIgnoreCase(operator)){
//                        if (measVal <= trackerVal)
//                            success = true;
//                    }
//                    else if ("==".equalsIgnoreCase(operator)){
//                        if (measVal == trackerVal)
//                            success = true;
//                    }
//                    else if ("!=".equalsIgnoreCase(operator)){
//                        if (measVal != trackerVal)
//                            success = true;
//                    }
//
////                if (success){
////                    double deviation;
////                    double deviationPerc;
////                    deviation = measVal - trackerVal;
////                    deviationPerc = deviation/trackerVal * 100;
////
////                    row.append("<tr>");
////                    row.append("<td>").append(measName).append("</td>");
////                    row.append("<td>").append(viewByVal).append("</td>");
////                    row.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(measVal, "", -1)).append("</td>");
////                    row.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(trackerVal, "", -1)).append("</td>");
////                    row.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(deviation, "", -1)).append("</td>");
////                    row.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(deviationPerc, "", -1)).append("</td>");
////                    row.append("</tr>");
////                }
////                break;
//                }
//            }
//        }
//        return success;
//    }
    private String getParamValues(ArrayListMultimap<String, TrackerCondition> trackCondMap) {
        Set condSet = trackCondMap.keySet();
        Iterator condItr = condSet.iterator();
        StringBuilder vals = new StringBuilder();
        while (condItr.hasNext()) {
            String paramValue = (String) condItr.next();
            if (!("All".equalsIgnoreCase(paramValue))) {
                vals.append(",").append(paramValue);
            }
        }
        if (vals.length() > 0) {
            vals.replace(0, 1, "");
        }
        return vals.toString();
    }
//    public static void main(String args[]){
//        SchedulerDAO dao = new SchedulerDAO();
//        Tracker tracker = dao.getTracker(305,false);
//        TrackerJob job = new TrackerJob();
//        job.sendTrackerHtml(tracker,flag);
//    }

    private void sendTrackerMail(String trackerMode, String bodyText, String toAddress, String subject) {
        String mobiletxt = "";
//            
        if (trackerMode.equalsIgnoreCase("Email")) {
            params = new PbMailParams();
            params.setBodyText(bodyText.toString());
            params.setToAddr(toAddress);
            params.setSubject(subject);
            params.setHasAttach(true);
            mailer = new PbMail(params);
            boolean status;
            try {
                status = mailer.sendMail();
            } catch (AddressException ex) {
                logger.error("Exception: ", ex);
            } catch (MessagingException ex) {
                logger.error("Exception: ", ex);
            }
        } else {
            com.progen.sms.SendSms sms = new com.progen.sms.SendSms();
            sms.setUserName("progen");
            sms.setPassword("progen123");
            sms.setMessageText(mobiletxt);
            sms.setMobileNo(toAddress);
            String phnumber[] = toAddress.split(",");
            try {
                sms.smssending(mobiletxt, phnumber);
            } catch (MalformedURLException ex) {
                logger.error("Exception: ", ex);
            } catch (IOException ex) {
                logger.error("Exception: ", ex);
            }
        }
    }

    private String getTrackerConditionRow(Tracker tracker, String viewByVal, String toAddress, ArrayList<Double> measValList, String headerText) {
        StringBuilder trackerCondRow = new StringBuilder();
        StringBuilder rowString = new StringBuilder();
        StringBuilder completeBodyText = new StringBuilder();
        String subject = "Tracker Alert : " + tracker.getTrackerName();
        rowString.append("<tr>");
        rowString.append("<td>").append(viewByVal).append("</td>");
        for (Double measValue : measValList) {
            rowString.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(measValue, "", -1)).append("</td>");
        }
        rowString.append("</tr>");
        completeBodyText.append(headerText).append(rowString).append("</tbody></table></body></html>");
        this.sendTrackerMail(tracker.getMode(), completeBodyText.toString(), toAddress, subject);
        return trackerCondRow.toString();
    }

    private boolean isConditionSatisfy(TrackerCondition eachCond, double measVal) {
        boolean success = false;
        String operator = eachCond.getOperator();
        double trackerVal = eachCond.getMeasureStartValue();
        double trackEndVal = eachCond.getMeasureEndValue();
        if (">".equals(operator)) {
            if (measVal > trackerVal) {
                success = true;
            }
        } else if (">=".equalsIgnoreCase(operator)) {
            if (measVal >= trackerVal) {
                success = true;
            }
        } else if ("<".equalsIgnoreCase(operator)) {
            if (measVal < trackerVal) {
                success = true;
            }
        } else if ("<=".equalsIgnoreCase(operator)) {
            if (measVal <= trackerVal) {
                success = true;
            }
        } else if ("==".equalsIgnoreCase(operator)) {
            if (measVal == trackerVal) {
                success = true;
            }
        } else if ("!=".equalsIgnoreCase(operator)) {
            if (measVal != trackerVal) {
                success = true;
            }
        } else if ("<>".equalsIgnoreCase(operator)) {
            if (trackerVal < measVal && measVal < trackEndVal) {
                success = true;
            }
        }
        return success;
    }

    private boolean isConditionSatisfied(TrackerCondition eachCond) //for targetBasis
    {
        boolean successed = false;
        String operator = eachCond.getOperator();
        double trackerVal = eachCond.getMeasureStartValue();
        double trackEndVal = eachCond.getMeasureEndValue();
        double devPerVal = eachCond.getDeviationPerVal();
        if (">".equals(operator)) {
            if (devPerVal > trackerVal) {
                successed = true;
            }
        } else if (">=".equalsIgnoreCase(operator)) {
            if (devPerVal >= trackerVal) {
                successed = true;
            }
        } else if ("<".equalsIgnoreCase(operator)) {
            if (devPerVal < trackerVal) {
                successed = true;
            }
        } else if ("<=".equalsIgnoreCase(operator)) {
            if (devPerVal <= trackerVal) {
                successed = true;
            }
        } else if ("==".equalsIgnoreCase(operator)) {
            if (devPerVal == trackerVal) {
                successed = true;
            }
        } else if ("!=".equalsIgnoreCase(operator)) {
            if (devPerVal != trackerVal) {
                successed = true;
            }
        } else if ("<>".equalsIgnoreCase(operator)) {
            if (trackerVal < devPerVal && devPerVal < trackEndVal) {
                successed = true;
            }
        }
        return successed;
    }
}
