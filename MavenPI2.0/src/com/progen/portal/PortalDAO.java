/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.progen.charts.GraphProperty;
import com.progen.dashboard.DashboardTableColorGroupHelper;
import com.progen.rulesHelp.RuleProperty;
import com.progen.servlet.ServletUtilities;
import com.progen.servlet.ServletWriterTransferObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import net.sourceforge.jtds.jdbc.ClobImpl;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class PortalDAO extends PbDb {

    public static Logger logger = Logger.getLogger(PortalDAO.class);
    ResourceBundle resBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new PortalResBundleSqlServer();
            } else {
                resBundle = new PortalResourceBundle();
            }
        }

        return resBundle;
    }

    public int insertTabMasterDet(String[] userList) {

        int seq = 0;
        String insertTMDetQuery = getResourceBundle().getString("insertTabMasterDet");
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            try {
                seq = super.getSequenceNumber(" select PRG_PORTAL_TAB_MASTER_SEQ.NEXTVAL FROM DUAL");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            userList[0] = Integer.toString(seq);
        }
        String finalQuery = "";

        try {
            finalQuery = buildQuery(insertTMDetQuery, userList);
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                seq = insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_PORTAL_TAB_MASTER");
            } else {
                execModifySQL(finalQuery);
            }

        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return seq;
    }

    boolean insertPortalMaster(String[] insretList) {

        ArrayList alist = new ArrayList();
        String insertPMasterQuery = getResourceBundle().getString("insertPortalMaster");

        String finalQuery = "";
        boolean result = false;

        try {

            finalQuery = buildQuery(insertPMasterQuery, insretList);
            //////////////////////////////////////////////////////////////////.println("finalQuery is " + finalQuery);
            execModifySQL(finalQuery);

            result = true;
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }
    }
//old code to delete start

    boolean insertPortalDetails(String[] detailList) {

        ArrayList alist = new ArrayList();
        String insertPDetailsQuery = getResourceBundle().getString("insertPortalDetails");

        String finalQuery = "";
        boolean result = false;
        try {

            finalQuery = buildQuery(insertPDetailsQuery, detailList);
            ////////////////////////////////////////////////////////////////.println("finalQuery is " + finalQuery);
            execModifySQL(finalQuery);

            result = true;
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }

    }
