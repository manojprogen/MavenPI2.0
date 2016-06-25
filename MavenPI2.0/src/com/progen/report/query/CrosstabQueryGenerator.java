/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import com.google.common.collect.ArrayListMultimap;
import com.progen.contypes.GetConnectionType;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class CrosstabQueryGenerator {

    public static Logger logger = Logger.getLogger(CrosstabQueryGenerator.class);
    private ArrayList rowViewBys = new ArrayList();
    private ArrayList colViewBys = new ArrayList();
    private ArrayList Qrycolumns = new ArrayList();
    private ArrayList QrycolumnsSumm = new ArrayList();//nonViewbyColumnList
    private ArrayList timeDetails = new ArrayList();//nonViewByMap
    private String orderByCol = "";
    private HashMap nonViewInput = new LinkedHashMap();
    private String ColorderByCol = "";
    private boolean isValueSortReq = true;
    ArrayList<Integer>[] finalColSpanList;
    ArrayList<Integer>[] finalColSpanList1;

    public boolean isIsValueSortReq() {
        return isValueSortReq;
    }

    public void setIsValueSortReq(boolean isValueSortReq) {
        this.isValueSortReq = isValueSortReq;
    }

    public String getColorderByCol() {
        return ColorderByCol;
    }

    public void setColorderByCol(String ColorderByCol) {
        this.ColorderByCol = ColorderByCol;
    }

    public boolean isDisableValueSort() {
        return disableValueSort;
    }

    public void setDisableValueSort(boolean disableValueSort) {
        this.disableValueSort = disableValueSort;
    }
    private boolean disableValueSort = false;

    public HashMap getNonViewInput() {
        return nonViewInput;
    }

    public void setNonViewInput(HashMap nonViewInput) {
        this.nonViewInput = nonViewInput;
    }
    public ArrayList viewByList = new ArrayList();
    public String nonViewbyColumnList = "";//
    public HashMap nonViewByMap = new LinkedHashMap();//
    public HashMap nonViewByMapNew = new LinkedHashMap();//
    private String colViewQuery = "";
    String gtType = "";// FIRST, LAST
    String subGtType = ""; // ALLFIRST , ALLLAST, BEFORE, AFTER
    private String measurePosition = "LAST";

    public String getColViewQuery() {
        return colViewQuery;
    }

    public void setColViewQuery(String colViewQuery) {
        this.colViewQuery = colViewQuery;
    }
    private boolean isColTrend = false;
    public ArrayList nonViewByList;
    public ArrayList nonViewByListRT;
    public ArrayList allColumnList;

    public ArrayList getQrycolumns() {
        return Qrycolumns;
    }

    public void setQrycolumns(ArrayList Qrycolumns) {
        this.Qrycolumns = Qrycolumns;
    }

    public ArrayList getQrycolumnsSumm() {
        return QrycolumnsSumm;
    }

    public void setQrycolumnsSumm(ArrayList QrycolumnsSumm) {
        this.QrycolumnsSumm = QrycolumnsSumm;
    }

    public ArrayList getColViewBys() {
        return colViewBys;
    }

    public void setColViewBys(ArrayList colViewBys) {
        this.colViewBys = colViewBys;
    }

    public ArrayList getRowViewBys() {
        return rowViewBys;
    }

    public void setRowViewBys(ArrayList rowViewBys) {
        this.rowViewBys = rowViewBys;
    }

    public ArrayList getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(ArrayList timeDetails) {
        this.timeDetails = timeDetails;
    }

    public String getOrderByCol() {
        return orderByCol;
    }

    public void setOrderByCol(String orderByCol) {
        this.orderByCol = orderByCol;
    }

    public String generateMultiMeasureCrossTabQry(String flatQuery) throws SQLException {
        //Part one Execute the Query and get distinct values if col view by does not contains time

        //
//        ProgenLog.log(ProgenLog.FINE,this,"generateMultiMeasureCrossTabQry","Enter flatQuery--"+flatQuery);
        logger.info("Enter flatQuery--" + flatQuery);
        String temp = "";
        //  String utemp = "";//delete
        StringBuilder utemp = new StringBuilder();
        //       String otemp = "";//delete
        StringBuilder otemp = new StringBuilder();
        String sqlstr = "";
        String temp1 = "";
        String in_qry = "";
        int ival = 0;
        int nocol = 0;


        if (gtType == null) {
            gtType = "";// FIRST, LAST
        }
        if (subGtType == null) {
            subGtType = ""; // ALLFIRST , ALLLAST, BEFORE, AFTER
        }
        if (measurePosition == null || (!measurePosition.equalsIgnoreCase("FIRST"))) {
            measurePosition = "LAST";
        }

        Connection con = null;
        CallableStatement st = null;
        Statement st2 = null;
        ResultSet rs = null;
        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();
        //    String finalQuery = "";
        StringBuilder finalQuery = new StringBuilder();
        //      String viewBySql = null ;
        StringBuilder viewBySql = new StringBuilder();
        //       String rowViewBySql = null ;
        StringBuilder rowViewBySql = new StringBuilder();
//        String nonViewBySql = null;
        StringBuilder nonViewBySql = new StringBuilder();
      //  String havingSql = null;
        StringBuilder havingSql=new StringBuilder();
        String newViewByOrder = "1";
        String connType = "Oracle";
        setColTrend();
        String[] headerViewByColList = new String[colViewBys.size()];
        int[] currPosList = new int[colViewBys.size() - 1];
        ArrayList<Integer>[] nextBreakList = new ArrayList[colViewBys.size() - 1];

        String headerViewByCol = "A_" + colViewBys.get(0).toString() + " ";

        if (colViewBys.get(0).toString().equalsIgnoreCase("Time")) {
            headerViewByCol = " \"" + colViewBys.get(0).toString().toUpperCase() + "\" ";
        }

        for (int i = 0; i < colViewBys.size(); i++) {
            if (colViewBys.get(i).toString().equalsIgnoreCase("Time")) {
                headerViewByColList[i] = " \"" + colViewBys.get(i).toString().toUpperCase() + "\" ";
            } else {
                headerViewByColList[i] = "A_" + colViewBys.get(i).toString() + " ";
            }
        }


        if (isColTrend && colViewBys.size() <= 0) {
            //      finalQuery = colViewQuery;
            finalQuery = new StringBuilder(colViewQuery);

        } else {

            ///Outer warpper for the Query
            for (int i = 0; i < colViewBys.size(); i++) {
                if (viewBySql == null) {
                    if (colViewBys.get(i).toString().equalsIgnoreCase("Time")) //             viewBySql = " \"TIME\" ";
                    {
                        viewBySql.append(" \"TIME\" ");
                    } else //         viewBySql = " A_" + colViewBys.get(i).toString() + " ";
                    {
                        viewBySql.append(" A_").append(colViewBys.get(i).toString()).append(" ");
                    }
                } else {
                    if (colViewBys.get(i).toString().equalsIgnoreCase("Time")) //      viewBySql += " , \"TIME\" ";
                    {
                        viewBySql.append(" , \"TIME\" ");
                    } else //       viewBySql += " , A_" + colViewBys.get(i).toString() + " ";
                    {
                        viewBySql.append(" , A_").append(colViewBys.get(i).toString()).append(" ");
                    }
                }
                if (i == 0) {
                    newViewByOrder = " 1 ";
                } else {
                    newViewByOrder += " , " + (i + 1) + " ";
                }
            }
            ///////////////
            for (int i = 0; i < Qrycolumns.size(); i++) {//
                if (nonViewBySql == null) {
                    //       nonViewBySql = " , sum(abs( A_" + Qrycolumns.get(i).toString() + "))  A_" + Qrycolumns.get(i).toString() +" ";
                    nonViewBySql.append(" , sum(abs( A_").append(Qrycolumns.get(i).toString()).append("))  A_").append(Qrycolumns.get(i).toString()).append(" ");
             //       havingSql = " having sum(abs( A_" + Qrycolumns.get(i).toString() + ")) > 0 ";
                    havingSql.append(" having sum(abs( A_").append(Qrycolumns.get(i).toString()).append(")) > 0 ");
                } else {
                    //             nonViewBySql += " , sum(abs( A_" + Qrycolumns.get(i).toString() + "))  A_" + Qrycolumns.get(i).toString() +" ";
                    nonViewBySql.append(" , sum(abs( A_").append(Qrycolumns.get(i).toString()).append("))  A_").append(Qrycolumns.get(i).toString()).append(" ");
         //           havingSql += " or  sum(abs( A_" + Qrycolumns.get(i).toString() + ")) > 0 ";
                    havingSql.append(" or  sum(abs( A_").append(Qrycolumns.get(i).toString()).append(")) > 0 ");
                }
            }
            if (isValueSortReq && !isColTrend) {
                if (colViewBys.size() <= 1) //       finalQuery = " select " + viewBySql + nonViewBySql + " from (" + flatQuery + ") co1 group by " + viewBySql + havingSql + " ";//order by 2
                {
                    finalQuery.append(" select ").append(viewBySql).append(nonViewBySql).append(" from (").append(flatQuery).append(") co1 group by ").append(viewBySql).append(havingSql).append(" ");//order by 2
                } else //            finalQuery = " select " + viewBySql + nonViewBySql + " from (" + flatQuery + ") co1 group by " + viewBySql + havingSql + " order by " + newViewByOrder;//order by 2
                {
                    finalQuery.append(" select ").append(viewBySql).append(nonViewBySql).append(" from (").append(flatQuery).append(") co1 group by ").append(viewBySql).append(havingSql).append(" order by ").append(newViewByOrder);
                }
            } else {
                {
                    //             finalQuery = " select " + viewBySql + " , "+ ColorderByCol + nonViewBySql + " from (" + flatQuery + ") co1 group by " + viewBySql + " , "+ColorderByCol + " order by " + ColorderByCol;//order by 2
                    finalQuery.append(" select ").append(viewBySql).append(" , ").append(ColorderByCol).append(nonViewBySql).append(" from (").append(flatQuery).append(") co1 group by ").append(viewBySql).append(" , ").append(ColorderByCol).append(" order by ").append(ColorderByCol);//order by 2
                }

            }
//                ProgenLog.log(ProgenLog.FINE,this,"generateMultiMeasureCrossTabQry","for cross Tab Query--"+finalQuery);
            logger.info("for cross Tab Query--: " + finalQuery);

            ////////////Executing and main cross Tab Query
//
        }

        try {
            GetConnectionType gettypeofconn = new GetConnectionType();
            connType = gettypeofconn.getConTypeByElementId(this.Qrycolumns.get(0).toString());
            ////.println("conntype is" + connType);

            con = getConnection(this.Qrycolumns.get(0).toString());

//                    if (connType.equalsIgnoreCase("Sqlserver")) {
//                        finalQuery = finalQuery.replace("nvl", "COALESCE");//temporary fix
//                        finalQuery = finalQuery.replace("Nvl", "COALESCE");//temporary fix
//                        finalQuery = finalQuery.replace("NVL", "COALESCE");//temporary fix
//                    } else if (connType.equalsIgnoreCase("mysql")) {
//                        finalQuery = finalQuery.replace("nvl(", "ifNULL(");//temporary fix --Remove this code by defining variable for nvl at top
//                        finalQuery = finalQuery.replace("Nvl(", "ifNULL(");//temporary fix
//                        finalQuery = finalQuery.replace("NVL(", "ifNULL(");//temporary fix
            String fnlQuery = "";
            if (connType.equalsIgnoreCase("Sqlserver")) {
                fnlQuery = finalQuery.toString().replace("nvl", "COALESCE");//temporary fix
                fnlQuery = fnlQuery.replace("Nvl", "COALESCE");//temporary fix
                fnlQuery = fnlQuery.replace("NVL", "COALESCE");//temporary fix
            } else if (connType.equalsIgnoreCase("mysql")) {
                fnlQuery = finalQuery.toString().replace("nvl(", "ifNULL(");//temporary fix --Remove this code by defining variable for nvl at top
                fnlQuery = fnlQuery.replace("Nvl(", "ifNULL(");//temporary fix
                fnlQuery = fnlQuery.replace("NVL(", "ifNULL(");//temporary fix

//           finalSql = finalSql.replace("trunc(", "convert(smalldatetime,");//temporary fix
//           finalSql = finalSql.replace("Trunc(", "convert(smalldatetime,");//temporary fix
//           finalSql = finalSql.replace("TRUNC(", "convert(smalldatetime,");//temporary fix
            }
//
            finalQuery = new StringBuilder(fnlQuery);
            if (connType.equalsIgnoreCase("Sqlserver")) {
                st2 = con.createStatement();
                rs = st2.executeQuery(finalQuery.toString());

            } else {
                st = con.prepareCall(finalQuery.toString());
                rs = st.executeQuery();

            }

            in_qry = "";
            sqlstr = "";
            int i = 1;
            String[] tempName = new String[colViewBys.size()];
            ArrayList<Integer>[] colSpanList = new ArrayList[colViewBys.size()];
            finalColSpanList = new ArrayList[colViewBys.size()];
            finalColSpanList1 = new ArrayList[colViewBys.size()];
            ArrayList<String>[] colTotalQuery = new ArrayList[colViewBys.size()];
            ArrayList<ArrayList>[] colTotalQueryTitles = new ArrayList[colViewBys.size()];
            String[] oldValList = new String[colViewBys.size()];
            String[] newValList = new String[colViewBys.size()];
            ArrayList<String> queryColName = new ArrayList();
            ArrayList<String> queryColSummName = new ArrayList();
            boolean[] breakChild = new boolean[colViewBys.size()];
            int[] collectVal = new int[colViewBys.size()];
            int count = 0;

            for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                queryColName.add(nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                String useSumm = null;
                if (QrycolumnsSumm.get(qryCol) != null) {
                    useSumm = QrycolumnsSumm.get(qryCol).toString().toUpperCase();
                    if (useSumm.equalsIgnoreCase("COUNT") || useSumm.equalsIgnoreCase("COUNTDISTINCT")) {
                        useSumm = "sum";
                    }
                } else {
                    useSumm = "sum";
                }
                queryColSummName.add(useSumm);
            }
            //
            //


            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                colSpanList[l1] = new ArrayList<Integer>();
                finalColSpanList[l1] = new ArrayList<Integer>();
                finalColSpanList1[l1] = new ArrayList<Integer>();
                oldValList[l1] = "";
                newValList[l1] = "";
                breakChild[l1] = false;
                collectVal[l1] = 1;
                colTotalQuery[l1] = new ArrayList<String>();
                colTotalQueryTitles[l1] = new ArrayList<ArrayList>();
                if (l1 < colViewBys.size() - 1) {
                    nextBreakList[l1] = new ArrayList<Integer>();
                }
            }

//                if(gtType.equals("FIRST")){
//                      for (int qryCol=0; qryCol<Qrycolumns.size();qryCol++){
//                          nonViewByList.add(temp1);
//                          nonViewByListRT.add(temp1);
//
//                      }
//
//                }
            while (rs.next()) {


                count++;
                temp = rs.getString(1).replace("'", "''");


                for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                    tempName[l1] = rs.getString(l1 + 1).replace("'", "''");
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
                        //
                        ///
                        ///

                        ///
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
                    ////////////////////
               //     String totalClause = "";
                    StringBuilder totalClause=new StringBuilder();
                    if (breakChild[l1] || count == 1) {
                        ArrayList t1 = new ArrayList();
                        int l12;
                        for (l12 = 0; l12 <= l1; l12++) {
                            t1.add(tempName[l12]);
                            if (l12 == 0) {
                    //            totalClause = headerViewByColList[l12] + " = '" + tempName[l12] + "'";
                                totalClause.append(headerViewByColList[l12]).append(" = '").append(tempName[l12]).append( "'");
                            } else {
                          //      totalClause += " and " + headerViewByColList[l12] + " = '" + tempName[l12] + "'";
                                totalClause.append(" and ").append(headerViewByColList[l12]).append(" = '").append(tempName[l12]).append("'");
                            }
                        }
                        int l13;
                        for (l13 = l12; l13 < colViewBys.size(); l13++) {
                            if (l13 == l12) {
                                t1.add("Sub Total");
                            } else {
                                t1.add(" ");
                            }
                        }

                        // colTotalQueryTitles[l1].add(nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                        colTotalQuery[l1].add(totalClause.toString());
                        colTotalQueryTitles[l1].add(t1);
                    }
                }

                if (i > 250) {
                    break;
                }

                //New code for i
                i = i + Qrycolumns.size();
                //

                /**
                 * code to replace start
                 */
//                    for (int qryCol=0; qryCol<Qrycolumns.size();qryCol++){
//                        temp1 = "A" + i;
//                        ival = i;
//                        i++;
//                        if (nonViewbyColumnList == null) {
//                            nonViewbyColumnList = temp;
//                        } else {
//                            nonViewbyColumnList += "," + temp;
//                        }
//                        nonViewByList.add(temp1);
//                        nonViewByListRT.add(temp1);
//                        ArrayList valueMeasure = new ArrayList();
//                        //valueMeasure.add(temp);
//                        for (int l1 = 0; l1 < colViewBys.size(); l1++) {
//                            valueMeasure.add(tempName[l1]);
//                         //////////////////////////////////
//                      /////////////////////
//
//
//                        }
//
//
//                        valueMeasure.add( nonViewInput.get("A_"+Qrycolumns.get(qryCol).toString()).toString());
//                        nonViewByMap.put(temp1, temp + "<BR>"+ nonViewInput.get("A_"+Qrycolumns.get(qryCol).toString()).toString());
//                        nonViewByMapNew.put(temp1, valueMeasure);
//                        // ////.println("nonViewByList=="+nonViewByList);
//                        // ////.println("nonViewByListRT=="+nonViewByListRT);
//                        // ////.println("nonViewByMap=="+nonViewByMap);
//                        String useSumm = null;
//                        if(QrycolumnsSumm.get(qryCol) !=null){
//                            useSumm =QrycolumnsSumm.get(qryCol).toString();
//                            if(useSumm.equalsIgnoreCase("COUNT")){
//                                useSumm="sum";
//                            }
//                        }else{
//                            useSumm =  "count";
//                        }
//                        String measureName =Qrycolumns.get(qryCol).toString();
//                        String totalClause =headerViewByCol + " = '" + temp + "'";
//
//                        for(int l1=0;l1<colViewBys.size(); l1++){
//                            if(l1==0){
//                                totalClause =headerViewByColList[l1] + " = '" + tempName[l1]+ "'";
//                            }else{
//                                totalClause +=" and " + headerViewByColList[l1] + " = '" + tempName[l1]+ "'";
//                            }
//
//                            }
//
//                        if (useSumm.equalsIgnoreCase("COUNTDISTINCT")) {
//                            utemp = utemp + " ,  nvl( sum( ( Case when " + totalClause+ " then    A_" + measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";
//
//                        } else {
//                            utemp = utemp + " ,  nvl(" + useSumm + "( Case when " + totalClause + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
//                        }
//
//
//
////                        if (useSumm.equalsIgnoreCase("COUNTDISTINCT")) {
////                            utemp = utemp + " ,  nvl( sum( ( Case when " + headerViewByCol + " = '" + temp + "' then    A_" + measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";
////
////                        } else {
////                            utemp = utemp + " ,  nvl(" + useSumm + "( Case when " + headerViewByCol + " = '" + temp + "' then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
////                        }
//                        otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
//                        //  }
//                        nocol = nocol + 1;
//
//                    } ///End of Measure for loop
                /**
                 * code to replace start
                 */
            }/// End of resultset while loop

            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                {
                    int j = ((collectVal[l1]));
                    colSpanList[l1].add(j);
                }
//                            //
//                            //colTotalQuery[l1]
//
            }

            ///New code with mulicol and subtotal
            i = 1;
            {//Start

                //gtType = "NO"; // First , LAST
                //subGtType = "NO";
                if (gtType.equals("FIRST")) {




                    for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                        for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                            finalColSpanList[l1].add(1);
                        }
                        temp1 = "A" + i;
                        nonViewByList.add(temp1);
                        nonViewByListRT.add(temp1);
                        ArrayList valueMeasure = new ArrayList();
                        valueMeasure.add("Grand Total");
                        if (measurePosition.equals("FIRST")) {
                            valueMeasure.add(queryColName.get(qryCol));
                        }
                        for (int l1 = 1; l1 < colViewBys.size(); l1++) {
                            valueMeasure.add(" ");

                        }
                        if (measurePosition.equals("LAST")) {
                            valueMeasure.add(queryColName.get(qryCol));
                        }

                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                        nonViewByMapNew.put(temp1, valueMeasure);
                        String measureName = Qrycolumns.get(qryCol).toString();
                        //    utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "(   A_" + measureName + "  ) ,0)   \"" + temp1 + "\"  ";
                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("(   A_").append(measureName).append("  ) ,0)   \"").append(temp1).append("\"  ");
                        //        otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                        i++;
                        nocol = nocol + 1;
                    }

                    //utemp = utemp + " ,  nvl( sum( ( Case when " + totalClause+ " then    A_" + measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";

                }

                if (subGtType.equals("ALLFIRST")) {
                    int kk = 0;
                    for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                        {
                            kk += colTotalQueryTitles[l1].size();
                        }

                    }
                    kk = kk * Qrycolumns.size();
                    for (int l12 = 0; l12 < colViewBys.size(); l12++) {
//
                        for (int j1 = 0; j1 < kk; j1++) {
                            finalColSpanList[l12].add(1);
                        }

                    }




                    if (measurePosition.equals("FIRST")) {



                        for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {


                            for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {
                                    temp1 = "A" + i;
                                    nonViewByList.add(temp1);
                                    nonViewByListRT.add(temp1);

                                    ArrayList valueMeasure = new ArrayList();
                                    valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();
                                    valueMeasure.add(0, queryColName.get(qryCol));
                                    nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                    nonViewByMapNew.put(temp1, valueMeasure);

                                    String measureName = Qrycolumns.get(qryCol).toString();
                                    //              utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry)+ " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                    utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                    //              otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                    otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                    i++;
                                    nocol = nocol + 1;
                                }
                            }



                        }
                    }

                    if (measurePosition.equals("LAST")) {

//
                        {

                            for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {


                                    for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                                        temp1 = "A" + i;
                                        nonViewByList.add(temp1);
                                        nonViewByListRT.add(temp1);

                                        ArrayList valueMeasure = new ArrayList();
                                        valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();

                                        valueMeasure.add(queryColName.get(qryCol));
                                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                        nonViewByMapNew.put(temp1, valueMeasure);
                                        String measureName = Qrycolumns.get(qryCol).toString();
                                        //utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                        //     otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                        i++;
                                        nocol = nocol + 1;
                                    }

                                }

                            }
                        }



                    }






                }




                {//Cols in between

                    if (subGtType.equals("BEFORE")) {
                        for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                            currPosList[l1] = 0;
                            nextBreakList[l1].add(0);
                        }

                    } else if (subGtType.equals("AFTER")) {
                        for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                            currPosList[l1] = 0;

                        }
                    }



                    if (measurePosition.equals("FIRST")) {
                        for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                                for (int j1 = 0; j1 < colSpanList[l1].size(); j1++) {

                                    finalColSpanList[l1].add(colSpanList[l1].get(j1));

                                    if (l1 < colViewBys.size() - 1) {
                                        if (nextBreakList[l1].size() >= 1) {
                                            nextBreakList[l1].add(colSpanList[l1].get(j1) + nextBreakList[l1].get(nextBreakList[l1].size() - 1));
                                        } else {
                                            nextBreakList[l1].add(colSpanList[l1].get(j1));
                                        }

                                    }


                                }
                            }
                        }
