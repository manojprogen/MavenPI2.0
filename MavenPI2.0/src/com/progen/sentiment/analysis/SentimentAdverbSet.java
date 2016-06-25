/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author arun
 */
public class SentimentAdverbSet {

    private ArrayList<SentimentAdverb> adverbs;

    public SentimentAdverbSet() {
        this.adverbs = new ArrayList<SentimentAdverb>();
    }

    public void addAdverb(String word, SentimentConnotation connotation) {
        SentimentAdverb adverb = new SentimentAdverb(word, connotation);
        adverbs.add(adverb);
    }

    public SentimentConnotation isWordAnAdverb(Iterable<String> advWords) {
        for (String adverb : advWords) {
            try {
                SentimentAdverb sentiAdverb = this.findSentimentAdverb(adverb);
                return sentiAdverb.getConnotation();
            } catch (NoSuchElementException nse) {
                continue;
            }
        }
        return SentimentConnotation.NONE;
    }

    private SentimentAdverb findSentimentAdverb(String adverb) {
        SentimentAdverb sentiAdverb = Iterables.find(adverbs, SentimentAdverb.getSentimentAdverbPredicate(adverb));
        return sentiAdverb;
    }
}
