/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.fileupload;

import com.progen.reportdesigner.bd.ReportTemplateBD;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class FileUploadAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(FileUploadAction.class);

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        FileUploadFormBean uploadForm = (FileUploadFormBean) form;
        FileOutputStream outputStream = null;
        FormFile formFile = null;
        PrintWriter out = response.getWriter();

        formFile = uploadForm.getFilename();
        String fileName = formFile.getFileName();
        byte[] fileData = formFile.getFileData();
        String filePath = request.getSession(false).getServletContext().getRealPath("/").replace("/build", "") + "WEB-INF/" + formFile.getFileName();

        File file = new File(filePath);
        try {
            file.delete();
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            if (file.exists()) {
                outputStream.write(formFile.getFileData());
            }
            outputStream.flush();
            outputStream.close();

            out.println(fileName);
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }

    public ActionForward onSuccess(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return null;
    }

    public ActionForward excelFileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String reportId = request.getParameter("reportId");
        Container container = Container.getContainerFromSession(request, reportId);
        FileUploadFormBean uploadForm = (FileUploadFormBean) form;
        FormFile formFile = null;
        formFile = uploadForm.getFilename();
        String fileName = formFile.getFileName();
        byte[] fileData = formFile.getFileData();
        FileOutputStream outputStream = null;
        String filePath = this.getServlet().getServletContext().getRealPath("/") + Calendar.getInstance().getTimeInMillis() + "_" + formFile.getFileName().replace("xlsx", "xls");

        File file = new File(filePath);
        try {
            file.delete();
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            if (file.exists()) {
                outputStream.write(formFile.getFileData());
            }
            outputStream.flush();
            outputStream.close();
            container.setExcelFilePath(filePath);
            ReportTemplateBD templateBd = new ReportTemplateBD();
            HashMap excelMap = templateBd.ReadExcelDetails(reportId, file);
//            templateBd.generateExcelreturnObject(reportId, container,file);
            request.setAttribute("excelDeatilsMap", excelMap);
            request.setAttribute("reportId", reportId);
            request.setAttribute("importExcelFrmReport", "true");
        } catch (FileNotFoundException ex) {
            logger.error("Exception:", ex);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        return mapping.findForward("excelUpload");
//    return null;

    }

    @Override
    protected Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("fileUpload", "fileUpload");
        map.put("onSuccess", "onSuccess");
        map.put("excelFileUpload", "excelFileUpload");
        return map;

    }
}
