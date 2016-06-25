package com.progen.querylayer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class UploadExcelFormBean extends ActionForm {

    private FormFile file;
    private String connectionname;
    private String datasourcename;

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public String getConnectionname() {
        return connectionname;
    }

    public void setConnectionname(String connectionname) {
        this.connectionname = connectionname;
    }

    public String getDatasourcename() {
        return datasourcename;
    }

    public void setDatasourcename(String datasourcename) {
        this.datasourcename = datasourcename;
    }
}
