/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import com.google.common.base.Function;

/**
 *
 * @author Administrator
 */
public enum MathOperator {

    LESS_THAN("LESS_THAN"),
    GREATER_THAN("GREATER_THAN"),
    EQUAL_TO("EQUAL_TO"),
    NOT_EQUAL_TO("NOT_EQUAL_TO"),
    GREATER_THAN_EQUAL_TO("GREATER_THAN_EQUAL_TO"),
    LESS_THAN_EQUAL_TO("LESS_THAN_EQUAL_TO"),
    BETWEEN("BETWEEN"),
    NONE("");
    private String operator;

    MathOperator(String operator) {
        this.operator = operator;
    }

    String getOperator() {
        return operator;
    }

    public static MathOperator getMathOperator(String operator) {
        if (operator.equals(LESS_THAN.getOperator())) {
            return LESS_THAN;
        } else if (operator.equals(GREATER_THAN.getOperator())) {
            return GREATER_THAN;
        } else if (operator.equals(GREATER_THAN_EQUAL_TO.getOperator())) {
            return GREATER_THAN_EQUAL_TO;
        } else if (operator.equals(LESS_THAN_EQUAL_TO.getOperator())) {
            return LESS_THAN_EQUAL_TO;
        } else if (operator.equals(EQUAL_TO.getOperator())) {
            return EQUAL_TO;
        } else if (operator.equals(NOT_EQUAL_TO.getOperator())) {
            return NOT_EQUAL_TO;
        } else if (operator.equals(BETWEEN.getOperator())) {
            return BETWEEN;
        } //        if ( operator.equals("LESS_THAN") )
        //            return LESS_THAN;
        //        else if ( operator.equals("GREATER_THAN") )
        //            return GREATER_THAN;
        //        else if ( operator.equals("GREATER_THAN_EQUAL_TO") )
        //            return GREATER_THAN_EQUAL_TO;
        //        else if ( operator.equals("LESS_THAN_EQUAL_TO") )
        //            return LESS_THAN_EQUAL_TO;
        //        else if ( operator.equals("EQUAL_TO") )
        //            return EQUAL_TO;
        //        else if ( operator.equals("NOT_EQUAL_TO") )
        //            return NOT_EQUAL_TO;
        //        else if ( operator.equals("BETWEEN") )
        //            return BETWEEN;
        else {
            return NONE;
        }
    }

    public static Function<String, String> convertOperatorString() {
        Function<String, String> function = new Function<String, String>() {

            public String apply(String f) {
                if (f.equals("<")) {
                    return LESS_THAN.getOperator();
                }
                if (f.equals(">")) {
                    return GREATER_THAN.getOperator();
                }
                if (f.equals("<=")) {
                    return LESS_THAN_EQUAL_TO.getOperator();
                }
                if (f.equals(">=")) {
                    return GREATER_THAN_EQUAL_TO.getOperator();
                }
                if (f.equals("=")) {
                    return EQUAL_TO.getOperator();
                }
                if (f.equals("!=")) {
                    return NOT_EQUAL_TO.getOperator();
                }
                if (f.equals("<>")) {
                    return BETWEEN.getOperator();
                } else {
                    return NONE.getOperator();
                }
            }
        };
        return function;
    }
}
