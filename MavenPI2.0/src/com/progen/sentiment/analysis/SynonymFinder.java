/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import edu.smu.tspell.wordnet.*;
import java.util.*;

/**
 *
 * @author arun
 */
public class SynonymFinder {

    private WordNetDatabase database;
    private static SynonymFinder INSTANCE;

    private SynonymFinder() {

        database = WordNetDatabase.getFileInstance();
    }

    public static SynonymFinder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SynonymFinder();
        }
        return INSTANCE;
    }

    public Iterable<String> findAdjectiveSynonyms(String word) {
        Set<String> synonyms = new HashSet<String>();
        Synset[] synsets = database.getSynsets(word, SynsetType.ADJECTIVE);
        AdjectiveSynset[] relatedSets;

        for (Synset synset : synsets) {
            AdjectiveSynset adjSynSet = (AdjectiveSynset) synset;
            synonyms.addAll(findAdjectiveSynonyms(adjSynSet));
        }


        return synonyms;
    }

    public Iterable<String> findAdverbSynonyms(String word) {
        Set<String> synonyms = new HashSet<String>();
        Synset[] synsets = database.getSynsets(word, SynsetType.ADVERB);
        AdjectiveSynset[] relatedSets;

        for (Synset synset : synsets) {
            AdverbSynset advSynSet = (AdverbSynset) synset;
            String[] wordForms = advSynSet.getWordForms();
            synonyms.addAll(Arrays.asList(wordForms));
        }
        return synonyms;
    }

    private List<String> findAdjectiveSynonyms(AdjectiveSynset adjSynset) {
        ArrayList<String> synonyms = new ArrayList<String>();
        //find similar
        for (AdjectiveSynset similar : adjSynset.getSimilar()) {
            synonyms.addAll(findWordForms(similar));
        }

        //find related
        for (AdjectiveSynset related : adjSynset.getRelated()) {
            synonyms.addAll(findWordForms(related));
        }

        return synonyms;
    }

    private List<String> findWordForms(Synset synset) {
        String[] words = synset.getWordForms();
        return Arrays.asList(words);
    }
}
