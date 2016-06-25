/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import com.google.common.base.Predicate;
import com.progen.report.ReportParameter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import prg.db.PbReturnObject;
import prg.db.Container;

/**
 *
 * @author Administrator
 */
public class ColorCode implements Serializable
{
     private static final long serialVersionUID = 2276765074881786L;
    private String measElementId;
    private ArrayList<ColorCodeRule> colorCodeRule;
    private ReportParameter repParameter;
    private String crossTabMeasure;
    private boolean isGradientBased;
    private boolean rowWiseColor;
    private List<String>rowViewBys;
     private String ViewByColumnValues="";
     private boolean isAvgBased=false;
     private boolean isPercentBased=false;
     private boolean minMaxBased=false;
    public ColorCode(String measure,ReportParameter repParameter,String crossTabMesId)
    {
        this.measElementId = measure;
        this.colorCodeRule = new ArrayList<ColorCodeRule>();
        this.repParameter=repParameter;
        this.crossTabMeasure=crossTabMesId;
        this.ViewByColumnValues=ViewByColumnValues;
    }
     public ColorCode(String measure,ReportParameter repParameter,String crossTabMesId,String ViewByColumnValues)
    {
        this.measElementId = measure;
        this.colorCodeRule = new ArrayList<ColorCodeRule>();
        this.repParameter=repParameter;
        this.crossTabMeasure=crossTabMesId;
        this.ViewByColumnValues=ViewByColumnValues;
    }

    public void addRule(String color, double StrtValue, double EndValue, MathOperator operator) {
        ColorCodeRule colorRule = new ColorCodeRule(color, StrtValue, EndValue, operator);
        this.colorCodeRule.add(colorRule);
//            String getcolourXml=colorRule.toXMl();
//            System.out.println("colourxml------"+getcolourXml);
    }

//    public String getColor(double strtValue, double endValue)
//    {
//
//    }
    public boolean isValid() {
        return (!this.colorCodeRule.isEmpty());
    }

    public void resetRule() {
        this.setColorCodeRule(new ArrayList<ColorCodeRule>());

    }
    public String getCrosstabMeasure(){
        return this.crossTabMeasure;
    }
    public void setCrosstabMeasure(String crossTabMeasure){
        this.crossTabMeasure = crossTabMeasure;
    }
     public String getColor(double value,int noOfDays,BigDecimal diff,String viewby) {
        for (ColorCodeRule rule : colorCodeRule) {

           String Colorrule="";
            if (rule.isRuleApplicable(value, noOfDays))
            {
                if(isGradientBased)
                  return getGradientColor(value, rule,noOfDays,diff);
                else
                    if(this.ViewByColumnValues.trim().equalsIgnoreCase(viewby.trim())){
                  Colorrule=rule.getColor();
                    }
                   return Colorrule;
                    
            }
        }
        return "";
    }
    public String getColor(double value,int noOfDays,BigDecimal diff) {
        for (ColorCodeRule rule : colorCodeRule) {
            
           String Colorrule="";
            if (rule.isRuleApplicable(value, noOfDays))
            {
                if(isGradientBased)
                  return getGradientColor(value, rule,noOfDays,diff);
                else
                    
                  Colorrule=rule.getColor();
                 
                   return Colorrule;
                    
            }
        }
        return "";
    }
     public String getAvgBasisColor(double value,int noOfDays,BigDecimal diff,BigDecimal avgVal) {
        for (ColorCodeRule rule : colorCodeRule) {

           String Colorrule="";
            if (rule.isAvgRuleApplicable(value, avgVal))
            {
                if(isGradientBased)
                  return getGradientColor(value, rule,noOfDays,diff);
                else
                  
                  Colorrule=rule.getColor();
                  
                   return Colorrule;
                    
            }
        }
        return "";
    }
     //added by Dinanath for below and above avg
      public String getPercentBasisColor(double value,int noOfDays,BigDecimal diff,BigDecimal avgVal) {
        for (ColorCodeRule rule : colorCodeRule) {

           String Colorrule="";
            if (rule.isPercentRuleApplicable(value, avgVal))
            {
                if(isGradientBased)
                  return getGradientColor(value, rule,noOfDays,diff);
                else
                  Colorrule=rule.getColor();
                
                   return Colorrule;
            }
        }
        return "";
    } 

