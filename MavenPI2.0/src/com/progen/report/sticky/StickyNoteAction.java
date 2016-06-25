/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.sticky;

import com.progen.reportdesigner.db.DashboardTemplateDAO;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class StickyNoteAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(StickyNoteAction.class);
    /*
     * forward name="success" path=""
     */

    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("createStickyNote", "createStickyNote");
        map.put("saveStickyNote", "saveStickyNote");
        map.put("buildAllStickyNotes", "buildAllStickyNotes");
        map.put("deleteStickyNote", "deleteStickyNote");
        map.put("forshowStickSession", "forshowStickSession");
        map.put("buildStickyNote", "buildStickyNote");
        map.put("saveRemainderForStrickyNote", "saveRemainderForStrickyNote");
        map.put("viewReportForStickyNote", "viewReportForStickyNote");
        map.put("getAlarmForSticky", "getAlarmForSticky");
        map.put("clearAlarmForSticky", "clearAlarmForSticky");
        return map;
    }

    public ActionForward createStickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String noteLabel = request.getParameter("noteLabel");
        String initialText = request.getParameter("intText");
        PrintWriter out = response.getWriter();
        String str = "";
        int divid = 0;
//        int divid=0;
        PbReturnObject pbReturn = new PbReturnObject();
        PbDb pbDb = new PbDb();
        String nxtsqlqry = "";
        if ((ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER))) {
            nxtsqlqry = " SELECT IDENT_CURRENT('PRG_USER_STICKYNOTE')";
        } else {
            nxtsqlqry = "SELECT PRG_USER_STICKYNOTE_SEQ.nextval NEXTID FROM dual";
        }

        pbReturn = pbDb.execSelectSQL(nxtsqlqry);
        divid = pbReturn.getFieldValueInt(0, 0);
        divid++;

//        divid=Integer.parseInt(dividStr)+1;
        str = "<div id='" + divid + "' name='name" + divid + "' class='' style='position:absolute;width:150px;left:600px;top:200px;'>";
        str = str + "<table border='0' width='100px' class='mycls' cellspacing='0' cellpadding='0' >";
        str = str + "<tr onmousemove=divMove('" + divid + "') class='navtitle1' id='" + divid + "tr'>";
        str = str + "<td id='titleBar'  style='cursor:move' width='80%' id='" + divid + "td'>";
//        str = str + "<td id='color'  style='cursor:move' width='10%' title='Colour'>";
//         str = str + "<a href='#'  onclick=\"applyColor('"+divid +"')\" class='ui-icon ui-icon-disk'></a></td>";
//

//        str = str + "<td id='titleBar'  style='cursor:move' width='60%'>";
        str = str + "<ilayer width='100%' onSelectStart='return false'>";

        str = str + "<layer id='xyz' width='100%' onMouseover='isHot=true;if (isN4) ddN4(theLayer)' onMouseout='isHot=false'  >";

        str = str + "<font><input type=text maxlength=15 size=14 class='inputlabel' value='" + noteLabel + "' readonly style='border:0px'></font>";
        str = str + "</layer>";
        str = str + "</ilayer>";
        str = str + "</td>";
        str = str + "<td style='cursor:hand' valign='top' width='10%' title=\"Save Note\">";
        str = str + "<a href='#' id='hrefID" + divid + "' onClick=\"javascript:saveStickyNote('txtArea" + divid + "','" + divid + "','save')\" class='ui-icon ui-icon-disk'></a>";
        str = str + "</td>";
        str = str + "<td style='cursor:hand' valign='top' width='10%' title=\"Delete Note\">";
        str = str + "<a href='#' onClick=\"deleteSticky('" + divid + "');return false;\" class='ui-icon ui-icon-trash'></a>";
        str = str + "</td>";
        str = str + "</tr>";
        str = str + " <tr>";
        str = str + "<td width='100%' valign='top' bgcolor='#d7faff' colspan='5'>";

        str = str + "<Textarea name='sticknote' border='0' cols='18' rows='8' class='mycls' style='overflow:none' id='txtArea" + divid + "' ";
        str = str + " onfocus='javascript:tAreaFocus(this)' >";
