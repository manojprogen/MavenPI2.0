/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.handsontable.bd;

import com.google.gson.Gson;
import com.progen.charts.JqplotGraphProperty;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTMeasureElement;
import com.progen.report.PbReportCollection;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.data.DataFacade;
import com.progen.report.display.util.NumberFormatter;
import com.progen.reportdesigner.db.ReportTemplateResBundleSqlServer;
import com.progen.reportdesigner.db.ReportTemplateResourceBundle;
import com.progen.reportdesigner.db.ReportTemplateResourceBundleMySql;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.PbReportViewerDAO;
import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class HandsonTableBD {

    public static Logger logger = Logger.getLogger(HandsonTableBD.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportTemplateResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new ReportTemplateResourceBundleMySql();
            } else {
                resourceBundle = new ReportTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public void insertHandsonTableData(String reportId, String reportName, String userId, String bussRoleId, String hotFileName, String hotFilePath) {
        String insertQry = getResourceBundle().getString("insertHandsonTableData");
        Object[] reportMaster = null;
        Gson gson = new Gson();
        String finalString = "";
        PbDb pbdb = new PbDb();
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportMaster = new Object[6];
            reportMaster[0] = reportId;
            reportMaster[1] = reportName;
            reportMaster[2] = userId;
            reportMaster[3] = bussRoleId;
            reportMaster[4] = hotFileName;
            reportMaster[5] = hotFilePath;
            finalString = pbdb.buildQuery(insertQry, reportMaster);
        } else {
            reportMaster = new Object[6];
            reportMaster[0] = reportId;
            reportMaster[1] = reportName;
            reportMaster[2] = userId;
            reportMaster[3] = bussRoleId;
            reportMaster[4] = hotFileName;
            reportMaster[5] = hotFilePath;
            finalString = pbdb.buildQuery(insertQry, reportMaster);
        }
        try {
            pbdb.execUpdateSQL(finalString);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject getHandsonTableData(String reportId, String userId) {
        PbReturnObject retObj = null;
        String finalString = "";
        String selectQry = getResourceBundle().getString("selecthandsontable");
        Object[] reportMaster = null;
        PbDb pbdb = new PbDb();

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportMaster = new Object[2];
            reportMaster[0] = reportId;
            reportMaster[1] = userId;
        } else {
            reportMaster = new Object[2];
            reportMaster[0] = reportId;
            reportMaster[1] = userId;
        }
        finalString = pbdb.buildQuery(selectQry, reportMaster);
        try {
            retObj = pbdb.execSelectSQL(finalString);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return retObj;
    }

//    public HandsontableDetails testForHandsonTable(String reportId) {
//        PbReturnObject pbrtObj = null;
//        pbrtObj = getHandsonTableData(reportId);
//        String filePath = pbrtObj.getFieldValueString(0, "FILEPATH_FOLDER");
//        String fileName = pbrtObj.getFieldValueString(0, "FILE_NAME");
//        HandsontableDetails hotDetails = new HandsontableDetails();
//        try {
//            FileInputStream fis2 = new FileInputStream(filePath + "/" + fileName);
//            ObjectInputStream ois2 = new ObjectInputStream(fis2);
//            hotDetails = (HandsontableDetails) ois2.readObject();
//            ois2.close();
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//        return hotDetails;
//    }
    public List getHandsonTableFormulaData(String operator, String[] rowDataList, ArrayList<String> measureNumbers, int headersize, String headerName, String operatorType, String numberValueId) {
        List handsontableData = new ArrayList();
        int i = 0;
        boolean testForValnType = false;
        Float numbervalue = 0.0f;
        if (operatorType != null && !operatorType.equalsIgnoreCase("")) {
            testForValnType = true;
            numbervalue = new Float(numberValueId);
        }
        List grandTotal = new ArrayList();
        float firstval = 0.0f;
        float secondval = 0.0f;
        float thirdval = 0.0f;
        float forthval = 0.0f;
        float fifthval = 0.0f;
        float sixthval = 0.0f;
        float seventhval = 0.0f;
        float eighthval = 0.0f;
        float ninthval = 0.0f;
        float tenthval = 0.0f;
        float leventh = 0.0f;
        float twelth = 0.0f;
        float thirteen = 0.0f;
        float forteen = 0.0f;
        float fifteen = 0.0f;
        while (i < rowDataList.length - (headersize - 1)) {
            Float value = 0.0f;
            if (headersize == 4) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            thirdval = thirdval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            thirdval = thirdval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        thirdval = thirdval + value;
                    }
                }
                i = i + 3;
            } else if (headersize == 5) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            forthval = forthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            forthval = forthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        forthval = forthval + value;
                    }
                }
                i = i + 4;
            } else if (headersize == 6) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            fifthval = fifthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            fifthval = fifthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        fifthval = fifthval + value;
                    }
                }
                i = i + 5;
            } else if (headersize == 7) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            sixthval = sixthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            sixthval = sixthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        sixthval = sixthval + value;
                    }
                }
                i = i + 6;
            } else if (headersize == 8) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            seventhval = seventhval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            seventhval = seventhval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        seventhval = seventhval + value;
                    }
                }
                i = i + 7;
            } else if (headersize == 9) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            eighthval = eighthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            eighthval = eighthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        eighthval = eighthval + value;
                    }
                }
                i = i + 8;
            } else if (headersize == 10) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            ninthval = ninthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            ninthval = ninthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        ninthval = ninthval + value;
                    }
                }
                i = i + 9;
            } else if (headersize == 11) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            tenthval = tenthval + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            tenthval = tenthval + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        tenthval = tenthval + value;
                    }
                }
                i = i + 10;
            } else if (headersize == 12) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                    tenthval = tenthval + new Float(rowDataList[i + 10]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            leventh = leventh + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            leventh = leventh + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        leventh = leventh + value;
                    }
                }
                i = i + 11;
            } else if (headersize == 13) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                    tenthval = tenthval + new Float(rowDataList[i + 10]);
                    leventh = leventh + new Float(rowDataList[i + 11]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            twelth = twelth + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            twelth = twelth + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        twelth = twelth + value;
                    }
                }
                i = i + 12;
            } else if (headersize == 14) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                    tenthval = tenthval + new Float(rowDataList[i + 10]);
                    leventh = leventh + new Float(rowDataList[i + 11]);
                    twelth = twelth + new Float(rowDataList[i + 12]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            thirteen = thirteen + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            thirteen = thirteen + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        thirteen = thirteen + value;
                    }
                }
                i = i + 13;
            } else if (headersize == 15) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                    tenthval = tenthval + new Float(rowDataList[i + 10]);
                    leventh = leventh + new Float(rowDataList[i + 11]);
                    twelth = twelth + new Float(rowDataList[i + 12]);
                    thirteen = thirteen + new Float(rowDataList[i + 13]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            forteen = forteen + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            forteen = forteen + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        forteen = forteen + value;
                    }
                }
                i = i + 14;
            } else if (headersize == 16) {
                for (int j = 0; j < headersize - 1; j++) {
                    handsontableData.add(rowDataList[i + j]);
                }
                if (i != 0) {
                    firstval = firstval + new Float(rowDataList[i + 1]);
                    secondval = secondval + new Float(rowDataList[i + 2]);
                    thirdval = thirdval + new Float(rowDataList[i + 3]);
                    forthval = forthval + new Float(rowDataList[i + 4]);
                    fifthval = fifthval + new Float(rowDataList[i + 5]);
                    sixthval = sixthval + new Float(rowDataList[i + 6]);
                    seventhval = seventhval + new Float(rowDataList[i + 7]);
                    eighthval = eighthval + new Float(rowDataList[i + 8]);
                    ninthval = ninthval + new Float(rowDataList[i + 9]);
                    tenthval = tenthval + new Float(rowDataList[i + 10]);
                    leventh = leventh + new Float(rowDataList[i + 11]);
                    twelth = twelth + new Float(rowDataList[i + 12]);
                    thirteen = thirteen + new Float(rowDataList[i + 13]);
                    forteen = forteen + new Float(rowDataList[i + 14]);
                }
                if (i == 0) {
                    handsontableData.add(headerName);
                } else {
                    List<Float> columFormula = new ArrayList<Float>();
                    for (int j = 0; j < measureNumbers.size(); j++) {
                        columFormula.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(j))].toString().trim()));
                    }
                    if (measureNumbers.size() == 1) {
                        if (testForValnType) {
                            value = getFirstVal(operatorType, numbervalue, columFormula);
                            handsontableData.add(value);
                            fifteen = fifteen + value;
                        } else {
                            handsontableData.add(new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim()));
                            fifteen = fifteen + new Float(rowDataList[i + Integer.parseInt(measureNumbers.get(0))].toString().trim());
                        }
                    } else {
                        value = getOperatorsValue(operatorType, numbervalue, testForValnType, operator, columFormula);
                        handsontableData.add(value);
                        fifteen = fifteen + value;
                    }
                }
                i = i + 15;
            }
        }

        if (headersize == 4) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
        } else if (headersize == 5) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
        } else if (headersize == 6) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
        } else if (headersize == 7) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
        } else if (headersize == 8) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
        } else if (headersize == 8) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
        } else if (headersize == 9) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
        } else if (headersize == 10) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
        } else if (headersize == 11) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
        } else if (headersize == 12) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
            grandTotal.add(leventh);
        } else if (headersize == 13) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
            grandTotal.add(leventh);
            grandTotal.add(twelth);
        } else if (headersize == 14) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
            grandTotal.add(leventh);
            grandTotal.add(twelth);
            grandTotal.add(thirteen);
        } else if (headersize == 15) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
            grandTotal.add(leventh);
            grandTotal.add(twelth);
            grandTotal.add(thirteen);
            grandTotal.add(forteen);
        } else if (headersize == 16) {
            grandTotal.add("Grand Total");
            grandTotal.add(firstval);
            grandTotal.add(secondval);
            grandTotal.add(thirdval);
            grandTotal.add(forthval);
            grandTotal.add(fifthval);
            grandTotal.add(sixthval);
            grandTotal.add(seventhval);
            grandTotal.add(eighthval);
            grandTotal.add(ninthval);
            grandTotal.add(tenthval);
            grandTotal.add(leventh);
            grandTotal.add(twelth);
            grandTotal.add(thirteen);
            grandTotal.add(forteen);
            grandTotal.add(fifteen);
        }
        handsontableData.addAll(grandTotal);
        return handsontableData;
    }

    public Float getFirstVal(String operatorType, Float numbervalue, List<Float> columFormula) {
        Float values = 0.0f;
        if (operatorType.equalsIgnoreCase("+")) {
            values = new Float(columFormula.get(0) + numbervalue);
        } else if (operatorType.equalsIgnoreCase("-")) {
            values = new Float(columFormula.get(0) - numbervalue);
        } else if (operatorType.equalsIgnoreCase("*")) {
            values = new Float(columFormula.get(0) * numbervalue);
        } else if (operatorType.equalsIgnoreCase("/")) {
            values = new Float(columFormula.get(0) / numbervalue);
        }
        return values;
    }

    public Float getOperatorsValue(String operatorType, Float numbervalue, boolean testForValnType, String operator, List<Float> columFormula) {
        Float values = 0.0f;
        if (testForValnType) {
            if (operator.equalsIgnoreCase("+")) {
                if (operatorType.equalsIgnoreCase("+")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values + new Float(columFormula.get(j));
                    }
                    values = values + numbervalue;
                } else if (operatorType.equalsIgnoreCase("-")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values + new Float(columFormula.get(j));
                    }
                    values = values - numbervalue;
                } else if (operatorType.equalsIgnoreCase("*")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values + new Float(columFormula.get(j));
                    }
                    values = values * numbervalue;
                } else {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values + new Float(columFormula.get(j));
                    }
                    values = values / numbervalue;
                }
            } else if (operator.equalsIgnoreCase("-")) {
                if (operatorType.equalsIgnoreCase("+")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values - new Float(columFormula.get(j));
                    }
                    values = values + numbervalue;
                } else if (operatorType.equalsIgnoreCase("-")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values - new Float(columFormula.get(j));
                    }
                    values = values - numbervalue;
                } else if (operatorType.equalsIgnoreCase("*")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values - new Float(columFormula.get(j));
                    }
                    values = values * numbervalue;
                } else {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values - new Float(columFormula.get(j));
                    }
                    values = values / numbervalue;
                }
            } else if (operator.equalsIgnoreCase("*")) {
                if (operatorType.equalsIgnoreCase("+")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values * new Float(columFormula.get(j));
                    }
                    values = values + numbervalue;
                } else if (operatorType.equalsIgnoreCase("-")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values * new Float(columFormula.get(j));
                    }
                    values = values - numbervalue;
                } else if (operatorType.equalsIgnoreCase("*")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values * new Float(columFormula.get(j));
                    }
                    values = values * numbervalue;
                } else {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values * new Float(columFormula.get(j));
                    }
                    values = values / numbervalue;
                }
            } else if (operator.equalsIgnoreCase("/")) {
                if (operatorType.equalsIgnoreCase("+")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values / new Float(columFormula.get(j));
                    }
                    values = values + numbervalue;
                } else if (operatorType.equalsIgnoreCase("-")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values / new Float(columFormula.get(j));
                    }
                    values = values - numbervalue;
                } else if (operatorType.equalsIgnoreCase("*")) {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values / new Float(columFormula.get(j));
                    }
                    values = values * numbervalue;
                } else {
                    for (int j = 0; j < columFormula.size(); j++) {
                        values = values / new Float(columFormula.get(j));
                    }
                    values = values / numbervalue;
                }
            }
        } else {
            if (operator.equalsIgnoreCase("+")) {
                for (int j = 0; j < columFormula.size(); j++) {
                    values = values + new Float(columFormula.get(j));
                }
            } else if (operator.equalsIgnoreCase("-")) {
                for (int j = 0; j < columFormula.size(); j++) {
                    values = values - new Float(columFormula.get(j));
                }
            } else if (operator.equalsIgnoreCase("*")) {
                for (int j = 0; j < columFormula.size(); j++) {
                    values = values * new Float(columFormula.get(j));
                }
            } else if (operator.equalsIgnoreCase("/")) {
                for (int j = 0; j < columFormula.size(); j++) {
                    values = values / new Float(columFormula.get(j));
                }
            }
        }
        return values;
    }

    public ArrayList getHotMeasureValues(int selectedMe, int calheaders, String[] rowDataList) {
        TreeSet<BigDecimal> values = new TreeSet<BigDecimal>();
        Float summVal = 0.0f;
        int i = 0;
        int allValue = rowDataList.length;
        int finalVal = allValue / calheaders;
        while (i < rowDataList.length - (calheaders)) {
            if (i != 0) {
                values.add(new BigDecimal(rowDataList[i + selectedMe].toString().trim()));
                summVal = summVal + new Float(rowDataList[i + selectedMe].toString().trim());
            }
            i = i + calheaders;
        }
        float avgVal = summVal / (finalVal - 2);
        ArrayList allValues = new ArrayList();
        allValues.add(values.last());
        allValues.add(NumberFormatter.getModifidNumber(new BigDecimal(avgVal)));
        allValues.add(values.first());
        return allValues;
    }

    public List getFilteredData(String operator, double firstvalOnly, double firstVal, double secondVal, String measureNo, String[] rowDataList, int calheaders) {
        List handsonFilter = new ArrayList();
        int no = Integer.parseInt(measureNo);
        int i = 0;
        while (i < rowDataList.length - (calheaders)) {
            if (i == 0) {
                for (int j = 0; j < calheaders; j++) {
                    handsonFilter.add(rowDataList[i + j].toString().trim());
                }
            } else {
                boolean testVal = false;
                testVal = isConditionSatisfy(operator, firstvalOnly, firstVal, secondVal, Double.parseDouble(rowDataList[i + no].toString().trim()));
                if (testVal) {
                    for (int j = 0; j < calheaders; j++) {
                        handsonFilter.add(rowDataList[i + j].toString().trim());
                    }
                }
            }
            i = i + calheaders;
        }
        return handsonFilter;
    }

    private boolean isConditionSatisfy(String operator, double firstone, double first, double second, double measValue) {
        boolean success = false;
        if (">".equals(operator)) {
            if (measValue > firstone) {
                success = true;
            }
        } else if (">=".equalsIgnoreCase(operator)) {
            if (measValue >= firstone) {
                success = true;
            }
        } else if ("<".equalsIgnoreCase(operator)) {
            if (measValue < firstone) {
                success = true;
            }
        } else if ("<=".equalsIgnoreCase(operator)) {
            if (measValue <= firstone) {
                success = true;
            }
        } else if ("==".equalsIgnoreCase(operator)) {
            if (measValue == firstone) {
                success = true;
            }
        } else if ("!=".equalsIgnoreCase(operator)) {
            if (measValue != firstone) {
                success = true;
            }
        } else if ("<>".equalsIgnoreCase(operator)) {
            if (first < measValue && measValue < second) {
                success = true;
            }
        }
        return success;
    }

    public HashMap<String, ArrayList> getAllFilesdata(String reportId, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReportViewerBD reportViewerBD = new PbReportViewerBD();
        String strURL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        strURL = strURL + "reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&action=open";
        request.setAttribute("url", strURL);
        request.setAttribute("REPORTID", reportId);

        HashMap map = null;
        Container container = null;

        reportViewerBD.prepareReport(reportId, userId, request, response,false);
        map = (HashMap) session.getAttribute("PROGENTABLES");

        container = (Container) map.get(reportId);

        HashMap GraphTypesHashMap = null;
        HashMap GraphSizesDtlsHashMap = null;
        String[] graphTypesArray = null;
        String[] grpTypeskeys = new String[0];
//                                            String ProGenImgPath = getServletContext().getRealPath("/") + "tempFolder/";
        ArrayList grpDetails = new ArrayList();
        String[] ViewBys = null;
        PbReturnObject retObj = new PbReturnObject();
        PbDb pbdb = new PbDb();

        PbReportCollection collect = container.getReportCollect();
        HashMap ParametersHashMap = container.getParametersHashMap();
        ArrayList paramList = (ArrayList) ParametersHashMap.get("Parameters");
        ArrayList parametersNames = (ArrayList) ParametersHashMap.get("ParametersNames");
        String rowViewBy = collect.reportRowViewbyValues.get(0);
        GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
        GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
        grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
        graphTypesArray = (String[]) (new TreeSet(GraphTypesHashMap.values())).toArray(new String[0]);
        DataFacade facade = new DataFacade(container);
        String defaultViewId = facade.getViewbyId();
        String paramurl = facade.getadhocParamUrl();
        // for(int i=0;i<graphTypesArray.length;i++)
        //{
        //  
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;
        ArrayList<String> sortCols = null;
        ArrayList rowSequence = new ArrayList();
        if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
        } else {
            sortCols = container.getSortColumns();
            if (sortCols != null && !sortCols.isEmpty()) {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();

                    if (container.isTopBottomSet()) {
                        int topbottomCount = container.getTopBottomCount();
                        if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_TOP_ROWS)) {
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }
                        } else if (container.getTopBottomType().equals(ContainerConstants.TOP_BOTTOM_TYPE_BOTTOM_ROWS)) {
                            if (container.getTopBottomMode().equals(ContainerConstants.TOP_BOTTOM_MODE_PERCENTWISE)) {
                                rowSequence = retObj.findTopBottomPercentWise(sortCols, sortTypes, topbottomCount);
                            } else {
                                rowSequence = retObj.findTopBottom(sortCols, sortTypes, topbottomCount);
                            }
                        }
                    } else {
                        rowSequence = container.getRetObj().sortDataSet(sortCols, sortTypes, sortDataTypes);
                    }
                    retObj.setViewSequence(rowSequence);
                } else {
                    ArrayList tableMeasure = container.getTableMeasure();
                    if (tableMeasure != null) {
                        ArrayList sortColumn = new ArrayList();
                        sortColumn.add("A_" + tableMeasure.get(0).toString());
                        char[] sortType = new char[1];//new String[1];
                        sortType[0] = ' ';
                        char[] sortdataType = new char[1];
                        sortdataType[0] = 'N';
                        container.getRetObj().setViewSequence(container.getRetObj().sortDataSet(sortColumn, sortType, sortdataType));
                    }
                }
            }
        }
        //}
        ArrayList orginalcolumns = new ArrayList();
        orginalcolumns = container.getOriginalColumns();

        PbGraphDisplay GraphDisplay = new PbGraphDisplay();
        GraphDisplay.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
