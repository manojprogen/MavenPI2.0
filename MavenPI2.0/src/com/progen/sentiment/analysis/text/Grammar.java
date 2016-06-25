/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis.text;

/**
 *
 * @author arun
 */
public enum Grammar {

    ADJECTIVE("JJ"),
    ADJECTIVE_COMPARATIVE("JJR"),
    ADJECTIVE_SUPERLATIVE("JJS"),
    ADVERB("RB"),
    ADVERB_COMPARATIVE("RBR"),
    ADVERB_SUPERLATIVE("RBS"),
    ARTILCE("DT"),
    CONJUNCTION_COORDINATING("CC"),
    CONJUNCTION_SUBORDINATING("IN"),
    INTERJECTION("UH"),
    NOUN("NN"),
    NOUN_PLURAL("NNS"),
    PROPER_NOUN("NNP"),
    PROPER_NOUN_PLURAL("NNPS"),
    PERSONAL_PRONOUN("PRP"),
    POSSESIVE_PRONOUN("PRP$"),
    VERB("VB"),
    VERB_PAST_TENSE("VBD"),
    VERB_GERUND("VBG"),
    VERB_PRESENT_TENSE("VBP"),
    VERB_PRESENT_TENSE_SINGULAR("VBZ"),
    WH_DETERMINER("WH"),
    WH_PRONOUN("WP"),
    WH_PRONOUN_POSSESSIVE("WP$"),
    WH_ADVERB("WRB");
    private String tag;

    Grammar(String tag) {
        this.tag = tag;
    }

    public static boolean isTagNoun(String tag) {
        if (NOUN.tag.equalsIgnoreCase(tag)
                || NOUN_PLURAL.tag.equalsIgnoreCase(tag)
                || PROPER_NOUN_PLURAL.tag.equalsIgnoreCase(tag)
                || PROPER_NOUN.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagVerb(String tag) {
        if (VERB.tag.equalsIgnoreCase(tag)
                || VERB_PAST_TENSE.tag.equalsIgnoreCase(tag)
                || VERB_GERUND.tag.equalsIgnoreCase(tag)
                || VERB_PRESENT_TENSE.tag.equalsIgnoreCase(tag)
                || VERB_PRESENT_TENSE_SINGULAR.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagProNoun(String tag) {
        if (PERSONAL_PRONOUN.tag.equalsIgnoreCase(tag)
                || POSSESIVE_PRONOUN.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagAdjective(String tag) {
        if (ADJECTIVE.tag.equalsIgnoreCase(tag)
                || ADJECTIVE_COMPARATIVE.tag.equalsIgnoreCase(tag)
                || ADJECTIVE_SUPERLATIVE.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagAdverb(String tag) {
        if (ADVERB.tag.equalsIgnoreCase(tag)
                || ADVERB_COMPARATIVE.tag.equalsIgnoreCase(tag)
                || ADVERB_SUPERLATIVE.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagConjunction(String tag) {
        if (CONJUNCTION_COORDINATING.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isTagInquisitive(String tag) {
        if (WH_ADVERB.tag.equalsIgnoreCase(tag)
                || WH_PRONOUN.tag.equalsIgnoreCase(tag)
                || WH_DETERMINER.tag.equalsIgnoreCase(tag)
                || WH_PRONOUN_POSSESSIVE.tag.equalsIgnoreCase(tag)) {
            return true;
        } else {
            return false;
        }
    }
}
