/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.entities;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.progen.report.KPIElement;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author progen
 */
public class KPI extends Report {

    List<String> elementIds;
    ArrayListMultimap<String, KPIElement> kpiElementsMap = ArrayListMultimap.create();
    ArrayListMultimap<String, KPIColorRange> kpiColorRange = ArrayListMultimap.create();
    ArrayListMultimap<String, KPIComment> kpiComments = ArrayListMultimap.create();
    ArrayListMultimap<String, KPITarget> kpiTargets = ArrayListMultimap.create();
    Map<String, String> kpiDrill = new HashMap<String, String>();
    HashMap<String, String> kpiTargetMapelements = new HashMap<String, String>();
    List<TargetData> targetData = new ArrayList<TargetData>();
    boolean showInsights = true;
    boolean showComments = true;
    boolean showGraphs = true;
    boolean mtdChecked = true;
    boolean qtdChecked = true;
    boolean ytdChecked = true;
    boolean currentChecked = true;
    private String drillViewBys = "";
    private HashMap<Integer, String> KPISequenceHashMap = new HashMap<Integer, String>();
    private HashMap<String, String> KpiStTypeHashMap = new HashMap<String, String>();
    Map<String, String> elementNames = new HashMap<String, String>();
    List<String> targetelementIds;
    ArrayListMultimap<String, KPIElement> targetkpiElementsMap = ArrayListMultimap.create();
    Map<String, String> kpiDrillRepType = new HashMap<String, String>();

    public void addKPIDrill(String elementId, String reportId) {
        kpiDrill.put(elementId, reportId);
    }

    public String getKPIDrill(String elementId) {
        return kpiDrill.get(elementId);
    }

    public ArrayListMultimap<String, KPIComment> getKPIComments() {
        return kpiComments;
    }

    public List<KPIComment> getKPIComments(String elementId) {
        return kpiComments.get(elementId);
    }

    public void addKPIComments(String elementId, KPIComment kpiComment) {
        kpiComments.put(elementId, kpiComment);
    }

    public void clearKPIComments(String elementId) {
        kpiComments.removeAll(elementId);
    }

    public ArrayListMultimap<String, KPITarget> getKPITargets() {
        return kpiTargets;
    }

    public List<KPITarget> getKPITargets(String elementId) {
        return kpiTargets.get(elementId);
    }

    public void addKPITarget(String elementId, KPITarget kpiTarget) {
        kpiTargets.put(elementId, kpiTarget);
    }

    public void clearKPITarget(String elementId) {
        kpiTargets.removeAll(elementId);
    }

    @Override
    public List<KPIElement> getKPIElements() {
        List<KPIElement> kpiElems = new ArrayList<KPIElement>();
        Collection<String> elmentsIds = KPISequenceHashMap.values();
        ArrayList<String> elmentsId = new ArrayList(elmentsIds);
        for (String string : elementIds) {
            if (!elmentsId.contains(string)) {
                elmentsId.add(string);
            }
        }
        for (String elemId : elmentsId) {
            List<KPIElement> elementList = kpiElementsMap.get(elemId);
            kpiElems.addAll(elementList);
        }
        return kpiElems;
    }

    public List<KPIElement> getKPIElements(String elementId) {
        return this.kpiElementsMap.get(elementId);
    }

    public ArrayListMultimap<String, KPIElement> getKPIElementsMap() {
        return this.kpiElementsMap;
    }

    public void addKPIElement(String refElementId, KPIElement kpiElement) {
        this.kpiElementsMap.put(refElementId, kpiElement);
    }

    public void addKPIColorRange(String elementId, KPIColorRange colorRange) {
        List<KPIColorRange> kpiColors = this.kpiColorRange.get(elementId);

        if (kpiColors != null) {
            for (int i = 0; i < kpiColors.size(); i++) {
                KPIColorRange range = kpiColors.get(i);
                if (range.equals(colorRange)) {
                    kpiColorRange.remove(elementId, range);
                    break;
                }
            }
        }

        this.kpiColorRange.put(elementId, colorRange);
    }

    public ArrayListMultimap<String, KPIColorRange> getKPIColorRanges() {
        return this.kpiColorRange;
    }

    public List<KPIColorRange> getKPIColorRanges(String elementId) {
        return this.kpiColorRange.get(elementId);
    }

    public String getKPIColor(String elementId, double value) {
        String color = "";
        List<KPIColorRange> kpiColors = kpiColorRange.get(elementId);
        if (kpiColorRange.containsKey(elementId)) {
            Iterable<KPIColorRange> filter = Iterables.filter(kpiColors, getPredicate(value));
            Iterator<KPIColorRange> iter = filter.iterator();

            if (iter.hasNext()) {
                color = iter.next().getColor();
            }
        } else {
            if (value > 0) {
                color = "Green";
            } else if (value == 0) {
                color = "Grey";
            } else {
                color = "Red";
            }
        }
        return color;
    }

