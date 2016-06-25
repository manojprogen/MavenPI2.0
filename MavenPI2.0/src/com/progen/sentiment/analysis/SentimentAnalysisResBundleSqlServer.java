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
public class SentimentAnalysisResBundleSqlServer extends ListResourceBundle implements Serializable {

    @ Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"insertSentimentBuzzWrd", "insert into prg_sentiment_buzzwords (BUZZWORD_ID,BUZZWORD,BUZZ_CATEGORY_ID) values(&,'&',&)"},
        {"insertSentimentBuzzWrdCatg", "insert into prg_sentiment_buzzword_categ(BUZZWORD_CATEG_ID,BUZZWORD_CATEGORY,BUZZWORD_CONNOTATION) values (&,'&','&') "},
        {"getSentimnetBuzzwrdCateg", "select BUZZWORD_CATEGORY,BUZZWORD_CONNOTATION from prg_sentiment_buzzword_categ where BUZZWORD_CATEG_ID=&"},
        {"getSentimentSubject", "select * from PRG_SENTIMENT_SUBJECTS"},
        {"getSentimentParameter", "select * from PRG_SENTIMENT_PARAMETERS"},
        {"insertSentimentAnalysis", "insert into PRG_SENTIMENT_ANALYSIS (SUBJECT_ID,PARAMETER_ID,POSITIVE_RATING,NEGATIVE_RATING,"
            + "DOUBTFUL_RATING,OTHER_RATING,SOURCE_SENTENCE,EXECUTED_DATE) values('&','&',&,&,&,&,'&',getdate())"},
        {"getConnectionId", "select SETUP_NUM_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='SENTIMENT_ROLE_CONNECTION'"},
        {"getParameterBuzzes", "select c.PARAMETER_NAME, b.BUZZWORD,a.CONNOTATION,c.PARAMETER_ID from PRG_SENTIMENT_PARAMETER_BUZZ a,PRG_SENTIMENT_BUZZWORDS b,PRG_SENTIMENT_PARAMETERS c "
            + " where a.BUZZWORD_ID= b.BUZZWORD_ID and  a.PARAMETER_ID=c.PARAMETER_ID and c.PARAMETER_ID=&"},
        {"getSentimentAdverbs", "select  * from PRG_SENTIMENT_ADVERBS"},
        {"insertSentimentSentenceDimension", "insert into PRG_SENTIMENT_SENTENCE VALUES('&')"},
        {"PRG_SENTIMENT_SENTENCE_DIM_SEQ", "select PRG_SENTIMENT_SENTENCE_DIM_SEQ.nextval from dual"},
        {"getParameterId", "select ident_current('PRG_SENTIMENT_PARAMETERS')"},
        {"insertSentimentParameterDimension", "insert into PRG_SENTIMENT_PARAMETERS(PARAMETER_NAME) VALUES('&')"},
        {"getSentimentParameterDimension", "select * from PRG_SENTIMENT_PARAMETERS WHERE upper(PARAMETER_NAME) = '&'"},
        {"insertSentimentParams", "insert into PRG_SENTIMENT_PARAMETERS(PARAMETER_NAME) VALUES('&')"},
        {"insertSentimentSubjects", "insert into PRG_SENTIMENT_SUBJECTS(SUBJECT_KEY) VALUES('&')"},
        {"insertSentimentBuzzwords", "insert into PRG_SENTIMENT_BUZZWORDS(BUZZWORD) values('&')"},
        {"insertSentimentParameterBuzz", "insert into PRG_SENTIMENT_PARAMETER_BUZZ(PARAMETER_ID,BUZZWORD_ID,CONNOTATION) values(&,&,'&')"},
        {"getBuzzwordName", "select BUZZWORD from PRG_SENTIMENT_BUZZWORDS"},
        {"getBuzzwordId", "select BUZZWORD_ID from PRG_SENTIMENT_BUZZWORDS where Upper(BUZZWORD)='&'"},
        {"getSentimentParameterBuzzs", "select PARAMETER_ID,BUZZWORD_ID,CONNOTATION from PRG_SENTIMENT_PARAMETER_BUZZ"},
        {"getSubjectId", "select SUBJECT_ID  from PRG_SENTIMENT_SUBJECT where SUBJECT='&'"},
        {"insertSentimentSubject", "insert into PRG_SENTIMENT_SUBJECT(SUBJECT) values('&')"},
        {"getSentimenSubjectDims", "select SUBJECT_ID,SUBJECT from PRG_SENTIMENT_SUBJECT"},
        {"getSentenceId", "select ident_current('PRG_SENTIMENT_SENTENCE')"},
        {"insertCustomSentiment", "insert into PRG_CUSTOM_SENTIMENT_DIMENSION(ATTRIBUTE_DATE1,ATTRIBUTE_CHAR1,ATTRIBUTE1,ATTRIBUTE_CHAR2,ATTRIBUTE_DATE2,ATTRIBUTE_CHAR3,ATTRIBUTE_CHAR4,ATTRIBUTE_CHAR5,ATTRIBUTE_CHAR6,ATTRIBUTE_CHAR7,ATTRIBUTE_CHAR8,ATTRIBUTE_CHAR9,ATTRIBUTE_CHAR10,ATTRIBUTE_CHAR11) "
            + "values('&','&',&,'&','&','&','&','&','&','&','&','&','&','&')"},
        {"getCustDimId", "select ident_current('PRG_CUSTOM_SENTIMENT_DIMENSION')"},
        {"updateSentimentParameter", "update PRG_SENTIMENT_PARAMETERS set PARAMETER_NAME='&' where PARAMETER_ID='&'"},
        {"getParamSynonyms", "select KEYWORD from PRG_SENTIMENT_PARAM_SYNONYMS where PARAMETER_ID='&'"},
        {"insertSentimentParamSynonyms", "insert into PRG_SENTIMENT_PARAM_SYNONYMS(SYNONYM_ID,PARAMETER_ID,KEYWORD) values(PRG_SENTIMENT_SYNONYM_SEQ.nextval,'&','&')"},
        {"getKeywords", "select * from PRG_SENTIMENT_PARAM_SYNONYMS"}
    };
}
