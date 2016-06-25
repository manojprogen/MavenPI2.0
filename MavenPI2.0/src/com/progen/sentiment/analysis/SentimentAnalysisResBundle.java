/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class SentimentAnalysisResBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        //metaminda-queries
        {"getSentimentParameter", "select * from PRG_SENTIMENT_PARAMETERS"},
        {"getSentimentSubject", "select SUBJECT_ID,SUBJECT_KEY from PRG_SENTIMENT_SUBJECTS"},
        {"insertSentimentParams", "insert into PRG_SENTIMENT_PARAMETERS(PARAMETER_ID,PARAMETER_NAME) VALUES(PRG_SENTIMENT_PARAMETERS_SEQ.nextval,'&')"},
        {"getSentimentParameterDimension", "select * from PRG_SENTIMENT_PARAMETERS WHERE upper(PARAMETER_NAME) = '&'"},
        {"insertSentimentSubjects", "insert into PRG_SENTIMENT_SUBJECTS(SUBJECT_ID,SUBJECT_KEY) VALUES(PRG_SENTIMENT_SUBJECTS_SEQ.nextval,'&')"},
        {"insertSentimentBuzzwords", "insert into PRG_SENTIMENT_BUZZWORDS(BUZZWORD_ID,BUZZWORD) values(&,'&')"},
        {"getBuzzwordDims", "select BUZZWORD_ID,BUZZWORD from PRG_SENTIMENT_BUZZWORDS"},
        {"getBuzzwordId", "select PRG_SENTIMENT_BUZZWORDS_SEQ.NEXTVAL next from dual"},
        {"insertSentimentParameterBuzz", "insert into PRG_SENTIMENT_PARAMETER_BUZZ(PARAMETER_ID,BUZZWORD_ID,CONNOTATION) values(&,&,'&')"},
        {"getSentimentParameterBuzzs", "select PARAMETER_ID,BUZZWORD_ID,CONNOTATION from PRG_SENTIMENT_PARAMETER_BUZZ"},
        {"getParameterBuzzes", "select c.PARAMETER_NAME, b.BUZZWORD,a.CONNOTATION,c.PARAMETER_ID from PRG_SENTIMENT_PARAMETER_BUZZ a,PRG_SENTIMENT_BUZZWORDS b,PRG_SENTIMENT_PARAMETERS c "
            + " where a.BUZZWORD_ID= b.BUZZWORD_ID and  a.PARAMETER_ID=c.PARAMETER_ID and c.PARAMETER_ID=&"},
        {"getSentimentAdverbs", "select  * from PRG_SENTIMENT_ADVERBS"},
        {"updateSentimentParameter", "update PRG_SENTIMENT_PARAMETERS set PARAMETER_NAME='&' where PARAMETER_ID='&'"},
        {"getParamSynonyms", "select KEYWORD from PRG_SENTIMENT_PARAM_SYNONYMS where PARAMETER_ID='&'"},
        {"insertSentimentParamSynonyms", "insert into PRG_SENTIMENT_PARAM_SYNONYMS(SYNONYM_ID,PARAMETER_ID,KEYWORD) values(PRG_SENTIMENT_SYNONYM_SEQ.nextval,'&','&')"},
        {"getKeywords", "select * from PRG_SENTIMENT_PARAM_SYNONYMS"},
        //

        //dataminda queries
        {"getConnectionId", "select SETUP_NUM_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='SENTIMENT_ROLE_CONNECTION'"},
        {"insertSentimentAnalysis", "insert into PRG_SENTIMENT_ANALYSIS (ANALYSIS_ID,SUBJECT_ID,PARAMETER_ID,POSITIVE_RATING,NEGATIVE_RATING,"
            + "DOUBTFUL_RATING,OTHER_RATING,SENTENCE_ID,EXECUTED_DATE,SENTIMENT_CUST_DIM_ID) values(PRG_SENTIMENT_ANALYSIS_SEQ.nextval,&,&,&,&,&,&,&,SYSDATE,&)"},
        {"getSentimenSubjectDims", "select SUBJECT_ID,SUBJECT from PRG_SENTIMENT_SUBJECT"},
        {"PRG_SENTIMENT_SUBJECT_DIM_SEQ", "select PRG_SENTIMENT_SUBJECT_DIM_SEQ.nextval from dual"},
        {"insertSentimentSentenceDimension", "insert into PRG_SENTIMENT_SENTENCE VALUES(&,'&')"},
        {"PRG_SENTIMENT_SENTENCE_DIM_SEQ", "select PRG_SENTIMENT_SENTENCE_DIM_SEQ.nextval from dual"},
        {"insertSentimentSubjectDim", "insert into PRG_SENTIMENT_SUBJECT(SUBJECT_ID,SUBJECT) values(&,'&')"},
        {"getSentimentParameterDimensionForUser", "select * from PRG_SENTIMENT_PARAMETERS WHERE upper(PARAMETER_NAME) = '&'"},
        {"insertSentimentParameterDimension", "insert into PRG_SENTIMENT_PARAMETERS(PARAMETER_ID,PARAMETER_NAME) VALUES(&,'&')"},
        {"PRG_SENTIMENT_PARAM_DIM_SEQ", "select PRG_SENTIMENT_PARAM_DIM_SEQ.nextval from dual"},
        {"getCustSentimentSeq", "select PRG_CUST_SENTIMENT_DIM_SEQ.nextval from dual"},
        {"insertCustomSentiment", "insert into PRG_CUSTOM_SENTIMENT_DIMENSION(SENTIMENT_CUST_DIM_ID,ATTRIBUTE1,ATTRIBUTE2,ATTRIBUTE3,ATTRIBUTE4,ATTRIBUTE5,ATTRIBUTE6,ATTRIBUTE7,ATTRIBUTE8,ATTRIBUTE9,ATTRIBUTE10,ATTRIBUTE11,ATTRIBUTE12,ATTRIBUTE13,ATTRIBUTE14,ATTRIBUTE15,"
            + "ATTRIBUTE_CHAR1,ATTRIBUTE_CHAR2,ATTRIBUTE_CHAR3,ATTRIBUTE_CHAR4,ATTRIBUTE_CHAR5,ATTRIBUTE_CHAR6,ATTRIBUTE_CHAR7,ATTRIBUTE_CHAR8,ATTRIBUTE_CHAR9,ATTRIBUTE_CHAR10,ATTRIBUTE_CHAR11,ATTRIBUTE_CHAR12,ATTRIBUTE_CHAR13,ATTRIBUTE_CHAR14,ATTRIBUTE_CHAR15,"
            + "ATTRIBUTE_DATE1,ATTRIBUTE_DATE2,ATTRIBUTE_DATE3,ATTRIBUTE_DATE4,ATTRIBUTE_DATE5,ATTRIBUTE_DATE6,ATTRIBUTE_DATE7,ATTRIBUTE_DATE8,ATTRIBUTE_DATE9,ATTRIBUTE_DATE10,ATTRIBUTE_DATE11,ATTRIBUTE_DATE12,ATTRIBUTE_DATE13,ATTRIBUTE_DATE14,ATTRIBUTE_DATE15,SENTENCE_ID) "
            + "values(&,&,&,&,&,&,&,&,&,&,&,&,&,&,&,&,'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&',&,&,&,&,&,&,&,&,&,&,&,&,&,&,&,&)"},
        {"getDataForAnalyze", "select FEEDBACKDATE,FORMCODE,SERNUMBER,SATISFACTION,REASONS,RECOMMENDATION,REASON,SERVICESTARS,UHID,NAME,REGDATE,USEDOURSERVICES,REFERREDBY,UPDATESONHEALTH,EMAILID,LETTER,STATUS,CENTRE from NPS_DATA1 where rownum<1001"},
        {"getTweetsQry", "SELECT TA.USER_ID,TA.TWEET_ID,TA.RETWEET_COUNT,TA.STATUS_COUNT,TU.TWITTER_ID,TU.FOLLOWER_COUNT,"
            + "TU.FOLLOWING_COUNT, TU.USER_NAME_ID, TU.USER_NAME, T.TWEET_TEXT ,TA.LOCATION, T.TWEET_DATE FROM PRG_TWEET_ANALYSIS TA, "
            + "PRG_TWITTER_USERS TU ,PRG_TWEETS T WHERE TA.TWEET_ID=T.TWEET_ID AND TU.USER_ID =TA.USER_ID"},
        {"insertCustomSentimentForTwitter", "insert into PRG_CUSTOM_SENTIMENT_DIMENSION(SENTIMENT_CUST_DIM_ID,ATTRIBUTE1,ATTRIBUTE2,ATTRIBUTE3,ATTRIBUTE4,ATTRIBUTE5,"
            + "ATTRIBUTE6,ATTRIBUTE7,ATTRIBUTE_CHAR1,ATTRIBUTE_CHAR2,ATTRIBUTE_CHAR3,ATTRIBUTE_CHAR4,ATTRIBUTE_DATE1) values(&,&,&,&,&,&,&,&,'&','&','&','&','&')"},
        {"reclassificationQry", "select SENTENCE_ID,SENTENCE from PRG_SENTIMENT_SENTENCE where rownum<101"},
        {"reclassifySentenceQry", "SELECT POSITIVE_RATING,NEGATIVE_RATING,OTHER_RATING,a.PARAMETER_NAME,c.ATTRIBUTE5,c.ATTRIBUTE_CHAR1,c.ATTRIBUTE_CHAR2,c.ATTRIBUTE_CHAR4,c.ATTRIBUTE_CHAR5,c.ATTRIBUTE_CHAR6 FROM "
            + "prg_sentiment_parameters a ,prg_sentiment_analysis b ,PRG_CUSTOM_SENTIMENT_DIMENSION c where sentence_id=& and a.parameter_id=b.parameter_id"
            + " and c.sentiment_cust_dim_id=b.sentiment_cust_dim_id "},
        {"insertReclassifiedData", "insert into PRG_RECLASSIFIED_DATA(SENTENCE_ID,POSITIVE_RATING,NEGATIVE_RATING,OTHER_RATING,PARAMETER_NAME)"
            + " values(&,&,&,&,'&')"},
        {"classifiedQry", "select SENTENCE_ID,POSITIVE_RATING,NEGATIVE_RATING,OTHER_RATING,PARAMETER_NAME from PRG_RECLASSIFIED_DATA where SENTENCE_ID=&"},
        {"deleteReclassifiedData", "delete from PRG_RECLASSIFIED_DATA where SENTENCE_ID=&"}
    //
    };
}
