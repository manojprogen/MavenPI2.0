/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 *
 * @author arun
 */
public enum SearchConstants {

    FAVORITE_ALREADY_EXISTS("SRCH_FAV_ALREADY_EXISTS", "Favorite Search Name already exists"),
    FAVORITE_SAVED_FAILED("SRCH_FAV_SAVE_FAIL", "Save of Favorite Search failed. Contact System Administrator."),
    FAVORITE_SAVED_SUCCESSFULLY("SRCH_FAV_SAVE_SUCC", "Search saved as Favorite");
    private String code;
    private String message;

    SearchConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getErrorCode() {
        return this.code;
    }

    public String getMessageCode() {
        return this.message;
    }

    public enum SearchOperatorConstants {

        OPERATOR_GREATER_THAN(">", "GT", ">"),
        OPERATOR_LESS_THAN("<", "LT", "<"),
        OPERATOR_GREATER_THAN_EQUAL_TO(">=", "GE", ">="),
        OPERATOR_LESS_THAN_EQUAL_TO("<=", "LE", "<="),
        OPERATOR_NOT_EQUAL_TO("!=", "NE", "!="),
        OPERATOR_EQUAL_TO("=", "EQ", "[^><!]="),
        OPERATOR_BOTTOM("BTM", "BTM", "BTM"),
        OPERATOR_TOP("TOP", "TOP", "TOP"),
        OPERATOR_NOT_INITIALIZED("OPERATOR_NOT_INITIALIZED", "OPERATOR_NOT_INITIALIZED", "OPERATOR_NOT_INITIALIZED");
        private String operator;
        private String operatorCode;
        private String operatorRegexPattern;

        SearchOperatorConstants(String operator, String operatorCode, String operatorRegexPattern) {
            this.operator = operator;
            this.operatorCode = operatorCode;
            this.operatorRegexPattern = operatorRegexPattern;
        }

        public String getOperator() {
            return this.operator;
        }

        public String getOperatorCode() {
            return this.operatorCode;
        }

        public String getOperatorRegexPattern() {
            return this.operatorRegexPattern;
        }

        public static SearchOperatorConstants getOperatorEnum(String operator) {
            if (operator.equals(OPERATOR_GREATER_THAN.getOperator())) {
                return OPERATOR_GREATER_THAN;
            } else if (operator.equals(OPERATOR_LESS_THAN.getOperator())) {
                return OPERATOR_LESS_THAN;
            } else if (operator.equals(OPERATOR_GREATER_THAN_EQUAL_TO.getOperator())) {
                return OPERATOR_GREATER_THAN_EQUAL_TO;
            } else if (operator.equals(OPERATOR_LESS_THAN_EQUAL_TO.getOperator())) {
                return OPERATOR_LESS_THAN_EQUAL_TO;
            } else if (operator.equals(OPERATOR_NOT_EQUAL_TO.getOperator())) {
                return OPERATOR_NOT_EQUAL_TO;
            } else if (operator.equals(OPERATOR_EQUAL_TO.getOperator())) {
                return OPERATOR_EQUAL_TO;
            } else if (operator.equalsIgnoreCase(OPERATOR_BOTTOM.getOperator())) {
                return OPERATOR_BOTTOM;
            } else if (operator.equalsIgnoreCase(OPERATOR_TOP.getOperator())) {
                return OPERATOR_TOP;
            } else {
                return OPERATOR_NOT_INITIALIZED;
            }
        }

        public static String getOperatorRegex() {
            String regex = null;
            EnumSet<SearchOperatorConstants> operatorSet = EnumSet.allOf(SearchOperatorConstants.class);

            for (SearchOperatorConstants operator : operatorSet) {
                if (operator != SearchOperatorConstants.OPERATOR_NOT_INITIALIZED) {
                    if (regex == null) {
                        regex = "(";
                    } else {
                        regex = regex + "|";
                    }
                    regex = regex + operator.getOperatorRegexPattern();
                }
            }
            regex = regex + ")";
            return regex;
        }

        public static String getOperatorRegexForComparison() {
            String regex = null;
            regex = getOperatorRegex();
            return ".+" + regex + ".+";
        }

        public static Iterable<String> getOperatorList() {
            EnumSet<SearchOperatorConstants> operatorSet = EnumSet.allOf(SearchOperatorConstants.class);
            ArrayList<String> operatorLst = new ArrayList<String>();
            for (SearchOperatorConstants operator : operatorSet) {
                if (operator != SearchOperatorConstants.OPERATOR_NOT_INITIALIZED) {
                    operatorLst.add(operator.getOperator());
                }
            }
            return operatorLst;
        }
    }
}
