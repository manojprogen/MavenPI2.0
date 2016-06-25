/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis.text;

//import com.progen.log.ProgenLog;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class PoSTagger {

    public static Logger logger = Logger.getLogger(PoSTagger.class);
    static MaxentTagger tagger;

    static {
        try {
            tagger = new MaxentTagger(System.getProperty("Tagger.dir"));
        } catch (IOException ex) {
            tagger = null;
//            ProgenLog.log(ProgenLog.SEVERE, PoSTagger.class, "static init", ex.getMessage());
            logger.error("static init", ex);
        } catch (ClassNotFoundException ex) {
            tagger = null;
            //            ProgenLog.log(ProgenLog.SEVERE, PoSTagger.class, "static init", ex.getMessage());
            logger.error("static init", ex);
        }
    }

    public static TaggedSentence tagSentence(String input) throws IOException, ClassNotFoundException {
        List<ArrayList<? extends HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(input));//(new FileReader("/home/arun/work/short.txt")));
        // MaxentTagger tagger = new MaxentTagger("/home/progen/left3words-wsj-0-18.tagger");        
        ArrayList<TaggedWord> taggedSentence = new ArrayList<TaggedWord>();
        for (ArrayList<? extends HasWord> sentence : sentences) {
            taggedSentence = tagger.tagSentence(sentence);
        }
        TaggedSentence sentence = new TaggedSentence(taggedSentence);
        return sentence;
    }

    public static List<TaggedSentence> extractSentences(BufferedReader reader) throws ClassNotFoundException, IOException {
        ArrayList<TaggedSentence> taggedSentences = new ArrayList<TaggedSentence>();
        //MaxentTagger tagger = new MaxentTagger("/home/arun/work/wordnet/stanford-postagger-2010-05-26/models/left3words-wsj-0-18.tagger");
        //MaxentTagger tagger = new MaxentTagger("/home/progen/left3words-wsj-0-18.tagger");        
        @SuppressWarnings("unchecked")
        List<ArrayList<? extends HasWord>> sentences = tagger.tokenizeText(reader);
        ArrayList<TaggedWord> taggedWordsInSentence = new ArrayList<TaggedWord>();
        TaggedSentence oneTaggedSentence;

        for (ArrayList<? extends HasWord> sentence : sentences) {
            taggedWordsInSentence = tagger.tagSentence(sentence);
            oneTaggedSentence = new TaggedSentence(taggedWordsInSentence);
            taggedSentences.add(oneTaggedSentence);
        }
        return taggedSentences;
    }
}
