/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportview.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.jdbc2.optional.JDBC4PreparedStatementWrapper;
import com.progen.db.ProgenDataSet;
import com.progen.report.ReportParameter;
import com.progen.report.colorgroup.ColorCode;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.query.PbReportQuery;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.xml.pbXmlUtilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sourceforge.jtds.jdbcx.proxy.PreparedStatementProxy;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import prg.db.Container;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;
import utils.db.ProgenParam;

/**
 *
 * @author db2admin
 */
public class SnapshotDesigner extends PbDb {

    public static Logger logger = Logger.getLogger(SnapshotDesigner.class);
    ResourceBundle resBun;
    public Container container = null;
    public HashMap ParametersHashMap = null;
    public HashMap TableHashMap = null;
    public HashMap GraphHashMap = null;
    public String reportId = "";
    public String[] reportIds = null;
//    public String snapreportId = "";
    public String completeUrl = "";
    public String snapDate = "";
    public String reportName = "";
    public String snapshotName = "";
    public String[] pMasterChildTags = {"pmName", "pmDisplayName", "pmDisplayType"};
    public String[] ppDetailChildTags = {"element_id", "paramDispName", "childElementId", "dimId", "dimTabId", "displayType", "relLevel", "dispSeqNo", "defaultValue", "isDisplay", "reportParameters"};
    public String[] ptMasterChildTags = {"timeLevel", "timeType"};
    public String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue", "realDate"};
    public String[] pvrMasterDataChildTags = {"pvrdefaultValue"};
    public String[] pgMasterChildTags = {"graphId", "graphName", "graphWidth", "graphHeight", "graphClassName",
        "graphTypeName", "graphSize", "SwapColumn", "graphLegend", "graphLegendLoc", "graphshowX", "graphshowY",
        "graphLYaxislabel", "graphRYaxislabel", "graphDrill", "graphBcolor", "graphFcolor", "graphData", "showGT", "measureNamePosition", "graphDisplayRows", "nbrFormat"};
    public String[] rqDetailsChildTags = {"elementId", "elementName", "elementAgg", "graphAxis"};
    public String[] rqMoreInfoChildTags = {"orderCol", "orderType", "totalRecord"};
    /////////////////////////////////////////////////
    public String[] MetaInfoChildValues = null;
    public String paramsString = "";
    public String paramString1 = "";
    public String paramString2 = "";
    public String isoneviewschedule = "";
    public String contextpath = "";
    //String measureString = "";
    //String measureString1 = "";
    public ArrayList params = new ArrayList();
    public ArrayList timeDetails = new ArrayList();
    public HashMap timeDimHashMap = new HashMap();
    public HashMap reportHashMap = new HashMap();
    public ArrayList REP_Elements = new ArrayList();
    public ArrayList CEP_Elements = new ArrayList();
    public ArrayList measures = new ArrayList();
    public ArrayList measuresNames = new ArrayList();
    public ArrayList grpCols = new ArrayList();
    public Set total_list = new LinkedHashSet();
    public String[] no_of_graphs = new String[0];
    public HttpServletRequest request = null;
    public Statement statement = null;
    public ResultSet resultSet = null;
    public LinkedHashMap<String, ArrayList<String>> reportParameters = new LinkedHashMap<String, ArrayList<String>>();
    public HashMap<String, HashMap<String, List>> operatorFilters = new HashMap<String, HashMap<String, List>>();
    public Container container1;
//    public String schedulerStatus=null;
    public String schedulerId = null;
    public String schedulerId1 = null;
    ////////////////////////////////////////////////
    //list of queries
    XMLOutputter serializer = null;
    Document document = null;
    //aded by sanhtosh.k on 10-03-2010
    ReportTemplateDAO TemplateDAO = new ReportTemplateDAO();
    String fromOption;

