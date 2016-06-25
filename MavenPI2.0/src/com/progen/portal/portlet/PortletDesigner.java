/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.portlet;

import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.jtds.jdbc.ClobImpl;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author db2admin
 */
public class PortletDesigner extends PbDb {

    public static Logger logger = Logger.getLogger(PortletDesigner.class);
    ResourceBundle resBundle = null;

    private ResourceBundle getResourceBundle() {
        if (resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) //                resBundle = (PortletDesignerResourceBundle) ResourceBundle.getBundle("com.progen.portal.portlet.PortletDesignerSqlResBundle");
            {
                resBundle = new PortletDesignerSqlResBundle();
            } else //                resBundle =  (PortletDesignerResourceBundle) ResourceBundle.getBundle("com.progen.portal.portlet.PortletDesignerResourceBundle");
            {
                resBundle = new PortletDesignerResourceBundle();
            }
        }
        return resBundle;
    }
//    PortletDesignerResourceBundle resBundle = (PortletDesignerResourceBundle) ResourceBundle.getBundle("com.progen.portal.portlet.PortletDesignerResourceBundle");
    public HashMap ParametersHashMap = null;
    public HashMap TableHashMap = null;
    public HashMap GraphHashMap = null;
    public HashMap ReportHashMap = null;
    public String portletName = null;
    public String portletDesc = null;
    public String displayType = null;
    public String[] KPIS = null;
    public String getKpiGraphStatus = "Y";
    //public String KPIGraph=null;
    //String UserFolderIds = null;
    public int portletId = 0;
    /////
    public String[] pMasterChildTags = {"pmName", "pmDisplayName", "pmDisplayType"};
    public String[] ppDetailChildTags = {"element_id", "paramDispName", "childElementId", "dimId", "dimTabId", "displayType", "relLevel", "dispSeqNo", "defaultValue", "isDisplay"};
    public String[] ptMasterChildTags = {"timeLevel", "timeType"};
    public String[] ptDetailsChildTags = {"timeColName", "timeColType", "timeColSeq", "timeFormSeq", "timeIsDisplayed", "defaultValue"};
    public String[] pvrMasterDataChildTags = {"pvrdefaultValue"};
    public String[] pgMasterChildTags = {"graphName", "graphType", "graphClass", "graphHeight", "graphWidth", "showLegent", "startRange", "endRange", "firstBreak", "secondBreak", "needleValue", "measType", "daytargetVal", "measureName"};
    public String[] rqDetailsChildTags = {"elementId", "elementName", "elementAgg", "graphAxis"};
    public String[] rqMoreInfoChildTags = {"orderCol", "orderType", "totalRecord"};
    /////////////////////////////////////////////////
    public String[] MetaInfoChildValues = null;
    public String paramsString = "";
    public String paramString1 = "";
    public String paramString2 = "";
    //String measureString = "";
    //String measureString1 = "";
    public ArrayList params = new ArrayList();
    public ArrayList timeDetails = new ArrayList();
    public HashMap timeDimHashMap = new HashMap();
    public ArrayList REP_Elements = new ArrayList();
    public ArrayList CEP_Elements = new ArrayList();
    public ArrayList measures = new ArrayList();
    public ArrayList measuresNames = new ArrayList();
    public ArrayList grpCols = new ArrayList();
    public Set total_list = new LinkedHashSet();
    public String[] no_of_graphs = new String[0];
    public HttpServletRequest request = null;
    public Connection connect = null;
    public Statement statement = null;
    public ResultSet resultSet = null;
    private String[] folderIds;
    private String userId;
    ////////////////////////////////////////////////
    //list of queries  
    public String getReportParamDetailsQuery = getResourceBundle().getString("getReportParamDetails");
    public String insertPortletXMLQuery = getResourceBundle().getString("insertPortletXML");
    XMLOutputter serializer = null;
    Document document = null;

    //
    public PortletDesigner() {
    }

    public void createDocument() throws Exception {
        Connection connection = null;
        PreparedStatement opstmt = null;
        ServletWriterTransferObject swt = null;
        File f1 = null;
        try {


            if (getParametersHashMap() != null) {
                if (getParametersHashMap().get("Parameters") != null) {
                    params = (ArrayList) getParametersHashMap().get("Parameters");
                }
                if (getParametersHashMap().get("TimeDetailstList") != null) {
                    timeDetails = (ArrayList) getParametersHashMap().get("TimeDetailstList");
                }
                if (getParametersHashMap().get("TimeDimHashMap") != null) {
                    timeDimHashMap = (HashMap) getParametersHashMap().get("TimeDimHashMap");
                }
            }
            if (getTableHashMap() != null) {
                if (getTableHashMap().get("REP") != null) {
                    REP_Elements = (ArrayList) getTableHashMap().get("REP");
                } else if (getParametersHashMap().get("RowViewByIds") != null) {
                    REP_Elements = (ArrayList) getParametersHashMap().get("RowViewByIds");
                } else {
                    if (params != null && params.size() != 0) {
                        REP_Elements.add(params.get(0));
                    }
                }
                if (getTableHashMap().get("CEP") != null) {
                    CEP_Elements = (ArrayList) getTableHashMap().get("CEP");
                }
                if (getTableHashMap().get("Measures") != null && getTableHashMap().get("MeasuresNames") != null) {
                    measures = (ArrayList) getTableHashMap().get("Measures");
                    measuresNames = (ArrayList) getTableHashMap().get("MeasuresNames");
                }

            }
            if (getGraphHashMap() != null) {
                if (getGraphHashMap().get("AllGraphColumns") != null) {
                    grpCols = (ArrayList) getGraphHashMap().get("AllGraphColumns");
                }
                if (getGraphHashMap().get("graphIds") != null) {
                    no_of_graphs = String.valueOf(getGraphHashMap().get("graphIds")).split(",");//((String) getGraphHashMap().get("graphIds")).split(",");
                }
            }
            total_list = new LinkedHashSet();

            for (int i = 0; i < params.size(); i++) {
                paramsString = paramsString + "," + String.valueOf(params.get(i)).replace("A_", "");
            }
            if (!(paramsString.equalsIgnoreCase(""))) {
                paramsString = paramsString.substring(1);
            }

            for (int i = 0; i < params.size(); i++) {
                paramString1 = paramString1 + "," + String.valueOf(params.get(i)).replace("A_", "") + "," + (i + 1);
            }

            if (!(paramString1.equalsIgnoreCase(""))) {
                paramString1 = paramString1.substring(1);
            }

            paramString2 = " case ";
            for (int i = 0; i < params.size(); i++) {
                paramString2 += " when element_id =" + String.valueOf(params.get(i)).replace("A_", "") + " then " + (i + 1) + " ";
            }
            paramString2 += " else 10000 end ";

            if (!(paramString2.equalsIgnoreCase(""))) {
                paramString2 = paramString2.substring(1);
            }
            /*
             * Iterator listIt = total_list.iterator(); for (int i = 0;
             * listIt.hasNext() == true; i++) { String temp = (String)
             * listIt.next(); if (temp.equalsIgnoreCase("Time")) { measureString
             * = measureString + ",-1"; measureString1 = measureString1 + ",-1,"
             * + (i + 1); } else { measureString = measureString + "," + temp;
             * measureString1 = measureString1 + "," + temp + "," + (i + 1); } }
             * if (!(measureString.equalsIgnoreCase(""))) { measureString =
             * measureString.substring(1); } if
             * (!(measureString1.equalsIgnoreCase(""))) { measureString1 =
             * measureString1.substring(1); }
             */

            Element root = new Element("portlet");
            root.setAttribute("version", "1.00001");
            root.setText("New Root");

            root = buildMetaInfo(root);
            root = buildParameters(root);
            root = buildTimeDetails(root);
            root = buildViewByDetails(root);
            root = buildQueryDetails(root);
            if (getDisplayType().equalsIgnoreCase("KPI Graph")) {
                root = buildKpiGraphStatus(root);
            }
            document = new Document(root);
            serializer = new XMLOutputter();

            String portletXML = serializer.outputString(document).toString().replaceAll("[\r\n]+", "");
            String query = getResourceBundle().getString("insertBasePortletDetails");
            Object[] objArr = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                objArr = new Object[8];
                objArr[0] = this.portletName;
                objArr[1] = this.portletDesc;
                objArr[2] = portletXML;
                objArr[3] = "I";
                objArr[4] = this.displayType;
                objArr[5] = this.folderIds[0];
                objArr[6] = this.userId;
                objArr[7] = this.userId;
            } else {
                objArr = new Object[9];
                objArr[0] = this.portletId;
                objArr[1] = this.portletName;
                objArr[2] = this.portletDesc;
                objArr[3] = "not_generated";
                objArr[4] = "I";
                objArr[5] = this.displayType;
                objArr[6] = this.folderIds[0];
                objArr[7] = this.userId;
                objArr[8] = this.userId;
            }

            PbDb pbdb = new PbDb();
            String finalQuery = pbdb.buildQuery(query, objArr);
            pbdb.execUpdateSQL(finalQuery);

            if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                swt = ServletUtilities.createBufferedWriter(portletName, "xml");
                Writer writer = swt.writer;
                writer.write(portletXML);
                writer.flush();
                writer.close();
                swt.setReportName(portletName);
                Reader reader = null;

                try {
                    reader = ServletUtilities.createBufferedReader(swt.fileName);
                    Clob clobObj = updatePortletXml(reader);
                    reader.close();
                    if (clobObj != null) {
                        f1 = new File(swt.fileName);
                        boolean success = f1.delete();


                    }

                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                } finally {
                    try {
                        reader.close();
                        if (f1 != null) {
                            f1.delete();
                        }
                    } catch (IOException ex) {
                        logger.error("Exception:", ex);
                    }
                }
            }



            /*
             * connection = ProgenConnection.getInstance().getConnection(); if
             * (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER))
             * { opstmt = connection.prepareStatement(insertPortletXMLQuery);
             * opstmt.setString(1,
             * serializer.outputString(document).toString().replaceAll("[\r\n]+",
             * "")); opstmt.setInt(2,Integer.parseInt(getFolderIds()[0]) );
             * opstmt.setInt(3, getPortletId()); } else { opstmt =
             * (OraclePreparedStatement)
             * connection.prepareStatement(insertPortletXMLQuery);
             * opstmt.setInt(2,Integer.parseInt(getFolderIds()[0]) );
             * opstmt.setInt(3, getPortletId()); // ((OraclePreparedStatement)
             * opstmt).setStringForClob(1,
             * serializer.outputString(document).toString().replaceAll("[\r\n]+",
             * "")); // }
             *
             * int rows = opstmt.executeUpdate();
             *
             * if (opstmt != null) { opstmt.close(); } if (connection != null) {
             * connection.close(); } HttpSession
             * session=getRequest().getSession(false); List<Portal> portals =
             * (List<Portal>) session.getAttribute("PORTALS"); List<PortLet>
             * portlets=null; PortLet portLet=null; for(Portal portal:portals){
             * portlets=portal.getPortlets(); Iterator<PortLet> moduleIter =
             * Iterables.filter(portlets, PortLet.getAccessPortletPredicate(
             * getPortletId())).iterator(); if (moduleIter.hasNext()) { portLet
             * = moduleIter.next(); break; }
             *
             * }
             * portLet.setXMLDocument(document); portLet.setPortletType("I");
             */
        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public Clob updatePortletXml(Reader reader) {
        ClobImpl clob = null;
        String portletsXmlQuery = getResourceBundle().getString("updateXml1");
        String finalQuery;
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[1];
        obj[0] = this.portletId;
        finalQuery = pbDb.buildQuery(portletsXmlQuery, obj);
        String portletQuery = getResourceBundle().getString("getXmlClob");
        portletQuery = super.buildQuery(portletQuery, new Object[]{this.portletId});
        Connection conn = null;
        OraclePreparedStatement opstmt = null;
        PreparedStatement sqlstmt = null;
        CLOB clobLoc = null;
        try {
            pbDb.execModifySQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        try {
            PbReturnObject retObj = super.execSelectSQL(portletQuery);
            char[] cbuf;
            int offset = 0;
            int len = 5196;
            int toRead = -1;
            Writer writer = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                {
                    if (retObj.getRowCount() > 0) {

                        clobLoc = (CLOB) retObj.getFieldValueOracleClob(0, "XML_PATH");
                        //clobLoc = CLOB.empty_lob();
                        clobLoc.truncate(clobLoc.length());
                        writer = clobLoc.setCharacterStream(1);
                    }
                }
            } else {
                if (retObj.getRowCount() > 0) {

                    clob = (ClobImpl) retObj.getFieldValueClob(0, "XML_PATH");
                    writer = clob.setCharacterStream(1);
                }
            }

            do {
                cbuf = new char[len];
                toRead = reader.read(cbuf, offset, len);
                writer.write(cbuf);
                writer.flush();
                if (toRead == -1) {
                    break;
                }
            } while (true);


            if (writer != null) {
                writer.close();
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {

                if (clobLoc != null) {
                    conn = ProgenConnection.getInstance().getConnection();
                    String updateQuery = getResourceBundle().getString("updateXml");
                    opstmt = (OraclePreparedStatement) conn.prepareStatement(updateQuery);
                    opstmt.setCLOB(1, clobLoc);
                    opstmt.setInt(2, this.portletId);
                    opstmt.executeUpdate();
                    conn.commit();
                    opstmt.close();
                    opstmt = null;
                    conn.close();
                    conn = null;
                }
            } else {
                if (clob != null) {

                    conn = ProgenConnection.getInstance().getConnection();
                    String updateQuery = getResourceBundle().getString("updateXml");
                    sqlstmt = (PreparedStatement) conn.prepareStatement(updateQuery);
                    sqlstmt.setClob(1, clob);
                    sqlstmt.setInt(2, this.portletId);
                    sqlstmt.executeUpdate();
                    //conn.commit();
                    sqlstmt.close();
                    sqlstmt = null;
                    conn.close();
                    conn = null;
                }
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (opstmt != null) {
                    opstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException ex) {
            }
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            return clob;
        } else {
            return clobLoc;
        }

    }

    public Element buildMetaInfo(Element root) {
        MetaInfoChildValues = new String[3];
        MetaInfoChildValues[0] = getPortletName();
        MetaInfoChildValues[1] = getPortletDesc();
        MetaInfoChildValues[2] = getDisplayType();


        Element pMasterEle = new Element("pMaster");
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

        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        String[] Obj = new String[2];

        Element pParameterEle = new Element("pParameter");
        Element ppDetailEle = null;
        Element child = null;
        int count = 1;


        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Obj[0] = paramsString;
                Obj[1] = paramString2;
            } else {
                Obj[0] = paramsString;
                Obj[1] = paramString1;
            }

            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);

            retObj = execSelectSQL(finalQuery);

            //added by santhosh.kumar@porgenbusiness.com on 03/12/2009
            if (timeDetails != null && timeDetails.size() != 0 && timeDimHashMap != null && timeDimHashMap.size() != 0) {
                ppDetailEle = new Element("ppDetail");
                Obj = new String[10];
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
                    ppDetailEle = new Element("ppDetail");
                    Obj = new String[10];
                    Obj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    Obj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    Obj[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    Obj[3] = retObj.getFieldValueString(i, dbColumns[3]);
                    Obj[4] = retObj.getFieldValueString(i, dbColumns[4]);
                    Obj[5] = retObj.getFieldValueString(i, dbColumns[5]);
                    Obj[6] = retObj.getFieldValueString(i, dbColumns[6]);
                    Obj[7] = String.valueOf((i + count));
                    Obj[8] = retObj.getFieldValueString(i, dbColumns[7]);
                    Obj[9] = retObj.getFieldValueString(i, dbColumns[8]);

                    for (int j = 0; j < ppDetailChildTags.length; j++) {
                        child = new Element(ppDetailChildTags[j]);
                        child.setText(Obj[j]);
                        ppDetailEle.addContent(child);
                    }
                    pParameterEle.addContent(ppDetailEle);
                }
                root.addContent(pParameterEle);
            }

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public Element buildTimeDetails(Element root) {
        String[] Obj = null;

        Element pTimeEle = new Element("pTime");
        Element ptMasterEle = new Element("ptMaster");
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
                    Obj = new String[6];
                    String key = (String) it.next();

                    timeDetailsFromHashMap = (ArrayList) timeDimHashMap.get(key);


                    Obj[0] = String.valueOf(timeDetailsFromHashMap.get(2));
                    Obj[1] = key;
                    Obj[2] = String.valueOf(timeDetailsFromHashMap.get(3));
                    Obj[3] = String.valueOf(timeDetailsFromHashMap.get(4));
                    Obj[4] = "N";
                    Obj[5] = "Current Date";

                    ptDetailsEle = new Element("ptDetails");
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

    public Element buildViewByDetails(Element root) {
        String[] Obj = null;
        Element pViewByEle = new Element("pViewBy");
        Element pvrdefaultValueEle = null;

        Element pvRowViewByEle = null;
        Element pvColViewByEle = null;
        Element pvrMasterDataEle = null;


        if (REP_Elements != null && REP_Elements.size() != 0) {
            pvRowViewByEle = new Element("pvRowViewBy");
            pvrMasterDataEle = new Element("pvrMasterData");
            for (int i = 0; i < REP_Elements.size(); i++) {
                Obj = new String[1];
                //Obj[0] = reportId;
                //Obj[1] = String.valueOf((i + 1));
                //Obj[2] = String.valueOf((i + 1));
                Obj[0] = String.valueOf(REP_Elements.get(i));

                for (int j = 0; j < pvrMasterDataChildTags.length; j++) {
                    pvrdefaultValueEle = new Element(pvrMasterDataChildTags[j]);
                    pvrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    pvrMasterDataEle.addContent(pvrdefaultValueEle);
                }
            }
            pvRowViewByEle.addContent(pvrMasterDataEle);
            pViewByEle.addContent(pvRowViewByEle);
        }


        if (CEP_Elements != null && CEP_Elements.size() != 0) {
            pvColViewByEle = new Element("pvColViewBy");
            pvrMasterDataEle = new Element("pvrMasterData");
            for (int i = 0; i < CEP_Elements.size(); i++) {
                Obj = new String[1];
                //Obj[0] = reportId;
                //Obj[1] = String.valueOf((i + 1));
                //Obj[2] = String.valueOf((j + 1));
                Obj[0] = String.valueOf(CEP_Elements.get(i));

                for (int j = 0; j < pvrMasterDataChildTags.length; j++) {
                    pvrdefaultValueEle = new Element(pvrMasterDataChildTags[j]);
                    pvrdefaultValueEle.setText(String.valueOf(Obj[j]));
                    pvrMasterDataEle.addContent(pvrdefaultValueEle);
                }
            }
            pvColViewByEle.addContent(pvrMasterDataEle);
            pViewByEle.addContent(pvColViewByEle);
        }
        root.addContent(pViewByEle);
        return root;
    }

    public Element buildQueryDetails(Element root) throws Exception {
        String[] Obj = null;
        Element rQueryEle = new Element("rQuery");
        Element pgMasterEle = null;
        Element rqDetailsEle = null;
        Element rqMoreInfoEle = null;
        Element child = null;

        String[] barChartColumnNames = null;
        String[] barChartColumnTitles = null;
        String[] viewBys = null;
        String[] axis = null;

        String aggTypeQuery = null;
        PbReturnObject grppbro = null;

        if (displayType != null) {
            if (displayType.equalsIgnoreCase("Table") || displayType.equalsIgnoreCase("kpi") || displayType.equalsIgnoreCase("BasicTarget")) {
                if (measures != null && measures.size() != 0) {
                    //for (int i = 0; i < measures.size(); i++) {
                    barChartColumnNames = (String[]) measures.toArray(new String[0]);
                    barChartColumnTitles = (String[]) measuresNames.toArray(new String[0]);

                    for (int k = 0; k < barChartColumnNames.length; k++) {
                        rqDetailsEle = new Element("rqDeatils");

                        Obj = new String[4];

                        Obj[0] = barChartColumnNames[k].replace("A_", "");
                        Obj[1] = barChartColumnTitles[k].replace("A_", "");
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                            aggTypeQuery = "select isnull(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        } else {
                            aggTypeQuery = "select nvl(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                        }

                        grppbro = execSelectSQL(aggTypeQuery);
                        Obj[2] = grppbro.getFieldValueString(0, 0);
                        //Obj[2] = "SUM";
                        Obj[3] = "0";

                        for (int l = 0; l < rqDetailsChildTags.length; l++) {
                            child = new Element(rqDetailsChildTags[l]);
                            child.setText(Obj[l]);
                            rqDetailsEle.addContent(child);
                        }
                        rQueryEle.addContent(rqDetailsEle);
                    }

                    rqMoreInfoEle = new Element("rqMoreInfo");
                    Obj = new String[3];
                    Obj[0] = barChartColumnNames[0].replace("A_", "");
                    Obj[1] = "ASC";
                    Obj[2] = "10";

                    for (int l = 0; l < rqMoreInfoChildTags.length; l++) {
                        child = new Element(rqMoreInfoChildTags[l]);
                        child.setText(Obj[l]);
                        rqMoreInfoEle.addContent(child);
                    }
                    rQueryEle.addContent(rqMoreInfoEle);
                    //}
                }
            } else if (displayType.equalsIgnoreCase("Graph") || displayType.equalsIgnoreCase("KPI Graph")) {
                if (no_of_graphs != null && no_of_graphs.length != 0) {
                    pgMasterEle = new Element("pgMaster");
                    for (int i = 0; i < no_of_graphs.length; i++) {

                        HashMap graphDetails = (HashMap) getGraphHashMap().get(no_of_graphs[i]);
                        if (displayType.equalsIgnoreCase("KPI Graph")) {
                            getKpiGraphStatus = graphDetails.get("getKpiGraphStatus").toString();
                        }
//                        if(displayType.equalsIgnoreCase("KPI Graph"))
                        Obj = new String[14];
//                        else
//                            Obj = new String[11];
                        Obj[0] = String.valueOf(graphDetails.get("graphName"));
                        Obj[1] = String.valueOf(graphDetails.get("graphTypeName"));

                        Obj[2] = String.valueOf(graphDetails.get("graphClassName"));
                        Obj[3] = String.valueOf(graphDetails.get("graphHeight"));
                        //Obj[4] = String.valueOf(graphDetails.get("graphWidth"));
                        Obj[4] = "400";
                        Obj[5] = "Y";


                        //add more tage for KPI graph
                        if (graphDetails.get("startRange") != null) {
                            Obj[6] = String.valueOf(graphDetails.get("startRange"));
                        } else {
                            Obj[6] = "";
                        }
                        if (graphDetails.get("endRange") != null) {
                            Obj[7] = String.valueOf(graphDetails.get("endRange"));
                        } else {
                            Obj[7] = "";
                        }
                        if (graphDetails.get("firstBreak") != null) {
                            Obj[8] = String.valueOf(graphDetails.get("firstBreak"));
                        } else {
                            Obj[8] = "";
                        }
                        if (graphDetails.get("secondBreak") != null) {
                            Obj[9] = String.valueOf(graphDetails.get("secondBreak"));
                        } else {
                            Obj[9] = "";
                        }
                        if (graphDetails.get("needleValue") != null) {
                            Obj[10] = String.valueOf(graphDetails.get("needleValue"));
                        } else {
                            Obj[10] = "";
                        }

                        if (displayType.equalsIgnoreCase("KPI Graph")) {
                            if (graphDetails.get("measType") != null) {
                                Obj[11] = String.valueOf(graphDetails.get("measType"));
                            } else {
                                Obj[11] = "";
                            }
                            if (graphDetails.get("daytargetVal") != null) {
                                Obj[12] = String.valueOf(graphDetails.get("daytargetVal"));
                            } else {
                                Obj[12] = "";
                            }
                            if (graphDetails.get("measureName") != null) {
                                Obj[13] = String.valueOf(graphDetails.get("measureName"));
                            } else {
                                Obj[13] = "";
                            }
                        } else {
                            Obj[11] = "";
                            Obj[12] = "";
                            Obj[13] = "";
                        }

                        for (int j = 0; j < pgMasterChildTags.length; j++) {
                            child = new Element(pgMasterChildTags[j]);
                            child.setText(Obj[j]);
                            pgMasterEle.addContent(child);
                        }
                        rQueryEle.addContent(pgMasterEle);

                        barChartColumnNames = (String[]) graphDetails.get("barChartColumnNames");
                        barChartColumnTitles = (String[]) graphDetails.get("barChartColumnTitles");
                        viewBys = (String[]) graphDetails.get("viewByElementIds");
                        if (viewBys == null) {
//                            viewBys=new String[getParametersHashMap().get("RowViewByIds").toString()];
                            viewBys = getParametersHashMap().get("RowViewByIds").toString().split(",");
                        }
                        axis = (String[]) graphDetails.get("axis");
                        if (axis == null) {
                            axis = new String[barChartColumnNames.length];
                        }
                        for (int k = viewBys.length; k < barChartColumnNames.length; k++) {
                            rqDetailsEle = new Element("rqDeatils");
                            Obj = new String[4];
                            Obj[0] = barChartColumnNames[k].replace("A_", "");
                            Obj[1] = barChartColumnTitles[k];
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                aggTypeQuery = "select isnull(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                            } else {
                                aggTypeQuery = "select nvl(aggregation_type,'SUM' ) from prg_user_all_info_details where element_id in (" + Obj[0] + ")";
                            }

                            grppbro = execSelectSQL(aggTypeQuery);
                            Obj[2] = grppbro.getFieldValueString(0, 0);

                            //Obj[2] = "SUM";

                            Obj[3] = axis[k - viewBys.length];

                            for (int l = 0; l < rqDetailsChildTags.length; l++) {
                                child = new Element(rqDetailsChildTags[l]);
                                child.setText(Obj[l]);
                                rqDetailsEle.addContent(child);
                            }
                            rQueryEle.addContent(rqDetailsEle);
                        }

                        rqMoreInfoEle = new Element("rqMoreInfo");
                        Obj = new String[3];
                        Obj[0] = barChartColumnNames[viewBys.length].replace("A_", "");
                        Obj[1] = "ASC";
                        Obj[2] = "10";

                        for (int l = 0; l < rqMoreInfoChildTags.length; l++) {
                            child = new Element(rqMoreInfoChildTags[l]);
                            child.setText(Obj[l]);
                            rqMoreInfoEle.addContent(child);
                        }
                        rQueryEle.addContent(rqMoreInfoEle);
                    }
                }
            }
        }
        root.addContent(rQueryEle);
        return root;
    }

    public HashMap getParametersHashMap() {
        return ParametersHashMap;
    }

    public void setParametersHashMap(HashMap ParametersHashMap) {
        this.ParametersHashMap = ParametersHashMap;
    }

    public HashMap getTableHashMap() {
        return TableHashMap;
    }

    public void setTableHashMap(HashMap TableHashMap) {
        this.TableHashMap = TableHashMap;
    }

    public HashMap getGraphHashMap() {
        return GraphHashMap;
    }

    public void setGraphHashMap(HashMap GraphHashMap) {
        this.GraphHashMap = GraphHashMap;
    }

    public HashMap getReportHashMap() {
        return ReportHashMap;
    }

    public void setReportHashMap(HashMap ReportHashMap) {
        this.ReportHashMap = ReportHashMap;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public String getPortletDesc() {
        return portletDesc;
    }

    public void setPortletDesc(String portletDesc) {
        this.portletDesc = portletDesc;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public int getPortletId() {
        return portletId;
    }

    public void setPortletId(int portletId) {
        this.portletId = portletId;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String[] getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(String[] folderIds) {
        this.folderIds = folderIds;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Element buildKpiGraphStatus(Element root) {
        Element rQueryEle = new Element("KiGraphStatus");
        Element child = null;
        child = new Element("status");
        child.setText(getKpiGraphStatus);
        rQueryEle.addContent(child);

        root.addContent(rQueryEle);

        return root;

    }
}