//old code to delete end

    public int insertPortalDetails(ArrayList<String> portaletparmlList) {
        String insertPDetailsQuery = getResourceBundle().getString("insertPortalDetails");
        String finalQuery = "";
        Object portaletList[] = null;
        int seq = 0;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                portaletList = new Object[6];
                portaletList[0] = portaletparmlList.get(1);
                portaletList[1] = portaletparmlList.get(4);
                portaletList[2] = portaletparmlList.get(5);
                portaletList[3] = portaletparmlList.get(2);
                portaletList[4] = portaletparmlList.get(3);
                portaletList[5] = portaletparmlList.get(6);
            } else {
                seq = super.getSequenceNumber("SELECT PRG_PORTLETS_MASTER_SEQ.NEXTVAL FROM DUAL");
                portaletList = new Object[7];
                portaletList[0] = portaletparmlList.get(1);
                portaletList[1] = seq;
                portaletList[2] = portaletparmlList.get(4);
                portaletList[3] = portaletparmlList.get(5);
                portaletList[4] = portaletparmlList.get(2);
                portaletList[5] = portaletparmlList.get(3);
                portaletList[6] = portaletparmlList.get(6);

            }
            finalQuery = buildQuery(insertPDetailsQuery, portaletList);
            execModifySQL(finalQuery);


        } catch (Exception exception) {
            logger.error("Exception:", exception);

        }
        return seq;
    }

    boolean insertPortletMaster(String[] detailList) {

        ArrayList alist = new ArrayList();
        String insertPDetailsQuery = getResourceBundle().getString("addPortletMaster");

        String finalQuery = "";
        boolean result = false;
        try {

            finalQuery = buildQuery(insertPDetailsQuery, detailList);
            ////////////////////////////////////////////////////////////////////////.println("finalQuery is " + finalQuery);
            execModifySQL(finalQuery);

            result = true;
            return result;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return result;
        }

    }

    PbReturnObject checkPortalName() {
        String chheckPortalNameQuery = getResourceBundle().getString("checkPortalName");
        ////////////////////////////////////////////////////////////////////////.println("chheckPortalNameQuery"+chheckPortalNameQuery);
        try {
            PbReturnObject pbro = execSelectSQL(chheckPortalNameQuery);
            return pbro;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return null;
        }

    }

    PbReturnObject checkUserPortalExistance(String userId) {
        String chheckUserPortalQuery = getResourceBundle().getString("checkUserPortal");
        ////////////////////////////////////////////////////////////////////////.println("chheckUserPortalQuery"+chheckUserPortalQuery);
        String finalQuery = "";
        boolean result = false;
        Object obj[] = new Object[1];
        obj[0] = userId;
        try {
            finalQuery = buildQuery(chheckUserPortalQuery, obj);
            ////////////////////////////////////////////////////////////////////////.println("finalQuery is " + finalQuery);
            PbReturnObject pbro = execSelectSQL(finalQuery);
            return pbro;
        } catch (Exception exception) {
            logger.error("Exception:", exception);
            return null;
        }

    }

    public static void main(String args[]) {
        PortalDAO obj1 = new PortalDAO();

    }

    public List<Portal> buildPortal(int USERID) {
        String getPortalId = getResourceBundle().getString("getPortalId");
        Object object[] = new Object[1];
        object[0] = USERID;
        String finalQuery = super.buildQuery(getPortalId, object);
        PbReturnObject returnObject = new PbReturnObject();
        PbReturnObject ruleReturnObject = new PbReturnObject();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String columnNames[] = null;
        String busIdQry = "";
        PbDb pbdb = new PbDb();
        PortletRuleHelper ruleHelper;
        List<Portal> portals = new ArrayList<Portal>();
        try {
            returnObject = super.execSelectSQL(finalQuery);
            String ruleDetails = "";
            columnNames = returnObject.getColumnNames();
            for (int row = 0; row < returnObject.getRowCount(); row++) {
                Portal portal = new Portal(returnObject.getFieldValueInt(row, columnNames[0]), returnObject.getFieldValueString(row, columnNames[1]));
                ruleDetails = "select RULE_DETAILS from PRG_PORTAL_TAB_MASTER where PORTAL_ID = " + returnObject.getFieldValueInt(row, columnNames[0]);
                ruleReturnObject = super.execSelectSQL(ruleDetails);
                portal.setPortalDes(returnObject.getFieldValueString(row, columnNames[2]));
                portal.setPortalOrder(returnObject.getFieldValueInt(row, columnNames[3]));
                List<PortLet> portLets = buildPortlet(returnObject.getFieldValueInt(row, columnNames[0]), USERID);
                for (PortLet portLet : portLets) {
                    if (returnObject.getFieldValueString(row, columnNames[11]) != null && !returnObject.getFieldValueString(row, columnNames[11]).equalsIgnoreCase("") && returnObject.getFieldValueString(row, columnNames[11]).equalsIgnoreCase("True")) {
                        if (returnObject.getFieldValueString(row, columnNames[9]) != null && !returnObject.getFieldValueString(row, columnNames[9]).equalsIgnoreCase("")) {
                            busIdQry = "select FOLDER_ID from PRG_USER_PORTLETS_MASTER where PORTLET_ID=" + portLet.getPortLetId();
                            PbReturnObject portFolderId = pbdb.execSelectSQL(busIdQry);
                            for (int j = 0; j < portFolderId.getRowCount(); j++) {
                                if (returnObject.getFieldValueString(row, columnNames[9]).equals(portFolderId.getFieldValueString(j, 0))) {
                                    if (ruleReturnObject.getFieldValueString(row, columnNames[10]) != null && !ruleReturnObject.getFieldValueString(row, columnNames[10]).equalsIgnoreCase("")) {
                                        PortletRuleHelper portletRuleHelper;
                                        portletRuleHelper = gson.fromJson(ruleReturnObject.getFieldValueString(row, columnNames[10]), PortletRuleHelper.class);
                                        portLet.setPortletRuleHelper(portletRuleHelper);
                                    }
                                }
                            }
                        }
                    }
                    portal.addPortlet(portLet);
                }
                //
                portals.add(portal);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        Portal portal = new Portal(-1, "Desinger");
        List<PortLet> portLets = buildPortlet(-1, USERID);
        portal.getPortlets().addAll(portLets);
        portals.add(portal);
        portal = new Portal(-2, "RelPortlets");
        portLets.clear();
        portLets = buildPortlet(-1, USERID);
        portal.getPortlets().addAll(new ArrayList<PortLet>());
        portals.add(portal);
        return portals;
    }

    public List<PortLet> buildPortlet(int portalId, int userId) {
        String getPortletfromBaseTable = getResourceBundle().getString("getPortletfromBaseTable");
        String getPortletfromUserTable = getResourceBundle().getString("getPortletfromUserTable");
        String getRule = getResourceBundle().getString("getRule");
        Object object[] = new Object[2];
        object[0] = portalId;
        object[1] = userId;
        String PortletfromBaseTableQuery = super.buildQuery(getPortletfromBaseTable, new Object[]{portalId});
        String PortletfromUserTable = super.buildQuery(getPortletfromUserTable, object);
        PbReturnObject portletBaseDetailsObject = new PbReturnObject();
        PbReturnObject portletuserDetailsObject = new PbReturnObject();
        String[] columnNames = null;
        ArrayList<PortLet> portLets = new ArrayList<PortLet>();
        Document document = null;
        SAXBuilder builder = new SAXBuilder();
        Object[] ruleObject = new Object[1];
        PbReturnObject rulereReturnObject = new PbReturnObject();
        PbReturnObject elementDetaisObject = new PbReturnObject();
        Gson gson = new GsonBuilder().serializeNulls().create();
        RuleProperty ruleProperty = null;
        String getElementDetails = getResourceBundle().getString("getElementDetails");
        String finalQuery = "";
        try {
            portletBaseDetailsObject = super.execSelectSQL(PortletfromBaseTableQuery);
            portletuserDetailsObject = super.execSelectSQL(PortletfromUserTable);
            columnNames = portletBaseDetailsObject.getColumnNames();
            PbReturnObject tempReturnObject = new PbReturnObject();
            int tempRow = 0;
            for (int row = 0; row < portletBaseDetailsObject.getRowCount(); row++) {
                tempRow = row;
                tempReturnObject = portletBaseDetailsObject;
                for (int j = 0; j < portletuserDetailsObject.getRowCount(); j++) {
                    if (portletuserDetailsObject.getFieldValueInt(j, columnNames[0]) == portletBaseDetailsObject.getFieldValueInt(row, columnNames[0])) {
                        tempReturnObject = portletuserDetailsObject;
                        tempRow = row;
                        row = j;
                        break;
                    }
                }

                PortLet portLet = new PortLet(tempReturnObject.getFieldValueInt(row, columnNames[0]), tempReturnObject.getFieldValueString(row, columnNames[1]));
                ruleObject[0] = tempReturnObject.getFieldValueInt(row, columnNames[0]);
                finalQuery = super.buildQuery(getRule, ruleObject);
                rulereReturnObject = super.execSelectSQL(finalQuery);

                portLet.setPortLetDes(tempReturnObject.getFieldValueString(row, columnNames[2]));
                portLet.setColumnNumber(tempReturnObject.getFieldValueInt(row, columnNames[5]));
                portLet.setSeqnumber(tempReturnObject.getFieldValueInt(row, columnNames[6]));
                if (rulereReturnObject.getRowCount() > 0) {
                    ruleProperty = gson.fromJson(rulereReturnObject.getFieldValueClobString(0, "RULE_DETAILS"), RuleProperty.class);
                    if (ruleProperty.getRuleOn() != null && !ruleProperty.getRuleOn().equalsIgnoreCase("Dimension")) {
                        portLet.setWhereClause(ruleProperty.getActualRule());
                    } else {
                        portLet.setWhereClause("");
                    }
                    portLet.setMeasureID(ruleProperty.getMeasureIDs());
                    portLet.setRuleOn(ruleProperty.getRuleOn());
                    portLet.setReportParams(ruleProperty.getReportParms());
                    ruleObject[0] = Joiner.on(",").join(ruleProperty.getMeasureIDs());
                    finalQuery = super.buildQuery(getElementDetails, ruleObject);
                    if (ruleProperty.getMeasureIDs().length > 1) {
                        elementDetaisObject = super.execSelectSQL(finalQuery);
                    }
                    if (elementDetaisObject != null && elementDetaisObject.getRowCount() > 0) {
                        String[] aggregationType = new String[elementDetaisObject.getRowCount()];
                        String[] measureNames = new String[elementDetaisObject.getRowCount()];
                        for (int i = 0; i < elementDetaisObject.getRowCount(); i++) {
                            aggregationType[i] = elementDetaisObject.getFieldValueString(i, "AGGREGATION_TYPE");
                            measureNames[i] = elementDetaisObject.getFieldValueString(i, "USER_COL_DESC");
                        }
                        portLet.setAggreGationType(aggregationType);
                        portLet.setMeasureNames(measureNames);
                    }

                }
                if (tempReturnObject.getFieldValueString(row, columnNames[4]).equalsIgnoreCase("I")) {
                    if (tempReturnObject.getFieldValueClobString(row, columnNames[3]) != null) {
                        try {

                            document = builder.build(new ByteArrayInputStream(tempReturnObject.getFieldValueClobString(row, columnNames[3]).trim().getBytes()));

                        } catch (JDOMException ex) {
                            logger.error("Exception:", ex);
                        } catch (IOException ex) {
                            logger.error("Exception:", ex);
                        }
                        portLet.setXMLDocument(document);
                    } else {
                        portLet.setXMLDocument(null);
                    }
                } else {
                    portLet.setXmlString((new StringBuilder(tempReturnObject.getFieldValueClobString(row, columnNames[3]))).toString());
                }
                portLet.setPortletType(tempReturnObject.getFieldValueString(row, columnNames[4]));
                portLet.setIsSharable(tempReturnObject.getFieldValueString(row, columnNames[7]));
                if (tempReturnObject.getFieldValueString(row, columnNames[11]) != null) {
                    portLet.setSortOrder(tempReturnObject.getFieldValueString(row, columnNames[9]));
                }
                portLet.setFolderId(tempReturnObject.getFieldValueInt(row, columnNames[8]));
                if (tempReturnObject.getFieldValueClobString(row, columnNames[12]) != null) {
                    GraphProperty graphProperty = null;
                    graphProperty = gson.fromJson(tempReturnObject.getFieldValueClobString(row, columnNames[12]), GraphProperty.class);
                    portLet.setGraphProperty(graphProperty);
                }
                if (tempReturnObject.getFieldValueString(row, columnNames[10]) != null || !tempReturnObject.getFieldValueString(row, columnNames[10]).equalsIgnoreCase("")) {
                    portLet.setPortletHeight(tempReturnObject.getFieldValueInt(row, columnNames[10]));


                }
                //relatedPortal
                if (tempReturnObject.getFieldValueString(row, columnNames[13]) != null || !tempReturnObject.getFieldValueString(row, columnNames[10]).equalsIgnoreCase("")) {
                    Set<String> relatedPortlets = new HashSet<String>();
                    String[] selectedPortlets = tempReturnObject.getFieldValueString(row, columnNames[13]).split(",");
                    for (String s : selectedPortlets) {
                        relatedPortlets.add(s);
                    }
                    portLet.setRelatedPortlets(relatedPortlets);

                    if (tempReturnObject.getFieldValueString(row, columnNames[10]) != null || !tempReturnObject.getFieldValueString(row, columnNames[10]).equalsIgnoreCase("")) {
                        portLet.setPortletHeight(tempReturnObject.getFieldValueInt(row, columnNames[10]));
                    }
                    if (tempReturnObject.getFieldValueClobString(row, columnNames[14]) != null && !tempReturnObject.getFieldValueClobString(row, columnNames[14]).equalsIgnoreCase("")) {
                        PortLetTimeHelper portletTimeHelper = null;
                        portletTimeHelper = gson.fromJson(tempReturnObject.getFieldValueClobString(row, columnNames[14]), PortLetTimeHelper.class);
                        portLet.setPortLetTimeHelper(portletTimeHelper);

                    }
                    if (tempReturnObject.getFieldValueString(row, columnNames[15]) != null && !tempReturnObject.getFieldValueString(row, columnNames[15]).equalsIgnoreCase("")) {

                        PortLetSorterHelper portLetSorterHelper;
                        portLetSorterHelper = gson.fromJson(tempReturnObject.getFieldValueString(row, columnNames[15]), PortLetSorterHelper.class);
                        portLet.setPortletSorterHelper(portLetSorterHelper);
                    }
                    if (tempReturnObject.getFieldValueString(row, columnNames[16]) != null && !tempReturnObject.getFieldValueString(row, columnNames[16]).equalsIgnoreCase("")) {
                        List<DashboardTableColorGroupHelper> tableColorGroupHelpers = null;
                        Type listType = new TypeToken<ArrayList<DashboardTableColorGroupHelper>>() {
                        }.getType();
                        tableColorGroupHelpers = new Gson().fromJson(tempReturnObject.getFieldValueString(row, columnNames[16]), listType);
                        portLet.setPortalTableColor(tableColorGroupHelpers);
                    }
//                 if(tempReturnObject.getFieldValueString(row, columnNames[17])!=null && !tempReturnObject.getFieldValueString(row, columnNames[17]).equalsIgnoreCase(""))
//                 {
//                      PortletRuleHelper portletRuleHelper;
//                      portletRuleHelper = gson.fromJson(tempReturnObject.getFieldValueString(row,columnNames[17]),PortletRuleHelper.class);
//                      portLet.setPortletRuleHelper(portletRuleHelper);
//                 }
                    portLets.add(portLet);
                    row = tempRow;
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return portLets;
    }

    public PbReturnObject getPortlets() {
        String query = "select PORTLET_ID, PORTLET_NAME, PORTLET_DESC, PORTLET_TYPE, PORTLET_REPORT_TYPE, PU_FIRSTNAME, UPDATE_ON from PRG_BASE_PORTLETS_MASTER,"
                + "PRG_AR_USERS where PU_ID=UPDATE_BY AND XML_PATH IS NOT NULL ";
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public int getNewPortletId() {
        String query = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            query = "SELECT IDENT_CURRENT('PRG_BASE_PORTLETS_MASTER')";
        } else {
            query = "select PRG_BASE_PORTLETS_MASTER_SEQ.nextval from dual";
        }
        int portletId = 0;
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
            BigDecimal bd = retObj.getFieldValueBigDecimal(0, 0);
            if (bd != null) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    portletId = bd.intValue();
                    portletId = portletId + 1;
                } else {
                    portletId = bd.intValue();

                }
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
        }
        return portletId;
    }

    public String getAllPortlets(String userid, String portalID) {
        String getRoles = getResourceBundle().getString("getRoles");
        Object[] objects = new Object[1];
        objects[0] = userid;
        String finalQuery = super.buildQuery(getRoles, objects);
        PbReturnObject pbReturnObject = new PbReturnObject();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            pbReturnObject = super.execSelectSQL(finalQuery);

            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {
                stringBuilder.append(getAllPortlets(pbReturnObject.getFieldValueInt(i, "USER_FOLDER_ID"), pbReturnObject.getFieldValueString(i, "FOLDER_NAME"), portalID));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return stringBuilder.toString();
    }

    public String getAllPortlets(int roleid, String folderName, String portalID) {
        String getPortlets = getResourceBundle().getString("getPortlets");
        Object[] objects = new Object[2];
        objects[0] = roleid;
        objects[1] = portalID;
        String finalQuery = super.buildQuery(getPortlets, objects);
        PbReturnObject pbReturnObject = new PbReturnObject();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            pbReturnObject = super.execSelectSQL(finalQuery);
            stringBuilder.append("<ul><li id='" + roleid + "'>");
            stringBuilder.append("<span class='folder'>" + folderName + "</span>");
            stringBuilder.append("<ul>");

            for (int i = 0; i < pbReturnObject.getRowCount(); i++) {

//                        stringBuilder.append("<li class='closed' id='" +pbReturnObject.getFieldValueString(i, "PORTLET_ID") + "'>");
//                        stringBuilder.append("<img src='icons pinvoke/chart.png'></img>");
//                        stringBuilder.append("<span id='portlet-"+pbReturnObject.getFieldValueString(i, "PORTLET_ID")+"' style='font-family:verdana;font-size:8pt'><a href=javascript:buildPortlet('" +pbReturnObject.getFieldValueString(i, "PORTLET_ID")+ "') style='text-Decoration:none'>" +pbReturnObject.getFieldValueString(i, "PORTLET_NAME")+ "</a></span>");
//                        stringBuilder.append("</li>");
                stringBuilder.append("<li><span class='file' style='cursor: pointer' onclick=buildPortletPreview('" + pbReturnObject.getFieldValueString(i, "PORTLET_ID") + "')>" + pbReturnObject.getFieldValueString(i, "PORTLET_NAME") + "</span></li>");

            }
            stringBuilder.append("</ul></li></ul>");

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return stringBuilder.toString();

    }

    public PbReturnObject getPortletsFormDesign(String portletID) {
        String getPortlesformDesign = getResourceBundle().getString("getPortlesformDesign");
        String finalQuery = "";
        PbReturnObject portleObject = new PbReturnObject();
        Object[] objects = new Object[1];
        objects[0] = portletID;
        finalQuery = super.buildQuery(getPortlesformDesign, objects);
        try {
            portleObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return portleObject;
    }

    public boolean portletAssign(String Portletid, String portalId, String portletHeight, String userID) {
        String portletAssign = getResourceBundle().getString("portletAssign");
        String savePortletQuery = "";
        String finalQuery = "";
        Object[] objects = null;
        boolean status = false;
        PbReturnObject pbReturnObject = new PbReturnObject();
        try {
            pbReturnObject = super.execSelectSQL("Select * from PRG_PORTAL_PORTLETS_ASSIGN where PORTL_ID = -1 AND PORTLET_ID=" + Portletid + " AND USER_ID= " + userID);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbReturnObject.getRowCount() > 0) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                finalQuery = "INSERT INTO PRG_PORTAL_PORTLETS_ASSIGN (PORTL_ID,PORTLET_ID,COLUMN_NUM,SEQUENCE_NUM,PORTLET_HEIGHT,USER_ID,IS_SHARABLE,SORTLEVEL,GRAPHPROPERTY) SELECT " + portalId + ",PORTLET_ID,COLUMN_NUM,SEQUENCE_NUM,PORTLET_HEIGHT,USER_ID,IS_SHARABLE,SORTLEVEL,GRAPHPROPERTY  FROM PRG_PORTAL_PORTLETS_ASSIGN WHERE PORTL_ID=-1 AND PORTLET_ID =" + Portletid + " AND USER_ID=" + userID;
            } else {
                finalQuery = "INSERT INTO PRG_PORTAL_PORTLETS_ASSIGN (ASSIGN_ID,PORTL_ID,PORTLET_ID,COLUMN_NUM,SEQUENCE_NUM,PORTLET_HEIGHT,USER_ID,IS_SHARABLE,SORTLEVEL,GRAPHPROPERTY) SELECT PRG_PORTAL_PORTLETS_ASSIGN_SEQ.NEXTVAL," + portalId + ",PORTLET_ID,COLUMN_NUM,SEQUENCE_NUM,PORTLET_HEIGHT,USER_ID,IS_SHARABLE,SORTLEVEL,GRAPHPROPERTY  FROM PRG_PORTAL_PORTLETS_ASSIGN WHERE PORTL_ID=-1 AND PORTLET_ID =" + Portletid + " AND USER_ID=" + userID;
            }
            savePortletQuery = "INSERT INTO PRG_USER_PORTLETS_MASTER(USER_ID,PORTLET_ID,PORTLET_NAME,PORTLET_DESC,CREATED_BY,CREATED_ON,UPDATE_BY,UPDATE_ON,PORTLET_TYPE,FOLDER_ID,XML_PATH,PORTAL_ID) SELECT  " + userID + ",PORTLET_ID,PORTLET_NAME,PORTLET_DESC,CREATED_BY,CREATED_ON,UPDATE_BY,UPDATE_ON,PORTLET_TYPE,FOLDER_ID, XML_PATH," + portalId + " FROM prg_base_portlets_master WHERE PORTLET_ID =" + Portletid;
            try {
                super.execModifySQL(savePortletQuery);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } else {
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            objects = new Object[5];
//            objects[0] = Portletid;
//            objects[1] = 1;
//            objects[2] = 1;
//            objects[3] = 0;
//            objects[4] = userID;
//        } else {
            objects = new Object[6];
            objects[0] = portalId;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                objects[1] = "IDENT_CURRENT('PRG_BASE_PORTLETS_MASTER')";
            } else {
                objects[1] = Portletid;
            }
            objects[2] = 1;
            objects[3] = 1;
            objects[4] = 0;
            objects[5] = userID;
//        }
            finalQuery = super.buildQuery(portletAssign, objects);
        }

        try {
            super.execModifySQL(finalQuery);
            status = true;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            status = false;
        }
        return status;
    }

    public boolean isPortletAssign(String Portletid, String portalId, String userId) {
        String isPortletAssign = getResourceBundle().getString("isPortletAssign");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = portalId;
        objects[1] = Portletid;
        objects[2] = userId;
        boolean status = false;
        finalQuery = super.buildQuery(isPortletAssign, objects);
        PbReturnObject returnObject = new PbReturnObject();
        try {
            returnObject = super.execSelectSQL(finalQuery);
            if (returnObject.getRowCount() > 0) {
                status = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return status;
    }

    public void savePortletProperties(String portalId, String portletId, String gsonString) {
        String savePortletProperties = getResourceBundle().getString("savePortletProperties");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = gsonString;
        objects[1] = portalId;
        objects[2] = portletId;
        finalQuery = super.buildQuery(savePortletProperties, objects);
        try {
            super.execModifySQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public boolean saveXmalOfPortlet(String portletXML, PortLet portLet, String userId, String portalId, String saveStatus, String nameofNewPortlet, String sortlevel) throws Exception {

        int portLetId = portLet.getPortLetId();
        boolean status = false;
        String finalQuery = "";
        Object[] objects = null;
        int updateId = 0;
        ArrayList<String> queryList = new ArrayList<String>();
        String saveAsNewPortlet = getResourceBundle().getString("saveAsNewPortlet");
        String saveXmalOfPortlet = getResourceBundle().getString("saveXmalOfPortlet");
        String updateAssignSortLevel = getResourceBundle().getString("updateAssignSortLevel");
        List<DashboardTableColorGroupHelper> portalTableColor = portLet.getPortalTableColor();
        PortLetSorterHelper sorthelper = portLet.getPortletSorterHelper();
//           PortletRuleHelper rulehelper=portLet.getPortletRuleHelper();
        Gson gson = new Gson();
        String portalColor = "";
        String portalSort = "";
//           String ruleDetail="";
        if (!portalTableColor.isEmpty()) {
            portalColor = gson.toJson(portalTableColor);
        }
        if (sorthelper != null) {
            portalSort = gson.toJson(sorthelper);
        }
//           if(rulehelper!=null)
//               ruleDetail=gson.toJson(rulehelper);

        if (saveStatus.equalsIgnoreCase("saveAsNew")) {
            objects = new Object[6];
            objects[0] = nameofNewPortlet;
            objects[1] = nameofNewPortlet;
            objects[2] = userId;
            objects[3] = userId;
            objects[4] = portLet.getPortletXMLHelper().getMetaInfo().get("DisplayType");
            objects[5] = portLetId;
            finalQuery = super.buildQuery(saveAsNewPortlet, objects);
            ArrayList queryListforSqlServer = new ArrayList();
            try {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    super.execModifySQL(finalQuery);
                    objects = new Object[4];
                    objects[0] = userId;
                    objects[1] = portletXML;
                    objects[2] = portalId;
                    objects[3] = "IDENT_CURRENT('PRG_BASE_PORTLETS_MASTER')";
                } else {
                    updateId = super.execInsertSqlReturnSEQ(finalQuery, "PRG_BASE_PORTLETS_MASTER_SEQ");
                    objects = new Object[4];
                    objects[0] = userId;
                    objects[1] = "not_generated";
                    objects[2] = portalId;
                    objects[3] = updateId;
                }
                if (!portalId.equalsIgnoreCase("-1")) {
                    finalQuery = super.buildQuery(saveXmalOfPortlet, objects);
                } else if (portalId.equalsIgnoreCase("-1")) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        finalQuery = "update PRG_BASE_PORTLETS_MASTER set XML_PATH='" + portletXML + "' WHERE PORTLET_ID=IDENT_CURRENT('PRG_BASE_PORTLETS_MASTER')";
                    } else {
                        finalQuery = "update PRG_BASE_PORTLETS_MASTER set XML_PATH='not_generated' WHERE PORTLET_ID=" + updateId;
                    }
                } else {
                    finalQuery = "update PRG_BASE_PORTLETS_MASTER set XML_PATH='not_generated' WHERE PORTLET_ID=" + updateId;
                }
                portletAssign(Integer.toString(updateId), portalId, null, userId);
                setPortalColors(portalColor, portalId, Integer.toString(updateId));
                updatePortLetSorterHelper(String.valueOf(updateId), portalId, portalSort);
//             updatePortLetRuleHelper(String.valueOf(updateId), portalId, ruleDetail);
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    super.execModifySQL(finalQuery);
                    status = true;
                }
                // 
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
//             Object[] updateObject =new  Object[3];
//          updateObject[0]=sortlevel;
//          updateObject[1]=portalId;
//          updateObject[2]=updateId;
//          super.execModifySQL(buildQuery(updateAssignSortLevel, updateObject));
        } else {
            if (!portalId.equalsIgnoreCase("-1")) {
                updateId = portLetId;
                String delePortlet = "DELETE FROM PRG_USER_PORTLETS_MASTER WHERE PORTAL_ID=" + portalId + "  AND PORTLET_ID=" + portLetId;
                queryList.add(delePortlet);
                objects = new Object[4];
                objects[0] = userId;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    objects[1] = portletXML;
                } else {
                    objects[1] = "not_generated";
                }
                objects[2] = portalId;
                objects[3] = portLetId;
                finalQuery = super.buildQuery(saveXmalOfPortlet, objects);
                setPortalColors(portalColor, portalId, Integer.toString(updateId));
                updatePortLetSorterHelper(String.valueOf(updateId), portalId, portalSort);
//        updatePortLetRuleHelper(String.valueOf(updateId), portalId, ruleDetail);
                //   
            } else {
                updateId = portLetId;
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    finalQuery = "update PRG_BASE_PORTLETS_MASTER set XML_PATH='" + portletXML + "' WHERE PORTLET_ID=" + portLetId;
                } else {
                    finalQuery = "update PRG_BASE_PORTLETS_MASTER set XML_PATH='not_generated' WHERE PORTLET_ID=" + portLetId;
                }
            }
        }
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)){
//           super.execModifySQL(finalQuery);
//        }else{
        try {
            Object[] updateObject = new Object[5];
            if (!sortlevel.equalsIgnoreCase("")) {
                updateObject[0] = sortlevel;
            } else {
                updateObject[0] = null;
            }
            updateObject[1] = portalId;
            updateObject[2] = portLetId;
            updateObject[3] = portalId;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                updateObject[4] = "IDENT_CURRENT('PRG_BASE_PORTLETS_MASTER')";
            } else {
                updateObject[4] = updateId;
            }
            queryList.add(buildQuery(updateAssignSortLevel, updateObject));
            if (!finalQuery.equalsIgnoreCase("")) {
                queryList.add(finalQuery);
                status = super.executeMultiple(queryList);
            }
            if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                ServletWriterTransferObject swt = null;
                swt = ServletUtilities.createBufferedWriter(portalId + "-" + portLetId, "xml");
                Writer writer = swt.writer;
                writer.write(portletXML);
                writer.flush();
                writer.close();
                swt.setReportName(portalId + "-" + portLetId);
                Reader reader = null;
                reader = ServletUtilities.createBufferedReader(swt.fileName);
                Clob clobObj = updatePortletXml(reader, updateId, portalId);
                reader.close();
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
//    }
        return status;
    }

    public Clob updatePortletXml(Reader reader, int portLetId, String portalId) {
        ClobImpl clob = null;
        String portletsXmlQuery = "";
        String portletQuery = "";
        if (!portalId.equalsIgnoreCase("-1")) {
            portletsXmlQuery = getResourceBundle().getString("updateXml1");
            portletQuery = getResourceBundle().getString("getXmlClob");
        } else {
            portletsXmlQuery = getResourceBundle().getString("updateXml2");
            portletQuery = getResourceBundle().getString("getXmlClob1");
        }
        String finalQuery = "";
        PbDb pbDb = new PbDb();
        Object[] obj = new Object[2];
        obj[0] = portLetId;
        obj[1] = portalId;
        if (!portalId.equalsIgnoreCase("-1")) {
            finalQuery = pbDb.buildQuery(portletsXmlQuery, new Object[]{portLetId, portalId});

            portletQuery = super.buildQuery(portletQuery, new Object[]{portLetId, portalId});
        } else {
            finalQuery = pbDb.buildQuery(portletsXmlQuery, new Object[]{portLetId});
            portletQuery = super.buildQuery(portletQuery, new Object[]{portLetId});
        }
        Connection conn = null;
        OraclePreparedStatement opstmt = null;
        PreparedStatement sqlstmt = null;
        CLOB clobLoc = null;
        try {
            pbDb.execModifySQL(finalQuery);

        } catch (Exception ex) {
//            logger.error("Exception:",ex);
        }


        try {
            // 
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
            String updateQuery = "";
            if (!portalId.equalsIgnoreCase("-1")) {
                updateQuery = getResourceBundle().getString("updateXml");
            } else {
                updateQuery = getResourceBundle().getString("updateXml1");
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {

                if (clobLoc != null) {
                    conn = ProgenConnection.getInstance().getConnection();

                    opstmt = (OraclePreparedStatement) conn.prepareStatement(updateQuery);
                    opstmt.setCLOB(1, clobLoc);
                    opstmt.setInt(2, portLetId);
                    if (!portalId.equalsIgnoreCase("-1")) {
                        opstmt.setInt(3, Integer.parseInt(portalId));
                    }
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

                    sqlstmt = (PreparedStatement) conn.prepareStatement(updateQuery);
                    sqlstmt.setClob(1, clob);
                    sqlstmt.setInt(2, portLetId);
                    sqlstmt.setInt(3, Integer.parseInt(portalId));
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
                logger.error("Exception:", ex);
            }
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            return clob;
        } else {
            return clobLoc;
        }

    }

    public int updatePortletTimeDetails(String portletId, String portalTabId, String gsonString) {
        int flag = 0;
        String updatePortletTimeDetails = getResourceBundle().getString("updatePortletTimeDetails");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = gsonString;
        objects[1] = portalTabId;
        objects[2] = portletId;
        finalQuery = super.buildQuery(updatePortletTimeDetails, objects);
        try {
            flag = super.execUpdateSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return flag;

    }

    public int resetPortletTimeDetails(String portletId, String portalTabId) {
        int flag = 0;
        String resetPortletTimeDetails = getResourceBundle().getString("resetPortletTimeDetails");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = null;
        objects[1] = portalTabId;
        objects[2] = portletId;
        finalQuery = super.buildQuery(resetPortletTimeDetails, objects);
        try {
            flag = super.execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return flag;
    }

    public int updatePortLetSorterHelper(String PortletId, String portalTabId, String gsonstring) {
        int flag = 0;
        String updatePortLetSorterHelper = getResourceBundle().getString("updatePortLetSorterHelper");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = gsonstring;
        objects[1] = portalTabId;
        objects[2] = PortletId;
        finalQuery = super.buildQuery(updatePortLetSorterHelper, objects);
        try {
            flag = super.execUpdateSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return flag;
    }

    public void updateRelatedPortals(String PortalTabId, String PortLetId, String reletedIds) {
        String updateQuery = "UPDATE PRG_PORTAL_PORTLETS_ASSIGN SET RELATED_PORTLETS='&' WHERE PORTL_ID='&' AND PORTLET_ID='&'";
        Object object[] = new Object[3];
        object[0] = reletedIds;
        object[1] = PortalTabId;
        object[2] = PortLetId;
        try {
            super.execModifySQL(buildQuery(updateQuery, object));
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void saveTargetValueOfKpi(String KpiDetails, String portletId, String portalTabId) {
        String insertQuery = "saveTargetValue";
        Object object[] = new Object[3];
        object[0] = KpiDetails;
        object[1] = portletId;
        object[2] = portalTabId;
        try {
            super.execModifySQL(buildQuery(insertQuery, object));
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public void setPortalColors(String PortalColors, String PortalId, String PortletId) {
        String updatePortletColors = getResourceBundle().getString("updatePortletColors");
        Object object[] = new Object[3];
        object[0] = PortalColors;
        object[1] = PortalId;
        object[2] = PortletId;
        String finalQuery = "";
        try {
            finalQuery = buildQuery(updatePortletColors, object);
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject resetTablePortlet(String portletId, String portalId, String userId) {
        String resetTablePortlet = getResourceBundle().getString("resetTablePortlet");
        Object object[] = new Object[3];
        object[0] = portalId;
        object[1] = userId;
        object[2] = portletId;
        PbReturnObject returnObject = new PbReturnObject();
        String PortletfromUserTable = super.buildQuery(resetTablePortlet, object);
        try {
            returnObject = super.execSelectSQL(PortletfromUserTable);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return returnObject;
    }

    public PortLet getPortlet(String portletId, String portalId, String userId) throws SQLException {
        PbReturnObject resetTableObject = this.resetTablePortlet(portletId, portalId, userId);
        return this.getSinglePortlet(resetTableObject);
    }

    public PortLet getSinglePortlet(PbReturnObject tempReturnObject) throws SQLException {
        PortLet portLet = new PortLet(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[0]), tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[1]));
        String getRule = getResourceBundle().getString("getRule");
        Object[] ruleObject = new Object[1];
        ruleObject[0] = tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[0]);
        String finalQuery = super.buildQuery(getRule, ruleObject);
        PbReturnObject rulereReturnObject = super.execSelectSQL(finalQuery);
        portLet.setPortLetDes(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[2]));
        portLet.setColumnNumber(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[5]));
        portLet.setSeqnumber(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[6]));
        Gson gson = new GsonBuilder().serializeNulls().create();
        RuleProperty ruleProperty = null;
        String getElementDetails = getResourceBundle().getString("getElementDetails");
        PbReturnObject elementDetaisObject = new PbReturnObject();
        Document document = null;
        SAXBuilder builder = new SAXBuilder();
        if (rulereReturnObject.getRowCount() > 0) {
            ruleProperty = gson.fromJson(rulereReturnObject.getFieldValueClobString(0, "RULE_DETAILS"), RuleProperty.class);
            if (ruleProperty.getRuleOn() != null && !ruleProperty.getRuleOn().equalsIgnoreCase("Dimension")) {
                portLet.setWhereClause(ruleProperty.getActualRule());
            } else {
                portLet.setWhereClause("");
            }
            portLet.setMeasureID(ruleProperty.getMeasureIDs());
            portLet.setRuleOn(ruleProperty.getRuleOn());
            portLet.setReportParams(ruleProperty.getReportParms());
            ruleObject[0] = Joiner.on(",").join(ruleProperty.getMeasureIDs());
            finalQuery = super.buildQuery(getElementDetails, ruleObject);
            if (ruleProperty.getMeasureIDs().length > 1) {
                elementDetaisObject = super.execSelectSQL(finalQuery);
            }
            if (elementDetaisObject != null && elementDetaisObject.getRowCount() > 0) {
                String[] aggregationType = new String[elementDetaisObject.getRowCount()];
                String[] measureNames = new String[elementDetaisObject.getRowCount()];
                for (int i = 0; i < elementDetaisObject.getRowCount(); i++) {
                    aggregationType[i] = elementDetaisObject.getFieldValueString(i, "AGGREGATION_TYPE");
                    measureNames[i] = elementDetaisObject.getFieldValueString(i, "USER_COL_DESC");
                }
                portLet.setAggreGationType(aggregationType);
                portLet.setMeasureNames(measureNames);
            }

        }
        if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[4]).equalsIgnoreCase("I")) {
            if (tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[3]) != null) {
                try {

                    document = builder.build(new ByteArrayInputStream(tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[3]).trim().getBytes()));

                } catch (JDOMException ex) {
                    logger.error("Exception:", ex);
                } catch (IOException ex) {
                    logger.error("Exception:", ex);
                }
                portLet.setXMLDocument(document);
            } else {
                portLet.setXMLDocument(null);
            }
        } else {
            portLet.setXmlString((new StringBuilder(tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[3]))).toString());
        }
        portLet.setPortletType(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[4]));
        portLet.setIsSharable(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[7]));
        if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[11]) != null) {
            portLet.setSortOrder(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[9]));
        }
        portLet.setFolderId(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[8]));
        if (tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[12]) != null) {
            GraphProperty graphProperty = null;
            graphProperty = gson.fromJson(tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[12]), GraphProperty.class);
            portLet.setGraphProperty(graphProperty);
        }
        if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[10]) != null || !tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[10]).equalsIgnoreCase("")) {
            portLet.setPortletHeight(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[10]));
        }
        //relatedPortal
        if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[13]) != null || !tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[10]).equalsIgnoreCase("")) {
            Set<String> relatedPortlets = new HashSet<String>();
            String[] selectedPortlets = tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[13]).split(",");
            for (String s : selectedPortlets) {
                relatedPortlets.add(s);
            }
            portLet.setRelatedPortlets(relatedPortlets);

            if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[14]) != null || !tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[14]).equalsIgnoreCase("")) {
                portLet.setPortletHeight(tempReturnObject.getFieldValueInt(0, tempReturnObject.getColumnNames()[14]));
            }
            if (tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[15]) != null && !tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[15]).equalsIgnoreCase("")) {
                PortLetTimeHelper portletTimeHelper = null;
                portletTimeHelper = gson.fromJson(tempReturnObject.getFieldValueClobString(0, tempReturnObject.getColumnNames()[15]), PortLetTimeHelper.class);
                portLet.setPortLetTimeHelper(portletTimeHelper);

            }
            if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[16]) != null && !tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[16]).equalsIgnoreCase("")) {
                PortLetSorterHelper portLetSorterHelper;
                portLetSorterHelper = gson.fromJson(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[16]), PortLetSorterHelper.class);
                portLet.setPortletSorterHelper(portLetSorterHelper);
            }
            if (tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[17]) != null && !tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[17]).equalsIgnoreCase("")) {
                List<DashboardTableColorGroupHelper> tableColorGroupHelpers = null;
                Type listType = new TypeToken<ArrayList<DashboardTableColorGroupHelper>>() {
                }.getType();
                tableColorGroupHelpers = new Gson().fromJson(tempReturnObject.getFieldValueString(0, tempReturnObject.getColumnNames()[17]), listType);
                portLet.setPortalTableColor(tableColorGroupHelpers);
            }
        }
        return portLet;
    }

    public HashMap getMesureInfoDetails(String measureIds) {

        String mIds[] = measureIds.split(",");
        String getElementDetails = getResourceBundle().getString("getElementDetails");
        Object[] values = new Object[1];
        String finalQuery;
        String aggregationType;
        String measureNames;
        HashMap<String, ArrayList> elementDetails = new HashMap<String, ArrayList>();

        for (int i = 0; i < mIds.length; i++) {
            ArrayList edetails = new ArrayList();
            values[0] = mIds[i];
            finalQuery = buildQuery(getElementDetails, values);
            try {
                PbReturnObject elementDetais = execSelectSQL(finalQuery);
                if (elementDetais != null && elementDetais.getRowCount() > 0) {
                    aggregationType = elementDetais.getFieldValueString(0, "AGGREGATION_TYPE");
                    measureNames = elementDetais.getFieldValueString(0, "USER_COL_DESC");
                    edetails.add(mIds[i]);
                    edetails.add(aggregationType);
                    edetails.add(measureNames);
                    elementDetails.put(mIds[i], edetails);

                }

            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }


        return elementDetails;

    }

    public boolean deleteEmptySelectedPortlets(String portletId, String portalTabId, String portletNames) {
        String finalQuery = "";
        boolean status = false;
        Object obj[] = new Object[1];
        ArrayList<String> queries = new ArrayList<String>();
        String query = null;
        query = "delete from PRG_USER_PORTLETS_MASTER where PORTLET_ID in (" + portletId + ") AND PORTAL_ID=" + portalTabId + "";
        queries.add(query);
        query = "delete from PRG_PORTAL_PORTLETS_ASSIGN where PORTLET_ID in (" + portletId + ") AND PORTL_ID=" + portalTabId + "";
        queries.add(query);
        PbDb pbdb = new PbDb();
        status = pbdb.executeMultiple(queries);
        if (status == true) {
            return true;
        } else {
            return false;
        }
    }

    public int updatePortLetRuleHelper(String PortletId, String portalTabId, String gsonstring) {
        int flag = 0;
        String updatePortLetRuleHelper = getResourceBundle().getString("updatePortLetRuleHelper");
        String finalQuery = "";
        Object[] objects = new Object[3];
        objects[0] = gsonstring;
        objects[1] = portalTabId;
        objects[2] = PortletId;
        finalQuery = super.buildQuery(updatePortLetRuleHelper, objects);
        try {
            flag = super.execUpdateSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return flag;
    }

    public int updatePortlalFilterProperties(String BusinessRole, String ruleDetail, boolean portalFilterFlag, String SelectedMeasure, String DimName, String currentTabId, String userId) {
        int flag = 0;
        String updatePortLetRuleHelper = getResourceBundle().getString("updatePortLetRuleHelper");
        String finalQuery = "";
        Object[] objects = new Object[7];
        objects[0] = BusinessRole;
        objects[1] = ruleDetail;
        objects[2] = portalFilterFlag;
        objects[3] = SelectedMeasure;
        objects[4] = DimName;
        objects[5] = currentTabId;
        objects[6] = userId;
        finalQuery = super.buildQuery(updatePortLetRuleHelper, objects);
        try {
            flag = super.execUpdateSQL(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return flag;
    }

    public String getAllPortletsForOneView(String userId) {
        String getAllBasePortlets = getResourceBundle().getString("getAllBasePortlets");
        String getAllUserPortlets = getResourceBundle().getString("getAllUserPortlets");
        Object[] objects = new Object[1];
        String[] baseColNames = null;
        String[] userColNames = null;
        String jsonString = null;
        Gson gson = new Gson();
        HashMap hmap = new HashMap();
        objects[0] = userId;
        String baseFinalQuery = super.buildQuery(getAllBasePortlets, objects);
        String userFinalQuery = super.buildQuery(getAllUserPortlets, objects);
        PbReturnObject basepbReturnObject = new PbReturnObject();
        PbReturnObject userpbReturnObject = new PbReturnObject();
        ArrayList<String> baseIds = new ArrayList<String>();
        ArrayList<String> baseNames = new ArrayList<String>();
        ArrayList<String> userTabIds = new ArrayList<String>();
        ArrayList<String> userTabNames = new ArrayList<String>();
        try {
            basepbReturnObject = super.execSelectSQL(baseFinalQuery);
            userpbReturnObject = super.execSelectSQL(userFinalQuery);
            baseColNames = basepbReturnObject.getColumnNames();
            userColNames = userpbReturnObject.getColumnNames();
            for (int i = 0; i < basepbReturnObject.getRowCount(); i++) {
                baseIds.add(basepbReturnObject.getFieldValueString(i, baseColNames[0]));
                baseNames.add(basepbReturnObject.getFieldValueString(i, baseColNames[1]));
            }
            for (int j = 0; j < userpbReturnObject.getRowCount(); j++) {
                userTabIds.add(userpbReturnObject.getFieldValueString(j, userColNames[0]));
                userTabNames.add(userpbReturnObject.getFieldValueString(j, userColNames[1]));
            }
            hmap.put("baseIds", baseIds);
            hmap.put("baseNames", baseNames);
            hmap.put("userTabIds", userTabIds);
            hmap.put("userTabNames", userTabNames);
            jsonString = gson.toJson(hmap);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return jsonString;
    }

    public void resetGlobalFilter(String portalTabId, String userId) {
        String resetGlobalFilter = getResourceBundle().getString("resetGlobalFilter");
        String finalQuery = "";
        Object[] objects = new Object[7];
        objects[0] = "";
        objects[1] = "";
        objects[2] = "";
        objects[3] = "All";
        objects[4] = "All";
        objects[5] = portalTabId;
        objects[6] = userId;
        finalQuery = super.buildQuery(resetGlobalFilter, objects);
        try {
            super.execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }
}
