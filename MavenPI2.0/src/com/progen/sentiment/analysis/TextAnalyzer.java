/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.collect.ArrayListMultimap;
import com.progen.sentiment.analysis.result.SentimentSentence;
import com.progen.sentiment.analysis.text.Grammar;
import com.progen.sentiment.analysis.text.TaggedSentence;
import edu.stanford.nlp.ling.TaggedWord;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class TextAnalyzer {

    public static Logger logger = Logger.getLogger(TextAnalyzer.class);
    SentimentParameterSet parameters;
    SentimentSubject subject;
    boolean isSubjectDriven;
    SentimentAdverbSet adverbs;
    String previousSentenceSubject = null;
    boolean isSentenceSplitted = false;
    boolean isParameterDriven;

    public TextAnalyzer(SentimentMetadata metadata)//SentimentSubject subject,SentimentParameterSet parameters,SentimentAdverbSet adverbs)
    {
        this.subject = metadata.getSentimentSubject();
        this.parameters = metadata.getParameterSet();
        this.adverbs = metadata.getSentimentAdverbSet();
        isSubjectDriven = metadata.isSubjectDriven();
        isParameterDriven = metadata.isParameterDriven();
    }

    void setPreviousSentenceSubject(String subject) {
        this.previousSentenceSubject = subject;
    }

    public ArrayListMultimap<String, String> suggestKeyWordsInSetenence(TaggedSentence sentence) {
        ArrayListMultimap<String, String> keyWords = ArrayListMultimap.create();
        for (TaggedWord word : sentence.getTaggedWords()) {
            if (Grammar.isTagNoun(word.tag())) {
                if (isSubjectDriven && !subject.isWordASubject(word.word())) {
                    keyWords.put("parameter", word.word());
                } else {
                    keyWords.put("parameter", word.word());
                }
            }

            if (Grammar.isTagAdjective(word.tag())) {
                keyWords.put("buzzword", word.word());
            }
        }
        return keyWords;
    }

    public SentimentSentence analyzeSentence(TaggedSentence sentence) {
        SentimentSentence sentimentSentence = new SentimentSentence(sentence.getSourceSentence());
        ArrayList<TaggedSentence> sentenceParts = splitSentenceByConjunction(sentence);
        ArrayList<SentimentParameter> paramsInSentenceLst = null;
        ArrayList<TaggedWord> paramsList = new ArrayList<TaggedWord>();
        SentimentConnotation connotation = SentimentConnotation.NONE;
        String buzzWord = null;
        ArrayList<SentimentParameter> unratedParameters = new ArrayList<SentimentParameter>();
        ArrayList<SentimentParameter> ratedParameters = new ArrayList<SentimentParameter>();
        if (sentenceParts.isEmpty()) {
            isSentenceSplitted = false;
        } else {
            isSentenceSplitted = true;
        }
        try {
            for (TaggedSentence onePart : sentenceParts) {
                paramsInSentenceLst = this.updateSentimentForSentence(sentimentSentence, onePart);

                if (!isSentenceSplitted) {
                    ratedParameters.addAll(paramsInSentenceLst);
                } else {
                    for (SentimentParameter parameter : paramsInSentenceLst) {
                        if (!parameter.isParameterRated()) {
                            unratedParameters.add(parameter);
                        } else {
                            ratedParameters.add(parameter);
                        }
                    }
                    if (!unratedParameters.isEmpty()) {
                        if (!ratedParameters.isEmpty()) {
                            SentimentParameter ratedParameter = ratedParameters.get(0);
                            connotation = ratedParameter.isParameterRated(SentimentConnotation.POSITIVE) ? SentimentConnotation.POSITIVE : SentimentConnotation.NONE;
                            if (connotation == SentimentConnotation.NONE) {
                                connotation = ratedParameter.isParameterRated(SentimentConnotation.NEGATIVE) ? SentimentConnotation.NEGATIVE : SentimentConnotation.NONE;
                            }
                            buzzWord = ratedParameter.getFirstBuzzWord(connotation);
                            for (SentimentParameter parameter : unratedParameters) {
                                parameter.addBuzzWord(buzzWord, connotation);
                            }
                            ratedParameters.addAll(unratedParameters);
                            unratedParameters.clear();
                        }

                    }
                }
            }
            //sentence is splitted but we couldn't get any parameter or buzz
            if (!unratedParameters.isEmpty()) {
                for (SentimentParameter unrated : unratedParameters) {
                    unrated.addBuzzWord("Other", SentimentConnotation.NONE);
                }
                ratedParameters.addAll(unratedParameters);
            }


            sentimentSentence.addRatings(ratedParameters);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return sentimentSentence;
    }

    private ArrayList<SentimentParameter> updateSentimentForSentence(SentimentSentence sentence, TaggedSentence taggedSentence) {
        List<TaggedWord> taggedWords = taggedSentence.getTaggedWords();
        ArrayList<SentimentParameter> ratedParameters = new ArrayList<SentimentParameter>();
        boolean analyzeSentence = false;
        String subjectInSentence = null;

        if (isSubjectDriven) {
            subjectInSentence = getSubjectInSentence(taggedWords);
            if (subjectInSentence != null) {
                analyzeSentence = true;
            }
        } else {
            analyzeSentence = true;
        }
        if (analyzeSentence) {
            ratedParameters = extractParamterBuzzInSentence(taggedWords);
            //sentence.addRatings(ratedParameters);
            sentence.setSubject(subjectInSentence);
        }
        return ratedParameters;
    }

    private String getSubjectInSentence(List<TaggedWord> sentence) {
        for (TaggedWord word : sentence) {
            if (Grammar.isTagNoun(word.tag()) && subject.isWordASubject(word.word())) {
                String correctedSubjectWord = subject.getSubject(word.word());
                previousSentenceSubject = correctedSubjectWord;
                return correctedSubjectWord;
            } else if (Grammar.isTagProNoun(word.tag())) {
                if (previousSentenceSubject != null) {
                    return previousSentenceSubject;
                }
            }
        }
        if (isSentenceSplitted) {
            return previousSentenceSubject;
        } else {
            //can't find anything in sentence
            previousSentenceSubject = null;
            return null;
        }
    }

    private ArrayList<SentimentParameter> extractParamterBuzzInSentence(List<TaggedWord> sentence) {
        ArrayList<SentimentParameter> parameterRatings = new ArrayList<SentimentParameter>();
        SentimentParameter parameter;
        int startPos = 0;

        TaggedWord taggedWord;
        if (isParameterDriven) {
            for (int i = 0; i < sentence.size(); i++) {
                taggedWord = sentence.get(i);
                if (isWordAParameter(taggedWord)) {
                    parameter = new SentimentParameter(taggedWord.word());
                    parameter.setWordPosition(i);
                    updateBuzzInParameter(startPos, parameter, sentence);
                    if (!parameter.isParameterRated() && !isSentenceSplitted) {
                        parameter.addBuzzWord("Other", SentimentConnotation.NONE);
                    }
                    parameterRatings.add(parameter);
                }
            }
        } else {
            parameter = new SentimentParameter("PARAMETER_NA");
            updateBuzzInParameter(0, parameter, sentence);
            parameterRatings.add(parameter);
        }
        if (parameterRatings.isEmpty()) {
            parameter = new SentimentParameter("PARAMETER_NA");
            //parameter.addBuzzWord("Other", SentimentConnotation.NONE);
            updateBuzzInParameter(0, parameter, sentence);
            if (!parameter.isParameterRated()) {
                parameter.addBuzzWord("Other", SentimentConnotation.NONE);
            }
            parameterRatings.add(parameter);
        }
        return parameterRatings;
    }

    private void updateBuzzInParameter(int startFrom, SentimentParameter parameter, List<TaggedWord> sentence) {
        int wordPos = parameter.getWordPosition();
        TaggedWord taggedWord = null;
        int paramPos = -1;
        SentimentConnotation adverbConnotation = SentimentConnotation.NONE;
        SentimentConnotation parameterConnotation = SentimentConnotation.NONE;


        //first check if its a question
        for (int i = startFrom; i < sentence.size(); i++) {
            taggedWord = sentence.get(i);
            if (isWordAQuestion(taggedWord)) {
                parameter.addBuzzWord(taggedWord.word(), SentimentConnotation.INQUISITIVE);
                return;
            }
        }

        for (int i = startFrom; i < sentence.size(); i++) {
            taggedWord = sentence.get(i);
            parameterConnotation = isWordABuzzForParameter(parameter, taggedWord);
            if (parameterConnotation != SentimentConnotation.NONE) {
                paramPos = i;
                //parameter.addBuzzWord(taggedWord.word(), connotation);
                break;
            }
        }

        //found some parameter buzz
        if (paramPos != -1) {
            String parmeterWordInSentence = taggedWord.word();
            //check if an adverb is changing the meaning of the sentence
            for (int i = startFrom; i < paramPos; i++) {
                taggedWord = sentence.get(i);
                adverbConnotation = isWordAnAdverb(taggedWord);
                if (adverbConnotation != SentimentConnotation.NONE) {
                    break;
                }
            }
            if (adverbConnotation != SentimentConnotation.NONE) {
                parameterConnotation = SentimentConnotation.flipConnotation(parameterConnotation);
            }

            parameter.addBuzzWord(parmeterWordInSentence, parameterConnotation);
        }
        //haven't found any parameter check if adverb is present
        //like Active hardly offers any Mileage
    }

    private boolean isWordAParameter(TaggedWord taggedWord) {
        String tag = taggedWord.tag();
        String word = taggedWord.word();
        //if ( Grammar.isTagNoun(tag) )
        if (parameters.isWordAParamter(word)) {
            return true;
        } else {
            return false;
        }
//        else
//            return false;
    }

    private SentimentConnotation isWordABuzzForParameter(SentimentParameter parameter, TaggedWord taggedWord) {
        String tag = taggedWord.tag();
        String word = taggedWord.word();
        HashSet<String> buzzWords = new HashSet<String>();
        buzzWords.add(word);
        if (Grammar.isTagAdjective(tag)) {
            for (String synonym : SynonymFinder.getInstance().findAdjectiveSynonyms(word)) {
                buzzWords.add(synonym);
            }
            return parameters.isWordABuzz(parameter.getParamter(), buzzWords);
        } else {
            return SentimentConnotation.NONE;
        }
    }

    private boolean isWordAQuestion(TaggedWord taggedWord) {
        String tag = taggedWord.tag();
        if (Grammar.isTagInquisitive(tag) || taggedWord.word().endsWith("?")) {
            return true;
        } else {
            return false;
        }
    }

    private SentimentConnotation isWordAnAdverb(TaggedWord taggedWord) {
        String tag = taggedWord.tag();
        String word = taggedWord.word();
        HashSet<String> advWords = new HashSet<String>();
        advWords.add(word);
        if (Grammar.isTagAdverb(tag)) {
            for (String synonym : SynonymFinder.getInstance().findAdverbSynonyms(word)) {
                advWords.add(synonym);
            }
            return adverbs.isWordAnAdverb(advWords);
        } else {
            return SentimentConnotation.NONE;
        }
    }

    private ArrayList<TaggedSentence> splitSentenceByConjunction(TaggedSentence sentence) {
        ArrayList<TaggedSentence> splittedSentence = new ArrayList<TaggedSentence>();
        ArrayList<ArrayList<TaggedWord>> sentenceParts = this.splitSentenceByConjunctionToWords(sentence);
        if (sentenceParts.isEmpty()) {
            splittedSentence.add(sentence);
        } else {
            TaggedSentence sentencePart;
            for (ArrayList<TaggedWord> onePart : sentenceParts) {
                sentencePart = new TaggedSentence(onePart);
                splittedSentence.add(sentencePart);
            }
        }
        return splittedSentence;
    }

    private ArrayList<ArrayList<TaggedWord>> splitSentenceByConjunctionToWords(TaggedSentence sentence) {
        ArrayList<ArrayList<TaggedWord>> sentenceParts = new ArrayList<ArrayList<TaggedWord>>();
        List<TaggedWord> taggedWords = sentence.getTaggedWords();
        int pos = getConjunctionPositionInSentence(taggedWords);
        if (pos != -1) {
            ArrayList<TaggedWord> onePart = new ArrayList<TaggedWord>();
            onePart.addAll(taggedWords.subList(0, pos));
            sentenceParts.add(onePart);
            onePart = new ArrayList<TaggedWord>();
            onePart.addAll(taggedWords.subList(pos + 1, taggedWords.size()));
            sentenceParts.add(onePart);
        }
        return sentenceParts;
    }

    private int getConjunctionPositionInSentence(List<TaggedWord> wordsInSentence) {
        for (int i = 0; i < wordsInSentence.size(); i++) {
            TaggedWord word = wordsInSentence.get(i);
            if (Grammar.isTagConjunction(word.tag())) {
                return i;
            }
        }
        return -1;
    }
}
