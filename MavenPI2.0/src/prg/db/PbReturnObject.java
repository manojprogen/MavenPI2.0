package prg.db;

import com.google.common.collect.ArrayListMultimap;
import com.progen.db.ProgenDataSet;
import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import com.progen.query.RunTimeMeasure;
import com.progen.report.ImportExcelDetail;
import com.progen.report.data.DataFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

public class PbReturnObject extends ProgenDataSet implements Serializable, Cloneable {

    public static Logger logger = Logger.getLogger(PbReturnObject.class);
    public String[] cols = null;
    /////////New Values to be supplied
    public ArrayList rowViewBys;
    public ArrayList ColViewBys;
    public int totalViewBys;
    public int totalOrderbys;
    public int rowViewCount;
    public int colViewCount;
    public HashMap nonViewInput;
    public ArrayList Qrycolumns;
    public boolean meausreOnCol;
    public int MeasurePos;
    ArrayList<Integer>[] finalColSpanList;
    public ArrayListMultimap<Integer, Integer> colSpanMap = ArrayListMultimap.create();
    ArrayList isSubTotalReq = new ArrayList();
    int colGenerator = 1;
    ArrayList[] rowViewValues = new ArrayList[rowViewCount];
    ArrayList[] rowViewSortedValues = new ArrayList[rowViewCount];
    String[] rowViewSortedValueDataType = new String[rowViewCount];
    ArrayList<Integer> rowViewSortedValueDataTypeInt = new ArrayList<Integer>();
    ArrayList[] rowViewSortValues = new ArrayList[rowViewCount];
    ArrayList[] colViewValues = new ArrayList[colViewCount];
    ArrayList[] colViewSortedValues = new ArrayList[colViewCount];
    public ArrayList[] finalColViewSortedValues = new ArrayList[colViewCount + 1];
    public ArrayList[] finalColViewSortedValues1 = new ArrayList[colViewCount + 1];
    ArrayList[] colViewSortValues = new ArrayList[colViewCount];
    HashMap nonViewByMap = new LinkedHashMap();//
    public HashMap<String, ArrayList> nonViewByMapNew = new LinkedHashMap();//
    public HashMap<String, ArrayList> nonViewByMapNew1 = new LinkedHashMap();//
    ArrayList<String> queryColName = new ArrayList();
    ArrayList<String> colViewSortedValues1 = new ArrayList();
    ArrayList<String> queryColName1 = new ArrayList();
    ArrayList<String> queryMeasureName = new ArrayList();
    ArrayList<String> finalQueryMeasureName = new ArrayList();
    ArrayList<String> finalColumnTypes = new ArrayList();
    ArrayList<Integer> finalColumnTypesInt = new ArrayList<Integer>();
    ArrayList<Integer> finalColumnSizes = new ArrayList<Integer>();
    ArrayList colValidList = new ArrayList();
    ArrayList newcolValidList = new ArrayList();
    ArrayList rowValidList = new ArrayList();
    ArrayList combineValidList = new ArrayList();
    ArrayList[] GTCol;
    ArrayList[][] SubTCol;
    HashMap[] subPosition;
    private static final long serialVersionUID = 7444798900426588179L;
    HashMap<String, Integer> combinedHash = new HashMap();
    ArrayList<Integer> finalQryPositions = new ArrayList<Integer>();
    int totalColBefore;
    public String gtType = "";// FIRST, LAST
    public String subGtType = ""; // ALLFIRST , ALLLAST, BEFORE, AFTER
    ArrayList<Integer> colSpanCurrPos = new ArrayList();
    ArrayList<Integer> colSpanBefPos = new ArrayList();
    ArrayList<Integer> colSpanCurrindex = new ArrayList();
    LinkedHashMap<String, ArrayList> subTotals = new LinkedHashMap<String, ArrayList>();
    int totalColFilled = 0;
    public ArrayList CrossTabfinalOrder = new ArrayList();
    ArrayList oColumnList;
    public boolean isGTNone = false;
    public boolean isSTNone = false;
    ArrayList GTColName = new ArrayList();
    public String GTDisplayName = "Grand Total";
//             private String[] crosstabelements=null;
    public Map< String, String[]> crosstabelements = new HashMap<String, String[]>();
    private String elementaction = null;
    private HttpServletRequest request1 = null;
    // code written by swati
    public HashMap<String, String> finalCrossTabReportDrillMap = new HashMap<String, String>();
    public HashMap<String, String> reportDrillMap = new HashMap<String, String>();
    public boolean summarizedMeasuresEnabled = false;
    public HashMap<String, ArrayList<String>> summerizedTableHashMap = new HashMap<String, ArrayList<String>>();
    public HashMap<String, String> reportDrillMaptooltip = new HashMap<String, String>();  //added by krishan
    public PbReturnObject summerizedMsrRetObj = null;
    public HashMap<String, String> crosstabMeasureId = new HashMap<String, String>();
    public HashMap<String, String> crosstabmeasureIdsmap = new HashMap<String, String>();//added by veena
    public ArrayList<String> crosstablist = new ArrayList<String>();//added by veena
    public int lLoop1 = 0;
    public HashMap<String, String> crosstabMsrMap = new HashMap<String, String>();
    public HashMap<String, String> MsrAggregationMap = new HashMap<String, String>();
    public PbReturnObject importExcelRetObj = null;
    public ImportExcelDetail importExcelDeatil = null;
    public BigDecimal[] val = null;//bhargavi
    public PbReturnObject zerocntretobj = null;
    public ArrayList<Integer> zerocntmsr = new ArrayList<Integer>();
    public Map<String, String> gtCTAvgType = new HashMap<String, String>();
    public ArrayList<String> newRowViewSortedValues = new ArrayList<String>();

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            logger.error("Exception:", ex);;
            return this;
        }
    }

    public void addToHashAndArry(int ColOrder, int coli, int mPos, String newColadd, PbReturnObject newCrossPb) {
        ArrayList a = new ArrayList();
        boolean addStTest = true;
        for (int iloop = 0; iloop <= colViewCount; iloop++) {
            if (iloop <= ColOrder) {

                if (iloop == MeasurePos) {
                    a.add(nonViewInput.get("A_" + Qrycolumns.get(mPos).toString()).toString());
                } else {
                    a.add(finalColViewSortedValues[iloop].get(coli));
                }
                //  finalColViewSortedValues[iloop].add(a);//need to revide final col at the last
            } else if (iloop == MeasurePos) {
                a.add(nonViewInput.get("A_" + Qrycolumns.get(mPos).toString()).toString());
                // finalColViewSortedValues[iloop].add(nonViewInput.get("A_"+Qrycolumns.get(mPos).toString()).toString());
            } else {
                if (addStTest) {
                    a.add("Sub total");
                    addStTest = false;
                } else {
                    a.add("");
                }
                // finalColViewSortedValues[iloop].add("");
            }

        }
        int finalColPos = 0;
        if (subGtType.equalsIgnoreCase("BEFORE")) {
            finalColPos = colSpanBefPos.get(ColOrder);

        } else {
            finalColPos = coli;
        }
        finalColPos = finalColPos + rowViewCount;
        nonViewByMapNew.put(newColadd, a);
        if (subPosition[ColOrder] != null && subPosition[ColOrder].get(newCrossPb.cols[finalColPos]) != null) {
            HashMap h = (HashMap) subPosition[ColOrder].get(newCrossPb.cols[finalColPos]);
            h.put(mPos, newColadd);
            subPosition[ColOrder].put(newCrossPb.cols[finalColPos], h);

        } else {
            HashMap h = new HashMap();
            h.put(mPos, newColadd);
            subPosition[ColOrder].put(newCrossPb.cols[finalColPos], h);
        }

    }

    public PbReturnObject() {
    }

    public PbReturnObject(boolean processGT) {
        this.processGT = processGT;
    }

    public PbReturnObject(ResultSet rset) {
        prepareObject(rset);
    }

    public PbReturnObject(ResultSet rset, boolean processGT) {
        //this.rset = rset;
        this.processGT = processGT;
        prepareObject(rset);
    }

    public PbReturnObject transposeReturnObject() {
        PbReturnObject newCrossPb = new PbReturnObject();
        if (rowViewCount == 0) {
            rowViewBys.add("Measure");
        }
        //code written by swathi purpose of summerizedmsrs
        ArrayList<String> summerizedQryeIds = new ArrayList<String>();
        ArrayList<String> summerizedQryAggregations = new ArrayList<String>();
        ArrayList<String> summerizedQryColNames = new ArrayList<String>();
        ArrayList<String> summerizedQryColTypes = new ArrayList<String>();
        if (this.summarizedMeasuresEnabled) {
            if (summerizedTableHashMap != null && summerizedTableHashMap.get("summerizedQryeIds") != null) {
                summerizedQryeIds.addAll((List<String>) summerizedTableHashMap.get("summerizedQryeIds"));
                summerizedQryAggregations.addAll((List<String>) summerizedTableHashMap.get("summerizedQryAggregations"));
                summerizedQryColNames.addAll((List<String>) summerizedTableHashMap.get("summerizedQryColNames"));
                summerizedQryColTypes.addAll((List<String>) summerizedTableHashMap.get("summerizedQryColTypes"));
            }
        }

//            //start of code by Nazneen for Union from both Return object
//             ArrayList<String> allQryeIds=new ArrayList<String>();
//            for(int i=0;i<rowViewCount;i++){
//                allQryeIds.add(this.getFieldValueString(i,0));
//            }
//            for(int i=0;i<summerizedMsrRetObj.rowCount;i++){
//                String crossTabId = summerizedMsrRetObj.getFieldValueString(i, 0);
//                if(!allQryeIds.contains(crossTabId)){
//                    allQryeIds.add(summerizedMsrRetObj.getFieldValueString(i, 0));
//                }
//            }
//             int newRowViewCount = allQryeIds.size();
//              //end of code by Nazneen for Union from both Return object
        rowViewValues = new ArrayList[rowViewCount];
        rowViewSortedValues = new ArrayList[rowViewCount];
        rowViewSortedValueDataType = new String[rowViewCount];
        rowViewSortValues = new ArrayList[rowViewCount];
        colViewValues = new ArrayList[colViewCount];
        subPosition = new HashMap[colViewCount];
        colViewSortedValues = new ArrayList[colViewCount];
        colViewSortValues = new ArrayList[colViewCount];
        finalColSpanList = new ArrayList[colViewCount + 1];
        GTCol = new ArrayList[Qrycolumns.size()];
        SubTCol = new ArrayList[colViewCount][Qrycolumns.size()];
        finalColViewSortedValues = new ArrayList[colViewCount + 1];
        finalColViewSortedValues1 = new ArrayList[colViewCount + 1];
        ArrayList<String> newColumnTypes = new ArrayList();
        ArrayList<Integer> newColumnTypesInt = new ArrayList<Integer>();
        ArrayList<Integer> newColumnSizes = new ArrayList<Integer>();
        ArrayList<Integer> newQryPositions = new ArrayList<Integer>();
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            rowViewValues[l1] = new ArrayList();
            rowViewSortedValues[l1] = new ArrayList();
            rowViewSortValues[l1] = new ArrayList();

        }
        for (int l1 = 0; l1 < colViewCount; l1++) {

            colViewValues[l1] = new ArrayList();
            colViewSortedValues[l1] = new ArrayList();
            colViewSortValues[l1] = new ArrayList();
            subPosition[l1] = new HashMap<String, HashMap>();
        }
        for (int l1 = 0; l1 < colViewCount + 1; l1++) {

            finalColSpanList[l1] = new ArrayList();
            finalColViewSortedValues[l1] = new ArrayList();
            finalColViewSortedValues1[l1] = new ArrayList();
        }

        totalColBefore = rowViewCount * 2 + colViewCount * 2;  //with there sort cols
        int colRepeat[] = new int[colViewCount];

        for (int recCount = 0; recCount < this.rowCount; recCount++) {
            String rowKey = "";
            String colKey = "";
            String totalKey = "";

            //Code to get Keys
            for (int colT = 0; colT < totalViewBys; colT++) {
                if (colT < rowViewCount) {
                    rowKey += ";;" + this.getFieldValueString(recCount, colT);
                    //
                    rowViewValues[colT].add(this.getFieldValueString(recCount, colT));
                    rowViewSortValues[colT].add(this.getFieldValueString(recCount, colT + totalViewBys));
                    rowViewSortedValueDataType[colT] = this.columnTypes[colT + totalViewBys];
                    rowViewSortedValueDataTypeInt.add(this.columnTypesInt[colT + totalViewBys]);
                }
                if (colT >= rowViewCount && colT < totalViewBys) {
                    colKey += ";;" + this.getFieldValueString(recCount, colT);
                    colViewValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT));
                    colViewSortValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT + totalViewBys));
                }
                totalKey += ";;" + this.getFieldValueString(recCount, colT);
            }
            if (rowValidList == null || (!rowValidList.contains(rowKey))) {
                rowValidList.add(rowKey);
                {
                    for (int colT = 0; colT < rowViewCount; colT++) {
                        if (colT < rowViewCount) {
                            rowViewSortedValues[colT].add(this.getFieldValueString(recCount, colT));
                        }
                    }
                }
            }

            String s = null;
            if (colValidList == null || (!colValidList.contains(colKey))) {
                colValidList.add(colKey);
                {
                    for (int colT = rowViewCount; colT < totalViewBys; colT++) {
                        if (colT >= rowViewCount && colT < totalViewBys) {
                            colViewSortedValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT));
                        }
                    }

                }
            }
            String k = null;
            if (combineValidList == null || (!combineValidList.contains(totalKey))) {
                combineValidList.add(totalKey);
            }
            combinedHash.put(totalKey, recCount);

        }
        ///////Sort Code
                /*
         * As we have selected distinct values and code is sorted internally as
         * of now avoiding the sort code sort should be done on sort col and
         * according the other array should be adjusted
         */
        String g = null;
        if (this.getCrosstabelements(ColViewBys.get(0).toString()) != null) {
            for (int colT = rowViewCount; colT < totalViewBys; colT++) {
                if (colT >= rowViewCount && colT < totalViewBys) {
                    colViewSortedValues[colT - rowViewCount].clear();
                }
            }

            ArrayList colViewSortedValues1 = new ArrayList();
            String crosstabelement[] = this.getCrosstabelements(ColViewBys.get(0).toString());
            for (int colT = 0; colT < crosstabelement.length; colT++) {
                colViewSortedValues1.add(crosstabelement[colT]);
            }
            colViewSortedValues[0].addAll(colViewSortedValues1);
        }

//               if(this.getSession()!=null && this.getSession().getAttribute("ReportId")!=null)
//               {
//                     if(this.getSession()!=null)
//                     {
//
//                   String Elementurl=(String)this.getSession().getAttribute("ReportId");
//                   
//                   String action=(String)this.getSession().getAttribute("iscrosstabrep");
//                   
//                   if(action.equalsIgnoreCase("yes"))
//                   {
//                   String crosstabelement[]=Elementurl.split(",");
//                     for (int colT = rowViewCount; colT < totalViewBys; colT++) {
//                        if (colT >= rowViewCount && colT < totalViewBys) {
//                            colViewSortedValues[colT - rowViewCount].clear();
//                        }
//                    }
//               }
//                     }
//               }
//            String [] cols1={"Aug-2011","Jun-2011","Jul-2011","Apr-2011","May-2011","Sep-2011","Oct-2011"};
//
//                 for(int l2=0;l2<colViewSortedValues.length; l2++){
//            
//
//           }
//           
//           
//           
//           
        /////////Same code as Cross Tab Query
        String[] tempName = new String[colViewCount];
        String[] tempName1 = new String[colViewCount];
        ArrayList<Integer>[] colSpanList = new ArrayList[colViewCount];

        //finalColSpanList1= new ArrayList[colViewCount];
        ArrayList<String>[] colTotalQuery = new ArrayList[colViewCount];
        ArrayList<ArrayList>[] colTotalQueryTitles = new ArrayList[colViewCount];
        String[] oldValList = new String[colViewCount];
        String[] newValList = new String[colViewCount];
        ArrayList<Integer>[] nextBreakList = new ArrayList[colViewCount - 1];

        boolean[] breakChild = new boolean[colViewCount];
        int[] collectVal = new int[colViewCount];
        int count = 0;

        for (int l1 = 0; l1 < colViewCount; l1++) {
            colSpanList[l1] = new ArrayList<Integer>();

            oldValList[l1] = "";
            newValList[l1] = "";
            breakChild[l1] = false;
            collectVal[l1] = 1;
            colTotalQuery[l1] = new ArrayList<String>();
            colTotalQueryTitles[l1] = new ArrayList<ArrayList>();
            if (l1 < colViewCount - 1) {
                nextBreakList[l1] = new ArrayList<Integer>();
            }
        }

        for (int lLoop = 0; lLoop < colViewSortedValues[0].size(); lLoop++) {

            count++;

            for (int l1 = 0; l1 < colViewCount; l1++) {
                tempName[l1] = colViewSortedValues[l1].get(lLoop).toString();
               if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (colSpanList[l1] == null || colSpanList[l1].size() <= 1) {
                        colSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        colSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        //
                        breakChild[l1 - 1] = false;
                        //
                    }
                }
                String totalClause = "";
                if (breakChild[l1] || lLoop == 1) {
                    colTotalQuery[l1].add(colViewSortedValues[l1].get(lLoop).toString());
                }

            }

            //
        }/// End of resultset while loop
//                for (int lLoop=0;lLoop<rowViewSortedValues[0].size();lLoop++) {
//                    Collections.sort(rowViewSortedValues[0]);
//                }

        for (int l1 = 0; l1 < colViewCount; l1++) {
            {
                int j = ((collectVal[l1]));
                colSpanList[l1].add(j);
            }
            //  //
            //    //

        }
        oColumnList = new ArrayList();
        oColumnList.addAll(Arrays.asList(this.getColumnNames()));

        ArrayList MeasureSpan = new ArrayList();
        for (int l1 = 0; l1 < Qrycolumns.size(); l1++) {//Use as dummy Loop
            MeasureSpan.add(colViewSortedValues[0].size());
            if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(l1).toString())) {
                queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(l1).toString()).toString());
                queryColName1.add(crosstabMsrMap.get("A_" + Qrycolumns.get(l1).toString()).toString());
            } else {
                queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(l1).toString()).toString());
                queryColName1.add(nonViewInput.get("A_" + Qrycolumns.get(l1).toString()).toString());
            }
            queryMeasureName.add("A_" + Qrycolumns.get(l1).toString());
            newColumnSizes.add(this.columnSizes[l1 + totalColBefore]);

//code added by Bhargavi Parsi on 7th Sep 2015
            int index = -1;
            for (int m = 0; m < cols.length; m++) {
                if (cols[m].equals("A_" + Qrycolumns.get(l1))) {
                    index = m;
                    break;
                }
            }

//                    newColumnTypes.add(this.columnTypes[l1+totalColBefore]);
//                    newColumnTypesInt.add(this.columnTypesInt[l1+totalColBefore]);
            newColumnTypes.add(this.columnTypes[index]);
            newColumnTypesInt.add(this.columnTypesInt[index]);
            //end of code by Bhargavci Parsi

            newQryPositions.add(l1 + totalColBefore);

        }
        int totalColSize = colViewCount - 1;
        if (meausreOnCol) {
            totalColSize = colViewCount;
        }
        ///
        for (int iloop = 0; iloop <= totalColSize; iloop++) {
            if (iloop == 0 && totalColSize <= 1) {
                isSubTotalReq.add("N");
            } else if (MeasurePos == iloop) {
                isSubTotalReq.add("N");
            } else if (MeasurePos < iloop && iloop != totalColSize) {
                isSubTotalReq.add("T");
            } else if (iloop < MeasurePos && iloop != totalColSize - 1) {
                isSubTotalReq.add("T");
            } else {
                isSubTotalReq.add("N");
            }
        }
        // 
        for (int l1 = 0, sList = 0; l1 <= totalColSize; l1++) {//Use as dummy Loop
            ///

            if (MeasurePos == l1) {
                sList = 1;
//                        finalColSpanList;
//                        finalColViewSortedValues;
                for (int j = 0; j < MeasureSpan.size(); j++) {
                    //
                    for (int i = l1; i <= colViewSortedValues.length; i++) {
                        for (int k = 0; k < Integer.parseInt(MeasureSpan.get(j).toString()); k++) {
                            if (i == l1) {
                                if (queryColName.isEmpty()) {
                                    for (int l11 = 0; l11 < Qrycolumns.size(); l11++) {//Use as dummy Loop
                                        MeasureSpan.add(colViewSortedValues[0].size());
                                        if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(l11).toString())) {
                                            queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(l11).toString()).toString());
                                        } else {
                                            queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(l11).toString()).toString());
                                        }
                                        queryMeasureName.add("A_" + Qrycolumns.get(l11).toString());
                                        newColumnSizes.add(this.columnSizes[l11 + totalColBefore]);

                                        //code added by Bhargavi Parsi on 7th Sep 2015
                                        int index = -1;
                                        for (int m = 0; m < cols.length; m++) {
                                            if (cols[m].equals("A_" + Qrycolumns.get(l11))) {
                                                index = m;
                                                break;
                                            }
                                        }
                                        //   newColumnTypes.add(this.columnTypes[l11+totalColBefore]);
                                        // newColumnTypesInt.add(this.columnTypesInt[l11+totalColBefore]);
                                        newColumnTypes.add(this.columnTypes[index]);
                                        newColumnTypesInt.add(this.columnTypesInt[index]);
                                        //end of code by Bhargavci Parsi

                                        newQryPositions.add(l11 + totalColBefore);

                                    }
                                }
//                                        
                                finalColViewSortedValues[i].add(queryColName.get(j));
                                if (!colViewSortedValues1.isEmpty()) {
                                    finalColViewSortedValues1[i].add(colViewSortedValues1.get(j));
                                }
                                finalQueryMeasureName.add(queryMeasureName.get(j));
                                finalColumnSizes.add(newColumnSizes.get(j));
                                finalColumnTypes.add(newColumnTypes.get(j));
                                finalColumnTypesInt.add(newColumnTypesInt.get(j));
                                finalQryPositions.add(newQryPositions.get(j));
                                //newQryPositions.add(oColumnList.indexOf("A_"+Qrycolumns.get(j).toString()));
                            }
                            if (i < colViewCount) {
                                finalColViewSortedValues[i + sList].add(colViewSortedValues[i].get(k));
                            }
                        }

                    }

                }
                break;
            } else {
                MeasureSpan = new ArrayList();
                queryColName = new ArrayList();
                colViewSortedValues1 = new ArrayList();
                newQryPositions = new ArrayList<Integer>();
                queryMeasureName = new ArrayList();
                ArrayList newList = new ArrayList();
                newColumnTypes = new ArrayList<String>();
                newColumnTypesInt = new ArrayList<Integer>();
                newColumnSizes = new ArrayList<Integer>();
                for (int k = 0; k < colViewSortedValues[l1].size(); k++) {
                    for (int j = 0; j < Qrycolumns.size(); j++) {
                        if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(j).toString())) {
                            queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(j).toString()).toString());
                        } else {
                            queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(j).toString()).toString());
                        }
                        queryMeasureName.add("A_" + Qrycolumns.get(j).toString());
                        newColumnSizes.add(this.columnSizes[j + totalColBefore]);

                        int index = -1;
                        for (int m = 0; m < cols.length; m++) {
                            if (cols[m].equals("A_" + Qrycolumns.get(j))) {
                                index = m;
                                break;
                            }
                        }

//                                  newColumnTypes.add(this.columnTypes[j+totalColBefore]);
//                                  newColumnTypesInt.add(this.columnTypesInt[j+totalColBefore]);
                        newColumnTypes.add(this.columnTypes[index]);
                        newColumnTypesInt.add(this.columnTypesInt[index]);
                        //newQryPositions.add(j+totalColBefore);
                        newQryPositions.add(oColumnList.indexOf("A_" + Qrycolumns.get(j).toString()));
                        finalColViewSortedValues[l1].add(colViewSortedValues[l1].get(k));
                    }

                }
                for (int k = 0; k < Qrycolumns.size(); k++) {
                    for (int j = 0; j < colViewSortedValues[l1].size(); j++) {
                        if (crosstabMsrMap != null) {
                            colViewSortedValues1.add(colViewSortedValues[l1].get(j).toString());
                        } else {
                            colViewSortedValues1.add(nonViewInput.get("A_" + Qrycolumns.get(j).toString()).toString());
                        }

                        //finalColViewSortedValues[l1].add(colViewSortedValues[l1].get(k));
                        finalColViewSortedValues1[l1].add(queryColName1.get(k));
                    }

                }
                for (int k = 0; k < colSpanList[l1].size(); k++) {
                    newList.add(Integer.parseInt(colSpanList[l1].get(k).toString()) * Qrycolumns.size());
                    for (int j = 0; j < Qrycolumns.size(); j++) {
                        MeasureSpan.add((colSpanList[l1].get(k).toString()));
                    }

                }
                colSpanList[l1] = newList;

                //
            }

        }

