package com.progen.target;

import com.progen.report.query.PbReportQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class PbAlertQuery extends PbDb {

    public String getAlertQuery(String targetId, String minTimeLevel, String grpId, String elementId) throws Exception {
        String targetQ = "select * from prg_target_master where prg_measure_id=(select measure_id from target_master where target_id=" + targetId + ")";
        String targetColId = "";
        String actualColId = "";
        String deviationColId = "";
        String roleId = "";
        PbReturnObject pbro = execSelectSQL(targetQ);
        targetColId = pbro.getFieldValueString(0, "TARGET_COLID");
        actualColId = pbro.getFieldValueString(0, "ACTUAL_COLID");
        deviationColId = pbro.getFieldValueString(0, "DEVIATION_COLID");

        String allIds = "";
        allIds = allIds + "," + targetColId + "," + actualColId + "," + deviationColId;
        String eleQueryForColId = "select * from prg_user_all_info_details where buss_col_id in(" + allIds + ") and "
                + " folder_id in(select bus_folder_id from target_measure_folder where bus_group_id=" + grpId + ")";
        PbReturnObject eleObj = execSelectSQL(eleQueryForColId);
        roleId = eleObj.getFieldValueString(0, "FOLDER_ID");
        HashMap allDet = new HashMap();
        for (int m = 0; m < eleObj.getRowCount(); m++) {
            allDet.put(eleObj.getFieldValueString(m, "BUSS_COL_ID"), eleObj.getFieldValueString(m, "ELEMENT_ID"));

        }

        ArrayList taregtQryElementIds = new ArrayList();
        String eleId = "";
        if (allDet.containsKey(targetColId)) {
            eleId = (String) allDet.get(targetColId);
        }
        taregtQryElementIds.add(0, eleId);
        if (allDet.containsKey(actualColId)) {
            eleId = (String) allDet.get(actualColId);
        }
        taregtQryElementIds.add(1, eleId);
        if (allDet.containsKey(deviationColId)) {
            eleId = (String) allDet.get(deviationColId);
        }
        taregtQryElementIds.add(2, eleId);
        String alertQ = "";
        PbTargetCollection collect = new PbTargetCollection();
        PbReportQuery repQuery = new PbReportQuery();
        LinkedHashMap timeDimMap = new LinkedHashMap();
        collect.targetId = targetId;
        collect.minTimeLevel = minTimeLevel;

        ArrayList targetQryAggregations = new ArrayList();
        targetQryAggregations.add(0, "SUM");
        targetQryAggregations.add(1, "SUM");
        targetQryAggregations.add(2, "SUM");

        //collect.timeDetailsArray = timeDetails;
        collect.timeDetailsMap = timeDimMap;

        collect.getParamMetaData();

        repQuery.setRowViewbyCols(collect.targetRowViewbyValues);
        repQuery.setColViewbyCols(collect.targetColViewbyValues);
        repQuery.setQryColumns(taregtQryElementIds);
        repQuery.setColAggration(targetQryAggregations);

        repQuery.setParamValue(collect.targetParametersValues);//Added by Amit
        repQuery.isTimeDrill = false;

        repQuery.setTimeDetails(collect.timeDetailsArray);
        repQuery.setDefaultMeasure(String.valueOf(taregtQryElementIds.get(0)));
        repQuery.setDefaultMeasureSumm(String.valueOf(targetQryAggregations.get(0)));
        repQuery.setUserId("41");
        repQuery.setBizRoles(roleId);

        PbReturnObject pbretObj = repQuery.getPbReturnObject(String.valueOf(taregtQryElementIds.get(0)));

        return alertQ;
    }
}
