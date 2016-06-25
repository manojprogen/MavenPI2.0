/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

/**
 *
 * @author arun
 */
public enum SentimentConnotation {

    POSITIVE,
    NEGATIVE,
    NEUTRAL,
    INQUISITIVE,
    NONE;

    public static SentimentConnotation getSentimentConnotation(String connotation) {
        if (connotation.equalsIgnoreCase("POSITIVE")) {
            return POSITIVE;
        } else if (connotation.equalsIgnoreCase("NEGATIVE")) {
            return NEGATIVE;
        } else if (connotation.equalsIgnoreCase("NEUTRAL")) {
            return NEUTRAL;
        } else {
            return NONE;
        }
    }

    public static SentimentConnotation flipConnotation(SentimentConnotation connotation) {
        if (connotation == POSITIVE) {
            return NEGATIVE;
        } else if (connotation == NEGATIVE) {
            return POSITIVE;
        } else {
            return connotation;
        }
    }
}
