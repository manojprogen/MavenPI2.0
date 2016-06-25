package com.progen.target;

import prg.db.PbDb;
import prg.db.PbReturnObject;

public class ExcelSheetValidator extends PbDb {

    public String checkSheetForTarget(String targetId, String sheetName) throws Exception {
        String status = "";
        String fileQuery = "select * from target_excelmaster where target_id=" + targetId;
        //////////////////////////.println(" fileQuery "+fileQuery);
        PbReturnObject pbro = execSelectSQL(fileQuery);
        String localFile = "";
        for (int f = 0; f < pbro.getRowCount(); f++) {
            localFile = pbro.getFieldValueString(f, "FILENAME");
            if (sheetName.equalsIgnoreCase(localFile)) {
                status = "Available";
            }
            if (status.equalsIgnoreCase("Available")) {
                break;
            }
        }
        return status;
    }

    public String checkSheetUploadStatus(String sheetName) throws Exception {
        String status = "";
        String excelQ = "select * from excelsheet_uplaod where excel_sheet_name='" + sheetName + "' order by upload_time";
        PbReturnObject pbro = execSelectSQL(excelQ);
        if (pbro.getRowCount() > 0) {
            status = "Uploaded";
        }
        return status;
    }
}
