/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.progen.report.ReportParameter;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ExcelColumnGroupBuilder {

    public static Logger logger = Logger.getLogger(ExcelColumnGroupBuilder.class);

    public ExcelColumnGroup buildExcelColumnGroup(String reportId, Container container) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();

        String colorQry = "select EXCEL_COL_ID,MEASURE_ID,ROW_ELEMENTS,COL_ELEMENTS,PARAMETER,EXCEL_COLUMN from PRG_AR_EXCEL_COLUMNS where REPORT_ID=" + reportId;
        LinkedHashMap paramHM = new LinkedHashMap();
        ArrayList colData;
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        RunTimeExcelColumn excelCol;
        String measureId;

        ArrayList rowElements;
        ArrayList colElements;
        ReportParameter repParam;
        try {
            pbro = pbdb.execSelectSQL(colorQry);

            if (pbro != null && pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    repParam = new ReportParameter();

                    measureId = pbro.getFieldValueString(i, "MEASURE_ID");
                    rowElements = this.getRowViewBys(pbro.getFieldValueString(i, "ROW_ELEMENTS"));
                    colElements = this.getColViewBys(pbro.getFieldValueString(i, "COL_ELEMENTS"));
                    paramHM = this.parseReportParamXML(pbro.getFieldValueClobString(i, "PARAMETER"));
                    colData = this.parseExcelColumnXML(pbro.getFieldValueClobString(i, "EXCEL_COLUMN"));
                    repParam.setReportParameters(paramHM);
                    repParam.setViewBys(rowElements, colElements);
                    excelCol = excelColGroup.getRunTimeColumn(measureId);
                    if (excelCol == null) {
                        excelCol = excelColGroup.createRunTimeColumn(measureId, null);
                        excelCol.setColumnPersisted(true);
                    }
                    excelCol.addColumn(repParam, colData);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return excelColGroup;
    }

    public ArrayList getRowViewBys(String rowViewBys) {
        ArrayList rowList = new ArrayList();
        String rowViews[] = rowViewBys.split(",");
        for (String row : rowViews) {
            rowList.add(row);
        }
        return rowList;
    }

    public ArrayList<String> getColViewBys(String colViewBys) {
        ArrayList colList = new ArrayList();
        String colViews[] = colViewBys.split(",");
        for (String col : colViews) {
            colList.add(col);
        }
        return colList;
    }

    public LinkedHashMap parseReportParamXML(String paramXml) {
        LinkedHashMap paramHashMap = new LinkedHashMap();
        Document paramDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            paramDocument = builder.build(new ByteArrayInputStream(paramXml.toString().getBytes()));
            root = paramDocument.getRootElement();
            List reportParameters = root.getChildren("ReportParameter");
            for (int i = 0; i < reportParameters.size(); i++) {
                Element repParam = (Element) reportParameters.get(i);
                List elementList = repParam.getChildren("ElementId");
                List valueList = repParam.getChildren("Value");
                Element element = (Element) elementList.get(0);
                Element value = (Element) valueList.get(0);
                paramHashMap.put(element.getText(), value.getText());
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return paramHashMap;
    }

    public ArrayList parseExcelColumnXML(String colXml) {
        ArrayList colData = new ArrayList();

        Document cellDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            cellDocument = builder.build(new ByteArrayInputStream(colXml.toString().getBytes()));
            root = cellDocument.getRootElement();

            if (root != null) {
                List dataList = root.getChildren("data");
                String data;
                colData = new ArrayList(dataList.size());
                if (dataList != null && dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        Element dataElem = (Element) dataList.get(i);
                        //String rowNum = dataElem.getAttributeValue("rowNum");
                        //int row = Integer.parseInt(rowNum);
                        data = dataElem.getText();
                        //colData.set(row, data);
                        colData.add(data);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return colData;
    }
}
