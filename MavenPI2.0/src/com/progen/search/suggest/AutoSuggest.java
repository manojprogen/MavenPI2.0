/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search.suggest;

import com.google.gson.annotations.Expose;
import com.progen.search.SearchConstants;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arun
 */
public class AutoSuggest {

    @Expose
    String query;
    @Expose
    ArrayList<String> suggestions;
    @Expose
    ArrayList<String> data;
    Iterable<SearchSuggestion> srchSuggestions;

    public AutoSuggest(String query) {
        this.query = query.trim();
        suggestions = new ArrayList<String>();
        data = new ArrayList<String>();
    }

    public String getQueryString() {
        return this.query;
    }

    public boolean isCompareQuery() {
        Iterable<String> operators = SearchConstants.SearchOperatorConstants.getOperatorList();
        for (String operator : operators) {
            if (query.endsWith(operator)) {
                return true;
            }
        }

        Pattern pattern = Pattern.compile(SearchConstants.SearchOperatorConstants.getOperatorRegexForComparison());
        Matcher matcher = pattern.matcher(query);
        if (matcher.matches()) {
            return true;
        }

        return false;
    }

    public void setSuggestionList(Iterable<SearchSuggestion> srchSuggestions) {
        this.srchSuggestions = srchSuggestions;
        for (SearchSuggestion suggestion : srchSuggestions) {
            suggestions.add(suggestion.getSuggestion());
            if (this.query.equals(suggestion.getSuggestion()) && suggestion.isKeywordAMeasure()) {
                suggestions.addAll(getSuggestLstWithOperator(suggestion.getSuggestion()));
            }
        }
    }

    private ArrayList<String> getSuggestLstWithOperator(String suggestion) {
        ArrayList<String> suggestionWithOper = new ArrayList<String>();
        Iterable<String> operators = SearchConstants.SearchOperatorConstants.getOperatorList();

        for (String operator : operators) {
            suggestionWithOper.add(suggestion + " " + operator);
        }
        return suggestionWithOper;

    }
}
