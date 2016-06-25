/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.base.Predicate;

/**
 *
 * @author arun
 */
public class SentimentAdverb {

    private String adverb;
    private SentimentConnotation connotation;

    public SentimentAdverb(String adverb, SentimentConnotation connotation) {
        this.adverb = adverb;
        this.connotation = connotation;
    }

    public static Predicate<SentimentAdverb> getSentimentAdverbPredicate(final String adverb) {
        Predicate<SentimentAdverb> predicate = new Predicate<SentimentAdverb>() {

            @Override
            public boolean apply(SentimentAdverb t) {
                if (t.getAdverb().equalsIgnoreCase(adverb)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    public String getAdverb() {
        return this.adverb;
    }

    public SentimentConnotation getConnotation() {
        return this.connotation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SentimentAdverb other = (SentimentAdverb) obj;
        if ((this.adverb == null) ? (other.adverb != null) : !this.adverb.equals(other.adverb)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.adverb != null ? this.adverb.hashCode() : 0);
        return hash;
    }
}