//           for(int l1=0;l1<=totalColSize; l1++){
//              // 
//              // 
//               if(l1<colViewCount){
//                  // 
//               }
//           }
        //Processing for hashMap and finalColspan
        tempName = new String[colViewCount + 1];
        tempName1 = new String[colViewCount + 1];
        oldValList = new String[colViewCount + 1];
        newValList = new String[colViewCount + 1];
        breakChild = new boolean[colViewCount + 1];
        collectVal = new int[colViewCount + 1];
        count = 0;
        //code written by swathi purpose of summerizedmsrs in CTReports
//           if(this.summarizedMeasuresEnabled){
//              String[] temp=new String[summerizedQryeIds.size()];
//            for(int i=0;i<summerizedQryeIds.size();i++){
//                ArrayList forViewMap = new ArrayList();
//                temp[i]=" "+","+summerizedQryColNames.get(i);
//                forViewMap.add(" "+","+summerizedQryColNames.get(i));
//                nonViewByMapNew.put("A_"+summerizedQryeIds.get(i),forViewMap);
//                CrossTabfinalOrder.add("A_"+summerizedQryeIds.get(i));
//            }
//           }
        //code ended
        if (summarizedMeasuresEnabled) {
            for (int i = 0; i < summerizedQryeIds.size(); i++) {
                if (reportDrillMap.get("A_" + summerizedQryeIds.get(i)) != null && !reportDrillMap.get("A_" + summerizedQryeIds.get(i)).isEmpty()) {
                    finalCrossTabReportDrillMap.put("A_" + summerizedQryeIds.get(i), reportDrillMap.get("A_" + summerizedQryeIds.get(i)));
                }
            }
        }
        for (int lLoop = 0; lLoop < finalColViewSortedValues[0].size(); lLoop++) {
            String colKey = "";

            count++;

            ArrayList forViewMap = new ArrayList();
            ArrayList forViewMap1 = new ArrayList();
            for (int l1 = 0; l1 <= totalColSize; l1++) {

                tempName[l1] = finalColViewSortedValues[l1].get(lLoop).toString();
                tempName1[l1] = finalColViewSortedValues1[l1].get(lLoop).toString();
                forViewMap.add(tempName[l1]);
                forViewMap1.add(tempName1[l1]);
                if (MeasurePos != l1) {
                    colKey += ";;" + tempName[l1];
                }
                if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (finalColSpanList[l1] == null || finalColSpanList[l1].size() <= 1) {
                        finalColSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        finalColSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        //
                        breakChild[l1 - 1] = false;
                        //
                    }
                }
                String totalClause = "";
//                               if (breakChild[l1] || lLoop == 1) {
//                                 colTotalQuery[l1].add(finalColViewSortedValues[l1].get(lLoop).toString());
//                               }

            }
            nonViewByMapNew.put("A" + colGenerator, forViewMap);
            nonViewByMapNew1.put("A" + colGenerator, forViewMap1);
            crosstabMeasureId.put("A" + colGenerator, finalQueryMeasureName.get(lLoop));
            if (!crosstablist.contains(finalQueryMeasureName.get(lLoop))) {
                crosstablist.add(finalQueryMeasureName.get(lLoop));

            }
            if (!crosstablist.isEmpty()) {
                if (lLoop1 < crosstablist.size()) {
                    crosstabmeasureIdsmap.put(crosstablist.get(lLoop1), "A" + colGenerator);
                    lLoop1++;
                }
            }
            CrossTabfinalOrder.add("A" + colGenerator);
            newcolValidList.add(colKey);
            //written by swati
            finalCrossTabReportDrillMap.put("A" + colGenerator, reportDrillMap.get(finalQueryMeasureName.get(lLoop)));
            colGenerator++;

            //New code for i
//                     i=i+ Qrycolumns.size();
            //
        }/// End of resultset while loop

        for (int l1 = 0; l1 <= totalColSize; l1++) {
            {
                int j = ((collectVal[l1]));
                finalColSpanList[l1].add(j);
            }

        }

        int addColumns = 0;
        if (!isGTNone) {
            addColumns = Qrycolumns.size();//For GT
        }
        if (!isSTNone) {
            for (int iloop = 0; iloop < finalColSpanList.length; iloop++) {
                // 
                if (isSubTotalReq.get(iloop).equals("T")) {
                    if (MeasurePos > iloop) {
                        addColumns = addColumns + (finalColSpanList[iloop].size() * Qrycolumns.size());
                    } else {
                        addColumns = addColumns + (finalColSpanList[iloop].size() * 1);
                    }

                    // 
                }
            }
        }

        String colKeys[] = (String[]) nonViewByMapNew.keySet().toArray(new String[0]);
        totalColFilled = rowViewCount + colKeys.length;//Amit comment for bug fic 28 jult 2011
        //totalColFilled=nonViewByMapNew.size()+colKeys.length;

        //code written by swati purpose of summerizedmsrs
        if (summarizedMeasuresEnabled) {
            totalColFilled = totalColFilled + summerizedQryeIds.size();
        }
        //code ended
        int finalsizeofArrays = totalColFilled + addColumns;
        ///Adding columns for sorting
        finalsizeofArrays = finalsizeofArrays + rowViewCount;

        newCrossPb.cols = new String[finalsizeofArrays];
        newCrossPb.columnTypes = new String[finalsizeofArrays];
        newCrossPb.columnTypesInt = new Integer[finalsizeofArrays];
        newCrossPb.columnSizes = new int[finalsizeofArrays];

        //start of code by Nazneen for Union from both Return object
        if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
            for (int recCount = 0; recCount < this.rowCount; recCount++) {
                String rowKeys = "";
                for (int colT = 0; colT < rowViewCount; colT++) {
                    rowKeys += ";;" + this.getFieldValueString(recCount, colT);
                }
                if (!newRowViewSortedValues.contains(rowKeys)) {
                    newRowViewSortedValues.add(rowKeys);
                }
            }
            for (int recCount = 0; recCount < summerizedMsrRetObj.rowCount; recCount++) {
                String rowKeys = "";
                for (int colT = 0; colT < rowViewCount; colT++) {
                    rowKeys += ";;" + summerizedMsrRetObj.getFieldValueString(recCount, colT);
                }
                if (!newRowViewSortedValues.contains(rowKeys)) {
                    newRowViewSortedValues.add(rowKeys);
                }
            }
        }

//                 if(summerizedMsrRetObj!=null && summerizedMsrRetObj.getRowCount()>0){
//                    for(int i=0;i<rowCount;i++){
//                         if (!newRowViewSortedValues.contains(this.getFieldValueString(i, 0))) {
//                            newRowViewSortedValues.add(this.getFieldValueString(i, 0));
//                        }
//                    }
//
//                    for (int i = 0; i < summerizedMsrRetObj.rowCount; i++) {
//                        String crossTabId = summerizedMsrRetObj.getFieldValueString(i, 0);
//                        if (!newRowViewSortedValues.contains(crossTabId)) {
//                            newRowViewSortedValues.add(summerizedMsrRetObj.getFieldValueString(i, 0));
//                        }
//                    }
//                    rowCount = newRowViewSortedValues.size();
//
//                }
        //end of code by Nazneen for Union from both Return object
        // 
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            // 
            newCrossPb.hMap.put("A_" + rowViewBys.get(l1), rowViewSortedValues[l1]);
            // 
            newCrossPb.cols[l1] = "A_" + rowViewBys.get(l1);
            newCrossPb.columnTypes[l1] = this.columnTypes[l1];
            newCrossPb.columnTypesInt[l1] = this.columnTypesInt[l1];
            newCrossPb.columnSizes[l1] = this.columnSizes[l1];
        }
//                    start of code by Nazneen for Union from both Return object
        ArrayList colValue1 = new ArrayList();
        ArrayList[] finalRowViewSortedValues = new ArrayList[newRowViewSortedValues.size()];
        finalRowViewSortedValues = new ArrayList[rowViewCount];
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            finalRowViewSortedValues[l1] = new ArrayList();
        }
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            for (int i = 0; i < rowViewSortedValues[0].size(); i++) {
                finalRowViewSortedValues[l1].add(i, rowViewSortedValues[l1].get(i));
            }
        }
        if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                    if(rowViewCount==1){
//                        for(int i=0;i<summerizedMsrRetObj.rowCount;i++){
//                            for(int j=0;j<rowViewCount;j++){
//                             String str= summerizedMsrRetObj.getFieldValueString(i,j);
//                                
//                             if(!rowViewSortedValues[j].contains(str)){
//                                  newCrossPb.hMap.put("A_"+rowViewBys.get(j),str);
//                             }
//                            }
//                        }

            for (int recCount = 0; recCount < summerizedMsrRetObj.rowCount; recCount++) {
                String rowKeys = "";
                boolean flag = false;
                String str = "";
                int size = rowViewSortedValues[0].size();
                int cnt = 0;
                for (int colT = 0; colT < rowViewCount; colT++) {
                    rowKeys += ";;" + summerizedMsrRetObj.getFieldValueString(recCount, colT);
                }
                for (int i = 0; i < size; i++) {
                    str = "";
                    for (int j = 0; j < rowViewCount; j++) {
                        str = str + ";;" + rowViewSortedValues[j].get(i);
                    }
                    if (!str.equals(rowKeys)) {
                        cnt++;
                    } else {
                        flag = true;
                        break;
                    }
                }

                if (!flag && cnt == size) {
                    rowKeys = rowKeys.substring(2);
                    String totalVal[] = rowKeys.split(";;");
                    for (int l1 = 0; l1 < rowViewCount; l1++) {
                        finalRowViewSortedValues[l1].add(totalVal[l1].toString());
                    }
                }

            }
            for (int l1 = 0; l1 < rowViewCount; l1++) {
                newCrossPb.hMap.put("A_" + rowViewBys.get(l1), finalRowViewSortedValues[l1]);
            }

//                        for(int i=0;i<newRowViewSortedValues.size();i++){
//                            colValue1.add(newRowViewSortedValues.get(i));
//                        }
//                        newCrossPb.hMap.put("A_"+rowViewBys.get(0),colValue1);
//                    }
        }
//                     end of code by Nazneen for Union from both Return object

        //codewritten by swati purpose of summerizedmsrs
        int totalCount = rowViewCount;
        if (summarizedMeasuresEnabled) {
            totalCount = totalCount + summerizedQryeIds.size();
            //  ArrayList<String>[] colValue = new ArrayList[summerizedQryeIds.size()];`
            ArrayListMultimap<String, String> map = ArrayListMultimap.create();
            HashMap<String, Integer> viewbyMap = new HashMap<String, Integer>();
            if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
                for (int i = 0; i < summerizedMsrRetObj.getRowCount(); i++) {
                    for (int j = 0; j < summerizedQryeIds.size(); j++) {
                        // Start of code by Nazneen for more than 1 row view by reports
                        String key = "";
                        for (int k = 0; k < rowViewCount; k++) {
                            key = key + ";;" + summerizedMsrRetObj.getFieldValueString(i, k);
                        }
//                             key =key.substring(1);
                        // takes only one rowviewby code
//                             viewbyMap.put(summerizedMsrRetObj.getFieldValueString(i, j), i);
                        viewbyMap.put(key, i);
                        // end of code by Nazneen for more than 1 row view by reports
                        // 
                        // map.put("A_"+summerizedQryeIds.get(j),summerizedMsrRetObj.getModifiedNumber(summerizedMsrRetObj.getFieldValueBigDecimal(i,"A_"+summerizedQryeIds.get(j)), "", -1));
                        map.put("A_" + summerizedQryeIds.get(j), summerizedMsrRetObj.getFieldValueString(i, "A_" + summerizedQryeIds.get(j)));
                    }
                }

                for (int k = 0; k < summerizedQryeIds.size(); k++) {
                    List<String> valList = map.get("A_" + summerizedQryeIds.get(k));
                    ArrayList list = new ArrayList();
                    ArrayList colValue = new ArrayList();
                    for (String val : valList) {
                        list.add(val);
                    }
                    int rownum;
//                         start of code by Nazneen for Union from both Return object
                    if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                       if(rowViewCount==1){
                        for (int x = 0; x < newRowViewSortedValues.size(); x++) {
                            // Start of code by Nazneen for more than 1 row view by reports
                            String viewKey = "";
                            for (int l = 0; l < rowViewCount; l++) {
//                                viewKey = viewKey+";;"+newRowViewSortedValues.get(x);
                                viewKey = newRowViewSortedValues.get(x);
                            }
//                            viewKey =viewKey.substring(1);
                            if (viewbyMap.containsKey(viewKey)) {
                                rownum = viewbyMap.get(viewKey);
                                colValue.add(list.get(rownum));
                            } else {
                                colValue.add("0");
                            }

                            // end of code by Nazneen for more than 1 row view by reports
                        }
//                         end of code by Nazneen for Union from both Return object
//                       } else {
//                       for(int x=0;x<rowViewSortedValues[0].size();x++){
//                           // Start of code by Nazneen for more than 1 row view by reports
//                           String viewKey = "";
//                           for(int l=0;l<rowViewCount;l++){
//                                viewKey = viewKey+"~"+rowViewSortedValues[l].get(x);
//                             }
//                             viewKey =viewKey.substring(1);
////                             if(viewbyMap.containsKey(rowViewSortedValues[0].get(x)))
////                          {
////                              rownum=viewbyMap.get(rowViewSortedValues[0].get(x));
////                              colValue.add(list.get(rownum));
////                          }
//                          if(viewbyMap.containsKey(viewKey))
//                          {
//                              rownum=viewbyMap.get(viewKey);
//                              
//                              colValue.add(list.get(rownum));
//                          }
//
//                             // end of code by Nazneen for more than 1 row view by reports
//                       }}
                    } else {
                        for (int x = 0; x < rowViewSortedValues[0].size(); x++) {
                            // Start of code by Nazneen for more than 1 row view by reports
                            String viewKey = "";
                            for (int l = 0; l < rowViewCount; l++) {
                                viewKey = viewKey + "~" + rowViewSortedValues[l].get(x);
                            }
                            viewKey = viewKey.substring(1);
//                             if(viewbyMap.containsKey(rowViewSortedValues[0].get(x)))
//                          {
//                              rownum=viewbyMap.get(rowViewSortedValues[0].get(x));
//                              colValue.add(list.get(rownum));
//                          }
                            if (viewbyMap.containsKey(viewKey)) {
                                rownum = viewbyMap.get(viewKey);

                                colValue.add(list.get(rownum));
                            }

                            // end of code by Nazneen for more than 1 row view by reports
                        }
                    }

                    // 
                    newCrossPb.hMap.put("A_" + summerizedQryeIds.get(k), colValue);
                    newCrossPb.cols[rowViewCount + k] = "A_" + summerizedQryeIds.get(k);
//                       newCrossPb.columnTypes[rowViewCount+k]= summerizedMsrRetObj.columnTypes[k+1];
//                       newCrossPb.columnTypesInt[rowViewCount+k]=summerizedMsrRetObj.columnTypesInt[k+1];
//                       newCrossPb.columnSizes[rowViewCount+k]= summerizedMsrRetObj.columnSizes[k+1];
                    newCrossPb.columnTypes[rowViewCount + k] = summerizedMsrRetObj.columnTypes[k + rowViewCount];
                    newCrossPb.columnTypesInt[rowViewCount + k] = summerizedMsrRetObj.columnTypesInt[k + rowViewCount];
                    newCrossPb.columnSizes[rowViewCount + k] = summerizedMsrRetObj.columnSizes[k + rowViewCount];
                }
            }
        }
        //code ended

        for (int cloop = 0; cloop < finalColSpanList.length; cloop++) {
            colSpanCurrindex.add(0);
            colSpanCurrPos.add(finalColSpanList[cloop].get(0));
            colSpanBefPos.add(0);
        }

        //   
        //ArrayList[] GTCol = new ArrayList[Qrycolumns.size()];
        //
        for (int iloop = 0; iloop < Qrycolumns.size(); iloop++) {

            GTCol[iloop] = new ArrayList<BigDecimal>();
            //SubTCol[iloop]= new ArrayList<BigDecimal>();
//                    for(int rowi=0;rowi<rowViewSortedValues[0].size();rowi++){
            if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                    if(rowViewCount==1){
                for (int rowi = 0; rowi < newRowViewSortedValues.size(); rowi++) {
                    GTCol[iloop].add(BigDecimal.ZERO);
                    //[iloop].add(BigDecimal.ZERO);
                }
//                    } else {
//                        for(int rowi=0;rowi<rowViewSortedValues[0].size();rowi++){
//                             GTCol[iloop].add(BigDecimal.ZERO);
//                        }
//                    }
            } else {
                for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                    GTCol[iloop].add(BigDecimal.ZERO);
                }
            }
        }
        resetSubTotal();
//            int initialVal=0;
//              if(summarizedMeasuresEnabled)
//                 initialVal=summerizedQryeIds.size();
        for (int coli = 0; coli < newcolValidList.size(); coli++) {
            ArrayList colValue = new ArrayList();
            int row = 0;
            int col = 0;
            int rowSize = 0;
            String colKey = "";
//                      start of code by Nazneen for Union from both Return object
            int tempRowi = 0;
            if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                    if(rowViewCount==1){
                rowSize = newRowViewSortedValues.size();
//                    } else {
//                        rowSize = rowViewSortedValues[0].size();
//                    }
            } else {
                rowSize = rowViewSortedValues[0].size();
            }
//                  for(int rowi=0;rowi<rowViewSortedValues[0].size();rowi++){
            for (int rowi = 0; rowi < rowSize; rowi++) {
                BigDecimal b = BigDecimal.ZERO;
//                      colKey =rowValidList.get(rowi).toString()+newcolValidList.get(coli);
                if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                        if(rowViewCount==1){
                    colKey = newRowViewSortedValues.get(rowi).toString() + newcolValidList.get(coli);
//                        } else {
//                             colKey =rowValidList.get(rowi).toString()+newcolValidList.get(coli);
//                        }
                } else {
                    colKey = rowValidList.get(rowi).toString() + newcolValidList.get(coli);
                }
//                          end of code by Nazneen for Union from both Return object

                // 
                if (combinedHash != null && combinedHash.get(colKey) != null) {

                    row = (combinedHash.get(colKey));
                    // 
                    col = finalQryPositions.get(coli);
                    //
                    if (this.getFieldValue(row, col) != null) {
                        colValue.add(this.getFieldValueString(row, col));
                        b = this.getFieldValueBigDecimal(row, col);

                    } else {
                        //colValue.add(combinedHash.get("0"));
                        colValue.add("0");
                        b = BigDecimal.ZERO;
                    }
                    //
                } else {
                    colValue.add("0");
                    //colValue.add(combinedHash.get("0"));
                }

                if (col >= totalColBefore) {
                    if (combinedHash != null && combinedHash.get(colKey) != null) {
                        if (b == null) {
                            b = BigDecimal.ZERO;
                        }
                        if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
                            GTCol[col - totalColBefore].set(tempRowi, b.add((BigDecimal) GTCol[col - totalColBefore].get(tempRowi)));
                            for (int cloop = 0; cloop < colViewCount; cloop++) {
                                SubTCol[cloop][col - totalColBefore].set(tempRowi, b.add((BigDecimal) SubTCol[cloop][col - totalColBefore].get(tempRowi)));
                            }
                        } else {
                            GTCol[col - totalColBefore].set(rowi, b.add((BigDecimal) GTCol[col - totalColBefore].get(rowi)));
                            for (int cloop = 0; cloop < colViewCount; cloop++) {
                                SubTCol[cloop][col - totalColBefore].set(rowi, b.add((BigDecimal) SubTCol[cloop][col - totalColBefore].get(rowi)));
                            }
                        }

                    }
//                            if(coli==0){
//                            GTCol[col-totalColBefore].add(b);
//                        }else{
//                           
//                           if(rowi>= GTCol[col-totalColBefore].size()){
//                               GTCol[col-totalColBefore].add(b);
//                           }else{
//                               
//                                GTCol[col-totalColBefore].set(rowi,b.add((BigDecimal)GTCol[col-totalColBefore].get(rowi)));
//                           }
//
//
//
//                         }
//
                }
//                        start of code by Nazneen for Union from both Return object
                if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
                    if (combinedHash != null && combinedHash.get(colKey) != null) {
                        tempRowi++;
                    }
                }
//                        end of code by Nazneen for Union from both Return object

            }//end of row forr loop
//                    newCrossPb.cols[rowViewCount+coli]= colKeys[coli];
//                    newCrossPb.columnTypes[rowViewCount+coli]=finalColumnTypes.get(coli);
//                    newCrossPb.columnTypesInt[rowViewCount+coli]= finalColumnTypesInt.get(coli);
//                    newCrossPb.columnSizes[rowViewCount+coli]= finalColumnSizes.get(coli);
//                    newCrossPb.hMap.put(colKeys[coli], colValue);
            // changed by swati purpose of summerized msrs
            newCrossPb.cols[totalCount + coli] = colKeys[coli];
            newCrossPb.columnTypes[totalCount + coli] = finalColumnTypes.get(coli);
            newCrossPb.columnTypesInt[totalCount + coli] = finalColumnTypesInt.get(coli);
            newCrossPb.columnSizes[totalCount + coli] = finalColumnSizes.get(coli);
            newCrossPb.hMap.put(colKeys[coli], colValue);

            if (!isSTNone) {
                processColSubTotal(coli, newCrossPb);
            }
        }

        newCrossPb.resetViewSequence();
        //
        //
//              start of code by Nazneen for Union from both Return object
//                newCrossPb.setRowCount(rowViewSortedValues[0].size());
        if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//                if(rowViewCount==1){
            newCrossPb.setRowCount(newRowViewSortedValues.size());
//                } else {
//                    newCrossPb.setRowCount(rowViewSortedValues[0].size());
//                }
        } else {
            newCrossPb.setRowCount(rowViewSortedValues[0].size());
        }
//                end of code by Nazneen for Union from both Return object

//                for(int iloop=0;iloop<GTCol.length;iloop++){
//                    //
//                }
//                for(int iloop=0;iloop<colViewCount;iloop++){
//                        //
////                    for(int cloop=0;cloop<Qrycolumns.size();cloop++)
////                        
//                }
//                for(int i =0; i <colViewSortedValues[0].size();i++){
//                    ArrayList colname = new ArrayList();
//
//                    for (int k = 0; k < MeasurePos ; k++) {
//                         for (int j = 0; j <=Qrycolumns.size() ; j++){
//                            if(k<colViewCount){
//                                colname.add(colViewSortedValues[k].get(i));
//                            }
//                            if (k == MeasurePos) {
//                                colname.add(nonViewInput.get("A_"+Qrycolumns.get(j).toString()).toString());
//                            }
//
//
//                        }
//                    }
//
//
//                    for (int j = 0; j <Qrycolumns.size() ; j++) {
//                        for (int k = MeasurePos; k <= totalColSize; k++) {
//
//                            if (k == MeasurePos) {
//                                colname.add(nonViewInput.get("A_"+Qrycolumns.get(j).toString()).toString());
//                            }
//                            if(k<colViewCount){
//                                colname.add(colViewSortedValues[k].get(i));
//                            }
//
//                        }
//                    }
//
//                  nonViewByMapNew.put("A"+colGenerator, colname);
//                  colGenerator++;
//                }
            /*
         * Creating new PbReturn Object
         */
        //creating list of column
//             int totalLoops =colViewValues.length-1;
//             if(meausreOnCol){
//                 totalLoops=colViewValues.length;
//             }
//           
        if (!isGTNone) {
            addGTTotalToretObj(newCrossPb);
        }
        if (!isSTNone) {
            alterCrossTabDispOrder();
        }
        if (!isGTNone && isSTNone) {
            AddGTinFinalList();
        }
        //ReProcessing for hashMap and finalColspan
        for (int l1 = 0; l1 < colViewCount + 1; l1++) {

            finalColSpanList[l1] = new ArrayList();

        }
        tempName = new String[colViewCount + 1];
        oldValList = new String[colViewCount + 1];
        newValList = new String[colViewCount + 1];
        breakChild = new boolean[colViewCount + 1];
        collectVal = new int[colViewCount + 1];
        count = 0;
        //
        //code written by swati purpose of summerizedmsrs
        if (summarizedMeasuresEnabled) {
            int msrCnt = summerizedQryeIds.size();
            if (msrCnt > 0) {
                for (int j = 0; j < msrCnt; j++) {
                    for (int i = 0; i <= totalColSize; i++) {
                        if (i <= msrCnt) {
                            finalColSpanList[i].add(1);
                        }
                    }
                }
            }

        }
        //code ended
        for (int lLoop = 0; lLoop < finalColViewSortedValues[0].size(); lLoop++) {
            String colKey = "";

            count++;

            ArrayList forViewMap = new ArrayList();
            for (int l1 = 0; l1 <= totalColSize; l1++) {
                tempName[l1] = finalColViewSortedValues[l1].get(lLoop).toString();
                forViewMap.add(tempName[l1]);
                if (MeasurePos != l1) {
                    colKey += ";;" + tempName[l1];
                }
                if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (finalColSpanList[l1] == null || finalColSpanList[l1].size() <= 1) {
                        finalColSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        finalColSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        //
                        breakChild[l1 - 1] = false;
                        //
                    }
                }
                String totalClause = "";
//                               if (breakChild[l1] || lLoop == 1) {
//                                 colTotalQuery[l1].add(finalColViewSortedValues[l1].get(lLoop).toString());
//                               }

            }
            //nonViewByMapNew.put("A"+colGenerator, forViewMap);
            //CrossTabfinalOrder.add("A"+colGenerator);
            //newcolValidList.add(colKey);
            //colGenerator++;

            //New code for i
