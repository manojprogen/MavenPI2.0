/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.business.group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MeasurePropertyBuilder {

    public static Logger logger = Logger.getLogger(MeasurePropertyBuilder.class);

    public MeasurePropertySet buildMeasureProperties(InputStream fileInputStream) {
        MeasurePropertySet measProperty = null;
        NodeList nodeLst = null;
        Document doc = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

            doc = db.parse(fileInputStream);
            nodeLst = doc.getElementsByTagName("measureCategories");
            HashSet<String> measureCategory = new HashSet<String>();
            measProperty = new MeasurePropertySet();
            for (int s = 0; s < nodeLst.getLength(); s++) {
                Node node = nodeLst.item(s);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element categoryElement = (Element) node;
                    NodeList categoryElmntLst = categoryElement.getElementsByTagName("category");
                    for (int i = 0; i < categoryElmntLst.getLength(); i++) {
                        Element fstNmElmnt = (Element) categoryElmntLst.item(i);
                        NodeList fstNm = fstNmElmnt.getChildNodes();
//                        
                        measureCategory.add(((Node) fstNm.item(0)).getNodeValue());
                    }

                }
            }
            measProperty.setMeasureCategory(measureCategory);
            nodeLst = doc.getElementsByTagName("measureTypes");
            for (int i = 0; i < nodeLst.getLength(); i++) {
                Node node = nodeLst.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element measTypeElement = (Element) node;
                    NodeList measuNodeList = measTypeElement.getElementsByTagName("measureType");
                    for (int j = 0; j < measuNodeList.getLength(); j++) {
                        MeasureType measureType = new MeasureType();
                        Node measureTypeNode = measuNodeList.item(j);
                        if (measureTypeNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element typeElement = (Element) measureTypeNode;
                            NodeList typeNodeList = typeElement.getElementsByTagName("type");
                            Element tElmnt = (Element) typeNodeList.item(0);
                            NodeList typeNm = tElmnt.getChildNodes();
                            measureType.setMeasureType(((Node) typeNm.item(0)).getNodeValue());
                            NodeList unitsNodeList = typeElement.getElementsByTagName("Units");
                            if (unitsNodeList.getLength() > 0) {
                                Node unitsNode = unitsNodeList.item(0);
                                if (unitsNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element unitElement = (Element) unitsNode;
                                    NodeList unitNodeList = unitElement.getElementsByTagName("unit");
                                    HashSet<String> measureUnits = new HashSet<String>();
                                    for (int u = 0; u < unitNodeList.getLength(); u++) {
                                        Element unitNameElement = (Element) unitNodeList.item(u);
                                        NodeList unitName = unitNameElement.getChildNodes();
                                        measureUnits.add(((Node) unitName.item(0)).getNodeValue());
                                    }
                                    measureType.setMeasureUnits(measureUnits);

                                }

                            }


                        }
                        measProperty.addMeasureType(measureType);

                    }

                }
//                

            }


        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return measProperty;
    }

    public static void main(String[] a) {
        String filePath = "/home/progen/work/central/pi/src/java/prg/business/group/measureProperties.xml";
        //InputStream servletStream = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/MetadataConn.xml");

        File file = new File(filePath);
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            MeasurePropertyBuilder propertyBuilder = new MeasurePropertyBuilder();
            propertyBuilder.buildMeasureProperties(fileInputStream);
        } catch (FileNotFoundException ex) {
            logger.error("Exception: ", ex);
        }

    }
}
