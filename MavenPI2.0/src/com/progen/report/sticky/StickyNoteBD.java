/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.sticky;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class StickyNoteBD {

    public String saveStickyNote(String topp, String leftp, String widthp, String heightp, String snote, String mode, String noteLabel,
            String parameters, String TimeLevel, String parameterXml, String PbReportId, String userId, String noteID, String backGDColor) {
//        
        String dateQuery = "";
        String nxtsqlqry = "";
        String currDate = "";
        String uniqueID = "";
        PbReturnObject pbroDate = new PbReturnObject();
        PbReturnObject pbroNext = new PbReturnObject();
        PbDb pbDb = new PbDb();
        String query = "";
        try {
            if (mode.equalsIgnoreCase("save")) {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    dateQuery = "select getdate()";
                    nxtsqlqry = " SELECT IDENT_CURRENT('PRG_USER_STICKYNOTE') ";
                } else {
                    dateQuery = "select sysdate from dual";
                    nxtsqlqry = "SELECT PRG_USER_STICKYNOTE_SEQ.nextval NEXTID FROM dual";
                }
                pbroDate = pbDb.execSelectSQL(dateQuery);
                pbroNext = pbDb.execSelectSQL(nxtsqlqry);
                currDate = pbroDate.getFieldValueString(0, 0);
                uniqueID = pbroNext.getFieldValueString(0, 0);
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                    
                    query = "INSERT INTO PRG_USER_STICKYNOTE VALUES ('" + userId + "',"
                            + "'" + PbReportId + "',"
                            + leftp + ","
                            + topp + ","
                            + widthp + ","
                            + heightp + ","
                            + "'Y',"
                            + "'" + noteLabel + "',"
                            + "'" + snote.replace("|__|", "&").replace("|_|", "+") + "',"
                            + "'" + parameters + "',"
                            + "'" + currDate + "',"
                            + "'" + TimeLevel + "',"
                            + "'" + parameterXml + "',"
                            + "'" + backGDColor + "','','')";
                    // //.println("query in sql server-----:::" + query);
                } else {

                    query = "INSERT INTO PRG_USER_STICKYNOTE VALUES ('" + userId + "',"
                            + "'" + PbReportId + "',"
                            + uniqueID + ","
                            + leftp + ","
                            + topp + ","
                            + widthp + ","
                            + heightp + ","
                            + "'Y',"
                            + "'" + noteLabel + "',"
                            + "'" + snote.replace("|__|", "&").replace("|_|", "+") + "',"
                            + "'" + parameters + "',"
                            + "sysdate,"
                            + "'" + TimeLevel + "',"
                            + "'" + parameterXml + "',"
                            + "'" + backGDColor + "','','')";


                }
                pbDb.execUpdateSQL(query);
                return uniqueID;
            } else if (mode.equalsIgnoreCase("update")) {
                String sysDate = "";
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    sysDate = "getdate()";
                } else {
                    sysDate = "sysdate";
                }
                query = "UPDATE PRG_USER_STICKYNOTE SET "
                        + " S_NOTE='" + snote.replace("|__|", "&").replace("|_|", "+") + "'"
                        + " ,POS_LEFT=" + leftp
                        + " ,POS_TOP=" + topp
                        + " ,POS_WIDTH=" + widthp
                        + " ,POS_HEIGHT=" + heightp
                        + " ,COLOR_CODE='" + backGDColor + "'"
                        + " ,CREATED_ON=" + sysDate
                        + " WHERE USER_ID='" + userId + "'"
                        + " AND REPORT_ID='" + PbReportId + "'"
                        + " AND PARAMETER_ID='" + parameters + "'"
                        + " AND TIME_LEVEL='" + TimeLevel + "'"
                        + " AND STICKY_NOTES_ID='" + noteID + "'";

            }

            pbDb.execUpdateSQL(query);
            return null;
        } catch (Exception e) {
        }

        return null;
    }

    public StringBuffer buildAllStickyNotes(int stickyNoteCount, ArrayList listcount, PbReturnObject stickyRetObj, HashMap stickListHashMap, String contextPath) throws ParseException {
        // 
//        
        int divLeft = 0;
        int divTop = 0;
        String styleStr = null;
        String divId = null;
        String sNote = null;
        String stickId = null;
        String closeFn = null;
        String tAreaSave = null;
        String tAreaBlur = null;
        String moveFun = null;
        String tAreaFoc = null;
        String createdDate = null;
        String hideStickySess = null;
        String backGDColor = "d7faff";
        StringBuffer stickyHTMLBuffer = new StringBuffer();
        for (int i = 0; i < stickyNoteCount; i++) {
            if (listcount.contains(i)) {
                divLeft = stickyRetObj.getFieldValueInt(i, "POS_LEFT");
                divTop = stickyRetObj.getFieldValueInt(i, "POS_TOP");
                stickId = stickyRetObj.getFieldValueString(i, "STICKY_NOTES_ID");
                sNote = stickyRetObj.getFieldValueString(i, "S_NOTE");
                createdDate = stickyRetObj.getFieldValueString(i, "CREATED_ON");
                sNote = sNote.trim();
                sNote = sNote.replace("<br>", "\n");
                styleStr = "left:" + divLeft + "px;top:" + divTop + "px;";
//                divId = "NOTE_" + (i + 1);
                closeFn = "deleteSticky('" + stickId + "');return false;";
                tAreaSave = "javascript:saveStickyNote('txtArea" + stickId + "','" + stickId + "','update')";
//                tAreaBlur = "javascript:updateNote(this,'" + stickId + "','')";
                tAreaFoc = "javascript:tAreaFocus(this)";
                moveFun = "javascript:funUp('" + stickId + "')";
                hideStickySess = "javascript:hideStickText('" + stickId + "','" + stickyRetObj.getFieldValueString(i, "STICKY_LABEL") + "')";
                String styleType = "";
//                
                if (stickyRetObj.getFieldValueString(i, "COLOR_CODE") != null || stickyRetObj.getFieldValueString(i, "COLOR_CODE") != "") {
                    backGDColor = stickyRetObj.getFieldValueString(i, "COLOR_CODE");
                }
                if (stickListHashMap != null) {
                    if (stickListHashMap.get(stickId) != null) {
                        styleType = (String) stickListHashMap.get(stickId);
                    }
                }
                String remainderType = stickyRetObj.getFieldValueString(i, "REMAINDER_TYPE");
                String remainderValue = stickyRetObj.getFieldValueString(i, "REMAINDER_VALUE");
                String remainder = "";
                String alarmImg = "";
//                
                if (remainderType != null && !remainderType.equalsIgnoreCase("")) {
                    remainder = this.getRemainder(remainderType, remainderValue);
                    alarmImg = contextPath + "/images/alarm.png";
                } else {
                    remainder = styleType;
                    alarmImg = contextPath + "/images/bell.png";
                }


//                

//                
                stickyHTMLBuffer.append("<DIV id=" + stickId + " name='name" + stickId + "' style='display:" + remainder + ";z-index:1000;position:absolute;width:150px;" + styleStr + "'>");
                stickyHTMLBuffer.append("<Table border='0' width='100px' class='mycls' style='background-color:" + backGDColor + "' cellspacing='0' cellpadding='0' style='height:auto'>");
                stickyHTMLBuffer.append("<Tr  onmousemove='divMove(" + stickId + ")' class='navtitle1'>");
//                stickyHTMLBuffer.append("<Td id='titleBar' onMouseUp=" + moveFun + " style='cursor:move' width='70%'>");
                stickyHTMLBuffer.append("<Td id='titleBar'  style='cursor:move' width='60%'>");
                stickyHTMLBuffer.append("<ilayer width='100%' onSelectStart='return false'>");
                stickyHTMLBuffer.append("<layer id='xyz' width='100%' onMouseover='isHot=true;if (isN4) ddN4(theLayer)' onMouseout='isHot=false'>");
                stickyHTMLBuffer.append("<font>" + stickyRetObj.getFieldValueString(i, "STICKY_LABEL") + "</font>");
                stickyHTMLBuffer.append("</layer></ilayer></Td>");

                stickyHTMLBuffer.append("<Td style='cursor:hand' valign='top' width='10%' title='Alarm'>");
                stickyHTMLBuffer.append("<img src=" + alarmImg + " onclick='openAlarm(" + stickId + ")'></Td>");

                stickyHTMLBuffer.append("<Td style='cursor:hand' valign='top' width='10%' title='Save Note'>");
                stickyHTMLBuffer.append("<a href='javascript:void(0)' onclick=" + tAreaSave + " class='ui-icon ui-icon-disk'></a></Td>");
                stickyHTMLBuffer.append("<Td style='cursor:hand' valign='top' width='10%' title='Hide For Session'>");
                stickyHTMLBuffer.append("<a href='javascript:void(0)' onclick=\"" + hideStickySess + "\" class='ui-icon ui-icon-gear'></a></Td>");
                stickyHTMLBuffer.append("<Td style='cursor:hand' valign='top' width='10%' title='Hide For Now'>");
                stickyHTMLBuffer.append("<a href='javascript:void(0)' onclick='hideStickTextNow(" + stickId + ")' class='ui-icon ui-icon-gear'></a> </Td>");
                stickyHTMLBuffer.append("<Td style='cursor:hand' valign='top' width='10%' title='Delete Note'>");
                stickyHTMLBuffer.append(" <a href='javascript:void(0)' onClick=" + closeFn + " class='ui-icon ui-icon-trash'></a></Td></Tr>");
                stickyHTMLBuffer.append("<Tr id='stickTextTr'  style='background-color:" + backGDColor + "'><Td width='100%' valign='top' style='background-color:" + backGDColor + "' colspan='5'>");
                stickyHTMLBuffer.append("<textarea name='sticknote' cols='18' rows='8'  class='mycls' style='overflow:auto;font-size:10px;font-family:verdana;background-color:" + backGDColor + "' id='txtArea" + stickId + "' ");
                stickyHTMLBuffer.append("onfocus=" + tAreaFoc + " >" + sNote + "</textarea> </Td></Tr>");
                stickyHTMLBuffer.append("<tr><td style=\"font-size:8px;width:100%\" colspan=7>Updated:&nbsp;" + createdDate + "</td></tr>");
                stickyHTMLBuffer.append("<tr width='100px'>");
                stickyHTMLBuffer.append("<table><tr><td> <div onclick=\"applyColorSticky('" + stickId + "','#FFFF00')\" style='width:9px;height:9px;background-color: #FFFF00; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#FF8000')\" style='width:9px;height:9px;background-color: #FF8000; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#58FA58')\" style='width:9px;height:9px;background-color: #58FA58; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#BE81F7' )\" style='width:9px;height:9px;background-color:#BE81F7; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#A9A9F5' )\" style='width:9px;height:9px;background-color:#A9A9F5; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#F5A9D0' )\" style='width:9px;height:9px;background-color:#F5A9D0; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("<td><div onclick=\"applyColorSticky('" + stickId + "','#D7FAFF' )\" style='width:9px;height:9px;background-color:#D7FAFF; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>");
                stickyHTMLBuffer.append("</tr></table>");
                stickyHTMLBuffer.append("</Table></DIV>");
                if (i != stickyNoteCount - 1) {
                    stickyHTMLBuffer.append("|_|");
                }
            }
        }
        return stickyHTMLBuffer;
    }

    public StringBuffer buildStickyNote(int stickNodeId, String reportId) {
        String qry = "SELECT STICKY_LABEL,S_NOTE FROM PRG_USER_STICKYNOTE WHERE STICKY_NOTES_ID=" + stickNodeId;
        PbReturnObject stickyReturn = new PbReturnObject();
        PbDb pbdb = new PbDb();
        StringBuffer sb = new StringBuffer("");

        try {
            stickyReturn = pbdb.execSelectSQL(qry);
            if (stickyReturn.getRowCount() > 0) {
                String snote = stickyReturn.getFieldValueString(0, "S_NOTE").replace("<br>", "\n");
                sb.append("<table border='0' width='100px'class='mycls' cellspacing='0' cellpadding='0' style='height:auto'><tr class='navtitle1' width='100%'><td  style='' width='90%'>");
                sb.append("<ilayer width='100%'><layer  width='100%'><font>");
                sb.append("" + stickyReturn.getFieldValueString(0, "STICKY_LABEL") + "");
                sb.append("</font></layer></ilayer></td>");
                sb.append("<td style='cursor:hand' valign='top' width='10%' title='Hide Note'>");
                sb.append("<a href='javascript:void(0)' onclick='hideDiv()' class='ui-icon ui-icon-gear'></a> </td>");
                sb.append("<tr id='stickTextTr'><Td width='100%' valign='top' bgcolor='#d7faff' colspan='4'>");
                sb.append("<textarea name='sticknote' cols='18' readonly='readonly' rows='8' class='mycls' style='overflow:auto;font-size:10px;font-family:verdana'>");
                sb.append("" + snote + "");
                sb.append("</textarea></td></tr><tr><td><a href='javascript:void(0)' onClick='openReport(" + stickNodeId + "," + reportId + ")'>Go to report</a></td></tr></table>");
            }
        } catch (Exception ex) {
        }
        return sb;
    }

    private String getRemainder(String remType, String remValue) throws ParseException {
        String remainder = "";
//        
//        
        Date utilDate = new Date();
        Calendar now = Calendar.getInstance();
//        
        if (remType.equalsIgnoreCase("Date")) {
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            SimpleDateFormat remaindDate = new SimpleDateFormat("mm/dd/yyyy");
            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-mm-dd");
            Date remDate = new Date();
            Date currDate = new Date();
            remDate = remaindDate.parse(remValue);
            currDate = currentDate.parse(sqlDate.toString());
            Calendar remCalender = Calendar.getInstance();
            Calendar currCalender = Calendar.getInstance();
            remCalender.setTime(remDate);
            currCalender.setTime(currDate);
            long remMilliSeconds = remCalender.getTimeInMillis();
            long currMilliSeconds = currCalender.getTimeInMillis();
//            
//             
//              
            if (remMilliSeconds <= currMilliSeconds) {
                return "";
            } else {
                return "none";
            }
        } else if (remType.equalsIgnoreCase("Day")) {
            int day = utilDate.getDay();
//            int day=now.get(Calendar.DAY_OF_WEEK-1);

            String currDay = "";
            if (day == 0) {
                currDay = "Sunday";
            }
            if (day == 1) {
                currDay = "Monday";
            }
            if (day == 2) {
                currDay = "Tuesday";
            }
            if (day == 3) {
                currDay = "Wednesday";
            }
            if (day == 4) {
                currDay = "Thursday";
            }
            if (day == 5) {
                currDay = "Friday";
            }
            if (day == 6) {
                currDay = "Saturday";
            }

            if (remValue.equalsIgnoreCase(currDay)) {
                return "";
            } else {
                return "none";
            }
        } else if (remType.equalsIgnoreCase("Month")) {
            Calendar calendar = Calendar.getInstance();
//            int day = utilDate.getDate();
            int month = utilDate.getMonth();
//            int year = utilDate.getYear();

            int day = calendar.get(Calendar.DATE);
//            int month = calendar.get(Calendar.MONTH+1);
            int year = calendar.get(Calendar.YEAR);
            calendar.set(year, month, day);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//            
//            
            if (remValue.equalsIgnoreCase("First")) {
                if (day == 1) {
                    return "";
                } else {
                    return "none";
                }
            } else if (remValue.equalsIgnoreCase("Last")) {
                if (day == maxDay) {
                    return "";
                } else {
                    return "none";
                }
            }
        }
        return remainder;
    }

    public HashMap getParamList(String repId, int stickyId, Container container) throws Exception {
        String qry = "select PARAM_XML from PRG_USER_STICKYNOTE where STICKY_NOTES_ID=" + stickyId;
        PbReturnObject pbro = new PbReturnObject();
        PbDb pbDb = new PbDb();
        pbro = pbDb.execSelectSQL(qry);
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        Element root = null;
        String elemenValue = "";
        String elemenId = "";
        String viewValue = "";
        String viewId = "";
        HashMap paramValueMap = new HashMap();
        HashMap timeMap = new HashMap();
        if (pbro.getFieldValueClobString(0, "PARAM_XML") != null && pbro.getFieldValueClobString(0, "PARAM_XML") != "") {
            document = builder.build(new ByteArrayInputStream(pbro.getFieldValueClobString(0, "PARAM_XML").getBytes()));
            root = document.getRootElement();
            List rowParam = root.getChildren("ParamSection");
            for (int i = 0; i < rowParam.size(); i++) {
                Element ParamSection = (Element) rowParam.get(i);
                List ElementId = ParamSection.getChildren("ElementId");
                for (int j = 0; j < ElementId.size(); j++) {
                    Element eleId = (Element) ElementId.get(j);
                    elemenId = eleId.getText();
                    List ElementValue = ParamSection.getChildren("ElementValue");
                    Element eleValue = (Element) ElementValue.get(j);
                    elemenValue = eleValue.getText();
                    //  //////////////.println("elemenId" + elemenId + "elemenValue---" + elemenValue);
                    paramValueMap.put(elemenId, elemenValue);
                }
                List ViewById = ParamSection.getChildren("ViewById");
                List ViewByValue = ParamSection.getChildren("ViewByValue");
                for (int j = 0; j < ViewById.size(); j++) {
                    Element viewById = (Element) ViewById.get(j);
                    viewId = viewById.getText();
                    Element viewByValue = (Element) ViewByValue.get(j);
                    viewValue = viewByValue.getText();
                    paramValueMap.put("CBOVIEW_BY" + viewId, viewValue);
                }
            }
            List timeParam = root.getChildren("TimeDimensions");

            for (int i = 0; i < timeParam.size(); i++) {
                Element timeSection = (Element) timeParam.get(i);
                List asOfDate = timeSection.getChildren("AsOfDate");
                for (int j = 0; j < asOfDate.size(); j++) {
                    Element asOf = (Element) asOfDate.get(j);
                    elemenId = asOf.getText();
                    List PeriodType = timeSection.getChildren("PeriodType");
                    Element perValue = (Element) PeriodType.get(j);
                    elemenValue = perValue.getText();
                    if (elemenValue.charAt(2) == '/') {
                        paramValueMap.put("CBO_AS_OF_DATE1", elemenId);
                        paramValueMap.put("CBO_AS_OF_DATE2", elemenValue);
                    } else {
                        paramValueMap.put("CBO_AS_OF_DATE", elemenId);
                        paramValueMap.put("CBO_PRG_PERIOD_TYPE", elemenValue);
                    }

                }
            }

        }
//         
//         HashMap parameterMap=container.getParametersHashMap();
//         ArrayList TimeDetailstList=(ArrayList) parameterMap.get("TimeDetailstList");
//         HashMap TimeDimHashMap=(HashMap) parameterMap.get("TimeDimHashMap");
//         ArrayList aSOFDATE=(ArrayList) TimeDimHashMap.get("AS_OF_DATE");
//         ArrayList pRGPERIODTYPE=(ArrayList) TimeDimHashMap.get("PRG_PERIOD_TYPE");
//         ArrayList pRGCOMPARE=(ArrayList) TimeDimHashMap.get("PRG_COMPARE");


//        ArrayList list=new ArrayList();
        return paramValueMap;
    }

    public static void main(String args[]) throws ParseException {
        Date d1 = new Date();
        Date d2 = new Date();
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.YEAR);

        java.sql.Date sqlDate = new java.sql.Date(d1.getTime());


        SimpleDateFormat formatter1 = new SimpleDateFormat("mm/dd/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-mm-dd");
        d1 = formatter1.parse("09/22/2010");
        d2 = formatter2.parse("2011-09-20");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(d1);
        calendar2.setTime(d2);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
//        

//       java.util.Date utilDate = new java.util.Date();
//    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//    
//    

    }
}