    public String getGradientColor(double divisor,ColorCodeRule rule,int noOfDays,BigDecimal diff)
    {
        String color=rule.getColor();
        double startValue=rule.getStrtValue();
        double endValue=rule.getEndValue();
        MathOperator operator=rule.getOperator();
        BigDecimal divident=new BigDecimal(startValue).multiply(new BigDecimal(noOfDays));
        BigDecimal divisorBig=new BigDecimal(divisor);
//        if(operator==MathOperator.BETWEEN)
//            divident=new BigDecimal(endValue).multiply(new BigDecimal(noOfDays));
//        if(divisorBig.compareTo(divident)<0)
//        {
//            BigDecimal temp=BigDecimal.ZERO;
//            temp=divisorBig;
//            divisorBig=divident;
//            divident=temp;
//        }
//        return ColorConstants.getInstance().getGradientColor(divisorBig,divident,color);
        return ColorConstants.getInstance().getGradientColor(divisorBig,diff,color,operator);
    }

    public String getMeasure() {
        return this.measElementId;
    }

    public void setMeasure(String measElementId) {
        this.measElementId = measElementId;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof ColorCode) 
            if (this.measElementId.equals(((ColorCode) o).getMeasure())) 
                if ( this.crossTabMeasure.equals(((ColorCode)o).getCrosstabMeasure()) )
                    if (this.repParameter.equals(((ColorCode) o).getRepParameter()))
                        isEqual = true;
        return isEqual;
    }

    public ReportParameter getRepParameter() {
        return repParameter;
    }

    public void setRepParameter(ReportParameter repParameter) {
        this.repParameter = repParameter;
    }
    public StringBuilder toXml(){
        StringBuilder colorXml=new StringBuilder("");
        colorXml.append("<ColorCodeRules>");
        for(ColorCodeRule rule:colorCodeRule){
            colorXml.append(rule.toXMl());
        }
        colorXml.append("</ColorCodeRules>");        
        return colorXml;
    }



     public static Predicate<ColorCode> getReportParameterPredicate(final ReportParameter currentParameter)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                if ( currentParameter.equals(input.getRepParameter()))
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
     public static Predicate<ColorCode> getReportMeasurePredicate(final String measureId,final String crossTabMesId)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                //System.out.println("measureId---"+measureId+"---"+input.getMeasure());
                if ( measureId.equals(input.getMeasure()) && crossTabMesId.equals(input.getCrosstabMeasure()) )
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
     //Added By Ram 2 May 2016  for Cross Tab Across Current Data.
  public static Predicate<ColorCode> getReportMeasurePredicateAcrossCurrentData(final String measureId,final String crossTabMesId)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                if (crossTabMesId.equals(input.getCrosstabMeasure()) )  
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
      public static Predicate<ColorCode> getReportMeasurePredicateAvgBasis(final String measureId,final String crossTabMesId)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                //System.out.println("measureId---"+measureId+"---"+input.getMeasure());
                if ( measureId.equals(input.getMeasure()) && crossTabMesId.equals(input.getCrosstabMeasure()) && input.isAvgBased)
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
      public static Predicate<ColorCode> getReportMeasurePredicateWithViewby(final String measureId,final String crossTabMesId,final String ViewBy)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                //System.out.println("measureId---"+measureId+"---"+input.getMeasure());
                if ( measureId.equals(input.getMeasure()) && crossTabMesId.equals(input.getCrosstabMeasure()) && ViewBy.trim().equals(input.getViewByColumnValues().trim()))
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
      
       //Added By Ram 2 May 2016  for Cross Tab Across Current Data. 
    public static Predicate<ColorCode> getReportMeasurePredicateWithViewbyAcrossCurrentData(final String measureId,final String crossTabMesId,final String ViewBy)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
               if ( crossTabMesId.equals(input.getCrosstabMeasure()) && ViewBy.trim().equals(input.getViewByColumnValues().trim())) 
                    return true;
                else
                    return false;
            }
        };
        return colorCodePredicate;
    }
      
   //Added By Ram 15April2016 for All ViewBys Color
  public static Predicate<ColorCode> getReportMeasurePredicateWithoutViewby(final String measureId,final String crossTabMesId)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                //System.out.println("measureId---"+measureId+"---"+input.getMeasure());
                if (measureId.equals(input.getMeasure()) && crossTabMesId.equals(input.getCrosstabMeasure()))
                    return true;
                else
                    return false;
                
            }
        };
        return colorCodePredicate;
    }
  
    //Added By Ram 2 May 2016 for CrossTab Across Current Data
  public static Predicate<ColorCode> getReportMeasurePredicateWithoutViewbyAcrossCurrentData(final String measureId,final String crossTabMesId)
    {
        Predicate<ColorCode> colorCodePredicate = new Predicate<ColorCode>() {
            public boolean apply(ColorCode input) {
                if (crossTabMesId.equals(input.getCrosstabMeasure()))
                    return true;
                else
                    return false;
                
            }
        };
        return colorCodePredicate;
    }
     public String getColorCodeJSON(int noOfDays){
         StringBuilder colorData=new StringBuilder();
         BigDecimal stValueBig=null;
         BigDecimal endValueBig=null;
         PbReturnObject pbro=new PbReturnObject();
         colorData.append("{");
        // System.out.println("colorCodeRule size--"+colorCodeRule.size());
         String colors="Colors:[";
         String operators="Operators:[";
         String startValue="StartValues:[";
         String endValue="EndValues:[";
         for(int i=0;i<colorCodeRule.size();i++){
           //  System.out.println("colorCodeRule.get(i).startValue--"+colorCodeRule.get(i).startValue);
             stValueBig=new BigDecimal(colorCodeRule.get(i).startValue);
             endValueBig=new BigDecimal(colorCodeRule.get(i).endValue);
             colors+="\"";
             colors+=colorCodeRule.get(i).color;
             colors+="\"";
             operators+="\"";
             operators+=colorCodeRule.get(i).Operator;
             operators+="\"";
             startValue+="\"";
             //code modified by Dinanath
             if(noOfDays==0)
             startValue+=stValueBig.multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_DOWN);
             else
             startValue+=stValueBig.multiply(new BigDecimal(noOfDays)).setScale(2, RoundingMode.HALF_DOWN);
             startValue+="\"";
             endValue+="\"";
             if(noOfDays==0)
             endValue+=endValueBig.multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_DOWN);
             else
             endValue+=endValueBig.multiply(new BigDecimal(noOfDays)).setScale(2, RoundingMode.HALF_DOWN);
             endValue+="\"";

             if(i!=colorCodeRule.size()-1){
                 colors+=",";
                 operators+=",";
                 startValue+=",";
                 endValue+=",";
             }
             if(i==colorCodeRule.size()-1){
                 colors+="],";
                 operators+="],";
                 startValue+="],";
                 endValue+="],";
             }
         }
         String gradient="isGradientBased:[\""+isGradientBased+"\"],";
          String avgbased="isAvgBased:[\"false\"]";

         colorData.append(colors).append(operators).append(startValue).append(endValue).append(gradient).append(avgbased);

         colorData.append("}");
         String color=colorData.toString().replace("LESS_THAN","<").replace("GREATER_THAN",">").replace("GREATER_THAN_EQUAL_TO",">=").replace("LESS_THAN_EQUAL_TO","<=")
                      .replace("EQUAL_TO", "=").replace("BETWEEN", "<>").replace("NOT_EQUAL_TO", "!=");
         //System.out.println("colorData---"+color);
         return color;
     }
 public String getAvgColorCodeJSON(int noOfDays){
         StringBuilder colorData=new StringBuilder();
         BigDecimal stValueBig=null;
         BigDecimal endValueBig=null;
         PbReturnObject pbro=new PbReturnObject();
         colorData.append("{");
        // System.out.println("colorCodeRule size--"+colorCodeRule.size());
         String colors="Colors:[";
         String operators="Operators:[";
         String startValue="StartValues:[";
         String endValue="EndValues:[";
         for(int i=0;i<colorCodeRule.size();i++){
           //  System.out.println("colorCodeRule.get(i).startValue--"+colorCodeRule.get(i).startValue);
             stValueBig=new BigDecimal(colorCodeRule.get(i).startValue);
             endValueBig=new BigDecimal(colorCodeRule.get(i).endValue);
             colors+="\"";
             colors+=colorCodeRule.get(i).color;
             colors+="\"";
             operators+="\"";
             operators+=colorCodeRule.get(i).Operator;
             operators+="\"";
             startValue+="\"";
             if (noOfDays == 0) {
                 if (isAvgBased) {
                     startValue += stValueBig.doubleValue();
                 } else if (isPercentBased) {
                     startValue += stValueBig.doubleValue();
                 }else{
                     startValue += stValueBig.multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_DOWN);
                 }
             } else if (isAvgBased) {
                 startValue += stValueBig.doubleValue();
             } else if (isPercentBased) {
                 startValue += stValueBig.doubleValue();
             } else {
                 startValue += stValueBig.multiply(new BigDecimal(noOfDays)).setScale(2, RoundingMode.HALF_DOWN);
             }
             startValue+="\"";
             endValue+="\"";
             if (noOfDays == 0) {
                 if (isAvgBased) {
                     endValue += endValueBig.doubleValue();
                 } else if (isPercentBased) {
                     endValue += endValueBig.doubleValue();
                 }else{
                     endValue += endValueBig.multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_DOWN);
                 }
             } else if (isAvgBased) {
                 endValue += endValueBig.doubleValue();
             } else if (isPercentBased) {
                 endValue += endValueBig.doubleValue();
             } else {
                 endValue += endValueBig.multiply(new BigDecimal(noOfDays)).setScale(2, RoundingMode.HALF_DOWN);
             }
             endValue+="\"";

             if(i!=colorCodeRule.size()-1){
                 colors+=",";
                 operators+=",";
                 startValue+=",";
                 endValue+=",";
             }
             if(i==colorCodeRule.size()-1){
                 colors+="],";
                 operators+="],";
                 startValue+="],";
                 endValue+="],";
             }
         }
         
         String gradient="isGradientBased:[\""+isGradientBased+"\"],";
          String avgbased="isAvgBased:[\""+isAvgBased+"\"],";
          String percentbased="isPercentBased:[\""+isPercentBased+"\"],";
            String isminMaxBased="isMinMaxBased:[\""+minMaxBased+"\"]";

         
         colorData.append(colors).append(operators).append(startValue).append(endValue).append(gradient).append(avgbased).append(percentbased).append(isminMaxBased);

         colorData.append("}");
         String color=colorData.toString().replace("LESS_THAN","<").replace("GREATER_THAN",">").replace("GREATER_THAN_EQUAL_TO",">=").replace("LESS_THAN_EQUAL_TO","<=")
                      .replace("EQUAL_TO", "=").replace("BETWEEN", "<>").replace("NOT_EQUAL_TO", "!=");
         //System.out.println("colorData---"+color);
         return color;
     }
 public String getMinMaxColorCodeJSON(){
         StringBuilder colorData=new StringBuilder();
         BigDecimal stValueBig=null;
         BigDecimal endValueBig=null;
         PbReturnObject pbro=new PbReturnObject();
         colorData.append("{");
         String isminMaxBased="isMinMaxBased:[\""+"true"+"\"]";
         colorData.append(isminMaxBased);
         colorData.append("}");
         String color=colorData.toString().replace("LESS_THAN","<").replace("GREATER_THAN",">").replace("GREATER_THAN_EQUAL_TO",">=").replace("LESS_THAN_EQUAL_TO","<=")
                      .replace("EQUAL_TO", "=").replace("BETWEEN", "<>").replace("NOT_EQUAL_TO", "!=");
         //System.out.println("colorData---"+color);
         return color;
     }
    public void setColorCodeRule(ArrayList<ColorCodeRule> colorCodeRule) {
        this.colorCodeRule = colorCodeRule;
    }

    public boolean isGradientBased() {
        return isGradientBased;
    }

    public void setGradientBased(boolean isGradientBased) {
        this.isGradientBased = isGradientBased;
    }

   
    public List<String> getRowViewBys() {
        return rowViewBys;
    }

   
    public void setRowViewBys(List<String> rowViewBys) {
        this.rowViewBys = rowViewBys;
    }

    
    public boolean isRowWiseColor() {
        return rowWiseColor;
    }

    
    public void setRowWiseColor(boolean rowWiseColor) {
        this.rowWiseColor = rowWiseColor;
    }

//   public  boolean isColorApplicable(String dimentionValue) {
//       return rowViewBys.contains(dimentionValue);
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

    /**
     * @return the isMinMax
     */
     //added by Dinanath for below and above avg
    public void setIsPercentBased(boolean isPercentBased){
        this.isPercentBased=isPercentBased;
    }
    public boolean isIsPercentBased(){
        return isPercentBased;
    }
   //end of code by Dinanath
}
