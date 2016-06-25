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
public class SentimentParameter {

    private int parameterId;
    private String parameter;
    private int wordPos;
    private SentimentConnotation connotation;
    private SentimentBuzzWordSet positive;
    private SentimentBuzzWordSet negative;
    private SentimentBuzzWordSet neutral;
    private SentimentBuzzWordSet question;
    private SentimentBuzzWordSet none;

    public SentimentParameter(String keyWord) {
        this.parameter = keyWord;
        positive = new SentimentBuzzWordSet();
        negative = new SentimentBuzzWordSet();
        neutral = new SentimentBuzzWordSet();
        none = new SentimentBuzzWordSet();
        question = new SentimentBuzzWordSet();
    }

    public SentimentConnotation getConnotation() {
        return connotation;
    }

    public void addBuzzWord(String buzzWord, SentimentConnotation connotation) {
        if (connotation == SentimentConnotation.POSITIVE) {
            positive.addSentimentBuzzWord(buzzWord);
        } else if (connotation == SentimentConnotation.NEGATIVE) {
            negative.addSentimentBuzzWord(buzzWord);
        } else if (connotation == SentimentConnotation.NEUTRAL) {
            neutral.addSentimentBuzzWord(buzzWord);
        } else if (connotation == SentimentConnotation.NONE) {
            none.addSentimentBuzzWord(buzzWord);
        } else if (connotation == SentimentConnotation.INQUISITIVE) {
            question.addSentimentBuzzWord(buzzWord);
        }
    }

    public String getParamter() {
        return parameter;
    }

    void setWordPosition(int wordPos) {
        this.wordPos = wordPos;
    }

    int getWordPosition() {
        return this.wordPos;
    }

    SentimentConnotation isWordABuzz(String buzzWord) {
        if (positive.isWordABuzzWord(buzzWord)) {
            return SentimentConnotation.POSITIVE;
        } else if (negative.isWordABuzzWord(buzzWord)) {
            return SentimentConnotation.NEGATIVE;
        } else if (neutral.isWordABuzzWord(buzzWord)) {
            return SentimentConnotation.NEUTRAL;
        } else {
            return SentimentConnotation.NONE;
        }
    }

    boolean isParameterRated() {
        return (positive.getBuzzWordCount() != 0
                || negative.getBuzzWordCount() != 0
                || neutral.getBuzzWordCount() != 0
                || none.getBuzzWordCount() != 0
                || question.getBuzzWordCount() != 0);
    }

    boolean isParameterRated(SentimentConnotation connotation) {
        if (connotation == SentimentConnotation.POSITIVE) {
            return positive.getBuzzWordCount() != 0;
        }
        if (connotation == SentimentConnotation.NEGATIVE) {
            return negative.getBuzzWordCount() != 0;
        }
        if (connotation == SentimentConnotation.NEUTRAL) {
            return neutral.getBuzzWordCount() != 0;
        }
        if (connotation == SentimentConnotation.INQUISITIVE) {
            return question.getBuzzWordCount() != 0;
        }
        if (connotation == SentimentConnotation.NONE) {
            return none.getBuzzWordCount() != 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SentimentParameter other = (SentimentParameter) obj;
        if ((this.parameter == null) ? (other.parameter != null) : !this.parameter.equals(other.parameter)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.parameter != null ? this.parameter.hashCode() : 0);
        return hash;
    }

    public static Predicate<SentimentParameter> getSentimentParamterPredicate(final String parameter) {
        Predicate<SentimentParameter> predicate = new Predicate<SentimentParameter>() {

            @Override
            public boolean apply(SentimentParameter t) {
                String storedParameter = t.getParamter();
                if (storedParameter.equalsIgnoreCase(parameter.trim()) || parameter.trim().toUpperCase().contains(storedParameter.toUpperCase())) {
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
        return this.parameter;
    }

    /**
     * @return the parameterId
     */
    public int getParameterId() {
        return parameterId;
    }

    /**
     * @param parameterId the parameterId to set
     */
    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    /**
     * @return the positive
     */
    public SentimentBuzzWordSet getPositive() {
        return positive;
    }

    /**
     * @param positive the positive to set
     */
    public void addBuzzwordSet(SentimentBuzzWordSet buzzWordSet, SentimentConnotation connotation) {

        if (connotation == SentimentConnotation.POSITIVE) {
            this.positive = buzzWordSet;
        } else if (connotation == SentimentConnotation.NEGATIVE) {
            this.negative = buzzWordSet;
        } else if (connotation == SentimentConnotation.NEUTRAL) {
            this.neutral = buzzWordSet;
        } else if (connotation == SentimentConnotation.NONE) {
            this.none = buzzWordSet;
        } else if (connotation == SentimentConnotation.INQUISITIVE) {
            this.question = buzzWordSet;
        }
    }

    /**
     * @return the negative
     */
    public SentimentBuzzWordSet getNegative() {
        return negative;
    }

    /**
     * @return the neutral
     */
    public SentimentBuzzWordSet getNeutral() {
        return neutral;
    }

    public String getFirstBuzzWord(SentimentConnotation connotation) {
        switch (connotation) {
            case POSITIVE:
                return positive.getFirstBuzzWord();
            case NEGATIVE:
                return negative.getFirstBuzzWord();
            case NONE:
                return none.getFirstBuzzWord();
            case INQUISITIVE:
                return question.getFirstBuzzWord();
            default:
                return "Other";
        }
    }
}