    private ResourceBundle getResourceBundle() {
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            return new SnapShotResBunSqlServer();
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            return new SnapShotResBunSqlServer();
        } else {
            return new SnapShotResourceBundle();
        }
    }

    public void createDocument(String reportId1, String completeUrl1, HttpServletRequest request, String snapDate1, String emailList, String userId, String repcustname, String fromOption) throws Exception {
        try {
//            if (reportId1.equalsIgnoreCase(null) || "".equalsIgnoreCase(reportId1)) {
//                reportId1 = completeUrl1.split(";")[1].split("=")[1];
//            }
            reportId = reportId1;
//            completeUrl = completeUrl1;
            snapDate = snapDate1;
            snapshotName = repcustname;
            String repIdQuery = "";
            //Start of code by sandeep on 16/10/14 for schedule// update local files in oneview
            if (isoneviewschedule != null && isoneviewschedule.equalsIgnoreCase("true")) {
                schedulerId = schedulerId1;
            } else {
                schedulerId = (String) request.getAttribute("schedulerId");
            }
            //End of code by sandeep on 16/10/14 for schedule// update local files in oneview
            this.fromOption = fromOption;
//            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                repIdQuery = "select IDENT_CURRENT('PRG_AR_REPORT_MASTER')";
//            } else {
//                repIdQuery = "select PRG_AR_REPORT_MASTER_SEQ.nextval from dual";
//            }

//            PbReturnObject pbrorepId = execSelectSQL(repIdQuery);
//            snapreportId = String.valueOf(pbrorepId.getFieldValueInt(0, 0));
            if (isoneviewschedule != null && isoneviewschedule.equalsIgnoreCase("true")) {
                container = this.getContainer();
            } else {
                HashMap map = new HashMap();
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                container = (prg.db.Container) map.get(reportId);
            }
            completeUrl = container.getReportCollect().completeUrl;
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            GraphHashMap = container.getGraphHashMap();
            reportHashMap = container.getReportHashMap();
            reportParameters = container.getReportCollect().reportParameters;
            operatorFilters = container.getReportCollect().operatorFilters;


            if (ParametersHashMap != null) {
                if (ParametersHashMap.get("Parameters") != null) {
                    params = (ArrayList) ParametersHashMap.get("Parameters");
                }
                if (ParametersHashMap.get("TimeDetailstList") != null) {
                    timeDetails = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                }
                if (ParametersHashMap.get("TimeDimHashMap") != null) {
                    timeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
                }
            }
            if (TableHashMap != null) {
                if (TableHashMap.get("REP") != null) {
                    REP_Elements = (ArrayList) TableHashMap.get("REP");
                } else {
                    if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                    }
                }
                if (TableHashMap.get("CEP") != null) {
                    CEP_Elements = (ArrayList) TableHashMap.get("CEP");
                }
                if (TableHashMap.get("Measures") != null && TableHashMap.get("MeasuresNames") != null) {
                    measures = (ArrayList) TableHashMap.get("Measures");
                    measuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                }

            }
            if (GraphHashMap != null) {
                if (GraphHashMap.get("AllGraphColumns") != null) {
                    grpCols = (ArrayList) GraphHashMap.get("AllGraphColumns");
                }
                if (GraphHashMap.get("graphIds") != null) {
                    no_of_graphs = String.valueOf(GraphHashMap.get("graphIds")).split(",");//((String) getGraphHashMap().get("graphIds")).split(",");
                }
            }
            total_list = new LinkedHashSet();

            for (int i = 0; i < params.size(); i++) {
                paramsString = paramsString + "," + String.valueOf(params.get(i)).replace("A_", "");
            }
            if (!(paramsString.equalsIgnoreCase(""))) {
                paramsString = paramsString.substring(1);
            }
            paramString2 = " case ";
            for (int i = 0; i < params.size(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    paramString2 += " when element_id =" + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";

                }
                else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    paramString2 += " when element_id =" + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";

                }
                else {
                    paramString1 = paramString1 + "," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1);

                }

            }
            if (!(paramString1.equalsIgnoreCase(""))) {
                        paramString1 = paramString1.substring(1);
            }
            paramString2 += " else 10000 end ";

            Element root = new Element("snapshot");
            //root.setAttribute("version", "1.00001");
            //root.setText("New Root");
            Element repVersionElem=new Element("Report_Version");
            repVersionElem.addContent(String.valueOf(container.getReportCollect().getReportVersion()));
            root.addContent(repVersionElem);

            root = buildMetaInfo(root);
            root = buildParameters(root);
            root = buildTimeDetails(root);
            // root = buildViewByDetails(root);
            root = buildQueryDetails(root);
            String contextpth1 = "";
            document = new Document(root);
            serializer = new XMLOutputter();
            //Start of code by sandeep on 16/10/14 for schedule// update local files in oneview
            if (isoneviewschedule != null && isoneviewschedule.equalsIgnoreCase("true")) {
                contextpth1 = contextpath;

            } else {
                contextpth1 = request.getContextPath();
            }
            //End of code by sandeep on 16/10/14 for schedule// update local files in oneview
            //   
            if (schedulerId != null) {
                String query1 = getResourceBundle().getString("updateSchedulerParamDetails");
                Connection con = ProgenConnection.getInstance().getConnection();
                String snapShotClob = serializer.outputString(document);
//                Object[] obj =new Object[6];
//                obj[0]=userId;
//                obj[1]=snapShotClob.replace("&lt;", "<").replace("&gt;", ">");
//                obj[2]=request.getContextPath();
//                String a[] = completeUrl.substring(1).split("/");
//                obj[3]=a[1];
//                obj[4]=schedulerId;
//                obj[5]=reportId;
//               String finalQuery = buildQuery(query1, obj);
//                
//                execUpdateSQL(finalQuery);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    PreparedStatementProxy opstmt = (PreparedStatementProxy) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setString(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(3, contextpth1);
                    String a[] = completeUrl.substring(1).split("/");
                    opstmt.setString(4, a[1]);
                    opstmt.setString(5, schedulerId);
                    opstmt.setString(6, reportId);
//                    int rows = opstmt.executeUpdate();
                    opstmt.executeUpdate();

                }
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    JDBC4PreparedStatementWrapper opstmt = (JDBC4PreparedStatementWrapper) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setString(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(3, contextpth1);
                    String a[] = completeUrl.substring(1).split("/");
                    opstmt.setString(4, a[1]);
                    opstmt.setString(5, schedulerId);
                    opstmt.setString(6, reportId);
//                    int rows = opstmt.executeUpdate();
                    opstmt.executeUpdate();
                } else {
                    OraclePreparedStatement opstmt;
                    opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setStringForClob(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(3, contextpth1);
                    String a[] = completeUrl.substring(1).split("/");
                    opstmt.setStringForClob(4, a[1]);
                    opstmt.setString(5, schedulerId);
                    opstmt.setString(6, reportId);
//                    int rows = opstmt.executeUpdate();
                    opstmt.executeUpdate();
                }
                //by Prabal
                if (con != null) {
                    con.close();
                }
            } else {
                PbReturnObject pbro = execSelectSQL("select pu_id from prg_ar_users WHERE pu_email in ('" + emailList + "') and pu_id !=" + userId);
                String query1 = getResourceBundle().getString("insertsnapshot");
                Connection con = ProgenConnection.getInstance().getConnection();
                String snapShotClob = serializer.outputString(document);

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    PreparedStatementProxy opstmt = (PreparedStatementProxy) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setString(2, reportId);
                    opstmt.setString(3, repcustname);
                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
                    opstmt.setString(4, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(5, contextpth1);
                    opstmt.setString(6, userId);
                    String a[] = completeUrl.substring(1).split("/");
                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
                    opstmt.setString(7, a[1]);
                    opstmt.setString(8, fromOption);
                    int rows = opstmt.executeUpdate();
                    //Code modified by Amar to stop creating separate scheduler for each user on march 2,2015
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportId);
//                    opstmt.setString(3, repcustname);
//                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
//                    opstmt.setString(4, snapShotClob);
//                    opstmt.setString(5, contextpth1);
//                    opstmt.setString(6, userId);
//                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
//                    opstmt.setString(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }
                    //end of code

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    JDBC4PreparedStatementWrapper opstmt = (JDBC4PreparedStatementWrapper) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setString(2, reportId);
                    opstmt.setString(3, repcustname);
                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
                    opstmt.setString(4, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(5, contextpth1);
                    opstmt.setString(6, userId);
                    String a[] = completeUrl.substring(1).split("/");
                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
                    opstmt.setString(7, a[1]);
                    opstmt.setString(8, fromOption);
                    int rows = opstmt.executeUpdate();
                    //Code modified by Amar to stop creating separate scheduler for each user on march 2,2015 
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportId);
//                    opstmt.setString(3, repcustname);
//                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
//                    opstmt.setString(4, snapShotClob);
//                    opstmt.setString(5,contextpth1);
//                    opstmt.setString(6, userId);
//                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
//                    opstmt.setString(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }
                    //end of code

                } else {
                    OraclePreparedStatement opstmt;
                    opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
                    opstmt.setString(1, userId);
                    opstmt.setString(2, reportId);
                    opstmt.setString(3, repcustname);
                    opstmt.setStringForClob(4, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                    opstmt.setString(5, contextpth1);
                    opstmt.setString(6, userId);
                    String a[] = completeUrl.substring(1).split("/");
                    opstmt.setStringForClob(7, a[1]);
                    opstmt.setString(8, fromOption);
                    int rows = opstmt.executeUpdate();
                    //Code modified by Amar to stop creating separate scheduler for each user on march 2,2015 
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportId);
//                    opstmt.setString(3, repcustname);
//                    opstmt.setStringForClob(4, serializer.outputString(document));
//                    opstmt.setString(5, contextpth1);
//                    opstmt.setString(6, userId);
//                    opstmt.setStringForClob(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }
                    //end of code by Amar
                }
            }


        } catch (Exception e) {
            logger.error("Exception:", e);
        }

    }

    public Element buildMetaInfo(Element root) {
        MetaInfoChildValues = new String[3];
        MetaInfoChildValues[0] = reportId;
        MetaInfoChildValues[1] = snapshotName;
        Element pMasterEle = new Element("Meta_Data");
        Element child = null;

        for (int i = 0; i < pMasterChildTags.length; i++) {
            child = new Element(pMasterChildTags[i]);
            child.setText(MetaInfoChildValues[i]);
            pMasterEle.addContent(child);
        }
        root.addContent(pMasterEle);

        return root;
    }

    public Element buildParameters(Element root) {

        Element pParameter = new Element("progen_parameter");
        Element child = null;
        child = new Element("url");
        child.setText(completeUrl);

        pParameter.addContent(child);
        String urldets[] = completeUrl.split(";");
        String defaultvalue = "";
        HashMap ParamDefValsHashMap = new HashMap();
        for (int i = 0; i < urldets.length; i++) {
            if (urldets[i].startsWith("CBOARP")) {

                String defkey = urldets[i].split("=")[0];
                String defvalue = urldets[i].split("=")[1];

                ParamDefValsHashMap.put(defkey, defvalue);
            }
        }

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];
        Obj[0] = paramsString;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            Obj[1] = paramString2;
        }
        else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            Obj[1] = paramString2;
        }
        else {
            Obj[1] = paramString1;
        }

        Element pParameterEle = new Element("Parameter_Master");
        Element ppDetailEle = null;
        int count = 1;

        try {
            String repParamDtlsQry = getResourceBundle().getString("getReportParamDetails");
            finalQuery = buildQuery(repParamDtlsQry, Obj);

            retObj = execSelectSQL(finalQuery);
            if (timeDetails != null && timeDetails.size() != 0 && timeDimHashMap != null && timeDimHashMap.size() != 0) {
                ppDetailEle = new Element("Parameter_Detail");
                Obj = new String[11];
                Obj[0] = "Time";
                Obj[1] = "Time";
                Obj[2] = "";
                Obj[3] = "";
                Obj[4] = "";
                Obj[5] = "C";
                Obj[6] = "";
                Obj[7] = String.valueOf((1));
                Obj[8] = null;
                Obj[9] = "N";
                Obj[10] = "";

                for (int j = 0; j < ppDetailChildTags.length; j++) {
                    child = new Element(ppDetailChildTags[j]);
                    child.setText(Obj[j]);
                    ppDetailEle.addContent(child);
                }
                pParameterEle.addContent(ppDetailEle);
                count = 2;
            }

            if (retObj != null && retObj.getRowCount() != 0) {
                dbColumns = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ppDetailEle = new Element("Parameter_Detail");
                    Obj = new String[11];
                    Obj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[3]);
                    Obj[4] = retObj.getFieldValueString(i, dbColumns[4]);
                    Obj[5] = retObj.getFieldValueString(i, dbColumns[5]);
                    Obj[6] = retObj.getFieldValueString(i, dbColumns[6]);
                    Obj[7] = String.valueOf((i + count));
                    defaultvalue = "CBOARP" + retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[8] = String.valueOf(ParamDefValsHashMap.get(defaultvalue));
                    Obj[9] = retObj.getFieldValueString(i, dbColumns[8]);
                    Obj[10] = String.valueOf(reportParameters.get(retObj.getFieldValueString(i, dbColumns[0]))).replace("[", "").replace("]", "");
                    for (int j = 0; j < ppDetailChildTags.length; j++) {
                        child = new Element(ppDetailChildTags[j]);
                        child.setText(Obj[j]);
                        ppDetailEle.addContent(child);
                    }
                    pParameterEle.addContent(ppDetailEle);
                }
                pParameter.addContent(pParameterEle);
            }
            Map<String, List> inMap = operatorFilters.get("IN");
            Map<String, List> notInMap = operatorFilters.get("NOTIN");
            Map<String, List> likeMap = operatorFilters.get("LIKE");
            Map<String, List> notLikeMap = operatorFilters.get("NOTLIKE");
            Gson gson = new Gson();
            Type tarType = new TypeToken<Map<String, List>>() {
            }.getType();
            String inMapStr = gson.toJson(inMap, tarType);
            String notInMapStr = gson.toJson(notInMap, tarType);
            String likeMapStr = gson.toJson(likeMap, tarType);
            String notLikeMapStr = gson.toJson(notLikeMap, tarType);
            child = new Element("IN");
            child.setText(inMapStr);
            pParameter.addContent(child);
            child = new Element("NOTIN");
            child.setText(notInMapStr);
            pParameter.addContent(child);
            child = new Element("LIKE");
            child.setText(likeMapStr);
            pParameter.addContent(child);
            child = new Element("NOTLIKE");
            child.setText(notLikeMapStr);
            pParameter.addContent(child);
            root.addContent(pParameter);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;


    }

    public Element buildTimeDetails(Element root) {
        String[] Obj = null;

        Element pTimeEle = new Element("progen_time");
        Element ptMasterEle = new Element("time_master");
        Element ptDetailsEle = null;
        Element child = null;
        ArrayList timeDetailsFromHashMap = null;

        try {
            if (timeDetails != null && timeDetails.size() != 0) {
                Obj = new String[2];
                Obj[0] = String.valueOf(timeDetails.get(0));
                Obj[1] = String.valueOf(timeDetails.get(1));

                for (int i = 0; i < ptMasterChildTags.length; i++) {
                    child = new Element(ptMasterChildTags[i]);
                    child.setText(Obj[i]);
                    ptMasterEle.addContent(child);
                }
                pTimeEle.addContent(ptMasterEle);
            }
            if (timeDimHashMap != null) {
                Set details = timeDimHashMap.keySet();
                Iterator it = details.iterator();
                while (it.hasNext()) {
                    Obj = new String[7];
                    String key = (String) it.next();

                    timeDetailsFromHashMap = (ArrayList) timeDimHashMap.get(key);


                    Obj[0] = String.valueOf(timeDetailsFromHashMap.get(2));
                    Obj[1] = key;
                    Obj[2] = String.valueOf(timeDetailsFromHashMap.get(3));
                    Obj[3] = String.valueOf(timeDetailsFromHashMap.get(4));
                    Obj[4] = "N";
                    Obj[5] = snapDate;
                    Obj[6] = String.valueOf(timeDetailsFromHashMap.get(0));

                    ptDetailsEle = new Element("timeDetails");
                    // String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue"};

                    for (int i = 0; i < ptDetailsChildTags.length; i++) {
                        child = new Element(ptDetailsChildTags[i]);
                        child.setText(Obj[i]);
                        ptDetailsEle.addContent(child);

                    }
                    pTimeEle.addContent(ptDetailsEle);

                    timeDetailsFromHashMap = null;
                    Obj = null;
                }
            }
            root.addContent(pTimeEle);

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public Element buildQueryDetails(Element root) throws Exception {
        String[] Obj = null;
        Element viewBysEle = new Element("progen_viewbys");
        Element table = new Element("progen_table");
        Element graph = new Element("graphs");
//        Element measuresEle = null;
        Element child = null;
        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] dispcols = null;
        String[] oricols = null;
        String[] rep = null;
        String[] cep = null;
        String[] viewBys = null;
        String[] axis = null;
//        String aggTypeQuery = null;
//        PbReturnObject grppbro = null;
//        ColorGroup colorGroup = null;

        //String graphIds = String.valueOf(GraphHashMap.get("graphIds"));
        boolean isHideTable = container.isHideTable();
        boolean AvgTotalReq = container.getAvgTotalReq();
        boolean GrandTotalReq = container.getGrandTotalReq();
        boolean NetTotalReq = container.getNetTotalReq();
        boolean CatMaxValueReq = container.getCatMaxValueReq();
        boolean CatMinValueReq = container.getCatMinValueReq();
        boolean OverAllMaxValueReq = container.getOverAllMaxValueReq();
        boolean OverAllMinValueReq = container.getOverAllMinValueReq();
        ArrayList DisplayColumns = container.getDisplayColumns();
        ArrayList OriginalColumns = container.getOriginalColumns();
        boolean isHideMsrHeading = container.isHideMsrHeading();
        //Added by Amar on Sep 23,2015
        String crossGTPos = container.getCrosstabGrandTotalDisplayPosition();
        //end of code

        //code to rep elements
        if (REP_Elements != null && REP_Elements.size() != 0) {
            rep = (String[]) REP_Elements.toArray(new String[0]);
            Element REPele = new Element("REP");
            for (int k = 0; k < rep.length; k++) {
                Element REPeleid = new Element("REP_id");
                REPeleid.setText(rep[k]);
                REPele.addContent(REPeleid);
            }
            viewBysEle.addContent(REPele);
        }
        //code to cep elements
        if (CEP_Elements != null && CEP_Elements.size() != 0) {
            cep = (String[]) CEP_Elements.toArray(new String[0]);
            Element CEPele = new Element("CEP");
            for (int k = 0; k < cep.length; k++) {
                Element CEPeleid = new Element("CEP_id");
                CEPeleid.setText(cep[k]);
                CEPele.addContent(CEPeleid);
            }
            viewBysEle.addContent(CEPele);
        }

        //code to add table details
        Element ishideEle = new Element("is_hide_table");
        ishideEle.setText(String.valueOf(isHideTable));
        table.addContent(ishideEle);


        Element showTotaldets = new Element("show_avg");
        showTotaldets.setText(String.valueOf(AvgTotalReq));
        table.addContent(showTotaldets);


        Element showTotaldets1 = new Element("show_grd_total");
        showTotaldets1.setText(String.valueOf(GrandTotalReq));
        table.addContent(showTotaldets1);


        Element showTotaldets2 = new Element("show_sub_total");
        showTotaldets2.setText(String.valueOf(NetTotalReq));
        table.addContent(showTotaldets2);


        Element showTotaldets3 = new Element("show_cat_max");
        showTotaldets3.setText(String.valueOf(CatMaxValueReq));
        table.addContent(showTotaldets3);


        Element showTotaldets4 = new Element("show_cat_min");
        showTotaldets4.setText(String.valueOf(CatMinValueReq));
        table.addContent(showTotaldets4);


        Element showTotaldets5 = new Element("show_max");
        showTotaldets5.setText(String.valueOf(OverAllMaxValueReq));
        table.addContent(showTotaldets5);


        Element showTotaldets6 = new Element("show_min");
        showTotaldets6.setText(String.valueOf(OverAllMinValueReq));
        table.addContent(showTotaldets6);
        //Code added by Amar on Sep 23,2015
        Element showCrossGTPos = new Element("CRGT_POS");
        showCrossGTPos.setText(String.valueOf(crossGTPos));
        table.addContent(showCrossGTPos);
        //end of code

        //Added by Ram 15Feb2016
        Element ishideMsrEle = new Element("Is_hide_msr_Heading");
        ishideMsrEle.setText(String.valueOf(isHideMsrHeading));
        table.addContent(ishideMsrEle);

        //code to dispcols elements
        if (DisplayColumns != null && DisplayColumns.size() != 0) {
            dispcols = (String[]) DisplayColumns.toArray(new String[0]);
            Element dispele = new Element("display_columns");
            for (int k = 0; k < dispcols.length; k++) {
                Element dispeleid = new Element("display_column");
                dispeleid.setText(dispcols[k]);
                dispele.addContent(dispeleid);
            }
            table.addContent(dispele);
        }
        ArrayList displayLabels = container.getDisplayLabels();
        //  
        // code written by swati purpose of crosstab report
        int dimCount = container.getViewByCount();
        ArrayList labelNames = new ArrayList();
        if (displayLabels != null && displayLabels.size() != 0) {
            Element displele = new Element("display_labels");
            Object labelName;
            if (container.isReportCrosstab()) {
                for (int i = 0; i < dimCount; i++) {
                    Element dispeleid = new Element("display_label");
                    dispeleid.setText((String) displayLabels.get(i));
                    displele.addContent(dispeleid);
                }
                for (int i = dimCount; i < displayLabels.size(); i++) {
                    labelNames = (ArrayList) displayLabels.get(i);
                    Element dispeleid = new Element("display_label");
                    dispeleid.setText(labelNames.toString());
                    displele.addContent(dispeleid);
                }
            } else {
                oricols = (String[]) displayLabels.toArray(new String[0]);
                for (int k = 0; k < oricols.length; k++) {
                    Element dispeleid = new Element("display_label");
                    dispeleid.setText(oricols[k]);
                    displele.addContent(dispeleid);
                }
            }
            table.addContent(displele);
        }
        //code to oricols elements
        if (OriginalColumns != null && OriginalColumns.size() != 0) {
            oricols = (String[]) OriginalColumns.toArray(new String[0]);
            Element oriele = new Element("original_columns");
            for (int k = 0; k < oricols.length; k++) {
                Element orieleid = new Element("original_column");
                orieleid.setText(oricols[k]);
                oriele.addContent(orieleid);
            }
            table.addContent(oriele);
        }
        ArrayList displayTypes = container.getDisplayTypes();
        if (displayTypes != null && displayTypes.size() != 0) {
            oricols = (String[]) displayTypes.toArray(new String[0]);
            Element dataTypeslele = new Element("display_types");
            for (int k = 0; k < oricols.length; k++) {
                Element dataTypeseleid = new Element("display_type");
                dataTypeseleid.setText(oricols[k]);
                dataTypeslele.addContent(dataTypeseleid);
            }
            table.addContent(dataTypeslele);
        }
        ArrayList dataTypes = container.getDataTypes();
        if (dataTypes != null && dataTypes.size() != 0) {
            oricols = (String[]) dataTypes.toArray(new String[0]);
            Element dataTypeslele = new Element("data_types");
            for (int k = 0; k < oricols.length; k++) {
                Element dataTypeseleid = new Element("data_type");
                dataTypeseleid.setText(oricols[k]);
                dataTypeslele.addContent(dataTypeseleid);
            }
            table.addContent(dataTypeslele);
        }

        ArrayList links = container.getLinks();
        if (links != null && links.size() != 0) {
            oricols = (String[]) links.toArray(new String[0]);
            Element dataTypeslele = new Element("links");
            for (int k = 0; k < oricols.length; k++) {
                Element dataTypeseleid = new Element("link");
                dataTypeseleid.setText(oricols[k]);
                dataTypeslele.addContent(dataTypeseleid);
            }
            table.addContent(dataTypeslele);
        }
//        ArrayList signs = container.getSigns();
//        if (signs != null && signs.size() != 0) {
//            oricols = (String[]) signs.toArray(new String[0]);
//            Element dataTypeslele = new Element("signs");
//            for (int k = 0; k < oricols.length; k++) {
//                Element dataTypeseleid = new Element("sign");
//                dataTypeseleid.setText(oricols[k]);
//                dataTypeslele.addContent(dataTypeseleid);
//            }
//            table.addContent(dataTypeslele);
//        }
        ArrayList reportQryElementIds = (ArrayList) reportHashMap.get("reportQryElementIds");
        if (reportQryElementIds != null && reportQryElementIds.size() != 0) {
            oricols = (String[]) reportQryElementIds.toArray(new String[0]);
            Element oriele = new Element("report_query_elementsIds");
            for (int k = 0; k < oricols.length; k++) {
                Element orieleid = new Element("report_query_elementsId");
                orieleid.setText(oricols[k]);
                oriele.addContent(orieleid);
            }
            table.addContent(oriele);
        }

        ArrayList reportQryAggregations = (ArrayList) reportHashMap.get("reportQryAggregations");
        if (reportQryAggregations != null && reportQryAggregations.size() != 0) {
            oricols = (String[]) reportQryAggregations.toArray(new String[0]);
            Element oriele = new Element("report_query_aggregations");
            for (int k = 0; k < oricols.length; k++) {
                Element orieleid = new Element("report_query_aggregation");
                orieleid.setText(oricols[k]);
                oriele.addContent(orieleid);
            }
            table.addContent(oriele);
        }


        ArrayList reportQueryColumnNames = (ArrayList) reportHashMap.get("reportQryColNames");
        if (reportQueryColumnNames != null && reportQueryColumnNames.size() != 0) {
            oricols = (String[]) reportQueryColumnNames.toArray(new String[0]);
            Element oriele = new Element("report_query_colnames");
            for (int k = 0; k < oricols.length; k++) {
                Element orieleid = new Element("report_query_colname");
                orieleid.setText(oricols[k]);
                oriele.addContent(orieleid);
            }
            table.addContent(oriele);
        }
//        Document colorDocument;
//        SAXBuilder builder = new SAXBuilder();
        StringBuilder colorBuilder = this.buildColorXml(container);
        String tobBottomBuilder = this.buildTopBottomXml(container);
        String tableFilter = this.buildTableFiltersXml(container);
        table.addContent(colorBuilder.toString());
        table.addContent(tobBottomBuilder);
        table.addContent(tableFilter);
//        colorDocument = builder.build(new ByteArrayInputStream(colorBuilder.toString().getBytes()));


//        colorGroup=container.getColorGroup();
//        ArrayList<ColorCode> colorCodeLst=colorGroup.colorCodeLst;
//        StringBuilder colorGroupXml=new StringBuilder("");
//        colorGroupXml.append("<ColorGrouping>");
//        for(ColorCode colorCode:colorCodeLst){
//            colorGroupXml.append(colorCode.toXml());
//                colorGroupXml.append("<MeasureId>");
//                colorGroupXml.append(colorCode.getMeasure());
//                colorGroupXml.append("</MeasureId>");
//                if(container.isReportCrosstab()){
//                colorGroupXml.append("<CrossTabMeasure>");
//                colorCode.getCrosstabMeasure();
//                colorGroupXml.append("</CrossTabMeasure>");
//
//            }
//        }
//        colorGroupXml.append("</ColorGrouping>");
//
//
//
//        table.addContent(colorGroupXml.toString());
//        

        //code for building color grouping  added by santhosh.k on 26-02-2010
//        HashMap ColorCodeMap = (HashMap) TableHashMap.get("ColorCodeMap");
//        if (ColorCodeMap != null) {
//            String[]  HashMap ColorCodeMap = (HashMap) TableHashMap.get("ColorCodeMap");
//        if (ColorCodeMap != null) {
//            String[] keySet = (String[]) ColorCodeMap.keySet().toArray(new String[0]);
//            String[] colorCodes = null;
//            String[] operators = null;
//            String[] sValues = null;
//            String[] eValues = null;
//            String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
//            String[] strOperatorsInWords = {"less than", "greater than", "less than equal to", "greater than equal to", "equal to", "not equal to", "between"};
//            for (int i = 0; i < keySet.length; i++) {
//                HashMap tempMap = (HashMap) ColorCodeMap.get(keySet[i]);
//                if (tempMap != null) {
//                    Element colorGroupingEle = new Element("color-grouping");
//
//                    colorGroupingEle.setAttribute("disColumnName", keySet[i]);
//                    colorCodes = (String[]) tempMap.get("colorCodes");
//                    operators = (String[]) tempMap.get("operators");
//                    sValues = (String[]) tempMap.get("sValues");
//                    eValues = (String[]) tempMap.get("eValues");
//
//                    Element colorCodesEle = new Element("colorCodes");
//                    Element operatorsEle = new Element("operators");
//                    Element sValuesEle = new Element("sValues");
//                    Element eValuesEle = new Element("eValues");
//
//                    for (int j = 0; j < colorCodes.length; j++) {
//                        Element colorCodeEle = new Element("color-code");
//                        colorCodeEle.setText(colorCodes[j]);
//                        colorCodesEle.addContent(colorCodeEle);
//                    }
//                    for (int j = 0; j < operators.length; j++) {
//                      HashMap ColorCodeMap = (HashMap) TableHashMap.get("ColorCodeMap");
//        if (ColorCodeMap != null) {
//            String[] keySet = (String[]) ColorCodeMap.keySet().toArray(new String[0]);
//            String[] colorCodes = null;
//            String[] operators = null;
//            String[] sValues = null;
//            String[] eValues = null;
//            String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
//            String[] strOperatorsInWords = {"less than", "greater than", "less than equal to", "greater than equal to", "equal to", "not equal to", "between"};
//            for (int i = 0; i < keySet.length; i++) {
//                HashMap tempMap = (HashMap) ColorCodeMap.get(keySet[i]);
//                if (tempMap != null) {
//                    Element colorGroupingEle = new Element("color-grouping");
//
//                    colorGroupingEle.setAttribute("disColumnName", keySet[i]);
//                    colorCodes = (String[]) tempMap.get("colorCodes");
//                    operators = (String[]) tempMap.get("operators");
//                    sValues = (String[]) tempMap.get("sValues");
//                    eValues = (String[]) tempMap.get("eValues");
//
//                    Element colorCodesEle = new Element("colorCodes");
//                    Element operatorsEle = new Element("operators");
//                    Element sValuesEle = new Element("sValues");
//                    Element eValuesEle = new Element("eValues");
//
//                    for (int j = 0; j < colorCodes.length; j++) {
//                        Element colorCodeEle = new Element("color-code");
//                        colorCodeEle.setText(colorCodes[j]);
//                        colorCodesEle.addContent(colorCodeEle);
//                    }
//                    for (int j = 0; j < operators.length; j++) {
//                        Element operatorEle = new Element("operator");
//                        for (int k = 0; k < strOperators.length; k++) {
//                            if (operators[j].equalsIgnoreCase(strOperators[k])) {
//                                operatorEle.setText(strOperatorsInWords[j]);
//                                break;
//                            }
//                        }
//                        operatorsEle.addContent(operatorEle);
//                    }
//                    for (int j = 0; j < sValues.length; j++) {
//                        Element sValueEle = new Element("sValue");
//                        sValueEle.setText(sValues[j]);
//                        sValuesEle.addContent(sValueEle);
//                    }
//                    for (int j = 0; j < eValues.length; j++) {
//                        Element eValueEle = new Element("eValue");
//                        eValueEle.setText(eValues[j]);
//                        eValuesEle.addContent(eValueEle);
//                    }
//                    colorGroupingEle.addContent(colorCodesEle);
//                    colorGroupingEle.addContent(operatorsEle);
//                    colorGroupingEle.addContent(sValuesEle);
//                    colorGroupingEle.addContent(eValuesEle);//adding to color grouping tag
//
//                    table.addContent(colorGroupingEle);//adding to parent tag
//                }   Element operatorEle = new Element("operator");
//                        for (int k = 0; k < strOperators.length; k++) {
//                            if (operators[j].equalsIgnoreCase(strOperators[k])) {
//                                operatorEle.setText(strOperatorsInWords[j]);
//                                break;
//                            }
//                        }
//                        operatorsEle.addContent(operatorEle);
//                    }
//                    for (int j = 0; j < sValues.length; j++) {
//                        Element sValueEle = new Element("sValue");
//                        sValueEle.setText(sValues[j]);
//                        sValuesEle.addContent(sValueEle);
//                    }
//                    for (int j = 0; j < eValues.length; j++) {
//                        Element eValueEle = new Element("eValue");
//                        eValueEle.setText(eValues[j]);
//                        eValuesEle.addContent(eValueEle);
//                    }
//                    colorGroupingEle.addContent(colorCodesEle);
//                    colorGroupingEle.addContent(operatorsEle);
//                    colorGroupingEle.addContent(sValuesEle);
//                    colorGroupingEle.addContent(eValuesEle);//adding to color grouping tag
//
//                    table.addContent(colorGroupingEle);//adding to parent tag
//                }keySet = (String[]) ColorCodeMap.keySet().toArray(new String[0]);
//            String[] colorCodes = null;
//            String[] operators = null;
//            String[] sValues = null;
//            String[] eValues = null;
//            String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
//            String[] strOperatorsInWords = {"less than", "greater than", "less than equal to", "greater than equal to", "equal to", "not equal to", "between"};
//            for (int i = 0; i < keySet.length; i++) {
//                HashMap tempMap = (HashMap) ColorCodeMap.get(keySet[i]);
//                if (tempMap != null) {
//                    Element colorGroupingEle = new Element("color-grouping");
//
//                    colorGroupingEle.setAttribute("disColumnName", keySet[i]);
//                    colorCodes = (String[]) tempMap.get("colorCodes");
//                    operators = (String[]) tempMap.get("operators");
//                    sValues = (String[]) tempMap.get("sValues");
//                    eValues = (String[]) tempMap.get("eValues");
//
//                    Element colorCodesEle = new Element("colorCodes");
//                    Element operatorsEle = new Element("operators");
//                    Element sValuesEle = new Element("sValues");
//                    Element eValuesEle = new Element("eValues");
//
//                    for (int j = 0; j < colorCodes.length; j++) {
//                        Element colorCodeEle = new Element("color-code");
//                        colorCodeEle.setText(colorCodes[j]);
//                        colorCodesEle.addContent(colorCodeEle);
//                    }
//                    for (int j = 0; j < operators.length; j++) {
//                        Element operatorEle = new Element("operator");
//                        for (int k = 0; k < strOperators.length; k++) {
//                            if (operators[j].equalsIgnoreCase(strOperators[k])) {
//                                operatorEle.setText(strOperatorsInWords[j]);
//                                break;
//                            }
//                        }
//                        operatorsEle.addContent(operatorEle);
//                    }
//                    for (int j = 0; j < sValues.length; j++) {
//                        Element sValueEle = new Element("sValue");
//                        sValueEle.setText(sValues[j]);
//                        sValuesEle.addContent(sValueEle);
//                    }
//                    for (int j = 0; j < eValues.length; j++) {
//                        Element eValueEle = new Element("eValue");
//                        eValueEle.setText(eValues[j]);
//                        eValuesEle.addContent(eValueEle);
//                    }
//                    colorGroupingEle.addContent(colorCodesEle);
//                    colorGroupingEle.addContent(operatorsEle);
//                    colorGroupingEle.addContent(sValuesEle);
//                    colorGroupingEle.addContent(eValuesEle);//adding to color grouping tag
//
//                    table.addContent(colorGroupingEle);//adding to parent tag
//                }

//            }
        //       }
        //end of code for building color grouping  added by santhosh.k on 26-02-2010

        //code for building Parameter grouping  added by santhosh.k on 10-03-2010 for the purpose of Parameter Grouping while saving as SnapShot
        HashMap ParameterGroupingMap = container.getParameterGroupAnalysisHashMap();

        ////.println("ParameterGroupingMap is " + ParameterGroupingMap);
        if (ParameterGroupingMap != null) {
            Element ParamGroupingsEle = new Element("Parameter-Groupings");

            String[] ParamElementIdsList = (String[]) new TreeSet(ParameterGroupingMap.keySet()).toArray(new String[0]);
            for (int i = 0; i < ParamElementIdsList.length; i++) {
                if (ParamElementIdsList[i].indexOf("_GroupName") == -1) {
                    Element ParameterGroupingEle = new Element("Parameter-Grouping");
                    ParameterGroupingEle.setAttribute("elementId", ParamElementIdsList[i]);
                    ParameterGroupingEle.setAttribute("groupName", String.valueOf(ParameterGroupingMap.get(ParamElementIdsList[i] + "_GroupName")));

                    HashMap SingleParameterGroupingMap = (HashMap) ParameterGroupingMap.get(ParamElementIdsList[i]);

                    ////.println("SingleParameterGroupingMap is "+SingleParameterGroupingMap);
                    String[] SingleParameterKeys = (String[]) new TreeSet(SingleParameterGroupingMap.keySet()).toArray(new String[0]);

                    for (int j = 0; j < SingleParameterKeys.length; j++) {
                        Element ParameterGroupEle = new Element("Parameter-Group");//building each  <Parameter-Group> tags
                        ParameterGroupEle.setAttribute("name", SingleParameterKeys[j]);//setting attributes and their values
                        ParameterGroupEle.setText(SingleParameterGroupingMap.get(SingleParameterKeys[j]).toString());//setting  <Parameter-Group> value
                        ParameterGroupingEle.addContent(ParameterGroupEle);//ading to <Parameter-Grouping> tags
                    }
                    ParamGroupingsEle.addContent(ParameterGroupingEle);
                }
            }
            table.addContent(ParamGroupingsEle);
        }
        //end of code for building Parameter grouping  added by santhosh.k on 10-03-2010
        // Code added by amar to store Hybrid Report Details
        if (container.isSummarizedMeasuresEnabled()) {
            Element hybRep = new Element("HybridReport");
            Element isSummEnable = new Element("Summarized-Enable");
            isSummEnable.setText(String.valueOf(container.isSummarizedMeasuresEnabled()));
            hybRep.addContent(isSummEnable);
            if (container.getSummerizedTableHashMap() != null && !container.getSummerizedTableHashMap().isEmpty()) {
                Element tabHashMap = new Element("SummTabHashMap");
                //String TableHashMap = container.getSummerizedTableHashMap().toString();
                Gson gson = new Gson();
                HashMap<String, ArrayList<String>> tableHashMap = container.getSummerizedTableHashMap();
                Type tarType = new TypeToken<Map<String, ArrayList>>() {
                }.getType();
                String inMapStr = gson.toJson(tableHashMap, tarType);
                tabHashMap.setText(inMapStr);
                hybRep.addContent(tabHashMap);
            }
            table.addContent(hybRep);
        } else {
            Element hybRep = new Element("HybridReport");
            Element isSummEnable = new Element("Summarized-Enable");
            isSummEnable.setText(String.valueOf(container.isSummarizedMeasuresEnabled()));
            hybRep.addContent(isSummEnable);
            table.addContent(hybRep);
        }
        // end of code for store Hybrid Report Details
        if (!this.fromOption.equalsIgnoreCase("dynamicHeadline") || !this.fromOption.equalsIgnoreCase("scheduler")) {
            //code for graphs
            if (no_of_graphs != null && no_of_graphs.length != 0) {
                for (int i = 0; i < no_of_graphs.length; i++) {
                    Element pgMasterEle = new Element("graph");
                    HashMap graphDetails = (HashMap) GraphHashMap.get(no_of_graphs[i]);
                    Obj = new String[22];
                    Obj[0] = String.valueOf(graphDetails.get("graphId"));
                    Obj[1] = String.valueOf(graphDetails.get("graphName"));
                    Obj[2] = String.valueOf(graphDetails.get("graphWidth"));
                    Obj[3] = String.valueOf(graphDetails.get("graphHeight"));
                    Obj[4] = String.valueOf(graphDetails.get("graphClassName"));
                    Obj[5] = String.valueOf(graphDetails.get("graphTypeName"));
                    Obj[6] = String.valueOf(graphDetails.get("graphSize"));
                    Obj[7] = String.valueOf(graphDetails.get("SwapColumn"));
                    Obj[8] = String.valueOf(graphDetails.get("graphLegend"));
                    Obj[9] = String.valueOf(graphDetails.get("graphLegendLoc"));
                    Obj[10] = String.valueOf(graphDetails.get("graphshowX"));
                    Obj[11] = String.valueOf(graphDetails.get("graphshowY"));
                    Obj[12] = String.valueOf(graphDetails.get("graphLYaxislabel"));
                    Obj[13] = String.valueOf(graphDetails.get("graphRYaxislabel"));
                    Obj[14] = String.valueOf(graphDetails.get("graphDrill"));
                    Obj[15] = String.valueOf(graphDetails.get("graphBcolor"));
                    Obj[16] = String.valueOf(graphDetails.get("graphFcolor"));
                    Obj[17] = String.valueOf(graphDetails.get("graphData"));
                    Obj[18] = String.valueOf(graphDetails.get("showGT"));
                    Obj[19] = String.valueOf(graphDetails.get("measureNamePosition"));
                    Obj[20] = String.valueOf(graphDetails.get("graphDisplayRows"));
                    Obj[20] = String.valueOf(graphDetails.get("nbrFormat"));

                    for (int j = 0; j < pgMasterChildTags.length; j++) {
                        child = new Element(pgMasterChildTags[j]);
                        child.setText(Obj[j]);
                        pgMasterEle.addContent(child);
                    }
                    barChartColumnNames = (String[]) graphDetails.get("barChartColumnNames");
                    barChartColumnTitles = (String[]) graphDetails.get("barChartColumnTitles");
                    String[] pieChartColumns = (String[]) graphDetails.get("pieChartColumns");
                    String[] barChartColumnNames1 = (String[]) graphDetails.get("barChartColumnNames1");
                    String[] barChartColumnTitles1 = (String[]) graphDetails.get("barChartColumnTitles1");
                    String[] barChartColumnNames2 = (String[]) graphDetails.get("barChartColumnNames2");
                    String[] barChartColumnTitles2 = (String[]) graphDetails.get("barChartColumnTitles2");
                    axis = (String[]) graphDetails.get("axis");
                    String[] viewByElementIds = (String[]) graphDetails.get("viewByElementIds");

                    if (viewByElementIds != null) {
                        Element viewByElementIdsele = new Element("viewByElementIds");
                        for (int k = 0; k < viewByElementIds.length; k++) {
                            Element viewByElementIdele = new Element("viewByElementId");
                            viewByElementIdele.setText(viewByElementIds[k]);
                            viewByElementIdsele.addContent(viewByElementIdele);
                        }
                        pgMasterEle.addContent(viewByElementIdsele);
                    }
                    if (axis != null) {
                        Element axissele = new Element("axiss");
                        for (int k = 0; k < axis.length; k++) {
                            Element axisele = new Element("axis");
                            axisele.setText(axis[k]);
                            axissele.addContent(axisele);
                        }
                        pgMasterEle.addContent(axissele);
                    }


                    if (barChartColumnNames != null) {
                        Element barChartColumnNamesele = new Element("barChartColumnNames");
                        for (int k = 0; k < barChartColumnNames.length; k++) {
                            Element barChartColumnNameseleid = new Element("barChartColumnName");
                            barChartColumnNameseleid.setText(barChartColumnNames[k]);
                            barChartColumnNamesele.addContent(barChartColumnNameseleid);
                        }
                        pgMasterEle.addContent(barChartColumnNamesele);
                    }
                    if (barChartColumnTitles != null) {
                        Element barChartColumnTitlessele = new Element("barChartColumnTitles");
                        for (int k = 0; k < barChartColumnTitles.length; k++) {
                            Element barChartColumnTitleseleid = new Element("barChartColumnTitle");
                            barChartColumnTitleseleid.setText(barChartColumnTitles[k]);
                            barChartColumnTitlessele.addContent(barChartColumnTitleseleid);
                        }
                        pgMasterEle.addContent(barChartColumnTitlessele);
                    }
                    if (pieChartColumns != null) {
                        Element pieChartColumnssele = new Element("pieChartColumns");
                        for (int k = 0; k < pieChartColumns.length; k++) {
                            Element pieChartColumnseleid = new Element("pieChartColumns");
                            pieChartColumnseleid.setText(pieChartColumns[k]);
                            pieChartColumnssele.addContent(pieChartColumnseleid);
                        }
                        pgMasterEle.addContent(pieChartColumnssele);
                    }
                    if (barChartColumnNames1 != null) {
                        if (barChartColumnNames1.length > 0) {
                            Element barChartColumnNames1ele = new Element("barChartColumnNames1");
                            for (int k = 0; k < barChartColumnNames1.length; k++) {
                                Element barChartColumnNames1eleid = new Element("barChartColumnName1");
                                barChartColumnNames1eleid.setText(barChartColumnNames1[k]);
                                barChartColumnNames1ele.addContent(barChartColumnNames1eleid);
                            }
                            pgMasterEle.addContent(barChartColumnNames1ele);
                        }
                    }
                    if (barChartColumnTitles1 != null) {
                        if (barChartColumnTitles1.length > 0) {
                            Element barChartColumnTitles1sele = new Element("barChartColumnTitles1");
                            for (int k = 0; k < barChartColumnTitles1.length; k++) {
                                Element barChartColumnTitles1eleid = new Element("barChartColumnTitle1");
                                barChartColumnTitles1eleid.setText(barChartColumnTitles1[k]);
                                barChartColumnTitles1sele.addContent(barChartColumnTitles1eleid);
                            }
                            pgMasterEle.addContent(barChartColumnTitles1sele);
                        }
                    }
                    if (barChartColumnNames2 != null) {
                        if (barChartColumnNames2.length > 0) {
                            Element barChartColumnNames2ele = new Element("barChartColumnNames2");
                            for (int k = 0; k < barChartColumnNames2.length; k++) {
                                Element barChartColumnNames2eleid = new Element("barChartColumnName2");
                                barChartColumnNames2eleid.setText(barChartColumnNames2[k]);
                                barChartColumnNames2ele.addContent(barChartColumnNames2eleid);
                            }
                            pgMasterEle.addContent(barChartColumnNames2ele);
                        }
                    }
                    if (barChartColumnTitles2 != null) {
                        if (barChartColumnTitles2.length > 0) {
                            Element barChartColumnTitles2sele = new Element("barChartColumnTitles2");
                            for (int k = 0; k < barChartColumnTitles2.length; k++) {
                                Element barChartColumnTitles2eleid = new Element("barChartColumnTitle2");
                                barChartColumnTitles2eleid.setText(barChartColumnTitles2[k]);
                                barChartColumnTitles2sele.addContent(barChartColumnTitles2eleid);
                            }
                            pgMasterEle.addContent(barChartColumnTitles2sele);
                        }
                    }
                    ArrayList rowValuesList = (ArrayList) graphDetails.get("RowValuesList");
                    if (rowValuesList != null) {
                        if (rowValuesList.size() > 0) {
                            Element rowValuesListsele = new Element("rowValuesList");
                            for (int k = 0; k < barChartColumnTitles.length; k++) {
                                Element rowValuesListeleid = new Element("rowValue");
                                rowValuesListeleid.setText(String.valueOf(rowValuesList.get(k)));
                                rowValuesListsele.addContent(rowValuesListeleid);
                            }
                            pgMasterEle.addContent(rowValuesListsele);
                        }
                    }
                    graph.addContent(pgMasterEle);
                }
            }
        }
        root.addContent(viewBysEle);
        root.addContent(table);
        root.addContent(graph);
        return root;
    }

    public String deleteSnapshot(String snapshotid) {
        String status = "failure";
        try {
            String deleteqry = getResourceBundle().getString("deletesnapshot");
            Object[] delobj = new Object[1];
            delobj[0] = snapshotid;
            String builddeleteqry = buildQuery(deleteqry, delobj);
            PbDb pbdb = new PbDb();
            pbdb.execModifySQL(builddeleteqry);
            status = "success";
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    //code written by swathi purpose of saving searchfilter and sortfilter in DB
    public String buildTableFiltersXml(Container container) {
        StringBuilder builder = new StringBuilder("");
        ArrayList<String> srchColumns = new ArrayList<String>();
        ArrayList<String> srchCondition = new ArrayList<String>();
        ArrayList<Object> srchValue = new ArrayList<Object>();
        srchColumns = container.getSearchColumns();
        srchCondition = container.getSearchConditions();
        srchValue = container.getSearchValues();
//        builder.append("<TableProperty>");
        builder.append("<TableSearch>");
        int index = 0;
        int srchIndex = 0;
        if (!srchColumns.isEmpty()) {
//            builder.append("<TableSearch>");
            for (String srchColumn : srchColumns) {
                builder.append("<Search>");
                builder.append("<Column>");
                builder.append(srchColumn);
                builder.append("</Column>");
                builder.append("<Condition>");
                builder.append(srchCondition.get(srchIndex));
                builder.append("</Condition>");
                builder.append("<Value>");
                if (srchCondition.get(srchIndex).equalsIgnoreCase("BT")) {
                    ArrayList<BigDecimal> betweenVal = (ArrayList) srchValue.get(srchIndex);
                    builder.append(betweenVal.get(0)).append(',').append(betweenVal.get(1));
                } else {
                    builder.append(srchValue.get(srchIndex).toString());
                }

                builder.append("</Value>");
                builder.append("</Search>");
                srchIndex++;
            }
//            builder.append("</TableSearch>");
        }
        builder.append("</TableSearch>");
        ArrayList<String> sortColumns = new ArrayList<String>(); //elementids in which sort happens
        char[] sortTypes;
        sortColumns = container.getSortColumns();
        sortTypes = container.getSortTypes();
        builder.append("<Sort>");
        if (!sortColumns.isEmpty()) {
            for (String sortColumn : sortColumns) {
                builder.append("<SortColumn>");
                builder.append("<ColumnName>");
                builder.append(sortColumn);
                builder.append("</ColumnName>");
                builder.append("<SortType>");
                builder.append(sortTypes[index]);
                builder.append("</SortType>");
                builder.append("</SortColumn>");
                index++;
            }
        }
        builder.append("</Sort>");
//         builder.append("</TableProperty>");
        return builder.toString();
    }

    public String buildTopBottomXml(Container container) {
        StringBuilder builder = new StringBuilder("");

        builder.append("<TopBottom>");

        ArrayList<String> sortCols = container.getSortColumns();
        if (sortCols != null && !sortCols.isEmpty()) {
            builder.append("<NoOfRows>").append(container.getTopBottomCount()).append("</NoOfRows>");
            builder.append("<TopBottomMode>").append(container.getTopBottomMode()).append("</TopBottomMode>");
            builder.append("<TopBottomType>").append(container.getTopBottomType()).append("</TopBottomType>");
            builder.append("<MeasColumn>").append(sortCols.get(0)).append("</MeasColumn>");
        }
        builder.append("</TopBottom>");
        return builder.toString();

    }

    public StringBuilder buildColorXml(Container container) {
        StringBuilder builder = new StringBuilder("");
        PbReportViewerBD repBd = new PbReportViewerBD();
        ColorGroup colorGroup = container.getColorGroup();
        ReportParameter repParameter = container.getReportParameter();
        Set<String> rowViewBys = repParameter.getRowViewByForParameter();
        Set<String> colViewBys = repParameter.getColViewByForParameter();

        ArrayList<ColorCode> colorCodeLst = new ArrayList<ColorCode>();
        ArrayList measureList = container.getTableDisplayMeasures();
        int originalMeasIndex;

        int viewByCount = (container.getViewByCount());
        ArrayList<String> displayCols = container.getDisplayColumns();


        for (int i = viewByCount; i < displayCols.size(); i++) {
            String measure = "";
            String colName = "";
            if (container.isReportCrosstab()) {
                originalMeasIndex = repBd.findMeasureIndexInCT(container, displayCols.get(i));
                originalMeasIndex = originalMeasIndex - (container.getViewByCount());
                if (measureList != null && !measureList.isEmpty()) {
                    measure = (String) measureList.get(originalMeasIndex);
                }
                if (container.getColumnViewByName().equalsIgnoreCase("Ddate")) {
                    colName = ((PbReturnObject) container.getRetObj()).crosstabMeasureId.get(displayCols.get(i));
                }
            }
            if (container.isReportCrosstab() && container.getColumnViewByName().equalsIgnoreCase("Ddate") && colorGroup.isColorCodePresent(colName, repParameter, measure)) {
                ColorCode colorCode = colorGroup.getColorCode(colName, repParameter, measure);
                colorCodeLst.add(colorCode);
            } else if (colorGroup.isColorCodePresent(displayCols.get(i), repParameter, measure)) {
                ColorCode colorCode = colorGroup.getColorCode(displayCols.get(i), repParameter, measure);
                colorCodeLst.add(colorCode);
            }
        }

        builder.append("<ColorGroup>");
        for (ColorCode colorCode : colorCodeLst) {
//            if(colorGroup.isColorCodePresent(colorCode.getMeasure(),repParameter,colorCode.getCrosstabMeasure())){
            builder.append("<Group>");
            builder.append("<Measure>");
            builder.append(colorCode.getMeasure());
            builder.append("</Measure>");
            builder.append("<CrossTabMeasure>");
            builder.append(colorCode.getCrosstabMeasure());
            builder.append("</CrossTabMeasure>");
            builder.append(colorCode.toXml());
            builder.append("</Group>");
//            }
        }

        if (!colorCodeLst.isEmpty()) {
            builder.append(repParameter.toXml());
            builder.append("<RowViewBys>");
            for (String rowViewBy : rowViewBys) {
                builder.append("<RowViewBy>");
                builder.append(rowViewBy);
                builder.append("</RowViewBy>");

            }
            builder.append("</RowViewBys>");
            builder.append("<ColViewBys>");
            for (String colViewBy : colViewBys) {
                builder.append("<ColViewBy>");
                builder.append(colViewBy);
                builder.append("</ColViewBy>");
            }
            builder.append("</ColViewBys>");
        }
        builder.append("<Noofdays>");
        builder.append(container.getNoOfDays());
        builder.append("</Noofdays>");
        builder.append("</ColorGroup>");
//        System.out.println("builder--" + builder);
        return builder;
    }

    public String getReportHeadlines() {
        String sqlQuery = getResourceBundle().getString("getReportHeadlines");
        PbReturnObject retObj = new PbReturnObject();
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<table id='tablesorter' class='tablesorter'  style='border-collapse: collapse;'><thead><th colspan='2' class='myhead'>Headlines</th></thead><tbody>");
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuilder.append("<tr id='").append(retObj.getFieldValueString(i, "PRG_PERSONALIZED_ID")).append("'><td class='collapsible' rowspan='2' width='10px'><a class='collapsed' id='").append(retObj.getFieldValueString(i, "PRG_PERSONALIZED_ID")).append(",").append(retObj.getFieldValueString(i, "PRG_REPORT_ID")).append("' onclick='getHeadlineData(this.id)'>");
                    tableBuilder.append("</a></td><td ><font color='' class='headlinestyle'>");

                    tableBuilder.append(retObj.getFieldValueString(i, "PRG_REPORT_HEADLINE")).append("</font></td></tr>");
                    tableBuilder.append("<tr class='expand-child'><td style='display:none' colspan='2'><div id='").append(retObj.getFieldValueString(i, "PRG_PERSONALIZED_ID")).append("div");
                    tableBuilder.append("'></div>");
                    tableBuilder.append("<div id='").append(retObj.getFieldValueString(i, "PRG_PERSONALIZED_ID")).append("prgBar").append("'></div>");
                    tableBuilder.append("</td></tr>");
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        tableBuilder.append("</tbody></table>");
        return tableBuilder.toString();
    }

    public Container getReturnObjectForHeadlines(String headlineId, Container container) {
        PbReportViewerDAO reportViewerDAO = new PbReportViewerDAO();
        Clob clob = null;
        ArrayList reportQueryAggregations = new ArrayList();
        ArrayList reportQryElementIds = new ArrayList();
        ArrayList reportQueryColumnNames = new ArrayList();
        ArrayList rowviewByValues = new ArrayList();
        ArrayList colviewByValues = new ArrayList();
        LinkedHashMap params = new LinkedHashMap();

        LinkedHashMap timeDetailsMap = new LinkedHashMap();
        ArrayList timeDetailsArray = new ArrayList();
        SAXBuilder builder = new SAXBuilder();
        pbXmlUtilities xmUtil = new pbXmlUtilities();
        Document document = null;
        Element root = null;
        ProgenParam pParam = new ProgenParam();
        PbReturnObject headlineObj = new PbReturnObject();
        try {
            clob = reportViewerDAO.readSnapShotXML(container, headlineId);


//        ProgenParam pParam = new ProgenParam();



            document = builder.build(clob.getCharacterStream());
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        root = document.getRootElement();
        if (root.getChild("progen_parameter") != null) {
            List progenParamLst = root.getChildren("progen_parameter");
//            Element viewEle = null;
//            List urlList=null;
//            Element urlEle=null;
            Element progenParamEle = (Element) progenParamLst.get(0);
            List paramMasterLst = progenParamEle.getChildren("Parameter_Master");
            Element paramMasterEle = (Element) paramMasterLst.get(0);
            List paramDetailLst = paramMasterEle.getChildren("Parameter_Detail");
            Element paramDetailEle;
            ArrayList<String> values;
            String elementId;
            String elementName = "";
            for (int i = 0; i < paramDetailLst.size(); i++) {
                paramDetailEle = (Element) paramDetailLst.get(i);
                values = new ArrayList<String>();
                elementId = xmUtil.getXmlTagValue(paramDetailEle, "element_id");
                values.add(xmUtil.getXmlTagValue(paramDetailEle, "defaultValue"));
                elementName = xmUtil.getXmlTagValue(paramDetailEle, "paramDispName");
                if (!"Time".equalsIgnoreCase(elementId)) {
                    params.put(xmUtil.getXmlTagValue(paramDetailEle, "element_id"), xmUtil.getXmlTagValue(paramDetailEle, "defaultValue"));

//
                }
            }
        }
        if (root.getChild("progen_viewbys") != null) {
            List viewByLst = root.getChildren("progen_viewbys");
            Element viewByElements = null;
            List rowEdgeParamList = null;
            Element repIdEle = null;
            List list;
            Element ele;
            ArrayList<String> rowViewByLst = new ArrayList<String>();
            for (int i = 0; i < viewByLst.size(); i++) {
                viewByElements = (Element) viewByLst.get(i);
                rowEdgeParamList = viewByElements.getChildren("REP");
                repIdEle = (Element) rowEdgeParamList.get(0);
                list = repIdEle.getChildren("REP_id");
                for (int j = 0; j < list.size(); j++) {
                    repIdEle = (Element) list.get(j);
                    rowViewByLst.add(repIdEle.getText());
                }
            }
            rowviewByValues = rowViewByLst;

        }

        if (root.getChild("progen_table") != null) {
            List tables = root.getChildren("progen_table");//Only one row as of now
            Element tableEle = (Element) tables.get(0);
            if (tableEle.getChild("report_query_elementsIds") != null) {
                List report_query_elementsList = ((Element) tableEle.getChildren("report_query_elementsIds").get(0)).getChildren("report_query_elementsId");

                for (int k = 0; k < report_query_elementsList.size(); k++) {
                    reportQryElementIds.add(((Element) report_query_elementsList.get(k)).getText());
                }


            }
            if (tableEle.getChild("report_query_aggregations") != null) {
                List report_query_aggregationsList = ((Element) tableEle.getChildren("report_query_aggregations").get(0)).getChildren("report_query_aggregation");

                for (int k = 0; k < report_query_aggregationsList.size(); k++) {
                    reportQueryAggregations.add(((Element) report_query_aggregationsList.get(k)).getText());
                }

            }
            if (tableEle.getChild("report_query_colnames") != null) {
                //((Element) tableEle.getChildren("report_query_colnames").get(0)).getAttributeValue("");
                //
                List report_query_colnamesList = ((Element) tableEle.getChildren("report_query_colnames").get(0)).getChildren("report_query_colname");

                for (int k = 0; k < report_query_colnamesList.size(); k++) {
                    reportQueryColumnNames.add(((Element) report_query_colnamesList.get(k)).getText());
                }
            }


            if (tableEle.getChild("TopBottom") != null) {
                List TopBottomList = null;
                List TopBottomTypeList = null;
                Element TopBottomEle = null;

                TopBottomList = tableEle.getChildren("TopBottom");

                TopBottomEle = (Element) TopBottomList.get(0);
                TopBottomTypeList = TopBottomEle.getChildren("TopBottomType");
                if (TopBottomTypeList != null && TopBottomTypeList.size() > 0) {
                    Element TopBottomTypeEle = (Element) TopBottomTypeList.get(0);
                    List TopBottomModeList = TopBottomEle.getChildren("TopBottomMode");
                    Element TopBottomModeEle = (Element) TopBottomModeList.get(0);
                    List NoOfRowsList = TopBottomEle.getChildren("NoOfRows");
                    Element NoOfRowsEle = (Element) NoOfRowsList.get(0);
                    List MeasColumnList = TopBottomEle.getChildren("MeasColumn");
                    Element MeasColumnEle = (Element) MeasColumnList.get(0);
                    container.setTopBottomColumn(TopBottomTypeEle.getText(), TopBottomModeEle.getText(), Integer.parseInt(NoOfRowsEle.getText()));
                    container.setSortColumn(MeasColumnEle.getText(), SortOrder.DESCENDING);
                    container.setSortColumn(MeasColumnEle.getText(), "1");
                    container.setSortColumnTopBottom(MeasColumnEle.getText(), "1");
                }


            }

            /*
             * Time Processing starts
             */

            List row = root.getChildren("progen_time");//Only one row as of now

            /*
             * Start of Processing of parameters
             */
            for (int i = 0; i < row.size(); i++) {//Loop for section two under portlet
                Element Companyname = (Element) row.get(i);


                List timeMasterRow = Companyname.getChildren("time_master");
                for (int j = 0; j < timeMasterRow.size(); j++) {
                    Element paramElement = (Element) timeMasterRow.get(j);
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeLevel"));
                    timeDetailsArray.add(xmUtil.getXmlTagValue(paramElement, "timeType"));
                }

                List timeDetailsRow = Companyname.getChildren("timeDetails");
                for (int j = 0; j < timeDetailsRow.size(); j++) {
                    ArrayList timeInfo = new ArrayList();
                    Element paramElement1 = (Element) timeDetailsRow.get(j);
                    String temp = xmUtil.getXmlTagValue(paramElement1, "timeColType");
                    // // ////.println("temp==" + temp);
                    String currVal = xmUtil.getXmlTagValue(paramElement1, "defaultValue");
                    String realDate = xmUtil.getXmlTagValue(paramElement1, "realDate");
                    if (currVal.equalsIgnoreCase("sysdate")) {
                        currVal = "Current Date";
                    }

                    if (currVal == null || "".equalsIgnoreCase(currVal) || currVal.equalsIgnoreCase("Current Date")) {

                        {//default Value
                            if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                                timeInfo.add(pParam.getdateforpage());
                            } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                                timeInfo.add(pParam.getmonthforpage());
                            } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                                timeInfo.add(pParam.getYearforpage());
                            } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                                timeInfo.add("Month");
                            } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                                timeInfo.add("Last Period");
                            }

                        }

                    } else {
                        if (temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1") || temp.equalsIgnoreCase("AS_OF_DATE") || temp.equalsIgnoreCase("AS_OF_DATE1")) {
                            timeInfo.add(realDate);
                        } else if (temp.equalsIgnoreCase("AS_OF_MONTH") || temp.equalsIgnoreCase("AS_OF_MONTH1")) {
                            timeInfo.add(realDate);
                        } else if (temp.equalsIgnoreCase("AS_OF_YEAR") || temp.equalsIgnoreCase("AS_OF_YEAR1")) {
                            timeInfo.add(realDate);
                        } else if (temp.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                            timeInfo.add("Month");
                        } else if (temp.equalsIgnoreCase("PRG_COMPARE")) {
                            timeInfo.add("Last Period");
                        }
                    }

                    timeInfo.add("CBO_" + temp);
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColName"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeColSeq"));
                    timeInfo.add(xmUtil.getXmlTagValue(paramElement1, "timeFormSeq"));

                    if (currVal == null || "".equalsIgnoreCase(currVal) || currVal.equalsIgnoreCase("Current Date")) {
                        timeInfo.add(currVal);
                    } else {
                        timeInfo.add(realDate);
                    }

                    timeInfo.add(temp);

                    timeDetailsMap.put(timeInfo.get(6), timeInfo);


                }
                // // ////.println("timeDetailsMap===" + timeDetailsMap);
                ArrayList timeInfo = new ArrayList();
                if (timeDetailsArray.get(0).toString().equalsIgnoreCase("Year") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_YEAR_CMP")) {
                    timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_YEAR");
                    timeDetailsArray.add(timeInfo.get(0));

                    timeInfo =
                            (ArrayList) timeDetailsMap.get("AS_OF_YEAR1");
                    timeDetailsArray.add(timeInfo.get(0));

                } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("DAY") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_DATE");
                    timeDetailsArray.add(timeInfo.get(0));

                    timeInfo = (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.add(timeInfo.get(0));

                    if (timeDetailsMap.get("PRG_COMPARE") != null) {
                        timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                        timeDetailsArray.add(timeInfo.get(0));
                    } else {
                        timeDetailsArray.add("Last Period");
                    }
                    //timeInfo = (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    //timeDetailsArray.add(timeInfo.get(0));
                } else if (timeDetailsArray.get(0).toString().equalsIgnoreCase("MONTH") && timeDetailsArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                    timeInfo = (ArrayList) timeDetailsMap.get("AS_OF_MONTH");
                    timeDetailsArray.add(timeInfo.get(0));

                    timeInfo =
                            (ArrayList) timeDetailsMap.get("PRG_PERIOD_TYPE");
                    timeDetailsArray.add(timeInfo.get(0));

                    timeInfo =
                            (ArrayList) timeDetailsMap.get("PRG_COMPARE");
                    timeDetailsArray.add(timeInfo.get(0));
                }

            }

        }
        PbReportQuery repQuery = new PbReportQuery();
        repQuery.setRowViewbyCols(rowviewByValues);
        repQuery.setColViewbyCols(colviewByValues);
        repQuery.setParamValue(params);
        repQuery.setQryColumns(reportQryElementIds);
        repQuery.setColAggration(reportQueryAggregations);
        repQuery.setTimeDetails(timeDetailsArray);
        headlineObj = repQuery.getPbReturnObject(String.valueOf(reportQryElementIds.get(0)));
        container.setRetObj(headlineObj);
        return container;
    }

    public String getHeadlineData(ProgenDataSet headlineObj, String[] headlineDetails, ArrayList displayLabels, int size, ArrayList<Integer> seq, ArrayList<String> displayColumns) {
        StringBuilder headlineBuilder = new StringBuilder();
        try {
//             container = reportViewerBD.readSnapShotXML(container, headlineDetails[0], collect);
//             seq=headlineObj.findTopBottom(  dimIdList,sortType,count);
//             for(int i=0;i<seq.size();i++){
//         System.out.print("**");
//         
//                  System.out.print("**");
//
//     }
//

            if (headlineObj.getRowCount() > 0) {
                headlineBuilder.append("<table id='headLinesTab' class='tablesorter'><thead>");
                for (int j = 0; j < displayLabels.size(); j++) {
                    headlineBuilder.append("<th align='center'>").append(displayLabels.get(j)).append("</th>");
                }
                headlineBuilder.append("<tbody>");
                for (int i = 0; i < seq.size(); i++) {

                    headlineBuilder.append("<tr>");
                    for (int k = 0; k < size; k++) {
                        headlineBuilder.append("<td >").append(headlineObj.getFieldValueString(seq.get(i), displayColumns.get(k))).append("</td>");
                    }
                    for (int l = size; l < displayColumns.size(); l++) {
                        BigDecimal val = null;
                        val = headlineObj.getFieldValueBigDecimal(seq.get(i), displayColumns.get(l));
                        headlineBuilder.append("<td align='right'>").append(NumberFormatter.getModifiedNumber(val, "", -1)).append("</td>");

                    }
                    headlineBuilder.append("</tr>");
                }
                headlineBuilder.append("</tbody></table>");
                headlineBuilder.append("||").append(headlineDetails[0]).append("div");
            }

//            container=snapshotDesigner.getReturnObjectForHeadlines(headlineDetails[0],container);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return headlineBuilder.toString();
    }

    public Container getContainer() {
        return container1;
    }

    public void setContainer(Container container1) {
        this.container1 = container1;
    }

    public String createCollectDocument(String reportIds, String completeUrl1, HttpServletRequest request, HttpServletResponse response, String snapDate1, String emailList, String userId, String repcustname, String fromOption, int count, int totalRep) throws Exception {

        String reportFileName = "";
        try {
            //Container container = null;

            HttpSession session = request.getSession(false);
            //reportIds = fReportIds;
            reportId = reportIds;
            snapDate = snapDate1;
            snapshotName = repcustname;
//            String repIdQuery = "";
            schedulerId = (String) request.getAttribute("schedulerId");
            this.fromOption = fromOption;
            // Added by Amar
            PbReportViewerBD reportViewerBD = new PbReportViewerBD();
            File tempFile = null;
            //  for(int k =0;k<fReportIds.length;k++){

            request.setAttribute("REPORTID", reportIds);
            PbReportViewerDAO dao = new PbReportViewerDAO();
//                 HashMap Viewbyhashmap = new HashMap();
            //session.setAttribute(reportID[i], Viewbyhashmap);
            request.setAttribute("isDepentReport", false);
            request.setAttribute("exportReport", true);
            reportViewerBD.prepareReport(reportIds, userId, request, response,false);
            container = Container.getContainerFromSession(request, reportIds);
//                 PbReportCollection collect = container.getReportCollect();

            String folderPath = (String) session.getAttribute("reportAdvHtmlFileProps") + "/collectStore";
            File folderDir = new File(folderPath);
            if (!folderDir.exists()) {
                folderDir.mkdir();
            }
            UUID fileUniqueId = UUID.randomUUID();
            reportFileName = "snapshot_" + reportIds + "_" + fileUniqueId + ".ser";
            String filePath = folderPath + "/" + reportFileName;
            tempFile = new File(filePath);
            if (tempFile != null) {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                }
            }

//            FileOutputStream fos = new FileOutputStream(tempFile);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(collect);
//            oos.flush();
//            oos.close();

//            HashMap map = new HashMap();
//            map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
//            container = (prg.db.Container) map.get(reportId);
//
            completeUrl = container.getReportCollect().completeUrl;
            ParametersHashMap = container.getParametersHashMap();
            TableHashMap = container.getTableHashMap();
            GraphHashMap = container.getGraphHashMap();
            reportHashMap = container.getReportHashMap();
            reportParameters = container.getReportCollect().reportParameters;
            operatorFilters = container.getReportCollect().operatorFilters;



            if (ParametersHashMap != null) {
                if (ParametersHashMap.get("Parameters") != null) {
                    params = (ArrayList) ParametersHashMap.get("Parameters");
                }
                if (ParametersHashMap.get("TimeDetailstList") != null) {
                    timeDetails = (ArrayList) ParametersHashMap.get("TimeDetailstList");
                }
                if (ParametersHashMap.get("TimeDimHashMap") != null) {
                    timeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
                }
            }
            if (TableHashMap != null) {
                if (TableHashMap.get("REP") != null) {
                    REP_Elements = (ArrayList) TableHashMap.get("REP");
                } else {
                    if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                    }
                }
                if (TableHashMap.get("CEP") != null) {
                    CEP_Elements = (ArrayList) TableHashMap.get("CEP");
                }
                if (TableHashMap.get("Measures") != null && TableHashMap.get("MeasuresNames") != null) {
                    measures = (ArrayList) TableHashMap.get("Measures");
                    measuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                }

            }
            if (GraphHashMap != null) {
                if (GraphHashMap.get("AllGraphColumns") != null) {
                    grpCols = (ArrayList) GraphHashMap.get("AllGraphColumns");
                }
                if (GraphHashMap.get("graphIds") != null) {
                    no_of_graphs = String.valueOf(GraphHashMap.get("graphIds")).split(",");//((String) getGraphHashMap().get("graphIds")).split(",");
                }
            }
            total_list = new LinkedHashSet();

            for (int i = 0; i < params.size(); i++) {
                paramsString = paramsString + "," + String.valueOf(params.get(i)).replace("A_", "");
            }
            if (!(paramsString.equalsIgnoreCase(""))) {
                paramsString = paramsString.substring(1);
            }
            paramString2 = " case ";
            for (int i = 0; i < params.size(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    paramString2 += " when element_id =" + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";

                }
                else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    paramString2 += " when element_id =" + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";

                }
                else {
                    paramString1 = paramString1 + "," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1);

                }

            }
            if (!(paramString1.equalsIgnoreCase(""))) {
                        paramString1 = paramString1.substring(1);
            }
            paramString2 += " else 10000 end ";

            Element root = new Element("snapshot");
            //root.setAttribute("version", "1.00001");
            //root.setText("New Root");
            Element repVersionElem=new Element("Report_Version");
            repVersionElem.addContent(String.valueOf(container.getReportCollect().getReportVersion()));
            root.addContent(repVersionElem);

            root = buildMetaInfo(root);
            root = buildParameters(root);
            root = buildTimeDetails(root);
            // root = buildViewByDetails(root);
            root = buildQueryDetails(root);

            document = new Document(root);
            serializer = new XMLOutputter();
            String snapShotClobTemp = serializer.outputString(document);
            String snapShotClobString = snapShotClobTemp.replace("&lt;", "<").replace("&gt;", ">");
            // Added to store serialize object into filesystem
            FileOutputStream fos = new FileOutputStream(tempFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(snapShotClobString);
            oos.flush();
            oos.close();
            //}
            //end of code

            //   
            if (count == 1) {
                if (schedulerId != null) {
                    String query1 = getResourceBundle().getString("updateSchedulerParamDetails");
                    Connection con = ProgenConnection.getInstance().getConnection();
                    String snapShotClob = serializer.outputString(document);
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        PreparedStatementProxy opstmt = (PreparedStatementProxy) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setString(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                        opstmt.setString(3, request.getContextPath());
                        //String a[] = completeUrl.substring(1).split("/");
                        opstmt.setString(4, "");
                        opstmt.setString(5, schedulerId);
                        opstmt.setString(6, reportIds);
                        int rows = opstmt.executeUpdate();

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        JDBC4PreparedStatementWrapper opstmt = (JDBC4PreparedStatementWrapper) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setString(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                        opstmt.setString(3, request.getContextPath());
                        // String a[] = completeUrl.substring(1).split("/");
                        opstmt.setString(4, "");
                        opstmt.setString(5, schedulerId);
                        opstmt.setString(6, reportIds);
                        int rows = opstmt.executeUpdate();

                    } else {
                        OraclePreparedStatement opstmt;
                        opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setStringForClob(2, snapShotClob.replace("&lt;", "<").replace("&gt;", ">"));
                        opstmt.setString(3, request.getContextPath());
                        String a[] = completeUrl.substring(1).split("/");
                        opstmt.setStringForClob(4, a[1]);
                        opstmt.setString(5, schedulerId);
                        opstmt.setString(6, reportIds);
                        int rows = opstmt.executeUpdate();
                    }
                    //by Prabal
                    if (con != null) {
                        con.close();
                    }
                } else {
                    PbReturnObject pbro = execSelectSQL("select pu_id from prg_ar_users WHERE pu_email in ('" + emailList + "') and pu_id !=" + userId);
                    String query1 = getResourceBundle().getString("insertsnapshot");
                    Connection con = ProgenConnection.getInstance().getConnection();
                    //  String snapShotClob = serializer.outputString(document);

                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        PreparedStatementProxy opstmt = (PreparedStatementProxy) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setString(2, reportIds);
                        opstmt.setString(3, repcustname);
                        //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
                        opstmt.setString(4, "");
                        opstmt.setString(5, request.getContextPath());
                        opstmt.setString(6, userId);
                        String a[] = completeUrl.substring(1).split("/");
                        //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
                        opstmt.setString(7, a[1]);
                        opstmt.setString(8, fromOption);
                        int rows = opstmt.executeUpdate();
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportIds);
//                    opstmt.setString(3, repcustname);
//                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
//                    opstmt.setString(4, "");
//                    opstmt.setString(5, request.getContextPath());
//                    opstmt.setString(6, userId);
//                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
//                    opstmt.setString(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }

                    } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        JDBC4PreparedStatementWrapper opstmt = (JDBC4PreparedStatementWrapper) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setString(2, reportIds);
                        opstmt.setString(3, repcustname);
                        //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
                        opstmt.setString(4, "");
                        opstmt.setString(5, request.getContextPath());
                        opstmt.setString(6, userId);
                        String a[] = completeUrl.substring(1).split("/");
                        //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
                        opstmt.setString(7, a[1]);
                        opstmt.setString(8, fromOption);
                        int rows = opstmt.executeUpdate();
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportIds);
//                    opstmt.setString(3, repcustname);
//                    //opstmt.setCharacterStream(4,new StringReader(snapShotClob),snapShotClob.length());
//                    opstmt.setString(4, "");
//                    opstmt.setString(5, request.getContextPath());
//                    opstmt.setString(6, userId);
//                    //opstmt.setCharacterStream(7, new StringReader(a[1]),a[1].length());
//                    opstmt.setString(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }

                    } else {
                        OraclePreparedStatement opstmt;
                        opstmt = (OraclePreparedStatement) con.prepareStatement(query1);
                        opstmt.setString(1, userId);
                        opstmt.setString(2, reportIds);
                        opstmt.setString(3, repcustname);
                        opstmt.setStringForClob(4, "");
                        opstmt.setString(5, request.getContextPath());
                        opstmt.setString(6, userId);
                        String a[] = completeUrl.substring(1).split("/");
                        opstmt.setStringForClob(7, a[1]);
                        opstmt.setString(8, fromOption);
                        int rows = opstmt.executeUpdate();
//                for (int i = 0; i < pbro.getRowCount(); i++) {
//                    opstmt.setString(1, String.valueOf(pbro.getFieldValueInt(i, 0)));
//                    opstmt.setString(2, reportIds);
//                    opstmt.setString(3, repcustname);
//                    opstmt.setStringForClob(4, "");
//                    opstmt.setString(5, request.getContextPath());
//                    opstmt.setString(6, userId);
//                    opstmt.setStringForClob(7, a[1]);
//                    rows = opstmt.executeUpdate();
//                }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return reportFileName;
    }
}