//                                            GraphDisplay.setProGenImgPath(ProGenImgPath);
        ProgenDataSet recordsRetObj = container.getRetObj();
        GraphDisplay.setCurrentDispRetObjRecords(recordsRetObj);//works 4 fx
        if (rowSequence.isEmpty()) {
            rowSequence = recordsRetObj.getViewSequence();
        }
        // GraphDisplay.setCurrentDispRetObjRecords(container.getDisplayedSetRetObj());//works 4 jfree
        //   GraphDisplay.setCurrentDispRecordsRetObjWithGT(container.getDisplayedSetRetObjWithGT());
        GraphDisplay.setAllDispRecordsRetObj(recordsRetObj);
        GraphDisplay.setNoOfDays(container.getNoOfDays());
        //added by santhosh.k on 01-03-2010 for reading info of entire dataset
        GraphDisplay.setColumnAverages(recordsRetObj.getColumnAverages());
        GraphDisplay.setColumnGrandTotals(recordsRetObj.getColumnGrandTotals());
        GraphDisplay.setColumnOverAllMinimums(recordsRetObj.getColumnOverAllMinimums());
        GraphDisplay.setColumnOverAllMaximums(recordsRetObj.getColumnOverAllMaximums());
        GraphDisplay.setResolution(Integer.parseInt(String.valueOf(session.getAttribute("screenwidth"))));
        //
        //GraphDisplay.setPiChartMeasurelabel(container.getTableMeasureNames().get(0).toString());
        // for setting graph color as per the theme
        String themeColor = "blue";
        if (session.getAttribute("theme") == null) {
            session.setAttribute("theme", themeColor);
        } else {
            themeColor = String.valueOf(session.getAttribute("theme"));
        }
        if (themeColor != null) {
            GraphDisplay.setUITheme((String) session.getAttribute("theme"));
        }
        //
        String c_img_separator = request.getContextPath() + "/TableDisplay/Images/separator.gif";
        ArrayList dataTypes = container.getDataTypes();
        ArrayList originalCols = container.getOriginalColumns();
        ArrayList mesLabel = container.getDisplayLabels();
        ArrayList<String> orginalMes = new ArrayList<String>();
        ArrayList<String> taMeas = container.getDisplayColumns();
        ArrayList<String> tableMeas = new ArrayList<String>();
        if (taMeas != null && mesLabel != null) {
            for (int m = 0; m < taMeas.size(); m++) {
                tableMeas.add(taMeas.get(m));
            }
            for (int n = 0; n < mesLabel.size(); n++) {
                orginalMes.add(mesLabel.get(n).toString());
            }
        }
        ArrayList tableMeasureNames = container.getTableMeasureNames();
        String[] dispHeaders = null;
        dispHeaders = (String[]) originalCols.toArray(new String[0]);
        ArrayList viewByColNames = container.getViewByColNames();
        ArrayList viewByElementIds = container.getViewByElementIds();
        int start = viewByElementIds.size();
        String TableId = null;
        String grpheight = null;
        String crosstabGrpElementIdQry = "select GRAPH_ID,ELEMENT_ID from PRG_AR_GRAPH_DETAILS where GRAPH_ID in(select GRAPH_ID from PRG_AR_GRAPH_MASTER where REPORT_ID=" + reportId + ")";
