/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis.text;

import edu.stanford.nlp.ling.TaggedWord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arun
 */
public class TaggedSentence {

    ArrayList<TaggedWord> taggedWords;

    public TaggedSentence(ArrayList<TaggedWord> taggedSentence) {
        this.taggedWords = taggedSentence;
    }

    public List<TaggedWord> getTaggedWords() {
        return this.taggedWords;
    }

    public String getSourceSentence() {
        StringBuilder sentence = new StringBuilder();
        for (TaggedWord aWord : taggedWords) {
            sentence.append(aWord.word()).append(" ");
        }
        return sentence.toString();
    }
}