//        str = str + " onblur=javascript:saveStickyNote(this,'" + divid + "','update')>";
        str = str + initialText;
        str = str + "</Textarea>";

        str = str + "</td>";
        str = str + "</tr>";
        str = str + "<tr width='100px'>";
        str = str + "<table><tr><td> <div onclick=\"applyColorSticky('" + divid + "','#FFFF00')\" style='width:9px;height:9px;background-color: #FFFF00; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "<td><div onclick=\"applyColorSticky('" + divid + "','#FF8000')\" style='width:9px;height:9px;background-color: #FF8000; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "<td><div onclick=\"applyColorSticky('" + divid + "','#58FA58')\" style='width:9px;height:9px;background-color: #58FA58; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "<td><div onclick=\"applyColorSticky('" + divid + "','#BE81F7' )\" style='width:9px;height:9px;background-color:#BE81F7; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "<td><div onclick=\"applyColorSticky('" + divid + "','#A9A9F5' )\" style='width:9px;height:9px;background-color:#A9A9F5; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "<td><div onclick=\"applyColorSticky('" + divid + "','#F5A9D0' )\" style='width:9px;height:9px;background-color:#F5A9D0; border-color: rgb(0, 0, 0);'>&nbsp;</div> </td>";
        str = str + "</tr></table>";
        str = str + "</table>";
        str = str + "</div>";
        out.print(str);
        return null;
    }

    public ActionForward saveStickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        HttpSession session = request.getSession(false);
        String parameterXml = null;
        String topp = request.getParameter("topp");
        String leftp = request.getParameter("leftp");
        String widthp = request.getParameter("widthp");
        String heightp = request.getParameter("heightp");
        String snote = request.getParameter("snote");
        String mode = request.getParameter("mode");
        String noteLabel = request.getParameter("noteLabel");
        String parameters = request.getParameter("parameters");
        String trbgcolor = request.getParameter("trbgcolor");
//        
        //////////////.println("parameters----"+parameters);
        String TimeLevel = request.getParameter("TimeLevelstr");
        String userId = "";
        String noteID = request.getParameter("noteID");
        StickyNoteBD stickyBD = new StickyNoteBD();
        if (session != null) {
            try {
                userId = String.valueOf(session.getAttribute("USERID"));
                XMLOutputter serializer = null;
                Document document = null;
                HashMap map = new HashMap();
                Container container = null;
                HashMap paramMap = new HashMap();
                HashMap paramTime = new HashMap();
//                String parameters = "";
                DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                ReportTemplateDAO repDAO = new ReportTemplateDAO();
                String PbReportId = request.getParameter("REPORTID");
                String completeURL = request.getParameter("completeURL");
                String timeLevelStr = "";

                HashMap ParamDefValsHashMap = new HashMap();

                if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                }
                if (map.get(PbReportId) != null) {
                    container = (prg.db.Container) map.get(PbReportId);

                } else {
                    container = new prg.db.Container();
                }
                String userFolders = repDAO.getReportUserFolders(PbReportId);
                String minTimeLevel = dbrdDAO.getUserFolderMinTimeLevel(userFolders);

                if (minTimeLevel.equalsIgnoreCase("5")) {
                    timeLevelStr = "Day";
                } else if (minTimeLevel.equalsIgnoreCase("4")) {
                    timeLevelStr = "Week";
                } else if (minTimeLevel.equalsIgnoreCase("3")) {
                    timeLevelStr = "Month";
                } else if (minTimeLevel.equalsIgnoreCase("2")) {
                    timeLevelStr = "Quarter";
                } else if (minTimeLevel.equalsIgnoreCase("1")) {
                    timeLevelStr = "Year";
                }
                request.setAttribute("timelevel", timeLevelStr);

                paramMap = container.getReportParameterHashMap();
                paramTime = container.getParametersHashMap();
                if (paramMap.size() == 0) {
                    paramMap = paramTime;
                }
                ArrayList timeArray = (ArrayList) paramMap.get("TimeDetailstList");
                HashMap timeDetsMap = (HashMap) paramMap.get("TimeDimHashMap");

                PrintWriter out = response.getWriter();

                Element root = new Element("StickyNote");
                root.setAttribute("version", "1.00001");
                root.setText("New Root");
                Element TimeDimensions = new Element("TimeDimensions");
                ArrayList dateArray = new ArrayList();
                ArrayList periodArray = new ArrayList();
                ArrayList compareArray = new ArrayList();

                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_PERIOD_TYPE");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");

                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DATE2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_DAY_ROLLING")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DATE");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_DAY_ROLLING");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_MONTH_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DMONTH1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DMONTH2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DYEAR1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DYEAR2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Day") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_RANGE")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_DQUARTER1");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_DQUARTER2");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("WEEK") && timeArray.get(1).toString().equalsIgnoreCase("PRG_WEEK_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_WEEK");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_WEEK1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("QUARTER") && timeArray.get(1).toString().equalsIgnoreCase("PRG_QUARTER_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_QUARTER");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_QUARTER1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("Month") && timeArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_MONTH");
                    periodArray = (ArrayList) timeDetsMap.get("PRG_PERIOD_TYPE");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }
                if (timeArray.get(0).toString().equalsIgnoreCase("YEAR") && timeArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                    dateArray = (ArrayList) timeDetsMap.get("AS_OF_YEAR");
                    periodArray = (ArrayList) timeDetsMap.get("AS_OF_YEAR1");
//                    compareArray = (ArrayList) timeDetsMap.get("PRG_COMPARE");
                }

                String date = dateArray.get(0).toString();
                String periodType = periodArray.get(0).toString();
