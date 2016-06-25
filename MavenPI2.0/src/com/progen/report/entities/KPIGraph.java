/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import com.progen.report.KPIElement;
import java.util.*;

/**
 *
 * @author progen
 */
public class KPIGraph extends Report {

    private double startRange;
    private double endRange;
    private double fsplit;
    private double ssplit;
    private double needle;
    private String kpiGraphType;
    private String graphXML;
    private double graphHeight;
    private double graphWidth;
    HashMap<String, KPIColorRange> kpiGraphColorRangeMap = new HashMap<String, KPIColorRange>();
    private KPIElement kpiElement;
    private String kpigrname;

    public KPIElement getKpiElement() {
        return kpiElement;
    }

    public void setKpiElement(KPIElement kpiElement) {
        this.kpiElement = kpiElement;
    }

    @Override
    public List<KPIElement> getKPIElements() {
        List<KPIElement> elems = new ArrayList<KPIElement>();
        elems.add(kpiElement);
        return elems;
    }

    public String getElementId() {
        return kpiElement.getElementId();
    }

    public double getEndRange() {
        return endRange;
    }

    public void setEndRange(double endRange) {
        this.endRange = endRange;
    }

    public double getFsplit() {
        return fsplit;
    }

    public void setFsplit(double fsplit) {
        this.fsplit = fsplit;
    }

    public String getGraphXML() {
        return graphXML;
    }

    public void setGraphXML(String graphXML) {
        this.graphXML = graphXML;
    }

    public String getKpiGraphType() {
        return kpiGraphType;
    }

    public void setKpiGraphType(String kpiGraphType) {
        this.kpiGraphType = kpiGraphType;
    }

    public String getKpiName() {
        return kpiElement.getElementName();
    }

    public double getNeedle() {
        return needle;
    }

    public void setNeedle(double needle) {
        this.needle = needle;
    }

    public double getSsplit() {
        return ssplit;
    }

    public void setSsplit(double ssplit) {
        this.ssplit = ssplit;
    }

    public double getStartRange() {
        return startRange;
    }

    public void setStartRange(double startRange) {
        this.startRange = startRange;
    }

    public void addkpiGrphColorRange(String risk, KPIColorRange kpiGraphColorRange) {
        kpiGraphColorRangeMap.put(risk, kpiGraphColorRange);
    }

    public HashMap<String, KPIColorRange> getKpiGrphColorRangeHashMap() {
        return kpiGraphColorRangeMap;
    }

    public void initializeRanges() {
        Double startVals[] = new Double[kpiGraphColorRangeMap.size()];
        Double endVals[] = new Double[kpiGraphColorRangeMap.size()];
        String operators[] = new String[kpiGraphColorRangeMap.size()];
        String colors[] = new String[kpiGraphColorRangeMap.size()];

        Set keySet = kpiGraphColorRangeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        int i = 0;
        while (iter.hasNext()) {
            String risk = iter.next();
            KPIColorRange kpiColorObj = kpiGraphColorRangeMap.get(risk);
            String operatorVal = kpiColorObj.getOperator();

            if (operatorVal.equalsIgnoreCase(">") || operatorVal.equalsIgnoreCase(">=")) {
                startVals[i] = kpiColorObj.getRangeStartValue();
                endVals[i] = kpiColorObj.getRangeEndValue();
                operators[i] = kpiColorObj.getOperator();
            } else if (operatorVal.equalsIgnoreCase("<") || operatorVal.equalsIgnoreCase("<=")) {
                startVals[i] = kpiColorObj.getRangeStartValue();
                endVals[i] = kpiColorObj.getRangeEndValue();
                operators[i] = kpiColorObj.getOperator();

            } else {
                startVals[i] = kpiColorObj.getRangeStartValue();
                endVals[i] = kpiColorObj.getRangeEndValue();
                operators[i] = kpiColorObj.getOperator();
            }
            i++;
        }

        for (int j = 0; j < operators.length; j++) {
            if (operators[j].equalsIgnoreCase(">")) {
                endVals[j] = null;
            } else if (operators[j].equalsIgnoreCase("<")) {
                Double temp = startVals[j];
                startVals[j] = null;
                endVals[j] = temp;
            } else if (operators[j].equalsIgnoreCase("between") || operators[j].equalsIgnoreCase("<>")) {
                if (startVals[j].compareTo(endVals[j]) > 0) {
                    Double temp = startVals[j];
                    startVals[j] = endVals[j];
                    endVals[j] = temp;
                }
            }
        }

        for (int j = 0; j < operators.length - 1; j++) {
            if (endVals[j] != startVals[j + 1]) {
                sortValues(operators, startVals, endVals);
                break;
            } else {
                setValuesToKpiGraph(operators, startVals, endVals);
                break;
            }
        }

    }

    private void sortValues(String[] operators, Double[] startVals, Double[] endVals) {

        Double temp;
        String tempOperator;
        for (int i = 0; i < startVals.length; i++) {
            for (int j = 0; j < (startVals.length - i) - 1; j++) {
                if (startVals[j + 1] == null) {
                    if (startVals[j] != null) {
                        temp = startVals[j];
                        startVals[j] = startVals[j + 1];
                        startVals[j + 1] = temp;

                        temp = endVals[j];
                        endVals[j] = endVals[j + 1];
                        endVals[j + 1] = temp;

                        tempOperator = operators[j];
                        operators[j] = operators[j + 1];
                        operators[j + 1] = tempOperator;


                    }
                } else {
                    if (startVals[j] != null) {
                        if (startVals[j].compareTo(startVals[j + 1]) > 0) {
                            temp = startVals[j];
                            startVals[j] = startVals[j + 1];
                            startVals[j + 1] = temp;

                            temp = endVals[j];
                            endVals[j] = endVals[j + 1];
                            endVals[j + 1] = temp;

                            tempOperator = operators[j];
                            operators[j] = operators[j + 1];
                            operators[j + 1] = tempOperator;
                        }
                    }
                }
            }
        }
        setValuesToKpiGraph(operators, startVals, endVals);

    }

    private void setValuesToKpiGraph(String[] operators, Double[] startVals, Double[] endVals) {

        setFsplit(endVals[0]);
        setSsplit(startVals[startVals.length - 1]);
        if (startVals[0] == null) {
            setStartRange(endVals[0] - 20);
        } else {
            setStartRange(startVals[0]);
        }

        if (endVals[endVals.length - 1] == null) {
            setEndRange(startVals[endVals.length - 1] + 20);
        } else {
            setEndRange(endVals[endVals.length - 1]);
        }
    }

    public double getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(double graphHeight) {
        this.graphHeight = graphHeight;
    }

    public double getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(double graphWidth) {
        this.graphWidth = graphWidth;
    }

    public String getKpigrname() {
        return kpigrname;
    }

    public void setKpigrname(String kpigrname) {
        this.kpigrname = kpigrname;
    }
}