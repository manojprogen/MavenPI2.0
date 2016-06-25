package com.uploadingfile.struts;

import com.progen.Createtable.CreatetableDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class StrutsUplaodAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {


        StrutsUplaodFile fileform = (StrutsUplaodFile) form;
        FormFile myFile = fileform.getFilename();
        String contentType = myFile.getContentType();
        String fileName = myFile.getFileName();
        int fileSize = myFile.getFileSize();
        boolean fileDeleted = false;
        FileOutputStream fos = null;
        byte[] fileData = myFile.getFileData();
        String tempFileName = this.getServlet().getServletContext().getRealPath("/").replace("\\.\\", "\\") + Calendar.getInstance().getTimeInMillis() + "_" + myFile.getFileName();

        String fileNAME = myFile.getFileName();
        File tempFile = new File(tempFileName);
        ArrayList columnNames = new ArrayList();
        String[] strcolumNames = null;
        String[] strcolumTypes = null;
        if (tempFile != null) {
            if (!tempFile.exists()) {
                tempFile.createNewFile();
                fos = new FileOutputStream(tempFile);
                fos.write(fileData);
                if (fos != null) {
                    fos.close();
                }
            }
            //call methods to read excel sheets

            CreatetableDAO xlupload = new CreatetableDAO();
            // columnNames = (xlupload.readExcel(tempFile, 0));
            strcolumNames = (String[]) columnNames.get(0);
            strcolumTypes = (String[]) columnNames.get(1);
        }



        request.setAttribute("strcolumNames", strcolumNames);
        request.setAttribute("strcolumTypes", strcolumTypes);
        request.setAttribute("tempFileName", tempFileName);
//             if (tempFile.exists()) {
//                fileDeleted = tempFile.delete();
//            }
//            

        return mapping.findForward("saveTable");

    }
}
