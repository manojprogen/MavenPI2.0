/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author arun
 */
public class SentimentNounSet {

    ArrayList<String> nouns;

    public SentimentNounSet() {
        nouns = new ArrayList<String>();
    }

    public void addNoun(String noun) {
        this.nouns.add(noun);
    }

    public boolean isWordAnalyzed(String noun) {
        try {
            Iterables.find(nouns, this.getWordPredicate(noun));
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }

    }

    private Predicate<String> getWordPredicate(final String word) {
        Predicate<String> predicate = new Predicate<String>() {

            @Override
            public boolean apply(String t) {
                if (word.trim().equalsIgnoreCase(t)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }
}