//                                for (int l1 = 0; l1 < colViewBys.size()-1; l1++){
//
//
//
//                                    }

                        int counter = 0;
                        for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {

                            for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                                currPosList[l1] = 0;

                            }


                            int l1 = colViewBys.size() - 1;
                            {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {
                                    ///for before
                                    if (subGtType.equals("BEFORE")) {
                                        for (int l12 = 0; l12 < l1; l12++) {
                                            int num = nextBreakList[l12].get(currPosList[l12]);
                                            if (num == counter) {
                                                temp1 = "A" + i;
                                                nonViewByList.add(temp1);
                                                nonViewByListRT.add(temp1);

                                                ArrayList valueMeasure = new ArrayList();
                                                valueMeasure = (ArrayList) ((colTotalQueryTitles[l12].get(currPosList[l12]))).clone();

                                                valueMeasure.add(0, queryColName.get(qryCol));
                                                nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                                nonViewByMapNew.put(temp1, valueMeasure);
                                                String measureName = Qrycolumns.get(qryCol).toString();
                                                //              utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l12].get(currPosList[l12]) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                                utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l12].get(currPosList[l12])).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                                //              otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                                otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                                i++;
                                                nocol = nocol + 1;


                                                currPosList[l12] = currPosList[l12] + 1;

                                            }
                                        }


                                    }

                                    {
                                        temp1 = "A" + i;
                                        nonViewByList.add(temp1);
                                        nonViewByListRT.add(temp1);
                                        ArrayList valueMeasure = new ArrayList();
                                        valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();
                                        valueMeasure.add(0, queryColName.get(qryCol));
                                        //

                                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                        nonViewByMapNew.put(temp1, valueMeasure);
                                        //
                                        String measureName = Qrycolumns.get(qryCol).toString();
                                        //              utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                        //            otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                        i++;
                                        nocol = nocol + 1;
                                        counter++;

                                    }


                                    ////for after
                                    if (subGtType.equals("AFTER")) {
                                        //for (int l12 = 0; l12 < l1; l12++)
                                        for (int l12 = l1 - 1; l12 >= 0; l12--) {
                                            int num = nextBreakList[l12].get(currPosList[l12]);
                                            if (num == counter) {
                                                temp1 = "A" + i;
                                                nonViewByList.add(temp1);
                                                nonViewByListRT.add(temp1);

                                                ArrayList valueMeasure = new ArrayList();
                                                valueMeasure = (ArrayList) ((colTotalQueryTitles[l12].get(currPosList[l12]))).clone();

                                                valueMeasure.add(0, queryColName.get(qryCol));
                                                nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                                nonViewByMapNew.put(temp1, valueMeasure);
                                                String measureName = Qrycolumns.get(qryCol).toString();
                                                //          utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l12].get(currPosList[l12]) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                                utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l12].get(currPosList[l12])).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                                //          otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                                otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                                i++;
                                                nocol = nocol + 1;


                                                currPosList[l12] = currPosList[l12] + 1;

                                            }
                                        }


                                    }

                                }
                            }



                        }

                    }
                    if (measurePosition.equals("LAST")) {
                        for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                            for (int j1 = 0; j1 < colSpanList[l1].size(); j1++) {
                                int kk = colSpanList[l1].get(j1) * Qrycolumns.size();
                                finalColSpanList[l1].add(kk);

                                if (l1 < colViewBys.size() - 1) {

                                    if (nextBreakList[l1].size() >= 1) {

                                        nextBreakList[l1].add(kk + nextBreakList[l1].get(nextBreakList[l1].size() - 1));
                                    } else {
                                        nextBreakList[l1].add(kk);
                                    }

                                }


                            }
                        }
