package com.progen.report.kpi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KPISingleGroupHelper implements Serializable {

    private static final long serialVersionUID = 2276765074887898L;
    private List<String> elementNameList;
    private List<String> elementIds;
    private String calcType;
    private String groupName;
    private List<String> atrelementIds;
    // private List<String> atrelementnames;
    private List<String> symbols;
    private List<String> alignment;
    private List<String> font;
    private List<String> numberFormat;
    private List<String> round;
    private String kpiType;
    private boolean KpiFormat;
    private boolean checkhead;
    private List<String> numberFormat1;
    private List<String> selectelementIds;
    private List<String> backGround;
    private List<String> negativevalue;
    Map<String, String> GroupkpiDrill = new HashMap<String, String>();
    Map<String, String> GroupkpiDrillType = new HashMap<String, String>();

    public void addGroupKPIDrill(String elementId, String reportId) {
        GroupkpiDrill.put(elementId, reportId);
    }

    public String getGroupKPIDrill(String elementId) {
        return GroupkpiDrill.get(elementId);
    }

    public List getElementNameList() {
        return elementNameList;
    }

    public void setElementNameList(List<String> elementNameList) {
        this.elementNameList = elementNameList;
    }

    public List<String> getElementIds() {
        return elementIds;
    }

    public void setElementIds(List<String> elementIds) {
        this.elementIds = elementIds;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getAtrelementIds() {
        if (atrelementIds != null) {
            return atrelementIds;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setAtrelementIds(List<String> atrelementIds) {
        this.atrelementIds = atrelementIds;
    }

    public List<String> getSymbols() {
        if (symbols != null) {
            return symbols;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<String> getAlignment() {
        if (alignment != null) {
            return alignment;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setAlignment(List<String> alignment) {
        this.alignment = alignment;
    }

    public List<String> getFont() {
        if (font != null) {
            return font;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setFont(List<String> font) {
        this.font = font;
    }

    public List<String> getNumberFormat() {
        if (numberFormat != null) {
            return numberFormat;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setNumberFormat(List<String> numberFormat) {
        this.numberFormat = numberFormat;
    }

    public List<String> getRound() {
        if (round != null) {
            return round;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setRound(List<String> round) {
        this.round = round;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public void addGroupKPIDrillType(String elementId, String reportType) {
        GroupkpiDrillType.put(elementId, reportType);
    }

    public String getGroupKPIDrillType(String elementId) {
        return GroupkpiDrillType.get(elementId);
    }

    public void setcheckformat(boolean KpiFormat) {
        this.KpiFormat = KpiFormat;
    }

    public boolean getcheckformat() {
        return this.KpiFormat;
    }

    public void setcheckhead(boolean checkhead) {
        this.checkhead = checkhead;
    }

    public boolean getcheckhead() {
        return this.checkhead;
    }

    public List<String> getgblFormatList() {
        if (numberFormat1 != null) {
            return numberFormat1;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setgblFormatList(List<String> numberFormat1) {
        this.numberFormat1 = numberFormat1;
    }

    public List<String> getselectrepIds() {
        if (selectelementIds != null) {
            return selectelementIds;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setselectrepIds(List<String> selectelementIds) {
        this.selectelementIds = selectelementIds;
    }

    public List<String> getBackGround() {
        if (backGround != null) {
            return backGround;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setBackGround(List<String> backGround) {
        this.backGround = backGround;
    }

    public List<String> getNegativevalue() {
        if (negativevalue != null) {
            return negativevalue;
        } else {
            return new ArrayList<String>();
        }
    }

    public void setNegativevalue(List<String> negativevalue) {
        this.negativevalue = negativevalue;
    }
}
