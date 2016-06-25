/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.collect.Iterables;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 *
 * @author arun
 */
public class SentimentBuzzWordSet {

    private HashSet<SentimentBuzzWord> buzzWords;

    public SentimentBuzzWordSet() {
        this.buzzWords = new HashSet<SentimentBuzzWord>();
    }

    public void addSentimentBuzzWord(String word) {
        SentimentBuzzWord buzzWord = new SentimentBuzzWord(word);
        buzzWords.add(buzzWord);
    }

    public void addSentimentBuzzWord(String word, int buzzWordId) {
        SentimentBuzzWord buzzWord = new SentimentBuzzWord(word);
        buzzWord.setBuzzWordId(buzzWordId);
        getBuzzWords().add(buzzWord);
    }

    public String getFirstBuzzWord() {
        for (SentimentBuzzWord sentimentBuzz : buzzWords) {
            return sentimentBuzz.getBuzzWord();
        }
        return "Other";
    }

    public boolean isWordABuzzWord(String word) {
        try {
            SentimentBuzzWord buzzWord = Iterables.find(getBuzzWords(), SentimentBuzzWord.getSentimentBuzzWordPredicate(word));
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }
    }

    public int getBuzzWordCount() {
        return getBuzzWords().size();
    }

    @Override
    public String toString() {
        String toString = "";
        for (SentimentBuzzWord buzz : getBuzzWords()) {
            toString += (buzz.getBuzzWord() + "   ");
        }
        return toString;
    }

    public HashSet<SentimentBuzzWord> getBuzzWords() {
        return buzzWords;
    }

    public void setBuzzWords(HashSet<SentimentBuzzWord> buzzWords) {
        this.buzzWords = buzzWords;
    }
}
