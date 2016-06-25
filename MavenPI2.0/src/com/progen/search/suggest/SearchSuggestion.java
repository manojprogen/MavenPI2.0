/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search.suggest;

/**
 *
 * @author arun
 */
public class SearchSuggestion {

    private String suggestion;
    private String type;

    public String getSuggestion() {
        return suggestion;
    }

    public String getType() {
        return type;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isKeywordAMeasure() {
        if ("Facts".equals(type)) {
            return true;
        } else {
            return false;
        }
    }
}
