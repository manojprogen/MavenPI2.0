/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import com.google.common.collect.Iterables;
import com.progen.report.ReportParameter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import prg.db.Container;

/**
 *
 * @author Administrator
 */
public class ColorGroup implements Observer, Serializable {

    private static final long serialVersionUID = 753264711956228L;
    private ArrayList<ColorCode> colorCodeLst = new ArrayList<ColorCode>();
    Container localContainer;
    private ArrayList<ColorCode> colorCodeForDefaultParams = new ArrayList<ColorCode>();
    private boolean rowWiseColor;
    private String dimentionValue;
    private List<String> eleId = new ArrayList<String>();
    private List<String> rowViewByValues = new ArrayList<String>();
    private String ViewByColumnValues = "";
    private boolean isAvgBased = false;
    private boolean isPercentBased = false;
    private boolean minMaxBased = false;
    private boolean isNewObject = false;
    String rowId;
    private Integer noOfDays;
    private boolean isColorAppyForAllParameters = false;

    public ColorCode createColorCode(String measId, ReportParameter repParameter, String crossTabMesId, String viewby)//, HashMap<String,String[]> colorCodeMap)
    {

        ColorCode colorCode = new ColorCode(measId, repParameter, crossTabMesId, viewby);//, rowEdge, colEdge,colorCodeMap);

        if (this.colorCodeLst.contains(colorCode)) {
            if (colorCodeLst.get(colorCodeLst.lastIndexOf(colorCode)).getViewByColumnValues().trim().equalsIgnoreCase(viewby)) {
                colorCode = colorCodeLst.get(colorCodeLst.lastIndexOf(colorCode));
            } else {
                colorCodeLst.add(colorCode);
            }
        } else {
            colorCodeLst.add(colorCode);

        }
//        colorCode.setCrosstabMeasure(crossTabMesId);
        return colorCode;
    }

    public boolean isColorCodePresent(String measId, ReportParameter repParameter, String crossTabMesId) {
        ColorCode colorCode = this.getColorCode(measId, repParameter, crossTabMesId);
        return colorCode.isValid();
    }

    //added by Dinanath
    public ColorCode getColorCodeObj(String measId, ReportParameter repParameter, String crossTabMesId) {
        ColorCode colorCode = this.getColorCode(measId, repParameter, crossTabMesId);
        return colorCode;
    }

    public boolean isColorCodePresent(String measId) {
        boolean status = false;
//       for(ColorCode colorCode:colorCodeLst){
//           if(colorCode.getMeasure().equalsIgnoreCase(measId))
//                status=true;
////       if(eleId.contains(measId))
//          // status=true;
//           break;
//       }
        this.rowId = measId;

        for (int i = 0; i < colorCodeLst.size(); i++) {
            ColorCode colorCode = colorCodeLst.get(i);
            if (colorCode.getMeasure().equalsIgnoreCase(measId)) {
                status = true;
            }
        }

        return status;
    }

    public String getColor(String measId, double endValue, ReportParameter repParameter, String crossTabMesId, BigDecimal diff, String Viewby,boolean flag) {
        String color = "";
        isColorAppyForAllParameters=flag;
        if (rowWiseColor) {

//             for(int i=0;i<colorCodeLst.size();i++){
//             ColorCode colorCode=colorCodeLst.get(i);
//             if(colorCode.getMeasure().equalsIgnoreCase(measId) && this.rowViewByValues.contains(this.getDimentionValue()))
//           }
            // ColorCode colorCode=null;
            if (this.rowViewByValues.contains(this.getDimentionValue())) {
                for (int i = 0; i < colorCodeLst.size(); i++) {
                    ColorCode colorCode = colorCodeLst.get(i);
                    if (colorCode.getMeasure().equalsIgnoreCase(rowId)) {
                        if(isColorAppyForAllParameters){
                         color= colorCode.getColor(endValue, this.noOfDays, diff);
                         }else
                        color = colorCode.getColor(endValue, this.noOfDays, diff, Viewby);
                    }
                }
            } else {
                color = "";
            }

        }
        //by Ram 02May2016
         if (Container.isColorAppyAcrossCurrentData()) {
            ColorCode colorCode = null;
            if (this.isAvgBased) {
                colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, Viewby);
            } else {
                if(isColorAppyForAllParameters){   //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
                    }else
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, Viewby);
            }

