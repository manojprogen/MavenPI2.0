/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uploadfile.excel;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * Form bean for Struts File Upload.
 * 
*/
public class StrutsUploadAndSaveForm extends ActionForm {

    private FormFile theFile;

    /**
     * @return Returns the theFile.
     */
    public FormFile getTheFile() {
        return theFile;
    }

    /**
     * @param theFile The FormFile to set.
     */
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }
}
