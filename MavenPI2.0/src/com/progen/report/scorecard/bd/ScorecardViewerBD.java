/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.bd;

import com.progen.report.display.DisplayParameters;
import com.progen.report.scorecard.ScoreCard;
import com.progen.report.scorecard.ScoreCardAction;
import com.progen.report.scorecard.ScoreCardConstants;
import com.progen.report.scorecard.db.ScorecardDAO;
import com.progen.report.scorecard.db.ScorecardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.util.PbMail;
import prg.util.PbMailParams;
import utils.db.ProgenParam;

/**
 *
 * @author progen
 */
public class ScorecardViewerBD extends PbDb {

    public static Logger logger = Logger.getLogger(ScorecardViewerBD.class);

    public String getScoreCardActionTypes() {
        StringBuilder output = new StringBuilder();
        try {
            String query = "select scard_action_type, scard_action_text from prg_ar_scorecard_action_types";
            PbReturnObject retObj = execSelectSQL(query);
            StringBuilder typeSB = new StringBuilder();
            StringBuilder dispValueSB = new StringBuilder();

            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    String type = retObj.getFieldValueString(i, 0);
                    String dispValue = retObj.getFieldValueString(i, 1);

                    typeSB.append(",\"").append(type).append("\"");
                    dispValueSB.append(",\"").append(dispValue).append("\"");
                }
            }

            if (typeSB.length() > 0) {
                output.append("{\"ActionType\":[").append(typeSB.substring(1)).append("],");
                output.append("\"DisplayLabel\":[").append(dispValueSB.substring(1)).append("]}");
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return output.toString();
    }

