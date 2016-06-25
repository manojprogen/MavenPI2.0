/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.sentiment.analysis;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.progen.studio.Studio;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 *
 * @author progen
 */
public class SentimentAnalysisAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(SentimentAnalysisAction.class);

    public ActionForward uploadTextFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("multipart/form-data")) {
            try {
                DataInputStream in = new DataInputStream(request.getInputStream());
                int formDataLength = request.getContentLength();
                byte dataBytes[] = new byte[formDataLength];
                int byteRead = 0;
                int totalBytesRead = 0;

                while (totalBytesRead < formDataLength) {
                    byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                    totalBytesRead += byteRead;
                }

                String file = new String(dataBytes);
                String saveFile = file.substring(file.indexOf("filename=\"") + 10);
                saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
                saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
                int lastIndex = contentType.lastIndexOf("=");

                String boundary = contentType.substring(lastIndex + 1, contentType.length());
                int pos;
                //extracting the index of file
                pos = file.indexOf("filename=\"");
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                int boundaryLocation = file.indexOf(boundary, pos) - 4;
                int startPos = ((file.substring(0, pos)).getBytes()).length;
                int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
                endPos = boundaryLocation;

                byte[] fileData = new byte[endPos - startPos];
                int index = 0;
                for (int i = startPos; i < endPos; i++) {
                    fileData[index++] = dataBytes[i];
                }

                ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
                BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
                SentimentAnalysisBD bd = new SentimentAnalysisBD();
                bd.parseAndAnalyzeText(reader);
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        }
        return mapping.findForward("homePage");
    }

    public ActionForward analyzeData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SentimentAnalysisBD analysisBd = new SentimentAnalysisBD();
        String from = request.getParameter("from");
        boolean flag = analysisBd.parseAndAnalyzeText(from);
        try {
            PrintWriter out = response.getWriter();
            out.print(flag);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward saveSubjectsAndParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String subject = request.getParameter("subject");
        String parameter = request.getParameter("parameter");
        Iterable<String> subjectArray = Splitter.on(";").split(subject);
        Iterable<String> paramArray = Splitter.on(";").split(parameter);
        SentimentAnalysisBD analysisBD = new SentimentAnalysisBD();
        analysisBD.saveSubjectsAndParameters(subjectArray, paramArray);


        return null;
    }

    public ActionForward getParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SentimentAnalysisBD analysisBD = new SentimentAnalysisBD();
        ArrayList<SentimentParameter> paramList = new ArrayList<SentimentParameter>();
        Gson gson = new Gson();
        try {
            PrintWriter out = response.getWriter();
            paramList = analysisBD.getParameters();
            out.print(gson.toJson(paramList));
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }


        return null;
    }

    public ActionForward getSubjectsAndParameters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        ArrayList<SentimentParameter> paramList = new ArrayList<SentimentParameter>();
        SentimentSubject subject = new SentimentSubject();
        String gsonString = "";
        Gson gson = new Gson();
        try {
            PrintWriter out = response.getWriter();
            paramList = analysisDAO.getParameters();
            subject = analysisDAO.getSubjects();
            gsonString = gson.toJson(subject) + "||" + gson.toJson(paramList);
            out.print(gsonString);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }


        return null;
    }

    public ActionForward saveBuzzwords(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String buzzPositive = request.getParameter("buzzPositive");
        String buzzNegative = request.getParameter("buzzNegative");
        String buzzNeutral = request.getParameter("buzzNeutral");
        String paramId = request.getParameter("paramId");
        String parameter = request.getParameter("parameter");
        SentimentAnalysisBD analysisBD = new SentimentAnalysisBD();
        Iterable<String> buzzPositiveArray = null;
        Iterable<String> buzzNegativeArray = null;
        Iterable<String> buzzNeutralArray = null;
        if (!buzzPositive.equalsIgnoreCase("") && buzzPositive != null) {
            buzzPositiveArray = Splitter.on(";").split(buzzPositive);
        }
        if (!buzzNegative.equalsIgnoreCase("") && buzzNegative != null) {
            buzzNegativeArray = Splitter.on(";").split(buzzNegative);
        }
        if (!buzzNeutral.equalsIgnoreCase("") && buzzNeutral != null) {
            buzzNeutralArray = Splitter.on(";").split(buzzNeutral);
        }
        analysisBD.saveBuzzwords(buzzPositiveArray, buzzNegativeArray, buzzNeutralArray, Integer.parseInt(paramId), parameter);


        return null;
    }

    public ActionForward getParamBuzzwords(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String parameterId = request.getParameter("paramId");
        SentimentAnalysisBD analysisBD = new SentimentAnalysisBD();

        try {
            PrintWriter out = response.getWriter();
            String buzzWordJson = analysisBD.getParamBuzzwords(parameterId);
            out.print(buzzWordJson);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward reclassifyData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SentimentAnalysisBD bd = new SentimentAnalysisBD();
        SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
        Studio studio = dao.createStudioforSentiment(request.getContextPath());
        HttpSession session = request.getSession(false);
        session.setAttribute("studio", studio);
        Gson gson = new Gson();
        request.setAttribute("itemList", gson.toJson(studio));
        // 
//     String data=bd.getDataForReclassification();
//     request.setAttribute("classifiedData", data);
        return mapping.findForward("gotoClassify");
    }

    public ActionForward reclassifySentence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
        String sentenceId = request.getParameter("sentenceId");
        String json = dao.getSenteceForReclassify(sentenceId);
        try {
            PrintWriter out = response.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward updateRatingForSentence(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String rows = request.getParameter("ratingRows");
        String sentenceId = request.getParameter("sentenceId");
        String parameters[] = request.getParameterValues("paramNames");
        String rating[] = new String[Integer.parseInt(rows)];
        SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
        for (int i = 0; i < Integer.parseInt(rows); i++) {
            rating[i] = request.getParameter("ratingRadio" + i);

        }
        dao.updateReclassifiedData(parameters, rating, sentenceId);
        return null;
    }

    public ActionForward getParametersAndBuzzwords(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArrayList<SentimentParameter> paramList = new ArrayList<SentimentParameter>();
        Gson gson = new Gson();
        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        String keywords;
        PrintWriter out;
        try {
            paramList = analysisDAO.getParameters();
            keywords = analysisDAO.getParamKeywords(paramList);
            paramList = analysisDAO.getParamIdBuzzwords(paramList);
            out = response.getWriter();
            out.print(gson.toJson(paramList) + "|" + keywords);

        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward saveEditedChanges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String parameterId = request.getParameter("parameterId");
        String parameter = request.getParameter("parameter");
        String positiveTxt = request.getParameter("positiveTxt");
        String negativeTxt = request.getParameter("negativeTxt");
        String neutralTxt = request.getParameter("neutralTxt");
        String keywordTxt = request.getParameter("keyword");
        Iterable<String> buzzPositiveArray = null;
        Iterable<String> buzzNegativeArray = null;
        Iterable<String> buzzNeutralArray = null;
        Iterable<String> keywordArray = null;
        if (!positiveTxt.equalsIgnoreCase("") && positiveTxt != null) {
            buzzPositiveArray = Splitter.on(";").split(positiveTxt);
        }
        if (!negativeTxt.equalsIgnoreCase("") && negativeTxt != null) {
            buzzNegativeArray = Splitter.on(";").split(negativeTxt);
        }
        if (!neutralTxt.equalsIgnoreCase("") && neutralTxt != null) {
            buzzNeutralArray = Splitter.on(";").split(neutralTxt);
        }

        if (!keywordTxt.equalsIgnoreCase("") && keywordTxt != null) {
            keywordArray = Splitter.on(";").split(keywordTxt);
        }


        SentimentAnalysisDAO analysisDAO = new SentimentAnalysisDAO();
        SentimentAnalysisBD analysisBD = new SentimentAnalysisBD();
        analysisDAO.updateSentimentParameters(Integer.parseInt(parameterId), parameter);
        analysisBD.saveBuzzwords(buzzPositiveArray, buzzNegativeArray, buzzNeutralArray, Integer.parseInt(parameterId), parameter);
        analysisBD.saveKeywords(parameterId, keywordArray);

        return null;
    }

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap<String, String>();
        map.put("uploadTextFile", "uploadTextFile");
        map.put("saveSubjectsAndParameters", "saveSubjectsAndParameters");
        map.put("getParameters", "getParameters");
        map.put("saveBuzzwords", "saveBuzzwords");
        map.put("getParamBuzzwords", "getParamBuzzwords");
        map.put("analyzeData", "analyzeData");
        map.put("getSubjectsAndParameters", "getSubjectsAndParameters");
        map.put("reclassifyData", "reclassifyData");
        map.put("reclassifySentence", "reclassifySentence");
        map.put("updateRatingForSentence", "updateRatingForSentence");
        map.put("getParametersAndBuzzwords", "getParametersAndBuzzwords");
        map.put("saveEditedChanges", "saveEditedChanges");
        return map;
    }
}
