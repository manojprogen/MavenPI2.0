/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.whatIf;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.data.DataFacade;
import com.progen.report.formula.Formula;
import com.progen.report.formula.FormulaEvaluator;
import com.progen.report.formula.MeasureDataSet;
import com.progen.reportview.bd.PbReportViewerBD;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;

/**
 *
 * @author sreekanth
 */
public class WhatIfScenarioBD {

    public static Logger logger = Logger.getLogger(WhatIfScenarioBD.class);
    private HashMap<String, String> stdNonStdDetails = new HashMap<String, String>();

    private WhatIfScenario createWhatIfScenario(String reportId, ArrayList<String> whatIfMeasureLst, ArrayList<String> dependantMeasureList) {
        WhatIfScenario wfScenario = new WhatIfScenario(reportId);
        wfScenario.setStdnonstddetailsMap(stdNonStdDetails);
        wfScenario.addWhatIfMeasures(whatIfMeasureLst);
        wfScenario.addDependantMeasures(dependantMeasureList);


        return wfScenario;
    }

    public boolean initializeWhatIf(Container container, ArrayList<String> whatIfMeasureLst, ArrayList<String> dependantMeasureList) {
        WhatIfScenario wfScenario = container.getWhatIfScenario();
        PbReportViewerBD viewerBd = new PbReportViewerBD();

        boolean needToUpdateContainer = false;
        boolean checkDependantMeasures = false;

        if (wfScenario != null) {
            wfScenario.setStdnonstddetailsMap(stdNonStdDetails);
            if (whatIfMeasureLst.isEmpty()) {
                container.setWhatIfScenario(null);
                needToUpdateContainer = true;
            } else {
                needToUpdateContainer = wfScenario.updateMeasures(whatIfMeasureLst);
            }
            checkDependantMeasures = wfScenario.updateDependantMeasure(dependantMeasureList);
            if (checkDependantMeasures == true && needToUpdateContainer == false) {
                needToUpdateContainer = true;
            }
            if (needToUpdateContainer) {
                viewerBd.removeRunTimeColumn(container, RTMeasureElement.WHATIF);
                viewerBd.removeRunTimeColumn(container, RTMeasureElement.WHATIFTARGET);
            }

        } else {
            if (!whatIfMeasureLst.isEmpty()) {
                wfScenario = this.createWhatIfScenario(container.getReportId(), whatIfMeasureLst, dependantMeasureList);
                wfScenario.setStdnonstddetailsMap(stdNonStdDetails);
                needToUpdateContainer = true;
                container.setWhatIfScenario(wfScenario);
            }
        }

        if (needToUpdateContainer) {
            for (String measEleId : whatIfMeasureLst) {
                viewerBd.addRunTimeColumn(container, RTMeasureElement.WHATIF_COLUMN, measEleId);
                viewerBd.initializeFontColorForMeasures(container);
            }
            if (dependantMeasureList.size() > 0) {
                for (String dependMeasEleId : dependantMeasureList) {
                    viewerBd.addRunTimeColumn(container, RTMeasureElement.WHATIF_COLUMN, dependMeasEleId);
                    viewerBd.initializeFontColorForMeasures(container);
                }
            }
        }
        return needToUpdateContainer;
    }

    // TODO: while updating do not reset old data
    private void updateWhatIfDataInContainer(Container container) {
        ProgenDataSet qryObj = container.getRetObj();
        ArrayList<BigDecimal> measData;
        ArrayList<BigDecimal> wfData;
        WhatIfScenario wfScenario = container.getWhatIfScenario();

        ArrayList<String> whatIfMeasureLst = wfScenario.getWhatIfMeasureList();
        ArrayList<String> whatIfdependantMesList = wfScenario.getDependantMeasureList();
        for (String measure : whatIfMeasureLst) {
//            
            measData = qryObj.retrieveNumericData(measure);
            wfData = this.performWhatIfOnMeasure(wfScenario, measure, container);
            qryObj.setRuntimeMeasure(measure + RTMeasureElement.WHATIF.getColumnType(), wfData);
        }
        for (String depenMeas : whatIfdependantMesList) {
            measData = qryObj.retrieveNumericData(depenMeas);
            wfData = this.performWhatIfOnDependantMeasure(wfScenario, depenMeas, container);
            qryObj.setRuntimeMeasure(depenMeas + RTMeasureElement.WHATIF.getColumnType(), wfData);
        }

    }