//              String compareType = compareArray.get(0).toString

                Element asOfDateValue = new Element("AsOfDate");
                asOfDateValue.setText(date);
                TimeDimensions.addContent(asOfDateValue);

                Element periodTypeValue = new Element("PeriodType");
                periodTypeValue.setText(periodType);
                TimeDimensions.addContent(periodTypeValue);

//                Element compareWithValue = new Element("CompareWith");
//                compareWithValue.setText(compareType);
//                TimeDimensions.addContent(compareWithValue);


                Element ParaValues = new Element("ParamSection");
                String urldets[] = completeURL.split(";");
                for (int j = 0; j < urldets.length; j++) {
                    if (urldets[j].startsWith("CBOARP")) {
                        String defkey = urldets[j].split("=")[0];
                        defkey = defkey.substring(6);
                        String defvalue = urldets[j].split("=")[1];
                        Element elementId = new Element("ElementId");
                        elementId.setText(defkey);
                        ParaValues.addContent(elementId);
                        Element elementValue = new Element("ElementValue");
                        elementValue.setText(defvalue);
                        ParaValues.addContent(elementValue);
                    }
                    if (urldets[j].startsWith("CBOVIEW_BY")) {
                        String defkey = urldets[j].split("=")[0];
                        defkey = defkey.substring(10);
                        String defvalue = urldets[j].split("=")[1];
                        Element viewBy = new Element("ViewById");
                        viewBy.setText(defkey);
                        ParaValues.addContent(viewBy);
                        Element viewByValue = new Element("ViewByValue");
                        viewByValue.setText(defvalue);
                        ParaValues.addContent(viewByValue);
                    }
                }
                root.addContent(TimeDimensions);
                root.addContent(ParaValues);
                document = new Document(root);
                serializer = new XMLOutputter();

                parameterXml = serializer.outputString(document);

                parameterXml = parameterXml.replaceAll("(\\r|\\n)", "");
                String uniqueID = stickyBD.saveStickyNote(topp, leftp, widthp, heightp, snote, mode, noteLabel,
                        parameters, TimeLevel, parameterXml, PbReportId, userId, noteID, trbgcolor);


                out.print(uniqueID);


            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }


        return null;
    }

    public ActionForward buildAllStickyNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession(false);
        String userId = String.valueOf(session.getAttribute("USERID"));
        String reportId = String.valueOf(request.getParameter("REPORTID"));
        String compURL = String.valueOf(request.getParameter("currentURL"));
