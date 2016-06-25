/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis.result;

import com.progen.sentiment.analysis.SentimentParameter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arun
 */
public class SentimentSentence {

    private String sentence;
    private ArrayList<SentimentParameter> sentenceParameters;
    private String subject;

    public SentimentSentence(String sentence) {
        this.sentence = sentence;
        this.sentenceParameters = new ArrayList<SentimentParameter>();
    }

    public void addRatings(List<SentimentParameter> ratings) {
        sentenceParameters.addAll(ratings);
    }

    public boolean isJunk() {
        return sentenceParameters.isEmpty();
    }

    public List<String> getParameterKeyWords() {
        ArrayList<String> parameterKeyWords = new ArrayList<String>();
        for (SentimentParameter parameter : sentenceParameters) {
            parameterKeyWords.add(parameter.getParamter());
        }
        return parameterKeyWords;
    }

    public ArrayList<SentimentParameter> getSentenceParameters() {
        return this.sentenceParameters;
    }

    public String getSentence() {
        return this.sentence;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }
}