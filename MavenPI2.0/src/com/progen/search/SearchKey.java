/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import com.google.common.base.Predicate;

/**
 *
 * @author arun
 */
public class SearchKey {

    private String searchKey;
    private SearchConstants.SearchOperatorConstants operator;
    private Object operand;

    public SearchKey() {
        this.operator = SearchConstants.SearchOperatorConstants.OPERATOR_NOT_INITIALIZED;
    }

    public Object getOperand() {
        return operand;
    }

    public SearchConstants.SearchOperatorConstants getOperator() {
        return operator;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setOperand(Object operand) {
        this.operand = operand;
    }

    public void setOperator(SearchConstants.SearchOperatorConstants operator) {
        this.operator = operator;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public String toString() {
        String enteredText = this.searchKey;
        if (operator != null) {
            enteredText = enteredText + " " + this.operator + " " + this.operand;
        }
        return enteredText;
    }

    public static Predicate<SearchKey> getSearchKeyPredicate(final String searchKey) {
        Predicate<SearchKey> predicate = new Predicate<SearchKey>() {

            @Override
            public boolean apply(SearchKey input) {
                if (input.getSearchKey().equals(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }
}