//                     i=i+ Qrycolumns.size();
            //
        }/// End of resultset while loop

        for (int l1 = 0; l1 <= totalColSize; l1++) {
            {
                int j = ((collectVal[l1]));
                finalColSpanList[l1].add(j);
            }

        }

        ArrayList<Integer> layerColSpanLst;
        for (int index = 0; index < finalColSpanList.length; index++) {
            layerColSpanLst = finalColSpanList[index];
            for (Integer span : layerColSpanLst) {
                colSpanMap.put(index, span);
            }
        }
        //code written by swathi purpose of summerized measures
        if (this.summarizedMeasuresEnabled) {
            String[] temp = new String[summerizedQryeIds.size()];

            for (int i = 0; i < summerizedQryeIds.size(); i++) {
                ArrayList forViewMap = new ArrayList();
                forViewMap.add(" ");
                forViewMap.add(summerizedQryColNames.get(i));
                nonViewByMapNew.put("A_" + summerizedQryeIds.get(i), forViewMap);
                CrossTabfinalOrder.add(i, "A_" + summerizedQryeIds.get(i));
            }
        }
        //code ended
        newCrossPb.colSpanMap = colSpanMap;
        newCrossPb.nonViewByMapNew = nonViewByMapNew;
        newCrossPb.nonViewByMapNew1 = nonViewByMapNew1;
        newCrossPb.finalColViewSortedValues = finalColViewSortedValues;
        newCrossPb.CrossTabfinalOrder = CrossTabfinalOrder;
        newCrossPb.gtType = gtType;
        newCrossPb.subGtType = subGtType;
        newCrossPb.isGTNone = isGTNone;
        newCrossPb.isSTNone = isSTNone;
        newCrossPb.crosstabMeasureId = crosstabMeasureId;
        newCrossPb.crosstabmeasureIdsmap = crosstabmeasureIdsmap;
//            if(newCrossPb.gtType.equalsIgnoreCase("NONE") || newCrossPb.gtType.equalsIgnoreCase("") || newCrossPb.gtType == null)
//                isGTNone=true;
//            if(newCrossPb.subGtType.equalsIgnoreCase("NONE") || newCrossPb.gtType.equalsIgnoreCase("") || newCrossPb.gtType==null)
//                isSTNone=true;

        ///////Adding Sorting Key This will now there as colcount
        //newCrossPb.addColumn(count, contextPath, columnCount);
        char[] newSortTypes = new char[rowViewCount];
        char[] newSortDataTypes = new char[rowViewCount];
        ArrayList sortMeasure = new ArrayList();
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            //newCrossPb.addColumn(nonViewByMapNew.size()+l1, "A_O_"+rowViewBys.get(l1), rowViewSortedValueDataTypeInt.get(l1));
            newCrossPb.hMap.put("A_O_" + rowViewBys.get(l1), rowViewSortValues[l1]);
            sortMeasure.add("A_O_" + rowViewBys.get(l1));
            //changed by veena for grand total issue
            newCrossPb.cols[l1 + nonViewByMapNew.size() + rowViewCount] = "A_O_" + rowViewBys.get(l1);
            newCrossPb.columnTypes[l1 + nonViewByMapNew.size() + rowViewCount] = rowViewSortedValueDataType[l1];
            if (!rowViewSortedValueDataTypeInt.isEmpty()) {
                newCrossPb.columnTypesInt[l1 + nonViewByMapNew.size() + rowViewCount] = rowViewSortedValueDataTypeInt.get(l1);
            }
            newSortTypes[l1] = 'A';
            if (rowViewSortedValueDataType[l1] != null) {
                newSortDataTypes[l1] = rowViewSortedValueDataType[l1].charAt(0);
            }
            newCrossPb.columnSizes[l1 + nonViewByMapNew.size() + rowViewCount] = this.columnSizes[l1];
            //end of changes
        }
      newCrossPb.MeasurePos = MeasurePos;
//            start of code by Nazneen for Union from both Return object
//             newCrossPb.setRowCount(rowViewSortedValues[0].size());
        if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
//            if(rowViewCount==1){
            newCrossPb.setRowCount(newRowViewSortedValues.size());
//            } else {
//                newCrossPb.setRowCount(rowViewSortedValues[0].size());
//            }
        } else {
            newCrossPb.setRowCount(rowViewSortedValues[0].size());
        }
//            end of code by Nazneen for Union from both Return object
        newCrossPb.resetViewSequence();
        newCrossPb.resetSubTotal();
        newCrossPb.processGT = true;
        newCrossPb.prepareObject(newCrossPb);
        ArrayList newViewSequence = newCrossPb.sortDataSet(sortMeasure, newSortTypes, newSortDataTypes);
        newCrossPb.setViewSequence(newViewSequence);
        newCrossPb.setColumnCount(nonViewByMapNew.size() - rowViewCount);

        //   newCrossPb.sort(0, newCrossPb.cols[0], "C");
        //written by swati
        newCrossPb.finalCrossTabReportDrillMap = finalCrossTabReportDrillMap;
        return newCrossPb;

    }

    public void alterCrossTabDispOrder() {
        if (subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_BEFORE) || subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_AFTER)) {    //if (subGtType.equalsIgnoreCase("BEFORE") || subGtType.equalsIgnoreCase("AFTER"))
            for (int iloop = CrossTabfinalOrder.size() - 1; iloop >= 0; iloop--) {
                String MeasureNameAti = CrossTabfinalOrder.get(iloop).toString();
                if (subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_BEFORE)) {
                    for (int cloop = colViewCount - 1; cloop >= 0; cloop--) {
                        if (isSubTotalReq.get(cloop).equals("T")) {
//                       // 
                            if (subPosition[cloop] != null && subPosition[cloop].get(MeasureNameAti) != null) {
                                HashMap h = (HashMap) subPosition[cloop].get(MeasureNameAti);
                                for (int iQry = Qrycolumns.size() - 1; iQry >= 0; iQry--) {
                                    if (h.get(iQry) != null) {
                                        String s = h.get(iQry).toString();
                                        CrossTabfinalOrder.add(iloop, s);
                                        updateFinalColList(iloop, s);
                                    }
                                }
                            }
                        }
                    }
                } else if (subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_AFTER)) {
                    for (int cloop = 0; cloop < colViewCount; cloop++) {
                        if (isSubTotalReq.get(cloop).equals("T")) {
                            //
                            if (subPosition[cloop] != null && subPosition[cloop].get(MeasureNameAti) != null) {
                                HashMap h = (HashMap) subPosition[cloop].get(MeasureNameAti);
                                for (int iQry = Qrycolumns.size() - 1; iQry >= 0; iQry--) {
                                    //for (int iQry = 0; iQry <Qrycolumns.size() ; iQry++) {
                                    if (h.get(iQry) != null) {
                                        String s = h.get(iQry).toString();

                                        if (iloop == CrossTabfinalOrder.size() - 1) {
                                            CrossTabfinalOrder.add(s);
                                            addFinalColList(s);
                                        } else {
                                            CrossTabfinalOrder.add(iloop + 1, s);
                                            updateFinalColList(iloop + 1, s);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } else if (subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_FIRST)) {   //"ALLFIRST"

            for (int cloop = colViewCount - 1; cloop >= 0; cloop--) {
                if (isSubTotalReq.get(cloop).equals("T")) {
//                       // 
                    for (int iloop = CrossTabfinalOrder.size() - 1; iloop >= 0; iloop--) {
                        String MeasureNameAti = CrossTabfinalOrder.get(iloop).toString();
                        if (subPosition[cloop] != null && subPosition[cloop].get(MeasureNameAti) != null) {
                            HashMap h = (HashMap) subPosition[cloop].get(MeasureNameAti);
                            for (int iQry = Qrycolumns.size() - 1; iQry >= 0; iQry--) {
                                if (h.get(iQry) != null) {
                                    String s = h.get(iQry).toString();
                                    CrossTabfinalOrder.add(0, s);
                                    updateFinalColList(0, s);
                                }
                            }
                        }

                    }

                }
            }
        } else if (subGtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_SUBTOTAL_LAST)) { //ALLLAST

            for (int cloop = colViewCount - 1; cloop >= 0; cloop--) {
                if (isSubTotalReq.get(cloop).equals("T")) {
//                       // 
                    for (int iloop = 0; iloop < CrossTabfinalOrder.size(); iloop++) {
                        String MeasureNameAti = CrossTabfinalOrder.get(iloop).toString();
                        if (subPosition[cloop] != null && subPosition[cloop].get(MeasureNameAti) != null) {
                            HashMap h = (HashMap) subPosition[cloop].get(MeasureNameAti);
                            for (int iQry = 0; iQry < Qrycolumns.size(); iQry++) {
                                if (h.get(iQry) != null) {
                                    String s = h.get(iQry).toString();
                                    CrossTabfinalOrder.add(s);
                                    addFinalColList(s);
                                }
                            }
                        }

                    }

                }
            }
        }
        AddGTinFinalList();

        //  
        // 
    }

    public void AddGTinFinalList() {
        // 
        if (gtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_FIRST)) {   //"FIRST"
            for (int iloop = GTColName.size() - 1; iloop >= 0; iloop--) {
                String s = GTColName.get(iloop).toString();
                CrossTabfinalOrder.add(0, s);
                updateFinalColList(0, s);
            }
        } else if (gtType.equalsIgnoreCase(ContainerConstants.CROSSTAB_GRANDTOTAL_LAST)) {    //"LAST"
            for (int iloop = 0; iloop < GTColName.size(); iloop++) {
                String s = GTColName.get(iloop).toString();
                CrossTabfinalOrder.add(s);
                addFinalColList(s);
            }
        }
    }

    public void updateFinalColList(int pos, String ColName) {
        ArrayList a = nonViewByMapNew.get(ColName);
//     
//     
//     
        for (int cloop = 0; cloop <= colViewCount; cloop++) {
//         
            finalColViewSortedValues[cloop].add(pos, a.get(cloop));
        }

    }

    public void addFinalColList(String ColName) {
        ArrayList a = nonViewByMapNew.get(ColName);
//     
//     
//     
        for (int cloop = 0; cloop <= colViewCount; cloop++) {
//         
            finalColViewSortedValues[cloop].add(a.get(cloop));
        }

    }

    public void resetSubTotal() {
        for (int cloop = 0; cloop < colViewCount; cloop++) {
            for (int iloop = 0; iloop < Qrycolumns.size(); iloop++) {

                //GTCol[iloop] = new ArrayList<BigDecimal>();
                SubTCol[cloop][iloop] = new ArrayList<BigDecimal>();
                for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                    //GTCol[iloop].add(BigDecimal.ZERO);
                    SubTCol[cloop][iloop].add(BigDecimal.ZERO);
                }
            }
        }

    }

    public void resetSubTotal(int j) {
        {
            for (int iloop = 0; iloop < Qrycolumns.size(); iloop++) {

                //GTCol[iloop] = new ArrayList<BigDecimal>();
                SubTCol[j][iloop] = new ArrayList<BigDecimal>();
                for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                    //GTCol[iloop].add(BigDecimal.ZERO);
                    SubTCol[j][iloop].add(BigDecimal.ZERO);
                }
            }
        }

    }

    public void resetSubTotal(int j, int k) {
        {
            {

                //GTCol[iloop] = new ArrayList<BigDecimal>();
                SubTCol[j][k] = new ArrayList<BigDecimal>();
                for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                    //GTCol[iloop].add(BigDecimal.ZERO);
                    SubTCol[j][k].add(BigDecimal.ZERO);
                }
            }
        }

    }

    public void processColSubTotal(int coli, PbReturnObject newCrossPb) {//isSubTotalReq Flags
        //for(cloop)

        if (subGtType.equalsIgnoreCase("ALLFIRST")
                || subGtType.equalsIgnoreCase("BEFORE")) {

            for (int cloop = 0; cloop < colViewCount; cloop++) {
                calculateColSubTotal(cloop, coli, newCrossPb);
            }

        } else {

            for (int cloop = colViewCount - 1; cloop >= 0; cloop--) {
                calculateColSubTotal(cloop, coli, newCrossPb);
            }
        }
    }

    public void calculateColSubTotal(int cloop, int coli, PbReturnObject newCrossPb) {
//        int orgColIndex = 0;
//        if (cloop >= MeasurePos) {
//            orgColIndex = cloop - 1;
//        } else {
//            orgColIndex = cloop;
//        }

        if (isSubTotalReq.get(cloop).toString().equalsIgnoreCase("T")) {
            if (colSpanCurrPos.get(cloop) == coli + 1) {
                if (cloop < MeasurePos) {
                    addSubToHash(cloop, coli, newCrossPb);
                    resetSubTotal(cloop);

                } else {

                    addSubToHash(cloop, coli, finalQryPositions.get(coli), newCrossPb);
                    resetSubTotal(cloop, finalQryPositions.get(coli) - totalColBefore);

                }
                if (colSpanCurrindex.get(cloop) < finalColSpanList[cloop].size() - 1) {

                    colSpanCurrindex.set(cloop, colSpanCurrindex.get(cloop) + 1);
                    colSpanBefPos.set(cloop, colSpanCurrPos.get(cloop));
                    colSpanCurrPos.set(cloop, colSpanCurrPos.get(cloop)
                            + finalColSpanList[cloop].get(colSpanCurrindex.get(cloop)));
                }
            }
        }//If sub total is there then only we will sum

    }

    public void addSubToHash(int ColOrder, int coli, PbReturnObject newCrossPb) {
        //
        for (int mloop = 0; mloop < Qrycolumns.size(); mloop++) {
            String mname = this.cols[mloop + totalColBefore] + " - A_" + coli + " - " + ColOrder + " ";
            ArrayList<BigDecimal> a = new ArrayList<BigDecimal>();
            a.addAll(SubTCol[ColOrder][mloop]);
            String mName1 = this.cols[totalColBefore + mloop];
            mName1 = mName1.replace("A_", "");
            int newPos = mloop;
            if (mName1 != null && mName1.length() > 0) {
                newPos = Qrycolumns.indexOf(mName1);
            }
            addToHashAndArry(ColOrder, coli, newPos, addSubTotalToretObj(coli, a, newCrossPb), newCrossPb);//Amit done changes in order correct subtotal order
            subTotals.put(mname, a);
        }
    } //nonViewInput.get("A_"+Qrycolumns.get(j).toString()).toString()

    public void addSubToHash(int ColOrder, int coli, int mPos, PbReturnObject newCrossPb) {
        {
            String mname = this.cols[mPos] + " - A_" + coli + " - " + ColOrder + " ";
            // 
            ArrayList<BigDecimal> a = new ArrayList<BigDecimal>();
            a.addAll(SubTCol[ColOrder][mPos - totalColBefore]);
            addToHashAndArry(ColOrder, coli, mPos - totalColBefore, addSubTotalToretObj(coli, a, newCrossPb), newCrossPb);
            subTotals.put(mname, a);
        }
    }

    public String addSubTotalToretObj(int coli, ArrayList colValue, PbReturnObject newCrossPb) {
        colGenerator++;
        String newColadd = "A_" + colGenerator;
        //newCrossPb.addColumn(newCrossPb.getColumnCount()-1, newColadd, finalColumnTypesInt.get(coli));
        int currPos = totalColFilled;//
        newCrossPb.cols[currPos] = newColadd;
        newCrossPb.columnTypes[currPos] = finalColumnTypes.get(coli);
        newCrossPb.columnTypesInt[currPos] = finalColumnTypesInt.get(coli);
        newCrossPb.columnSizes[currPos] = finalColumnSizes.get(coli);
        newCrossPb.hMap.put(newColadd, colValue);
        //
        //
        //
        totalColFilled++;
        return (newColadd);
    }