//                                for (int l1 = colViewBys.size()-1; l1 >=0 ; l1--){
//                                    if (subGtType.equals("BEFORE")){
//                                        finalColSpanList1[l1].add(Qrycolumns.size());
//                                        for (int l2 = l1-1 ; l2 >=0 ; l2--){
//                                            finalColSpanList1[l1].add(Qrycolumns.size());
//                                        }
//                                        for (int l3 = l1; l3 >=colViewBys.size()-1 ; l1++){
//
//                                        }
//
//                                    }
//                                    if (subGtType.equals("AFTER")){
//
//                                        for (int l2 = l1 ; l2 >=0 ; l2--){
//
//                                        }
//                                        for (int l3 = l1; l3 >=colViewBys.size()-1 ; l1++){
//
//                                        }
//
//                                    }
//
//                                }


//                                for (int l1 = 0; l1 < colViewBys.size()-1; l1++){
//
//
//
//                                    }

                        {

                            int counter = 0;
                            int l1 = colViewBys.size() - 1;
                            {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {

                                    if (subGtType.equals("BEFORE")) {

                                        // for (int l12 = l1-1; l12 >=0 ; l12--)
                                        for (int l12 = 0; l12 < l1; l12++) {
                                            int num = nextBreakList[l12].get(currPosList[l12]);
//
//
//
//
                                            if (num == counter) {

                                                for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {

                                                    temp1 = "A" + i;
                                                    nonViewByList.add(temp1);
                                                    nonViewByListRT.add(temp1);

                                                    ArrayList valueMeasure = new ArrayList();
                                                    valueMeasure = (ArrayList) ((colTotalQueryTitles[l12].get(currPosList[l12]))).clone();

                                                    valueMeasure.add(queryColName.get(qryCol));
                                                    nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                                    nonViewByMapNew.put(temp1, valueMeasure);
                                                    String measureName = Qrycolumns.get(qryCol).toString();
                                                    //              utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l12].get(currPosList[l12]) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                                    utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l12].get(currPosList[l12])).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                                    //               otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                                    otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                                    i++;
                                                    nocol = nocol + 1;




                                                }
                                                currPosList[l12] = currPosList[l12] + 1;

                                            }
                                        }


                                    }
                                    ///qty sub total loop for before
//                                            for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
//                                            }

                                    for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                                        temp1 = "A" + i;
                                        nonViewByList.add(temp1);
                                        nonViewByListRT.add(temp1);
                                        ArrayList valueMeasure = new ArrayList();
                                        valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();
                                        valueMeasure.add(queryColName.get(qryCol));
                                        ///
                                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                        nonViewByMapNew.put(temp1, valueMeasure);
                                        //finalColSpanList[l1].add(1);
                                        ///
                                        String measureName = Qrycolumns.get(qryCol).toString();
                                        //        utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                        //           otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                        i++;
                                        nocol = nocol + 1;
                                        counter++;

                                    }

                                    if (subGtType.equals("AFTER")) {
                                        //for (int l12 = 0; l12 < l1; l12++)
                                        for (int l12 = l1 - 1; l12 >= 0; l12--) {
                                            int num = nextBreakList[l12].get(currPosList[l12]);
                                            if (num == counter) {
                                                for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {

                                                    temp1 = "A" + i;
                                                    nonViewByList.add(temp1);
                                                    nonViewByListRT.add(temp1);

                                                    ArrayList valueMeasure = new ArrayList();
                                                    valueMeasure = (ArrayList) ((colTotalQueryTitles[l12].get(currPosList[l12]))).clone();

                                                    valueMeasure.add(queryColName.get(qryCol));
                                                    nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                                    nonViewByMapNew.put(temp1, valueMeasure);
                                                    String measureName = Qrycolumns.get(qryCol).toString();
                                                    //                 utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l12].get(currPosList[l12]) + " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                                    utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l12].get(currPosList[l12])).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                                    //                  otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                                    otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                                    i++;
                                                    nocol = nocol + 1;




                                                }
                                                currPosList[l12] = currPosList[l12] + 1;

                                            }
                                        }


                                    }

                                }
                            }



                        }

                    }


                }//cols in between

                if (subGtType.equals("ALLLAST")) {
//                            int kk = 0;
//                            for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
//                                {
//                                    kk += colTotalQueryTitles[l1].size();
//                                }
//
//                            }
//
//                            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
//                                for (int j1 = 0; j1 < kk * Qrycolumns.size(); j1++) {
//                                    finalColSpanList[l1].add(1);
//                                }
//
//                            }
                    int kk = 0;
                    for (int l1 = 0; l1 < colViewBys.size() - 1; l1++) {
                        {
                            kk += colTotalQueryTitles[l1].size();
                        }

                    }
                    kk = kk * Qrycolumns.size();
                    for (int l12 = 0; l12 < colViewBys.size(); l12++) {
                        //
                        for (int j1 = 0; j1 < kk; j1++) {
                            finalColSpanList[l12].add(1);
                        }

                    }

                    if (measurePosition.equals("FIRST")) {

                        for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {


                            for (int l1 = colViewBys.size() - 1; l1 >= 0; l1--) {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {
                                    temp1 = "A" + i;
                                    nonViewByList.add(temp1);
                                    nonViewByListRT.add(temp1);
                                    ArrayList valueMeasure = new ArrayList();
                                    valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();
                                    valueMeasure.add(0, queryColName.get(qryCol));
                                    nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                    nonViewByMapNew.put(temp1, valueMeasure);
                                    //finalColSpanList[l1].add(1);
                                    String measureName = Qrycolumns.get(qryCol).toString();
                                    //               utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry)+ " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                    utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                    //               otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                    otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                    i++;
                                    nocol = nocol + 1;
                                }
                            }



                        }

                    }
                    if (measurePosition.equals("LAST")) {

                        {


                            for (int l1 = colViewBys.size() - 2; l1 >= 0; l1--) {
                                for (int arry = 0; arry < colTotalQueryTitles[l1].size(); arry++) {
                                    for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                                        temp1 = "A" + i;
                                        nonViewByList.add(temp1);
                                        nonViewByListRT.add(temp1);
                                        ArrayList valueMeasure = new ArrayList();
                                        valueMeasure = (ArrayList) ((colTotalQueryTitles[l1].get(arry))).clone();
                                        valueMeasure.add(queryColName.get(qryCol));
                                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                                        nonViewByMapNew.put(temp1, valueMeasure);

                                        //finalColSpanList[l1].add(1);
                                        String measureName = Qrycolumns.get(qryCol).toString();
                                        //          utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "( Case when " + colTotalQuery[l1].get(arry)+ " then    A_" + measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("( Case when ").append(colTotalQuery[l1].get(arry)).append(" then    A_").append(measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                        //            otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                                        i++;
                                        nocol = nocol + 1;

                                    }

                                }
                            }



                        }

                    }


                }





                if (gtType.equals("LAST")) {
                    for (int qryCol = 0; qryCol < Qrycolumns.size(); qryCol++) {
                        for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                            finalColSpanList[l1].add(1);
                        }
                        temp1 = "A" + i;
                        nonViewByList.add(temp1);
                        nonViewByListRT.add(temp1);
                        ArrayList valueMeasure = new ArrayList();
                        valueMeasure.add("Grand Total");
                        if (measurePosition.equals("FIRST")) {
                            valueMeasure.add(queryColName.get(qryCol));
                        }
                        for (int l1 = 1; l1 < colViewBys.size(); l1++) {
                            valueMeasure.add(" ");

                        }
                        if (measurePosition.equals("LAST")) {
                            valueMeasure.add(queryColName.get(qryCol));
                        }
                        nonViewByMap.put(temp1, temp + "<BR>" + nonViewInput.get("A_" + Qrycolumns.get(qryCol).toString()).toString());
                        nonViewByMapNew.put(temp1, valueMeasure);
                        String measureName = Qrycolumns.get(qryCol).toString();
                        //                 utemp = utemp + " ,  nvl( " + queryColSummName.get(qryCol) + "(   A_" + measureName + "  ) ,0)   \"" + temp1 + "\"  ";
                        utemp.append(" ,  nvl( ").append(queryColSummName.get(qryCol)).append("(   A_").append(measureName).append("  ) ,0)   \"").append(temp1).append("\"  ");
                        //                 otemp = otemp + " , " + temp1 + " as  \"" + temp1 +"\"";
                        otemp.append(" , ").append(temp1).append(" as  \"").append(temp1).append("\"");
                        i++;
                        nocol = nocol + 1;
                    }

                    //utemp = utemp + " ,  nvl( sum( ( Case when " + totalClause+ " then    A_" + measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";

                }

            }//End


            String[] a1 = (String[]) (nonViewByMapNew.keySet()).toArray(new String[0]);

            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                colSpanList[l1] = new ArrayList<Integer>();
                finalColSpanList[l1] = new ArrayList<Integer>();
                finalColSpanList1[l1] = new ArrayList<Integer>();
                oldValList[l1] = "";
                newValList[l1] = "";
                breakChild[l1] = false;
                collectVal[l1] = 1;

            }
            count = 0;