    private ArrayList<BigDecimal> performWhatIfOnMeasure(WhatIfScenario scenario, String measure, Container container) {
        DataFacade facade = new DataFacade(container);
        WhatIfMeasure whatIfMeasure = scenario.findWhatIfMeasure(measure);
        int rowCount = facade.getRowCount();
        ArrayList<BigDecimal> whatIfData = new ArrayList<BigDecimal>();
        BigDecimal wfValue;
        BigDecimal measValue;
//        
        String rowName = "";
        BigDecimal sensitivityBigDecimal;
        for (int i = 0; i < rowCount; i++) {
            BigDecimal sliderValue = new BigDecimal(whatIfMeasure.getSliderValue() / 100.);
            measValue = facade.getMeasureDataForComputation(i, measure);
            if (scenario.getWhatIfSensitivity() != null && scenario.getWhatIfSensitivity().initializeWhatIfSensitivity()) {
//                
                rowName = facade.getDimensionData(i, scenario.getWhatIfSensitivity().getDimensionId());
                if (scenario.getWhatIfSensitivity().isApplicable(rowName)) {

                    sensitivityBigDecimal = new BigDecimal(scenario.getWhatIfSensitivity().getSensitivity(rowName));
                    // measValue = measValue.add(measValue.multiply(sensitivityBigDecimal));
                    sliderValue = sliderValue.multiply(sensitivityBigDecimal);
                }
            }
            if (whatIfMeasure.isIsStandard()) {
                wfValue = measValue.add(measValue.multiply(sliderValue));
            } else {
                wfValue = measValue.subtract(measValue.multiply(sliderValue));
            }
            whatIfData.add(wfValue);

        }

        return whatIfData;

    }

    private ArrayList<BigDecimal> performWhatIfOnDependantMeasure(WhatIfScenario scenario, String measure, Container container) {
        DataFacade facade = new DataFacade(container);
        WhatIfDependantMeasure dependantMeasure = scenario.findDependantMeasure(measure);

        int rowCount = facade.getRowCount();
        ArrayList<BigDecimal> whatIfDepenData = new ArrayList<BigDecimal>();

        BigDecimal depMeasWhatIfValue = null;
        BigDecimal depMeasValue;
        BigDecimal rltdMeasValue;
        BigDecimal rltdMeasWhatIfValue;


        for (int i = 0; i < rowCount; i++) {
            depMeasValue = facade.getMeasureDataForComputation(i, measure);
            rltdMeasValue = facade.getMeasureDataForComputation(i, dependantMeasure.getRelatedMeasure());
            rltdMeasWhatIfValue = facade.getMeasureDataForComputation(i, dependantMeasure.getRelatedMeasure() + RTMeasureElement.WHATIF.getColumnType());
            if (rltdMeasValue.compareTo(BigDecimal.ZERO) == 0) {
                rltdMeasValue = BigDecimal.ONE;
            }
            if (rltdMeasWhatIfValue.compareTo(BigDecimal.ZERO) == 0) {
                rltdMeasWhatIfValue = BigDecimal.ONE;
            }
            if (dependantMeasure.isIsStandard() && Boolean.parseBoolean(scenario.getStdnonstddetailsMap().get(dependantMeasure.getRelatedMeasure()))) {
                depMeasWhatIfValue = depMeasValue.multiply(rltdMeasWhatIfValue);
                depMeasWhatIfValue = depMeasWhatIfValue.divide(rltdMeasValue);
            } else if (Boolean.parseBoolean(scenario.getStdnonstddetailsMap().get(dependantMeasure.getRelatedMeasure())) && !dependantMeasure.isIsStandard()) {
                // depMeasWhatIfValue = depMeasValue.divide(rltdMeasWhatIfValue.round(mc));
                depMeasWhatIfValue = depMeasValue.divide(rltdMeasWhatIfValue, 18, RoundingMode.HALF_UP);
                depMeasWhatIfValue = depMeasWhatIfValue.multiply(rltdMeasValue);
            } else if (!Boolean.parseBoolean(scenario.getStdnonstddetailsMap().get(dependantMeasure.getRelatedMeasure())) && dependantMeasure.isIsStandard()) {
                depMeasWhatIfValue = depMeasValue.divide(rltdMeasWhatIfValue, 18, RoundingMode.HALF_UP);
                depMeasWhatIfValue = depMeasWhatIfValue.multiply(rltdMeasValue);
            } else if (!Boolean.parseBoolean(scenario.getStdnonstddetailsMap().get(dependantMeasure.getRelatedMeasure())) && !dependantMeasure.isIsStandard()) {
                depMeasWhatIfValue = depMeasValue.divide(rltdMeasValue, 18, RoundingMode.HALF_UP);
                depMeasWhatIfValue = depMeasWhatIfValue.multiply(rltdMeasWhatIfValue);
            } else {
                depMeasWhatIfValue = depMeasValue.multiply(rltdMeasWhatIfValue);
                depMeasWhatIfValue = depMeasWhatIfValue.divide(rltdMeasValue);
            }
            whatIfDepenData.add(depMeasWhatIfValue);
        }
        return whatIfDepenData;
    }