//                                            retObj = pbdb.execSelectSQL(crosstabGrpElementIdQry);
//                                            String crosstabGraphColumnMeasure = "";
//                                            if (retObj.getRowCount() > 0) {
//                                                crosstabGraphColumnMeasure = String.valueOf(container.getReportCollect().getNonViewByMap().get("A_" + retObj.getFieldValueString(0, "ELEMENT_ID")));
//                                            }
        // GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        ViewBys = (String[]) viewByElementIds.toArray(new String[0]);
        for (int viewIndex = 0; viewIndex < ViewBys.length; viewIndex++) {
            if (ViewBys[viewIndex].equalsIgnoreCase("Time")) {
                ViewBys[viewIndex] = ViewBys[viewIndex];
            } else {
                if (ViewBys[viewIndex].contains("A_")) {
                    ViewBys[viewIndex] = ViewBys[viewIndex];
                } else {
                    ViewBys[viewIndex] = "A_" + ViewBys[viewIndex];
                }
            }
        }
        GraphDisplay.setViewByColNames((String[]) viewByColNames.toArray(new String[0]));
        GraphDisplay.setViewByElementIds(ViewBys);
        GraphDisplay.setCtxPath(request.getContextPath());
        GraphDisplay.setTimelevel(container.getTimeLevel());

        ArrayList links = container.getLinks();
        if (links != null && links.size() != 0) {
            GraphDisplay.setJscal(String.valueOf(links.get(0)));
        } else {
            GraphDisplay.setJscal("reportViewer.do?reportBy=viewReport&REPORTID=" + reportId + "&");
        }
        GraphDisplay.setSession(request.getSession(false));
        GraphDisplay.setResponse(response);