    public void sendMail(String scoreCardId, String memberId, String score, String toAddress, String subject, String mailContent, String startDate, String endDate, String elemName) {

        PbMailParams params = new PbMailParams();
        params.setBodyText(mailContent);
        params.setToAddr(toAddress);
        params.setSubject(subject);

        PbMail pbMail = new PbMail(params);

        try {
            boolean status = pbMail.sendMail();
            if (status) {
                StringBuilder actionXml = new StringBuilder();
                actionXml.append("<action>");
                actionXml.append("<strtDate>").append(startDate).append("</strtDate>");
                actionXml.append("<endDate>").append(endDate).append("</endDate>");
                actionXml.append("<to>").append(toAddress).append("</to>");
                actionXml.append("<subject>").append(subject).append("</subject>");
                actionXml.append("<mailContent>").append(mailContent).append("</mailContent>");
                actionXml.append("</action>");

                ScorecardTemplateDAO dao = new ScorecardTemplateDAO();
                dao.insertScardActionDetails(scoreCardId, memberId, score, ScoreCardConstants.ACTION_TYPE_EMAIL, actionXml.toString(), startDate, endDate, elemName);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void saveNote(String scoreCardId, String memberId, String score, String noteText, String startDate, String endDate, String elemName) {
        StringBuilder actionXml = new StringBuilder();

        actionXml.append("<action>");
        actionXml.append("<note>").append(noteText).append("</note>");
        actionXml.append("</action>");

        ScorecardTemplateDAO dao = new ScorecardTemplateDAO();
        dao.insertScardActionDetails(scoreCardId, memberId, score, ScoreCardConstants.ACTION_TYPE_NOTE, actionXml.toString(), startDate, endDate, elemName);

    }

    public String getScoreCardActions(String userId, String scoreCardId, String memberId) {
        StringBuilder output = new StringBuilder();
        ScorecardTemplateDAO dao = new ScorecardTemplateDAO();
        List<ScoreCardAction> actionList = dao.getScoreCardActions(scoreCardId, memberId);
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

        StringBuilder scardActionsTableHdr = new StringBuilder();
        StringBuilder scardMemActionsTableHdr = new StringBuilder();
        StringBuilder actionsTableFtr = new StringBuilder();

        scardActionsTableHdr.append("<table border=\"1\" width=\"100%\" class=\"tablesorter\" style=\"border-collapse:collapse;border-left-style: hidden;border-right-style: hidden;\"><thead><tr>").append("<th>Scorecard</th>").append("<th>Action Start Date</th>").append("<th>Action End Date</th>").append("<th>Action Period Score</th>").append("<th>Action</th>").append("</tr></thead>").append("<tbody>");

        scardMemActionsTableHdr.append("<table border=\"1\" width=\"100%\" class=\"tablesorter\" style=\"border-collapse:collapse;border-left-style: hidden;border-right-style: hidden;\">").append("<thead><tr>").append("<th>Measure</th>").append("<th>Action Start Date</th>").append("<th>Action End Date</th>").append("<th>Action Period Score</th>").append("<th>Action</th>").append("</tr></thead>").append("<tbody>");

        actionsTableFtr.append("</tbody></table>");

        StringBuilder scardActions = new StringBuilder();
        StringBuilder scardMemberActions = new StringBuilder();

        if (!actionList.isEmpty()) {
            for (ScoreCardAction action : actionList) {
                Date startDate = action.getStartDate();
                Date endDate = action.getEndDate();
                String impact = action.getImpact();
                int scardId = action.getScorecardId();
                int scardMemberId = action.getMemberId();
                String elementName = action.getActionItemName();
                String startDateStr = df.format(startDate);
                String endDateStr = df.format(endDate);


                if (impact == null || "".equalsIgnoreCase(impact)) {
                    impact = buildImpact(userId, scardId, scardMemberId, startDate, endDate);

                    if (impact != null) {
                        dao.updateImpact(action.getScardActionDetailId(), impact);
                    }
                }

                StringBuilder temp = new StringBuilder();
                temp.append("<tr>");
                temp.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\">").append(elementName).append("</td>");
                temp.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\">").append(startDateStr).append("</td>");
                temp.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\">").append(endDateStr).append("</td>");
                temp.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\">").append(impact).append("</td>");
                temp.append("<td style=\"border-left-style: hidden;border-right-style: hidden;\">").append(generateHtml(action.getActionType(), action.getActionDetail())).append("</td>");
                temp.append("</tr>");

                if (scardMemberId > 0) {
                    scardMemberActions.append(temp);
                } else {
                    scardActions.append(temp);
                }
            }

            if (scardActions.length() > 0) {
                output.append("<h5>Scorecard Actions</h5>");
                output.append(scardActionsTableHdr);
                output.append(scardActions);
                output.append(actionsTableFtr);
                output.append("<br><br>");
            }

            if (scardMemberActions.length() > 0) {
                output.append("<h5>Measure Actions</h5>");
                output.append(scardMemActionsTableHdr);
                output.append(scardMemberActions);
                output.append(actionsTableFtr);
            }
        }
        return output.toString();
    }

    private String generateHtml(String actionType, String actionDetails) {
        StringBuilder html = new StringBuilder();

        Document cellDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            cellDocument = builder.build(new ByteArrayInputStream(actionDetails.toString().getBytes()));
            root = cellDocument.getRootElement();

            if (root != null) {
                if (ScoreCardConstants.ACTION_TYPE_EMAIL.equalsIgnoreCase(actionType)) {
                    String to = "";
                    String subject = "";
                    String content = "";
                    List toList = root.getChildren("to");
                    if (toList != null && toList.size() > 0) {
                        Element toElem = (Element) toList.get(0);
                        to = toElem.getText();
                    }

                    List subjList = root.getChildren("subject");
                    if (subjList != null && subjList.size() > 0) {
                        Element subjElem = (Element) subjList.get(0);
                        subject = subjElem.getText();
                    }

                    List contentList = root.getChildren("mailContent");
                    if (contentList != null && contentList.size() > 0) {
                        Element contentElem = (Element) contentList.get(0);
                        content = contentElem.getText();
                    }

                    html.append("<strong>To :</strong>").append(to).append("<br>");
                    html.append("<strong>Subject :</strong>").append(subject).append("<br>");
                    html.append("<strong>Body </strong><br>");
                    html.append(content);
                } else if (ScoreCardConstants.ACTION_TYPE_NOTE.equalsIgnoreCase(actionType)) {
                    String content = "";
                    List contentList = root.getChildren("mailContent");
                    if (contentList != null && contentList.size() > 0) {
                        Element contentElem = (Element) contentList.get(0);
                        content = contentElem.getText();
                    }
                    html.append("<p>");
                    html.append("<br>");
                    html.append(content);
                    html.append("</p>");
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return html.toString();
    }

    public String loadScorecard(String scardId, String userId, String dateVal, String cmpVal, String timeLevel) {
        StringBuilder output = new StringBuilder();
        ScorecardDAO dao = new ScorecardDAO();
        List<String> idList = new ArrayList<String>();
        idList.add(scardId);
        List<ScoreCard> scardList = dao.getScoreCards(idList);
        if (!scardList.isEmpty()) {
            ScoreCard scard = scardList.get(0);
            String folderId = scard.getFolderId();
            ScorecardBuilder builder = new ScorecardBuilder();
            ArrayList<String> timeDetails = buildTimeInfo(dateVal, cmpVal, timeLevel);
            output.append(builder.buildScoreCard(scard, userId, folderId, timeDetails));
        }

        return output.toString();
    }

    public static ArrayList<String> buildTimeInfo(String dateVal, String cmpVal, String timeLevel) {
        ArrayList<String> timeDetails = new ArrayList<String>();
        timeDetails.add("Day");
        timeDetails.add("PRG_STD");
        timeDetails.add(dateVal);
        timeDetails.add(timeLevel);
        timeDetails.add(cmpVal);
        return timeDetails;
    }

    private String buildImpact(String userId, int scardId, int memberId, Date startDate, Date endDate) {
        ScorecardBuilder builder = new ScorecardBuilder();
        String impact = builder.buildImpactString(userId, scardId, memberId, startDate, endDate);
        return impact;
    }

    public HashMap loadParams(String scardId, String userId) {
        StringBuilder output = new StringBuilder();
        ScorecardDAO scardDAO = new ScorecardDAO();
        String minTimeLevel = null;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        ArrayList dateArray = new ArrayList();
        ArrayList weekArray = new ArrayList();
        ArrayList asofWeekArray = new ArrayList();
        ArrayList compareArray = new ArrayList();
        ArrayList monthArray = new ArrayList();
        ArrayList periodTypeArray = new ArrayList();
        ArrayList yearArray = new ArrayList();
        ArrayList asofYrArray = new ArrayList();
        ArrayList qtrArray = new ArrayList();
        ArrayList asofQtrArray = new ArrayList();

        ArrayList timeDetails = new ArrayList();
        HashMap timeDimMap = new HashMap();
        ProgenParam pParam = new ProgenParam();

        try {
            String folderId = scardDAO.getFolderIDForScoreCard(scardId);
            String dimId = "Time-Period Basis";
            String timeParam = "AS_OF_DATE";
            String timeParamArr[] = new String[3];
            timeParamArr[0] = "AS_OF_DATE";
            timeParamArr[1] = "PRG_PERIOD_TYPE";
            timeParamArr[2] = "PRG_COMPARE";

            if (folderId != null) {
                minTimeLevel = reportTemplateDAO.getUserFolderMinTimeLevel(folderId);
                for (int i = 0; i < timeParamArr.length; i++) {
                    timeParam = timeParamArr[i];
                    if (minTimeLevel.equals("5")) {
                        if (dimId.equalsIgnoreCase("Time-Period Basis")) {
                            if (timeParam.equalsIgnoreCase("AS_OF_DATE")) {
                                //                                    dateArray.add(pParam.getdateforpage().toString());//added on 28-11-09
                                dateArray.add(null); //Default Date is Null at first
                                dateArray.add("CBO_AS_OF_DATE");
                                dateArray.add("DATE");
                                dateArray.add("1");
                                dateArray.add("1");
                                dateArray.add(null);
                                dateArray.add("AS_OF_DATE");
                                timeDimMap.put("AS_OF_DATE", dateArray);
                            } else if (timeParam.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                                periodTypeArray.add("AGGREGATION");
                                periodTypeArray.add("2");
                                periodTypeArray.add("2");
                                periodTypeArray.add("MONTH");
                                periodTypeArray.add("PRG_PERIOD_TYPE");
                                timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                            } else if (timeParam.equalsIgnoreCase("PRG_COMPARE")) {
                                compareArray.add("LAST PERIOD");
                                compareArray.add("CBO_PRG_COMPARE");
                                compareArray.add("COMPARE");
                                compareArray.add("3");
                                compareArray.add("1001");
                                compareArray.add("LAST PERIOD");
                                compareArray.add("PRG_COMPARE");
                                timeDimMap.put("PRG_COMPARE", compareArray);
                            }
                            timeDetails.add("Day");
                            timeDetails.add("PRG_STD");
                            //timeDetails.add(sdf.format(date));
                            //timeDetails.add("05/31/2008");
                            timeDetails.add(pParam.getdateforpage().toString());//added on 28-11-09
                            timeDetails.add("Month");
                            timeDetails.add("Last Period");
                        }
                    } else if (minTimeLevel.equals("4")) {
                        timeParam = "AS_OF_WEEK";
                        if (timeParam.equalsIgnoreCase("AS_OF_WEEK")) {
                            weekArray.add(null);
                            weekArray.add("CBO_AS_OF_WEEK");
                            weekArray.add("WEEK");
                            weekArray.add("1");
                            weekArray.add("1");
                            weekArray.add(null);
                            weekArray.add("AS_OF_WEEK");
                            timeDimMap.put("AS_OF_WEEK", weekArray);
                        } else if (timeParam.equalsIgnoreCase("AS_OF_WEEK1")) {
                            asofWeekArray.add(null);
                            asofWeekArray.add("CBO_AS_OF_WEEK1");
                            asofWeekArray.add("COMPAREWEEK");
                            asofWeekArray.add("2");
                            asofWeekArray.add("2");
                            asofWeekArray.add(null);
                            asofWeekArray.add("AS_OF_WEEK1");
                            timeDimMap.put("AS_OF_WEEK1", asofWeekArray);
                        }
                        timeDetails.add("WEEK");
                        timeDetails.add("PRG_WEEK_CMP");
                        timeDetails.add(null);
                        timeDetails.add(null);
                    } else if (minTimeLevel.equals("3")) {
                        timeParam = "AS_OF_MONTH";
                        if (timeParam.equalsIgnoreCase("AS_OF_MONTH")) {
                            monthArray.add(null);
                            monthArray.add("CBO_AS_OF_MONTH");
                            monthArray.add("MONTH");
                            monthArray.add("1");
                            monthArray.add("1");
                            monthArray.add(null);
                            monthArray.add("AS_OF_MONTH");
                            timeDimMap.put("AS_OF_MONTH", monthArray);
                        } else if (timeParam.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                            periodTypeArray.add("MONTH");
                            periodTypeArray.add("CBO_PRG_PERIOD_TYPE");
                            periodTypeArray.add("AGGREGATION");
                            periodTypeArray.add("2");
                            periodTypeArray.add("2");
                            periodTypeArray.add("MONTH");
                            periodTypeArray.add("PRG_PERIOD_TYPE");
                            timeDimMap.put("PRG_PERIOD_TYPE", periodTypeArray);
                        } else if (timeParam.equalsIgnoreCase("PRG_COMPARE")) {
                            compareArray.add("LAST PERIOD");
                            compareArray.add("CBO_PRG_COMPARE");
                            compareArray.add("COMPARE");
                            compareArray.add("3");
                            compareArray.add("1001");
                            compareArray.add("LAST PERIOD");
                            compareArray.add("PRG_COMPARE");
                            timeDimMap.put("PRG_COMPARE", compareArray);
                        }
                        timeDetails.add("Month");
                        timeDetails.add("PRG_STD");
                        timeDetails.add(null);
                        timeDetails.add("Month");
                        timeDetails.add("Last Period");
                    } else if (minTimeLevel.equals("2")) {
                        timeParam = "AS_OF_QUARTER";
                        if (timeParam.equalsIgnoreCase("AS_OF_QUARTER")) {
                            qtrArray.add(null);
                            qtrArray.add("CBO_AS_OF_QUARTER");
                            qtrArray.add("QUARTER");
                            qtrArray.add("1");
                            qtrArray.add("1");
                            qtrArray.add(null);
                            qtrArray.add("AS_OF_QUARTER");
                            timeDimMap.put("AS_OF_QUARTER", qtrArray);
                        } else if (timeParam.equalsIgnoreCase("AS_OF_QUARTER1")) {
                            asofQtrArray.add(null);
                            asofQtrArray.add("CBO_AS_OF_QUARTER1");
                            asofQtrArray.add("COMPAREQUARTER");
                            asofQtrArray.add("2");
                            asofQtrArray.add("2");
                            asofQtrArray.add(null);
                            asofQtrArray.add("AS_OF_QUARTER1");
                            timeDimMap.put("AS_OF_QUARTER1", asofQtrArray);
                        }
                        timeDetails.add("QUARTER");
                        timeDetails.add("PRG_QUARTER_CMP");
                        timeDetails.add(null);
                        timeDetails.add(null);

                    } else if (minTimeLevel.equals("1")) {
                        timeParam = "AS_OF_YEAR";
                        if (timeParam.equalsIgnoreCase("AS_OF_YEAR")) {
                            yearArray.add(String.valueOf(pParam.getYearforpage()));
                            yearArray.add("CBO_AS_OF_YEAR");
                            yearArray.add("Year");
                            yearArray.add("1");
                            yearArray.add("1");
                            yearArray.add(null);
                            yearArray.add("AS_OF_YEAR");
                            timeDimMap.put("AS_OF_YEAR", yearArray);
                        } else if (timeParam.equalsIgnoreCase("AS_OF_YEAR1")) {
                            asofYrArray.add(String.valueOf(pParam.getYearforpage("365")));
                            asofYrArray.add("CBO_AS_OF_YEAR1");
                            asofYrArray.add("Compare Year");
                            asofYrArray.add("2");
                            asofYrArray.add("2");
                            asofYrArray.add(null);
                            asofYrArray.add("AS_OF_YEAR1");
                            timeDimMap.put("AS_OF_YEAR1", asofYrArray);
                        }
                        timeDetails.add("YEAR");
                        timeDetails.add("PRG_YEAR_CMP");
                        timeDetails.add(null);
                        timeDetails.add(null);
                    }
                }
            }

            DisplayParameters dispParams = new DisplayParameters();
//            String timeHtml = dispParams.displayTimeParams(timeDimMap);
//            output.append(timeHtml);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        return timeDimMap;
    }

    public HashMap<String, String> getDimensions(String[] folderIds) {
        HashMap<String, String> dimensionDataMap = new HashMap<String, String>();
        ScorecardDAO scorecardDAO = new ScorecardDAO();
        dimensionDataMap = scorecardDAO.getDimensions(folderIds);
        return dimensionDataMap;
    }

    public String displayTimeParamsForScorecard(HashMap timeDimMap) {
        DisplayParameters dispParams = new DisplayParameters();
        String timeHtml = "";
        try {
            timeHtml = dispParams.displayTimeParams(timeDimMap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return timeHtml;
    }

    public String dispayTimeParamsForScorecardTracker(HashMap timeDimMap) {
        DisplayParameters dispParams = new DisplayParameters();
        String timeHtml = "";
        timeHtml = dispParams.displayTimeParamsForScorecardTracker(timeDimMap);

        return timeHtml;
    }
}