//by bhargavi for exclude zero in calculating gt

    public void forzerocount() {
        val = new BigDecimal[zerocntretobj.getRowCount()];
        for (int cnt = 0; cnt < zerocntretobj.getRowCount(); cnt++) {
            int zerocountct = 0;
            BigDecimal bdcross = null;
            int mcnt = Qrycolumns.size();
            int n = 0;
            for (int i = 0; i < mcnt; i++) {
                zerocountct = 0;

                for (int j = n; j < zerocntretobj.getColumnCount(); j = j + mcnt) {

                    Object Obj = zerocntretobj.getFieldValue(cnt, j);

                    if (zerocntretobj.columnTypes[j] != null) {
                        if (zerocntretobj.columnTypesInt[j] == Types.BIGINT
                                || zerocntretobj.columnTypesInt[j] == Types.DECIMAL || zerocntretobj.columnTypesInt[j] == Types.DOUBLE
                                || zerocntretobj.columnTypesInt[j] == Types.FLOAT || zerocntretobj.columnTypesInt[j] == Types.INTEGER
                                || zerocntretobj.columnTypesInt[j] == Types.NUMERIC || zerocntretobj.columnTypesInt[j] == Types.REAL
                                || zerocntretobj.columnTypesInt[j] == Types.SMALLINT || zerocntretobj.columnTypesInt[j] == Types.TINYINT
                                || zerocntretobj.columnTypes[j].equalsIgnoreCase("NUMBER")) {

                            BigDecimal bdecimal = null;

                            if (Obj != null) {
                                bdecimal = zerocntretobj.getFieldValueBigDecimal(cnt, j);
                                BigDecimal bd = new BigDecimal("0");
                                if (bdecimal == null || bdecimal.compareTo(bd) == 0) {
                                    zerocountct = zerocountct + 1;
                                    bdcross = new BigDecimal(zerocountct);

                                }
                            }
                        }
                    }
                }
                n = n + 1;
                zerocntmsr.add(zerocountct);
            }

        }
    }//end of code by bhargavi

    public void addGTTotalToretObj(PbReturnObject newCrossPb) {
        //
        // for(int iloop=0;iloop<GTCol.length;iloop++)//Amit Commented to correct GT Order 16May2011
        HashMap<String, ArrayList> tempGt = new HashMap<String, ArrayList>();
//      //added by Nazneen
//      for(int i=0;i<Qrycolumns.size();i++){
//      int getTheMeasurePosInOldRetObj =oColumnList.indexOf("A_"+Qrycolumns.get(i));
//      tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
//      }
        zerocntretobj = newCrossPb;//bhargavi
        this.forzerocount();   //bhargavi
        for (int i = 0; i < Qrycolumns.size(); i++) {
            int getTheMeasurePosInOldRetObj = oColumnList.indexOf("A_" + Qrycolumns.get(i));
            tempGt.put((String) Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj - totalColBefore]);
            boolean flag = checkforChangepercentMsr((String) Qrycolumns.get(i));
            ArrayList<String> list = new ArrayList<String>();
            if (flag) {
                list = getCuurentAndPriorElementIDs((String) Qrycolumns.get(i));
                if (list != null && !list.isEmpty()) {
                    // 
                    //  if(list.containsAll(Qrycolumns)){
                    if (Qrycolumns.containsAll(list)) {
                        // 
                        ArrayList list1 = tempGt.get((String) list.get(0));
                        ArrayList list2 = tempGt.get((String) list.get(1));
                        // 
                        for (int k = 0; k < GTCol[0].size(); k++) {
                            BigDecimal curr = BigDecimal.ZERO;
                            BigDecimal prior = BigDecimal.ZERO;
                            BigDecimal crctVal = BigDecimal.ZERO;
                            BigDecimal sub = BigDecimal.ZERO;
                            curr = (BigDecimal) list1.get(k);
                            prior = (BigDecimal) list2.get(k);
                            // 
                            if (prior.intValue() != 0) {
                                sub = curr.subtract(prior);
                                crctVal = (sub.divide(prior, MathContext.DECIMAL32)).multiply(new BigDecimal(100));
                            }
                            // 
                            GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(k, crctVal);
                        }
                    }
                }
            }
            if (colViewCount < 2) {
                String eleId = Qrycolumns.get(i).toString();
                String gtCTAvgTypeStr = "";
                if (gtCTAvgType != null && gtCTAvgType.containsKey("A_" + eleId)) {
                    gtCTAvgTypeStr = gtCTAvgType.get("A_" + eleId);
                }
                if (MsrAggregationMap != null && MsrAggregationMap.containsKey(Qrycolumns.get(i).toString())) {
                    if (MsrAggregationMap.get(Qrycolumns.get(i).toString()).toString().equalsIgnoreCase("avg") || gtCTAvgTypeStr.equalsIgnoreCase("AVG")) {
                        int cnt = colViewSortedValues[0].size();
                        //commmented by Nazneen
//              ArrayList list1=tempGt.get((String)Qrycolumns.get(i));
////               
//               BigDecimal count = new BigDecimal(cnt);
//                for(int k=0;k<GTCol[0].size();k++){
//                BigDecimal gtValue = BigDecimal.ZERO;
//                BigDecimal finalVal = BigDecimal.ZERO;
//                 gtValue=(BigDecimal)list1.get(k);
//                 finalVal=gtValue.divide(count,MathContext.DECIMAL32);
//                 GTCol[getTheMeasurePosInOldRetObj-totalColBefore].set(k, finalVal);
//                }
//                tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
//              //added by Nazneen for Avg cols created by formula's
                        String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
                        PbDb pbdb = new PbDb();
                        try {
                            PbReturnObject retobj = pbdb.execSelectSQL(qry);
                            if (retobj.getRowCount() > 0) {
                                String tempFormula = retobj.getFieldValueString(0, 0);
                                String refferedElements = retobj.getFieldValueString(0, 1);
                                String userColType = retobj.getFieldValueString(0, 2);
                                String refElementType = retobj.getFieldValueString(0, 3);
                                String aggType = retobj.getFieldValueString(0, 4);
                                tempFormula = tempFormula.replace("SUM", "").replace("AVG", "").replace("MIN", "").replace("MAX", "").replace("COUNT", "").replace("COUNTDISTINCT", "");
                                if (!userColType.equalsIgnoreCase("summarized") && !userColType.equalsIgnoreCase("calculated") && aggType.equalsIgnoreCase("avg") || gtCTAvgTypeStr.equalsIgnoreCase("AVG")) {
                                    ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                    // BigDecimal count = new BigDecimal(cnt);
                                    int z = i;
                                    for (int k = 0; k < GTCol[0].size(); k++) {
                                        //code modified by bhargavi
                                        BigDecimal count = new BigDecimal(cnt);
                                        BigDecimal gtValue = BigDecimal.ZERO;
                                        BigDecimal finalVal = BigDecimal.ZERO;
                                        BigDecimal zerocnt = BigDecimal.ZERO;
                                        gtValue = (BigDecimal) list1.get(k);
                                        int zerocnte = zerocntmsr.get(z);
                                        zerocnt = new BigDecimal(zerocnte);
                                        count = count.subtract(zerocnt);
                                        if (count.equals(BigDecimal.ZERO)) {
                                            count = new BigDecimal(cnt);
                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                        } else {
                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                        }
                                        GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(k, finalVal);
                                        z = z + Qrycolumns.size();
                                    }
                                } else if (userColType.equalsIgnoreCase("SUMMARIZED")) {
                                    String refEleArray[] = refferedElements.split(",");
                                    int len = refEleArray.length;
                                    int flag1 = 1;
                                    for (int j = 0; j < len; j++) {
                                        String elementId = refEleArray[j];
                                        String getBussColName = "select USER_COL_NAME from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                                        PbReturnObject retobj1 = pbdb.execSelectSQL(getBussColName);
                                        if (retobj1.getRowCount() > 0) {
                                            String bussColName = retobj1.getFieldValueString(0, 0);
                                            if (tempFormula.contains(bussColName)) {
                                                tempFormula = tempFormula.replace(bussColName, elementId);
                                            }
                                        } else {
//                             tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                            ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                            // BigDecimal count = new BigDecimal(cnt);
                                            int z = i;
                                            for (int n = 0; n < GTCol[0].size(); n++) {
                                                //code modified by bhargavi
                                                BigDecimal count = new BigDecimal(cnt);
                                                BigDecimal gtValue = BigDecimal.ZERO;
                                                BigDecimal finalVal = BigDecimal.ZERO;

                                                gtValue = (BigDecimal) list1.get(n);
                                                BigDecimal zerocnt = BigDecimal.ZERO;
                                                int zerocnte = zerocntmsr.get(z);
                                                zerocnt = new BigDecimal(zerocnte);
                                                count = count.subtract(zerocnt);
                                                if (count.equals(BigDecimal.ZERO)) {
                                                    count = new BigDecimal(cnt);
                                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                } else {
                                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                }
                                                GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(n, finalVal);
                                                z = z + Qrycolumns.size();
                                            }
                                            break;
                                        }
                                    }
                                    ScriptEngineManager mgr = new ScriptEngineManager();
                                    ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                    String formula = "";
                                    int flagVal = 1;
                                    for (int k = 0; k < GTCol[0].size(); k++) {
                                        String newFormula = tempFormula;
                                        for (int j = 0; j < len; j++) {
                                            String elementId = refEleArray[j];
                                            flagVal = 1;
                                            if (Qrycolumns.contains(elementId)) {
                                                if (tempGt.get(elementId) != null) {
                                                    if (tempGt.get(elementId).get(k) != null) {
                                                        BigDecimal val = (BigDecimal) tempGt.get(elementId).get(k);
                                                        newFormula = newFormula.replace(elementId, tempGt.get(elementId).get(k).toString());
                                                    } else {
                                                        if (newFormula.contains(elementId)) {
                                                            flagVal = 0;
                                                        }
                                                    }
                                                } else {
                                                    if (newFormula.contains(elementId)) {
                                                        flagVal = 0;
                                                    }
                                                }
                                            } else {
                                                flagVal = 0;
                                            }
                                            if (flagVal == 0) {
                                                break;
                                            }
                                        }
                                        if (flagVal == 1) {
                                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                                                newFormula = "SELECT " + newFormula;
                                            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                tempGt.put((String) Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj - totalColBefore]);
                                            } else {
                                                newFormula = "SELECT " + newFormula + " FROM DUAL";
                                            }
                                            if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                                PbReturnObject retobj2 = pbdb.execSelectSQL(newFormula);
                                                if (retobj2.getRowCount() > 0) {
                                                    formula = retobj2.getFieldValueString(0, 0);
                                                    if (formula.equalsIgnoreCase("")) { //Condition added By Govardhan
                                                        formula = "0.00";
                                                    }
                                                    BigDecimal finalValue = new BigDecimal(formula);
                                                    finalValue = finalValue.setScale(2, RoundingMode.CEILING);
                                                    GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(k, finalValue);
                                                } else {
//                                            tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                                    ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                                    // BigDecimal count = new BigDecimal(cnt);
                                                    int z = i;
                                                    for (int n = 0; n < GTCol[0].size(); n++) {
                                                        //code modified by bhargavi
                                                        BigDecimal count = new BigDecimal(cnt);
                                                        BigDecimal gtValue = BigDecimal.ZERO;
                                                        BigDecimal finalVal = BigDecimal.ZERO;

                                                        gtValue = (BigDecimal) list1.get(n);
                                                        BigDecimal zerocnt = BigDecimal.ZERO;
                                                        int zerocnte = zerocntmsr.get(z);
                                                        zerocnt = new BigDecimal(zerocnte);
                                                        count = count.subtract(zerocnt);
                                                        if (count.equals(BigDecimal.ZERO)) {
                                                            count = new BigDecimal(cnt);
                                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                        } else {
                                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                        }
                                                        GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(n, finalVal);
                                                        z = z + Qrycolumns.size();
                                                    }
                                                    break;
                                                }
                                            } else {
//                                            tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                                ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                                // BigDecimal count = new BigDecimal(cnt);
                                                int z = i;
                                                for (int n = 0; n < GTCol[0].size(); n++) {
                                                    //code modified by bhargavi
                                                    BigDecimal count = new BigDecimal(cnt);
                                                    BigDecimal gtValue = BigDecimal.ZERO;
                                                    BigDecimal finalVal = BigDecimal.ZERO;

                                                    gtValue = (BigDecimal) list1.get(n);
                                                    BigDecimal zerocnt = BigDecimal.ZERO;
                                                    int zerocnte = zerocntmsr.get(z);
                                                    zerocnt = new BigDecimal(zerocnte);
                                                    count = count.subtract(zerocnt);
                                                    if (count.equals(BigDecimal.ZERO)) {
                                                        count = new BigDecimal(cnt);
                                                        finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                    } else {
                                                        finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                    }
                                                    GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(n, finalVal);
                                                    z = z + Qrycolumns.size();
                                                }
                                                break;
                                            }
                                        } else {
//                                         tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                            ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                            // BigDecimal count = new BigDecimal(cnt);
                                            int z = i;
                                            for (int n = 0; n < GTCol[0].size(); n++) {
                                                //code modified by bhargavi
                                                BigDecimal count = new BigDecimal(cnt);
                                                BigDecimal gtValue = BigDecimal.ZERO;
                                                BigDecimal finalVal = BigDecimal.ZERO;

                                                gtValue = (BigDecimal) list1.get(n);
                                                BigDecimal zerocnt = BigDecimal.ZERO;
                                                int zerocnte = zerocntmsr.get(z);
                                                zerocnt = new BigDecimal(zerocnte);
                                                count = count.subtract(zerocnt);
                                                if (count.equals(BigDecimal.ZERO)) {
                                                    count = new BigDecimal(cnt);
                                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                } else {
                                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                                }
                                                GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(n, finalVal);
                                                z = z + Qrycolumns.size();
                                            }
                                            break;
                                        }
                                    }
                                } else {
//                    tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                    ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                    // BigDecimal count = new BigDecimal(cnt);
                                    int z = i;
                                    for (int n = 0; n < GTCol[0].size(); n++) {
                                        //code modified by bhargavi
                                        BigDecimal count = new BigDecimal(cnt);
                                        BigDecimal gtValue = BigDecimal.ZERO;
                                        BigDecimal finalVal = BigDecimal.ZERO;

                                        gtValue = (BigDecimal) list1.get(n);
                                        BigDecimal zerocnt = BigDecimal.ZERO;
                                        int zerocnte = zerocntmsr.get(z);
                                        zerocnt = new BigDecimal(zerocnte);
                                        count = count.subtract(zerocnt);
                                        if (count.equals(BigDecimal.ZERO)) {
                                            count = new BigDecimal(cnt);
                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                        } else {
                                            finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                        }
                                        GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(n, finalVal);
                                        z = z + Qrycolumns.size();
                                    }
                                }
                            } else {
//                    tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                                ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                                // BigDecimal count = new BigDecimal(cnt);
                                //code modified by bhargavi
                                int z = i;
                                for (int o = 0; o < GTCol[0].size(); o++) {
                                    BigDecimal count = new BigDecimal(cnt);
                                    BigDecimal gtValue = BigDecimal.ZERO;
                                    BigDecimal finalVal = BigDecimal.ZERO;
                                    BigDecimal zerocnt = BigDecimal.ZERO;
                                    gtValue = (BigDecimal) list1.get(o);
                                    int zerocnte = zerocntmsr.get(z);
                                    zerocnt = new BigDecimal(zerocnte);
                                    count = count.subtract(zerocnt);
                                    if (count.equals(BigDecimal.ZERO)) {
                                        count = new BigDecimal(cnt);
                                        finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                    } else {
                                        finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                    }
                                    GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(o, finalVal);
                                    z = z + Qrycolumns.size();
                                }
                            }
                        } catch (SQLException ex) {
//                            tempGt.put((String)Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj-totalColBefore]);
                            ArrayList list1 = tempGt.get((String) Qrycolumns.get(i));
                            // BigDecimal count = new BigDecimal(cnt);
                            //code modified by bhargavi
                            int z = i;
                            for (int k = 0; k < GTCol[0].size(); k++) {
                                BigDecimal count = new BigDecimal(cnt);
                                BigDecimal gtValue = BigDecimal.ZERO;
                                BigDecimal zerocnt = BigDecimal.ZERO;
                                BigDecimal finalVal = BigDecimal.ZERO;
                                gtValue = (BigDecimal) list1.get(k);
                                int zerocnte = zerocntmsr.get(z);
                                zerocnt = new BigDecimal(zerocnte);
                                count = count.subtract(zerocnt);
                                if (count.equals(BigDecimal.ZERO)) {
                                    count = new BigDecimal(cnt);
                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                } else {
                                    finalVal = gtValue.divide(count, MathContext.DECIMAL32);
                                }
                                GTCol[getTheMeasurePosInOldRetObj - totalColBefore].set(k, finalVal);
                                z = z + Qrycolumns.size();
                            }
                            logger.error("Exception:", ex);
                        }
                    } else {
                        tempGt.put((String) Qrycolumns.get(i), GTCol[getTheMeasurePosInOldRetObj - totalColBefore]);
                    }
                    //end of code by Nazneen
                }
            }
        }
        for (int iloop = 0; iloop < Qrycolumns.size(); iloop++) {
            boolean addGTTest = true;
            colGenerator++;
            String newColadd = "A_" + colGenerator;
//        
            int getTheMeasurePosInOldRetObj = oColumnList.indexOf("A_" + Qrycolumns.get(iloop));
            //getTheMeasurePosInOldRetObj
            //newCrossPb.addColumn(newCrossPb.getColumnCount()-1, newColadd, finalColumnTypesInt.get(coli));
            int currPos = totalColFilled;//
            newCrossPb.cols[currPos] = newColadd;
//    newCrossPb.columnTypes[currPos]=this.columnTypes[iloop+totalColBefore];
//    newCrossPb.columnTypesInt[currPos]=this.columnTypesInt[iloop+totalColBefore] ;
//    newCrossPb.columnSizes[currPos]=this.columnSizes[iloop+totalColBefore] ;
            newCrossPb.columnTypes[currPos] = this.columnTypes[getTheMeasurePosInOldRetObj];
            newCrossPb.columnTypesInt[currPos] = this.columnTypesInt[getTheMeasurePosInOldRetObj];
            newCrossPb.columnSizes[currPos] = this.columnSizes[getTheMeasurePosInOldRetObj];
            ArrayList a = GTCol[getTheMeasurePosInOldRetObj - totalColBefore];
            newCrossPb.hMap.put(newColadd, a);
            totalColFilled++;
//   String mName1 =this.cols[totalColBefore+iloop];
//         mName1= mName1.replace("A_", "");
//         int newPos=iloop;
//         if(mName1!=null && mName1.length()>0)
//             newPos = Qrycolumns.indexOf(mName1);

            ArrayList b = new ArrayList();
            for (int cloop = 0; cloop <= colViewCount; cloop++) {
                if (cloop == MeasurePos) {
                    if (crosstabMsrMap != null && crosstabMsrMap.containsKey(queryMeasureName.get(iloop).toString())) {
                        b.add(crosstabMsrMap.get(queryMeasureName.get(iloop)));
                    } else {
                        b.add(nonViewInput.get(queryMeasureName.get(iloop)));//Code change for GT order
                    }            //written by swati
                    finalCrossTabReportDrillMap.put(newColadd, reportDrillMap.get(queryMeasureName.get(iloop)));
                    crosstabMeasureId.put(newColadd, queryMeasureName.get(iloop));
//                    if(!crosstablist.isEmpty()){
//                        if(lLoop1<crosstablist.size()){
//                    crosstabmeasureIdsmap.put(crosstablist.get(lLoop1), "A"+colGenerator);
//                    lLoop1++;
//                        }
//               }
//            crosstabmeasureIdsmap.put(queryMeasureName.get(iloop), newColadd);
                } else {
                    if (addGTTest == true) {
                        b.add(GTDisplayName);
                        addGTTest = false;
                    } else {
                        b.add("");
                    }
                }
            }
            nonViewByMapNew.put(newColadd, b);
            nonViewByMapNew1.put(newColadd, b);
            GTColName.add(newColadd);
        }

        //return(newColadd);
    }

    public String getHtml(PbReturnObject retObj) {
        // String sqlQuery = getResourceBundle().getString("viewhtml");

        StringBuilder tableBuilder = new StringBuilder();
//        
//        
        //try
        {

            //  retObj = execSelectSQL(sqlQuery);
            if (retObj != null && retObj.getRowCount() > 0) {
                tableBuilder = tableBuilder.append("<table class=\"tablesorter\" border =1 cellpadding=1 cellspacing=1> ");
                tableBuilder.append("<tr>");
                for (int iloop = 0; iloop < CrossTabfinalOrder.size(); iloop++) {
                    tableBuilder = tableBuilder.append("<td>").append(CrossTabfinalOrder.get(iloop)).append("</td>");
                }
                tableBuilder.append("</tr>");
                for (int iloop = 0; iloop < finalColViewSortedValues.length; iloop++) {
                    tableBuilder.append("<tr>");
                    for (int k = 0; k < rowViewCount; k++) {
//                    ArrayList a =nonViewByMapNew.get(retObj.getColumnNames()[k]);
//                    if(a!=null){
//                       tableBuilder=tableBuilder.append("<td>").append(a.toString()).append("</td>");
//                    }else
                        tableBuilder = tableBuilder.append("<td>").append(retObj.getColumnNames()[k]).append("</td>");
                    }

                    for (int k = 0; k < finalColViewSortedValues[iloop].size(); k++) {
                        if (finalColViewSortedValues[iloop].get(k) != null) {
                            tableBuilder = tableBuilder.append("<td>").append(finalColViewSortedValues[iloop].get(k)).append("</td>");
                        } else {
                            tableBuilder = tableBuilder.append("<td>").append("SubTotal").append("</td>");
                        }
                    }
//                    for(int k=rowViewCount+finalColViewSortedValues[0].size();k<retObj.getColumnCount();k++){
//                        tableBuilder=tableBuilder.append("<td>").append(retObj.getColumnNames()[k]).append("</td>");
//                    }

                    tableBuilder.append("</tr>");
                }
//                 
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuilder.append("<tr>");
                    for (int j = 0; j < CrossTabfinalOrder.size() + rowViewCount; j++) {
                        if (retObj.getFieldValue(i, j) != null) {
                            if (j < rowViewCount) {
                                tableBuilder.append("<td>").append(retObj.getFieldValueString(i, j)).append("</td>");
                            } else {
                                //
                                tableBuilder.append("<td>").append(retObj.getFieldValueString(i, CrossTabfinalOrder.get(j - rowViewCount).toString())).append("</td>");

                            }
                        } else {
                            tableBuilder.append("<td>").append("--").append("</td>");
                        }

                    }
                    tableBuilder.append("</tr>");
                }
                tableBuilder.append("</table>");
            }
        }
//        catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//        }
//        
        return tableBuilder.toString();
    }

    public void prepareObject(PbReturnObject retObj) {
        // long startTime = System.currentTimeMillis();
        try {

            retObj.colCount = retObj.getColumnCount();

            retObj.grandTotals = new BigDecimal[colCount];
            retObj.crosstabmapkeys = new BigDecimal[colCount];
            retObj.avgTotals = new BigDecimal[colCount];
            retObj.max = new BigDecimal[colCount];
            retObj.min = new BigDecimal[colCount];

            retObj.columnOverAllMaximums = new TreeMap();
            retObj.columnOverAllMinimums = new TreeMap();
            retObj.columnAverages = new TreeMap();
            retObj.columnGrandTotals = new TreeMap();
            retObj.columnGrandTotalscross = new TreeMap();
            retObj.rowGrandTotals = new TreeMap();

            for (int i = 0; i < retObj.colCount; i++) {

//                grandTotals[i] = new BigDecimal("0");
//                avgTotals[i] = new BigDecimal("0");
//                max[i] = new BigDecimal("0");
//                min[i] = new BigDecimal("0");
                retObj.grandTotals[i] = BigDecimal.ZERO;
                retObj.crosstabmapkeys[i] = BigDecimal.ZERO;
                retObj.avgTotals[i] = BigDecimal.ZERO;
                retObj.max[i] = BigDecimal.ZERO;
                retObj.min[i] = BigDecimal.ZERO;
            }

            for (int cnt = 0; cnt < retObj.getRowCount(); cnt++) {

                BigDecimal RowGrandTotal = BigDecimal.ZERO;
                for (int i = 0; i < retObj.getColumnCount(); i++) {
                    {
                        Object Obj = retObj.getFieldValue(cnt, i);
                        int zerocountct = 0;

                        if (retObj.processGT) {
                          
                            if (retObj.columnTypes[i] != null) {
                                if (retObj.columnTypesInt[i] == Types.BIGINT
                                        || retObj.columnTypesInt[i] == Types.DECIMAL || retObj.columnTypesInt[i] == Types.DOUBLE
                                        || retObj.columnTypesInt[i] == Types.FLOAT || retObj.columnTypesInt[i] == Types.INTEGER
                                        || retObj.columnTypesInt[i] == Types.NUMERIC || retObj.columnTypesInt[i] == Types.REAL
                                        || retObj.columnTypesInt[i] == Types.SMALLINT || retObj.columnTypesInt[i] == Types.TINYINT
                                        || retObj.columnTypes[i].equalsIgnoreCase("NUMBER")) {
                                    //code to build max,min,acg and grand total of entire record set
                                    BigDecimal bdecimal = null;
                                    BigDecimal bdcross = null;
                                    if (Obj != null) {
                                        bdecimal = retObj.getFieldValueBigDecimal(cnt, i);
                                        int zerocountvaluect = 0;
                                        BigDecimal bd = new BigDecimal("0");
                                        if (bdecimal == null || bdecimal.compareTo(bd) == 0) {
                                            zerocountct = zerocountct + 1;
                                            bdcross = new BigDecimal(zerocountct);

                                        }
                                    } else {
                                        bdecimal = BigDecimal.ZERO;
                                    }
                                    if (bdcross == null) {
                                        bdcross = new BigDecimal("0");
                                    }
                                    if (cnt == 0) {
                                        retObj.grandTotals[i] = bdecimal;
                                        retObj.max[i] = bdecimal;
                                        retObj.min[i] = bdecimal;
                                    } else {
                                        if (retObj.grandTotals[i] == null) {
                                            retObj.grandTotals[i] = BigDecimal.ZERO;
                                            retObj.crosstabmapkeys[i] = BigDecimal.ZERO;
                                        }
                                        // 
                                        if (bdecimal != null) {
                                        } else {
                                            bdecimal = BigDecimal.ZERO;
                                        }
                                        retObj.grandTotals[i] = retObj.grandTotals[i].add(bdecimal);
                                        retObj.crosstabmapkeys[i] = retObj.crosstabmapkeys[i].add(bdcross);
                                        if (retObj.max[i] == null) {
                                        } else {
                                            retObj.max[i] = retObj.max[i].max(bdecimal);
                                        }
                                        if (retObj.min[i] == null) {
                                        } else {
                                            retObj.min[i] = retObj.min[i].min(bdecimal);
                                        }

//                                    retObj.max[i]=retObj.max[i].max(bdcross);
//                                    retObj.min[i]=retObj.min[i].min(bdcross);
                                    }
                                    //code to buiold row wise grand total
                                    if (i == 0) {
                                        RowGrandTotal = bdecimal;
                                    } else {
                                        // 
                                        if (bdecimal != null) {
                                            RowGrandTotal = RowGrandTotal.add(bdecimal);
                                        } else {
                                            RowGrandTotal = RowGrandTotal.add(BigDecimal.ZERO);
                                        }
                                    }
                                    bdecimal = null;
                                }
                            }
                        }
                    }
                }
                if (retObj.processGT) {
                    retObj.rowGrandTotals.put("RowGrandTotal_" + retObj.rowCount, RowGrandTotal);
                }
                /// rowCount++;
            }
            if (retObj.rowCount != 0 && retObj.processGT) {
                BigDecimal dividend = new BigDecimal(String.valueOf(retObj.rowCount));

                for (int colIndex = 0; colIndex < retObj.colCount; colIndex++) {
                    if (retObj.columnTypes[colIndex] != null) {
                        retObj.columnOverAllMaximums.put(retObj.cols[colIndex], retObj.max[colIndex]);
                        retObj.columnOverAllMinimums.put(retObj.cols[colIndex], retObj.min[colIndex]);
                        retObj.columnGrandTotals.put(retObj.cols[colIndex], retObj.grandTotals[colIndex]);
                        retObj.columnGrandTotalscross.put(retObj.cols[colIndex], retObj.crosstabmapkeys[colIndex]);
                        retObj.avgTotals[colIndex] = retObj.grandTotals[colIndex].divide(dividend, MathContext.DECIMAL64);
                        retObj.columnAverages.put(retObj.cols[colIndex], retObj.avgTotals[colIndex]);
                    }
                }
                dividend = null;
            }
            this.initializeViewSequence();

        } catch (Exception ex) {

            logger.error("Exception:", ex);;
        }
        // long endTime = System.currentTimeMillis();
//        
    }

    public void prepareObject(ResultSet rset) {
        // long startTime = System.currentTimeMillis();
        try {
            int size = 0;
            if (rset.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                while (rset.next()) {
                    size++;
                }
            }
            ResultSetMetaData meta = rset.getMetaData();
            int colCount = meta.getColumnCount();
            cols = new String[colCount];
            columnTypes = new String[colCount];

            //added by uday
            columnTypesInt = new Integer[colCount];
            //end

            columnSizes = new int[colCount];
            grandTotals = new BigDecimal[colCount];
            avgTotals = new BigDecimal[colCount];
            max = new BigDecimal[colCount];
            min = new BigDecimal[colCount];

            columnOverAllMaximums = new TreeMap();
            columnOverAllMinimums = new TreeMap();
            columnAverages = new TreeMap();
            columnGrandTotals = new TreeMap();
            rowGrandTotals = new TreeMap();
            gtZeroCount = new int[colCount];
            columnGrandTotalsZeroCount = new TreeMap();

            for (int i = 0; i < colCount; i++) {
                if (rset.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                    if (meta.getColumnLabel(i + 1) == null || meta.getColumnName(i + 1) == meta.getColumnLabel(i + 1)) {
                        hMap.put(meta.getColumnName(i + 1), new ArrayList(size));
                    } else {
                        hMap.put(meta.getColumnLabel(i + 1), new ArrayList(size));
                    }
                } else if (meta.getColumnLabel(i + 1) == null || meta.getColumnName(i + 1) == meta.getColumnLabel(i + 1)) {
                    hMap.put(meta.getColumnName(i + 1), new ArrayList());
                } else {
                    hMap.put(meta.getColumnLabel(i + 1), new ArrayList());
                }

                if (meta.getColumnLabel(i + 1) == null || meta.getColumnName(i + 1) == meta.getColumnLabel(i + 1)) {
                    cols[i] = meta.getColumnName(i + 1);
                } else {
                    cols[i] = meta.getColumnLabel(i + 1);
                }
                columnTypes[i] = meta.getColumnTypeName(i + 1);

                //added by uday
                columnTypesInt[i] = meta.getColumnType(i + 1);
                //end

                columnSizes[i] = meta.getColumnDisplaySize(i + 1);
//                grandTotals[i] = new BigDecimal("0");
//                avgTotals[i] = new BigDecimal("0");
//                max[i] = new BigDecimal("0");
//                min[i] = new BigDecimal("0");
                grandTotals[i] = BigDecimal.ZERO;
                avgTotals[i] = BigDecimal.ZERO;
                max[i] = BigDecimal.ZERO;
                min[i] = BigDecimal.ZERO;
                gtZeroCount[i] = 0;
            }
            BigDecimal bd = new BigDecimal("0");
            while (rset.next()) {

                BigDecimal RowGrandTotal = BigDecimal.ZERO;
                for (int i = 0; i < colCount; i++) {
//                    int zerocount=0;
                    if (meta.getColumnType(i + 1) == Types.BLOB) {
                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getBlob(cols[i]));
                    } else if (meta.getColumnType(i + 1) == Types.CLOB) {
                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getClob(cols[i]));
                    } else if (meta.getColumnType(i + 1) == Types.DATE) {
                        ((ArrayList) (hMap.get(cols[i]))).add(rset.getTimestamp(cols[i]));
                    } else {
                        Object Obj = rset.getObject(cols[i]);
                        ((ArrayList) (hMap.get(cols[i]))).add(Obj);

                        if (processGT) {
                            if (meta.getColumnType(i + 1) == Types.BIGINT || meta.getColumnType(i + 1) == Types.DECIMAL || meta.getColumnType(i + 1) == Types.DOUBLE || meta.getColumnType(i + 1) == Types.FLOAT || meta.getColumnType(i + 1) == Types.INTEGER || meta.getColumnType(i + 1) == Types.NUMERIC || meta.getColumnType(i + 1) == Types.REAL || meta.getColumnType(i + 1) == Types.SMALLINT || meta.getColumnType(i + 1) == Types.TINYINT) {
                                //code to build max,min,acg and grand total of entire record set
                                BigDecimal bdecimal = null;
                                if (Obj != null) {
                                    bdecimal = rset.getBigDecimal(cols[i]);
//                                 int p=i;
//                                  String key="";
//                                  int zerocountvalue=0;
////                                   BigDecimal bd = new BigDecimal("0");
//                                    if( bdecimal==null || bdecimal.compareTo(bd)==0){
//                                    zerocount= zerocount+1;
//                                    for(int s=0;s<cols.length;s++){
//                            if(p==s)
//                                 key=cols[s];
//                                        }
//                                     if(zerocountmap.containsKey(key))
//                                       zerocountvalue=zerocountmap.get(key);
//                                  int zeroval;
//                                        zeroval = zerocountvalue+zerocount;
//                                    zerocountmap.put( key,zeroval);
//
//                                    }
                                } else {
                                    bdecimal = BigDecimal.ZERO;
                                }
                                if (rowCount == 0) {
                                    grandTotals[i] = bdecimal;
                                    max[i] = bdecimal;
                                    min[i] = bdecimal;
                                    if (bdecimal == null || bdecimal.compareTo(bd) == 0) {
                                        gtZeroCount[i] = gtZeroCount[i] + 1;
                                    }
                                } else {
                                    grandTotals[i] = grandTotals[i].add(bdecimal);
                                    max[i] = max[i].max(bdecimal);
                                    min[i] = min[i].min(bdecimal);
                                    if (bdecimal == null || bdecimal.compareTo(bd) == 0) {
                                        gtZeroCount[i] = gtZeroCount[i] + 1;
                                    }
                                }
                                //code to buiold row wise grand total
                                if (i == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                            }
                        }
                    }
                }
                if (processGT) {
                    rowGrandTotals.put("RowGrandTotal_" + rowCount, RowGrandTotal);
                }
                rowCount++;
            }
            if (rowCount != 0 && processGT) {
                BigDecimal dividend = new BigDecimal(String.valueOf(rowCount));

                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    columnOverAllMaximums.put(cols[colIndex], max[colIndex]);
                    columnOverAllMinimums.put(cols[colIndex], min[colIndex]);
                    columnGrandTotals.put(cols[colIndex], grandTotals[colIndex]);
                    avgTotals[colIndex] = grandTotals[colIndex].divide(dividend, MathContext.DECIMAL64);
                    columnAverages.put(cols[colIndex], avgTotals[colIndex]);
                    //written by swati purpose of finding no of zeros in gt
                    columnGrandTotalsZeroCount.put(cols[colIndex], gtZeroCount[colIndex]);
                }
                dividend = null;
            }
            this.initializeViewSequence();

        } catch (SQLException ex) {

            logger.error("Exception:", ex);;
        }
        // long endTime = System.currentTimeMillis();