//                                            GraphDisplay.setOut(out);
        GraphDisplay.setReportId(reportId);

        HashMap[] graphMapDetails = container.getGraphMapDetails();

        GraphDisplay.setGraphHashMap(container.getGraphHashMap());
        GraphDisplay.setGraphMapDetails(graphMapDetails);
        GraphDisplay.setIsCrosstab(container.isReportCrosstab());
        GraphDisplay.setSortColumns(container.getSortColumns());


        if (Integer.parseInt(container.getColumnViewByCount()) != 0) {
            //grpDetails = GraphDisplay.get2dGraphHeaders(barChartColumnNames, barChartColumnTitles, viewbysTemp,container);
            grpDetails = GraphDisplay.get2dGraphHeaders(container);
        } else {
            grpDetails = GraphDisplay.getGraphHeaders(container.getNoOfDays(), container);
        }
        container.setGraphHashMap(GraphDisplay.getGraphHashMap());
        //code to change graph types

        String selectedgraphtype1 = null;
        String graphTypename = null;
        String graphTypeid = null;
        String graphid = null;
        int measureRound = 0;
        String nbrFormat = "";
        String graphId = "";
        String[] grphIds;
        JqplotGraphProperty graphproperty = new JqplotGraphProperty();
        PbReportViewerDAO reportViewerdao = new PbReportViewerDAO();
        ArrayList graphDetails = new ArrayList();
        boolean testgraph = false;
        graphDetails = reportViewerdao.getGraphDetails(reportId);
        if (!graphDetails.isEmpty()) {
            selectedgraphtype1 = "jq";
            graphTypename = graphDetails.get(0).toString();
            graphTypeid = graphDetails.get(1).toString();
            graphid = graphDetails.get(2).toString();
            testgraph = true;
        }

        ArrayList rowviewlist = new ArrayList();
        int graphrowcount = 10;
        int actualrowcount = 0;
        actualrowcount = recordsRetObj.getViewSequence().size();
        graphrowcount += container.getFromRow();
        if (actualrowcount < graphrowcount) {
            graphrowcount = actualrowcount;
        }
        HashMap singleGraphDetails = null;
        HashMap GraphHashMap = null;
        ArrayList graphlist = new ArrayList();
        ArrayList columnnames = new ArrayList();
        String[] PrevbarChartColumnNames = null;
        String[] PrevbarChartColumnTitles = null;
        String[] viewByColumns = null;
        columnnames.add("View By");
        GraphHashMap = container.getGraphHashMap();
        singleGraphDetails = (HashMap) GraphHashMap.get(graphid);

        if (singleGraphDetails != null) {
            PrevbarChartColumnNames = (String[]) singleGraphDetails.get("barChartColumnNames");
            PrevbarChartColumnTitles = (String[]) singleGraphDetails.get("barChartColumnTitles");
            viewByColumns = (String[]) singleGraphDetails.get("viewByElementIds");
            for (int h = viewByColumns.length; h < PrevbarChartColumnNames.length; h++) {
                graphlist.add(PrevbarChartColumnNames[h]);
                columnnames.add(PrevbarChartColumnTitles[h]);
            }
        }
        if (recordsRetObj != null && recordsRetObj.rowCount > 0) {
            for (int a = 0; a < graphrowcount; a++) {
                if (container.getDataTypes().get(0).equals("D")) {
                    if (recordsRetObj.getFieldValueDateString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)).contains("\"")) {
                        rowviewlist.add(recordsRetObj.getFieldValueDateString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)));
                    } else {
                        rowviewlist.add(recordsRetObj.getFieldValueDateString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)));
                    }
                } else {
                    if (recordsRetObj.getFieldValueDateString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)).contains("\"")) {
                        rowviewlist.add(recordsRetObj.getFieldValueString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)));
                    } else {
                        rowviewlist.add(recordsRetObj.getFieldValueString(Integer.parseInt(rowSequence.get(a).toString()), (String) orginalcolumns.get(0)));
                    }

                }
            }
        }
        ArrayList graphcolumns = new ArrayList();
        graphcolumns.addAll(columnnames);

        for (int k = container.getFromRow(); k < graphrowcount; k++) {
            graphcolumns.add(rowviewlist.get(k));
            for (int f = 0; f < graphlist.size(); f++) {
                String MeasureValue = recordsRetObj.getFieldValueString(Integer.parseInt(rowSequence.get(k).toString()), (String) graphlist.get(f));
                if (MeasureValue == null || MeasureValue.isEmpty() || MeasureValue.equalsIgnoreCase("")) {
                    MeasureValue = "0.0";
                }
                BigDecimal bd = BigDecimal.ZERO;
                if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(f))) {
                    bd = recordsRetObj.getFieldValueRuntimeMeasure(Integer.parseInt(rowSequence.get(k).toString()), (String) graphlist.get(f));
                } else {
                    bd = new BigDecimal(MeasureValue);
                }
                graphcolumns.add(NumberFormatter.getModifiedNumberFormat(bd, nbrFormat, measureRound).replaceAll(",", ""));
//                            if (RTMeasureElement.isRunTimeMeasure((String) graphlist.get(f)))
//                                minvalue=0;
//                            else{
//                            if (MeasureValue.contains("-")) {
//                                minvalue = -1;
//                            } else {
//                                minvalue = 0;
//                            }
//                        }
            }
        }
        HashMap<String, ArrayList> hashMap = new HashMap<String, ArrayList>();
        ArrayList values = new ArrayList();
//
        if (testgraph) {
//                                              session.removeAttribute("graphData");
//                                              session.removeAttribute("graphHeaders");
//                                              session.removeAttribute("reportId");
//                                              session.removeAttribute("repName");
            session.setAttribute("graphData", graphcolumns);
//                                              session.setAttribute("reportId", reportId);
//                                              session.setAttribute("repName", container.getReportDesc());
//                                              session.setAttribute("graphHeaders", columnnames);
            values.add(reportId);
            values.add(container.getReportDesc());
            hashMap.put("graphHeaders", graphcolumns);
            hashMap.put("graphData", columnnames);
            hashMap.put("repNameandId", values);

            hashMap.put("graphDetails", graphDetails);
        }
        return hashMap;
    }
}
