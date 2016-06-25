/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Administrator
 */
public class ColorCodeRule implements Serializable {

    private static final long serialVersionUID = 2276765074881787L;
    String color;
    Double startValue;
    Double endValue;
    MathOperator Operator;
    String viewBy;
    boolean IsMinMax = false;

    public ColorCodeRule(String color, double strtValue, double endValue, MathOperator operator) {
        this.color = color;
        this.startValue = strtValue;
        this.endValue = endValue;
        this.Operator = operator;
        this.viewBy = "";

    }

    public ColorCodeRule(String color, double strtValue, double endValue, MathOperator operator, String ViewBy) {
        this.color = color;
        this.startValue = strtValue;
        this.endValue = endValue;
        this.Operator = operator;
        this.viewBy = ViewBy;

    }

    public ColorCodeRule(String color, double strtValue, double endValue, MathOperator operator, boolean isMinMax) {
        this.color = color;
        this.startValue = strtValue;
        this.endValue = endValue;
        this.Operator = operator;
        this.IsMinMax = true;
        this.viewBy = "";

    }

    public String getColor() {
        return this.color;
    }

    public Double getStrtValue() {
        return this.startValue;
    }

    public Double getEndValue() {
        return this.endValue;
    }

    public MathOperator getOperator() {
        return this.Operator;
    }

    public boolean isRuleApplicable(double value, int noOfDays) {
        if (noOfDays == 0) {
            noOfDays = 1;
        }
        if (Operator == MathOperator.BETWEEN) {
            return (value > this.startValue * noOfDays && value < this.endValue * noOfDays);
        }
        if (Operator == MathOperator.LESS_THAN) {
            return (value < this.startValue * noOfDays);
        }
        if (Operator == MathOperator.GREATER_THAN) {
            return (value > this.startValue * noOfDays);
        }
        if (Operator == MathOperator.LESS_THAN_EQUAL_TO) {
            return (value <= this.startValue * noOfDays);
        }
        if (Operator == MathOperator.GREATER_THAN_EQUAL_TO) {
            return (value >= this.startValue * noOfDays);
        }
        if (Operator == MathOperator.EQUAL_TO) {
            return (value == this.startValue * noOfDays);
        }
        if (Operator == MathOperator.NOT_EQUAL_TO) {
            return (value != this.startValue * noOfDays);
        }

//            if (Operator == MathOperator.BETWEEN) {
//                return (value > this.startValue * 1 && value < this.endValue * 1);
//            }
//            if (Operator == MathOperator.LESS_THAN) {
//                return (value < this.startValue * 1);
//            }
//            if (Operator == MathOperator.GREATER_THAN) {
//                return (value > this.startValue * 1);
//            }
//            if (Operator == MathOperator.LESS_THAN_EQUAL_TO) {
//                return (value <= this.startValue * 1);
//            }
//            if (Operator == MathOperator.GREATER_THAN_EQUAL_TO) {
//                return (value >= this.startValue * 1);
//            }
//            if (Operator == MathOperator.EQUAL_TO) {
//                return (value == this.startValue * 1);
//            }
//            if (Operator == MathOperator.NOT_EQUAL_TO) {
//                return (value != this.startValue * 1);
//            }
        //if operator = <
        //return ( value < this.startValue)
        //if opeator = >
        //return ( value > this.startValue )
        // ==
        // !=
        //<>
        return false;
    }

    public boolean isAvgRuleApplicable(double value, BigDecimal avgVal) {

//        if (noOfDays == 0) {
        if (Operator == MathOperator.BETWEEN) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage > this.startValue * 1 && Percentage < this.endValue * 1);
        }
        if (Operator == MathOperator.LESS_THAN) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage < this.startValue * 1);
        }
        if (Operator == MathOperator.GREATER_THAN) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage > this.startValue * 1);
        }
        if (Operator == MathOperator.LESS_THAN_EQUAL_TO) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage <= this.startValue * 1);
        }
        if (Operator == MathOperator.GREATER_THAN_EQUAL_TO) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage >= this.startValue * 1);
        }
        if (Operator == MathOperator.EQUAL_TO) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage == this.startValue * 1);
        }
        if (Operator == MathOperator.NOT_EQUAL_TO) {
            double Percentage = (value / avgVal.doubleValue()) * 100;
            return (Percentage != this.startValue * 1);
        }
//        } else {
//
//        }

        return false;
    }
    public boolean isPercentRuleApplicable(double value, BigDecimal avgVal) {
//        if (noOfDays == 0) {
          int  noOfDays = 1;
//        }
        if (Operator == MathOperator.BETWEEN) {
            return (value > avgVal.doubleValue() * noOfDays && value < avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.LESS_THAN) {
            return (value < avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.GREATER_THAN) {
            return (value > avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.LESS_THAN_EQUAL_TO) {
            return (value <= avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.GREATER_THAN_EQUAL_TO) {
            return (value >= avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.EQUAL_TO) {
            return (value == avgVal.doubleValue() * noOfDays);
        }
        if (Operator == MathOperator.NOT_EQUAL_TO) {
            return (value != avgVal.doubleValue() * noOfDays);
        }
        return false;
    }
    public String toXMl() {
        StringBuffer colorXml = new StringBuffer("");
        colorXml.append("<ColorCodeRule>");
        if (color != null) {
            colorXml.append("<ColorCode>");
            colorXml.append(color);
            colorXml.append("</ColorCode>");
        }
        if (Operator != null) {
            colorXml.append("<Operator>");
            colorXml.append(Operator.toString());
            colorXml.append("</Operator>");
        }
        if (startValue != null) {
            colorXml.append("<StrtValue>");
//            colorXml.append(startValue*noOfDays);//added by Dinanath
            colorXml.append(startValue);//added by Dinanath
            colorXml.append("</StrtValue>");
        }
        if (viewBy != null) {
            colorXml.append("<viewBy>");
            colorXml.append(viewBy);
            colorXml.append("</viewBy>");
        } else {
            colorXml.append("<viewBy>");
            colorXml.append("");
            colorXml.append("</viewBy>");

        }
        if (endValue != null && endValue != 0.0) {
            colorXml.append("<EndValue>");
//            colorXml.append(endValue*noOfDays);//added by Dinanath
            colorXml.append(endValue);//added by Dinanath
            colorXml.append("</EndValue>");
        }
        colorXml.append("</ColorCodeRule>");

//        System.out.println("colorxml in colorcoderule-----" + colorXml.toString());
        return colorXml.toString();

    }
}