    private Predicate<KPIColorRange> getPredicate(final double value) {
        Predicate<KPIColorRange> predicate = new Predicate<KPIColorRange>() {

            public boolean apply(KPIColorRange rule) {
//                
                String operator = rule.getOperator();
                if (">".equals(operator)) {
                    if (value > rule.getRangeStartValue()) {
                        return true;
                    }
                } else if (">=".equals(operator)) {
                    if (value >= rule.getRangeStartValue()) {
                        return true;
                    }
                } else if ("<".equals(operator)) {
                    if (value < rule.getRangeStartValue()) {
                        return true;
                    }
                } else if ("<=".equals(operator)) {
                    if (value <= rule.getRangeStartValue()) {
                        return true;
                    }
                } else if ("=".equals(operator)) {
                    if (value == rule.getRangeStartValue()) {
                        return true;
                    }
                } else if ("between".equalsIgnoreCase(operator)) {
                    if (value >= rule.getRangeStartValue() && value <= rule.getRangeEndValue()) {
                        return true;
                    }
                }
                return false;
            }
        };
        return predicate;
    }

    public Double getTargetValue(String elementId, String timeLevel) {
        Double target = null;
        List<KPITarget> kpiTargets = getKPITargets(elementId);
        for (KPITarget kpiTarget : kpiTargets) {
            if (kpiTarget.getTimeLevel().equalsIgnoreCase(timeLevel)) {
                target = kpiTarget.getTargetValue();
                break;
            }
        }
        return target;
    }

    public List<String> getElementIds() {
        if (KPISequenceHashMap.isEmpty()) {
            return elementIds;
        } else {
            List<String> tempList = new ArrayList<String>();
            Set<Integer> seqSet = new TreeSet<Integer>(KPISequenceHashMap.keySet());
            for (Integer keyInteger : seqSet) {
                tempList.add(KPISequenceHashMap.get(keyInteger));
            }
            for (String str : elementIds) {
                if (!tempList.contains(str)) {
                    tempList.add(str);
                }
            }
            return tempList;
        }

    }

    public void setElementIds(List<String> elementIds) {
        this.elementIds = elementIds;
    }

    public void setTargetData(List<TargetData> targetData) {
        this.targetData = targetData;
    }

    public void addTargetData(TargetData tgtData) {
        this.targetData.add(tgtData);
    }

    public BigDecimal getTargetData(String measureId, String timeLevel, String timePeriod) {
        BigDecimal value = null;
        if (targetData != null) {
            for (TargetData data : targetData) {
                if (measureId.equalsIgnoreCase(data.getMeasureId()) && timeLevel.equalsIgnoreCase(data.getTimeLevel())) {
                    value = data.getTargetValues().get(timePeriod);
                }
            }
        }
        return value;
    }

    public Double getDeviationVal(Double currVal, Double targetValue) {
        Double devVal = null;
        if (currVal != null && targetValue != null) {
            devVal = currVal - targetValue;
        }
        return devVal;
    }

    public BigDecimal getDeviationPer(Double currVal, Double targetValue) {
        Double devPer = null;
        BigDecimal devBigdec = BigDecimal.ZERO;
        if (currVal != null && targetValue != null) {
            if (targetValue != 0.0) {
                devBigdec = new BigDecimal((((currVal - targetValue) / targetValue) * 100));
            } else {
                devBigdec = new BigDecimal((currVal) * 100.0);
                devPer = (currVal) * 100.0;
            }
        }
        return devBigdec;
    }

    public String getBasicKpiColor(Double basicDevVal, String elementID) {
        String color = "";
        if (KpiStTypeHashMap.get(elementID) == null) {
            KpiStTypeHashMap.put(elementID, "Standard");
        }
        if (!KpiStTypeHashMap.isEmpty()) {
            if (getKpiStTypeHashMap().get(elementID).equalsIgnoreCase("Standard")) {
                if (basicDevVal > 0) {
                    color = "9ACD32";//green
                } else if (basicDevVal < 0) {
                    color = "f24040";//red
                } else {
                    color = "#FF9933";
                }
            } else {
                if (basicDevVal > 0) {
                    color = "f24040";
                } else if (basicDevVal < 0) {
                    color = "9ACD32";
                } else {
                    color = "#FF9933";
                }
            }
        }
        return color;
    }

    public Map<String, String> getKPIDrill() {
        return this.kpiDrill;
    }