//        
    }

    public void writeString() {

        /*
         * for (int i = 0; i < rowCount; i++) {
         * //////////////////////.println("---------ROW START:::" + i +
         * "---------"); for (int j = 0; j < cols.length; j++) {
         * //////////////////////.println(cols[j] + "===" + ((ArrayList)
         * htable.get(cols[j])).get(i)); }
         * //////////////////////.println("---------ROW END:::" + i +
         * "---------"); }
         */
    }

    public void deleteRow(int row) {
        for (int i = 0; i
                < cols.length; i++) {
            ((ArrayList) hMap.get(cols[i])).remove(row);//removeElementAt(row);
        }

        rowCount--;
        this.resetViewSequence();
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColumnCount() {
        if (cols != null) {
            return cols.length;
        } else {
            return 0;
        }

    }

    public Object getFieldValueBlob(int row, String colName) {
//        row = this.getActualRow(row);
        return ((Blob) ((ArrayList) hMap.get(colName)).get(row));
    }

    public Timestamp getFieldValueTimestamp(int row, String colName) {
        return ((Timestamp) ((ArrayList) hMap.get(colName)).get(row));

    }

    public Object getFieldValueClob(int row, String colName) {
        //       row = this.getActualRow(row);
        return ((Clob) ((ArrayList) hMap.get(colName)).get(row));
    }

    public Object getFieldValueOracleClob(int row, String colName) {
        //       row = this.getActualRow(row);
        return ((CLOB) ((ArrayList) hMap.get(colName)).get(row));
    }

    public String getFieldUnknown(int row, int column) {
        try {
            if (columnTypes[column].toUpperCase().equals("VARCHAR")) {
                return getFieldValueString(row, cols[column]);
            } else if (columnTypes[column].toUpperCase().equals("DATE")
                    || columnTypes[column].toUpperCase().equals("DATETIME")
                    || columnTypes[column].toUpperCase().equals("TIMESTAMP")
                    || columnTypes[column].toUpperCase().equals("DATETIME2")) {
                return getFieldValueDateString(row, cols[column]);
            } else if (columnTypes[column].toUpperCase().equals("TEXT") || columnTypes[column].toUpperCase().equals("CLOB")) {
                return getFieldValueClobString(row, cols[column]);
            } else {
                return String.valueOf(getFieldValue(row, cols[column]));
            }

        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return null;
        }

    }

    public String getFieldValueClobString(int row, String colName) {
        //      row = this.getActualRow(row);
        try {
            Clob clb = (Clob) (((ArrayList) hMap.get(colName)).get(row));
            if (clb != null) {
                Reader reader = (Reader) clb.getCharacterStream();
                BufferedReader brd = new BufferedReader(reader);
                String val = brd.readLine();
                brd.close();
                reader.close();
                return val;
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.error("Exception:", e);
            return null;
        } catch (SQLException e) {
            logger.error("Exception:", e);
            return null;
        }

    }

    public String getFieldValueOracleClobString(int row, String colName) {
        //       row = this.getActualRow(row);
        try {
            CLOB clb = (CLOB) (((ArrayList) hMap.get(colName)).get(row));
            if (clb != null) {
                Reader reader = (Reader) clb.getCharacterStream();
                BufferedReader brd = new BufferedReader(reader);
                String val = brd.readLine();
                brd.close();
                reader.close();
                return val;
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.error("Exception:", e);
            return null;
        } catch (SQLException e) {
            logger.error("Exception:", e);
            return null;
        }

    }

    public Object getFieldValue(int row, String colName) {
        //       row = this.getActualRow(row);
        return ((ArrayList) hMap.get(colName)).get(row);
    }

    public Object getFieldValue(int row, int column) {
//        row = this.getActualRow(row);
        try {
            return getFieldValue(row, cols[column]);
        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return null;
        }

    }

    public String getFieldValueString(int row, String colName) {
        //     row = this.getActualRow(row);
        try {
            String value = null;
//            if ((((ArrayList) hMap.get(colName)).get(row)) != null) {
            if (hMap != null && hMap.get(colName) != null && (((ArrayList) hMap.get(colName)).get(row)) != null) {
                value = (((ArrayList) hMap.get(colName)).get(row)).toString();
            } else {
                value = "";
            }

            return value;
        } catch (Exception e) {
            logger.error("Exception:", e);
            //return null;
            return "";
        }

    }

    public String getFieldValueString(int row, int column) {
        //        row = this.getActualRow(row);
        try {
            return getFieldValueString(row, cols[column]);
        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return "";
        }

    }

    public int getFieldValueInt(int row, String colName) {
        //        row = this.getActualRow(row);
        try {
            String value = (((ArrayList) hMap.get(colName)).get(row)).toString();
            //return Integer.parseInt(value);	//MODIFIED
            java.math.BigDecimal bd = new java.math.BigDecimal(value);	//MODIFIED
            return bd.intValue();	//MODIFIED
        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return 0;
        }

    }

    //added for numberformat
    public String getFieldValueFormatInt(int row, String colName) {
        //       row = this.getActualRow(row);
        try {
            String value = "";
            String value1 = ".00";
            NumberFormat nf = NumberFormat.getInstance(numberFormat);
            nf.setMinimumFractionDigits(1);
            nf.setMaximumFractionDigits(1);
            value = (((ArrayList) hMap.get(colName)).get(row)).toString();
            Double db = Double.parseDouble(value);
            value = nf.format(db);

            if (value.indexOf(".") < 0) {
                value += value1;
            }

            return value;
        } catch (NumberFormatException e) {
            logger.error("Exception:", e);
            return "0";
        }

    }

    public int getFieldValueInt(int row, int column) {
        //       row = this.getActualRow(row);
        try {
            return getFieldValueInt(row, cols[column]);
        } catch (Exception e) {
                  logger.error("Exception:", e);
            return 0;
        }

    }

    public void updateFieldValue(int row, String colName, Object value) {
        //        row = this.getActualRow(row);
        ((ArrayList) hMap.get(colName)).set(row, value);
    }

    public String getFieldValueDateString(int row, String colName) {
        //      row = this.getActualRow(row);
        try {
            Object dateType = ((ArrayList) hMap.get(colName)).get(row);
            java.util.Date date;
            if (dateType instanceof String) {
                String dateString = ((ArrayList) hMap.get(colName)).get(row).toString();
                Timestamp celldata = Timestamp.valueOf(dateString);
                date = (java.util.Date) (celldata);
            } else {
                date = (java.util.Date) ((ArrayList) hMap.get(colName)).get(row);
            }
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(date);
        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return "";
        }

    }

    public String getFieldValueDateString(int row, int column) {
        //     row = this.getActualRow(row);
        try {
            return getFieldValueDateString(row, cols[column]);
        } catch (Exception e) {
            return null;
        }

    }

    public java.util.Date getFieldValueDate(int row, String colName) {
        //        row = this.getActualRow(row);
        try {
            java.util.Date date = (java.util.Date) ((ArrayList) hMap.get(colName)).get(row);
            return date;
        } catch (Exception e) {
                  //logger.error("Exception:", e);
            return null;
        }

    }

    public java.util.Date getFieldValueDate(int row, int column) {
        //       row = this.getActualRow(row);
        try {
             java.util.Date date = getFieldValueDate(row, cols[column]);
            if(date!=null){
            return date;
            }
            else {
                return null;
            }
        } catch (Exception e) {
                  logger.error("Exception:", e);
            return null;
        }

    }

    public BigDecimal getFieldValueBigDecimal(int row, String colName) {
        //       row = this.getActualRow(row);
        try {
            if (getFieldValue(row, colName) == null || getFieldValueString(row, colName).equalsIgnoreCase("")) {
                return new BigDecimal("0");//modified by santhosh.kumar@progenbusiness.com on 07/10/2009
            } else {
                return new BigDecimal(getFieldValueString(row, colName));
            }

        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return null;
        }

    }

    public BigDecimal getFieldValueBigDecimal(int row, int colNo) {
        //        row = this.getActualRow(row);
        try {
            if (getFieldValueString(row, cols[colNo]).equalsIgnoreCase("")) {
                return new BigDecimal("0");//modified by santhosh.kumar@progenbusiness.com on 07/10/2009
            } else {
                return new BigDecimal(getFieldValueString(row, cols[colNo]));
            }

        } catch (Exception e) {
                  logger.error("Exception:", e);;
            return null;
        }

    }

    public void setDateFormat(String inputFormat) {
        if (inputFormat != null) {
            dateFormat = inputFormat;
        }

    }

    //added for numberformat
    public void setNumberFormat(Locale locale) {
        if (locale != null) {
            this.numberFormat = locale;
        }

    }

    public Object getObject(String key) {
        Object object = null;
        if (key != null) {
            object = hMap2.get(key);
        }

        return object;
    }

    public void setObject(String key, Object value) {
        if (key != null && value != null) {
            hMap2.put(key, value);
        }

    }

    public String[] getColumnNames() {
        return this.cols;
    }

    public void setColumnNames(String[] inpCols) {
        this.cols = inpCols;
    }

    public String[] getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(String[] columnTypes) {
        this.columnTypes = columnTypes;
    }

    public int[] getColumnSizes() {
        return columnSizes;
    }

    public void setColumnSizes(int[] columnSizes) {
        this.columnSizes = columnSizes;
    }

    public void setFieldValueString(String colName, String value) {
        if (colName != null && value != null) {
            setHash.put(colName, value);
        }

    }

    public void setFieldValueDate(String colName, java.util.Date value) {
        if (colName != null && value != null) {
            setHash.put(colName, value);
        }

    }

    public void setFieldValue(String colName, Object value) {
        if (colName != null && value != null) {
            setHash.put(colName, value);
        }

    }

    public void setFieldValueAt(int index, String columnName, Object value) {
        ((ArrayList) this.hMap.get(columnName)).add(index, value);//setElementAt(value, index);  //set(index, value);
    }

    public void addRow() {
        int size = setHash.size();
        if (size > 0) {
            if (cols.length <= 0) {
            } else {
                int colCount = getColumnCount();
                for (int i = 0; i
                        < colCount; i++) {
                    if (hMap.get(cols[i]) == null) {
                        hMap.put(cols[i], new ArrayList());
                    }

                }
                for (int i = 0; i
                        < colCount; i++) {
                    ((ArrayList) (hMap.get(cols[i]))).add(setHash.get(cols[i]));
                }

                rowCount++;
                setHash.clear();
            }

        } else {
        }
//       this.resetViewSequence();

    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setDataForColumn(String colName, ArrayList<Object> data) throws InvalidDataException {
        if (data.size() < rowCount) {
            throw new InvalidDataException("Row count not matching");
        }

        hMap.put(colName, data);
    }

    public void addSortRow(int index) {
        if (cols.length <= 0) {
        } else {
            int colCount = this.getColumnCount();
            if (this.getRowCount() == 0) {
                for (int i = 0; i
                        < colCount; i++) {
                    if (this.hMap.get(cols[i]) == null) {
                        this.hMap.put(cols[i], new ArrayList());
                    }

                }
            }

            for (int i = 0; i
                    < colCount; i++) {
                ((ArrayList) (this.hMap.get(cols[i]))).add(index, this.setHash.get(cols[i]));
            }

            this.rowCount++;
            //     this.resetViewSequence();
        }

    }

    public void appendRow(int row) {
        int count = retSearch.getRowCount();
        for (int i = 0; i < cols.length; i++) {
            retSearch.setFieldValue(cols[i], this.getFieldValue(row, cols[i]));
        }

        retSearch.addRow();
    }


    /*
     * added by santhosh on 02/09/2009 for top5
     */
    public PbReturnObject sort(int sortType, String colName, String dataType) {
        // sortType: 0-ascending, 1-descending
        // dataType: C-characters, D-Date, N-Number
        retSort = new PbReturnObject();
        retSort.setColumnNames(cols);

        int compIndex = 0;
        int index = 0;
        if (dataType.equals("N")) {
            BigDecimal currVal; //getFieldValueBigDecimal
            for (int i = 0; i
                    < rowCount; i++) {
                index = 0;
                currVal= this.getFieldValueBigDecimal(i, colName);

                if (sortType == 0) //Ascending
                {
                    index = 0;
                    for (int j = 0; j
                            < retSort.getRowCount(); j++) {
                        if (currVal.subtract(retSort.getFieldValueBigDecimal(j, colName)).floatValue() > 0) {
                            index = j + 1;
                        }

                    }
                } else //Descending
                {
                    index = 0; //retSort.getRowCount();
                    for (int j = retSort.getRowCount() - 1; j
                            >= 0; j--) {
                        if (currVal.subtract(retSort.getFieldValueBigDecimal(j, colName)).floatValue() < 0) {
                            index = j + 1;
                            break;

                        }

                    }
                }
                for (int a = 0; a
                        < cols.length; a++) {
                    retSort.setFieldValue(cols[a], this.getFieldValue(i, cols[a]));
                }

                retSort.addSortRow(index);
            }

        } else if (dataType.equals("C")) {
            String currVal = null;
            for (int i = 0; i
                    < rowCount; i++) {
                index = 0;
                currVal =this.getFieldValueString(i, colName);

                if (sortType == 0) //Ascending
                {
                    index = 0;
                    for (int j = 0; j
                            < retSort.getRowCount(); j++) {
                        if (currVal.compareTo(retSort.getFieldValueString(j, colName)) > 0) {
                            index = j + 1;
                        }

                    }
                } else //Descending
                {
                    index = 0; //retSort.getRowCount();
                    for (int j = retSort.getRowCount() - 1; j
                            >= 0; j--) {
                        if (currVal.compareTo(retSort.getFieldValueString(j, colName)) < 0) {
                            index = j + 1;
                            break;

                        }

                    }
                }
                for (int a = 0; a < cols.length; a++) {
                    retSort.setFieldValue(cols[a], this.getFieldValue(i, cols[a]));
                }

                retSort.addSortRow(index);
            }

        } else if (dataType.equals("D")) {
            java.util.Date currVal = null;
            for (int i = 0; i
                    < rowCount; i++) {
                index = 0;
                currVal =this.getFieldValueDate(i, colName);

                if (sortType == 0) //Ascending
                {
                    index = 0;
                    for (int j = 0; j
                            < retSort.getRowCount(); j++) {
                        if (currVal.compareTo(retSort.getFieldValueDate(j, colName)) > 0) {
                            index = j + 1;
                        }

                    }
                } else //Descending
                {
                    index = 0; //retSort.getRowCount();
                    for (int j = retSort.getRowCount() - 1; j
                            >= 0; j--) {
                        if (currVal.compareTo(retSort.getFieldValueDate(j, colName)) < 0) {
                            index = j + 1;
                            break;

                        }

                    }
                }
                for (int a = 0; a
                        < cols.length; a++) {
                    retSort.setFieldValue(cols[a], this.getFieldValue(i, cols[a]));
                }

                retSort.addSortRow(index);
            }

        }

        return retSort;
    }

//ADDED BY UDAY on 24/12/2009 for displaying numbers in terms of K,Mn,Bn,Tn
    public String getModifiedNumber(BigDecimal bd) {
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;

        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        /*
         * if ((num / getPowerOfTen(12)) > 0.99) // check for Trillion {
         * doubleVal = (num / getPowerOfTen(12)); temp =
         * String.valueOf(truncate(doubleVal)) + "Tn"; } else if ((num /
         * getPowerOfTen(9)) > 0.99) // check for Billion { doubleVal = (num /
         * getPowerOfTen(9)); temp = String.valueOf(truncate(doubleVal)) + "Bn";
         * } else
         */
        if ((num / getPowerOfTen(6)) > 0.99) // check for Million
        {
            doubleVal = (num / getPowerOfTen(6));
            //temp = String.valueOf(truncate(doubleVal)) + "M";
            temp= nFormat.format(doubleVal) + "M";
        } else if ((num / getPowerOfTen(3)) > 0.99) // check for K
        {
            doubleVal = (num / getPowerOfTen(3));
            //temp = String.valueOf(truncate(doubleVal)) + "K";
            temp =nFormat.format(doubleVal) + "K";
        } else {
            //temp = String.valueOf(num);
            temp = nFormat.format(num);
        }
        return temp;
    }

    public String getModifiedNumber(BigDecimal bd, String nbrSymbol) {
        //  ////.println("nbrSymbol=="+nbrSymbol);
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);


        /*
         * if ((num / getPowerOfTen(12)) > 0.99) // check for Trillion {
         * doubleVal = (num / getPowerOfTen(12)); temp =
         * String.valueOf(truncate(doubleVal)) + "Tn"; } else if ((num /
         * getPowerOfTen(9)) > 0.99) // check for Billion { doubleVal = (num /
         * getPowerOfTen(9)); temp = String.valueOf(truncate(doubleVal)) + "Bn";
         * } else
         */
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
                temp = nFormat.format(bd) + " %";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Bn")) {
                doubleVal = (num / getPowerOfTen(9));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "Bn";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "Cr";
            }

        } else {
            if (Math.round(bd.doubleValue()) == bd.doubleValue()) {
                nFormat.setMaximumFractionDigits(0);
                nFormat.setMinimumFractionDigits(0);
            } else {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
            }
            temp = nFormat.format(bd);
        }

        return temp;
    }

    public String getModifiedNumber(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        if (bd == null) {
            return temp;
        }
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);


        /*
         * if ((num / getPowerOfTen(12)) > 0.99) // check for Trillion {
         * doubleVal = (num / getPowerOfTen(12)); temp =
         * String.valueOf(truncate(doubleVal)) + "Tn"; } else if ((num /
         * getPowerOfTen(9)) > 0.99) // check for Billion { doubleVal = (num /
         * getPowerOfTen(9)); temp = String.valueOf(truncate(doubleVal)) + "Bn";
         * } else
         */
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + "K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd) + " %";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + "M";
            }
        } else {
            setFormat(nFormat, bd.doubleValue(), precision);
            temp = nFormat.format(bd);
        }
        return temp;
    }

    private void setFormat(NumberFormat nFormat, double value, int precision) {
        if (precision < 0) {
            if (Math.round(value) == value) {
                nFormat.setMaximumFractionDigits(0);
                nFormat.setMinimumFractionDigits(0);
            } else {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
            }
        } else {
            nFormat.setMaximumFractionDigits(precision);
            nFormat.setMinimumFractionDigits(precision);
        }
    }

    private void setFormat(NumberFormat nFormat, double value) {
        if (Math.round(value) == value) {
            nFormat.setMaximumFractionDigits(0);
            nFormat.setMinimumFractionDigits(0);
        } else {
            nFormat.setMaximumFractionDigits(2);
            nFormat.setMinimumFractionDigits(2);
        }
    }

    public String getModifiedNumber(double bd, String nbrSymbol) {

        String temp = "";
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                bd = (bd / getPowerOfTen(3));
                //temp = String.valueOf(truncate(bd)) + "K";
                temp =nFormat.format(bd) + "K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                bd = (bd / getPowerOfTen(6));
                //temp = String.valueOf(truncate(bd)) + "M";
                temp =nFormat.format(bd) + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs")) {
                //temp = String.valueOf(bd);
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                bd = (bd / getPowerOfTen(6));
                //temp = String.valueOf(truncate(bd)) + "M";
                temp =nFormat.format(bd) + "M";
            }

        } else {
            //temp = String.valueOf(bd);
            temp = nFormat.format(bd);
        }
        //.out.println("temp2=="+temp);
        return temp;
        /*
         * if ((bd / getPowerOfTen(12)) > 0.99) // check for Trillion { bd = (bd
         * / getPowerOfTen(12)); temp = String.valueOf(truncate(bd)) + "Tn"; }
         * else if ((bd / getPowerOfTen(9)) > 0.99) // check for Billion { bd =
         * (bd / getPowerOfTen(9)); temp = String.valueOf(truncate(bd)) + "Bn";
         * }
         *
         * if ((bd / getPowerOfTen(6)) > 0.99) // check for Million { bd = (bd /
         * getPowerOfTen(6)); temp = String.valueOf(truncate(bd)) + "Mn"; } else
         * if ((bd / getPowerOfTen(3)) > 0.99) // check for K { bd = (bd /
         * getPowerOfTen(3)); temp = String.valueOf(truncate(bd)) + "K"; } else
         * { temp = String.valueOf(bd); }
         */

    }

    public long getPowerOfTen(int num) {
        long bd = 1;
        for (int i = 0; i
                < num; i++) {
            bd = bd * 10;
        }

        return bd;
    }

    private double truncate(double x) {
        DecimalFormat df = new DecimalFormat("0.##");
        String d = df.format(x);
        d= d.replaceAll(",", ".");
        Double dbl = Double.parseDouble(d);
        return dbl;
    }

    public String getModifiedNumber(double bd) {

        String temp = "";
        /*
         * String[] keysSybls = {"K", "M", "B", "T"}; int[] keys = {3, 6, 9,
         * 12};
         *
         * for (int i = 0; i < keys.length; i++) { if (bd /
         * getPowerOfTen(keys[i]) > 0.99) { bd = (bd / getPowerOfTen(keys[i]));
         * temp = String.valueOf(truncate(bd)) + keysSybls[i]; break; } }
         */

        /*
         * if ((bd / getPowerOfTen(12)) > 0.99) // check for Trillion { bd = (bd
         * / getPowerOfTen(12)); temp = String.valueOf(truncate(bd)) + "Tn"; }
         * else if ((bd / getPowerOfTen(9)) > 0.99) // check for Billion { bd =
         * (bd / getPowerOfTen(9)); temp = String.valueOf(truncate(bd)) + "Bn";
         * }
         */
        if ((bd / getPowerOfTen(6)) > 0.99) // check for Million
        {
            bd = (bd / getPowerOfTen(6));
            temp =String.valueOf(truncate(bd)) + "M";
        } else if ((bd / getPowerOfTen(3)) > 0.99) // check for K
        {
            bd = (bd / getPowerOfTen(3));
            temp =String.valueOf(truncate(bd)) + "K";
        } else {
            temp = String.valueOf(bd);
        }
        return temp;
    }

    public double getMaximumValueInColumn(int column) {
        double maxValue = 0.0;

        for (int i = 0; i
                < rowCount; i++) {
            if (i == 0) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i))) > maxValue) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

        }
        return maxValue;
    }

    public double getMaximumValueInColumn(String colName) {
        double maxValue = 0.0;

        for (int i = 0; i
                < rowCount; i++) {
            if (i == 0) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i))) > maxValue) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

        }
        return maxValue;
    }

    public double getMinimumValueInColumn(int column) {
        double minValue = 0.0;

        for (int i = 0; i
                < rowCount; i++) {
            if (i == 0) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i))) < minValue) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

        }
        return minValue;
    }

    public double getMinimumValueInColumn(String colName) {
        double minValue = 0.0;

        for (int i = 0; i
                < rowCount; i++) {
            if (i == 0) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i))) < minValue) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

        }
        return minValue;
    }

    public double getDisplayedSetMaxValueInColumn(int fromRow, int toRow, int column) {
        double maxValue = 0.0;

        for (int i = fromRow; i
                < toRow; i++) {
            if (i == 0) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i))) > maxValue) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

        }
        return maxValue;
    }

    public double getDisplayedSetMaxValueInColumn(int fromRow, int toRow, String colName) {
        double maxValue = 0.0;

        for (int i = fromRow; i
                < toRow; i++) {
            if (i == 0) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i))) > maxValue) {
                maxValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

        }
        return maxValue;
    }

    public double getDisplayedSetMinValueInColumn(int fromRow, int toRow, int column) {
        double minValue = 0.0;

        for (int i = fromRow; i
                < toRow; i++) {
            if (i == 0) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i))) < minValue) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(cols[column])).get(i)));
            }

        }
        return minValue;
    }

    public double getDisplayedSetMinValueInColumn(int fromRow, int toRow, String colName) {
        double minValue = 0.0;

        for (int i = fromRow; i
                < toRow; i++) {
            if (i == 0) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

            if (Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i))) < minValue) {
                minValue = Double.parseDouble(String.valueOf(((ArrayList) hMap.get(colName)).get(i)));
            }

        }
        return minValue;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public BigDecimal getColumnMaximumValue(String colName) {

        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getMaximum();
            } else {
                return (BigDecimal) (columnOverAllMaximums.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnMaximumValue(int column) {
        try {

            return (BigDecimal) (columnOverAllMaximums.get(cols[column]));
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnMinimumValue(String colName) {

        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getMinimum();
            } else {
                return (BigDecimal) (columnOverAllMinimums.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnMinimumValue(int column) {
        try {
            return (BigDecimal) (columnOverAllMinimums.get(cols[column]));
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnGrandTotalValue(String colName) {

        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                if(columnGrandTotals!=null && columnGrandTotals.get(colName)!=null &&columnGrandTotals.get(colName)!=""){
                    return (BigDecimal) (columnGrandTotals.get(colName));
                }else{
                return this.rtMeasMap.get(colName).getGrandTotal();
                }
            } else {
                return (BigDecimal) (columnGrandTotals.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnGrandTotalValue(int column) {
        try {
            return (BigDecimal) (columnGrandTotals.get(cols[column]));
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnAverageValue(String colName) {

        try {
            if (RTMeasureElement.isRunTimeMeasure(colName)) {
                return this.rtMeasMap.get(colName).getAverage();
            } else {
                return (BigDecimal) (columnAverages.get(colName));
            }
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    public BigDecimal getColumnAverageValue(int column) {
        try {
            return (BigDecimal) (columnAverages.get(cols[column]));
        } catch (Exception e) {
            return new BigDecimal("0");
        }

    }

    private char getDataTypeForColumn(String columnId) {
        int index = 0;
        for (String columnName : this.getColumnNames()) {
            if (columnName.equals(columnId)) {
                break;
            }
            index++;
        }

        if (columnTypes[index].equalsIgnoreCase("NUMBER")) {
            return 'N';
        } else {
            return 'C';
        }
    }

    public void setRunTimeMeasureData(String measure, RunTimeMeasure rtMeasure) {
        if (this.rtMeasMap == null) {
            this.rtMeasMap = (new HashMap<String, RunTimeMeasure>());
        }
        this.rtMeasMap.put(measure, rtMeasure);
    }

//    private ArrayList<BigDecimal> getPercentWiseOfColumn(String measEleId)
//    {
//        ArrayList<BigDecimal> percentWiseData = new ArrayList<BigDecimal>();
//        BigDecimal total = this.getColumnGrandTotalValue(measEleId);
//        if (total.intValue() == 0) {
//            total = new BigDecimal("1");
//        }
//        ArrayList<BigDecimal> actualData = this.retrieveData(measEleId);
//        BigDecimal percent = new BigDecimal(100);
//
//        for ( BigDecimal measData : actualData )
//        {
//            percentWiseData.add(measData.divide(total, MathContext.DECIMAL64).multiply(percent));
//        }
//
//        return percentWiseData;
//    }
    
    public BigDecimal getFieldValueRuntimeMeasurerank(int row, String measEleId) {
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            RunTimeMeasure rtMeasure;
            if (this.rtMeasMap != null) {
                rtMeasure = this.rtMeasMap.get(measEleId);
            } else {
                rtMeasure = null;
            }
            if (rtMeasure != null) {
                return rtMeasure.getData(row);//.get(row);
            }
        }
        return new BigDecimal(0);
    }
    
    public BigDecimal getFieldValueRuntimeMeasure(int row, String measEleId) {
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            RunTimeMeasure rtMeasure;
            if (this.rtMeasMap != null) {
                rtMeasure = this.rtMeasMap.get(measEleId);
            } else {
                rtMeasure = null;
            }
            if (rtMeasure != null) {
                return rtMeasure.getData(row);//.get(row);
            }
        }
        return new BigDecimal(0);
    }

    public String getFieldValueRuntimeDimension(int row, String dimEleId) {
        if (RTDimensionElement.isRunTimeDimension(dimEleId)) {
            return this.rtDimMap.get(dimEleId).getData(row);//.get(row);
        }
        return "";
    }

    public String getSignStyleforRankMeasure(String measEleId, String priorMeasEleId, int row) {
        ArrayList<BigDecimal> rankList = this.rtMeasMap.get(measEleId).getData();
        ArrayList<BigDecimal> priorRankList = this.rtMeasMap.get("A_" + priorMeasEleId + RTMeasureElement.RANK.getColumnType()).getData();

        if (rankList.get(row).intValue() < priorRankList.get(row).intValue()) {
            return "positive";
        } else if (rankList.get(row).intValue() > priorRankList.get(row).intValue()) {
            return "negative";
        } else {
            return "nochange";
        }

    }


    /*
     * private char[] getSortDataTypes(ArrayList<String> sortDataTypes) { char[]
     * rowDataTypes = new char[sortDataTypes.size()]; int i = 0; for (String
     * dataType : sortDataTypes) { rowDataTypes[i] = dataType.charAt(0); i++; }
     * return rowDataTypes;
    }
     */
    
    public ArrayList<BigDecimal> retrieveNumericDatarank(String measEleId,Container container) {
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        DataFacade facade = new DataFacade(container);
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            if(measEleId.contains("_PYtdrank")|| measEleId.contains("_PQtdrank")|| measEleId.contains("_PMtdrank")|| measEleId.contains("_Qtdrank")|| measEleId.contains("_Ytdrank")){
        measEleId=measEleId.replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "");

                RunTimeMeasure rtMeasure;
                if (this.rtMeasMap != null) {
                rtMeasure = this.rtMeasMap.get(measEleId);
            } else {
                rtMeasure = null;
            }
               if (rtMeasure != null) {

                      for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measEleId));}
                  }else{
                      measEleId=measEleId.replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PMtdrank", "").replace("_Qtdrank", "").replace("_Ytdrank", "");
                     String measrid=measEleId;
                      for (int i = 0; i < this.rowCount; i++) {
                          
                dataLst.add(facade.getFormattedMeasureDatarank(i,measrid));}
            
                  }
                  
            }else{
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measEleId));
            }
            }
        } else {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueBigDecimal(i, measEleId));
            }
        }
        return dataLst;
    }

    public ArrayList<BigDecimal> retrieveNumericData(String measEleId) {
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measEleId));
            }
        } else {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueBigDecimal(i, measEleId));
            }
        }
        return dataLst;
    }

    public ArrayList<Object> retrieveData(String column) {
//       // long startTime = System.currentTimeMillis();
        ArrayList<Object> dataLst = new ArrayList<Object>(this.rowCount);

        int colType = getColumnType(column);

        //Number can be measure alone?
        if (isNumberColumn(colType)) {
            for (Object data : retrieveNumericData(column)) {
                dataLst.add(data);
            }
        } else if (isTextColumn(colType)) {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueString(i, column));
            }
        } else if (isDateColumn(colType)) {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueDate(i, column));
            }
        }

