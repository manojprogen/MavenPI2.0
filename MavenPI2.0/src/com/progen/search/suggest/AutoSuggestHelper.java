/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search.suggest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author arun
 */
public class AutoSuggestHelper {

    private final AutoSuggest autoSuggest;

    public AutoSuggestHelper(String srchText) {
        autoSuggest = new AutoSuggest(srchText);
    }

    public String buildAutoSuggestJson() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(autoSuggest);
    }

    public boolean isAutoSuggestQryNeeded() {
        return !autoSuggest.isCompareQuery();
    }

    public void setSuggestionList(Iterable<SearchSuggestion> srchSuggestLst) {
        autoSuggest.setSuggestionList(srchSuggestLst);
    }
}