    public void setInsightAndCommentViewStaus(boolean insightViewStatus, boolean commentViewStatus, boolean graphViewStatus) {
        this.showInsights = insightViewStatus;
        this.showComments = commentViewStatus;
        this.showGraphs = graphViewStatus;

    }

    public boolean isShowInsights() {
        return showInsights;
    }

    public boolean isShowComments() {
        return showComments;
    }

    public boolean isShowGraphs() {
        return showGraphs;
    }

    public void setKpiStTypeHashMap(HashMap<String, String> KpiStTypeHashMap) {
        this.KpiStTypeHashMap = KpiStTypeHashMap;
    }

    public HashMap<String, String> getKpiStTypeHashMap() {
        return KpiStTypeHashMap;
    }

    public HashMap<Integer, String> getKPISequenceHashMap() {
        return KPISequenceHashMap;
    }

    public void setKPISequenceHashMap(HashMap<Integer, String> KPISequenceHashMap) {
        this.KPISequenceHashMap = KPISequenceHashMap;
    }

    public void addelementNames(String elementId, String elementName) {
        elementNames.put(elementId, elementName);
    }

    public String getelementNames(String elementId) {
        return elementNames.get(elementId);
    }

    public boolean isMTDChecked() {
        return mtdChecked;
    }

    public boolean isQTDChecked() {
        return qtdChecked;
    }

    public boolean isYTDChecked() {
        return ytdChecked;
    }

    public boolean isCurrentChecked() {
        return currentChecked;
    }

    public void setMtdQtdYtdViewStatus(boolean mtdstatus, boolean qtdStatus, boolean ytdStatus, boolean currentStatus) {
        this.mtdChecked = mtdstatus;
        this.qtdChecked = qtdStatus;
        this.ytdChecked = ytdStatus;
        this.currentChecked = currentStatus;
    }

    public void setDrillViewBys(String drillViewBys) {
        this.drillViewBys = drillViewBys;
    }

    public String getDrillViewBys() {
        return drillViewBys;
    }

    public void addKpiTargetelementMap(String elementId, String Targetelement) {
        this.kpiTargetMapelements.put(elementId, Targetelement);
    }

    public String getKpiTragetMap(String elementId) {
        return this.kpiTargetMapelements.get(elementId);
    }

    public void setTargetElementIds(List<String> targetelementIds) {
        this.targetelementIds = targetelementIds;
    }

    public List<String> getTargetElementIds() {
        return this.targetelementIds;
    }

    public void addTargetKPIElement(String refElementId, KPIElement kpiElement) {
        this.targetkpiElementsMap.put(refElementId, kpiElement);
    }

    public List<KPIElement> getTargetKPIElements(String elementId) {
        return this.targetkpiElementsMap.get(elementId);
    }

    public List<KPIElement> gettargetKpiElements() {
        List<KPIElement> targetelems = new ArrayList<KPIElement>();
        if (this.targetelementIds != null) {
            for (String elem : this.targetelementIds) {
                targetelems.addAll(this.targetkpiElementsMap.get(elem));
            }
        }
        return targetelems;
    }

    public BigDecimal getDeviationVal(BigDecimal currVal, BigDecimal targetValue) {
        BigDecimal devVal = null;
        Double current = 0.0;
        Double target = 0.0;
        Double dev = 0.0;
        if (currVal != null && targetValue != null) {
            current = currVal.doubleValue();
            target = targetValue.doubleValue();
            dev = current - target;
            devVal = new BigDecimal(dev);
//           devVal = currVal.subtract(targetValue) ;
        }
        return devVal;
    }

    public BigDecimal getDeviationPer(BigDecimal currVal, BigDecimal targetValue) {
        BigDecimal devPer = null;
        double val = 100;
        Double current = 0.0;
        Double target = 0.0;
        Double devperVal = 0.0;
        if (currVal != null && targetValue != null) {
            current = currVal.doubleValue();
            target = targetValue.doubleValue();
            if (targetValue != BigDecimal.ZERO) {
                devperVal = (((current - target) / target) * 100);
            } //              devPer =   (((currVal.subtract(targetValue)).divide(targetValue)).multiply(BigDecimal.valueOf(val)));
            //            devPer = (((currVal - targetValue) / targetValue) * 100) ;
            else {
                devperVal = current * 100;
            }
        }
        devPer = new BigDecimal(devperVal);
        return devPer;
    }

    public void addKPIDrillRepType(String elementId, String reporttype) {
        kpiDrillRepType.put(elementId, reporttype);
    }

    public String getKPIDrillRepType(String elementId) {
        return kpiDrillRepType.get(elementId);
    }

    public void clerKpiDrill() {
        kpiDrill.clear();
    }

    public void clearKpiDrillType() {
        kpiDrillRepType.clear();
    }
}
