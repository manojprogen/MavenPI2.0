package com.progen.targetview.db;

import java.sql.Connection;
import java.util.ArrayList;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import prg.db.PbDb;
import utils.db.ProgenConnection;

public class PbTargetExcelXml extends PbDb {

    public static Logger logger = Logger.getLogger(PbTargetExcelXml.class);
    XMLOutputter serializer = null;
    Document document = null;
    private int targetId = 0;
    public String[] primViewTags = {"primViewVal"};
    public String[] secViewTags = {"secViewVal"};
    private ArrayList primList = new ArrayList();
    private ArrayList secList = new ArrayList();

    public int generateTargetDetId() throws Exception {
        int seq = 0;
        String seQ = "select &.nextval from dual";
        Object relSeqObj[] = new Object[1];
        relSeqObj[0] = "TARGET_EXCEL_DET_SEQ";
        String finalseQ = buildQuery(seQ, relSeqObj);
        seq = getSequenceNumber(finalseQ);

        return seq;
    }

    public void createDocumentForPrim(String targetDetId) throws Exception {
        Element root = new Element("TargetViewBy");
        root.setAttribute("version", "1.00001");
        root.setText("New Root");
        // root = buildPrimViewByDetails(root);
        // document = new Document(root);
        String insertTargetPrimViewXML = "update TARGET_EXCELMASTER set ROWVIEWBYVALUES=? where TARGET_EXCEL_DET=?";
        //serializer = new XMLOutputter();
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
        ////////////////////////////////////////////////////////////////////////.println(" serializer.outputString(document) "+serializer.outputString(document));

        opstmt = (OraclePreparedStatement) connection.prepareStatement(insertTargetPrimViewXML);
        String primV = "";

        for (int m = 0; m < primList.size(); m++) {
            primV = primV + "~" + primList.get(m).toString();
        }

        if (primV.length() > 0) {
            primV = primV.substring(1);
        }

        // opstmt.setStringForClob(1, serializer.outputString(document));
        opstmt.setStringForClob(1, primV);
        opstmt.setString(2, targetDetId);

        int rows = opstmt.executeUpdate();
    }

    public void createDocumentForSec(String targetDetId) throws Exception {
        Element root = new Element("TargetViewBy");
        root.setAttribute("version", "1.00001");
        root.setText("New Root");
        // root = buildSecViewByDetails(root);
        // document = new Document(root);
        String insertTargetSecViewXML = "update TARGET_EXCELMASTER set COLUMNVIEWBYVALUES=? where TARGET_EXCEL_DET=?";
        // serializer = new XMLOutputter();
        OraclePreparedStatement opstmt = null;
        Connection connection = ProgenConnection.getInstance().getConnection();
        // //////////////////////////////////////////////////////////////////////.println(" serializer.outputString(document) "+serializer.outputString(document));

        opstmt = (OraclePreparedStatement) connection.prepareStatement(insertTargetSecViewXML);
        String secV = "";
        for (int m = 0; m < secList.size(); m++) {
            secV = secV + "~" + secList.get(m).toString();
        }
        if (secV.length() > 0) {
            secV = secV.substring(1);
        }
        // opstmt.setStringForClob(1, serializer.outputString(document));
        opstmt.setStringForClob(1, secV);
        opstmt.setString(2, targetDetId);

        int rows = opstmt.executeUpdate();
    }

    public Element buildPrimViewByDetails(Element root) {
        Element ppDetailEle = null;
        Element child = null;
        int count = 1;
        ppDetailEle = new Element("ViewDetail");

        try {
            for (int i = 0; i < primList.size(); i++) {

                String[] Obj = new String[1];
                Obj[0] = primList.get(i).toString();

                for (int j = 0; j < primViewTags.length; j++) {
                    child = new Element(primViewTags[j]);
                    child.setText(Obj[j]);
                    ppDetailEle.addContent(child);

                }
                root.addContent(ppDetailEle);
            }

        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    public Element buildSecViewByDetails(Element root) {

        Element ppDetailEle = null;
        Element child = null;
        int count = 1;
        ppDetailEle = new Element("ViewDetail");

        try {
            for (int i = 0; i < secList.size(); i++) {
                String[] Obj = new String[1];
                Obj[0] = secList.get(i).toString();

                for (int j = 0; j < secViewTags.length; j++) {
                    child = new Element(secViewTags[j]);
                    child.setText(Obj[j]);
                    ppDetailEle.addContent(child);

                }
                root.addContent(ppDetailEle);
            }


        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }
        return root;
    }

    /**
     * @return the targetId
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    /**
     * @return the primList
     */
    public ArrayList getPrimList() {
        return primList;
    }

    /**
     * @param primList the primList to set
     */
    public void setPrimList(ArrayList primList) {
        this.primList = primList;
    }

    /**
     * @return the secList
     */
    public ArrayList getSecList() {
        return secList;
    }

    /**
     * @param secList the secList to set
     */
    public void setSecList(ArrayList secList) {
        this.secList = secList;
    }
}