//        
        String urldets[] = compURL.split(";");
        String defkey = "";
        String defvalue = "";
        String nextValidate = "";
        HashMap paramTime = new HashMap();
        ArrayList timeArray = new ArrayList();
        HashMap map = new HashMap();
        Container container = null;
        StickyNoteBD stickyBD = new StickyNoteBD();
        PrintWriter out = null;
        if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
            map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        }
        if (map.get(reportId) != null) {
            container = (prg.db.Container) map.get(reportId);
        }
        paramTime = container.getParametersHashMap();
        String url = container.getReportCollect().completeUrl;

        timeArray = (ArrayList) paramTime.get("TimeDetailstList");

        // String timeArraystr1 = request.getParameter("timeArraystr");
        ArrayList timeArraystr1 = new ArrayList();
        if (timeArraystr1 == null) {
            timeArraystr1 = timeArray;
        }
        //   String timeArraystr2[] = timeArraystr1.split(",");
        HashMap ParamUrlHashMap = new HashMap();
        for (int i = 2; i < urldets.length; i++) {
            if (urldets[i].startsWith("CBOARP")) {
                defkey = urldets[i].split("=")[0];
                defkey = defkey.substring(6);
                defvalue = urldets[i].split("=")[1];
            }
            if (urldets[i].startsWith("CBOVIEW_BY")) {
                defkey = urldets[i].split("=")[0];
                defkey = defkey.substring(10);
                defvalue = urldets[i].split("=")[1];
            }
            ParamUrlHashMap.put(defkey, defvalue);
        }
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        Element root = null;
        String query = "SELECT * FROM PRG_USER_STICKYNOTE "
                + "WHERE USER_ID = '" + userId + "'"
                + " AND REPORT_ID = '" + reportId + "'";

        PbReturnObject stickyRetObj = null;
        int stickyNoteCount = 0;
        ArrayList listcount = new ArrayList();
        String noteInitial = "ENTER YOUR NOTES HERE";
        Connection stickyCon = ProgenConnection.getInstance().getConnection();
        try {
            out = response.getWriter();

            Statement StickySt = stickyCon.createStatement();
            ResultSet StickyRs = StickySt.executeQuery(query);
            stickyRetObj = new PbReturnObject(StickyRs);
            String DateValue = "";
            HashMap paramValueMap = new HashMap();
            String PeriodValue = "";
            String CompareValue = "";
            String elemenValue = "";
            String elemenId = "";
            String viewValue = "";
            String viewId = "";
            for (int r = 0; r < stickyRetObj.getRowCount(); r++) {
                if (stickyRetObj.getFieldValueClobString(r, "PARAM_XML") != null && stickyRetObj.getFieldValueClobString(r, "PARAM_XML") != "") {
                    document = builder.build(new ByteArrayInputStream(stickyRetObj.getFieldValueClobString(r, "PARAM_XML").getBytes()));
                    root = document.getRootElement();
                    List row = root.getChildren("TimeDimensions");
                    for (int p = 0; p < row.size(); p++) {
                        Element TimeDimensions = (Element) row.get(p);
                        List asOfDate = TimeDimensions.getChildren("AsOfDate");
                        for (int j = 0; j < asOfDate.size(); j++) {
                            Element asOfDateValue = (Element) asOfDate.get(j);
                            DateValue = asOfDateValue.getText();
                        }
                        List periodType = TimeDimensions.getChildren("PeriodType");
                        for (int j = 0; j < periodType.size(); j++) {
                            Element periodTypeValue = (Element) periodType.get(j);
                            PeriodValue = periodTypeValue.getText();
                        }
                        /*
                         * List compareType =
                         * TimeDimensions.getChildren("CompareWith"); for (int j
                         * = 0; j < compareType.size(); j++) { Element
                         * compareWithValue = (Element) compareType.get(j);
                         * CompareValue = compareWithValue.getText(); }
                         */
                    }
                    if (timeArray.contains(DateValue)) {
                        if (timeArray.contains(PeriodValue)) {
                            // if (timeArray.contains(CompareValue)) {
                            nextValidate = "Success";
                            //    }
                        }
                    } else {
                        nextValidate = "Fail";
                    }
                    if (nextValidate.equalsIgnoreCase("Success")) {
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
                                paramValueMap.put(viewId, viewValue);
                            }
                        }
                        Set paramKeys = paramValueMap.keySet();
                        Object paramKeysarr[] = paramKeys.toArray();
                        Set paramUrlKeys = ParamUrlHashMap.keySet();
                        Object paramUrlKeyarr[] = paramUrlKeys.toArray();
                        String urlParamValue = "";
                        String xmlParamValue = "";
                        int count = 0;
//                        

                        for (int k = 0; k < paramKeysarr.length; k++) {
                            urlParamValue = String.valueOf(ParamUrlHashMap.get(paramUrlKeyarr[k]));
                            xmlParamValue = String.valueOf(paramValueMap.get(paramKeysarr[k]));
                            if (urlParamValue.equalsIgnoreCase(xmlParamValue)) {
                                count++;
                            }
                        }

                        if (count == paramKeysarr.length) {
                            listcount.add(r);

                        } else {
                            // //.println("Sticky Note Not Available");
                        }
                    }
                }
            }
            String contextPath = request.getContextPath();
            stickyNoteCount = stickyRetObj.getRowCount();
            stickyCon.close();
            stickyCon = null;
            HashMap stickListHashMap = new HashMap();
            if (session.getAttribute("stickListHashMap") != null) {
                stickListHashMap = (HashMap) session.getAttribute("stickListHashMap");
            }
            StringBuffer stickyHTMLBuffer = stickyBD.buildAllStickyNotes(stickyNoteCount, listcount, stickyRetObj, stickListHashMap, contextPath);
            out.print(stickyHTMLBuffer);

        } catch (Exception e) {
            stickyRetObj = new PbReturnObject();
            logger.error("Exception:", e);
        } finally {
            try {
                if (stickyCon != null) {
                    stickyCon.close();
                }
                stickyCon = null;
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public ActionForward buildStickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        int stickNodeId = Integer.parseInt(request.getParameter("stickNodeId"));
//         
        String reportId = String.valueOf(request.getParameter("reportId"));
//        int reportId=Integer.parseInt(request.getParameter("reportId"));
        StickyNoteBD stickyBD = new StickyNoteBD();


        StringBuffer sb = stickyBD.buildStickyNote(stickNodeId, reportId);
        response.getWriter().print(sb);
        return null;
    }

    public ActionForward viewReportForStickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        int stickNodeId = Integer.parseInt(request.getParameter("stickyId"));
        String reportId = String.valueOf(request.getParameter("REPORTID"));
        StickyNoteBD stickyBD = new StickyNoteBD();
        HashMap map = new HashMap();
        Container container = null;

        if (request.getSession(false).getAttribute("PROGENTABLES") != null) {
            map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
        }
        if (map.get(reportId) != null) {
            container = (prg.db.Container) map.get(reportId);
        }

        HashMap actualMap = stickyBD.getParamList(reportId, stickNodeId, container);
        String keySet[] = (String[]) actualMap.keySet().toArray(new String[0]);

        String url = "";
//        
        String viewBy = "";
        for (int i = 0; i < keySet.length; i++) {
            if (keySet[i].length() > 9) {
                viewBy = keySet[i].substring(0, 10);
//               
            }
            if (viewBy.equalsIgnoreCase("CBOVIEW_BY")) {
                url = url + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);
            } else if (keySet[i].equalsIgnoreCase("CBO_AS_OF_DATE")) {
                url = url + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);
            } else if (keySet[i].equalsIgnoreCase("CBO_AS_OF_DATE1")) {
                url = url + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);
            } else if (keySet[i].equalsIgnoreCase("CBO_AS_OF_DATE2")) {
                url = url + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);
            } else if (keySet[i].equalsIgnoreCase("CBO_PRG_PERIOD_TYPE")) {
                url = url + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);
            } else {
                url = url + "CBOARP" + keySet[i];
                url = url + "=" + actualMap.get(keySet[i]);

            }
            if (i != keySet.length - 1) {
                url = url + ";";
            }
        }
        url = url.replace(";", "&");
        url = request.getContextPath() + "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&" + url;

        response.getWriter().print(url);

        return null;
    }

    public ActionForward deleteStickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        int stickyId = Integer.parseInt(request.getParameter("stickNodeId"));
        PbDb pbdb = new PbDb();
        String sql = "DELETE FROM PRG_USER_STICKYNOTE WHERE STICKY_NOTES_ID=" + stickyId;
        pbdb.execUpdateSQL(sql);
        return null;
    }

    public ActionForward forshowStickSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
