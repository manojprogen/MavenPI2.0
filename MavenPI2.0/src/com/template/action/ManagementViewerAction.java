/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.template.db.TemplateManagementDAO;
import com.template.db.TemplatePageGenerator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * @author mayank
 */
public class ManagementViewerAction extends LookupDispatchAction {

      public static Logger logger = Logger.getLogger(ManagementViewerAction.class);
    /* forward name="success" path="" */
      TemplatePageGenerator pageGenerator = new TemplatePageGenerator();
      Gson gson = new Gson();
    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    
    
//    @Override
//    public ActionForward execute(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//        
//        return mapping.findForward(SUCCESS);
//    }

    /**
     * This is the action called from the Struts framework.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */

    @Override
    protected Map getKeyMethodMap() {
       Map<String,String> map = new HashMap<>();
       map.put("viewTemplate","viewTemplate");
       map.put("getAvailableTemplateCharts","getAvailableTemplateCharts");
       map.put("getTemplateCharts","getTemplateCharts");
       map.put("getKPIValue","getKPIValue");
       map.put("getTemplateId","getTemplateId");
       map.put("getTemplateKPIData","getTemplateKPIData");
       map.put("getMeasureTotal","getMeasureTotal");
       map.put("saveTemplate","saveTemplate");
       return map;
    }
    
 
    
    public ActionForward viewTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)
    {
        System.out.println("=========================template=============================");
        return mapping.findForward("showTemplate");
    }     
    
    
    public ActionForward getTemplateCharts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//       PbReportViewerDAO dao=new PbReportViewerDAO();
        
           String fileLocation = pageGenerator.getFilePath(request.getSession(false));
        
       TemplateManagementDAO dao = new TemplateManagementDAO();

        String data = null;
        
//        String reportData = report.chartRequestHandlerDrills(request,bizzRoleName);

            try {
                data = dao.getChartsDataDrills(request, fileLocation);
            } catch (FileNotFoundException | SQLException ex) {
               data = "false";
                logger.error("Exception:", ex);
            }
        
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        out.print(data);
//       PrintWriter out =null;
//        try {
//            response.getWriter().print(reportData);
//        } catch (IOException ex) {
//            logger.error("Exception:",ex);
//        }
        return null;
    }
     public ActionForward getAvailableTemplateCharts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String reportId = "4243";
        String reportName = "P2P Analysis";
         HttpSession session = request.getSession(false);
         Type tarType = new TypeToken<List<String>>(){}.getType();
        List<String> templatePage = new ArrayList<>();
        String pageId = "default";
        templatePage = gson.fromJson(session.getAttribute("tempPages").toString(),tarType);
//        String[] pageInfo = templatePage.get(0).split(":");
//        reportId = pageInfo[0].replace("m_", "");
//        pageId = pageInfo[2];
        String report = "";
       
         report = pageGenerator.getTemplatePage(request, reportId, reportName,pageId);

        PrintWriter out = null;
        try {
            response.getWriter().print(report);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
     
    public ActionForward getKPIValue(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
        TemplatePageGenerator json = new TemplatePageGenerator();
       String result =  json.getKPIValue(request);
      try {
            response.getWriter().print(result);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }    
    public ActionForward getTemplateId(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
          HttpSession session = request.getSession(false);
          session.setAttribute("tempPages", request.getParameter("pages"));
       String result =  pageGenerator.getTemplateId(request);
      try {
            response.getWriter().print(result);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }    
    public ActionForward getTemplateKPIData(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
     String templateInfo = pageGenerator.getTemplateKPIData(request);
      try {
            response.getWriter().print(templateInfo);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }    
     
    public ActionForward getMeasureTotal(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws SQLException
    {
     String templateInfo = pageGenerator.getMeasureTotal(request);
      try {
            response.getWriter().print(templateInfo);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
}
        return null;
    }
    public ActionForward saveTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        TemplateManagementDAO dao = new TemplateManagementDAO();
        dao.saveTemplate(request);
        return null;
    }

}
