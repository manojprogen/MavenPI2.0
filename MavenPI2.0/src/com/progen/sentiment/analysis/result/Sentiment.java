/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis.result;

import com.progen.sentiment.analysis.SentimentCustomAnalysis;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentiment is a bunch of sentence's which have some rating
 *
 * @author arun
 */
public class Sentiment {

    ArrayList<SentimentSentence> sentences;
    ArrayList<SentimentCustomAnalysis> custAnalysisLst;

    public Sentiment() {
        sentences = new ArrayList<SentimentSentence>();
        custAnalysisLst = new ArrayList<SentimentCustomAnalysis>();
    }

    public void addSentence(SentimentSentence sentence) {
        sentences.add(sentence);
    }

    public List<SentimentSentence> getSentences() {
        return sentences;
    }

    public void addCustAnalysis(SentimentCustomAnalysis custAnalysis) {
        custAnalysisLst.add(custAnalysis);
    }

    public ArrayList<SentimentCustomAnalysis> getCustAnalysisLst() {
        return custAnalysisLst;
    }
}



    /**
     * create table prg_sentiment_analysis
(
Analysis_Id Number,
Analysis_Source Varchar2(500),
Analysis_Location Varchar2(500),
keyword_id Number,
keyword Varchar2(400),
keyword_grp_id Number,
keyword_grp_name Varchar2(400),
positive_rating Number,
negative_rating Number,
doubtful_rating Number,
Other_rating Number,
Source_Sentence Varchar2(4000),
Executed_Date Date
);
     */