            if (colorCode != null) {
              if(isColorAppyForAllParameters){   //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    color =  colorCode.getColor(endValue, this.noOfDays, diff);
                    }else
                color = colorCode.getColor(endValue, this.noOfDays, diff, Viewby);

            } else {
                color = "";
            }
        }
         else{
             if(this.getEleId().contains(measId)) {
            ColorCode colorCode = null;
            if (this.isAvgBased) {
                colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, Viewby);
            } else {
                if(isColorAppyForAllParameters){   //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
                    }else
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, Viewby);
            }

            if (colorCode != null) {
              if(isColorAppyForAllParameters){   //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    color =  colorCode.getColor(endValue, this.noOfDays, diff);
                    }else
                color = colorCode.getColor(endValue, this.noOfDays, diff, Viewby);

            } else {
                color = "";
            }
        }
    }
//        
        return color;

    }

    public String getColor(String measId, double endValue, ReportParameter repParameter, String crossTabMesId, BigDecimal diff, String Viewby, BigDecimal AvgVal,ColorCode colorCodefrom,boolean flag) {
        String color = "";
        isColorAppyForAllParameters=flag;
        if (rowWiseColor) {

//             for(int i=0;i<colorCodeLst.size();i++){
//             ColorCode colorCode=colorCodeLst.get(i);
//             if(colorCode.getMeasure().equalsIgnoreCase(measId) && this.rowViewByValues.contains(this.getDimentionValue()))
//           }
            // ColorCode colorCode=null;
            if (this.rowViewByValues.contains(this.getDimentionValue())) {
                for (int i = 0; i < colorCodeLst.size(); i++) {
                    ColorCode colorCode = colorCodeLst.get(i);
                    if (colorCode.getMeasure().equalsIgnoreCase(rowId)) {             //Added by Ram For Apply color for all ViewBys
                         if(isColorAppyForAllParameters){
                         color= colorCode.getColor(endValue, this.noOfDays, diff);
                         }else
                        color = colorCode.getColor(endValue, this.noOfDays, diff, Viewby);
                    }
                }
            } else {
                color = "";
            }

        }
             if(this.getEleId().contains(measId)) {
            ColorCode colorCode = null;
            if (colorCodefrom.isIsAvgBased()) {
                 
                colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, Viewby);
            }else {
                    if(isColorAppyForAllParameters){   //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
                    }else
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, Viewby);
            }

            if (colorCode != null) {
                if (colorCode.isIsPercentBased()) {
                    color = colorCode.getPercentBasisColor(endValue, this.noOfDays, diff, AvgVal);
                } else if (colorCode.isIsAvgBased()) {
                    color = colorCode.getAvgBasisColor(endValue, this.noOfDays, diff, AvgVal);
                } else {
                    if(isColorAppyForAllParameters){  //Added By Ram 15April2016 for apply color on measure of All Viewbys.  
                    color = colorCode.getColor(endValue, this.noOfDays, diff);  
                    }else
                    color = colorCode.getColor(endValue, this.noOfDays, diff, Viewby);
                    
                }

            } else {
                color = "";
            }
        }

//        
        return color;

    }

    public String getColor(String measId, double endValue, ReportParameter repParameter, String crossTabMesId, BigDecimal diff) {
        String color = "";

        if (rowWiseColor) {

//             for(int i=0;i<colorCodeLst.size();i++){
//             ColorCode colorCode=colorCodeLst.get(i);
//             if(colorCode.getMeasure().equalsIgnoreCase(measId) && this.rowViewByValues.contains(this.getDimentionValue()))
//           }
            // ColorCode colorCode=null;
            if (this.rowViewByValues.contains(this.getDimentionValue())) {
                for (int i = 0; i < colorCodeLst.size(); i++) {
                    ColorCode colorCode = colorCodeLst.get(i);
                    if (colorCode.getMeasure().equalsIgnoreCase(rowId)) {
                        color = colorCode.getColor(endValue, this.noOfDays, diff);
                    }
                }
            } else {
                color = "";
            }

        }
        if (this.getEleId().contains(measId)) {
            ColorCode colorCode = null;
            if (this.isAvgBased) {
                colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, "");
            } else {
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, "");
            }

            if (colorCode != null) {

                color = colorCode.getColor(endValue, this.noOfDays, diff);

            } else {
                color = "";
            }
        }