//        
        HttpSession session = request.getSession(false);
        String status = "";
        HashMap stickListHashMap = new HashMap();
        HashMap stickValueMap = new HashMap();
        boolean isExists = false;

        if (session != null) {
            String stickListId = request.getParameter("stickListId");
            String dispStick = String.valueOf(request.getParameter("disp"));
            if (dispStick.equalsIgnoreCase("none")) {
                stickListHashMap = (HashMap) session.getAttribute("stickListHashMap");
//                session.setAttribute("stickListHashMap", stickListHashMap);
//                status = "block";
                stickValueMap = (HashMap) session.getAttribute("stickValueMap");
                if (stickListHashMap != null) {
                    if (stickListHashMap.remove(stickListId) != null || stickListHashMap.remove(stickListId) != "") {
                        stickValueMap.remove(stickListId);
                        stickListHashMap.remove(stickListId);
                        isExists = true;
                    }
                }
                session.setAttribute("stickValueMap", stickValueMap);
                session.setAttribute("stickListHashMap", stickListHashMap);
                response.getWriter().print(isExists);
            }
            // response.getWriter().print(status);
            return null;
        } else {
            return mapping.findForward("sessionExpired");
        }
    }

    public ActionForward saveRemainderForStrickyNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String selType = request.getParameter("selType");
        String selValue = request.getParameter("selValue");
