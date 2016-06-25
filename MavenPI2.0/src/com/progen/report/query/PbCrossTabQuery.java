/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

/**
 * @filename PbCrossTabQuery
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 25, 2009, 4:11:33 PM
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import utils.db.ProgenConnection;

public class PbCrossTabQuery extends PbDb {

    public static Logger logger = Logger.getLogger(PbCrossTabQuery.class);
    PbCrossTabQueryResourceBundle resBundle = (PbCrossTabQueryResourceBundle) ResourceBundle.getBundle("com.progen.report.query.PbCrossTabQueryResourceBundle");
//    public String viewByCol;
    public StringBuilder viewByCol;
    private String viewByColGroup;
    public String outerViewByCol;
    public String headerViewByCol;
    public boolean isheaderTrend;
    public String orderByCol;
    private String orderByColGroup;
    private String outerOrderByCol;
    public String measureName;
    public String summReq;
    public int noViewByCol;
    public int noOrderByCol;
    private String fromTables;
    private String whereClause;
    public String headerQuery;
    public ArrayList viewByList;
    public ArrayList nonViewByList;
    public ArrayList graphColList;
    public ArrayList nonViewByListRT;
    public ArrayList allColumnList;
    public String nonViewbyColumnList;
    public String elementIdForConn;
    public ArrayList roweleIds = new ArrayList();
    //public HashMap nonViewbyList = new HashMap();
    public HashMap nonViewByMap = new HashMap();
    public HashMap roweleIdsMap = new HashMap();
    private String defaultSortedColumn = null;// added by santhosh.kumar@progenbusiness.com on 05/01/2010
    public HashMap ParameterGroupAnalysisHashMap = null;
    public String eleId = "";
    public int ival = 0;
    public boolean isCustomCrosstab = false;
    public HashMap bucketAnalysisHashMap = new HashMap();
    public String reportId = "";
    public String bucketInnerTablesql;
    public String crossTabQuery = "";
    private ArrayList crossTabNonViewBys = new ArrayList();

    public PbCrossTabQuery() {
     //   viewByCol = "";
        viewByCol=new StringBuilder();
        orderByCol = "";
        measureName = "";
        summReq = "";
        noViewByCol = 0;
        noOrderByCol = 0;
        fromTables = "";
        whereClause = "";
        headerViewByCol = "";
        outerViewByCol = "";
        outerOrderByCol = "";
        viewByList = new ArrayList();
        nonViewByList = new ArrayList();
        allColumnList = new ArrayList();
        nonViewByListRT = new ArrayList();
    }

    public String[] getViewByArray() {

        return (String[]) viewByList.toArray(new String[0]);
    }

    public String[] getNonViewByArray() {

        return (String[]) nonViewByList.toArray(new String[0]);
    }

    public String[] getGraphColList() {
        int i;
        graphColList = new ArrayList();
        for (i = 0; i < viewByList.size(); i++) {
            graphColList.add(viewByList.get(i));
        }
        for (i = 0; i < nonViewByList.size(); i++) {
            graphColList.add(nonViewByList.get(i));
        }

        return (String[]) graphColList.toArray(new String[0]);
    }

    public String[] getAllColArray() {
        int i;
        allColumnList = new ArrayList();
        for (i = 0; i < viewByList.size(); i++) {
            allColumnList.add(viewByList.get(i));
        }
        for (i = 0; i < nonViewByListRT.size(); i++) {
            allColumnList.add(nonViewByListRT.get(i));
        }

        return (String[]) allColumnList.toArray(new String[0]);
    }

    /**
     * Creates a new instance of CrossTabQuery
     */
    public void setViewByCol(String pViewByCol) {
        String alias1 = "";
        int j = pViewByCol.lastIndexOf(".");
        if (j != -1) {
            alias1 = pViewByCol.substring(j + 1, pViewByCol.length());
        } else {
            alias1 = pViewByCol;
        }
        alias1 = alias1.replace(" ", "").replace(",", "").replace("-", "").replace("'", "").replace("(", "").replace(")", "");
        if (this.viewByCol == null || this.viewByCol.equals("")) {
    //        this.viewByCol = pViewByCol + " v" + (this.noViewByCol + 1) + alias1;
            this.viewByCol.append(pViewByCol).append(" v").append(this.noViewByCol + 1).append(alias1);
            this.viewByColGroup = pViewByCol;
            this.outerViewByCol = " v" + (this.noViewByCol + 1) + alias1;

        } else {
 //           this.viewByCol += ", " + pViewByCol + " v" + (this.noViewByCol + 1) + alias1;
            this.viewByCol.append(", ").append(pViewByCol).append(" v").append(this.noViewByCol + 1).append( alias1);
            this.viewByColGroup += " , " + pViewByCol;
            this.outerViewByCol += ",  v" + (this.noViewByCol + 1) + alias1;
        }
        viewByList.add("v" + (this.noViewByCol + 1) + alias1);
        //// ////.println("setViewByCol=="+viewByList);
        this.noViewByCol++;
    }

    public void setOrderByCol(String pOrderByCol) {
        String alias1 = "";
        int j = pOrderByCol.lastIndexOf(".");
        if (j != -1) {
            alias1 = pOrderByCol.substring(j + 1, pOrderByCol.length());
        } else {
            alias1 = pOrderByCol;
        }
        alias1 = alias1.replace(" ", "").replace(",", "").replace("-", "").replace("'", "").replace("(", "").replace(")", "");


        if (this.orderByCol == null || this.orderByCol.equals("")) {
            this.orderByCol = pOrderByCol + " o" + (this.noOrderByCol + 1) + alias1;
            this.orderByColGroup = pOrderByCol;
            this.outerOrderByCol = " o" + (this.noOrderByCol + 1) + alias1;
        } else {
            this.orderByCol += " ,  " + pOrderByCol + " o" + (this.noOrderByCol + 1) + alias1;
            this.orderByColGroup += " , " + pOrderByCol;
            this.outerOrderByCol += ",  o" + (this.noOrderByCol + 1) + alias1;
        }
        this.noOrderByCol++;
    }

    public void setHeaderViewByCol(String pHeaderViewByCol) {
        this.headerViewByCol = pHeaderViewByCol;
    }

    public void setMeasureSummValues(String pMeasure, String pSumm) {
        this.measureName = pMeasure;
        this.summReq = pSumm;
    }

    public void setHeaderQuery(String pHeaderQuery) {
        this.headerQuery = pHeaderQuery;
    }

    public void setFromTables(String pTables) {
        if (this.fromTables == null || this.fromTables.equals("")) {
            this.fromTables = " from " + pTables;
        } else {
            this.fromTables += " , " + pTables + " ";
        }
    }

    public void setCompleteFromTables(String pTables) {
        this.fromTables = " " + pTables;

    }

    public void setAdditionalWhereClause(String pWhere) {
        if (this.whereClause == null || this.whereClause.equals("")) {
            this.whereClause = " and  " + pWhere;
        } else {
            this.whereClause += " and " + pWhere + " ";
        }
    }

    public void setWhereClause(String pWhere) {
        if (this.whereClause == null || this.whereClause.equals("")) {
            this.whereClause = " where  " + pWhere;
        } else {
            this.whereClause += " and " + pWhere + " ";
        }
    }
    ////////////////for Query designer //////////////

    public String get2dQuery() throws SQLException {
        String temp = "";
        String utemp = "";
        String sqlstr = "";
        String temp1 = "";
        String in_qry = "";

        int nocol = 0;

        Connection con = null;
        CallableStatement st = null;
        ResultSet rs = null;
        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();


        sqlstr = this.headerQuery;
        nonViewbyColumnList = null;
        try {

            con = getConnection(this.elementIdForConn);
            st = con.prepareCall(sqlstr);
            rs = st.executeQuery();




            int i = 1;

            while (rs.next()) {
                temp = rs.getString(1).replace("'", "''");
                /*
                 * if (temp.length()>=30) temp1= temp.substring(1,20) +".."
                 * +temp.substring(temp.length()-5,temp.length()-1); else
                temp1=temp;
                 */
                temp1 = "A" + i;
                i++;

                //temp1= temp1.replace(" & ","");

                if (nonViewbyColumnList == null) {
                    nonViewbyColumnList = temp;
                } else {
                    nonViewbyColumnList += "," + temp;
                }
                nonViewByList.add(temp1);
                nonViewByListRT.add(temp1);
                nonViewByMap.put(temp1, temp);
                if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                    utemp = utemp + " ,  nvl( count  (distinct ( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";
                } else {
                    utemp = utemp + " ,  nvl(" + this.summReq + "( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                }

                nocol = nocol + 1;
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
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

        temp = "";
        sqlstr = "";
        //nonViewByListRT.add("r_total");
        //nonViewByMap.put("r_total", "Total");




        {
            in_qry = "";
            in_qry = in_qry + " select  " + viewByCol + " ";
            in_qry = in_qry + utemp + "   ";
            //in_qry = in_qry + utemp + " , nvl(" + this.summReq + " (   " + this.measureName + "   ),0 ) \"r_total\"   ";
            in_qry = in_qry + this.fromTables;
            in_qry = in_qry + this.whereClause;
            in_qry = in_qry + "group by  " + viewByColGroup + "  , " + orderByColGroup + "  ";
            if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                in_qry = in_qry + " having nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) >0   ";
            } else {
                in_qry = in_qry + " having nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 ) >0   ";
            }
            if (defaultSortedColumn != null && !"".equalsIgnoreCase(defaultSortedColumn)) {
                if (!defaultSortedColumn.equalsIgnoreCase("Time")) {
                    //in_qry = in_qry + "order by  " + orderByColGroup + "  ";
                    in_qry = in_qry + "order by  " + defaultSortedColumn + "  ";
                }
            } else {
                in_qry = in_qry + "order by  " + orderByColGroup + "  ";
                //in_qry = in_qry + "order by  " +  "A_" + defaultSortedColumn + "  ";
            }

            //in_qry = in_qry + "order by  " + orderByColGroup + "  ";


            sqlstr = in_qry;

        }
        return sqlstr;
    }

    /////////////////////
    public String get2dIntellQuery() throws SQLException {
        String temp = "";
  //      String utemp = "";
        StringBuilder utemp=new StringBuilder();
        String otemp = "";
        String sqlstr = "";
        String temp1 = "";
        String in_qry = "";
        int ival = 0;
        int nocol = 0;

        Connection con = null;
        CallableStatement st = null;
        ResultSet rs = null;
        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();
        // ////.println("isCustomCrosstab====="+isCustomCrosstab);
        String buckettotoueterwrapertable = "";
        /*
         * String replaceIndex=""; String isNewViewBy=""; String isCrossTab="";
         * String isReplace=""; String bucketName="";
         */

        sqlstr = this.headerQuery;
        nonViewbyColumnList = null;
        if (this.summReq == null || this.summReq.equalsIgnoreCase("NULL")) {
            this.summReq = "COUNT";
        }
        try {
            in_qry = "";
            sqlstr = "";

            if (!isCustomCrosstab) {


                if (isheaderTrend) {
                    sqlstr = this.headerQuery;

                } else {
                    {


                        in_qry = in_qry + " select   ";
                        in_qry = in_qry + " " + headerViewByCol + "    A1  ";
                        if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                            in_qry = in_qry + " , nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) A2   ";
                        } else {
                            in_qry = in_qry + " , nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 )  A2   ";
                        }


                        in_qry = in_qry + this.fromTables;
                        in_qry = in_qry + this.whereClause;
                        in_qry = in_qry + "group by  " + headerViewByCol + "  ";



                        if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                            in_qry = in_qry + " having nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) >0   ";
                        } else {
                            in_qry = in_qry + " having nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 ) >0   ";
                        }
                        in_qry = in_qry + "order by  " + headerViewByCol + " desc  ";//Amit not to merge

                        sqlstr = in_qry;



                    }

                }
                // ////.println("sqlstr test for crossccvcfff tab" + sqlstr);
                // ////.println("");

                con = getConnection(this.elementIdForConn);

                st = con.prepareCall(sqlstr);
                rs = st.executeQuery();




                in_qry = "";
                sqlstr = "";


                int i = 1;



                while (rs.next()) {
                    temp = rs.getString(1).replace("'", "''");

                    int count = 0;
                    if (i > 250) {
                        break;
                    }
                    temp1 = "A" + i;
                    ival = i;
                    i++;

                    if (nonViewbyColumnList == null) {
                        nonViewbyColumnList = temp;
                    } else {
                        nonViewbyColumnList += "," + temp;
                    }
                    nonViewByList.add(temp1);
                    nonViewByListRT.add(temp1);
                    nonViewByMap.put(temp1, temp);
                    // ////.println("nonViewByList=="+nonViewByList);
                    // ////.println("nonViewByListRT=="+nonViewByListRT);
                    // ////.println("nonViewByMap=="+nonViewByMap);

                    if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
        //            utemp = utemp + " ,  nvl( count  (distinct ( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";
                      utemp.append(" ,  nvl( count  (distinct ( Case when ").append(headerViewByCol).append(" = '").append(temp).append("' then    ").append(this.measureName).append("  else null end  )) ,0)   \"").append(temp1).append("\"  ");
                    } else {
                   // utemp = utemp + " ,  nvl(" + this.summReq + "( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";
                     utemp.append(" ,  nvl(").append(this.summReq).append("( Case when ").append(headerViewByCol).append(" = '").append(temp).append("' then    ").append(this.measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                    }
                    //  }

                    nocol = nocol + 1;
                }



                ////Put here

            } else {
                // ////.println("--------"+bucketAnalysisHashMap);

                if (bucketAnalysisHashMap != null && bucketAnalysisHashMap.size() > 0) {

                    HashMap metaData = (HashMap) bucketAnalysisHashMap.get("metaData");
                    String elementId = String.valueOf(metaData.get("elementId"));
                    HashMap BucketDetails = (HashMap) bucketAnalysisHashMap.get("BucketDetails");

                    if (BucketDetails != null && BucketDetails.size() > 0) {
                        String isNewViewBy = String.valueOf(metaData.get("isNewViewBy"));
                        String isCrossTab = String.valueOf(metaData.get("isCrossTab"));
                        String isReplace = String.valueOf(metaData.get("isReplace"));
                        String replaceIndex = String.valueOf(metaData.get("replaceIndex"));

                        String eleName = "<Bucket-" + elementId + ">";
                        ArrayList displayValues = (ArrayList) BucketDetails.get("displayValues");
                        ArrayList startLimit = (ArrayList) BucketDetails.get("startLimit");
                        ArrayList endLimit = (ArrayList) BucketDetails.get("endLimit");
                        String bucketName = String.valueOf(metaData.get("bucketName"));
                        // ////.println("isCrossTab==="+isCrossTab+"isReplace=="+isReplace+"isNewViewBy=="+isNewViewBy+"replaceIndex=="+replaceIndex);
                        if (isCrossTab.equalsIgnoreCase("Y")) {
                            for (int i1 = 0; i1 < displayValues.size(); i1++) {
                    if(!this.summReq.equalsIgnoreCase("count")){
               //     utemp += " , "+ this.summReq+"( case when " +  this.measureName + " between  " + String.valueOf(startLimit.get(i1)) + " and " + String.valueOf(endLimit.get(i1)) + " then " +this.measureName + " else 0.0 end ) As \"A"+(i1+1)+"\"" ;
                                    utemp.append(" , ").append(this.summReq).append("( case when ").append(this.measureName).append(" between  ").append(String.valueOf(startLimit.get(i1))).append(" and ").append(String.valueOf(endLimit.get(i1))).append(" then ").append(this.measureName).append(" else 0.0 end ) As \"A").append(i1 + 1).append("\"");
                    }else{
                                    //    utemp += " , "+ this.summReq+"( case when " +  this.measureName + " between  " + String.valueOf(startLimit.get(i1)) + " and " + String.valueOf(endLimit.get(i1)) + " then " +this.measureName + " else 0.0 end ) As \"A"+(i1+1)+"\"" ;
           //        utemp += " , ( case when "+ this.summReq+"(" +  this.measureName + ") between  " + String.valueOf(startLimit.get(i1)) + " and " + String.valueOf(endLimit.get(i1)) + " then "+ this.summReq+"(" +  this.measureName + ") else 0.0 end ) As \"A"+(i1+1)+"\"" ;
                                    utemp.append(" , ( case when ").append(this.summReq).append("(").append(this.measureName).append(") between  ").append(String.valueOf(startLimit.get(i1))).append(" and ").append(String.valueOf(endLimit.get(i1))).append(" then ").append(this.summReq).append("(").append(this.measureName).append(") else 0.0 end ) As \"A").append(i1 + 1).append("\"");
                                }
                                nonViewByList.add("A" + (i1 + 1));
                                nonViewByListRT.add("A" + (i1 + 1));
                                nonViewByMap.put("A" + (i1 + 1), displayValues.get(i1));
                            }
                        } else {

                            in_qry = in_qry + " select   ";
                            in_qry = in_qry + " " + headerViewByCol + "    A1  ";
                            if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                                in_qry = in_qry + " , nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) A2   ";
                            } else {
                                in_qry = in_qry + " , nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 )  A2   ";
                            }


                            in_qry = in_qry + this.fromTables;
                            in_qry = in_qry + this.whereClause;
                            in_qry = in_qry + "group by  " + headerViewByCol + "  ";



                            if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                                in_qry = in_qry + " having nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) >0   ";
                            } else {
                                in_qry = in_qry + " having nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 ) >0   ";
                            }
                            in_qry = in_qry + "order by  2 desc ";

                            sqlstr = in_qry;

                            // ////.println("sqlstr test for crossccvcfff tab" + sqlstr);


                            // ////.println("");

                            con = getConnection(this.elementIdForConn);

                            st = con.prepareCall(sqlstr);
                            rs = st.executeQuery();




                            in_qry = "";
                            sqlstr = "";


                            int i = 1;



                            while (rs.next()) {
                                temp = rs.getString(1).replace("'", "''");

                                int count = 0;
                                if (i > 250) {
                                    break;
                                }
                                temp1 = "A" + i;
                                ival = i;
                                i++;

                                if (nonViewbyColumnList == null) {
                                    nonViewbyColumnList = temp;
                                } else {
                                    nonViewbyColumnList += "," + temp;
                                }
                                nonViewByList.add(temp1);
                                nonViewByListRT.add(temp1);
                                nonViewByMap.put(temp1, temp);


                                if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
     //               utemp = utemp + " ,  nvl( count  (distinct ( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  )) ,0)   \"" + temp1 + "\"  ";
                                    utemp.append(" ,  nvl( count  (distinct ( Case when ").append(headerViewByCol).append(" = '").append(temp).append("' then    ").append(this.measureName).append("  else null end  )) ,0)   \"").append(temp1).append("\"  ");
                                } else {
                                    utemp.append(" ,  nvl(").append(this.summReq).append("( Case when ").append(headerViewByCol).append(" = '").append(temp).append("' then    ").append(this.measureName).append("  else null end  ) ,0)   \"").append(temp1).append("\"  ");
                                }
                                //  }

                                nocol = nocol + 1;
                            }




                  String groupcase = " ( ";
                  StringBuilder grpcase=new StringBuilder(groupcase);
                            for (i = 0; i < displayValues.size(); i++) {
                    if(i==0){
            //        grpcase += "  select '" + displayValues.get(i) + "' as  bucket_name, " + String.valueOf(startLimit.get(i)) + " as start_Limit ," + String.valueOf(endLimit.get(i)) + " as end_Limit from dual" ;
                                    grpcase.append("  select '").append(displayValues.get(i)).append("' as  bucket_name, ").append(String.valueOf(startLimit.get(i))).append(" as start_Limit ,").append(String.valueOf(endLimit.get(i))).append(" as end_Limit from dual");
                    }else{
         //           grpcase.append("  union all select '" + displayValues.get(i) + "' as  bucket_name, " + String.valueOf(startLimit.get(i)) + " as start_Limit ," + String.valueOf(endLimit.get(i)) + " as end_Limit from dual" ;
                                    grpcase.append("  union all select '").append(displayValues.get(i)).append("' as  bucket_name, ").append(String.valueOf(startLimit.get(i))).append(" as start_Limit ,").append(String.valueOf(endLimit.get(i))).append(" as end_Limit from dual");
                                }

                            }
          //      grpcase += " ) "+bucketName+"_"+reportId;
                            grpcase.append(" ) ").append(bucketName).append("_").append(reportId);

               if(isNewViewBy.equalsIgnoreCase("Y")){
   //            viewByCol+=","+bucketName+"_"+reportId+".bucket_name "+"  v"+(viewByList.size()+1)+"bucket_name ";
                                viewByCol.append(",").append(bucketName).append("_").append(reportId).append(".bucket_name " + "  v").append(viewByList.size() + 1).append("bucket_name ");
               viewByColGroup+=","+bucketName+"_"+reportId+".bucket_name ";
               orderByColGroup+=","+bucketName+"_"+reportId+".bucket_name ";
               viewByList.add( "v"+(viewByList.size()+1)+"bucket_name ");
               String fromtablesarr[]=this.fromTables.toUpperCase().split("WHERE");
               String tableslist= fromtablesarr[0]+","+grpcase+" WHERE  "+ fromtablesarr[1];
                this.fromTables=tableslist;
               }else{


                                // ////.println("viewByList=rrr=="+viewByList);
                                ArrayList newViewByList = new ArrayList();

                                for (int k = 0; k < viewByList.size(); k++) {
                                    // ////.println("in for loop=="+k);
                                    if (Integer.parseInt(replaceIndex) == k) {
                                        // ////.println("in if--------");
                    if(k==0){
        //              viewByCol=bucketName+"_"+reportId+".bucket_name "+"  v"+(viewByList.size()+1)+"bucket_name ";
                                            viewByCol.append(bucketName).append("_").append(reportId).append(".bucket_name " + "  v").append(viewByList.size() + 1).append("bucket_name ");
                      viewByColGroup=bucketName+"_"+reportId+".bucket_name ";
                      orderByColGroup=bucketName+"_"+reportId+".bucket_name ";

                    }else{
                    //  viewByCol+=","+bucketName+"_"+reportId+".bucket_name "+"  v"+(viewByList.size()+1)+"bucket_name ";
                                            viewByCol.append(",").append(bucketName).append("_").append(reportId).append(".bucket_name " + "  v").append(viewByList.size() + 1).append("bucket_name ");
                      viewByColGroup+=","+bucketName+"_"+reportId+".bucket_name ";
                      orderByColGroup+=","+bucketName+"_"+reportId+".bucket_name ";

                                        }
                                        newViewByList.add("v" + (viewByList.size() + 1) + "bucket_name ");

                                    } else {
                                        // ////.println("in if--else------");
                     if(k==0){
         //            viewByCol=viewByList.get(k).toString().substring(2)+"  v"+(k+1)+viewByList.get(k).toString().substring(2);
                                            viewByCol.append(viewByList.get(k).toString().substring(2)).append("  v").append(k + 1).append(viewByList.get(k).toString().substring(2));
                      viewByColGroup=viewByList.get(k).toString().substring(2);
                      orderByColGroup=viewByList.get(k).toString().substring(2);

                    }else{
           //           viewByCol+=","+viewByList.get(k).toString().substring(2)+"  v"+(k+1)+viewByList.get(k).toString().substring(2);
                                            viewByCol.append(",").append(viewByList.get(k).toString().substring(2)).append("  v").append(k + 1).append(viewByList.get(k).toString().substring(2));
                      viewByColGroup+=","+viewByList.get(k).toString().substring(2);
                      orderByColGroup+=","+viewByList.get(k).toString().substring(2);
                                        }
                                        newViewByList.add("v" + (k + 1) + viewByList.get(k).toString().substring(2));
                                    }

                                    if (k == viewByList.size() - 1) {
                                        viewByList = newViewByList;
                                        String fromtablesarr[] = this.fromTables.toUpperCase().split("WHERE");
                                        String tableslist = fromtablesarr[0] + "," + grpcase + " WHERE  " + fromtablesarr[1];
                                        this.fromTables = tableslist;
                                    }
                                }

                            }

                        }

                    }
                    // ////.println("nonViewByList=="+nonViewByList);
                    // ////.println("nonViewByListRT=="+nonViewByListRT);
                    // ////.println("nonViewByMap=="+nonViewByMap);
                    // ////.println("utemp==="+utemp);
                    setCrossTabNonViewBys(nonViewByList);



                }

            }

        } catch (SQLException e) {
            logger.error("Exception:", e);
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

        temp = "";
        sqlstr = "";
        //nonViewByListRT.add("r_total");
        //nonViewByMap.put("r_total", "Total");


        // ////.println("viewByCol====gggg=="+viewByCol);

        {
            in_qry = "";
            in_qry = in_qry + " select  " + viewByCol + " ";
            in_qry = in_qry + utemp + "   ";
            /*
             * if(!isCustomCrosstab){ //
             * ////.println("----------------------------------------"); String
             * fromtablesarr[]=this.fromTables.toUpperCase().split("WHERE"); //
             * ////.println("fromtablesarr[0]===="+fromtablesarr[0]); //
             * ////.println("fromtablesarr[1]===="+fromtablesarr[1]); //
             * ////.println("
             * buckettotoueterwrapertable);"+buckettotoueterwrapertable);
             * if(!buckettotoueterwrapertable.equalsIgnoreCase("")){ in_qry =
             * in_qry + fromtablesarr[0]+","+buckettotoueterwrapertable+" WHERE
             * "+ fromtablesarr[1]; }else{ in_qry = in_qry + this.fromTables;
             *
             * }
             *
             * }else{
             */
            in_qry = in_qry + this.fromTables;

            //}
            in_qry = in_qry + this.whereClause;
            if (!isCustomCrosstab) {


                in_qry = in_qry + "group by  " + viewByColGroup + "  , " + orderByColGroup + "  ";
                if (this.summReq.equalsIgnoreCase("COUNTDISTINCT")) {
                    in_qry = in_qry + " having nvl(ABS( count(distinct (   " + this.measureName + "  ) )),0 ) >0   ";
                } else {
                    in_qry = in_qry + " having nvl(ABS(" + this.summReq + " (   " + this.measureName + "  ) ),0 ) >0   ";
                }
                if (defaultSortedColumn != null) {
                    if (!defaultSortedColumn.equalsIgnoreCase("Time")) {
                        in_qry = in_qry + "order by  " + orderByColGroup + "  ";
                        //in_qry = in_qry + "order by  " +  "A_" + defaultSortedColumn + "  ";

                    }
                } else {
                    in_qry = in_qry + "order by  " + orderByColGroup + "  ";
                    //in_qry = in_qry + "order by  " +  "A_" + defaultSortedColumn + "  ";
                }
            } else {

                in_qry = in_qry + "group by  " + viewByColGroup;
            }
            sqlstr = in_qry;
        }



        //// ////.println("i val=="+ival);
        //// ////.println("getParameterGroupAnalysisHashMap=="+getParameterGroupAnalysisHashMap());
        if (getParameterGroupAnalysisHashMap() != null && getParameterGroupAnalysisHashMap().size() != 0) {
            HashMap vlistmap = new HashMap();
            //// ////.println("viewByList=="+viewByList);
                String oviewByCol="";
                StringBuilder oviewByCol1=new StringBuilder();
                for(int k=0;k<viewByList.size();k++){
       //         oviewByCol+=",v"+(k+1)+viewByList.get(k).toString().substring(2);
                oviewByCol1.append(",v").append(k + 1).append(viewByList.get(k).toString().substring(2));
                oviewByCol=oviewByCol1.toString();
                vlistmap.put(viewByList.get(k).toString().substring(2),"v"+(k+1)+viewByList.get(k).toString().substring(2));
            }
            //// ////.println("vlistmap=="+vlistmap);
            //// ////.println("roweleIdsMap=="+roweleIdsMap);
            if (!oviewByCol.equalsIgnoreCase("")) {
                if (oviewByCol.startsWith(",")) {
                    oviewByCol = oviewByCol.substring(1);
                }
            }
          String  arrString="";
          StringBuilder arrString1=new StringBuilder();
            //// ////.println("roweleIds=="+roweleIds);
            for(int i=0;i<roweleIds.size();i++)
            {
              //  arrString+=","+roweleIds.get(i);
                arrString1.append(",").append(roweleIds.get(i));
                arrString=arrString1.toString();
            }
            if (!arrString.equalsIgnoreCase("")) {
                arrString = arrString.substring(1);
            }
            //// ////.println("arrString=="+arrString);
            String finalViewByColarr[] = arrString.split(",");
            String totoueterwraper = "";
            String grpoWrapper = "";
            if (!arrString.equalsIgnoreCase("")) {
                for (int j = 0; j < finalViewByColarr.length; j++) {
                    String grpViewBycol = finalViewByColarr[j];
                    //finalViewByColarr[j].replaceAll("\"", " ");
                    //  grpViewBycol = grpViewBycol.trim();
                    // grpViewBycol = grpViewBycol.substring(2);
                    //// ////.println("grpViewBycol==" + grpViewBycol);
                    HashMap viewgrpMap = (HashMap) getParameterGroupAnalysisHashMap().get(grpViewBycol.trim());
                    int count = 0;

                    if (viewgrpMap != null) {
                        Set s = viewgrpMap.keySet();
                        String keys[] = (String[]) s.toArray(new String[0]);
                    String groupcase = " case ";
                    StringBuilder grpcase=new StringBuilder(groupcase);
                        for (int i = 0; i < keys.length; i++) {
                            if (!keys[i].equalsIgnoreCase("others")) {
              //              grpcase += " when " +vlistmap.get(String.valueOf(roweleIdsMap.get(finalViewByColarr[j]))) + " in( " + String.valueOf(viewgrpMap.get(keys[i])) + ") then '" + keys[i] + "'";
                                grpcase.append(" when ").append(vlistmap.get(String.valueOf(roweleIdsMap.get(finalViewByColarr[j])))).append(" in( ").append(String.valueOf(viewgrpMap.get(keys[i]))).append(") then '").append(keys[i]).append("'");
                            }
                        }
     //               grpcase += " else 'others' end as  "+ vlistmap.get(String.valueOf(roweleIdsMap.get(finalViewByColarr[j])))  + "_G";
                        grpcase.append(" else 'others' end as  ").append(vlistmap.get(String.valueOf(roweleIdsMap.get(finalViewByColarr[j])))).append("_G");


                    if (!grpcase.toString().equalsIgnoreCase("")) {
                            totoueterwraper += "," + grpcase;
                        }

                    }
                }


                String mesureString = "";
                for (int i = 1; i <= ival; i++) {
                    mesureString += ",A" + i;
                }

                if (!totoueterwraper.equalsIgnoreCase("")) {
                    // totoueterwraper = totoueterwraper.substring(1);
                    //// ////.println("totoueterwraper===" + totoueterwraper);
              /*
                     * if(roweleIdsMap!=null && roweleIdsMap.size()>0) { String
                     * maparr[]=new String[roweleIdsMap.size()]; for(int
                     * k=0;k<roweleIdsMap.size();k++) {
                     * maparr[k]=String.valueOf(roweleIdsMap.get(k));
                     *
                     * }
                     * if(viewByCol.startsWith(",")){
                     * viewByCol=viewByCol.substring(1); String
                     * viewByColarr[]=viewByCol.split(",");
                     *
                     * }
                     * }
                     */



                    sqlstr = " select " + oviewByCol + "" + totoueterwraper + mesureString + " from (" + sqlstr + ")";
                }
                //// ////.println("sqlstr==="+sqlstr);
            }
        }
        // ////.println("sqlstr=///=="+sqlstr);
        setCrossTabQuery(sqlstr);

        return sqlstr;
    }

    ///////////////
    public String get2dQueryOld() throws SQLException {
        String temp = "";
        String utemp = "";
        String sqlstr = "";
        String temp1 = "";
        String in_qry = "";

        int nocol = 0;

        Connection con = null;
        CallableStatement st = null;
        ResultSet rs = null;
//        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();


        sqlstr = this.headerQuery;
        nonViewbyColumnList = null;
        try {

            con = getConnection(this.elementIdForConn);
            st = con.prepareCall(sqlstr);
            rs = st.executeQuery();

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ////.println.println(sqlstr);




            while (rs.next()) {
                temp = rs.getString(1).replace("'", "''");
                if (temp.length() >= 30) {
                    temp1 = temp.substring(1, 20) + ".." + temp.substring(temp.length() - 5, temp.length() - 1);
                } else {
                    temp1 = temp;
                }

                temp1 = temp1.replace(" & ", "");
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ////.println.println("++++====Amit==+++++" + temp1);
                if (nonViewbyColumnList == null) {
                    nonViewbyColumnList = temp;
                } else {
                    nonViewbyColumnList += "," + temp;
                }
                nonViewByList.add(temp);
                nonViewByListRT.add(temp);
                utemp = utemp + " ,  nvl(" + this.summReq + "( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  ) ,0)   \"" + temp1 + "\"  ";

                nocol = nocol + 1;
            }
        } catch (SQLException e) {
            System.err.println("<P> SQL error: <PRE> " + e + " </PRE> </P>\n");
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

        temp = "";
        sqlstr = "";
        nonViewByListRT.add("r_total");




        {
            in_qry = "";
            in_qry = in_qry + " select  " + viewByCol + " ";
            in_qry = in_qry + utemp + " , nvl(" + this.summReq + " (   " + this.measureName + "   ),0 ) r_total   ";
            in_qry = in_qry + this.fromTables;
            in_qry = in_qry + this.whereClause;
            in_qry = in_qry + "group by  " + viewByColGroup + "  , " + orderByColGroup + "  ";
            in_qry = in_qry + "order by  " + orderByColGroup + "  ";


            sqlstr = in_qry;

        }
        return sqlstr;
    }

    public String get2dQueryWithPer() throws SQLException {
        String temp = "";
        String utemp = "";
        String sqlstr = "";
        String temp1 = "";
        String in_qry = "";

        int nocol = 0;

        Connection con = null;
        CallableStatement st = null;
        ResultSet rs = null;
//        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();


        sqlstr = this.headerQuery;
        nonViewbyColumnList = null;
        try {

            con = ProgenConnection.getInstance().getConnection();
            st = con.prepareCall(sqlstr);
            rs = st.executeQuery();






            while (rs.next()) {
                temp = rs.getString(1).replace("'", "''");
                if (temp.length() >= 30) {
                    temp1 = temp.substring(1, 20) + ".." + temp.substring(temp.length() - 5, temp.length() - 1);
                } else {
                    temp1 = temp;
                }

                temp1 = temp1.replace(" & ", "");

                if (nonViewbyColumnList == null) {
                    nonViewbyColumnList = temp;
                } else {
                    nonViewbyColumnList += "," + temp;
                }
                nonViewByList.add(temp);
                nonViewByListRT.add(temp);
                utemp = utemp + " ,  nvl(round(" + this.summReq + "( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  ) /decode(" + this.summReq + " (   " + this.measureName + "   ),0,null," + this.summReq + " (   " + this.measureName + "   ) )*100 ,2),0)  \"" + temp + "\"  ";

                nocol = nocol + 1;
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
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

        temp = "";
        sqlstr = "";
        nonViewByListRT.add("r_total");




        {
            in_qry = "";
            in_qry = in_qry + " select  " + viewByCol + " ";
            in_qry = in_qry + utemp + " ,nvl((" + this.summReq + " (   " + this.measureName + "   )*100 )/decode(nvl(" + this.summReq + " (   " + this.measureName + "   ),0 ),0,null,nvl(" + this.summReq + " (   " + this.measureName + "   ),0 ))         ,0 )  r_total   ";//nvl(" + this.summReq  + " (   " + this.measureName + "   ),0 )
            in_qry = in_qry + this.fromTables;
            in_qry = in_qry + this.whereClause;
            in_qry = in_qry + "group by  " + viewByColGroup + "  , " + orderByColGroup + "  ";
            in_qry = in_qry + "order by  " + orderByColGroup + "  ";


            sqlstr = in_qry;

        }
        return sqlstr;
    }

    public String get2dQueryWithGT() throws SQLException {
        String temp = "";
        String utemp = "";
        String qout = "";
        String sqlstr = "";
        String in_qry = "";

        int nocol = 0;

        Connection con = null;
        CallableStatement st = null;
        ResultSet rs = null;
//        ProgenConnection pg = null;
//        pg = new ProgenConnection();
        nonViewByList = new ArrayList();
        nonViewByListRT = new ArrayList();


        sqlstr = this.headerQuery;
        nonViewbyColumnList = null;
        try {

            con = ProgenConnection.getInstance().getConnection();
            st = con.prepareCall(sqlstr);
            rs = st.executeQuery();






            while (rs.next()) {
                temp = rs.getString(1).replace("'", "''");
                if (nonViewbyColumnList == null) {
                    nonViewbyColumnList = temp;
                } else {
                    nonViewbyColumnList += "," + temp;
                }
                nonViewByList.add(temp);
                nonViewByListRT.add(temp);
                utemp = utemp + " ,  nvl(" + this.summReq + "( Case when " + headerViewByCol + " = '" + temp + "' then    " + this.measureName + "  else null end  ) ,0)/decode(" + this.summReq + " (   " + this.measureName + "   ),0,null," + this.summReq + " (   " + this.measureName + "   ) )   \"" + temp + "\"  ";
                qout = qout + "  ,  nvl (sum (\"" + temp + "\" ) over(),0) \"G_" + temp + "\"  , (\"" + temp + "\")  \"" + temp + "\"  ";

                nocol = nocol + 1;
            }
        } catch (SQLException e) {
            logger.error("Exception:", e);
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

        temp = "";
        sqlstr = "";
        nonViewByListRT.add("r_total");




        {
            in_qry = "";
            in_qry = in_qry + " select  " + viewByCol + "  , " + orderByCol + "  ";
            in_qry = in_qry + utemp + " , nvl(" + this.summReq + " (   " + this.measureName + "   ),0 ) r_total   ";
            in_qry = in_qry + this.fromTables;
            in_qry = in_qry + this.whereClause;
            in_qry = in_qry + "group by  " + viewByColGroup + "  , " + orderByColGroup + "  ";
            in_qry = in_qry + "order by  " + orderByColGroup + "  ";


            sqlstr = " select " + this.outerViewByCol + qout + " , r_total from (" + in_qry + ") order by " + this.outerOrderByCol;
            // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ////.println.println(sqlstr);
        }
        return sqlstr;
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

    public String getDefaultSortedColumn() {
        return defaultSortedColumn;
    }

    public void setDefaultSortedColumn(String defaultSortedColumn) {
        this.defaultSortedColumn = defaultSortedColumn;
    }

    ////////////////
    public boolean isIsheaderTrend() {
        return isheaderTrend;
    }

    public void setIsheaderTrend(boolean isheaderTrend) {
        this.isheaderTrend = isheaderTrend;
    }

    public static void main(String[] a) throws SQLException {
        PbCrossTabQuery cbt = new PbCrossTabQuery();
        cbt.elementIdForConn = "14303";
        cbt.setHeaderQuery("SELECT  distinct   (d.prg_display_short_name) prg_display_short_name ,"
                + " (d.prg_display_short_name) SERIES_CODE1  FROM PRG_IND_MSL_DATA  M ,"
                + " PRG_IND_SERIES_MSL_EDT D where upper(m.series_code) = upper(d.series_code) "
                + " and series_category = 'Consumer Demography' and "
                + " PRG_DISPLAY_NAME is not null");
        cbt.setHeaderViewByCol("d.prg_display_short_name");
        cbt.setViewByCol("m.STATE_CODE");
        cbt.setOrderByCol("m.STATE_CODE");
        cbt.setViewByCol("m.DIST_CODE");
        cbt.setOrderByCol("m.DIST_CODE");
        cbt.setViewByCol("m.YR_CODE");
        cbt.setOrderByCol("m.YR_CODE");
        cbt.setViewByCol("d.PRG_BUCKET");
        cbt.setOrderByCol("d.PRG_BUCKET");
        cbt.setMeasureSummValues("VALUE", "Sum");
        cbt.setFromTables(" PRG_IND_MSL_DATA  M");
        cbt.setFromTables("PRG_IND_SERIES_MSL_EDT D ");
        cbt.setWhereClause(" upper(m.series_code) = upper(d.series_code) ");
        cbt.setWhereClause(" series_category = 'Consumer Demography'");
        String sqlstr = cbt.get2dQueryOld();
        // ////.println.println("sqlstr is  " + sqlstr);
    }

    /**
     * @return the ParameterGroupAnalysisHashMap
     */
    public HashMap getParameterGroupAnalysisHashMap() {
        return ParameterGroupAnalysisHashMap;
    }

    /**
     * @param ParameterGroupAnalysisHashMap the ParameterGroupAnalysisHashMap to
     * set
     */
    public void setParameterGroupAnalysisHashMap(HashMap ParameterGroupAnalysisHashMap) {
        this.ParameterGroupAnalysisHashMap = ParameterGroupAnalysisHashMap;
    }

    public String getCrossTabQuery() {
        return crossTabQuery;
    }

    public void setCrossTabQuery(String crossTabQuery) {
        this.crossTabQuery = crossTabQuery;
    }

    public ArrayList getCrossTabNonViewBys() {
        return crossTabNonViewBys;
    }

    public void setCrossTabNonViewBys(ArrayList crossTabNonViewBys) {
        this.crossTabNonViewBys = crossTabNonViewBys;
    }
}