//
            for (int ii = 0; ii < a1.length; ii++) {
                ArrayList a = (ArrayList) nonViewByMapNew.get(a1[ii]);



                count++;



                for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                    tempName[l1] = a.get(l1).toString();
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

                    if ((!newValList[l1].equals(oldValList[l1]))
                            || (l1 > 0 && breakChild[l1 - 1])) {
                        //
                        ///
                        ///

                        ///
                        oldValList[l1] = newValList[l1];
                        if (finalColSpanList[l1] == null || finalColSpanList[l1].size() <= 1) {

                            finalColSpanList[l1].add((collectVal[l1] - 1));

                        } else {
                            {
                                int j = ((collectVal[l1] - 1));
                                finalColSpanList[l1].add(j);

                            }

                        }
                        breakChild[l1] = true;
                        collectVal[l1] = 1;
                        if (l1 >= 1) {
                            //
                            breakChild[l1 - 1] = false;
                            //
                        }
                    }

                }
            }

            for (int l1 = 0; l1 < colViewBys.size(); l1++) {
                finalColSpanList[l1].add((collectVal[l1]));
//
//
            }



        } catch (SQLException ex) {
//                ProgenLog.log(ProgenLog.SEVERE,this,"generateMultiMeasureCrossTabQry","Exception "+ex.getMessage());
            logger.error("Exception", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }
        }
        ///