//       // long endTime = System.currentTimeMillis();
//        
        return dataLst;
    }

    public ArrayList<BigDecimal> retrieveDataBasedOnViewSeq(String measEleId) {
        // long startTime = System.currentTimeMillis();
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        int row;
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            for (int i = 0; i < viewSequence.size(); i++) {
                row = viewSequence.get(i);
                dataLst.add(this.getFieldValueRuntimeMeasure(row, measEleId));
            }

        } else {
            for (int i = 0; i < viewSequence.size(); i++) {
                row = viewSequence.get(i);
                dataLst.add(this.getFieldValueBigDecimal(row, measEleId));
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return dataLst;
    }

    public Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes) {
        // long startTime = System.currentTimeMillis();
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][sortColumns.size()];
        int j;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : sortColumns) {
                if (rowDataTypes[j] == 'N') {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i));//this.getFieldValueBigDecimal(i,column);
                } else if (rowDataTypes[j] == 'C') {//
                    try {
                        String data1 = "";
                        if (column.equalsIgnoreCase("A_TIME") || column.equalsIgnoreCase("A_O_TIME") || column.equalsIgnoreCase("TIME")) {
                            data1 = (this.getDimensionColumnDate(column, this.viewSequence.get(i)));
                        } else {
                            data1 = this.getDimensionColumnData(column, this.viewSequence.get(i));
                        }
                        cellData = data1;
                    } catch (Exception exception) {
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }

                } else {
                    try {
                        BigDecimal bigDecimal1 = new BigDecimal(this.getDimensionColumnData(column, this.viewSequence.get(i)));
                        cellData = bigDecimal1; //hardcoding not good assuming C is Dimension
                    } catch (Exception exception) {
                        //
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }
                }
                data[i][j] = cellData;
                j++;
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return data;
    }

    public Object[][] retrieveDataBasedOnViewSeq(ArrayList<String> columns) {
        // long startTime = System.currentTimeMillis();
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][columns.size()];
        int j;
        int columnType;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : columns) {
                columnType = this.getColumnType(column);

                if (isNumberColumn(columnType)) {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i));//this.getFieldValueBigDecimal(i,column);
                } else {
                    cellData = this.getDimensionColumnData(column, this.viewSequence.get(i)); //hardcoding not good assuming C is Dimension
                }
                data[i][j] = cellData;
                j++;
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return data;
    }

    private boolean isNumberColumn(int colType) {
        if (Types.BIGINT == colType
                || Types.DECIMAL == colType
                || Types.NUMERIC == colType
                || Types.DOUBLE == colType
                || Types.FLOAT == colType
                || Types.INTEGER == colType
                || Types.SMALLINT == colType) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTextColumn(int colType) {
        if (Types.CHAR == colType
                || Types.NCHAR == colType
                || Types.NVARCHAR == colType
                || Types.VARCHAR == colType) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDateColumn(int colType) {
        if (Types.DATE == colType) {
            return true;
        } else {
            return false;
        }
    }

    private int getColumnType(String columnName) {
        int index = 0;
        if (RTMeasureElement.isRunTimeMeasure(columnName)) {
            return Types.NUMERIC;
        }
        if (RTDimensionElement.isRunTimeDimension(columnName)) {
            return Types.VARCHAR;
        }
        for (String column : cols) {
            if (column.equalsIgnoreCase(columnName)) {
                break;
            }
            index++;
        }
        return columnTypesInt[index];
    }

    private BigDecimal getMeasureColumnData(String column, int row) {
        BigDecimal data;
        if (RTMeasureElement.isRunTimeMeasure(column)) {
            data = this.getFieldValueRuntimeMeasure(row, column);
        } else {
            data = this.getFieldValueBigDecimal(row, column);
        }
        return data;
    }

    private String getDimensionColumnData(String column, int row) {
        String data;
        if (RTDimensionElement.isRunTimeDimension(column)) {
            data = this.getFieldValueRuntimeDimension(row, column);
        } else {
            data = this.getFieldValueString(row, column);
        }
        return data;
    }

    private String getDimensionColumnDate(String column, int row) {

        return this.getFieldValueStringDate(row, column);

    }

    /*
     * public PbReturnObject searchModified(ArrayList srchColumns, ArrayList
     * patterns, ArrayList origColumns, ArrayList dataTypes) {
     *
     * PbReturnObject toRet = null; String exists = ""; String colCells = "";
     * String rowValues = ""; Object[] types = null; String whereClause = "";
     * String fileName = null; String tabNamePattern = "tabPattern";
     * StringBuffer sb = new StringBuffer(); String[] columns = (String[])
     * origColumns.toArray(new String[0]);
     *
     * try { fileName = System.currentTimeMillis() + ".xls";
     * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); String driver =
     * "Driver=Microsoft Excel Driver (*.xls);ReadOnly=1;DBQ=" + fileName;
     * Connection connection = DriverManager.getConnection("jdbc:odbc:" +
     * driver);
     *
     * Statement statement = connection.createStatement();
     *
     * if (connection.isReadOnly()) { connection.setReadOnly(false); }
     *
     * DatabaseMetaData databasemetadata = connection.getMetaData(); for
     * (ResultSet rs = databasemetadata.getTables(null, null, tabNamePattern,
     * new String[]{"TABLE"}); rs.next();) { exists = "exists"; }
     *
     * types = dataTypes.toArray(); if (!exists.equals("exists")) { int rCnt =
     * this.rowCount; int colCnt = columns.length; for (int j = 0; j < colCnt;
     * j++) { if ("C".equals(String.valueOf(types[j]))) { colCells = colCells +
     * columns[j] + " TEXT,"; } else if ("N".equals(String.valueOf(types[j]))) {
     * colCells = colCells + columns[j] + " NUMBER,"; } else if
     * ("D".equals(String.valueOf(types[j]))) { colCells = colCells + columns[j]
     * + " DATE,"; }
     *
     * }
     *
     * colCells = colCells.substring(0, colCells.length() - 1);
     * sb.append("create table " + tabNamePattern); sb.append("(" + colCells +
     * ")");
     *
     * boolean flag = statement.execute(sb.toString());
     *
     * for (int i = 0; i < rCnt; i++) { for (int j = 0; j < colCnt; j++) { if
     * ("C".equals(String.valueOf(types[j]))) { rowValues = rowValues + "'" +
     * this.getFieldValueString(i, columns[j]) + "',"; } else if
     * ("N".equals(String.valueOf(types[j]))) { if
     * (columns[j].lastIndexOf("_percentwise") != -1) { columns[j] =
     * columns[j].replace("_percentwise", ""); //MathContext.DECIMAL128
     * rowValues = rowValues + "'" + this.getFieldValueBigDecimal(i,
     * columns[j]).divide(getColumnGrandTotalValue(columns[j]), 2).multiply(new
     * BigDecimal("100")) + "',"; } else { rowValues = rowValues + "'" +
     * this.getFieldValueString(i, columns[j]) + "',"; } } else if
     * ("D".equals(String.valueOf(types[j]))) { rowValues = rowValues + "'" +
     * this.getFieldValueDateString(i, columns[j]) + "',"; } } rowValues =
     * rowValues.substring(0, rowValues.length() - 1); rowValues = "insert into
     * [" + tabNamePattern + "$] values (" + rowValues + ")";
     *
     * statement.executeUpdate(rowValues); rowValues = ""; } }
     *
     * connection.close(); connection = DriverManager.getConnection("jdbc:odbc:"
     * + driver); connection.setReadOnly(true); Statement statement1 =
     * connection.createStatement();
     *
     * int indx = 0; String dtyp = ""; String pattern = ""; String clause = "";
     * StringTokenizer stk = null; for (int i = 0; i < srchColumns.size(); i++)
     * { indx = origColumns.indexOf(srchColumns.get(i)); dtyp =
     * String.valueOf(dataTypes.get(indx)); pattern =
     * String.valueOf(patterns.get(i)); pattern = pattern.trim();
     *
     * if ("C".equals(dtyp)) { if (pattern.indexOf("*") >= 0) { clause = " LIKE
     * "; pattern = pattern.replace('*', '%'); } else { clause = " = "; }
     * whereClause += String.valueOf(srchColumns.get(i)); whereClause += clause
     * + "'"; whereClause += pattern + "'"; whereClause += " AND "; } else if
     * ("N".equals(dtyp)) { if (pattern.startsWith(">") ||
     * pattern.startsWith("<")) { whereClause +=
     * String.valueOf(srchColumns.get(i)); whereClause += pattern; whereClause
     * += " AND "; } else if (pattern.startsWith("BT")) { pattern =
     * pattern.substring(2); pattern = pattern.trim(); stk = new
     * StringTokenizer(pattern, ",");
     *
     * whereClause += String.valueOf(srchColumns.get(i)); if
     * (stk.hasMoreTokens()) { whereClause += " BETWEEN " + stk.nextToken() + "
     * AND "; } if (stk.hasMoreTokens()) { whereClause += stk.nextToken(); }
     * whereClause += " AND "; } else { whereClause +=
     * String.valueOf(srchColumns.get(i)); whereClause += " = "; whereClause +=
     * pattern; whereClause += " AND "; } } } whereClause =
     * whereClause.substring(0, whereClause.length() - 4); String selQuery =
     * "select * from [" + tabNamePattern + "$]" + " WHERE " + whereClause;
     *
     *
     * ResultSet rs1 = statement1.executeQuery(selQuery); toRet = new
     * PbReturnObject(rs1, true); connection.close(); } catch (Exception e) {
     * logger.error("Exception:",e); }
     *
     * try { java.io.File file = new java.io.File(fileName); file.delete(); }
     * catch (Exception e) { logger.error("Exception:",e); }
     *
     * return toRet;
     *
     * }
     */
    //end of modified sort and search methods by santhosh.kumar@progenbusiness.com on 08-02-2010
    public static void main(String[] args) {
        PbReturnObject ret = new PbReturnObject();
        double bd = 30.012;
        double bd1 = 0.0;
        String temp = "";
        Object obj = null;


        /*
         * for (int i = 20; i >= 0; i--) { if ((bd / ret.getPowerOfTen(i)) >
         * 0.99) { bd1 = (bd / ret.getPowerOfTen(i)); bd1 = Math.ceil(bd1) *
         * ret.getPowerOfTen(i);
         *
         * break; } }
         */
    }

    public TreeMap getRowGrandTotals() {
        return rowGrandTotals;
    }

    public void setRowGrandTotals(TreeMap rowGrandTotals) {
        this.rowGrandTotals = rowGrandTotals;
    }

    public int[] getColumnTypesInt() {
        int[] colTypesInt = new int[columnTypesInt.length];
        for (int i = 0; i < columnTypesInt.length; i++) {
            colTypesInt[i] = columnTypesInt[i];
        }
        return colTypesInt;
    }

    public void setColumnTypesInt(int[] columnTypesInt) {
        this.columnTypesInt = new Integer[columnTypesInt.length];
        for (int i = 0; i < columnTypesInt.length; i++) {
            this.columnTypesInt[i] = columnTypesInt[i];
        }
    }

    public void addColumn(int position, String columnName, int columnType) {
        List<Integer> columnTypesLst = Arrays.asList(this.columnTypesInt);
        ArrayList<Integer> newColumnTypes = new ArrayList<Integer>();
        newColumnTypes.addAll(columnTypesLst);
        newColumnTypes.add(position, columnType);
        this.columnTypesInt = (Integer[]) newColumnTypes.toArray(new Integer[]{});

        List<String> colList = Arrays.asList(this.cols);
        ArrayList<String> newColumns = new ArrayList<String>();
        newColumns.addAll(colList);
        newColumns.add(position, columnName);
        cols = (String[]) newColumns.toArray(new String[]{});
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * @return the grandTotals
     */
    public BigDecimal[] getGrandTotals() {
        return grandTotals;
    }

    /**
     * @param grandTotals the grandTotals to set
     */
    public void setGrandTotals(BigDecimal[] grandTotals) {
        this.grandTotals = grandTotals;
    }

    /**
     * @return the avgTotals
     */
    public BigDecimal[] getAvgTotals() {
        return avgTotals;
    }

    /**
     * @param avgTotals the avgTotals to set
     */
    public void setAvgTotals(BigDecimal[] avgTotals) {
        this.avgTotals = avgTotals;
    }

    /**
     * @return the max
     */
    public BigDecimal[] getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(BigDecimal[] max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public BigDecimal[] getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(BigDecimal[] min) {
        this.min = min;
    }

    /**
     * @return the processGT
     */
    public boolean isProcessGT() {
        return processGT;
    }

    /**
     * @param processGT the processGT to set
     */
    public void setProcessGT(boolean processGT) {
        this.processGT = processGT;
    }
    //end of code added by santhosh.k on 01-03-2010

    public void prepareObject(PbReturnObject retObj, ArrayList<Integer> viewSequence) {
        // long startTime = System.currentTimeMillis();

        try {

            retObj.colCount = retObj.getColumnCount();

            retObj.grandTotals = new BigDecimal[colCount];
            retObj.avgTotals = new BigDecimal[colCount];
            retObj.max = new BigDecimal[colCount];
            retObj.min = new BigDecimal[colCount];

            retObj.columnOverAllMaximums = new TreeMap();
            retObj.columnOverAllMinimums = new TreeMap();
            retObj.columnAverages = new TreeMap();
            retObj.columnGrandTotals = new TreeMap();
            retObj.rowGrandTotals = new TreeMap();

            for (int i = 0; i < retObj.colCount; i++) {

//                grandTotals[i] = new BigDecimal("0");
//                avgTotals[i] = new BigDecimal("0");
//                max[i] = new BigDecimal("0");
//                min[i] = new BigDecimal("0");
                retObj.grandTotals[i] = BigDecimal.ZERO;
                retObj.avgTotals[i] = BigDecimal.ZERO;
                retObj.max[i] = BigDecimal.ZERO;
                retObj.min[i] = BigDecimal.ZERO;
            }

            for (int cnt = 0; cnt < retObj.getViewSequence().size(); cnt++) {

                BigDecimal RowGrandTotal = BigDecimal.ZERO;
                for (int i = 0; i < retObj.getColumnCount(); i++) {
                    {
                        Object Obj = retObj.getFieldValue(retObj.getViewSequence().get(cnt), i);

                        if (retObj.processGT) {
//                            
                            if (retObj.columnTypesInt[i] == Types.BIGINT
                                    || retObj.columnTypesInt[i] == Types.DECIMAL || retObj.columnTypesInt[i] == Types.DOUBLE
                                    || retObj.columnTypesInt[i] == Types.FLOAT || retObj.columnTypesInt[i] == Types.INTEGER
                                    || retObj.columnTypesInt[i] == Types.NUMERIC || retObj.columnTypesInt[i] == Types.REAL
                                    || retObj.columnTypesInt[i] == Types.SMALLINT || retObj.columnTypesInt[i] == Types.TINYINT
                                    || retObj.columnTypes[i].equalsIgnoreCase("NUMBER")) {
                                //code to build max,min,acg and grand total of entire record set
                                BigDecimal bdecimal = null;
                                if (Obj != null) {
                                    bdecimal = retObj.getFieldValueBigDecimal(retObj.getViewSequence().get(cnt), i);
                                } else {
                                    bdecimal = BigDecimal.ZERO;
                                }
                                if (cnt == 0) {
                                    retObj.grandTotals[i] = bdecimal;
                                    retObj.max[i] = bdecimal;
                                    retObj.min[i] = bdecimal;
                                } else {
                                    retObj.grandTotals[i] = retObj.grandTotals[i].add(bdecimal);
                                    retObj.max[i] = retObj.max[i].max(bdecimal);
                                    retObj.min[i] = retObj.min[i].min(bdecimal);
                                }
                                //code to buiold row wise grand total
                                if (i == 0) {
                                    RowGrandTotal = bdecimal;
                                } else {
                                    RowGrandTotal = RowGrandTotal.add(bdecimal);
                                }
                                bdecimal = null;
                            }
                        }
                    }
                }
                if (retObj.processGT) {
                    retObj.rowGrandTotals.put("RowGrandTotal_" + retObj.rowCount, RowGrandTotal);
                }
                /// rowCount++;
            }
            if (retObj.rowCount != 0 && retObj.processGT) {
                BigDecimal dividend = new BigDecimal(String.valueOf(retObj.rowCount));

                for (int colIndex = 0; colIndex < retObj.colCount; colIndex++) {
                    retObj.columnOverAllMaximums.put(retObj.cols[colIndex], retObj.max[colIndex]);
                    retObj.columnOverAllMinimums.put(retObj.cols[colIndex], retObj.min[colIndex]);
                    retObj.columnGrandTotals.put(retObj.cols[colIndex], retObj.grandTotals[colIndex]);
                    retObj.avgTotals[colIndex] = retObj.grandTotals[colIndex].divide(dividend, MathContext.DECIMAL64);
                    retObj.columnAverages.put(retObj.cols[colIndex], retObj.avgTotals[colIndex]);
                }
                dividend = null;
            }
            //this.initializeViewSequence();

        } catch (Exception ex) {

            logger.error("Exception:", ex);;
        }
        // long endTime = System.currentTimeMillis();
//        
    }
//end of code needed for totals for viewsequence

    public String getFieldValueStringDate(int row, String colName) {
        //        row = this.getActualRow(row);
        try {
            if (colName.equalsIgnoreCase("A_TIME")) {
                colName = "A_O_TIME";
            }
            String date = (String) ((ArrayList) hMap.get(colName)).get(row);
            return date;
        } catch (Exception e) {
            return null;
        }

    }

    public String[] getCrosstabelements(String colviewby) {
        return this.crosstabelements.get(colviewby);
    }

    public void setCrosstabelements(String[] crosstabelements, String colviewby) {
        this.crosstabelements.put(colviewby, crosstabelements);
    }

    public String getElementaction() {
        return elementaction;
    }

    /**
     * @param elementaction the elementaction to set
     */
    public void setElementaction(String elementaction) {
        this.elementaction = elementaction;
    }

    /**
     * @return the request1
     */
    public HttpServletRequest getRequest1() {
        return request1;
    }

    /**
     * @param request1 the request1 to set
     */
    public void setRequest1(HttpServletRequest request1) {
        this.request1 = request1;
    }

    public ArrayList<BigDecimal> retrieveNumericDataForMultiTime(String measeId, String groupId, String aggType) {
        ArrayList<BigDecimal> dataLst = new ArrayList<BigDecimal>(this.rowCount);
        String prevGroupHeader = null;
        BigDecimal measVal = null;
        BigDecimal groupHeaderVal = null;
        ArrayList<BigDecimal> groupValList = new ArrayList<BigDecimal>();
        if (RTMeasureElement.isRunTimeMeasure(measeId)) {
            for (int i = 0; i < this.rowCount; i++) {
                dataLst.add(this.getFieldValueRuntimeMeasure(i, measeId));
            }

        } else {
            for (int i = 0; i < this.rowCount; i++) {
                measVal = this.getFieldValueBigDecimal(i, measeId);
                String currGroupHeader = getFieldValueString(i, groupId);
                if (prevGroupHeader != null && prevGroupHeader.equalsIgnoreCase(currGroupHeader)) {
                    dataLst.add(measVal);
                    groupValList.add(measVal);
                } else if (prevGroupHeader != null && !prevGroupHeader.equalsIgnoreCase(currGroupHeader)) {
                    groupHeaderVal = getAggrigationResult(groupValList, aggType);
                    dataLst.add(groupHeaderVal);
                    dataLst.add(measVal);
                    groupValList = new ArrayList<BigDecimal>();
                    groupValList.add(measVal);
                    prevGroupHeader = currGroupHeader;

                }
                if (prevGroupHeader == null) {
                    prevGroupHeader = currGroupHeader;
                    dataLst.add(measVal);
                    groupValList.add(measVal);
                }
            }
            if (!groupValList.isEmpty()) {
                groupHeaderVal = getAggrigationResult(groupValList, aggType);
                dataLst.add(groupHeaderVal);
            }
        }

        return dataLst;
    }

    public BigDecimal getAggrigationResult(ArrayList<BigDecimal> valList, String aggType) {
        BigDecimal result = new BigDecimal(0);
        if (aggType.equalsIgnoreCase("avg")) {
            for (BigDecimal val : valList) {
                result = result.add(val);
            }
            result = result.divide(new BigDecimal(valList.size()));
        } else if (aggType.equalsIgnoreCase("sum")) {
            for (BigDecimal val : valList) {
                result = result.add(val);
            }
        } else if (aggType.equalsIgnoreCase("min")) {
            result = (BigDecimal) Collections.min(valList);
        } else if (aggType.equalsIgnoreCase("max")) {
            result = (BigDecimal) Collections.max(valList);
        } else if (aggType.equalsIgnoreCase("count")) {
        }
        return result;
    }

    public boolean checkforChangepercentMsr(String elementid) {
        boolean flag = false;
        String query = "select REF_ELEMENT_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementid;
        PbDb pbdb = new PbDb();
        PbReturnObject retobj;
        try {
            retobj = pbdb.execSelectSQL(query);
            if (retobj.getFieldValueString(0, "REF_ELEMENT_TYPE").equalsIgnoreCase("4")) {
                flag = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return flag;
    }

    public ArrayList<String> getCuurentAndPriorElementIDs(String elementId) {
        String query = "SELECT ELEMENT_ID,REF_ELEMENT_TYPE FROM PRG_USER_ALL_INFO_DETAILS WHERE ref_element_id IN (SELECT ref_element_id FROM PRG_USER_ALL_INFO_DETAILS WHERE element_id=" + elementId + ") order by ref_element_type ";
        PbDb pbdb = new PbDb();
        ArrayList<String> elementIdlist = new ArrayList<String>();
        //HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
        try {
            PbReturnObject retobj = pbdb.execSelectSQL(query);
            if (retobj != null && retobj.getRowCount() > 0) {
                for (int j = 0; j < retobj.getRowCount(); j++) {
                    if (retobj.getFieldValueString(j, 1).equalsIgnoreCase("1") || retobj.getFieldValueString(j, 1).equalsIgnoreCase("2")) {
                        elementIdlist.add(retobj.getFieldValueString(j, 0));
                    }
                }
                // map.put(elementId, elementIdlist);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return elementIdlist;
    }

    public PbReturnObject generateRetObjforExcelImport() {
        PbReturnObject newExcelRetObj = new PbReturnObject();
//       int excelviewbys=importExcelDeatil.getExcelViewbys().size();
        int excelviewbys = this.rowViewBys.size();
        int excelmsrs = importExcelDeatil.getExcelMeasures().size();
        int totalCols = this.cols.length + excelmsrs;
        newExcelRetObj.cols = new String[totalCols];
        newExcelRetObj.columnTypes = new String[totalCols];
        newExcelRetObj.columnTypesInt = new Integer[totalCols];
        newExcelRetObj.columnSizes = new int[totalCols];
        newExcelRetObj.grandTotals = new BigDecimal[totalCols];
        newExcelRetObj.hMap = (HashMap) this.hMap.clone();
        newExcelRetObj.columnGrandTotals = (TreeMap) this.columnGrandTotals.clone();
        newExcelRetObj.rowCount = this.rowCount;
        for (int i = 0; i < this.cols.length; i++) {
            newExcelRetObj.cols[i] = this.cols[i];
            newExcelRetObj.columnTypes[i] = this.columnTypes[i];
            newExcelRetObj.columnTypesInt[i] = this.columnTypesInt[i];
            newExcelRetObj.columnSizes[i] = this.columnSizes[i];
            newExcelRetObj.grandTotals[i] = this.grandTotals[i];
        }
        for (int k = this.cols.length; k < newExcelRetObj.cols.length; k++) {
            newExcelRetObj.grandTotals[k] = BigDecimal.ZERO;
        }
        ArrayListMultimap reportMap = ArrayListMultimap.create();
        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.rowViewCount; j++) {
                reportMap.put(i, this.getFieldValueString(i, j));
            }
        }
        ArrayListMultimap<String, String> map = ArrayListMultimap.create();
        HashMap<List, Integer> viewbyMap = new HashMap<List, Integer>();
        if (this.importExcelRetObj != null && importExcelRetObj.getRowCount() > 0) {
            for (int i = 0; i < importExcelRetObj.getRowCount(); i++) {
                List list = new ArrayList();
                for (int j = 0; j < this.rowViewBys.size(); j++) {
                    list.add(importExcelRetObj.getFieldValueString(i, (String) importExcelDeatil.getRepToExcelMapping().get(rowViewBys.get(j))));
                }
                viewbyMap.put(list, i);
                for (int k = excelviewbys; k < importExcelRetObj.cols.length; k++) {
                    map.put(importExcelRetObj.cols[k], importExcelRetObj.getFieldValueString(i, k));
                }
            }
        }
        for (int k = excelviewbys; k < importExcelRetObj.cols.length; k++) {
            BigDecimal bdecimal = BigDecimal.ZERO;
            List<String> valList = map.get(importExcelRetObj.cols[k]);
            ArrayList colValue = new ArrayList();
            int rownum;
            int index = this.cols.length + k - excelviewbys;
            for (int x = 0; x < reportMap.size(); x++) {
                if (viewbyMap.containsKey(reportMap.get(x))) {
                    rownum = viewbyMap.get(reportMap.get(x));
                    colValue.add(valList.get(rownum));
                    bdecimal = new BigDecimal((String) valList.get(rownum));
                    newExcelRetObj.grandTotals[index] = newExcelRetObj.grandTotals[index].add(bdecimal);
                } else {
                    colValue.add(0);
                    newExcelRetObj.grandTotals[index] = newExcelRetObj.grandTotals[index].add(bdecimal);
                }
            }
            newExcelRetObj.hMap.put(importExcelRetObj.cols[k], colValue);
            newExcelRetObj.cols[this.cols.length + k - excelviewbys] = importExcelRetObj.cols[k];
            newExcelRetObj.columnTypes[this.cols.length + k - excelviewbys] = importExcelRetObj.columnTypes[k];
            newExcelRetObj.columnTypesInt[this.cols.length + k - excelviewbys] = importExcelRetObj.columnTypesInt[k];
            newExcelRetObj.columnSizes[this.cols.length + k - excelviewbys] = importExcelRetObj.columnSizes[k];
        }
        for (int k = this.cols.length; k < newExcelRetObj.cols.length; k++) {
            newExcelRetObj.columnGrandTotals.put(newExcelRetObj.cols[k], newExcelRetObj.grandTotals[k]);
        }
        newExcelRetObj.initializeViewSequence();
        return newExcelRetObj;
    }

    public PbReturnObject genereateNewReturnObjectForExcel() {
        PbReturnObject newMultiViewbyRetObj = new PbReturnObject();
        int excelviewbys = importExcelDeatil.getExcelViewbys().size();
        int excelmsrs = importExcelDeatil.getExcelMeasures().size();

        ArrayList msrList = importExcelDeatil.getExcelMeasures();
        String[] viewbys = (String[]) this.rowViewBys.toArray(new String[rowViewBys.size()]);
        ArrayList<String> columnNames = new ArrayList<String>();
        for (String viewby : viewbys) {
            columnNames.add((String) importExcelDeatil.getRepToExcelMapping().get(viewby));
        }
        for (String msr : importExcelDeatil.getExcelMeasures()) {
            columnNames.add(msr);
        }
        newMultiViewbyRetObj.setColumnNames(columnNames.toArray(new String[columnNames.size()]));
        newMultiViewbyRetObj.columnTypes = new String[columnNames.size()];
        newMultiViewbyRetObj.columnTypesInt = new Integer[columnNames.size()];
        newMultiViewbyRetObj.columnSizes = new int[columnNames.size()];
        BigDecimal[] currentValues = new BigDecimal[msrList.size()];

        for (int m = 0; m < currentValues.length; m++) {
            currentValues[m] = BigDecimal.ZERO;
        }

        String[] currDimValue = null;
        for (int n = 0; n < importExcelRetObj.getRowCount(); n++) {
            String[] temp = new String[viewbys.length];
            for (int k = 0; k < viewbys.length; k++) {
                temp[k] = importExcelRetObj.getFieldValueString(n, importExcelDeatil.getRepToExcelMapping().get(viewbys[k]));
            }
            if (currDimValue == null) {
                currDimValue = new String[temp.length];
                for (int l = 0; l < temp.length; l++) {
                    currDimValue[l] = temp[l];
                }
            }
            boolean flag = compareStringArray(temp, currDimValue);
            if (flag) {
                for (int i = 0; i < msrList.size(); i++) {
                    BigDecimal bd = importExcelRetObj.getFieldValueBigDecimal(n, (String) msrList.get(i));
                    if (bd != null) {
                        currentValues[i] = currentValues[i].add(bd);
                    }

                }

            } else {
                //Add to the new return object
                for (int k = 0; k < viewbys.length; k++) {
                    newMultiViewbyRetObj.setFieldValue(importExcelDeatil.getRepToExcelMapping().get(viewbys[k]), currDimValue[k]);
                }

                for (int i = 0; i < msrList.size(); i++) {
                    newMultiViewbyRetObj.setFieldValue((String) msrList.get(i), currentValues[i]);

                    BigDecimal bd = importExcelRetObj.getFieldValueBigDecimal(n, (String) msrList.get(i));
                    currentValues[i] = bd;
                }

                newMultiViewbyRetObj.addRow();

                //update the current dim value and clear the current value array
                for (int l = 0; l < temp.length; l++) {
                    currDimValue[l] = temp[l];
                }
            }
            if (n == importExcelRetObj.getRowCount() - 1) {
                for (int k = 0; k < viewbys.length; k++) {
                    newMultiViewbyRetObj.setFieldValue(importExcelDeatil.getRepToExcelMapping().get(viewbys[k]), currDimValue[k]);
                }

                for (int i = 0; i < msrList.size(); i++) {
                    newMultiViewbyRetObj.setFieldValue((String) msrList.get(i), currentValues[i]);

                    BigDecimal bd = importExcelRetObj.getFieldValueBigDecimal(n, (String) msrList.get(i));
                    currentValues[i] = bd;
                }

                newMultiViewbyRetObj.addRow();
            }
        }
        for (int i = 0; i < importExcelRetObj.getColumnCount(); i++) {
            if (columnNames.contains(importExcelRetObj.cols[i])) {
                int index = columnNames.indexOf(importExcelRetObj.cols[i]);
                newMultiViewbyRetObj.columnTypes[index] = importExcelRetObj.columnTypes[i];
                newMultiViewbyRetObj.columnTypesInt[index] = importExcelRetObj.columnTypesInt[i];
                newMultiViewbyRetObj.columnSizes[index] = importExcelRetObj.columnSizes[i];
            }

        }
        newMultiViewbyRetObj.resetViewSequence();
        return newMultiViewbyRetObj;
    }

    private boolean compareStringArray(String[] temp, String[] currDimValue) {
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equalsIgnoreCase(currDimValue[i])) {
                count = count + 1;
            }
        }
        if (count == temp.length) {
            return true;
        } else {
            return false;
        }
    }
    //Added by Amar to get time field value

    public int getFieldValueAfterTime(int row, String colName) {
        try {
            if (getFieldValue(row, colName) == null || getFieldValueString(row, colName).equalsIgnoreCase("")) {
                return 0;
            } else {
                return Integer.parseInt(getFieldValueString(row, colName));
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    // end of code
    /*
     * public PbReturnObject generateRetObjforExcelImport() { PbReturnObject
     * newExcelRetObj = new PbReturnObject(); int
     * excelmsrs=importExcelRetObj.cols.length-rowViewCount; int
     * totalCols=this.cols.length+excelmsrs; rowViewSortedValues = new
     * ArrayList[rowViewCount]; for (int l1 = 0; l1 < rowViewCount; l1++) {
     * rowViewSortedValues[l1] = new ArrayList(); } // for (int
     * recCount=0;recCount<this.rowCount;recCount++){ // for (int colT = 0; colT
     * < rowViewCount; colT++) { //// if (colT < rowViewCount) { //
     * rowViewSortedValues[colT].add(this.getFieldValueString(recCount, colT));
     * //// } // } // } for (int colT = 0; colT < rowViewCount; colT++) {
     * rowViewSortedValues[colT]=(ArrayList)this.hMap.get(this.cols[colT]); }
     * newExcelRetObj.cols=new String[totalCols]; newExcelRetObj.columnTypes=new
     * String[totalCols]; newExcelRetObj.columnTypesInt=new Integer[totalCols];
     * newExcelRetObj.columnSizes=new int[totalCols]; newExcelRetObj.grandTotals
     * = new BigDecimal[totalCols]; newExcelRetObj.hMap=(HashMap)
     * this.hMap.clone();
     * newExcelRetObj.columnGrandTotals=(TreeMap)this.columnGrandTotals.clone();
     * newExcelRetObj.rowCount=this.rowCount; for(int
     * i=0;i<this.cols.length;i++){ newExcelRetObj.cols[i]=this.cols[i];
     * newExcelRetObj.columnTypes[i]=this.columnTypes[i];
     * newExcelRetObj.columnTypesInt[i]=this.columnTypesInt[i];
     * newExcelRetObj.columnSizes[i]=this.columnSizes[i];
     * newExcelRetObj.grandTotals[i]= this.grandTotals[i]; }
     *
     * ArrayListMultimap<String, String> map = ArrayListMultimap.create();
     * HashMap<String,Integer> viewbyMap=new HashMap<String, Integer>();
     * if(this.importExcelRetObj!=null && importExcelRetObj.getRowCount()>0){
     * for(int i=0;i<importExcelRetObj.getRowCount();i++){ for(int
     * j=0;j<importExcelRetObj.cols.length;j++){
     * viewbyMap.put(importExcelRetObj.getFieldValueString(i,j), i); //
     * map.put("A_"+importExcelRetObj.cols[j],importExcelRetObj.getFieldValueString(i,
     * j) );
     * map.put(importExcelRetObj.cols[j],importExcelRetObj.getFieldValueString(i,
     * j) ); } }
     *
     * for(int k=this.cols.length;k<newExcelRetObj.cols.length;k++)
     * newExcelRetObj.grandTotals[k] = BigDecimal.ZERO;
     *
     * // for(int k=0;k<importExcelRetObj.cols.length;k++){ for(int
     * k=1;k<importExcelRetObj.cols.length;k++){ // List<String>
     * valList=map.get("A_"+importExcelRetObj.cols[k]); int
     * index=this.cols.length+(k-1); BigDecimal bdecimal = BigDecimal.ZERO;
     * List<String> valList=map.get(importExcelRetObj.cols[k]); ArrayList
     * list=new ArrayList(valList); // ArrayList list=new ArrayList(); ArrayList
     * colValue=new ArrayList(); // for(String val:valList) // list.add(val);
     * int rownum; for(int x=0;x<rowViewSortedValues[0].size();x++){
     * if(viewbyMap.containsKey(rowViewSortedValues[0].get(x))) {
     * rownum=viewbyMap.get(rowViewSortedValues[0].get(x));
     * colValue.add(list.get(rownum)); bdecimal=new
     * BigDecimal((String)list.get(rownum)); newExcelRetObj.grandTotals[index] =
     * newExcelRetObj.grandTotals[index].add(bdecimal); }else{ colValue.add(0);
     * newExcelRetObj.grandTotals[index] =
     * newExcelRetObj.grandTotals[index].add(bdecimal); } }
     * newExcelRetObj.hMap.put(importExcelRetObj.cols[k],colValue);
     * newExcelRetObj.cols[this.cols.length+k-1]= importExcelRetObj.cols[k];
     * newExcelRetObj.columnTypes[this.cols.length+k-1]=
     * importExcelRetObj.columnTypes[k];
     * newExcelRetObj.columnTypesInt[this.cols.length+k-1]=importExcelRetObj.columnTypesInt[k];
     * newExcelRetObj.columnSizes[this.cols.length+k-1]=
     * importExcelRetObj.columnSizes[k]; } } for(int
     * k=this.cols.length;k<newExcelRetObj.cols.length;k++)
     * newExcelRetObj.columnGrandTotals.put(newExcelRetObj.cols[k],
     * newExcelRetObj.grandTotals[k]); newExcelRetObj.initializeViewSequence();
     * return newExcelRetObj; }
     */

    // Added by Amar
    public PbReturnObject transposeExportReturnObject() {
        PbReturnObject newCrossPb = new PbReturnObject();
        if (rowViewCount == 0) {
            rowViewBys.add("Measure");
        }
        //code written by swathi purpose of summerizedmsrs
        ArrayList<String> summerizedQryeIds = new ArrayList<String>();
        ArrayList<String> summerizedQryAggregations = new ArrayList<String>();
        ArrayList<String> summerizedQryColNames = new ArrayList<String>();
        ArrayList<String> summerizedQryColTypes = new ArrayList<String>();
        if (this.summarizedMeasuresEnabled) {
            summerizedQryeIds.addAll((List<String>) summerizedTableHashMap.get("summerizedQryeIds"));
            summerizedQryAggregations.addAll((List<String>) summerizedTableHashMap.get("summerizedQryAggregations"));
            summerizedQryColNames.addAll((List<String>) summerizedTableHashMap.get("summerizedQryColNames"));
            summerizedQryColTypes.addAll((List<String>) summerizedTableHashMap.get("summerizedQryColTypes"));
        }
        rowViewValues = new ArrayList[rowViewCount];
        rowViewSortedValues = new ArrayList[rowViewCount];
        rowViewSortedValueDataType = new String[rowViewCount];
        rowViewSortValues = new ArrayList[rowViewCount];
        colViewValues = new ArrayList[colViewCount];
        subPosition = new HashMap[colViewCount];
        colViewSortedValues = new ArrayList[colViewCount];
        colViewSortValues = new ArrayList[colViewCount];
        finalColSpanList = new ArrayList[colViewCount + 1];
        GTCol = new ArrayList[Qrycolumns.size()];
        SubTCol = new ArrayList[colViewCount][Qrycolumns.size()];
        finalColViewSortedValues = new ArrayList[colViewCount + 1];
        ArrayList<String> newColumnTypes = new ArrayList();
        ArrayList<Integer> newColumnTypesInt = new ArrayList<Integer>();
        ArrayList<Integer> newColumnSizes = new ArrayList<Integer>();
        ArrayList<Integer> newQryPositions = new ArrayList<Integer>();
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            rowViewValues[l1] = new ArrayList();
            rowViewSortedValues[l1] = new ArrayList();
            rowViewSortValues[l1] = new ArrayList();

        }
        for (int l1 = 0; l1 < colViewCount; l1++) {

            colViewValues[l1] = new ArrayList();
            colViewSortedValues[l1] = new ArrayList();
            colViewSortValues[l1] = new ArrayList();
            subPosition[l1] = new HashMap<String, HashMap>();
        }
        for (int l1 = 0; l1 < colViewCount + 1; l1++) {

            finalColSpanList[l1] = new ArrayList();
            finalColViewSortedValues[l1] = new ArrayList();
        }

        totalColBefore = rowViewCount * 2 + colViewCount * 2;  //with there sort cols
        int colRepeat[] = new int[colViewCount];

        for (int recCount = 0; recCount < this.rowCount; recCount++) {
            String rowKey = "";
            String colKey = "";
            String totalKey = "";

            //Code to get Keys
            for (int colT = 0; colT < totalViewBys; colT++) {
                if (colT < rowViewCount) {
                    rowKey += ";;" + this.getFieldValueString(recCount, colT);
                    //
                    rowViewValues[colT].add(this.getFieldValueString(recCount, colT));
                    rowViewSortValues[colT].add(this.getFieldValueString(recCount, colT + totalViewBys));
                    rowViewSortedValueDataType[colT] = this.columnTypes[colT + totalViewBys];
                    rowViewSortedValueDataTypeInt.add(this.columnTypesInt[colT + totalViewBys]);

                }
                if (colT >= rowViewCount && colT < totalViewBys) {
                    colKey += ";;" + this.getFieldValueString(recCount, colT);
                    colViewValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT));
                    colViewSortValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT + totalViewBys));

                }
                totalKey += ";;" + this.getFieldValueString(recCount, colT);

            }
            if (rowValidList == null || (!rowValidList.contains(rowKey))) {
                rowValidList.add(rowKey);
                {
                    for (int colT = 0; colT < rowViewCount; colT++) {
                        if (colT < rowViewCount) {
                            rowViewSortedValues[colT].add(this.getFieldValueString(recCount, colT));
                        }
                    }

                }
            }
            String s = null;
            if (colValidList == null || (!colValidList.contains(colKey))) {
                colValidList.add(colKey);
                {
                    for (int colT = rowViewCount; colT < totalViewBys; colT++) {
                        if (colT >= rowViewCount && colT < totalViewBys) {
                            colViewSortedValues[colT - rowViewCount].add(this.getFieldValueString(recCount, colT));
                        }
                    }

                }
            }
            String k = null;
            if (combineValidList == null || (!combineValidList.contains(totalKey))) {
                combineValidList.add(totalKey);
            }
            combinedHash.put(totalKey, recCount);

        }
        String g = null;
        if (this.getCrosstabelements(ColViewBys.get(0).toString()) != null) {
            for (int colT = rowViewCount; colT < totalViewBys; colT++) {
                if (colT >= rowViewCount && colT < totalViewBys) {
                    colViewSortedValues[colT - rowViewCount].clear();
                }
            }

            ArrayList colViewSortedValues1 = new ArrayList();
            String crosstabelement[] = this.getCrosstabelements(ColViewBys.get(0).toString());
            for (int colT = 0; colT < crosstabelement.length; colT++) {
                colViewSortedValues1.add(crosstabelement[colT]);
            }
            colViewSortedValues[0].addAll(colViewSortedValues1);
        }
        String[] tempName = new String[colViewCount];
        ArrayList<Integer>[] colSpanList = new ArrayList[colViewCount];

        //finalColSpanList1= new ArrayList[colViewCount];
        ArrayList<String>[] colTotalQuery = new ArrayList[colViewCount];
        ArrayList<ArrayList>[] colTotalQueryTitles = new ArrayList[colViewCount];
        String[] oldValList = new String[colViewCount];
        String[] newValList = new String[colViewCount];
        ArrayList<Integer>[] nextBreakList = new ArrayList[colViewCount - 1];

        boolean[] breakChild = new boolean[colViewCount];
        int[] collectVal = new int[colViewCount];
        int count = 0;

        for (int l1 = 0; l1 < colViewCount; l1++) {
            colSpanList[l1] = new ArrayList<Integer>();

            oldValList[l1] = "";
            newValList[l1] = "";
            breakChild[l1] = false;
            collectVal[l1] = 1;
            colTotalQuery[l1] = new ArrayList<String>();
            colTotalQueryTitles[l1] = new ArrayList<ArrayList>();
            if (l1 < colViewCount - 1) {
                nextBreakList[l1] = new ArrayList<Integer>();
            }
        }
        for (int lLoop = 0; lLoop < colViewSortedValues[0].size(); lLoop++) {
            count++;
            for (int l1 = 0; l1 < colViewCount; l1++) {
                tempName[l1] = colViewSortedValues[l1].get(lLoop).toString();
                //////////////////
                //  
                if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (colSpanList[l1] == null || colSpanList[l1].size() <= 1) {
                        colSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        colSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        //
                        breakChild[l1 - 1] = false;
                        //
                    }
                }
                String totalClause = "";
                if (breakChild[l1] || lLoop == 1) {
                    colTotalQuery[l1].add(colViewSortedValues[l1].get(lLoop).toString());
                }

            }

        }/// End of resultset while loop

        for (int l1 = 0; l1 < colViewCount; l1++) {
            {
                int j = ((collectVal[l1]));
                colSpanList[l1].add(j);
            }
        }
        oColumnList = new ArrayList();
        oColumnList.addAll(Arrays.asList(this.getColumnNames()));

        // 
        ArrayList MeasureSpan = new ArrayList();
        for (int l1 = 0; l1 < Qrycolumns.size(); l1++) {//Use as dummy Loop
            MeasureSpan.add(colViewSortedValues[0].size());
            if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(l1).toString())) {
                queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(l1).toString()).toString());
            } else {
                queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(l1).toString()).toString());
            }
            queryMeasureName.add("A_" + Qrycolumns.get(l1).toString());
            newColumnSizes.add(this.columnSizes[l1 + totalColBefore]);
            newColumnTypes.add(this.columnTypes[l1 + totalColBefore]);
            newColumnTypesInt.add(this.columnTypesInt[l1 + totalColBefore]);
            newQryPositions.add(l1 + totalColBefore);

        }
        int totalColSize = colViewCount - 1;
        if (meausreOnCol) {
            totalColSize = colViewCount;
        }
        ///
        for (int iloop = 0; iloop <= totalColSize; iloop++) {
            if (iloop == 0 && totalColSize <= 1) {
                isSubTotalReq.add("N");
            } else if (MeasurePos == iloop) {
                isSubTotalReq.add("N");
            } else if (MeasurePos < iloop && iloop != totalColSize) {
                isSubTotalReq.add("T");
            } else if (iloop < MeasurePos && iloop != totalColSize - 1) {
                isSubTotalReq.add("T");
            } else {
                isSubTotalReq.add("N");
            }
        }
        // 
        for (int l1 = 0, sList = 0; l1 <= totalColSize; l1++) {//Use as dummy Loop
            ///

            if (MeasurePos == l1) {
                sList = 1;
//                        finalColSpanList;
//                        finalColViewSortedValues;
                for (int j = 0; j < MeasureSpan.size(); j++) {
                    //
                    for (int i = l1; i <= colViewSortedValues.length; i++) {
                        for (int k = 0; k < Integer.parseInt(MeasureSpan.get(j).toString()); k++) {
                            if (i == l1) {
                                if (queryColName.isEmpty()) {
                                    for (int l11 = 0; l11 < Qrycolumns.size(); l11++) {//Use as dummy Loop
                                        MeasureSpan.add(colViewSortedValues[0].size());
                                        if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(l11).toString())) {
                                            queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(l11).toString()).toString());
                                        } else {
                                            queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(l11).toString()).toString());
                                        }
                                        queryMeasureName.add("A_" + Qrycolumns.get(l11).toString());
                                        newColumnSizes.add(this.columnSizes[l11 + totalColBefore]);
                                        newColumnTypes.add(this.columnTypes[l11 + totalColBefore]);
                                        newColumnTypesInt.add(this.columnTypesInt[l11 + totalColBefore]);
                                        newQryPositions.add(l11 + totalColBefore);
                                    }
                                }