    public void updateSlidersAndPerformWhatIf(Container container, ArrayList<String> measLst, ArrayList<Double> sliderValues) {
        WhatIfScenario wfScenario = container.getWhatIfScenario();
        int index = 0;
        for (String measure : measLst) {

            wfScenario.updateSlider(measure, sliderValues.get(index));
            index++;
        }
        this.performWhatIfInContainer(container);
    }

    public void performWhatIfInContainer(Container container) {
        WhatIfScenario wfScenario = container.getWhatIfScenario();
        this.updateWhatIfDataInContainer(container);
        this.computeWhatIfTargets(container);
    }

    public void addTargetMeasure(Container container, String whatIfTargetMeasuresname, String whatIftrgtFormula) {
        WhatIfScenario whatIfScenario = null;
        if (container.getWhatIfScenario() != null) {
            whatIfScenario = container.getWhatIfScenario();
            whatIfScenario.addWhatIfTarget(whatIfTargetMeasuresname, whatIftrgtFormula);

        }
        if (container != null) {
            computeWhatIfTargets(container);
        }
    }

    private void computeWhatIfTargets(Container container) {
        WhatIfScenario whatIfScenario = container.getWhatIfScenario();
        ArrayList<WhatIfTarget> whatIfTargetLst = whatIfScenario.getWhatIfTargets();
        ProgenDataSet qryObj = container.getRetObj();

        HashSet<MeasureDataSet> measDataLst;

        ArrayList<BigDecimal> measData;
        ArrayList<BigDecimal> resultDataLst;



        Formula targetFormula;
        if (!whatIfTargetLst.isEmpty()) {
            for (WhatIfTarget target : whatIfTargetLst) {
                targetFormula = target.getTargetFormula();
                Set<String> exprSet = targetFormula.getFormulaMeasureSet();
                measDataLst = new HashSet<MeasureDataSet>();
                ArrayList<String> columnList = (ArrayList) ((ArrayList) container.getTableHashMap().get("Measures"));
                for (String expression : exprSet) {
                    if (columnList.contains(expression)) {
                        if (RTMeasureElement.isRunTimeMeasure(expression)) {
                            measData = qryObj.getRunTimeMeasureData(expression);
                        } else {
                            measData = qryObj.retrieveNumericData(expression);
                        }
                        MeasureDataSet measDataSet = new MeasureDataSet(expression, measData);
                        measDataLst.add(measDataSet);
                    }
                }
                FormulaEvaluator formulaEvaluator = new FormulaEvaluator();
                if (!measDataLst.isEmpty()) {
                    resultDataLst = formulaEvaluator.evaluateFormulaForDataSet(targetFormula, measDataLst, qryObj.getRowCount());
                    //qryObj.getRowCount();
                    //after evaluation you will get one ArrayList<BigDecimal>
                    //set the arraylist to returnobject
                    qryObj.setRuntimeMeasure(target.getTargetMeasureId() + RTMeasureElement.WHATIFTARGET.getColumnType(), resultDataLst);
                    PbReportViewerBD pbReportViewerBD = new PbReportViewerBD();
                    pbReportViewerBD.addRunTimeColumn(container, RTMeasureElement.WHATIF_TARGET_COLUMN, target.getTargetMeasureId(), target.getTargetMeasureName());
                }
            }
        }
    }

    public void updateSensitivity(Container container, HashMap<String, Double> senFactorHashMap) {
        String dimId = "";
        WhatIfScenario whatIfScenario = null;
        dimId = container.getOriginalColumns().get(0);
        whatIfScenario = container.getWhatIfScenario();
        for (String dimValue : senFactorHashMap.keySet()) {
            whatIfScenario.updateSensitivity(dimId, dimValue, senFactorHashMap.get(dimValue));
        }

    }

    public String editWhatIfTargetMeasure(Container container) {

        WhatIfScenario whatIfScenario = null;
        ArrayList<WhatIfTarget> ifTarget = null;
        Formula formula = null;
        StringBuilder trgtIds = new StringBuilder();
        StringBuilder trgtNames = new StringBuilder();
        StringBuilder datajson = new StringBuilder();
        StringBuilder tempformulaSb = new StringBuilder();
        trgtIds.append("[");
        trgtNames.append("[");
        tempformulaSb.append("[");

        try {
            whatIfScenario = container.getWhatIfScenario();
            //
            ifTarget = whatIfScenario.getWhatIfTargets();
            int i = 0;
            for (WhatIfTarget ifTrgt : ifTarget) {
                formula = ifTrgt.getTargetFormula();
                trgtIds.append("\"").append(ifTrgt.getTargetMeasureId()).append("\"");
                trgtNames.append("\"").append(ifTrgt.getTargetMeasureName()).append("\"");
                tempformulaSb.append("\"").append(formula.getFormulaExpr()).append("\"");

                if (i < ifTarget.size() - 1) {
                    trgtIds.append(",");
                    trgtNames.append(",");
                    tempformulaSb.append(",");
                }
                i++;

            }
            trgtIds.append("]");
            trgtNames.append("]");
            tempformulaSb.append("]");
            ArrayList<String> measureids = container.getTableDisplayMeasures();
            ArrayList<String> measuresNames = container.getReportMeasureNames();
            String formulaStr = tempformulaSb.toString();

            for (String string : measureids) {
                formulaStr = formulaStr.replace(string, measuresNames.get(measureids.indexOf(string)));
            }
            datajson.append("{");
            datajson.append("\"trgtIDs\":").append(trgtIds).append(",").append("\"trgtNames\":").
                    append(trgtNames).append(",").append("\"trgtFormulas\":").append(formulaStr).append(",").append("\"actualFormula\":").append(tempformulaSb);
            datajson.append("}");
            // 
        } catch (NullPointerException e) {
            logger.error("Exception:", e);
        }



        return datajson.toString();
    }

