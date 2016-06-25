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
public class SentimentBuzzWord {

    private int buzzWordId;
    private String buzzWord;

    public SentimentBuzzWord(String buzzWord) {
        this.buzzWord = buzzWord;
    }

    public String getBuzzWord() {
        return buzzWord;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SentimentBuzzWord other = (SentimentBuzzWord) obj;
        if ((this.buzzWord == null) ? (other.buzzWord != null) : !this.buzzWord.equals(other.buzzWord)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.buzzWord != null ? this.buzzWord.hashCode() : 0);
        return hash;
    }

    public static Predicate<SentimentBuzzWord> getSentimentBuzzWordPredicate(final String buzzWord) {
        Predicate<SentimentBuzzWord> predicate = new Predicate<SentimentBuzzWord>() {

            @Override
            public boolean apply(SentimentBuzzWord t) {
                if (t.getBuzzWord().equalsIgnoreCase(buzzWord.trim())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return predicate;
    }

    @Override
    public String toString() {
        return buzzWord;
    }

    /**
     * @return the buzzWordId
     */
    public int getBuzzWordId() {
        return buzzWordId;
    }

    /**
     * @param buzzWordId the buzzWordId to set
     */
    public void setBuzzWordId(int buzzWordId) {
        this.buzzWordId = buzzWordId;
    }
}