//        
        return color;

    }

    public String getColor(String measId, double endValue, ReportParameter repParameter, String crossTabMesId, BigDecimal diff, BigDecimal AvgVal, String Viewby,boolean flag) {
        String color = "";
        isColorAppyForAllParameters=flag;
        if (rowWiseColor) {

//             for(int i=0;i<colorCodeLst.size();i++){
//             ColorCode colorCode=colorCodeLst.get(i);
//             if(colorCode.getMeasure().equalsIgnoreCase(measId) && this.rowViewByValues.contains(this.getDimentionValue()))
//           }
            // ColorCode colorCode=null;
            if (this.rowViewByValues.contains(this.getDimentionValue())) {
                for (int i = 0; i < colorCodeLst.size(); i++) {
                    ColorCode colorCode = colorCodeLst.get(i);
                    if (colorCode.getMeasure().equalsIgnoreCase(rowId)) {
                        if(isColorAppyForAllParameters){  //Added By Ram For apply color of All viewbys
                         color= colorCode.getColor(endValue, this.noOfDays, diff);
                         }else
                        color = colorCode.getColor(endValue, this.noOfDays, diff, "");
                    }
                }
            } else {
                color = "";
            }

        }
        //by Ram 02May2016
        if (Container.isColorAppyAcrossCurrentData()) {
               ColorCode colorCode = null;
            colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, "");
            if (this.isNewObject) {
                         if(isColorAppyForAllParameters){  //Added By Ram For apply color of All viewbys
                         colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
                         }else
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, Viewby);
            }
            if (colorCode != null) {
                if (!this.isNewObject) {
                    if (colorCode.isIsPercentBased()) {
                        color = colorCode.getPercentBasisColor(endValue, this.noOfDays, diff, AvgVal);
                    } else {
                    color = colorCode.getAvgBasisColor(endValue, this.noOfDays, diff, AvgVal);
                    }
                } else if (colorCode.isIsPercentBased()) {
                    color = colorCode.getPercentBasisColor(endValue, this.noOfDays, diff, AvgVal);
                } else {
                    color = colorCode.getColor(endValue, this.noOfDays, diff);
                }
            } else {
                color = "";
            }
        }
         else{
        if (this.getEleId().contains(measId)) {
            ColorCode colorCode = null;
            colorCode = this.getColorCodewithAvgBasis(measId, repParameter, crossTabMesId, "");
            if (this.isNewObject) {
                         if(isColorAppyForAllParameters){  //Added By Ram For apply color of All viewbys
                         colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
                         }else
                colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, Viewby);
            }
            if (colorCode != null) {
                if (!this.isNewObject) {
                    if (colorCode.isIsPercentBased()) {//added by Dinanath
                        color = colorCode.getPercentBasisColor(endValue, this.noOfDays, diff, AvgVal);
                    } else {
                    color = colorCode.getAvgBasisColor(endValue, this.noOfDays, diff, AvgVal);
                    }
                } else if (colorCode.isIsPercentBased()) {//added by Dinanath
                    color = colorCode.getPercentBasisColor(endValue, this.noOfDays, diff, AvgVal);
                } else {
                    color = colorCode.getColor(endValue, this.noOfDays, diff);
                }
            } else {
                color = "";
            }
        }
        }
