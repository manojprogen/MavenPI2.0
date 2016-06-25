/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

/**
 *
 * @author progen
 */
public class SentimentMetadata {

    SentimentParameterSet sentimentParamSet;
    SentimentSubject sentimentSubject;
    SentimentAdverbSet sentimentAdverbSet;

    public SentimentMetadata(SentimentParameterSet paramSet, SentimentSubject subject, SentimentAdverbSet adverbSet) {
        this.sentimentParamSet = paramSet;
        this.sentimentSubject = subject;
        this.sentimentAdverbSet = adverbSet;
    }

    public boolean isSubjectDriven() {
        return sentimentSubject.isSubjectDriven();
    }

    public boolean isParameterDriven() {
        return sentimentParamSet.isParameterAnalysisNeeded();
    }

    public SentimentParameterSet getParameterSet() {
        return this.sentimentParamSet;
    }

    public SentimentSubject getSentimentSubject() {
        return this.sentimentSubject;
    }

    public SentimentAdverbSet getSentimentAdverbSet() {
        return this.sentimentAdverbSet;
    }
}
