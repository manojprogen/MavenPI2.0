/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.excel;

import com.progen.report.ReportParameter;
import java.io.ByteArrayInputStream;
import java.util.*;
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
public class ExcelCellFormatGroupBuilder {

    public static Logger logger = Logger.getLogger(ExcelCellFormatGroupBuilder.class);

    public ExcelCellFormatGroup buildExcelCellGroup(String reportId, Container container) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        int repID = Integer.parseInt(reportId);
        String colorQry = "select EXCEL_CELL_ID,ROW_ELEMENTS,COL_ELEMENTS,PARAMETER,EXCEL_CELL from PRG_AR_EXCEL_CELL_PROPS where REPORT_ID=" + reportId;
        LinkedHashMap paramHM = new LinkedHashMap();
        Map<String, String> cellHM;
        ExcelCellFormatGroup excelCellGroup = container.getExcelCellGroup();
        ExcelCellFormat excelCell;

        ArrayList rowElements;
        ArrayList colElements;
        ReportParameter repParam;
        try {
            pbro = pbdb.execSelectSQL(colorQry);
            String colNames[] = pbro.getColumnNames();
            if (pbro != null && pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    repParam = new ReportParameter();

                    rowElements = this.getRowViewBys(pbro.getFieldValueString(i, "ROW_ELEMENTS"));
                    colElements = this.getColViewBys(pbro.getFieldValueString(i, "COL_ELEMENTS"));
                    paramHM = this.parseReportParamXML(pbro.getFieldValueClobString(i, "PARAMETER"));
                    cellHM = this.parseExcelCellXML(pbro.getFieldValueClobString(i, "EXCEL_CELL"));
                    repParam.setReportParameters(paramHM);
                    repParam.setViewBys(rowElements, colElements);
                    excelCell = excelCellGroup.createExcelCell(repParam, cellHM);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return excelCellGroup;
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

    public Map<String, String> parseExcelCellXML(String cellXml) {
        Map<String, String> cellMap = new HashMap<String, String>();

        Document cellDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            cellDocument = builder.build(new ByteArrayInputStream(cellXml.toString().getBytes()));
            root = cellDocument.getRootElement();

            if (root != null) {
                String row;
                String measId;
                String comment;
                String bgColor;
                String fontColor;

                List rowList = root.getChildren("row");
                List measIdList = root.getChildren("measure");
                List commentList = root.getChildren("comment");
                List fontColorList = root.getChildren("fontColor");
                List bgColorList = root.getChildren("bgColor");

                if (measIdList != null && measIdList.size() > 0) {
                    Element measIdElem = (Element) measIdList.get(0);
                    measId = measIdElem.getText();
                    cellMap.put("Measure", measId);
                }
                if (rowList != null && rowList.size() > 0) {
                    Element rowElem = (Element) rowList.get(0);
                    row = rowElem.getText();
                    cellMap.put("Row", row);
                }
                if (commentList != null && commentList.size() > 0) {
                    Element commentElem = (Element) commentList.get(0);
                    comment = commentElem.getText();
                    cellMap.put("Comment", comment);
                }
                if (fontColorList != null && fontColorList.size() > 0) {
                    Element fontColorElem = (Element) fontColorList.get(0);
                    fontColor = fontColorElem.getText();
                    cellMap.put("FontColor", fontColor);
                }
                if (bgColorList != null && bgColorList.size() > 0) {
                    Element bgColorElem = (Element) bgColorList.get(0);
                    bgColor = bgColorElem.getText();
                    cellMap.put("BgColor", bgColor);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return cellMap;
    }
}
