/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.progen.report.PbReportCollectionResBunSqlServer;
import com.progen.report.PbReportCollectionResourceBundle;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class WhatIfScenarioBuilder {

    public static Logger logger = Logger.getLogger(WhatIfScenarioBuilder.class);
    ResourceBundle resBndle = null;
    public Document document = null;

    private ResourceBundle getResourceBundle() {
        if (resBndle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBndle = new PbReportCollectionResBunSqlServer();
            } else {
                resBndle = new PbReportCollectionResourceBundle();
            }
        }
        return resBndle;
    }

    public WhatIfScenario buildWhatIfScenario(String reportId) {
        String selectWhatifQuery = getResourceBundle().getString("selectWhatifQuery");
        PbReturnObject pbReturnObject = new PbReturnObject();
        String finalQuery = "";
        Object object[] = new Object[1];
        object[0] = reportId;
        SAXBuilder builder = new SAXBuilder();
        Document whatIfDocument;
        Element root = null;
        WhatIfScenario wfScenario = null;
        ArrayList<String> whatIfMesArrayList = new ArrayList<String>();
        ArrayList<Double> sliderValuesArrayList = new ArrayList<Double>();
        ArrayList<String> targetEmtIdList = new ArrayList<String>();
        ArrayList<String> targetMeasureNameList = new ArrayList<String>();
        ArrayList<String> targetFormulaList = new ArrayList<String>();
        ArrayList<String> whatIfDepenMeasure = new ArrayList<String>();
        HashMap<String, String> stdNonStdDetails = new HashMap<String, String>();
        String whatIfReltId;
        StringBuilder whatIfXML = null;
        String dimensionId = null;
        HashMap<String, Double> sensitivityMap = new HashMap<String, Double>();
        PbDb pbdb = new PbDb();

        try {
            finalQuery = pbdb.buildQuery(selectWhatifQuery, object);
            //
            pbReturnObject = pbdb.execSelectSQL(finalQuery);
            if (pbReturnObject.getRowCount() != 0) {
                document = builder.build(new ByteArrayInputStream(pbReturnObject.getFieldValueClobString(0, "WHATIF_DETAILS").getBytes()));
                whatIfXML = new StringBuilder(pbReturnObject.getFieldValueClobString(0, "WHATIF_DETAILS"));

                whatIfDocument = builder.build(new ByteArrayInputStream(whatIfXML.toString().getBytes()));
                wfScenario = new WhatIfScenario(reportId);
                //copy the code for getting ArrayList of WhatIfMeasures and Slider Values
                root = whatIfDocument.getRootElement();
                List row = root.getChildren("WhatIfMeasures");
                for (int i = 0; i < row.size(); i++) {
                    Element WhatIfScenario = (Element) row.get(i);
                    List measuresList = WhatIfScenario.getChildren("measure");
                    for (int j = 0; j < measuresList.size(); j++) {
                        Element WhatIfMeasures = (Element) measuresList.get(j);
                        List measures = WhatIfMeasures.getChildren("elementID");
                        List stdNonStd = null;
                        if (WhatIfMeasures.getChildren("stdNonStd") != null) {
                            stdNonStd = WhatIfMeasures.getChildren("stdNonStd");
                        }
                        List sliderValues = WhatIfMeasures.getChildren("sliderValue");
                        Element measuresValue = (Element) measures.get(0);
                        Element stdNonStdElement = null;
                        if (stdNonStd != null && !stdNonStd.isEmpty()) {
                            stdNonStdElement = (Element) stdNonStd.get(0);
                        }
                        Element sliderElement = (Element) sliderValues.get(0);

                        whatIfMesArrayList.add(measuresValue.getText());
                        if (stdNonStdElement != null) {
                            stdNonStdDetails.put(measuresValue.getText(), stdNonStdElement.getText());
                        } else {
                            stdNonStdDetails.put(measuresValue.getText(), "true");
                        }
                        sliderValuesArrayList.add(Double.parseDouble(sliderElement.getText()));
                        // 
                        // 

                    }
                }

                wfScenario.addWhatIfMeasures(whatIfMesArrayList);
                //loop through second arraylist of slider values
                for (int i = 0; i < sliderValuesArrayList.size(); i++) {
                    wfScenario.updateSlider(whatIfMesArrayList.get(i), sliderValuesArrayList.get(i));
                }
                List targetRow = root.getChildren("WhatIfTarget");
                for (int tj = 0; tj < targetRow.size(); tj++) {
                    Element whatifTergetElement = (Element) targetRow.get(tj);
                    List trgMeasuresList = whatifTergetElement.getChildren("measure");
                    for (int ti = 0; ti < trgMeasuresList.size(); ti++) {
                        Element element = (Element) trgMeasuresList.get(ti);
                        List elmtIdsList = element.getChildren("elementID");
                        Element elementId = (Element) elmtIdsList.get(0);
                        targetEmtIdList.add(elementId.getText());

                        List measureNameList = element.getChildren("measureName");
                        Element measureName = (Element) measureNameList.get(0);
                        targetMeasureNameList.add(measureName.getText());

                        List formulaList = element.getChildren("formula");
                        for (int fi = 0; fi < formulaList.size(); fi++) {
                            Element formlElement = (Element) formulaList.get(fi);
                            List evaluationList = formlElement.getChildren("evaluation");
                            Element evaluation = (Element) evaluationList.get(0);
                            targetFormulaList.add(evaluation.getText());
//                            
                        }

                    }
                }
                List dependatMeasRow = root.getChildren("WhatIfDependant");
                for (int dR = 0; dR < dependatMeasRow.size(); dR++) {
                    Element depeMeasElement = (Element) dependatMeasRow.get(dR);
                    List depenMeasureList = depeMeasElement.getChildren("measure");
                    for (int di = 0; di < depenMeasureList.size(); di++) {
                        Element element = (Element) depenMeasureList.get(di);
                        List elmtIdsList = element.getChildren("dependantMeasure");
                        List stsnonStd = element.getChildren("stdNonStd");
                        Element elementId = (Element) elmtIdsList.get(0);
                        Element stdNonStdElement = null;
                        if (stsnonStd != null && !stsnonStd.isEmpty()) {
                            stdNonStdElement = (Element) stsnonStd.get(0);
                        }
                        whatIfDepenMeasure.add(elementId.getText());
                        if (stdNonStdElement != null) {
                            stdNonStdDetails.put(elementId.getText(), stdNonStdElement.getText());
                        } else {
                            stdNonStdDetails.put(elementId.getText(), "true");
                        }
                    }

                }

                wfScenario.addDependantMeasures(whatIfDepenMeasure);
                wfScenario.setStdnonstddetailsMap(stdNonStdDetails);
                List whatIfSensitiList = root.getChildren("WhatIfSensitivity");
                for (int si = 0; si < whatIfSensitiList.size(); si++) {
                    Element sensiElement = (Element) whatIfSensitiList.get(si);
                    List sensiMeasList = sensiElement.getChildren("measure");
                    for (int sj = 0; sj < sensiMeasList.size(); sj++) {
                        Element siElement = (Element) sensiMeasList.get(sj);
                        List dimeList = siElement.getChildren("dimensionId");
                        Element dimenElement = (Element) dimeList.get(0);
                        dimensionId = dimenElement.getText();
                        List measAndva = siElement.getChildren("measuresAndvalues");
                        for (int sk = 0; sk < measAndva.size(); sk++) {
                            Element mAndVaElement = (Element) measAndva.get(sk);
                            List keyList = mAndVaElement.getChildren("key");
                            List valueList = mAndVaElement.getChildren("value");
                            Element keyElement = (Element) keyList.get(0);
                            Element valueElement = (Element) valueList.get(0);
                            String tempKey = keyElement.getText();
                            double tempvalue = Double.parseDouble(valueElement.getText());
                            sensitivityMap.put(tempKey, tempvalue);

                        }


                    }

                }

                //start parsing of WhatIfTargetMeasure
                //loop and start adding target
                if (dimensionId != null && !sensitivityMap.isEmpty()) {

                    for (String dimValue : sensitivityMap.keySet()) {
                        wfScenario.updateSensitivity(dimensionId, dimValue, sensitivityMap.get(dimValue));
                    }
                }
                for (int j = 0; j < targetMeasureNameList.size(); j++) {
                    wfScenario.addWhatIfTarget(targetEmtIdList.get(j), targetMeasureNameList.get(j), targetFormulaList.get(j));
                }

            }

        } catch (JDOMException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return wfScenario;
    }
}