//              for(int l1=0;l1<colViewBys.size(); l1++){
//
//
//                }

        //       String oRowViewBySql="";
        StringBuilder oRowViewBySql = new StringBuilder();
        for (int k = 0; k < rowViewBys.size(); k++) {
            if (rowViewBySql == null) {
                if (rowViewBys.get(k).toString().equalsIgnoreCase("Time")) {
                    //              rowViewBySql = " TIME ";
                    rowViewBySql.append(" TIME ");
                    //    oRowViewBySql = " TIME as  \"TIME\" ";
                    oRowViewBySql.append(" TIME as  \"TIME\" ");
                    viewByList.add("TIME");
                } else {
                    //                  rowViewBySql = " A_" + rowViewBys.get(k).toString() + " ";
                    rowViewBySql.append(" A_").append(rowViewBys.get(k).toString()).append(" ");
                    //      oRowViewBySql = " A_" + rowViewBys.get(k).toString() + " as " +" \"A_" + rowViewBys.get(k).toString() + "\" ";
                    oRowViewBySql.append(" A_").append(rowViewBys.get(k).toString()).append(" as ").append(" \"A_").append(rowViewBys.get(k).toString()).append("\" ");
                    viewByList.add("A_" + rowViewBys.get(k).toString() + "");
                }

            } else {
                if (rowViewBys.get(k).toString().equalsIgnoreCase("Time")) {
                    //      rowViewBySql += " , TIME  ";
                    rowViewBySql.append(" , TIME  ");
                    //      oRowViewBySql += " , TIME as  \"TIME\" ";
                    oRowViewBySql.append(" , TIME as  \"TIME\" ");
                    viewByList.add("TIME");
                } else {
                    //             rowViewBySql += " , A_" + rowViewBys.get(k).toString() + " ";
                    rowViewBySql.append(" , A_").append(rowViewBys.get(k).toString()).append(" ");
                    //           oRowViewBySql += " , A_" + rowViewBys.get(k).toString() + " as " +" \"A_" + rowViewBys.get(k).toString() + "\" ";
                    oRowViewBySql.append(" , A_").append(rowViewBys.get(k).toString()).append(" as ").append(" \"A_").append(rowViewBys.get(k).toString()).append("\" ");
                    viewByList.add("A_" + rowViewBys.get(k).toString() + "");
                }

            }
        }

        if (connType.equalsIgnoreCase("Sqlserver")) {
            //         finalQuery =  " Select " + oRowViewBySql +"  "  + otemp + " from ( " +  " Select " + rowViewBySql +", " +orderByCol  + utemp + " from ( "+ flatQuery + " ) co2 group by " + rowViewBySql +", " +orderByCol +  " ) co4 " + " order by " +orderByCol + " " ;
            finalQuery.append(" Select ").append(oRowViewBySql).append("  ").append(otemp).append(" from ( ").append(" Select ").append(rowViewBySql).append(", ").append(orderByCol).append(utemp).append(" from ( ").append(flatQuery).append(" ) co2 group by ").append(rowViewBySql).append(", ").append(orderByCol).append(" ) co4 ").append(" order by ").append(orderByCol).append(" ");

        } else //finalQuery =  " Select " + oRowViewBySql +"  "  + otemp + " from ( " +  " Select " + rowViewBySql +", " +orderByCol  + utemp + " from ( "+ flatQuery + " ) co3 group by " + rowViewBySql +", " +orderByCol + " order by " +orderByCol + " ) La1 " ;
        {
            finalQuery.append(" Select ").append(oRowViewBySql).append("  ").append(otemp).append(" from ( ").append(" Select ").append(rowViewBySql).append(", ").append(orderByCol).append(utemp).append(" from ( ").append(flatQuery).append(" ) co3 group by ").append(rowViewBySql).append(", ").append(orderByCol).append(" order by ").append(orderByCol).append(" ) La1 ");
        }

//
//

//        ProgenLog.log(ProgenLog.FINE,this,"generateMultiMeasureCrossTabQry","Return finalQuery--"+finalQuery);
        logger.info("Return finalQuery--: " + finalQuery.toString());

        return (finalQuery.toString());
    }

    public void setColTrend() {
        for (int i = 0; i < colViewBys.size(); i++) {
            if (colViewBys.get(i).toString().equalsIgnoreCase("TIME")) {
                isColTrend = true;
            }
        }
    }

    public Connection getConnection(String elementId) {
        Connection connection = null;
        try {
            connection = ProgenConnection.getInstance().getConnectionForElement(elementId);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return connection;
    }

    ArrayListMultimap<Integer, Integer> getColumnSpanMap() {
        ArrayListMultimap<Integer, Integer> colSpan = ArrayListMultimap.create();
        ArrayList<Integer> layerColSpanLst;

        for (int index = 0; index < finalColSpanList.length; index++) {
            layerColSpanLst = finalColSpanList[index];
            for (Integer span : layerColSpanLst) {
                colSpan.put(index, span);
            }
        }
        return colSpan;
    }
}