//        
        int stickyId = Integer.parseInt(request.getParameter("stickyId"));
        PrintWriter out = response.getWriter();
        PbDb pbdb = new PbDb();
        String updateSql = "UPDATE PRG_USER_STICKYNOTE SET REMAINDER_TYPE='" + selType + "', REMAINDER_VALUE='" + selValue + "' WHERE STICKY_NOTES_ID=" + stickyId;
//        
        int ct = pbdb.execUpdateSQL(updateSql);
//        
        if (ct > 0) {
            out.print("success");
        } else {
            out.print("fail");
        }
        return null;
    }

    public ActionForward getAlarmForSticky(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        int stickId = Integer.parseInt(request.getParameter("stickyId"));
        PbReturnObject pbro = new PbReturnObject();
        PbDb pbDb = new PbDb();
        String qry = "select REMAINDER_TYPE,REMAINDER_VALUE from PRG_USER_STICKYNOTE where STICKY_NOTES_ID=" + stickId;
        pbro = pbDb.execSelectSQL(qry);
        String remainder = "";
        if (pbro != null && pbro.getRowCount() > 0) {

            remainder = remainder + pbro.getFieldValueString(0, "REMAINDER_TYPE") + ":";
            remainder = remainder + pbro.getFieldValueString(0, "REMAINDER_VALUE");
        }
        response.getWriter().print(remainder);
        return null;
    }

    public ActionForward clearAlarmForSticky(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        int stickId = Integer.parseInt(request.getParameter("stickyId"));
        PbReturnObject pbro = new PbReturnObject();
        PbDb pbDb = new PbDb();
        String qry = "UPDATE  PRG_USER_STICKYNOTE SET REMAINDER_TYPE='',REMAINDER_VALUE='' WHERE STICKY_NOTES_ID=" + stickId;
        pbro = pbDb.execSelectSQL(qry);
        return null;
    }

    public static void main(String a[]) throws ParseException, Exception {
        String date1 = "2010-09-04 14:54:57.797";
        String date2 = "2010-09-05 14:54:57.797";
        Date d1 = new Date();
        Date d2 = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.mmm");
        d1 = formatter.parse(date1);
        d2 = formatter.parse(date2);
//        
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(d1);
        calendar2.setTime(d2);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
//        
//         PbDb pbdb = new PbDb();
//        String updateSql = "UPDATE PRG_USER_STICKYNOTE SET REMAINDER_TYPE='Date', REMAINDER_VALUE='09/29/2010' WHERE STICKY_NOTES_ID=287";
//        
//        int ct = pbdb.execUpdateSQL(updateSql);

    }
}