//        
        return color;

    }

    public ColorCode getColorCode(String measId, ReportParameter repParameter, String crossTabMesId) {
        ColorCode clrcode = null;
        Iterable<ColorCode> matchingColorCodes =null;
        //By Ram 2 May 2016
        if (Container.isColorAppyAcrossCurrentData()) {
             matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateAcrossCurrentData(measId, crossTabMesId));
        }else{
         matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicate(measId, crossTabMesId));
        }
        Iterator<ColorCode> paramIter = matchingColorCodes.iterator();
        if (paramIter.hasNext()) {
            return paramIter.next();
        } else {
            return new ColorCode(measId, repParameter, crossTabMesId);
        }

    }

    public ColorCode getColorCodewithViewBY(String measId, ReportParameter repParameter, String crossTabMesId, String Viewby) {
        ColorCode clrcode = null;
        Iterable<ColorCode> matchingColorCodes =null;
         //By Ram 2 May 2016
        if (Container.isColorAppyAcrossCurrentData()) {
             matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateWithViewbyAcrossCurrentData(measId, crossTabMesId, Viewby));
        }else{
         matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateWithViewby(measId, crossTabMesId, Viewby));
        }
        Iterator<ColorCode> paramIter = matchingColorCodes.iterator();
        if (paramIter.hasNext()) {

            clrcode = paramIter.next();
        } else {
            clrcode = new ColorCode(measId, repParameter, crossTabMesId);
        }

        return clrcode;

    }
    //By Ram 17Apr2017 color code for All ViewBys.
       public ColorCode getColorCodewithoutViewBY(String measId, ReportParameter repParameter, String crossTabMesId) {
        ColorCode clrcode = null;
        Iterable<ColorCode> matchingColorCodes=null;
        if (Container.isColorAppyAcrossCurrentData()) {
            matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateWithoutViewbyAcrossCurrentData(measId, crossTabMesId));
        }else{
         matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateWithoutViewby(measId, crossTabMesId));
        }
        Iterator<ColorCode> paramIter = matchingColorCodes.iterator();
        if (paramIter.hasNext()) {

            clrcode = paramIter.next();
        } else {
            clrcode = new ColorCode(measId, repParameter, crossTabMesId);
        }

        return clrcode;

    }

    public ColorCode getColorCodewithAvgBasis(String measId, ReportParameter repParameter, String crossTabMesId, String Viewby) {
        ColorCode clrcode = null;
        Iterable<ColorCode> matchingColorCodes = Iterables.filter(this.colorCodeForDefaultParams, ColorCode.getReportMeasurePredicateAvgBasis(measId, crossTabMesId));
        Iterator<ColorCode> paramIter = matchingColorCodes.iterator();
        if (paramIter.hasNext()) {
            clrcode = paramIter.next();
            isNewObject = false;
        } else {
            isNewObject = true;
            clrcode = new ColorCode(measId, repParameter, crossTabMesId);
        }
        return clrcode;
    }

    public ColorCodeTransferObject[] getColorCodeTransObject() {
        ColorCodeTransferObject[] colorCodeTrans = new ColorCodeTransferObject[colorCodeLst.size()];
        ReportParameter repParam;
//        String rowViewBys="";
//        String colViewBys="";
//code modified by Dinanath
        ArrayList rowViewBys = new ArrayList();
        ArrayList colViewBys = new ArrayList();
        String measureId;
        StringBuilder paramXml;
        StringBuilder ruleXml;
        String crossTabMes = "";
        ColorCodeTransferObject colorCodeTransObj = null;
        int row = 0;
        for (ColorCode colorList : colorCodeLst) {
            repParam = colorList.getRepParameter();
            measureId = colorList.getMeasure();
            int ct = -1;
            for (String rowView : repParam.getRowViewByForParameter()) {
//                rowViewBys+=rowView;
                if (!rowViewBys.contains(rowView)) {
                    rowViewBys.add(rowView);
                }
                ct++;
                if (ct != repParam.getRowViewByForParameter().size() - 1) {
//                rowViewBys+=",";
                    rowViewBys.add(",");
                }
            }

            ct = -1;
            for (String colView : repParam.getColViewByForParameter()) {
//                colViewBys+=colView;
                if (!colViewBys.contains(colView)) {
                    colViewBys.add(colView);
                }
                ct++;
                if (ct != repParam.getColViewByForParameter().size() - 1) {
//                colViewBys+=",";
                    colViewBys.add(",");
                }

            }
            ruleXml = colorList.toXml();
            paramXml = repParam.toXml();
            boolean IsAvgbased = colorList.isIsAvgBased();
            boolean isPercentBased = colorList.isIsPercentBased();
            boolean isMinMaxBased = colorList.isMinMaxBased();
            crossTabMes = colorList.getCrosstabMeasure();
            if (isMinMaxBased) {
                colorCodeTransObj = new ColorCodeTransferObject(measureId, rowViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), colViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), paramXml, ruleXml, crossTabMes, false, isMinMaxBased, isPercentBased);
            } else if (IsAvgbased) {
                colorCodeTransObj = new ColorCodeTransferObject(measureId, rowViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), colViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), paramXml, ruleXml, crossTabMes, IsAvgbased, false, isPercentBased);
            } else if (isPercentBased) {
                colorCodeTransObj = new ColorCodeTransferObject(measureId, rowViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), colViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), paramXml, ruleXml, crossTabMes, IsAvgbased, false, isPercentBased);
            } else {
                colorCodeTransObj = new ColorCodeTransferObject(measureId, rowViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), colViewBys.toString().replaceAll("\\[", "").replaceAll("\\]", ""), paramXml, ruleXml, crossTabMes);

            }
            colorCodeTransObj.setGradientBased(colorList.isGradientBased());
