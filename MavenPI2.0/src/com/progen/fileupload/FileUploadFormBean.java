/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.fileupload;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author progen
 */
public class FileUploadFormBean extends ActionForm {

    private FormFile filename;

    public FormFile getFilename() {
        return filename;
    }

    public void setFilename(FormFile filename) {
        this.filename = filename;
    }
}
