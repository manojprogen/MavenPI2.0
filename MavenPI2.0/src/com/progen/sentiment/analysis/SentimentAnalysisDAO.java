/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.gson.Gson;
import com.progen.sentiment.analysis.result.Sentiment;
import com.progen.sentiment.analysis.result.SentimentSentence;
import com.progen.studio.Studio;
import com.progen.studio.StudioItem;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class SentimentAnalysisDAO extends PbDb {

    public static Logger logger = Logger.getLogger(SentimentAnalysisDAO.class);
    ResourceBundle resourceBundle;
    final static int BATCH_COUNT = 500;
    int sequence = -1;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new SentimentAnalysisResBundleSqlServer();
            } else {
                resourceBundle = new SentimentAnalysisResBundle();
            }
        }
        return resourceBundle;
    }

    public SentimentSubject loadSentimentSubject() {
        SentimentSubject sentimentSubject = new SentimentSubject();
        try {
            String sentimentSubjectQry = "";
            PbReturnObject retObj = null;
            sentimentSubjectQry = getResourceBundle().getString("getSentimentSubject");
            retObj = execSelectSQL(sentimentSubjectQry);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                sentimentSubject.addSubject(retObj.getFieldValueString(i, 1));
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sentimentSubject;
    }

    public SentimentAdverbSet loadSentimentAdverbs() {
        SentimentAdverbSet sentimentAdverbs = new SentimentAdverbSet();
        try {
            String sentimentSubjectQry = "";
            PbReturnObject adverbsRecord = null;
            sentimentSubjectQry = getResourceBundle().getString("getSentimentAdverbs");
            adverbsRecord = execSelectSQL(sentimentSubjectQry);

            for (int i = 0; i < adverbsRecord.getRowCount(); i++) {
                SentimentConnotation connotation = SentimentConnotation.getSentimentConnotation(adverbsRecord.getFieldValueString(i, 2));
                sentimentAdverbs.addAdverb(adverbsRecord.getFieldValueString(i, 1), connotation);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sentimentAdverbs;
    }

    public SentimentParameterSet loadSentimentParameter() {
        SentimentParameterSet sentimentParamSet = new SentimentParameterSet();
        try {
            String sentimentParamQry = "";
            String finalQuery;
            PbReturnObject retObj = null;
            sentimentParamQry = getResourceBundle().getString("getSentimentParameter");
            retObj = execSelectSQL(sentimentParamQry);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                sentimentParamSet.addSentimentParameter(retObj.getFieldValueString(i, 1));
                PbReturnObject pbRetObj = null;
                String paramDetailQry = getResourceBundle().getString("getParameterBuzzes");
                Object[] parameterId = new Object[1];
                parameterId[0] = retObj.getFieldValueInt(i, 0);
                finalQuery = buildQuery(paramDetailQry, parameterId);

                pbRetObj = execSelectSQL(finalQuery);
                for (int j = 0; j < pbRetObj.getRowCount(); j++) {
                    SentimentConnotation connotation = SentimentConnotation.getSentimentConnotation(pbRetObj.getFieldValueString(j, 2));
                    sentimentParamSet.addBuzzWordForParameter(pbRetObj.getFieldValueString(j, 0), pbRetObj.getFieldValueString(j, 1), connotation);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sentimentParamSet;
    }

    public boolean insertSentimentAnalysisResults(Sentiment sentiment, String from) {
        boolean flag = false;
        try {
            int postv = 0;
            int negatv = 0;
            int none = 0;
            int question = 0;
            int other = 0;
            int sentenceId;
            int parameterId;
            String finalQuery = null;

            int subjectId = 0;

            HashMap<String, Integer> sentenceMap = new HashMap<String, Integer>();
            ArrayList<String> queryList = new ArrayList<String>();
            String getConnectionIdQry = getResourceBundle().getString("getConnectionId");
            PbReturnObject retObj = null;
            retObj = execSelectSQL(getConnectionIdQry);
            String connectionId = retObj.getFieldValueString(0, 0);
            List<SentimentSentence> sentences = sentiment.getSentences();
            String insertQuery = getResourceBundle().getString("insertSentimentAnalysis");
            Object[] bind = new Object[7];
            SentimentCustomAnalysis customAnalysis = null;
            int i = 0;
            int dimensionId = -1;
            ArrayList<String> customDimQryLst = new ArrayList<String>();
            ArrayList<String> sentenceQryLst = new ArrayList<String>();
            ArrayList<String> parameterQryLst = new ArrayList<String>();
            HashMap<String, Integer> paramMap = new HashMap<String, Integer>();
            ArrayList<String> finalQryLst = new ArrayList<String>();

            for (SentimentSentence sentence : sentences) {
                String sentimentSubject = sentence.getSubject();
                if (sentimentSubject != null && sentenceMap.get(sentimentSubject.toUpperCase()) != null) {
                    subjectId = sentenceMap.get(sentimentSubject.toUpperCase());
                } else {
                    if (sentimentSubject != null && !sentimentSubject.equalsIgnoreCase("")) {
                        subjectId = insertSentimentSubjectDim(sentimentSubject, connectionId);
                        sentenceMap.put(sentimentSubject.toUpperCase(), subjectId);
                    }
                }
                sentenceId = insertSentenceDimension(sentence, connectionId, sentenceQryLst);
                ArrayList<SentimentParameter> paramList = sentence.getSentenceParameters();
                for (SentimentParameter parameter : paramList) {
                    parameterId = insertParameterDimension(parameter, connectionId, parameterQryLst, paramMap);
                    postv = 0;
                    negatv = 0;
                    none = 0;
                    other = 0;
                    question = 0;
                    if (parameter.isParameterRated(SentimentConnotation.POSITIVE)) {
                        postv = 1;
                    } else if (parameter.isParameterRated(SentimentConnotation.NEGATIVE)) {
                        negatv = 1;
                    } else if (parameter.isParameterRated(SentimentConnotation.INQUISITIVE)) {
                        question = 1;
                    } else if (parameter.isParameterRated(SentimentConnotation.NEUTRAL)) {
                        none = 1;
                    } else {
                        other = 1;
                    }

                    if (sentiment.getCustAnalysisLst() != null && !sentiment.getCustAnalysisLst().isEmpty() && sentiment.getCustAnalysisLst().get(i) != null) {
                        customAnalysis = sentiment.getCustAnalysisLst().get(i);
                        customAnalysis.setSentenceId(sentenceId);
//                        if(!"twitter".equalsIgnoreCase(from))
                        dimensionId = insertCustomSentimentDimension(customAnalysis, customDimQryLst);
//                        else
//                           dimensionId=insertCustomSentimentDimensionForTwitter(customAnalysis, customDimQryLst) ;
                    }


                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                        bind = new Object[8];
                        if (sentimentSubject != null && !sentimentSubject.equalsIgnoreCase("")) {
                            bind[0] = subjectId;
                        } else {
                            bind[0] = "null";
                        }
                        bind[1] = parameterId;
                        bind[2] = postv;
                        bind[3] = negatv;
                        bind[4] = question;
                        bind[5] = other;
                        bind[6] = sentenceId;

                        if (dimensionId != -1) {
                            bind[7] = dimensionId;
                        } else {
                            bind[7] = "null";
                        }
                    } else {
                        bind = new Object[7];
                        if (sentimentSubject != null && !sentimentSubject.equalsIgnoreCase("")) {
                            bind[0] = subjectId;
                        } else {
                            bind[0] = "null";
                        }
                        bind[1] = parameterId;
                        bind[2] = postv;
                        bind[3] = negatv;
                        bind[4] = question;
                        bind[5] = other;
                        bind[6] = sentenceId;
                    }
                    finalQuery = buildQuery(insertQuery, bind);
                    queryList.add(finalQuery);
                }
                i++;
            }

            finalQryLst.addAll(queryList);
            finalQryLst.addAll(customDimQryLst);
            finalQryLst.addAll(parameterQryLst);
            finalQryLst.addAll(sentenceQryLst);
            flag = insertQueries(finalQryLst);

//=======
//            Connection con = null;
//            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
//            boolean check= executeMultiple(queryList,con);
//            if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE))
//            {
//                con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
//                executeMultiple(customDimQryLst,con);
//            }
//            con = null;
//>>>>>>> Stashed changes
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return false;
        }
        return flag;
    }

    private boolean insertQueries(ArrayList<String> queryLst) {
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        int i = 0;
        try {
            con.setAutoCommit(false);
            Statement statement = con.createStatement();
            for (String query : queryLst) {
                statement.addBatch(query);
                if (i == BATCH_COUNT) {
                    //
                    statement.executeBatch();
                    statement.clearBatch();
                    i = 0;
                }
                i++;
            }
            if (i > 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
            con.commit();
            con.close();
            return true;

        } catch (SQLException ex) {
            con = null;
            logger.error("Exception:", ex);
            return false;

        }
    }

    private int insertSentenceDimension(SentimentSentence sentence, String connectionId, ArrayList<String> sentenceQryLst) throws Exception {

        Connection con;
        int sentenceId;

        Object[] bind;
        PbReturnObject retObj = null;
        String insertQuery = getResourceBundle().getString("insertSentimentSentenceDimension");


        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            sentenceId = getDimensionSequence();// getSequenceNumber(getResourceBundle().getString("PRG_SENTIMENT_SENTENCE_DIM_SEQ"), con);
            bind = new Object[2];
            bind[0] = sentenceId;
            bind[1] = sentence.getSentence().replace("'", "").replace("`", "");
            String finalQuery = buildQuery(insertQuery, bind);
            sentenceQryLst.add(finalQuery);

        } else {
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            String sentenceIdQuery = getResourceBundle().getString("getSentenceId");
            bind = new Object[1];
            bind[0] = sentence.getSentence();
            String finalQuery = buildQuery(insertQuery, bind);
            execUpdateSQL(finalQuery, con);
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            retObj = execSelectSQL(sentenceIdQuery, con);
            sentenceId = retObj.getFieldValueInt(0, 0);
        }

        return sentenceId;

    }

    private int getDimensionSequence() throws Exception {
        if (sequence == -1) {
            String sequenceQry = "select max(sentence_id) from prg_sentiment_sentence";
            Connection con = ProgenConnection.getInstance().getConnectionByConId(getConnectionId());
            PbReturnObject seqRetObj = execSelectSQL(sequenceQry, con);
            if (seqRetObj != null && seqRetObj.getRowCount() > 0) {
                sequence = seqRetObj.getFieldValueInt(0, 0);
                sequence++;
            }
            con = null;
            return sequence;

        } else {
            return sequence++;
        }
    }

    private int insertParameterDimension(SentimentParameter parameter, String connectionId, ArrayList<String> paramQryLst, HashMap<String, Integer> paramMap) throws Exception {
        Connection con;

        int parameterId = 0;

        if (paramMap != null && paramMap.containsKey(parameter.getParamter())) {
            return paramMap.get(parameter.getParamter());
        } else {

            String paramQuery = getResourceBundle().getString("getSentimentParameterDimensionForUser");
            Object[] bind = new Object[1];
            PbReturnObject retoObject = null;
            bind[0] = parameter.getParamter().toUpperCase();
            String finalQuery = buildQuery(paramQuery, bind);
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            String insertQuery = getResourceBundle().getString("insertSentimentParameterDimension");

            PbReturnObject parameterRecord = execSelectSQL(finalQuery, con);


            if (parameterRecord.getRowCount() > 0) {
                parameterId = parameterRecord.getFieldValueInt(0, 0);
                paramMap.put(parameter.getParamter(), parameterId);
                return parameterId;
            }


            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                String paramDimQuery = getResourceBundle().getString("PRG_SENTIMENT_PARAM_DIM_SEQ");
                parameterId = getSequenceNumber(paramDimQuery, con);
                paramMap.put(parameter.getParamter(), parameterId);
                bind = new Object[2];
                bind[0] = parameterId;
                bind[1] = parameter.getParamter();
                finalQuery = buildQuery(insertQuery, bind);
                paramQryLst.add(finalQuery);

            } else {
                String paramIdQry = getResourceBundle().getString("getParameterId");
                bind = new Object[1];
                bind[0] = parameter.getParamter();
                finalQuery = buildQuery(insertQuery, bind);
                parameterId = retoObject.getFieldValueInt(0, 0);
            }
            return parameterId;
        }

    }

    public SentimentMetadata loadSentimentMetadata() {
        SentimentSubject subject = loadSentimentSubject();
        SentimentParameterSet paramSet = loadSentimentParameter();
        SentimentAdverbSet adverbSet = loadSentimentAdverbs();
        SentimentMetadata sentimentMetadata = new SentimentMetadata(paramSet, subject, adverbSet);
        return sentimentMetadata;

    }

    public void insertSentimentParameter(SentimentParameter sentimentParameter) {

        String sqlParamQuery = getResourceBundle().getString("insertSentimentParams");
        String selectParamQuery = getResourceBundle().getString("getSentimentParameter");
        PbReturnObject retObj;
        Object[] values = new Object[1];
        String finalQuery = "";
        String param = sentimentParameter.getParamter();
        boolean flag = false;
        try {

            retObj = execSelectSQL(selectParamQuery);
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (param.trim().equalsIgnoreCase(retObj.getFieldValueString(i, "PARAMETER_NAME"))) {
                        sentimentParameter.setParameterId(retObj.getFieldValueInt(i, "PARAMETER_ID"));
                        flag = true;
                        break;
                    }
                }
            }
            if (flag == false) {
                values[0] = sentimentParameter.getParamter().trim();
                finalQuery = buildQuery(sqlParamQuery, values);
                execModifySQL(finalQuery);
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public void insertSentimentSubjects(SentimentSubject sentimentSubject) {
        String sqlSubjectQuery = getResourceBundle().getString("insertSentimentSubjects");
        String getSubjectsQuery = getResourceBundle().getString("getSentimentSubject");
        PbReturnObject retObj = new PbReturnObject();
        String finalQuery = "";

        Object[] values = new Object[1];
        boolean flag = false;
        try {
            retObj = execSelectSQL(getSubjectsQuery);
            for (String subject : sentimentSubject.getSubjectKeys()) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (subject.equalsIgnoreCase(retObj.getFieldValueString(i, "SUBJECT_KEY"))) {
                        flag = true;
                        break;
                    }
                }

                if (flag == false) {
                    values[0] = subject.trim();
                    finalQuery = buildQuery(sqlSubjectQuery, values);
                    execModifySQL(finalQuery);
                }
                flag = false;

            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public ArrayList<SentimentParameter> getParameters() {
        ArrayList<SentimentParameter> paramList = new ArrayList<SentimentParameter>();
        SentimentParameter sentimentParameter = null;
        String sqlParamsQuery = getResourceBundle().getString("getSentimentParameter");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(sqlParamsQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                sentimentParameter = new SentimentParameter(retObj.getFieldValueString(i, "PARAMETER_NAME"));
                sentimentParameter.setParameterId(retObj.getFieldValueInt(i, "PARAMETER_ID"));
                paramList.add(sentimentParameter);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return paramList;
    }

    public String getParamBuzzwords(String parameterId) {
        String sqlQuery = getResourceBundle().getString("getParameterBuzzes");
        PbReturnObject retObj = null;
        ArrayList<String> positiveArray = new ArrayList<String>();
        ArrayList<String> negativeArray = new ArrayList<String>();
        ArrayList<String> neutralArray = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder("");
        StringBuilder strBuilder = new StringBuilder();
        String connotation = "";
        try {
            Object[] values = new Object[1];
            values[0] = parameterId;
            retObj = execSelectSQL(buildQuery(sqlQuery, values));
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    connotation = retObj.getFieldValueString(i, "CONNOTATION");
                    if (connotation.equalsIgnoreCase("POSITIVE")) {
                        positiveArray.add(retObj.getFieldValueString(i, "BUZZWORD"));
                    } else if (connotation.equalsIgnoreCase("NEGATIVE")) {
                        negativeArray.add(retObj.getFieldValueString(i, "BUZZWORD"));
                    } else if (connotation.equalsIgnoreCase("NEUTRAL")) {
                        neutralArray.add(retObj.getFieldValueString(i, "BUZZWORD"));
                    }
                }
            }
            stringBuilder.append("{Positive:[");
            int i = 0;
            for (String positive : positiveArray) {
                stringBuilder.append("\"").append(positive).append("\"");
                if (i != positiveArray.size() - 1) {
                    stringBuilder.append(",");
                }
                i++;
            }

            i = 0;
            stringBuilder.append("],Negative:[");
            for (String negative : negativeArray) {
                stringBuilder.append("\"").append(negative).append("\"");
                if (i != negativeArray.size() - 1) {
                    stringBuilder.append(",");
                }
                i++;
            }
            i = 0;
            stringBuilder.append("],Neutral:[");
            for (String neutral : neutralArray) {
                stringBuilder.append("\"").append(neutral).append("\"");
                if (i != neutralArray.size() - 1) {
                    stringBuilder.append(",");
                }
                i++;
            }
            stringBuilder.append("]}");

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return stringBuilder.toString();
    }

    public SentimentBuzzWordSet insertBuzzwords(SentimentBuzzWordSet buzzwordSet) {

        String sqlBuzzQuery = getResourceBundle().getString("insertSentimentBuzzwords");
        String selectQuery = getResourceBundle().getString("getBuzzwordDims");
        SentimentBuzzWordSet buzzWordSet = new SentimentBuzzWordSet();
        PbReturnObject retObj = null;
        Object[] values;
        boolean flag = false;
        int buzzId;
        for (SentimentBuzzWord buzzword : buzzwordSet.getBuzzWords()) {
            try {
                retObj = execSelectSQL(selectQuery);
                if (retObj != null && retObj.getRowCount() > 0) {
                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        if (buzzword.getBuzzWord().trim().equalsIgnoreCase(retObj.getFieldValueString(i, "BUZZWORD"))) {
                            buzzWordSet.addSentimentBuzzWord(buzzword.getBuzzWord().trim(), retObj.getFieldValueInt(i, "BUZZWORD_ID"));
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag == false) {
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                        String buzzIdQuery = getResourceBundle().getString("getBuzzwordId");
                        buzzId = getSequenceNumber(buzzIdQuery);
                        values = new Object[2];
                        values[0] = buzzId;
                        values[1] = buzzword.getBuzzWord().trim();
                        execUpdateSQL(buildQuery(sqlBuzzQuery, values));

                    } else {
                        values = new Object[1];
                        values[0] = buzzword.getBuzzWord().trim();
                        buzzId = insertAndGetSequenceInSQLSERVER(buildQuery(sqlBuzzQuery, values), "PRG_SENTIMENT_BUZZWORDS");
                    }

                    buzzWordSet.addSentimentBuzzWord(buzzword.getBuzzWord().trim(), buzzId);
                }
                flag = false;
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }

        }
        Gson gson = new Gson();


        return buzzWordSet;
    }

    public void insertParameterBuzzes(SentimentParameter sentimentParameter) {

        SentimentBuzzWordSet positive = sentimentParameter.getPositive();
        SentimentBuzzWordSet negative = sentimentParameter.getNegative();
        SentimentBuzzWordSet neutral = sentimentParameter.getNeutral();
        saveParameterBuzzes(positive, sentimentParameter, "POSITIVE");
        saveParameterBuzzes(negative, sentimentParameter, "NEGATIVE");
        saveParameterBuzzes(neutral, sentimentParameter, "NEUTRAL");

    }

    public void saveParameterBuzzes(SentimentBuzzWordSet buzzwordSet, SentimentParameter sentimentParameter, String connotation) {
        String sqlParamBuzzQuery = getResourceBundle().getString("insertSentimentParameterBuzz");
        String sentimentParamBuzzQuery = getResourceBundle().getString("getSentimentParameterBuzzs");
        HashSet<SentimentBuzzWord> hashSet;
        PbReturnObject retObj = null;

        Object[] values;
        boolean flag = false;
        hashSet = buzzwordSet.getBuzzWords();
        for (SentimentBuzzWord sentimentBuzzWord : hashSet) {
            try {
                retObj = execSelectSQL(sentimentParamBuzzQuery);

                if (retObj != null && retObj.getRowCount() > 0) {
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        if (sentimentParameter.getParameterId() == retObj.getFieldValueInt(j, "PARAMETER_ID")
                                && sentimentBuzzWord.getBuzzWordId() == retObj.getFieldValueInt(j, "BUZZWORD_ID")
                                && connotation.equalsIgnoreCase(retObj.getFieldValueString(j, "CONNOTATION"))) {
                            flag = true;
                            break;
                        }
                    }

                }
                if (flag == false) {
                    values = new Object[3];
                    values[0] = sentimentParameter.getParameterId();
                    values[1] = sentimentBuzzWord.getBuzzWordId();
                    values[2] = connotation;
                    execModifySQL(buildQuery(sqlParamBuzzQuery, values));
                }
                flag = false;
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }


        }

    }

    private int insertSentimentSubjectDim(String sentimentSubject, String connectionId) {
        Connection con;
        int subjectId = 0;
        PbReturnObject retObj = null;
        PbReturnObject returnObj = null;
        boolean flag = false;
        Object[] obj = new Object[1];
        con = ProgenConnection.getInstance().getConnectionByConId(connectionId);


        String getSubjectsQuery = getResourceBundle().getString("getSentimenSubjectDims");
        String insertSubjectsQuery = getResourceBundle().getString("insertSentimentSubjectDim");

        try {
            returnObj = execSelectSQL(getSubjectsQuery, con);
            for (int i = 0; i < returnObj.getRowCount(); i++) {
                if (sentimentSubject != null && sentimentSubject.equalsIgnoreCase(returnObj.getFieldValueString(i, "SUBJECT"))) {
                    subjectId = returnObj.getFieldValueInt(i, "SUBJECT_ID");
                    flag = true;
                    break;
                }

            }
            if (flag == false) {

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    String nextQuery = getResourceBundle().getString("PRG_SENTIMENT_SUBJECT_DIM_SEQ");
                    con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                    subjectId = getSequenceNumber(nextQuery, con);
                    obj = new Object[2];
                    obj[0] = subjectId;
                    obj[1] = sentimentSubject;
                    con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                    execUpdateSQL(buildQuery(insertSubjectsQuery, obj), con);

                } else {
                    String subjectIdQuery = getResourceBundle().getString("getSubjectId");
                    obj = new Object[1];
                    obj[0] = sentimentSubject;
                    con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                    execUpdateSQL(buildQuery(insertSubjectsQuery, obj), con);
                    con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
                    retObj = execSelectSQL(buildQuery(subjectIdQuery, obj), con);
                    subjectId = retObj.getFieldValueInt(0, "SUBJECT_ID");

                }

            }
            flag = false;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return subjectId;
    }

    public String getConnectionId() {
        String getConnectionIdQry = getResourceBundle().getString("getConnectionId");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(getConnectionIdQry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj.getFieldValueString(0, 0);
    }

    public PbReturnObject getDataForAnalyze() {
        PbReturnObject pbro = null;
        String getDataForAnalyzeQry = getResourceBundle().getString("getDataForAnalyze");
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        try {
            pbro = execSelectSQL(getDataForAnalyzeQry, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pbro;
    }

    public List<SentimentCustomAnalysis> getCustomAnalysisLst() {
        List<SentimentCustomAnalysis> customAnalysisLst = new ArrayList<SentimentCustomAnalysis>();
        SentimentCustomAnalysis sentimentCustAnalysis;
        PbReturnObject pbro = null;
        String getDataForAnalyzeQry = getResourceBundle().getString("getDataForAnalyze");
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        try {
            pbro = execSelectSQL(getDataForAnalyzeQry, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        if (pbro != null && pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                sentimentCustAnalysis = new SentimentCustomAnalysis();
                sentimentCustAnalysis.setAttributeDate1(pbro.getFieldValueDate(i, "FEEDBACKDATE"));
                sentimentCustAnalysis.setAttributeChar1(pbro.getFieldValueString(i, "FORMCODE"));
                sentimentCustAnalysis.setAttribute1(pbro.getFieldValueInt(i, "SERNUMBER"));
                sentimentCustAnalysis.setAttribute2(pbro.getFieldValueInt(i, "SATISFACTION"));
                sentimentCustAnalysis.setAttributeChar2(pbro.getFieldValueString(i, "REASONS"));
                sentimentCustAnalysis.setAttribute3(pbro.getFieldValueInt(i, "RECOMMENDATION"));
                sentimentCustAnalysis.setAttributeChar3(pbro.getFieldValueString(i, "REASON"));
                sentimentCustAnalysis.setAttributeChar4(pbro.getFieldValueString(i, "SERVICESTARS"));
                sentimentCustAnalysis.setAttributeChar5(pbro.getFieldValueString(i, "UHID"));
                sentimentCustAnalysis.setAttributeChar6(pbro.getFieldValueString(i, "NAME"));
                sentimentCustAnalysis.setAttributeDate2(pbro.getFieldValueDate(i, "REGDATE"));
                sentimentCustAnalysis.setAttributeChar7(pbro.getFieldValueString(i, "USEDOURSERVICES"));
                sentimentCustAnalysis.setAttributeChar8(pbro.getFieldValueString(i, "REFERREDBY"));
                sentimentCustAnalysis.setAttributeChar9(pbro.getFieldValueString(i, "UPDATESONHEALTH"));
                sentimentCustAnalysis.setAttributeChar10(pbro.getFieldValueString(i, "EMAILID"));
                sentimentCustAnalysis.setAttributeChar11(pbro.getFieldValueString(i, "LETTER"));
                sentimentCustAnalysis.setAttributeChar12(pbro.getFieldValueString(i, "STATUS"));
                sentimentCustAnalysis.setAttributeChar13(pbro.getFieldValueString(i, "CENTRE"));
                customAnalysisLst.add(sentimentCustAnalysis);
            }
        }
        return customAnalysisLst;
    }

    public SentimentSubject getSubjects() {

        SentimentSubject sentimentSubject = new SentimentSubject();
        String getSubjectsQuery = getResourceBundle().getString("getSentimentSubject");
        PbReturnObject retObj = new PbReturnObject();
        try {
            retObj = execSelectSQL(getSubjectsQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                sentimentSubject.addSubject(retObj.getFieldValueString(i, "SUBJECT_KEY"));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return sentimentSubject;

    }

    public List<SentimentCustomAnalysis> getCustomAnalysisLstForTwitter() {
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        String getTweetsQry = getResourceBundle().getString("getTweetsQry");
        SentimentCustomAnalysis custAnalysis;
        List<SentimentCustomAnalysis> custAnalysisLst = new ArrayList<SentimentCustomAnalysis>();
        try {
            PbReturnObject pbro = execSelectSQL(getTweetsQry, con);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                custAnalysis = new SentimentCustomAnalysis();
                custAnalysis.setAttribute1(pbro.getFieldValueInt(i, "USER_ID"));
                custAnalysis.setAttribute2(pbro.getFieldValueInt(i, "TWEET_ID"));
                custAnalysis.setAttribute3(pbro.getFieldValueInt(i, "RETWEET_COUNT"));
                custAnalysis.setAttribute4(pbro.getFieldValueInt(i, "STATUS_COUNT"));
                custAnalysis.setAttribute5(pbro.getFieldValueInt(i, "TWITTER_ID"));
                custAnalysis.setAttribute6(pbro.getFieldValueInt(i, "FOLLOWER_COUNT"));
                custAnalysis.setAttribute7(pbro.getFieldValueInt(i, "FOLLOWING_COUNT"));
                custAnalysis.setAttributeChar1(pbro.getFieldValueString(i, "USER_NAME_ID"));
                custAnalysis.setAttributeChar2(pbro.getFieldValueString(i, "USER_NAME").replace("'", "").replace("`", ""));
                custAnalysis.setAttributeChar3(pbro.getFieldValueString(i, "TWEET_TEXT").replace("'", "").replace("`", ""));
                custAnalysis.setAttributeChar4(pbro.getFieldValueString(i, "LOCATION"));
                custAnalysis.setAttributeDate1(pbro.getFieldValueDate(i, "TWEET_DATE"));
                custAnalysisLst.add(custAnalysis);
            }
        } catch (Exception ex) {
        }
        return custAnalysisLst;
    }
//    private int insertCustomSentimentDimensionForTwitter(SentimentCustomAnalysis custAnalysis,ArrayList<String> customDimQryLst)
//   {
//        int dimensionId=-1;
//        PbReturnObject pbro;
//        String connectionId=getConnectionId();
//        Connection con=null;
//        Object bindObj[]=new Object[13];
//        String finalQuery="";
//        Calendar calender=Calendar.getInstance();
//        calender.setTime(custAnalysis.getAttributeDate1());
//        String date1=calender.get(Calendar.DATE)+"-"+(calender.getDisplayName(Calendar.MONTH,1,Locale.getDefault()))+"-"+calender.get(Calendar.YEAR);
//        try {
//            con=ProgenConnection.getInstance().getConnectionByConId(connectionId);
//            String seqQry = getResourceBundle().getString("getCustSentimentSeq");
//            pbro=execSelectSQL(seqQry,con);
//            String insertQuery = getResourceBundle().getString("insertCustomSentimentForTwitter");
//            bindObj[0]=pbro.getFieldValueInt(0, 0);
//            bindObj[1]=custAnalysis.getAttribute1();
//            bindObj[2]=custAnalysis.getAttribute1();
//            bindObj[3]=custAnalysis.getAttribute1();
//            bindObj[4]=custAnalysis.getAttribute1();
//            bindObj[5]=custAnalysis.getAttribute1();
//            bindObj[6]=custAnalysis.getAttribute1();
//            bindObj[7]=custAnalysis.getAttribute1();
//
//            bindObj[8]=custAnalysis.getAttributeChar1();
//            bindObj[9]=custAnalysis.getAttributeChar1();
//            bindObj[10]=custAnalysis.getAttributeChar1();
//            bindObj[11]=custAnalysis.getAttributeChar1();
//            bindObj[12]=date1;
//
//            finalQuery=buildQuery(insertQuery, bindObj);
//            //con=ProgenConnection.getInstance().getConnectionByConId(connectionId);
//            //
//            customDimQryLst.add(finalQuery);
////            int count=execUpdateSQL(finalQuery, con);
////            if(count>0)
//            dimensionId=pbro.getFieldValueInt(0, 0);
////            else
////                dimensionId=-1;
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//        return dimensionId;
//   }

    private int insertCustomSentimentDimension(SentimentCustomAnalysis custAnalysis, ArrayList<String> customDimQryLst) {
        int dimensionId = -1;
        PbReturnObject pbro;
        String connectionId = getConnectionId();
        Connection con = null;
        Object bindObj[] = new Object[47];
        String finalQuery = "";
        try {
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            String seqQry = getResourceBundle().getString("getCustSentimentSeq");
            String insertQuery = getResourceBundle().getString("insertCustomSentiment");

            dimensionId = getSequenceNumber(seqQry, con);
            bindObj[0] = dimensionId;
            bindObj[1] = custAnalysis.getAttribute1();
            bindObj[2] = custAnalysis.getAttribute2();
            bindObj[3] = custAnalysis.getAttribute3();
            bindObj[4] = custAnalysis.getAttribute4();
            bindObj[5] = custAnalysis.getAttribute5();
            bindObj[6] = custAnalysis.getAttribute6();
            bindObj[7] = custAnalysis.getAttribute7();
            bindObj[8] = custAnalysis.getAttribute8();
            bindObj[9] = custAnalysis.getAttribute9();
            bindObj[10] = custAnalysis.getAttribute10();
            bindObj[11] = custAnalysis.getAttribute11();
            bindObj[12] = custAnalysis.getAttribute12();
            bindObj[13] = custAnalysis.getAttribute13();
            bindObj[14] = custAnalysis.getAttribute14();
            bindObj[15] = custAnalysis.getAttribute15();

            bindObj[16] = custAnalysis.getAttributeChar1() == null ? "null" : custAnalysis.getAttributeChar1();
            bindObj[17] = custAnalysis.getAttributeChar2() == null ? "null" : custAnalysis.getAttributeChar2();
            bindObj[18] = custAnalysis.getAttributeChar3() == null ? "null" : custAnalysis.getAttributeChar3();
            bindObj[19] = custAnalysis.getAttributeChar4() == null ? "null" : custAnalysis.getAttributeChar4();
            bindObj[20] = custAnalysis.getAttributeChar5() == null ? "null" : custAnalysis.getAttributeChar5();
            bindObj[21] = custAnalysis.getAttributeChar6() == null ? "null" : custAnalysis.getAttributeChar6();
            bindObj[22] = custAnalysis.getAttributeChar7() == null ? "null" : custAnalysis.getAttributeChar7();
            bindObj[23] = custAnalysis.getAttributeChar8() == null ? "null" : custAnalysis.getAttributeChar8();
            bindObj[24] = custAnalysis.getAttributeChar9() == null ? "null" : custAnalysis.getAttributeChar9();
            bindObj[25] = custAnalysis.getAttributeChar10() == null ? "null" : custAnalysis.getAttributeChar10();
            bindObj[26] = custAnalysis.getAttributeChar11() == null ? "null" : custAnalysis.getAttributeChar11();
            bindObj[27] = custAnalysis.getAttributeChar12() == null ? "null" : custAnalysis.getAttributeChar12();
            bindObj[28] = custAnalysis.getAttributeChar13() == null ? "null" : custAnalysis.getAttributeChar13();
            bindObj[29] = custAnalysis.getAttributeChar14() == null ? "null" : custAnalysis.getAttributeChar14();
            bindObj[30] = custAnalysis.getAttributeChar15() == null ? "null" : custAnalysis.getAttributeChar15();

            bindObj[31] = getFormattedDate(custAnalysis.getAttributeDate1());
            bindObj[32] = getFormattedDate(custAnalysis.getAttributeDate2());
            bindObj[33] = getFormattedDate(custAnalysis.getAttributeDate3());
            bindObj[34] = getFormattedDate(custAnalysis.getAttributeDate4());
            bindObj[35] = getFormattedDate(custAnalysis.getAttributeDate5());
            bindObj[36] = getFormattedDate(custAnalysis.getAttributeDate6());
            bindObj[37] = getFormattedDate(custAnalysis.getAttributeDate7());
            bindObj[38] = getFormattedDate(custAnalysis.getAttributeDate8());
            bindObj[39] = getFormattedDate(custAnalysis.getAttributeDate9());
            bindObj[40] = getFormattedDate(custAnalysis.getAttributeDate10());
            bindObj[41] = getFormattedDate(custAnalysis.getAttributeDate11());
            bindObj[42] = getFormattedDate(custAnalysis.getAttributeDate12());
            bindObj[43] = getFormattedDate(custAnalysis.getAttributeDate13());
            bindObj[44] = getFormattedDate(custAnalysis.getAttributeDate14());
            bindObj[45] = getFormattedDate(custAnalysis.getAttributeDate15());

            bindObj[46] = custAnalysis.getSentenceId();
            finalQuery = buildQuery(insertQuery, bindObj);
            //
            //con=ProgenConnection.getInstance().getConnectionByConId(connectionId);
            //
            customDimQryLst.add(finalQuery);

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return dimensionId;
    }

    private String getFormattedDate(Date date) {
        String formattedDate = "null";
        if (date != null) {
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            formattedDate = calender.get(Calendar.DATE) + "-" + (calender.getDisplayName(Calendar.MONTH, 1, Locale.getDefault())) + "-" + calender.get(Calendar.YEAR);
            formattedDate = "'" + formattedDate + "'";
        }
        return formattedDate;
    }

    public String getDataForClassification() {
        String reclassificationQuery = getResourceBundle().getString("reclassificationQry");
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        StringBuilder idBuilder = new StringBuilder();
        StringBuilder sentenceBuilder = new StringBuilder();
        idBuilder.append("{ SentenceIds:[");
        sentenceBuilder.append(" Sentence:[");
        try {
            PbReturnObject pbro = execSelectSQL(reclassificationQuery, con);
            for (int i = 0; i < pbro.getRowCount(); i++) {
                idBuilder.append("\"").append(pbro.getFieldValueInt(i, "SENTENCE_ID")).append("\"");
                sentenceBuilder.append("\"").append(pbro.getFieldValueString(i, "SENTENCE")).append("\"");
                if (i != pbro.getRowCount() - 1) {
                    idBuilder.append(",");
                    sentenceBuilder.append(",");
                }
            }
            idBuilder.append("],");
            sentenceBuilder.append("] }");
            idBuilder.append(sentenceBuilder);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return idBuilder.toString();
    }

    public Studio createStudioforSentiment(String cxtPath) {
        String reclassificationQuery = getResourceBundle().getString("reclassificationQry");
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        Studio studio = new Studio();
        StudioItem item;
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(reclassificationQuery, con);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                item = new StudioItem();
                item.setAttribute1(retObj.getFieldValueString(i, 0));
                item.setAttribute2(retObj.getFieldValueString(i, 1).replace("\"", ""));
                item.setAttribute3("");

                studio.addItem(item);
            }
            studio.addLabel("Sentence Id");
            studio.addLabel("Sentence");
            studio.addLabel("Reclassify");
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }



        return studio;
    }

    public String getSenteceForReclassify(String sentenceId) {
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        String qry = getResourceBundle().getString("reclassifySentenceQry");
        String classifiedQry = getResourceBundle().getString("classifiedQry");
        StringBuilder posBuilder = new StringBuilder();
        StringBuilder nagBuilder = new StringBuilder();
        StringBuilder otherBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        StringBuilder userInfoBuilder = new StringBuilder();

//        paramBuilder.append("{ Parameters:[");
//        posBuilder.append("Positives:[");
//        nagBuilder.append("Negatives:[");
//        otherBuilder.append("Others:[");
        userInfoBuilder.append("UserDetails:[");
        Object bindObj[] = new Object[1];
        bindObj[0] = sentenceId;
        String finalQry = buildQuery(qry, bindObj);
        PbReturnObject pbro = null;
        PbReturnObject classifiedPbro = null;
        StringBuilder builder = null;
        try {
            pbro = execSelectSQL(finalQry, con);
            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
//            classifiedPbro=execSelectSQL(buildQuery(classifiedQry, bindObj),con);
//           if(classifiedPbro!=null&& classifiedPbro.getRowCount()>0)
//           {
//                 builder=getRatingForSentence(classifiedPbro);
//           } else
//           {
            builder = getRatingForSentence(pbro);
//           }
//           for(int i=0;i<pbro.getRowCount();i++)
//           {
//               paramBuilder.append("\"").append(pbro.getFieldValueString(i, "PARAMETER_NAME")).append("\"");
//               posBuilder.append("\"").append(pbro.getFieldValueString(i, "POSITIVE_RATING")).append("\"");
//               nagBuilder.append("\"").append(pbro.getFieldValueString(i, "NEGATIVE_RATING")).append("\"");
//               otherBuilder.append("\"").append(pbro.getFieldValueString(i, "OTHER_RATING")).append("\"");
//               if(i!=pbro.getRowCount()-1)
//               {
//                    paramBuilder.append(",");
//                    posBuilder.append(",");
//                    nagBuilder.append(",");
//                    otherBuilder.append(",");
//               }
//           }
            if (pbro != null && pbro.getRowCount() > 0) {

                userInfoBuilder.append("\"").append(pbro.getFieldValueInt(0, "ATTRIBUTE5")).append("\",");
                userInfoBuilder.append("\"").append(pbro.getFieldValueString(0, "ATTRIBUTE_CHAR1")).append("\",");
                userInfoBuilder.append("\"").append(pbro.getFieldValueString(0, "ATTRIBUTE_CHAR5")).append("\",");
                userInfoBuilder.append("\"").append(pbro.getFieldValueString(0, "ATTRIBUTE_CHAR6")).append("\"");
            }
            userInfoBuilder.append("] }");
            builder.append(userInfoBuilder);
//           paramBuilder.append("],");
//           posBuilder.append("],");
//           nagBuilder.append("],");
//           otherBuilder.append("] }");
            //       paramBuilder.append(userInfoBuilder).append(posBuilder).append(nagBuilder).append(otherBuilder);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return builder.toString();
    }

    public StringBuilder getRatingForSentence(PbReturnObject pbro) {
        StringBuilder posBuilder = new StringBuilder();
        StringBuilder nagBuilder = new StringBuilder();
        StringBuilder otherBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        paramBuilder.append("{ Parameters:[");
        posBuilder.append("Positives:[");
        nagBuilder.append("Negatives:[");
        otherBuilder.append("Others:[");
        for (int i = 0; i < pbro.getRowCount(); i++) {
            paramBuilder.append("\"").append(pbro.getFieldValueString(i, "PARAMETER_NAME")).append("\"");
            posBuilder.append("\"").append(pbro.getFieldValueString(i, "POSITIVE_RATING")).append("\"");
            nagBuilder.append("\"").append(pbro.getFieldValueString(i, "NEGATIVE_RATING")).append("\"");
            otherBuilder.append("\"").append(pbro.getFieldValueString(i, "OTHER_RATING")).append("\"");
            if (i != pbro.getRowCount() - 1) {
                paramBuilder.append(",");
                posBuilder.append(",");
                nagBuilder.append(",");
                otherBuilder.append(",");
            }
        }
        paramBuilder.append("],");
        posBuilder.append("],");
        nagBuilder.append("],");
        otherBuilder.append("], ");
        return paramBuilder.append(posBuilder).append(nagBuilder).append(otherBuilder);
    }

    public void updateReclassifiedData(String[] parameters, String[] rating, String sentenceId) {
        String connectionId = getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        String deleteQry = getResourceBundle().getString("deleteReclassifiedData");
        String qry = getResourceBundle().getString("insertReclassifiedData");
        ArrayList<String> qryList = new ArrayList<String>();
        String finalQry = "";
        Object bindObj[] = new Object[5];
        Object bindDeleteObj[] = new Object[1];
        bindDeleteObj[0] = sentenceId;
        try {
            execUpdateSQL(buildQuery(deleteQry, bindDeleteObj), con);
            for (int i = 0; i < parameters.length; i++) {
                bindObj[0] = sentenceId;
                if (rating[i].equalsIgnoreCase("positive")) {
                    bindObj[1] = 1;
                } else {
                    bindObj[1] = 0;
                }

                if (rating[i].equalsIgnoreCase("negative")) {
                    bindObj[2] = 1;
                } else {
                    bindObj[2] = 0;
                }

                if (rating[i].equalsIgnoreCase("other")) {
                    bindObj[3] = 1;
                } else {
                    bindObj[3] = 0;
                }
                bindObj[4] = parameters[i];
                finalQry = buildQuery(qry, bindObj);
                qryList.add(finalQry);
            }

            con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
            executeMultiple(qryList, con);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public ArrayList<SentimentParameter> getParamIdBuzzwords(ArrayList<SentimentParameter> paramList) {
        String sqlQuery = getResourceBundle().getString("getParameterBuzzes");
        SentimentBuzzWordSet positiveBuzzWordSet;
        SentimentBuzzWordSet negativeBuzzWordSet;
        SentimentBuzzWordSet neutralBuzzWordSet;
        String connotation = "";
        PbReturnObject sentimentObj = new PbReturnObject();

        Object[] values = new Object[1];
        try {

            for (SentimentParameter parameter : paramList) {
                positiveBuzzWordSet = new SentimentBuzzWordSet();
                negativeBuzzWordSet = new SentimentBuzzWordSet();
                neutralBuzzWordSet = new SentimentBuzzWordSet();
                values[0] = parameter.getParameterId();
                sentimentObj = execSelectSQL(buildQuery(sqlQuery, values));
                for (int i = 0; i < sentimentObj.getRowCount(); i++) {
                    connotation = sentimentObj.getFieldValueString(i, "CONNOTATION");
                    if (connotation.equalsIgnoreCase("POSITIVE")) {
                        positiveBuzzWordSet.addSentimentBuzzWord(sentimentObj.getFieldValueString(i, "BUZZWORD"));
                        parameter.addBuzzwordSet(positiveBuzzWordSet, SentimentConnotation.getSentimentConnotation(connotation));
                    } else if (connotation.equalsIgnoreCase("NEGATIVE")) {
                        negativeBuzzWordSet.addSentimentBuzzWord(sentimentObj.getFieldValueString(i, "BUZZWORD"));
                        parameter.addBuzzwordSet(negativeBuzzWordSet, SentimentConnotation.getSentimentConnotation(connotation));
                    } else if (connotation.equalsIgnoreCase("NEUTRAL")) {
                        neutralBuzzWordSet.addSentimentBuzzWord(sentimentObj.getFieldValueString(i, "BUZZWORD"));
                        parameter.addBuzzwordSet(neutralBuzzWordSet, SentimentConnotation.getSentimentConnotation(connotation));
                    }
                }
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return paramList;
    }

    public void updateSentimentParameters(int parameterId, String parameter) {
        String selectParamQuery = getResourceBundle().getString("getSentimentParameter");
        String updateQuery = getResourceBundle().getString("updateSentimentParameter");
        String finalQuery;
        PbReturnObject selectObj = new PbReturnObject();
        Object[] values;
        boolean flag = false;
        try {
            selectObj = execSelectSQL(selectParamQuery);
            for (int i = 0; i < selectObj.getRowCount(); i++) {
                if (parameter.trim().equalsIgnoreCase(selectObj.getFieldValueString(i, "PARAMETER_NAME"))) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                values = new Object[2];
                values[0] = parameter;
                values[1] = parameterId;
                finalQuery = buildQuery(updateQuery, values);
                execModifySQL(finalQuery);
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

    }

    public void updateSentimentBuzzwords(String buzzword) {

        String selectBuzzQuery = getResourceBundle().getString("getBuzzwordDims");
        String updateBuzzQuery = getResourceBundle().getString("");
    }

    public String getParamKeywords(ArrayList<SentimentParameter> paramList) {
        StringBuilder paramIdLst = new StringBuilder();
        StringBuilder keywordLst;
        StringBuilder keywords = new StringBuilder();
        String selectQuery = getResourceBundle().getString("getParamSynonyms");
        PbReturnObject retObj = new PbReturnObject();
        Object[] values = new Object[1];
        paramIdLst.append("[");

        keywords.append("[");

        try {
            for (int j = 0; j < paramList.size(); j++) {
                SentimentParameter parameter = paramList.get(j);
                values[0] = parameter.getParameterId();
                retObj = execSelectSQL(buildQuery(selectQuery, values));
                if (retObj.getRowCount() > 0) {
                    keywordLst = new StringBuilder();
//                     paramIdLst.append("\"").append(parameter.getParameterId()).append("\",");
//                     keywordLst.append("\"");
                    keywords.append("{\"paramId\":").append(parameter.getParameterId()).append(",");
                    for (int i = 0; i < retObj.getRowCount(); i++) {

                        keywordLst.append(retObj.getFieldValueString(i, "KEYWORD"));
                        if (i != retObj.getRowCount() - 1) {
                            keywordLst.append(";");
                        }
                    }
                    keywords.append("\"keywords\":\"").append(keywordLst).append("\"},");
//                      keywordLst.append("\",");
                }

            }
            keywords.append("]");
//             paramIdLst.append("]");
//             keywordLst.append("]");
            if (keywords.charAt(keywords.length() - 2) == ',') {
                keywords.deleteCharAt(keywords.length() - 2);
            }
//              if(keywordLst.charAt(keywordLst.length()-2)==',')
//                  keywordLst.deleteCharAt(keywordLst.length()-2);
//             keywords.append("{parameterIds:").append(paramIdLst).append(",keywords:").append(keywordLst).append("}");;

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return keywords.toString();
    }

    public void saveKeywords(String parameterId, String keyword) {

        String sqlQuery = getResourceBundle().getString("insertSentimentParamSynonyms");
        String selectQuery = getResourceBundle().getString("getKeywords");
        PbReturnObject retObj = new PbReturnObject();
        String finalQuery;
        Object[] values = new Object[2];
        boolean flag = false;

        try {
            retObj = execSelectSQL(selectQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (parameterId.equalsIgnoreCase(retObj.getFieldValueString(i, "PARAMETER_ID"))
                        && keyword.trim().equalsIgnoreCase(retObj.getFieldValueString(i, "KEYWORD"))) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                values[0] = parameterId;
                values[1] = keyword.trim();
                finalQuery = buildQuery(sqlQuery, values);
                execModifySQL(finalQuery);
            }

        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


    }
}
