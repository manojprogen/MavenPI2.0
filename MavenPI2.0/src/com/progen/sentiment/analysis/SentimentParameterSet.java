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
public class SentimentParameterSet {

    private ArrayList<SentimentParameter> parameters;

    public SentimentParameterSet() {
        this.parameters = new ArrayList<SentimentParameter>();
    }

    public void addSentimentParameter(String word) {
        SentimentParameter paramter = new SentimentParameter(word);
        getParameters().add(paramter);
    }

    public void addBuzzWordForParameter(String word, String buzzWord, SentimentConnotation connotation) {
        SentimentParameter parameter = this.findSentimentParameter(word);
        parameter.addBuzzWord(buzzWord, connotation);
    }

    public void addBuzzWordForParameter(String word, Iterable<String> buzzWords, SentimentConnotation connotation) {
        SentimentParameter parameter = this.findSentimentParameter(word);
        for (String buzzWord : buzzWords) {
            parameter.addBuzzWord(buzzWord, connotation);
        }
    }

    public boolean isWordAParamter(String word) {
        try {
            findSentimentParameter(word);
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }
    }

    private SentimentConnotation isWordABuzz(String paramWord, String buzzWord) {
        try {
            SentimentParameter parameter = findSentimentParameter(paramWord);
            return parameter.isWordABuzz(buzzWord);
        } catch (NoSuchElementException nse) {
            return SentimentConnotation.NONE;
        }
    }

    public SentimentConnotation isWordABuzz(String paramWord, Iterable<String> buzzWords) {
        try {
            SentimentParameter parameter = findSentimentParameter(paramWord);
            SentimentConnotation connotation;
            for (String buzzWord : buzzWords) {
                connotation = isWordABuzz(paramWord, buzzWord);
                if (connotation != SentimentConnotation.NONE) {
                    return connotation;
                } else {
                    continue;
                }
            }
            return SentimentConnotation.NONE;
        } catch (NoSuchElementException nse) {
            return SentimentConnotation.NONE;
        }
    }

    private SentimentParameter findSentimentParameter(String word) throws NoSuchElementException {
        SentimentParameter parameter = Iterables.find(getParameters(), SentimentParameter.getSentimentParamterPredicate(word));
        return parameter;
    }

    /**
     * @return the parameters
     */
    public ArrayList<SentimentParameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(ArrayList<SentimentParameter> parameters) {
        this.parameters = parameters;
    }

    public boolean isParameterAnalysisNeeded() {
        if (parameters.size() == 1) {
            SentimentParameter sentimentParameter = parameters.get(0);
            return !"PARAMETER_NA".equalsIgnoreCase(sentimentParameter.getParamter());
        }
        return true;
    }
}