//            colorCodeTrans=colorCodeTransObj;
            colorCodeTrans[row] = colorCodeTransObj;
            row++;

        }
        return colorCodeTrans;
    }

    @Override
    public void update(Observable o, Object arg) {
        Iterable<ColorCode> matchingColorCodes = Iterables.filter(colorCodeLst, ColorCode.getReportParameterPredicate((ReportParameter) o));
        this.colorCodeForDefaultParams.clear();
        //Added by Ram For apply color on all parameters.
       localContainer =new Container();
        if (localContainer.isColorAppyForAllParameters()) {                  
            for (int i = 0; i < colorCodeLst.size(); i++) {
                this.colorCodeForDefaultParams.add(colorCodeLst.get(i));
            }
        }else{  //end Ram code
        for (ColorCode colorCode : matchingColorCodes) {
            this.colorCodeForDefaultParams.add(colorCode);
        }
        }

        noOfDays = (Integer) arg;
    }

    public String getColorRuleJSON(String measId, ReportParameter repParameter, String crossTabMesId, String viewby) {
        String colorRules = "";

        if (this.isColorCodePresent(measId, repParameter, crossTabMesId)) {

            ColorCode colorCode = this.getColorCodewithViewBY(measId, repParameter, crossTabMesId, viewby);
            if (colorCode.isIsAvgBased()) {
                colorRules = colorCode.getAvgColorCodeJSON(this.noOfDays);
            } else if (colorCode.isIsPercentBased()) {
                colorRules = colorCode.getAvgColorCodeJSON(this.noOfDays);
            } else {
                colorRules = colorCode.getColorCodeJSON(this.noOfDays);
            }
        }

        return colorRules;
    }
    //By Ram for getting all applied color in GUI. 
     public String getColorRuleJSON(String measId, ReportParameter repParameter, String crossTabMesId) {
        String colorRules = "";

        if (this.isColorCodePresent(measId, repParameter, crossTabMesId)) {

            ColorCode colorCode = this.getColorCodewithoutViewBY(measId, repParameter, crossTabMesId);
            if (colorCode.isIsAvgBased()) {
                colorRules = colorCode.getAvgColorCodeJSON(this.noOfDays);
            } else if (colorCode.isIsPercentBased()) {
                colorRules = colorCode.getAvgColorCodeJSON(this.noOfDays);
            } else {
                colorRules = colorCode.getColorCodeJSON(this.noOfDays);
            }
        }

        return colorRules;
    }

    public void resetColorForMeasure(String measId, ReportParameter repParameter, String crossTabMesId) {
        if (this.isColorCodePresent(measId, repParameter, crossTabMesId)) {

            ColorCode colorCode = this.getColorCode(measId, repParameter, crossTabMesId);
            colorCodeLst.remove(colorCode);
            colorCodeForDefaultParams.remove(colorCode);
        }
    }

    public boolean isRowWiseColor() {
        return rowWiseColor;
    }

    public void setRowWiseColor(boolean rowWiseColor) {
        this.rowWiseColor = rowWiseColor;
    }

    public String getDimentionValue() {
        return dimentionValue;
    }

    public void setDimentionValue(String dimentionValue) {
        this.dimentionValue = dimentionValue;
    }

    public List<String> getEleId() {
        return eleId;
    }

    public void setEleId(List<String> eleId) {
        this.eleId = eleId;
    }

    public List<String> getRowViewByValues() {
        return rowViewByValues;
    }

    public void setRowViewByValues(List<String> rowViewByValues) {
        this.rowViewByValues = rowViewByValues;
    }

//    public void setViewByColumnValues(String rowViewByColumnValues) {
//        this.ViewByColumnValues = rowViewByColumnValues;
//    }
    /**
     * @return the ViewByColumnValues
     */
    public String getViewByColumnValues() {
        return ViewByColumnValues;
    }

    /**
     * @param ViewByColumnValues the ViewByColumnValues to set
     */
    public void setViewByColumnValues(String ViewByColumnValues) {
        this.ViewByColumnValues = ViewByColumnValues;
    }

    /**
     * @return the isAvgBased
     */
    public boolean isIsAvgBased() {
        return isAvgBased;
    }

    /**
     * @param isAvgBased the isAvgBased to set
     */
    public void setIsAvgBased(boolean isAvgBased) {
        this.isAvgBased = isAvgBased;
    }

    /**
     * @return the minMaxBased
     */
    public boolean isMinMaxBased() {
        return minMaxBased;
    }

    /**
     * @param minMaxBased the minMaxBased to set
     */
    public void setMinMaxBased(boolean minMaxBased) {
        this.minMaxBased = minMaxBased;
    }

    //added by Dinanath for below and above avg
    public void setIsPercentBased(boolean isPercentBased) {
        this.isPercentBased = isPercentBased;
}

    public boolean isIsPercentBased() {
        return isPercentBased;
    }
    //end of code by Dinanath

}