    public Iterable<String> removeWhatIfTargetMeasure(Container container, String trgtMeasId) {
        WhatIfScenario whatIfScenario = container.getWhatIfScenario();
        Iterable<String> removedMeas = whatIfScenario.removeTargetAndDependentTargetMeas(trgtMeasId);
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        for (String measId : removedMeas) {
            measId += RTMeasureElement.WHATIFTARGET.getColumnType();
            // 
            viewerBd.removeMeasureColumn(container, measId);
        }
        return whatIfScenario.getWhatIfTargetMeasureList();

    }

    public void renameWhatIfTargetMeasure(String measId, String measLabel, Container container) {
        WhatIfScenario whatIfScenario = container.getWhatIfScenario();
        whatIfScenario.renameTargetMeasure(measId, measLabel);
        measId += RTMeasureElement.WHATIFTARGET.getColumnType();
        container.changeLabelForMeasure(measId, measLabel);
    }

    public String getWhatIfdetails(Container container) {

        StringBuilder whatifBuilder = new StringBuilder();
        whatifBuilder.append("");
        StringBuilder whatIfElementIDs = new StringBuilder();
        StringBuilder measuresNames = new StringBuilder();
        whatIfElementIDs.append("[");
        measuresNames.append("[");
        ArrayList<String> measureList = container.getTableDisplayMeasures();
        ArrayList<String> measureNames = container.getReportMeasureNames();

        whatifBuilder.append("{");
        for (int i = 0; i < measureList.size(); i++) {
            whatIfElementIDs.append("\"").append(measureList.get(i)).append("\"");
            measuresNames.append("\"").append(measureNames.get(i)).append("\"");
            if (i < measureList.size() - 1) {
                whatIfElementIDs.append(",");
                measuresNames.append(",");
            }

        }
        whatIfElementIDs.append("]");
        measuresNames.append("]");
        whatifBuilder.append("\"whatIfElementIDs\":").append(whatIfElementIDs).append(",").append("\"measuresNames\":").append(measuresNames);
        whatifBuilder.append("}");


        return whatifBuilder.toString();
    }

    public String editSenSitivity(Container container) {
        WhatIfScenario whatIfScenario = null;
        WhatIfSensitivity sensitivity = null;
        ArrayList<SensitivityFactor> sensitivityFactors = null;
        StringBuilder stringBuilder = new StringBuilder();
        String dimensionId = "";

        stringBuilder.append("<option value=\"none\">---select---</option>");
        whatIfScenario = container.getWhatIfScenario();
        if (whatIfScenario.getWhatIfSensitivity() != null) {
            sensitivity = whatIfScenario.getWhatIfSensitivity();
        }
        dimensionId = sensitivity.getDimensionId();

        ArrayListMultimap<Double, String> sensitivityKeys = sensitivity.getDistinctSensitivityKeys();
        for (Double sensitivityFactor : sensitivityKeys.keySet()) {

            List<String> sensiList = sensitivityKeys.get(sensitivityFactor);
            stringBuilder.append("<option value=\"").append(dimensionId).append("-").append(sensitivityFactor).append("-");
            if (sensiList.size() > 1) {
                stringBuilder.append(Joiner.on(",").join(sensiList)).append("\">");
                for (int i = 1; i < sensiList.size(); i++) {
                    sensiList.remove(i);
                }
                stringBuilder.append(Joiner.on(",").join(sensiList)).append("...</option>");
            } else {
                stringBuilder.append(Joiner.on(",").join(sensiList)).append("\">").append(Joiner.on(",").join(sensiList)).append("</option>");
            }

        }
        return stringBuilder.toString();
    }

    public HashMap<String, String> getStdNonStdDetails() {
        return stdNonStdDetails;
    }

    public void setStdNonStdDetails(HashMap<String, String> stdNonStdDetails) {
        this.stdNonStdDetails = stdNonStdDetails;
    }
}
