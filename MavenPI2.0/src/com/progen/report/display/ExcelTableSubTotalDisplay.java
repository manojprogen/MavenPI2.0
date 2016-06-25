/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableSubtotalRow;
import com.progen.report.display.table.TableSubtotalDisplay;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.format.Alignment;
import jxl.write.*;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;

/**
 *
 * @author progen
 */
public class ExcelTableSubTotalDisplay extends TableSubtotalDisplay {

    public static Logger logger = Logger.getLogger(ExcelTableSubTotalDisplay.class);

    public ExcelTableSubTotalDisplay(TableBuilder builder) {
        super(builder);
    }

    @Override
    public StringBuilder generateSubTotalHtml(TableSubtotalRow[] subTotalRow) {
        //
        boolean isCrossTabReport = false;
        if (tableBldr.facade.container.getReportCollect().reportColViewbyValues != null && tableBldr.facade.container.getReportCollect().reportColViewbyValues.size() > 0) {
            isCrossTabReport = true;
        }
        String msrId = "";
        String textAlign = null;
        TableSubtotalRow row;
        StringBuilder subTotalHtml = new StringBuilder();
        String cssClass = "";
        int colCount;
        String data = "";
        Double doubleValue = 0.0;
        int rowStart = 0;
        WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD);
        ArrayList<String> hidemeasurecount = tableBldr.facade.container.getReportCollect().getHideMeasures();
        WritableCell cell;
        int actualcol = 0;
        HashMap NFMap = new HashMap();
        HashMap<String, String> crosstabMeasureId = ((PbReturnObject) tableBldr.facade.container.getRetObj()).crosstabMeasureId;
        ArrayList dataTypes = tableBldr.facade.container.getDataTypes();
        ArrayList rowviewbyValues = tableBldr.facade.container.getReportCollect().reportRowViewbyValues;
        ArrayList<Integer> index = new ArrayList<Integer>();
        if (hidemeasurecount.size() > 0) {
            for (int i = 0; i < hidemeasurecount.size(); i++) {
                index.add(tableBldr.facade.container.getDisplayColumns().indexOf("A_" + hidemeasurecount.get(i)));
            }
        }

        for (int i = 0; i < subTotalRow.length; i++) {

            rowStart = getWritableSheet().getRows();
            row = subTotalRow[i];
            colCount = row.getColumnCount();
            actualcol = tableBldr.getFromColumn();
            for (int col = tableBldr.getFromColumn(); col < colCount; col++) {
                if (!index.contains(col)) {
                    String Symbol = "";
                    String nbrSymbol = "";
                    msrId = row.getMeasrId(col);
                    if (col >= tableBldr.facade.container.getViewByCount()) {
                        if (isCrossTabReport) {
                            if (crosstabMeasureId.containsKey(msrId) && tableBldr.facade.getTextAlign(col, crosstabMeasureId.get(msrId).toString()) != null) {
                                textAlign = tableBldr.facade.getTextAlign(col, crosstabMeasureId.get(msrId).toString());
                            } else {
                                textAlign = "right";
                            }
                        } else {
                            if (tableBldr.facade.getTextAlign(col, msrId) != null) {
                                textAlign = tableBldr.facade.getTextAlign(col, msrId);
                            } else {
                                textAlign = "right";
                            }
                        }
                    }
                    WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
                    WritableCellFormat cf2 = new WritableCellFormat(writableFont, NumberFormats.THOUSANDS_FLOAT);
                    try {
                        writableCellFormat.setWrap(true);
                        cf2.setWrap(true);
                    } catch (WriteException ex) {
                        logger.error("Exception:", ex);
                    }

                    Symbol = tableBldr.facade.container.symbol.get(row.getMeasrId(col));
                    data = row.getRowData(col);
                    cssClass = row.getCssClass(col);
                    NFMap = (HashMap) tableBldr.facade.container.getTableHashMap().get("NFMap");
                    if (NFMap != null && !NFMap.isEmpty()) {
                        if (tableBldr.facade.container.isReportCrosstab() && crosstabMeasureId != null && !crosstabMeasureId.isEmpty()) {
                            if (crosstabMeasureId.containsKey(row.getMeasrId(col)) && NFMap.get(crosstabMeasureId.get(row.getMeasrId(col))) != null) {
                                nbrSymbol = String.valueOf(NFMap.get(crosstabMeasureId.get(row.getMeasrId(col))));
                            }
                        } else if (NFMap.get(row.getMeasrId(col)) != null) {
                            nbrSymbol = String.valueOf(NFMap.get(row.getMeasrId(col)));
                        }
                    }
                    if (cssClass.equalsIgnoreCase("subTotalCell")) {
                        //cell = new Label(col, rowStart,data);
                        cell = new Label(actualcol, rowStart, data);
                        cell.setCellFormat(writableCellFormat);

                    } else {
                        if (textAlign != null) {
                            try {
                                if (textAlign.equalsIgnoreCase("left")) {
                                    writableCellFormat.setAlignment(Alignment.LEFT);
                                    cf2.setAlignment(Alignment.LEFT);
                                } else if (textAlign.equalsIgnoreCase("center")) {
                                    writableCellFormat.setAlignment(Alignment.CENTRE);
                                    cf2.setAlignment(Alignment.CENTRE);
                                } else if (textAlign.equalsIgnoreCase("right")) {
                                    writableCellFormat.setAlignment(Alignment.RIGHT);
                                    cf2.setAlignment(Alignment.RIGHT);
                                } else {
                                    writableCellFormat.setAlignment(Alignment.GENERAL);
                                    cf2.setAlignment(Alignment.GENERAL);
                                }
                            } catch (Exception ex) {
                                logger.error("Exception:", ex);
                            }
                        }
                        if ((Symbol != null && !Symbol.equalsIgnoreCase("")) || (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) || "D".equals(dataTypes.get(col))) {
                            //cell = new Label(col, rowStart,data,writableCellFormat);
                            cell = new Label(actualcol, rowStart, data, writableCellFormat);
                        } else if ("N".equals(dataTypes.get(col)) && !rowviewbyValues.contains(row.getMeasrId(col).replace("A_", ""))) {
                            if (data != null && !data.equalsIgnoreCase("") && isNumeric(data)) {
                                doubleValue = new Double(data.replace(",", ""));
                                //cell = new jxl.write.Number(col,rowStart,doubleValue,cf2);
                                cell = new jxl.write.Number(actualcol, rowStart, doubleValue, cf2);
                            } else //cell = new Label(col,rowStart,data,writableCellFormat);
                            {
                                cell = new Label(actualcol, rowStart, data, writableCellFormat);
                            }
                        } else {
                            //cell = new Label(col,rowStart,data,writableCellFormat);
                            cell = new Label(actualcol, rowStart, data, writableCellFormat);
                        }
//                    cell.setCellFormat(writableCellFormat);
                    }
                    try {
                        writableSheet.addCell(cell);
                    } catch (WriteException ex) {
                        logger.error("Exception:", ex);
                    } catch (Exception ex) {
                        logger.error("Exception:", ex);
                    }
                    actualcol++;
                }

            }
            //  
        }
        return subTotalHtml;
    }

    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
