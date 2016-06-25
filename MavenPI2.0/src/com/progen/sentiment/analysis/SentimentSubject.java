/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author arun
 */
public class SentimentSubject {

    Set<String> subjectKeys;

    public SentimentSubject() {
        subjectKeys = new HashSet<String>();
    }

    public void addSubject(String subject) {
        subjectKeys.add(subject);
    }

    public boolean isWordASubject(String word) {
        for (String subject : subjectKeys) {
            if (subject.equalsIgnoreCase(word)) {
                return true;
            }
        }
//        if ( subjectKeys.contains(word.toUpperCase()) )
//            return true;
        for (String subject : subjectKeys) {
            if (word.toUpperCase().contains(subject.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isWordPartOfSubject(String word) {
        for (String key : subjectKeys) {
            if (key.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the subjectKeys
     */
    public Set<String> getSubjectKeys() {
        return subjectKeys;
    }

    public boolean isSubjectDriven() {
        return !subjectKeys.isEmpty();
    }

    public String getSubject(String malformedSubject) {
        for (String key : subjectKeys) {
            if (malformedSubject.toUpperCase().contains(key.toUpperCase())) {
                return key;
            }
        }
        return malformedSubject;
    }
}