//                                        
                                finalColViewSortedValues[i].add(queryColName.get(j));
                                finalQueryMeasureName.add(queryMeasureName.get(j));
                                finalColumnSizes.add(newColumnSizes.get(j));
                                finalColumnTypes.add(newColumnTypes.get(j));
                                finalColumnTypesInt.add(newColumnTypesInt.get(j));
                                finalQryPositions.add(newQryPositions.get(j));
                                //newQryPositions.add(oColumnList.indexOf("A_"+Qrycolumns.get(j).toString()));
                            }
                            if (i < colViewCount) {
                                finalColViewSortedValues[i + sList].add(colViewSortedValues[i].get(k));
                            }
                        }
                    }
                }
                break;
            } else {
                MeasureSpan = new ArrayList();
                queryColName = new ArrayList();
                newQryPositions = new ArrayList<Integer>();
                queryMeasureName = new ArrayList();
                ArrayList newList = new ArrayList();
                newColumnTypes = new ArrayList<String>();
                newColumnTypesInt = new ArrayList<Integer>();
                newColumnSizes = new ArrayList<Integer>();
                for (int k = 0; k < colViewSortedValues[l1].size(); k++) {
                    for (int j = 0; j < Qrycolumns.size(); j++) {
                        if (crosstabMsrMap != null && crosstabMsrMap.containsKey("A_" + Qrycolumns.get(j).toString())) {
                            queryColName.add(crosstabMsrMap.get("A_" + Qrycolumns.get(j).toString()).toString());
                        } else {
                            queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(j).toString()).toString());
                        }
                        queryMeasureName.add("A_" + Qrycolumns.get(j).toString());
                        newColumnSizes.add(this.columnSizes[j + totalColBefore]);
                        newColumnTypes.add(this.columnTypes[j + totalColBefore]);
                        newColumnTypesInt.add(this.columnTypesInt[j + totalColBefore]);
                        //newQryPositions.add(j+totalColBefore);
                        newQryPositions.add(oColumnList.indexOf("A_" + Qrycolumns.get(j).toString()));
                        finalColViewSortedValues[l1].add(colViewSortedValues[l1].get(k));
                    }
                }
                for (int k = 0; k < colSpanList[l1].size(); k++) {
                    newList.add(Integer.parseInt(colSpanList[l1].get(k).toString()) * Qrycolumns.size());
                    for (int j = 0; j < Qrycolumns.size(); j++) {
                        MeasureSpan.add((colSpanList[l1].get(k).toString()));
                    }

                }
                colSpanList[l1] = newList;
            }
        }
        //Processing for hashMap and finalColspan

        tempName = new String[colViewCount + 1];
        oldValList = new String[colViewCount + 1];
        newValList = new String[colViewCount + 1];
        breakChild = new boolean[colViewCount + 1];
        collectVal = new int[colViewCount + 1];
        count = 0;
        //code written by swathi purpose of summerizedmsrs in CTReports
        //code ended
        if (summarizedMeasuresEnabled) {
            for (int i = 0; i < summerizedQryeIds.size(); i++) {
                if (reportDrillMap.get("A_" + summerizedQryeIds.get(i)) != null && !reportDrillMap.get("A_" + summerizedQryeIds.get(i)).isEmpty()) {
                    finalCrossTabReportDrillMap.put("A_" + summerizedQryeIds.get(i), reportDrillMap.get("A_" + summerizedQryeIds.get(i)));
                }
            }
        }
        for (int lLoop = 0; lLoop < finalColViewSortedValues[0].size(); lLoop++) {
            String colKey = "";

            count++;

            ArrayList forViewMap = new ArrayList();
            for (int l1 = 0; l1 <= totalColSize; l1++) {
                tempName[l1] = finalColViewSortedValues[l1].get(lLoop).toString();
                forViewMap.add(tempName[l1]);
                if (MeasurePos != l1) {
                    colKey += ";;" + tempName[l1];
                }
                if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (finalColSpanList[l1] == null || finalColSpanList[l1].size() <= 1) {
                        finalColSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        finalColSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        //
                        breakChild[l1 - 1] = false;
                        //
                    }
                }
                String totalClause = "";

            }
            nonViewByMapNew.put("A" + colGenerator, forViewMap);
            crosstabMeasureId.put("A" + colGenerator, finalQueryMeasureName.get(lLoop));
            if (!crosstablist.contains(finalQueryMeasureName.get(lLoop))) {
                crosstablist.add(finalQueryMeasureName.get(lLoop));

            }
            if (!crosstablist.isEmpty()) {
                if (lLoop1 < crosstablist.size()) {
                    crosstabmeasureIdsmap.put(crosstablist.get(lLoop1), "A" + colGenerator);
                    lLoop1++;
                }
            }
            CrossTabfinalOrder.add("A" + colGenerator);
            newcolValidList.add(colKey);
            //written by swati
            finalCrossTabReportDrillMap.put("A" + colGenerator, reportDrillMap.get(finalQueryMeasureName.get(lLoop)));
            colGenerator++;

        }/// End of resultset while loop
        for (int l1 = 0; l1 <= totalColSize; l1++) {
            {
                int j = ((collectVal[l1]));
                finalColSpanList[l1].add(j);
            }
        }
        int addColumns = 0;
        if (!isGTNone) {
            addColumns = Qrycolumns.size();//For GT
        }
        if (!isSTNone) {
            for (int iloop = 0; iloop < finalColSpanList.length; iloop++) {
                // 
                if (isSubTotalReq.get(iloop).equals("T")) {
                    if (MeasurePos > iloop) {
                        addColumns = addColumns + (finalColSpanList[iloop].size() * Qrycolumns.size());
                    } else {
                        addColumns = addColumns + (finalColSpanList[iloop].size() * 1);
                    }
                    // 
                }
            }
        }
        String colKeys[] = (String[]) nonViewByMapNew.keySet().toArray(new String[0]);
        totalColFilled = rowViewCount + colKeys.length;//Amit comment for bug fic 28 jult 2011
        //totalColFilled=nonViewByMapNew.size()+colKeys.length;

        //code written by swati purpose of summerizedmsrs
        if (summarizedMeasuresEnabled) {
            totalColFilled = totalColFilled + summerizedQryeIds.size();
        }
        //code ended
        int finalsizeofArrays = totalColFilled + addColumns;
        ///Adding columns for sorting
        finalsizeofArrays = finalsizeofArrays + rowViewCount;

        newCrossPb.cols = new String[finalsizeofArrays];
        newCrossPb.columnTypes = new String[finalsizeofArrays];
        newCrossPb.columnTypesInt = new Integer[finalsizeofArrays];
        newCrossPb.columnSizes = new int[finalsizeofArrays];
        // 
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            // 
            newCrossPb.hMap.put("A_" + rowViewBys.get(l1), rowViewSortedValues[l1]);
            // 
            newCrossPb.cols[l1] = "A_" + rowViewBys.get(l1);
            newCrossPb.columnTypes[l1] = this.columnTypes[l1];
            newCrossPb.columnTypesInt[l1] = this.columnTypesInt[l1];
            newCrossPb.columnSizes[l1] = this.columnSizes[l1];
        }

        //codewritten by swati purpose of summerizedmsrs
        int totalCount = rowViewCount;
        if (summarizedMeasuresEnabled) {
            totalCount = totalCount + summerizedQryeIds.size();
            //  ArrayList<String>[] colValue = new ArrayList[summerizedQryeIds.size()];`
            ArrayListMultimap<String, String> map = ArrayListMultimap.create();
            HashMap<String, Integer> viewbyMap = new HashMap<String, Integer>();
            if (summerizedMsrRetObj != null && summerizedMsrRetObj.getRowCount() > 0) {
                for (int i = 0; i < summerizedMsrRetObj.getRowCount(); i++) {
                    for (int j = 0; j < summerizedQryeIds.size(); j++) {// takes only one rowviewby code
                        viewbyMap.put(summerizedMsrRetObj.getFieldValueString(i, j), i);
                        map.put("A_" + summerizedQryeIds.get(j), summerizedMsrRetObj.getFieldValueString(i, "A_" + summerizedQryeIds.get(j)));
                    }
                }
                for (int k = 0; k < summerizedQryeIds.size(); k++) {
                    List<String> valList = map.get("A_" + summerizedQryeIds.get(k));
                    ArrayList list = new ArrayList();
                    ArrayList colValue = new ArrayList();
                    for (String val : valList) {
                        list.add(val);
                    }
                    int rownum;
                    for (int x = 0; x < rowViewSortedValues[0].size(); x++) {
                        if (viewbyMap.containsKey(rowViewSortedValues[0].get(x))) {
                            rownum = viewbyMap.get(rowViewSortedValues[0].get(x));
                            colValue.add(list.get(rownum));
                        }
                    }
                    // 
                    newCrossPb.hMap.put("A_" + summerizedQryeIds.get(k), colValue);
                    newCrossPb.cols[rowViewCount + k] = "A_" + summerizedQryeIds.get(k);
                    newCrossPb.columnTypes[rowViewCount + k] = summerizedMsrRetObj.columnTypes[k + 1];
                    newCrossPb.columnTypesInt[rowViewCount + k] = summerizedMsrRetObj.columnTypesInt[k + 1];
                    newCrossPb.columnSizes[rowViewCount + k] = summerizedMsrRetObj.columnSizes[k + 1];
                }
            }
        }
        //code ended

        for (int cloop = 0; cloop < finalColSpanList.length; cloop++) {
            colSpanCurrindex.add(0);
            colSpanCurrPos.add(finalColSpanList[cloop].get(0));
            colSpanBefPos.add(0);
        }
        for (int iloop = 0; iloop < Qrycolumns.size(); iloop++) {

            GTCol[iloop] = new ArrayList<BigDecimal>();
            //SubTCol[iloop]= new ArrayList<BigDecimal>();
            for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                GTCol[iloop].add(BigDecimal.ZERO);
                //[iloop].add(BigDecimal.ZERO);
            }
        }
        resetSubTotal();
        for (int coli = 0; coli < newcolValidList.size(); coli++) {
            ArrayList colValue = new ArrayList();
            int row = 0;
            int col = 0;

            for (int rowi = 0; rowi < rowViewSortedValues[0].size(); rowi++) {
                BigDecimal b = BigDecimal.ZERO;

                String colKey = rowValidList.get(rowi).toString() + newcolValidList.get(coli);
                // 
                if (combinedHash != null && combinedHash.get(colKey) != null) {
                    row = (combinedHash.get(colKey));
                    // 
                    col = finalQryPositions.get(coli);
                    //
                    if (this.getFieldValue(row, col) != null) {
                        colValue.add(this.getFieldValueString(row, col));
                        b = this.getFieldValueBigDecimal(row, col);

                    } else {
                        //colValue.add(combinedHash.get("0"));
                        colValue.add("0");
                        b = BigDecimal.ZERO;
                    }
                    //
                } else {
                    colValue.add("0");
                    //colValue.add(combinedHash.get("0"));
                }

                if (col >= totalColBefore) {
                    if (b == null) {
                        b = BigDecimal.ZERO;
                    }
                    GTCol[col - totalColBefore].set(rowi, b.add((BigDecimal) GTCol[col - totalColBefore].get(rowi)));
                    for (int cloop = 0; cloop < colViewCount; cloop++) {
                        SubTCol[cloop][col - totalColBefore].set(rowi, b.add((BigDecimal) SubTCol[cloop][col - totalColBefore].get(rowi)));
                    }
                }

            }//end of row forr loop
            // changed by swati purpose of summerized msrs
            newCrossPb.cols[totalCount + coli] = colKeys[coli];
            newCrossPb.columnTypes[totalCount + coli] = finalColumnTypes.get(coli);
            newCrossPb.columnTypesInt[totalCount + coli] = finalColumnTypesInt.get(coli);
            newCrossPb.columnSizes[totalCount + coli] = finalColumnSizes.get(coli);
            newCrossPb.hMap.put(colKeys[coli], colValue);

            if (!isSTNone) {
                processColSubTotal(coli, newCrossPb);
            }
        }

        newCrossPb.resetViewSequence();
        //
        //
        newCrossPb.setRowCount(rowViewSortedValues[0].size());

        if (!isGTNone) {
            addGTTotalToretObj(newCrossPb);
        }
        if (!isSTNone) {
            alterCrossTabDispOrder();
        }
        if (!isGTNone && isSTNone) {
            AddGTinFinalList();
        }
        //ReProcessing for hashMap and finalColspan
        for (int l1 = 0; l1 < colViewCount + 1; l1++) {

            finalColSpanList[l1] = new ArrayList();

        }
        tempName = new String[colViewCount + 1];
        oldValList = new String[colViewCount + 1];
        newValList = new String[colViewCount + 1];
        breakChild = new boolean[colViewCount + 1];
        collectVal = new int[colViewCount + 1];
        count = 0;
        //
        //code written by swati purpose of summerizedmsrs
        if (summarizedMeasuresEnabled) {
            int msrCnt = summerizedQryeIds.size();
            if (msrCnt > 0) {
                for (int j = 0; j < msrCnt; j++) {
                    for (int i = 0; i <= totalColSize; i++) {
                        if (i <= msrCnt) {
                            finalColSpanList[i].add(1);
                        }
                    }
                }
            }

        }
        //code ended
        for (int lLoop = 0; lLoop < finalColViewSortedValues[0].size(); lLoop++) {
            String colKey = "";

            count++;

            ArrayList forViewMap = new ArrayList();
            for (int l1 = 0; l1 <= totalColSize; l1++) {
                tempName[l1] = finalColViewSortedValues[l1].get(lLoop).toString();
                forViewMap.add(tempName[l1]);
                if (MeasurePos != l1) {
                    colKey += ";;" + tempName[l1];
                }
                 if (count == 1) {
                    oldValList[l1] = tempName[l1];
                    newValList[l1] = tempName[l1];
                    collectVal[l1] = 1;

                } else {
                    newValList[l1] = tempName[l1];
                    collectVal[l1]++;
                }

                if ((!newValList[l1].equals(oldValList[l1])) || (l1 > 0 && breakChild[l1 - 1])) {

                    oldValList[l1] = newValList[l1];
                    if (finalColSpanList[l1] == null || finalColSpanList[l1].size() <= 1) {
                        finalColSpanList[l1].add((collectVal[l1] - 1));
                    } else {
                        int j = ((collectVal[l1] - 1));
                        finalColSpanList[l1].add(j);
                    }
                    breakChild[l1] = true;
                    collectVal[l1] = 1;
                    if (l1 >= 1) {
                        breakChild[l1 - 1] = false;
                    }
                }
                String totalClause = "";
//                               if (breakChild[l1] || lLoop == 1) {
//                                 colTotalQuery[l1].add(finalColViewSortedValues[l1].get(lLoop).toString());
//                               }

            }

        }/// End of resultset while loop

        for (int l1 = 0; l1 <= totalColSize; l1++) {
            {
                int j = ((collectVal[l1]));
                finalColSpanList[l1].add(j);
            }

        }

        ArrayList<Integer> layerColSpanLst;
        for (int index = 0; index < finalColSpanList.length; index++) {
            layerColSpanLst = finalColSpanList[index];
            for (Integer span : layerColSpanLst) {
                colSpanMap.put(index, span);
            }
        }
        //code written by swathi purpose of summerized measures
        if (this.summarizedMeasuresEnabled) {
            String[] temp = new String[summerizedQryeIds.size()];

            for (int i = 0; i < summerizedQryeIds.size(); i++) {
                ArrayList forViewMap = new ArrayList();
                forViewMap.add(" ");
                forViewMap.add(summerizedQryColNames.get(i));
                nonViewByMapNew.put("A_" + summerizedQryeIds.get(i), forViewMap);
                CrossTabfinalOrder.add(i, "A_" + summerizedQryeIds.get(i));
            }
        }
        //code ended
        newCrossPb.colSpanMap = colSpanMap;
        newCrossPb.nonViewByMapNew = nonViewByMapNew;
        newCrossPb.finalColViewSortedValues = finalColViewSortedValues;
        newCrossPb.CrossTabfinalOrder = CrossTabfinalOrder;
        newCrossPb.gtType = gtType;
        newCrossPb.subGtType = subGtType;
        newCrossPb.isGTNone = isGTNone;
        newCrossPb.isSTNone = isSTNone;
        newCrossPb.crosstabMeasureId = crosstabMeasureId;
        newCrossPb.crosstabmeasureIdsmap = crosstabmeasureIdsmap;
        ///////Adding Sorting Key This will now there as colcount
        //newCrossPb.addColumn(count, contextPath, columnCount);
        char[] newSortTypes = new char[rowViewCount];
        char[] newSortDataTypes = new char[rowViewCount];
        ArrayList sortMeasure = new ArrayList();
        for (int l1 = 0; l1 < rowViewCount; l1++) {
            //newCrossPb.addColumn(nonViewByMapNew.size()+l1, "A_O_"+rowViewBys.get(l1), rowViewSortedValueDataTypeInt.get(l1));
            newCrossPb.hMap.put("A_O_" + rowViewBys.get(l1), rowViewSortValues[l1]);
            sortMeasure.add("A_O_" + rowViewBys.get(l1));
            //changed by veena for grand total issue
            newCrossPb.cols[l1 + nonViewByMapNew.size() + rowViewCount] = "A_O_" + rowViewBys.get(l1);
            newCrossPb.columnTypes[l1 + nonViewByMapNew.size() + rowViewCount] = rowViewSortedValueDataType[l1];
            if (!rowViewSortedValueDataTypeInt.isEmpty()) {
                newCrossPb.columnTypesInt[l1 + nonViewByMapNew.size() + rowViewCount] = rowViewSortedValueDataTypeInt.get(l1);
            }
            newSortTypes[l1] = 'A';
            if (rowViewSortedValueDataType[l1] != null) {
                newSortDataTypes[l1] = rowViewSortedValueDataType[l1].charAt(0);
            }
            newCrossPb.columnSizes[l1 + nonViewByMapNew.size() + rowViewCount] = this.columnSizes[l1];
            //end of changes
        }

        newCrossPb.MeasurePos = MeasurePos;
        newCrossPb.setRowCount(rowViewSortedValues[0].size());
        newCrossPb.resetViewSequence();//
        newCrossPb.resetSubTotal();
        newCrossPb.processGT = true;
        newCrossPb.prepareObject(newCrossPb);
