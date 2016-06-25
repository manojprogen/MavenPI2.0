/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.progen.sentiment.analysis.result.Sentiment;
import com.progen.sentiment.analysis.result.SentimentSentence;
import com.progen.sentiment.analysis.text.PoSTagger;
import com.progen.sentiment.analysis.text.TaggedSentence;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author arun
 */
public class SentimentAnalysisBD {

    public static Logger logger = Logger.getLogger(SentimentAnalysisBD.class);

    void parseAndAnalyzeText(BufferedReader reader) {
        try {
            SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
            List<TaggedSentence> sentences = PoSTagger.extractSentences(reader);
            SentimentMetadata metadata = dao.loadSentimentMetadata();
            TextAnalyzer analyzer = new TextAnalyzer(metadata);
            List<SentimentSentence> analyzedSentences = new ArrayList<SentimentSentence>();
            Sentiment sentimentResult = new Sentiment();
            for (TaggedSentence oneSentence : sentences) {
                SentimentSentence ratedSentence = analyzer.analyzeSentence(oneSentence);
                if (!ratedSentence.isJunk()) {
                    sentimentResult.addSentence(ratedSentence);
                }
            }
            dao.insertSentimentAnalysisResults(sentimentResult, "");


        } catch (ClassNotFoundException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "parseAndAnalyzeText", ex.getMessage());
            logger.error("Exception", ex);
        } catch (IOException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "parseAndAnalyzeText", ex.getMessage());
            logger.error("Exception", ex);
        }
    }

    boolean parseAndAnalyzeText(String from) {
        boolean flag = false;
        try {
            SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
            List<TaggedSentence> sentences = new ArrayList<TaggedSentence>();
            logger.info("time1----" + System.currentTimeMillis());
            List<SentimentCustomAnalysis> custAnalysisLst;
            if (!"twitter".equalsIgnoreCase(from)) {
                custAnalysisLst = dao.getCustomAnalysisLst();
            } else {
                custAnalysisLst = dao.getCustomAnalysisLstForTwitter();
            }
            logger.info("date2----" + System.currentTimeMillis());
            for (SentimentCustomAnalysis custAnalysis : custAnalysisLst) {
                sentences.add(PoSTagger.tagSentence(custAnalysis.getAttributeChar3()));
                // sentences.add(PoSTagger.tagSentence("@ airtel_in But your 3G plans are too costly ."));
            }
            logger.info("time2----" + System.currentTimeMillis());
            SentimentMetadata metadata = dao.loadSentimentMetadata();
            TextAnalyzer analyzer = new TextAnalyzer(metadata);
            Sentiment sentimentResult = new Sentiment();
            int i = 0;
            for (TaggedSentence oneSentence : sentences) {
                SentimentSentence ratedSentence = analyzer.analyzeSentence(oneSentence);
                if (!ratedSentence.isJunk()) {
                    sentimentResult.addSentence(ratedSentence);
                    sentimentResult.addCustAnalysis(custAnalysisLst.get(i));
                }
                i++;
            }
            flag = dao.insertSentimentAnalysisResults(sentimentResult, from);


        } catch (ClassNotFoundException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "parseAndAnalyzeText", ex.getMessage());
            logger.error("Exception", ex);
            return false;
        } catch (IOException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "parseAndAnalyzeText", ex.getMessage());
            logger.error("Exception", ex);
            return false;
        }
        return flag;
    }

    public void saveSubjectsAndParameters(Iterable<String> subjectArray, Iterable<String> paramArray) {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        SentimentParameter sentimentParameter;
        SentimentSubject sentimentSubject = new SentimentSubject();;
        for (String param : paramArray) {
            if (!param.trim().equalsIgnoreCase("")) {
                sentimentParameter = new SentimentParameter(param);
                analysisDAO.insertSentimentParameter(sentimentParameter);
            }
        }
        //loop throug the paramArray Iterable
        //create one SentimentParameter class
        //in dao add method insertSentimentParameter which will take argument SentimentParameter
        //Method will check if parameter is there, if there update id
        //else insert and update id
        for (String sub : subjectArray) {
            if (!sub.trim().equalsIgnoreCase("")) {
                sentimentSubject.addSubject(sub.trim());
            }

        }
        analysisDAO.insertSentimentSubjects(sentimentSubject);
    }

    public ArrayList<SentimentParameter> getParameters() {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        ArrayList<SentimentParameter> paramList = new ArrayList<SentimentParameter>();
        paramList = analysisDAO.getParameters();
        return paramList;
    }

    public void saveBuzzwords(Iterable<String> buzzPositiveArray, Iterable<String> buzzNegativeArray, Iterable<String> buzzNeutralArray, int paramId, String parameter) {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        SentimentParameter sentimentParameter = new SentimentParameter(parameter);
        sentimentParameter.setParameterId(paramId);

        SentimentBuzzWordSet positive = new SentimentBuzzWordSet();
        SentimentBuzzWordSet negative = new SentimentBuzzWordSet();
        SentimentBuzzWordSet neutral = new SentimentBuzzWordSet();

        if (buzzPositiveArray != null && buzzPositiveArray.iterator().hasNext()) {
            for (String buzzword : buzzPositiveArray) {
                if (!buzzword.trim().equalsIgnoreCase("")) {
                    positive.addSentimentBuzzWord(buzzword);
                }
            }
        }
        if (buzzNegativeArray != null && buzzNegativeArray.iterator().hasNext()) {
            for (String buzzword : buzzNegativeArray) {
                if (!buzzword.trim().equalsIgnoreCase("")) {
                    negative.addSentimentBuzzWord(buzzword);
                }
            }
        }
        if (buzzNeutralArray != null && buzzNeutralArray.iterator().hasNext()) {
            for (String buzzword : buzzNeutralArray) {
                if (!buzzword.trim().equalsIgnoreCase("")) {
                    neutral.addSentimentBuzzWord(buzzword);
                }
            }
        }
        positive = analysisDAO.insertBuzzwords(positive);
        negative = analysisDAO.insertBuzzwords(negative);
        neutral = analysisDAO.insertBuzzwords(neutral);

        sentimentParameter.addBuzzwordSet(positive, SentimentConnotation.POSITIVE);
        sentimentParameter.addBuzzwordSet(negative, SentimentConnotation.NEGATIVE);
        sentimentParameter.addBuzzwordSet(neutral, SentimentConnotation.NEUTRAL);

        analysisDAO.insertParameterBuzzes(sentimentParameter);
        //setSentimentParameter();

        //create SentimentParameter class set paramName and id
        //create SentimentBuzzWordSet positive
        //pass it to the DAO - In the DAO check if exists and get id or insert and get id. Update id in the SentimentBuzzWord
        //create SentimentBuzzWordSet negative
        //pass it to the DAO - In the DAO check if exists and get id or insert and get id. Update id in the SentimentBuzzWord
        //create SentimentBuzzWordSet neutral
        //pass it to the DAO - In the DAO check if exists and get id or insert and get id. Update id in the SentimentBuzzWord
        //update positive/negative/neutral into SentimentParameter
        //in dao one method where you will pass SentimentParameter
        //method is insert into PRG_SENTIMENT_PARAM_BUZZ

        // analysisDAO.saveBuzzwords(buzzPositiveArray, buzzNegativeArray, buzzNeutralArray, paramId, parameter);

    }

    public String getParamBuzzwords(String parameterId) {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        String buzzWordJson = analysisDAO.getParamBuzzwords(parameterId);
        return buzzWordJson;
    }

    public String getDataForReclassification() {
        SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
        String json = dao.getDataForClassification();
        // 
        return json;
    }

    public void saveKeywords(String parameterId, Iterable<String> keywordArray) {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        for (String keyword : keywordArray) {
            if (!keyword.trim().equalsIgnoreCase("")) {
                analysisDAO.saveKeywords(parameterId, keyword);
            }
        }
    }
}