//            
        ArrayList newViewSequence = newCrossPb.sortDataSet(sortMeasure, newSortTypes, newSortDataTypes);
//            
//            
        newCrossPb.setViewSequence(newViewSequence);
//            

        newCrossPb.setColumnCount(nonViewByMapNew.size() - rowViewCount);
        //written by swati
        newCrossPb.finalCrossTabReportDrillMap = finalCrossTabReportDrillMap;
        return newCrossPb;

    }
    public Object[][] retrieveDataRunTime(ArrayList<String> sortColumns, char[] rowDataTypes,Container container) {
        // long startTime = System.currentTimeMillis();
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][sortColumns.size()];
//        Datafacade 
        int j;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : sortColumns) {
                if (rowDataTypes[j] == 'N') {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i));//this.getFieldValueBigDecimal(i,column);
                } else if (rowDataTypes[j] == 'C') {//
                    try {
                        String data1 = "";
                        if (column.equalsIgnoreCase("A_TIME") || column.equalsIgnoreCase("A_O_TIME") || column.equalsIgnoreCase("TIME")) {
                            data1 = (this.getDimensionColumnDate(column, this.viewSequence.get(i)));
                        } else {
                            data1 = this.getDimensionColumnData(column, this.viewSequence.get(i));
                        }
                        cellData = data1;
                    } catch (Exception exception) {
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }

                } else {
                    try {
                        BigDecimal bigDecimal1 = new BigDecimal(this.getDimensionColumnData(column, this.viewSequence.get(i)));
                        cellData = bigDecimal1; //hardcoding not good assuming C is Dimension
                    } catch (Exception exception) {
                        //
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }
                }
                data[i][j] = cellData;
                j++;
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return data;
    }
    //added by anitha
        public Object[][] retrieveData(ArrayList<String> sortColumns, char[] rowDataTypes,Container container) {
        // long startTime = System.currentTimeMillis();
        Object cellData;
        Object[][] data = new Object[this.viewSequence.size()][sortColumns.size()];
        int j;

        for (int i = 0; i < this.viewSequence.size(); i++) {
            j = 0;
            for (String column : sortColumns) {
                if (rowDataTypes[j] == 'N') {
                    cellData = this.getMeasureColumnData(column, this.viewSequence.get(i),container);//this.getFieldValueBigDecimal(i,column);
                } else if (rowDataTypes[j] == 'C') {//
                    try {
                        String data1 = "";
                        if (column.equalsIgnoreCase("A_TIME") || column.equalsIgnoreCase("A_O_TIME") || column.equalsIgnoreCase("TIME")) {
                            data1 = (this.getDimensionColumnDate(column, this.viewSequence.get(i)));
                        } else {
                            data1 = this.getDimensionColumnData(column, this.viewSequence.get(i));
                        }
                        cellData = data1;
                    } catch (Exception exception) {
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }

                } else {
                    try {
                        BigDecimal bigDecimal1 = new BigDecimal(this.getDimensionColumnData(column, this.viewSequence.get(i)));
                        cellData = bigDecimal1; //hardcoding not good assuming C is Dimension
                    } catch (Exception exception) {
                        //
                        cellData = this.getDimensionColumnData(column, this.viewSequence.get(i));
                    }
                }
                data[i][j] = cellData;
                j++;
            }
        }
        // long endTime = System.currentTimeMillis();
//        
        return data;
    }
        private BigDecimal getMeasureColumnData(String element, int row,Container container) {
        BigDecimal data;
        DataFacade dfacade = new DataFacade(container);
        try{
        if (RTMeasureElement.isRunTimeMeasure(element)&& (element.contains("_MTD")|| element.contains("_QTD")|| element.contains("_YTD")|| element.contains("_PMTD")|| element.contains("_PQTD")|| element.contains("_PYTD")
                ||element.contains("_MOM")||element.contains("_QOQ")||element.contains("_YOY")||element.contains("_MOYM")||element.contains("_QOYQ")||element.contains("_MOMPer")||element.contains("_QOQPer")||element.contains("_YOYPer")||element.contains("_MOYMPer")||element.contains("_QOYQPer")
                ||element.contains("_PYMTD")||element.contains("_PYQTD")||element.contains("_WTD")||element.contains("_PWTD")||element.contains("_PYWTD")||element.contains("_WOWPer")||element.contains("_WOYWPer")||element.contains("_WOW")||element.contains("_WOYW"))) {
            data = (BigDecimal) this.getFieldValue(row, element);
        }else if (RTMeasureElement.isRunTimeMeasure(element)){
            data = this.getFieldValueRuntimeMeasure(row, element);
        }else {
            data = this.getFieldValueBigDecimal(row, element);
        }}catch(Exception e){
            data = dfacade.getMeasureDataForComputationRT(row, element);
        }
        return data;
    }
        //end of code by anitha
}
