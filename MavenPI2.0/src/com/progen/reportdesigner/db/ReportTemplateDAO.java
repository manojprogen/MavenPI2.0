/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.reportdesigner.db;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.progen.cache.ReportCacheManager;
import com.progen.cacheLayer.CacheLayer;
import com.progen.db.ProgenDataSet;
import com.progen.oneView.bd.OneViewBD;
import com.progen.query.RTMeasureElement;
import com.progen.report.*;
import com.progen.report.colorgroup.ColorCodeTransferObject;
import com.progen.report.colorgroup.ColorGroup;
import com.progen.report.display.DisplayParameters;
import com.progen.report.display.GetDefaultParameterLov;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.excel.*;
import com.progen.report.query.GetDimFactMapping;
import com.progen.report.query.PbReportQuery;
import com.progen.report.query.PbTimeRanges;
import com.progen.report.segmentation.Segment;
import com.progen.report.whatIf.WhatIfScenario;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.reportview.db.JsonGenerator;
import com.progen.reportview.db.PbReportViewerDAO;
import com.progen.scheduler.ReportDetails;
import com.progen.scheduler.ReportSchedule;
import com.progen.studio.StudioDao;
import com.progen.users.PrivilegeManager;
import com.progen.wigdets.ProgenWidgetsDAO;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OraclePreparedStatement;
import org.apache.log4j.Logger;
import prg.db.*;
import utils.db.ProgenConnection;

/*
 * @filename ReportTemplateDAO @author santhosh.kumar@progenbusiness.com @date
 * Sep 18, 2009, 4:59:03 PM
 */

public class ReportTemplateDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ReportTemplateDAO.class);
    private HashMap TableHashMap = null;
    ResourceBundle resourceBundle;// = new ReportTemplateResourceBundle();
    private ArrayList reportQryColNames = null;
    private ArrayList reportQryColTypes = null;
    private ArrayList originalReportQryColTypes = null;
    private HashMap ParameterGroupMap = null;
    private boolean overWriteExistingReport = false;
    public String dateEleId = "";
    String avgvaltype = "";
    HashMap avgcalmap = new HashMap();
    Set avgvalcount = new HashSet();
//    private String buildPbreturnObject(Container firstContainer, Container secondContainer, ArrayList<String> mergeDispCols, ArrayList<String> mergeDispLabels, ArrayList<Integer> mergeSequence, int numOfRowViewBy,String userId) {
//
//        ProgenDataSet firstRetObj = firstContainer.getRetObj();
//        ProgenDataSet secondRetObj = secondContainer.getRetObj();
//        ArrayList<String> firstRowViewBy = firstContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> secRowViewBy = secondContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> firstColViewBy=firstContainer.getReportCollect().reportColViewbyValues;
//        ArrayList<String> secColViewBy=secondContainer.getReportCollect().reportColViewbyValues;
//
//        int rowViewLength = firstRowViewBy.size();
//
//        ArrayList<String> rowViewByFirstCont = new ArrayList<String>();
//        ArrayList<String> rowViewBySecCont = new ArrayList<String>();
//        for (int m = 0; m < rowViewLength; m++) {
//            String changeRowViewfirstCont = ("A_").concat(firstRowViewBy.get(m));
//            rowViewByFirstCont.add(changeRowViewfirstCont);
//            String changeRowViewSecCont = ("A_").concat(secRowViewBy.get(m));
//            rowViewBySecCont.add(changeRowViewSecCont);
//        }
//
//
////        int p=0;
////        ArrayList<String> sortTyp = new ArrayList<String>();
////        ArrayList<String> sortDataTyp = new ArrayList<String>();
//        char[] sortType = new char[rowViewLength];
//        char[] sortDataType = new char[rowViewLength];
//        for (int p = 0; p < rowViewLength; p++) {
//            sortType[p] = '0';
//            sortDataType[p] = 'C';
//        }
////        while(p<rowViewLength)
////        {
////
//////            sortTyp.add("0");
//////            sortDataTyp.add("C");
//////            p++;
////        }
//        ArrayList<Integer> firstSeq = new ArrayList<Integer>();
//        ArrayList<Integer> secSeq = new ArrayList<Integer>();
//        for (int q = 0; q < rowViewLength; q++) {
//            firstSeq = firstRetObj.sortDataSet(rowViewByFirstCont, sortType, sortDataType);
//            secSeq = secondRetObj.sortDataSet(rowViewBySecCont, sortType, sortDataType);
//        }
//
////        DataSnapshotGenerator snapGen=new DataSnapshotGenerator();
////        snapGen.generateAndStoreHtmlSnapshot(secondContainer, userId);
//
//        PbReturnObject newRetObj = new PbReturnObject();
//        String[] colNameArray = new String[mergeDispLabels.size()];
//
//        for (int i = 0; i < mergeDispCols.size(); i++) {
////            colNameArray[i]=mergeDispCols.get(i);
//            colNameArray[i] = mergeDispLabels.get(i);
//        }
//        newRetObj.setColumnNames(colNameArray);
//        int length = 0;
//
////        if(firstRetObj.getRowCount()==secondRetObj.getRowCount()||firstRetObj.getRowCount()>secondRetObj.getRowCount())
////            length=firstRetObj.getRowCount();
////        else
////            length=secondRetObj.getRowCount();
//
//        if (firstSeq.size() == secSeq.size() || firstSeq.size() > secSeq.size()) {
//            length = firstSeq.size();
//        } else {
//            length = secSeq.size();
//            for (int x = 0; x < numOfRowViewBy; x++) {
//                int seq = mergeSequence.get(x);
//                mergeSequence.set(x, 2);
//            }
//        }
//        int z = 0;
//        String compareHtml="";
//        ArrayList<String> firstDimValues = new ArrayList<String>();
//        ArrayList<String> secDimValues = new ArrayList<String>();
//        if( firstColViewBy.isEmpty() && secColViewBy.isEmpty()){
//            for (int r = 0; r < length; r++) {
//    //            for(int s=0;s<rowViewLength;s++){
//                if (firstRowViewBy.get(z).equals(mergeDispCols.get(z).replaceFirst("1_", "").replace("A_", ""))) {
//                    if (firstSeq.size() > r) {
//                        firstDimValues.add(firstRetObj.getFieldValueString(firstSeq.get(r), mergeDispCols.get(z).replaceFirst("1_", "")));
//                    }
//                    if (secSeq.size() > r) {
//                        secDimValues.add(secondRetObj.getFieldValueString(secSeq.get(r), mergeDispCols.get(z).replaceFirst("2_", "")));
//                    }
//                }
//    //            }
//            }
//            int a = 0;
//            int b = 0;
//            int c = 0;
//    //        for(int i=0;i<length;i++)
//    //        {
//    //        int i=0;
//            int flag = 0;
//            while (a < firstDimValues.size() && b < secDimValues.size()) {
//                for (int j = 0; j < mergeDispCols.size(); j++) {
//
//    //                
////int num=firstDimValues.get(a).compareTo(secDimValues.get(b));
//                    if (firstDimValues.get(a).compareTo(secDimValues.get(b)) == 0) {
//                        String value = null;
//                        if (mergeSequence.get(j) == 1) {
//
//                            //if()
//                            //                    String v=mergeDispLabels.get(j);
//
//    //                            if(firstSeq.size()>i){
//                            //                        if(firstRowViewBy.get(z).equals(mergeDispCols.get(j).replaceFirst("1_", "").replace("A_", ""))){
//                            //                            String dimVal1=firstRetObj.getFieldValueString(firstSeq.get(i),mergeDispCols.get(j).replaceFirst("1_", ""));
//                            //                            String dimVal2=secondRetObj.getFieldValueString(secSeq.get(i),mergeDispCols.get(j).replaceFirst("1_", ""));
//                            //                            if(dimVal1.equals(dimVal2)){
//                            //
//                            //                            }
//                            //                        }
//                            //                        
//                            if (rowViewLength > j) {
//                                value = firstRetObj.getFieldValueString(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                            } else {
//                                BigDecimal val = firstRetObj.getFieldValueBigDecimal(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                                value = NumberFormatter.getModifiedNumber(val, "", -1);
//                            }
//
//
//    //                            value=firstRetObj.getFieldValueString(firstSeq.get(a),mergeDispCols.get(j).replaceFirst("1_", ""));
//    //                            }
//    //                            else{
//    //                                value1="-";
//    //                            }
//
//
//                            //                    else
//                            //                        value1="na";
//                        } else {
//    //                           if(secSeq.size()>i){
//                            //                        
//                            if (rowViewLength > j) {
//                                value = secondRetObj.getFieldValueString(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                            } else {
//                                BigDecimal val = secondRetObj.getFieldValueBigDecimal(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                                value = NumberFormatter.getModifiedNumber(val, "", -1);
//                            }
//
//    //                            value=secondRetObj.getFieldValueString(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//    //                            }
//    //                            else{
//    //                                value1="-";
//    //                            }
//                            //if(secondRetObj.getRowCount()>=i)
//                            //                    value1=secondRetObj.getFieldValueString(firstSeq.get(i), mergeDispCols.get(j).replace("_2", ""));
//                            //                    else
//                            //                        value1="na";
//
//                        }
//
//                        newRetObj.setFieldValue(colNameArray[j], value);
//                        //                    
//                        //                    value=newRetObj.getFieldValueString(i, j);
//                        flag = 0;
//    //                        a++;
//    //                        b++;
//
//                    } else if (firstDimValues.get(a).compareTo(secDimValues.get(b)) < 0) {
//                        String value = null;
//                        if (mergeSequence.get(j) == 1) {
//    //                            if(firstSeq.size()>i){
//                            if (rowViewLength > j) {
//                                value = firstRetObj.getFieldValueString(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                            } else {
//                                BigDecimal val = firstRetObj.getFieldValueBigDecimal(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                                value = NumberFormatter.getModifiedNumber(val, "", -1);
//                            }
//    //                            value=firstRetObj.getFieldValueString(firstSeq.get(a),mergeDispCols.get(j).replaceFirst("1_", ""));
//    //                            }
//                        } else {
//                            value = "-";
//                        }
//
//                        newRetObj.setFieldValue(colNameArray[j], value);
//                        flag = 1;
//    //                        a++;
//                    } else if (firstDimValues.get(a).compareTo(secDimValues.get(b)) > 0) {
//                        String value = null;
//                        if (mergeSequence.get(j) == 1) {
//                            value = "-";
//                        } else {
//    //                           if(secSeq.size()>i){
//                            if (rowViewLength > j) {
//                                value = secondRetObj.getFieldValueString(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                            } else {
//                                BigDecimal val = secondRetObj.getFieldValueBigDecimal(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                                value = NumberFormatter.getModifiedNumber(val, "", -1);
//                            }
//    //                            value=secondRetObj.getFieldValueString(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//    //                            }
//
//                        }
//
//                        newRetObj.setFieldValue(colNameArray[j], value);
//                        flag = 2;
//    //                        b++;
//                    }
//                }
//                if (flag == 0) {
//                    a++;
//                    b++;
//                } else if (flag == 1) {
//                    a++;
//                } else if (flag == 2) {
//                    b++;
//                }
//                newRetObj.addRow();
//
//            }
//            if (a < firstDimValues.size()) {
//                for (int j = 0; j < mergeDispCols.size(); j++) {
//                    String value = null;
//                    if (mergeSequence.get(j) == 1) {
//                        //                            if(firstSeq.size()>i){
//                        if (rowViewLength > j) {
//                            value = firstRetObj.getFieldValueString(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                        } else {
//                            BigDecimal val = firstRetObj.getFieldValueBigDecimal(firstSeq.get(a), mergeDispCols.get(j).replaceFirst("1_", ""));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
//    //                        value=firstRetObj.getFieldValueString(firstSeq.get(a),mergeDispCols.get(j).replaceFirst("1_", ""));
//                        //                            }
//                    } else {
//                        value = "-";
//                    }
//                    newRetObj.setFieldValue(colNameArray[j], value);
//                }
//                newRetObj.addRow();
//                a++;
//            }
//            if (b < secDimValues.size()) {
//                for (int j = 0; j < mergeDispCols.size(); j++) {
//                    String value = null;
//                    if (mergeSequence.get(j) == 1) {
//                        //                            if(firstSeq.size()>i){
//                        if (rowViewLength > j) {
//                            value = secondRetObj.getFieldValueString(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                        } else {
//                            BigDecimal val = secondRetObj.getFieldValueBigDecimal(secSeq.get(b), mergeDispCols.get(j).replaceFirst("2_", ""));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
//    //                        value=secondRetObj.getFieldValueString(secSeq.get(b),mergeDispCols.get(j).replaceFirst("2_", ""));
//                        //                            }
//                    } else {
//                        value = "-";
//                    }
//                    newRetObj.setFieldValue(colNameArray[j], value);
//                }
//                newRetObj.addRow();
//                b++;
//            }
//    //            a=0;
//    //            b=0;
//    //             newRetObj.addRow();
//    //        }
//            newRetObj.resetViewSequence();
//
//            compareHtml = "<table class=\"tablesorter\" style=\"width:100%\"> <tr>";
//            compareHtml += getHtml(newRetObj, mergeSequence);
//            compareHtml +="</table>";
//        }
//        else{
//            PbReturnObject newFirsRetObj=new PbReturnObject();
//            PbReturnObject newSecRetObj=new PbReturnObject();
//            ArrayList newFirstDispLabels=firstContainer.getDisplayLabels();
//            ArrayList<String> newFirstDispCols=firstContainer.getDisplayColumns();
//            ArrayList newSecDispLabels=secondContainer.getDisplayLabels();
//            ArrayList<String> newSecDispCols=secondContainer.getDisplayColumns();
//            String firstRepName=firstContainer.getReportName();
//            String secRepName=secondContainer.getReportName();
//            int noOfRowView=firstRowViewBy.size();
//            if(firstColViewBy.isEmpty()){
//                int colLength=firstRetObj.getColumnNames().length;
//
//                String[] colNameArr = new String[colLength];
//                for (int i = 0; i < colLength; i++) {
////                    ArrayList<String> colViewByList=new ArrayList<String>();
//                    colNameArr[i]  = newFirstDispLabels.get(i).toString();
//
////                    colViewByList=(ArrayList<String>)newFirstDispLabels.get(i);
////                    for(int m=0;m<colViewByList.size();m++){
////                        colNameArr[i] = colViewByList.get(m);
////                    }
////                    colNameArr[i] = newFirstDispLabels.get(i);
//                }
//                newFirsRetObj.setColumnNames(colNameArr);
//                ArrayList<Integer> mergeSeq=new ArrayList<Integer>();
//                for(int q=0;q<colLength;q++){
//                    mergeSeq.add(1);
//                }
//                for(int l=0;l<firstRetObj.getRowCount();l++){
//                    for( int k=0;k<colLength;k++){
//                        String value="";
//                        if(noOfRowView>k){
//                            value = firstRetObj.getFieldValueString(firstSeq.get(l), newFirstDispCols.get(k));
//                        }
//                        else{
//                            BigDecimal val = firstRetObj.getFieldValueBigDecimal(firstSeq.get(l), newFirstDispCols.get(k));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
////                        String value = firstRetObj.getFieldValueString(l, newFirstDispCols.get(k));
//                        newFirsRetObj.setFieldValue(colNameArr[k], value);
//                    }
//                    newFirsRetObj.addRow();
//                }
//                compareHtml = "<table><tr><td>";
//                compareHtml += "<table class=\"tablesorter\" style=\"width:50%\"> <tr>";
//                compareHtml += getHtml(newFirsRetObj,mergeSeq);
//                compareHtml += "</table>";
//                compareHtml += "</td>";
//            }
//            else{
//                int colLength=firstRetObj.getColumnNames().length;
//
////                ArrayList<String> newFirstDispLabels=firstContainer.getDisplayLabels();
////                ArrayList<String> newFirstDispCols=firstContainer.getDisplayColumns();
//                String[] colNameArr = new String[colLength];
//                String rowViewBy= "";
//                String[] colViewBy=new String[colLength];
//                int j=0;
//                for (int i = 0; i < colLength; i++) {
//                    ArrayList<String> colViewByList=new ArrayList<String>();
//                    if(noOfRowView>i){
//                        rowViewBy  = newFirstDispLabels.get(i).toString();
//                            colNameArr[i] = rowViewBy;
//                    }
//                    else{
//
//                        colViewByList=(ArrayList<String>)newFirstDispLabels.get(i);
////                        colViewByList.add(newSecDispLabels.get(i));
//                        for(int m=0;m<colViewByList.size()-1;m++){
//                            colViewBy[j]=colViewByList.get(0);
//                            colNameArr[i]=(colViewBy[j]).concat("-").concat(colViewByList.get(m+1));
//                        }
//                        j++;
//                    }
//                }
////                for (int i = 0; i < colLength; i++) {
////                    colNameArr[i] = newFirstDispLabels.get(i);
////                }
//                newFirsRetObj.setColumnNames(colNameArr);
//                ArrayList<Integer> mergeSeq=new ArrayList<Integer>();
//                for(int q=0;q<colLength;q++){
//                    mergeSeq.add(1);
//                }
//                for(int l=0;l<firstRetObj.getRowCount();l++){
//                    for( int k=0;k<colLength;k++){
//                        String value="";
//                        if(noOfRowView>k){
//                            value = firstRetObj.getFieldValueString(firstSeq.get(l), newFirstDispCols.get(k));
//                        }
//                        else{
//                            BigDecimal val = firstRetObj.getFieldValueBigDecimal(firstSeq.get(l), newFirstDispCols.get(k));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
////                        String value = firstRetObj.getFieldValueString(l, newFirstDispCols.get(k));
//                        newFirsRetObj.setFieldValue(colNameArr[k], value);
//                    }
//                    newFirsRetObj.addRow();
//                }
//                compareHtml = "<table><tr><td>";
//                compareHtml += "<table class=\"tablesorter\" style=\"width:50%\"> <tr>";
//                compareHtml += getHtml(newFirsRetObj,mergeSeq);
//                compareHtml += "</table>";
//                compareHtml += "</td>";
//            }
//            if(secColViewBy.isEmpty()){
//                int colLength=secondRetObj.getColumnNames().length;
//
//                String[] colNameArr = new String[colLength];
//                for (int i = 0; i < colLength; i++) {
////                    ArrayList<String> colViewByList=new ArrayList<String>();
////                    colViewByList=(ArrayList<String>)newSecDispLabels.get(i);
////                    for(int m=0;m<colViewByList.size();m++){
////                        colNameArr[i] = colViewByList.get(m);
////                    }
//                    colNameArr[i]  = newSecDispLabels.get(i).toString();
////                    colNameArr[i] = newSecDispLabels.get(i);
//                }
//                newSecRetObj.setColumnNames(colNameArr);
//                ArrayList<Integer> mergeSeq=new ArrayList<Integer>();
//                for(int q=0;q<colLength;q++){
//                    mergeSeq.add(2);
//                }
//                for(int l=0;l<secondRetObj.getRowCount();l++){
//                    for( int k=0;k<colLength;k++){
//                        String value="";
//                        if(noOfRowView>k){
//                            value = secondRetObj.getFieldValueString(secSeq.get(l), newSecDispCols.get(k));
//                        }
//                        else{
//                            BigDecimal val = secondRetObj.getFieldValueBigDecimal(secSeq.get(l), newSecDispCols.get(k));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
////                        String value = secondRetObj.getFieldValueString(l, newSecDispCols.get(k));
//                        newSecRetObj.setFieldValue(colNameArr[k], value);
//                    }
//                    newSecRetObj.addRow();
//                }
//                compareHtml += "<td>";
//                compareHtml += "<table class=\"tablesorter\" style=\"width:50%\"> <tr>";
//                compareHtml += getHtml(newSecRetObj,mergeSeq);
//                compareHtml += "</table>";
//                compareHtml += "</td></tr></table>";
//            }
//            else{
//                int colLength=secondRetObj.getColumnNames().length;
////                int noOfRowView=secRowViewBy.size();
////                ArrayList<String> newFirstDispLabels=firstContainer.getDisplayLabels();
////                ArrayList<String> newFirstDispCols=firstContainer.getDisplayColumns();
//                String[] colNameArr = new String[colLength];
//                String rowViewBy= "";
//                String[] colViewBy=new String[colLength];
//                int j=0;
//                for (int i = 0; i < colLength; i++) {
//                    ArrayList<String> colViewByList=new ArrayList<String>();
//                    if(noOfRowView>i){
//                        rowViewBy  = newSecDispLabels.get(i).toString();
//                            colNameArr[i] = rowViewBy;
//                    }
//                    else{
//
//                        colViewByList=(ArrayList<String>)newSecDispLabels.get(i);
////                        colViewByList.add(newSecDispLabels.get(i));
//                        for(int m=0;m<colViewByList.size()-1;m++){
//                            colViewBy[j]=colViewByList.get(0);
//                            colNameArr[i]=(colViewBy[j]).concat("-").concat(colViewByList.get(m+1));
//                        }
//                        j++;
//                    }
//                }
//                newSecRetObj.setColumnNames(colNameArr);
//                ArrayList<Integer> mergeSeq=new ArrayList<Integer>();
//                for(int q=0;q<colLength;q++){
//                    mergeSeq.add(2);
//                }
//                for(int l=0;l<secondRetObj.getRowCount();l++){
//                    for( int k=0;k<colLength;k++){
//                        String value="";
//                        if(noOfRowView>k){
//                            value = secondRetObj.getFieldValueString(secSeq.get(l), newSecDispCols.get(k));
//                        }
//                        else{
//                            BigDecimal val = secondRetObj.getFieldValueBigDecimal(secSeq.get(l), newSecDispCols.get(k));
//                            value = NumberFormatter.getModifiedNumber(val, "", -1);
//                        }
////                        String value = secondRetObj.getFieldValueString(l, newSecDispCols.get(k));
//                        newSecRetObj.setFieldValue(colNameArr[k], value);
//                    }
//                    newSecRetObj.addRow();
//                }
//                compareHtml += "<td>";
//                compareHtml += "<table class=\"tablesorter\" style=\"width:50%\"> <tr>";
//                compareHtml += getHtml(newSecRetObj,mergeSeq);
//                compareHtml += "</table>";
//                compareHtml += "</td></tr></table>";
//            }
//        }
//        return compareHtml;
//    }

//    public String getHtml(PbReturnObject retObj, ArrayList<Integer> mergeSequence) {
//
//        StringBuilder tableBuilder = new StringBuilder();
//
//        try {
//
//
//
//            if (retObj != null && retObj.getRowCount() > 0) {
////                tableBuilder = tableBuilder.append("<table class=\"tablesorter\" > <tr>");
//                for (int k = 0; k < retObj.getColumnNames().length; k++) {
//                    String color = "blue";
//                    if (mergeSequence.get(k) == 1) {
//                        color = " #95CEF9";//"#b4d9ee";
//                    } else {
//                        color = "cadetblue";
//                    }
//                    tableBuilder = tableBuilder.append("<th class=\"header\" style=\"background-color:"+color+"\">").append(retObj.getColumnNames()[k]).append("</th>");
//                }
//                tableBuilder.append("</tr>");
//                for (int i = 0; i < retObj.getRowCount(); i++) {
//                    tableBuilder.append("<tr>");
//                    for (int j = 0; j < retObj.getColumnCount(); j++) {
//                        tableBuilder.append("<td align='right'>").append(retObj.getFieldValueString(i, j)).append("</td>");
//                    }
//                    tableBuilder.append("</tr>");
//                }
////                tableBuilder.append("</table>");
//            }
//        } catch (Exception ex) {
//            
//        }
////        
//
//        return tableBuilder.toString();
//    }
//    public String getNewPbReturnObject(Container firstContainer, Container secondContainer, String firstRepId, String secondRepId,String userId) {
//
//        ArrayList<String> firstDispCols = firstContainer.getDisplayColumns();
//        ArrayList<String> firstDispLables = firstContainer.getDisplayLabels();
//        ArrayList<String> secDispCols = secondContainer.getDisplayColumns();
//        ArrayList<String> secDispLables = secondContainer.getDisplayLabels();
//        ArrayList<String> firstRowViewBy = firstContainer.getReportCollect().reportRowViewbyValues;
//        ArrayList<String> firstColViewBy=firstContainer.getReportCollect().reportColViewbyValues;
//        ArrayList<String> secColViewBy=secondContainer.getReportCollect().reportColViewbyValues;
//        int firstColViewBySize=firstColViewBy.size();
//        int secColViewBySize=secColViewBy.size();
//        ArrayList<String> mergeDispCols = new ArrayList<String>();
//        ArrayList<String> mergeDispLabels = new ArrayList<String>();
//        ArrayList<Integer> mergeSequence = new ArrayList<Integer>();
//        int numOfRowViewBy = 0;
//       int k=0;
//    if( firstColViewBy.isEmpty() && secColViewBy.isEmpty()){
//        for (int i = 0; i < firstDispCols.size(); i++) {
//            for (int j = 0; j < secDispCols.size(); j++) {
//                if (firstDispCols.get(i).equals(secDispCols.get(j))) {
////                    for (int k = 0; k < firstRowViewBy.size(); k++) {
//                    if(firstRowViewBy.size()>k){
//                        if (firstRowViewBy.get(k).equals(firstDispCols.get(i).replace("A_", ""))) {
//                            mergeDispCols.add(firstDispCols.get(i));
//                            mergeDispLabels.add(firstDispLables.get(i));
//                            mergeSequence.add(1);
//                            numOfRowViewBy++;
//                            k++;
//                        }
//                    } else {
//                            mergeDispCols.add(("1_").concat(firstDispCols.get(i)));
//                            mergeDispCols.add(("2_").concat(secDispCols.get(j)));
//                            mergeDispLabels.add(firstDispLables.get(i).concat("(").concat(firstContainer.getReportName()).concat(")"));
//                            mergeDispLabels.add(secDispLables.get(j).concat("(").concat(secondContainer.getReportName()).concat(")"));
//                            mergeSequence.add(1);
//                            mergeSequence.add(2);
//                           }
////                    }
//                }
//            }
//        }
//        for (int i = 0; i < firstDispCols.size(); i++) {
//            for (int j = 0; j < secDispCols.size(); j++) {
//                if (!firstDispCols.get(i).equals(secDispCols.get(j))) {
//                    if (!mergeDispCols.contains(firstDispCols.get(i)) && !mergeDispCols.contains(("1_").concat(firstDispCols.get(i)))) {
//                        mergeDispCols.add(("1_").concat(firstDispCols.get(i)));
//                        mergeDispLabels.add(firstDispLables.get(i).concat("(").concat(firstContainer.getReportName()).concat(")"));
//                        mergeSequence.add(1);
//                    }
//                    if (!mergeDispCols.contains(secDispCols.get(j)) && !mergeDispCols.contains(("2_").concat(secDispCols.get(j)))) {
//                        mergeDispCols.add(("2_").concat(secDispCols.get(j)));
//                        mergeDispLabels.add(secDispLables.get(j).concat("(").concat(secondContainer.getReportName()).concat(")"));
//                        mergeSequence.add(2);
//                    }
//                }
//            }
//        }
//    }
//        String html = buildPbreturnObject(firstContainer, secondContainer, mergeDispCols, mergeDispLabels, mergeSequence, numOfRowViewBy,userId);
//        return html;
//    }
    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ReportTemplateResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                resourceBundle = new ReportTemplateResourceBundleMySql();
            } else {
                resourceBundle = new ReportTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public String getUserGroups(String userId) {
//        String grpIds = "";
        StringBuilder grpIds = new StringBuilder(200);
        String getUserGroupsQuery = "";
        Object obj[] = null;
        String finalQuery = "";
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        try {
            getUserGroupsQuery = getResourceBundle().getString("getUserGroups");
            obj = new Object[1];
            obj[0] = userId;
            finalQuery = buildQuery(getUserGroupsQuery, obj);
            retObj = execSelectSQL(finalQuery);

            dbColumns = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
//                grpIds += "," + retObj.getFieldValueString(i, dbColumns[0]);
                grpIds.append(",").append(retObj.getFieldValueString(i, dbColumns[0]));
            }
//            if (!grpIds.equalsIgnoreCase("")) {
//                grpIds = grpIds.substring(1);
//            }
            if (grpIds.length() > 0) {
                grpIds = new StringBuilder(grpIds.substring(1));
            }
            return grpIds.toString();
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            return grpIds.toString();
        }catch (Exception ex) {
           logger.error("Exception", ex);
            return grpIds.toString();
        }

    }

    public String getUserFoldersByUserId(String pbUserId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = null;

        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            obj = new Object[1];
            obj[0] = pbUserId;
            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() != 0) {
                colNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    FolderId = retObj.getFieldValueString(i, colNames[0]);
                    FolderName = retObj.getFieldValueString(i, colNames[1]);
                    outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                    outerBuffer.append("<input type='radio' name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims();' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();

    }

    public String getUserDims(String foldersIds, String userId) {

//        ProgenLog.log(ProgenLog.FINE, this, "getUserDims", "Enter foldersIds--" + foldersIds + " userId " + userId);
        logger.info("Enter foldersIds--" + foldersIds + " userId " + userId);
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
//        String finalQuery1 = null;
//        String[] colNames1 = null;
//
//        Object obj[] = new Object[1];
//        Object obj1[] = new Object[2];
//        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
//            obj[0] = foldersIds;
//        } else {
//            obj[0] = "null";
//        }
//        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
//            obj1[0] = foldersIds;
//        } else {
//            obj1[0] = "null";
//        }
//        obj1[1] = userId;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        String busFolderId = "";
        String elementId = "";
        //  String sql = getResourceBundle().getString("getUserDims");
        // String sql1 = getResourceBundle().getString("getFavParams");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            // finalQuery = buildQuery(sql, obj);
            ////////.println("finalQuery getUserDims is : " + finalQuery);
            // retObj = execSelectSQL(finalQuery);

//added for Favourite parameters on 29-12-2009
            // finalQuery1 = buildQuery(sql1, obj1);
            // retObj1 = execSelectSQL(finalQuery1);
            // colNames1 = retObj1.getColumnNames();
            retObj = this.getUserDimensions(foldersIds, userId, favName);
            retObj1 = this.getFavoritesResultSet(foldersIds, userId, favName);
            colNames = retObj.getColumnNames();
            /*
             * userId = retObj1.getFieldValueString(0, colNames1[0]);
             * busFolderId = retObj1.getFieldValueString(0, colNames1[1]);
             * favName = retObj1.getFieldValueString(0, colNames1[2]);
             * if(retObj1.getRowCount()>0){ outerBuffer.append("<li
             * class='closed' id='" + favName + "'>"); outerBuffer.append("<img
             * src='icons pinvoke/Dimension.png'></img>");
             * outerBuffer.append("<span style='font-family:verdana;'>" +
             * favName + "</span>"); outerBuffer.append("<ul
             * id='dimName-'"+favName+"'>"); for (int i = 0; i <
             * retObj1.getRowCount(); i++) { elementId =
             * retObj1.getFieldValueString(i, colNames1[3]); if
             * (elementId.equalsIgnoreCase("AS_OF_DATE") ||
             * elementId.equalsIgnoreCase("PRG_PERIOD_TYPE") ||
             * elementId.equalsIgnoreCase("PRG_COMPARE")) {
             *
             * outerBuffer.append("<li class='closed' id='"+elementId+"'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='"+elementId+"'
             * style='font-family:verdana;'>"+elementId+"</span>");
             * outerBuffer.append("</li>"); }
             * outerBuffer.append(getFavParamMbrs(busFolderId, elementId)); }
             * outerBuffer.append("</ul>"); outerBuffer.append("</li>"); }
             */
//end

            String minTimeLevel = getUserFolderMinTimeLevel(foldersIds);
            if (minTimeLevel.equals("5")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Period Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Period Basis' style='font-family:verdana;'>Time-Period Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Range Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Range Basis' style='font-family:verdana;'>Time-Range Basis</span>");
                outerBuffer.append("</li>");
                //commented for spanco on 1
                outerBuffer.append("<li class='closed' id='timeDimension-Rolling Period'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Rolling Period' style='font-family:verdana;'>Time-Rolling Period</span>");
                outerBuffer.append("</li>");

//                //commented this code only for Nerolac
//
//                outerBuffer.append("<li class='closed' id='timeDimension-Month Range Basis'>");
//                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
//                outerBuffer.append("<span id='elmnt-Time Dimension-Month Range Basis' style='font-family:verdana;'>Time-Month Range Basis</span>");
//                outerBuffer.append("</li>");
//                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Range Basis'>");
//                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
//                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Range Basis' style='font-family:verdana;'>Time-Quarter Range Basis</span>");
//                outerBuffer.append("</li>");
//                outerBuffer.append("<li class='closed' id='timeDimension-Year Range Basis'>");
//                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
//                outerBuffer.append("<span id='elmnt-Time Dimension-Year Range Basis' style='font-family:verdana;'>Time-Year Range Basis</span>");
//                outerBuffer.append("</li>");
//                outerBuffer.append("<li class='closed' id='timeDimension-Cohort Basis'>");
//                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
//                outerBuffer.append("<span id='elmnt-Time Dimension-Cohort Basis' style='font-family:verdana;'>Time-Cohort Basis</span>");
//                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("4")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Week Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Week Basis' style='font-family:verdana;'>Time-Week Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("3")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Month Basis' style='font-family:verdana;'>Time-Month Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Compare Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Compare Month Basis' style='font-family:verdana;'>Time-Compare Month Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("2")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis' style='font-family:verdana;'>Time-Quarter Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("1")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;'>Time-Year Basis</span>");
                outerBuffer.append("</li>");
            }
//added for favparams
            if (retObj1.getRowCount() > 0) {
                outerBuffer.append("<li class='closed'>");
                outerBuffer.append("<img src='icons pinvoke/folder-horizontal.png'>&nbsp;<span>Favourite Params</span>");
                outerBuffer.append("<ul id='favourParams' class='background'>");
                outerBuffer.append(getFavParams(foldersIds, userId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.error("Exception: ", exception);
        }
        logger.info("successful");
        logger.info("Exit");
        return outerBuffer.toString();

    }

    private PbReturnObject getFavoritesResultSet(String foldersIds, String userId, String favName) {
        PbReturnObject pbro = new PbReturnObject();
        String sql = getResourceBundle().getString("getFavParams");
        Object obj[] = new Object[2];
        String finalQuery = null;
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "null";
        }
        obj[1] = userId;
        finalQuery = buildQuery(sql, obj);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }

        logger.info("successful");
        return pbro;
    }

//Added by Ram
    public PbReturnObject getLookupData(String viewByEleIds) {
        String finalQuery = null;
        PbReturnObject pbro = null;
        if (viewByEleIds != null && !viewByEleIds.isEmpty()) {
            String sql = getResourceBundle().getString("getlookupdata");
            Object obj[] = new Object[1];
            obj[0] = viewByEleIds;
            finalQuery = buildQuery(sql, obj);
            try {
                pbro = execSelectSQL(finalQuery);
            } catch (SQLException ex) {
               logger.error("Exception", ex);
            }catch (Exception ex) {
               logger.error("Exception", ex);
            }
        }
        logger.info("successful");
        return pbro;
    }

    public PbReturnObject getDimsData() {
        PbReturnObject pbro = new PbReturnObject();
        String finalQuery = null;
        String sql = getResourceBundle().getString("getdimdata");
        Object obj[] = new Object[1];
        obj[0] = 'Y';
        finalQuery = buildQuery(sql, obj);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }

        logger.info("successful");
        return pbro;
    }

    public String unableLookup(String dimName) {
        PbReturnObject pbro = new PbReturnObject();
        String finalQuery = null;
        String message = "";
        int unable = 0;
        String sql = getResourceBundle().getString("unableLookup");
        Object obj[] = new Object[1];
        obj[0] = dimName;
        finalQuery = buildQuery(sql, obj);
        try {
            unable = execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);

        }
        logger.info("successful");
        if (unable != 0) {
            message = "Lookup Unable successfully.";
        } else {
            message = "Lookup Unable is not successfully.";
        }
        return message;
    }

    public String enableLookup(String dimName) {
        PbReturnObject pbro = new PbReturnObject();
        String finalQuery = null;
        String message = "";
        int enable = 0;
        String sql = getResourceBundle().getString("enableLookup");
        Object obj[] = new Object[1];
        obj[0] = dimName;
        finalQuery = buildQuery(sql, obj);
        try {
            enable = execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        logger.info("successful");
        if (enable != 0) {
            message = "Lookup Enable successfully.";
        } else {
            message = "Lookup Enable is not successfully.";
        }
        return message;
    }
    //Ended by ram    
    public PbReturnObject getUserDimensions(String foldersIds, String userId, String favName) {
        PbReturnObject pbro = new PbReturnObject();
        String sql = getResourceBundle().getString("getUserDims");
        Object obj[] = new Object[1];
        String finalQuery = null;
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "null";
        }
        finalQuery = buildQuery(sql, obj);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return pbro;
    }

    public String editFavoriteParameters(String foldersIds, String userId, String favName) {
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
//        String finalQuery = null;
        String[] colNames = null;
//        String finalQuery1 = null;
//        String[] colNames1 = null;
//
//        Object obj[] = new Object[1];
//        Object obj1[] = new Object[2];
//        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
//            obj[0] = foldersIds;
//        } else {
//            obj[0] = "null";
//        }
//        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
//            obj1[0] = foldersIds;
//        } else {
//            obj1[0] = "null";
//        }
//        obj1[1] = userId;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favParamDesc = "";
//        String busFolderId = "";
//        String elementId = "";
//        String sql = getResourceBundle().getString("getUserDims");
//        String sql1 = getResourceBundle().getString("getFavParams");
        StringBuilder outerBuffer = new StringBuilder("");
        ArrayList<String> paramsList = new ArrayList<String>();
        try {
//            finalQuery = buildQuery(sql, obj);
//            retObj = execSelectSQL(finalQuery);

//            finalQuery1 = buildQuery(sql1, obj1);
//
//            retObj1 = execSelectSQL(finalQuery1);
            retObj = this.getUserDimensions(foldersIds, userId, favName);
            retObj1 = this.getFavoritesResultSet(foldersIds, userId, favName);
            colNames = retObj.getColumnNames();
//            colNames1 = retObj1.getColumnNames();

            if (retObj1.getRowCount() > 0) {
                for (int i = 0; i < retObj1.getRowCount(); i++) {
                    if (retObj1.getFieldValueString(i, "FAV_PARAM_NAME").equalsIgnoreCase(favName)) {
                        paramsList.add(retObj1.getFieldValueString(i, "ELEMENT_ID"));
                        favParamDesc = retObj1.getFieldValueString(i, "FAV_PARAM_DESC");
                    }
                }
                outerBuffer.append(getFavoriteParameters(foldersIds, userId, favName));
            }
            outerBuffer.append("@");
            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append(getNonFavoriteParamters(subFolderId, dimId, paramsList));
            }
            outerBuffer.append("<input type='hidden' name='favouriteParamName' id='favouriteParamName' value=").append(favName).append(">");
            outerBuffer.append("<input type='hidden' name='favouriteParamDesc' id='favouriteParamDesc' value=").append(favParamDesc).append(">");
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getNonFavoriteParamters(String subFolderId, String dimId, ArrayList<String> paramsLst) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            ////////.println("getUserDimsMbrs query is : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                if (!paramsLst.contains(elementid)) {
                    outerBuffer.append("<li class='navtitle-hover DimensionULClass' id='" + elementid + "'  style='width: 200px; height: auto; color: white;'>");
                    outerBuffer.append("<table id='" + MbrId + "'><tr><td style='color: black;'>" + MbrName + "</td></tr></table>");
                    outerBuffer.append("</li>");
                }
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public PbReturnObject getNonFavoriteParamters(String subFolderId, String dimId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;
        String sql = getResourceBundle().getString("getUserDimsMbrs");
        try {

            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return retObj;
    }

    private String getFavoriteParameters(String foldersIds, String usrId, String favouriteName) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        PbReturnObject retObj1 = null;
        String finalQuery1 = null;
        String[] colNames1 = null;
        Object obj[] = new Object[2];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        obj[1] = usrId;
        String userId = "";
        String favName = "";
        String subFolderId = "";
        String elementId = "";
        String userId1 = "";
        String favName1 = "";
        String subFolderId1 = "";
        String sql = getResourceBundle().getString("getFavParams");
        String getFavParamNamessql = getResourceBundle().getString("getFavParamNames");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            finalQuery1 = buildQuery(getFavParamNamessql, obj);
            //  
            //  
            retObj1 = execSelectSQL(finalQuery1);
            colNames1 = retObj1.getColumnNames();
            ArrayList<String> favLst = new ArrayList<String>();
            for (int j = 0; j < retObj1.getRowCount(); j++) {
                userId = retObj1.getFieldValueString(j, colNames1[0]);
                subFolderId = retObj1.getFieldValueString(j, colNames1[1]);
                favName = retObj1.getFieldValueString(j, colNames1[2]);

                for (int i = 0; i < retObj.getRowCount(); i++) {
                    userId1 = retObj.getFieldValueString(i, colNames[0]);
                    subFolderId1 = retObj.getFieldValueString(i, colNames[1]);
                    favName1 = retObj.getFieldValueString(i, colNames[2]);
                    elementId = retObj.getFieldValueString(i, colNames[3]);
                    if (favName.equalsIgnoreCase(favName1) && favName.equalsIgnoreCase(favouriteName)) {
                        outerBuffer.append(getFavoriteParameterMembers(subFolderId, elementId));
                    }
                }
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    private String getFavoriteParameterMembers(String subFolderId, String elementId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = elementId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getFavParamMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed navtitle-hover' id='drop" + elementid + "' style='width: 200px; height: auto; color: white;'>");
                outerBuffer.append("<table id='" + MbrId + "'><tr><td><a class='ui-icon ui-icon-close' href=javascript:deleteFavouriteDim('drop" + elementid + "')></a></td><td style='color: black;'>" + MbrName + "</td></tr></table>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getCustomMembers(String foldersIds) {
//        ProgenLog.log(ProgenLog.FINE, this, "getCustomMembers", "Enter foldersIds--" + foldersIds);
        logger.info("Enter foldersIds--" + foldersIds);
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        String custmemName = "";

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = "SELECT distinct USER_COL_NAME,REF_ELEMENT_ID from PRG_USER_ALL_INFO_DETAILS  where SUB_FOLDER_TYPE='Formula' and FOLDER_ID in(" + obj[0] + ") order by USER_COL_NAME,REF_ELEMENT_ID";
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            retObj.writeString();
            for (int i = 0; i < retObj.getRowCount(); i++) {

                custmemName = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed'>");
                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + custmemName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }

        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getUserDimsMbrs(String subFolderId, String dimId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = dimId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            ////////.println("getUserDimsMbrs query is : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");

                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }

        logger.info("successful");
        return outerBuffer.toString();
    }

    public HashMap getGrpDimensionsBySize(HashMap GraphDetails, String grpSize) {
        try {
            String sql = getResourceBundle().getString("getGrpDimensionsBySize");
            String finalQuery = "";
            Object[] Obj = new Object[1];
            finalQuery = buildQuery(sql, Obj);
            PbReturnObject retObj = execSelectSQL(finalQuery);
            String[] dbColumns = retObj.getColumnNames();

            GraphDetails.put("graphWidth", retObj.getFieldValueString(0, dbColumns[0]));
            GraphDetails.put("graphHeight", retObj.getFieldValueString(0, dbColumns[1]));
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return GraphDetails;
    }

    public String getMeasures(String foldersIds, ArrayList Parameters, String contextPath) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        if (Parameters != null && !Parameters.isEmpty()) {
//            String dimFactRel = "select fact_id from ( "
//                    + "SELECT info.DIM_ID,info.BUSS_TABLE_ID,rlt.buss_table_id2 fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id "
//                    + "and rlt.buss_table_id2=isfact.buss_table_id and isfact.is_fact='Y' "
//                    + "union "
//                    + "SELECT info.ELEMENT_ID,info.BUSS_TABLE_ID,rlt.buss_table_id fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id2 "
//                    + "and rlt.buss_table_id=isfact.buss_table_id and isfact.is_fact='Y')b1 "
//                    + "group by fact_id having count(distinct DIM_ID) >=(select count (distinct DIM_ID) "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS where element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and buss_table_id = fact_id) ";

            obj = new Object[2];
            obj[0] = foldersIds;
//            obj[1] = dimFactRel;
//                    try {
//           obj[1] = getdimmap.getFact(Parameters);
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }

//            obj[1] = dimFactRel;
            try {
                obj[1] = getdimmap.getFact(Parameters);
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
                logger.error("Exception", ex);
            }

            logger.info("successful");
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                outerBuffer.append("<span class=\"gFontFamily gFontSize12\" title='" + facttooltip + "'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");
                outerBuffer.append(getMeasureElements(userFolderIds, subFolderTabid, contextPath));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getMeasuresForReport(String foldersIds, ArrayList Parameters, String contextPath, ArrayList tabIds) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        String tableIds = "";
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        if (Parameters != null && !Parameters.isEmpty()) {
//            String dimFactRel = "select fact_id from ( "
//                    + "SELECT info.DIM_ID,info.BUSS_TABLE_ID,rlt.buss_table_id2 fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id "
//                    + "and rlt.buss_table_id2=isfact.buss_table_id and isfact.is_fact='Y' "
//                    + "union "
//                    + "SELECT info.ELEMENT_ID,info.BUSS_TABLE_ID,rlt.buss_table_id fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id2 "
//                    + "and rlt.buss_table_id=isfact.buss_table_id and isfact.is_fact='Y')b1 "
//                    + "group by fact_id having count(distinct DIM_ID) >=(select count (distinct DIM_ID) "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS where element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and buss_table_id = fact_id) ";

            obj = new Object[2];
            obj[0] = foldersIds;
//            obj[1] = dimFactRel;
//                    try {
//           obj[1] = getdimmap.getFact(Parameters);
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }

//            obj[1] = dimFactRel;
            if (tabIds != null && !tabIds.isEmpty()) {
                for (int i = 0; i < tabIds.size(); i++) {
                    if (i == 0) {
                        tableIds = (String) tabIds.get(i);
                    } else {
                        tableIds = tableIds + "," + (String) tabIds.get(i);
                    }
                }
                obj[1] = tableIds;
            }
//           obj[1] = getdimmap.getFact(Parameters);
            sql = getResourceBundle().getString("getFactsNew");
        } else if (tabIds != null && !tabIds.isEmpty()) {
            obj = new Object[2];
            obj[0] = foldersIds;
            for (int i = 0; i < tabIds.size(); i++) {
                if (i == 0) {
                    tableIds = (String) tabIds.get(i);
                } else {
                    tableIds = tableIds + "," + (String) tabIds.get(i);
                }
            }
            obj[1] = tableIds;
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                outerBuffer.append("<span class=\"gFontFamily gFontSize12\" title='" + facttooltip + "'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");
                outerBuffer.append(getMeasureElements(userFolderIds, subFolderTabid, contextPath));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getMeasureElements(String userFolderIds, String subFolderTabid, String contextPath) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        //obj[0] = userFolderIds;
        obj[0] = subFolderTabid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
        String colId = "";

        String sql = getResourceBundle().getString("getFactElements");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //  
            ////////.println("finalQuery in getmeasureelements are : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                REFElementId = retObj.getFieldValueString(i, colNames[2]);
                Formula = retObj.getFieldValueString(i, colNames[7]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                    outerBuffer.append("<li class='closed'>");
                    if (!Formula.equalsIgnoreCase("")) {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<span class=\"gFontFamily gFontSize12\" class='open ' class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "')\">" + ElementName + "</span>"); //<a href=''</a>

                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<span class=\"gFontFamily gFontSize12\" class='open' id='" + ElementId1 + "'   title='" + Formula + "' )\">" + ElementName1 + "</span>");//<a href=''></a>
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }
            //code to add custom measures

            /*
             * String getCustomFactElements =
             * getResourceBundle().getString("getCustomFactElements"); Object
             * objnew[] = new Object[2]; objnew[0] = userFolderIds; finalQuery =
             * buildQuery(getCustomFactElements, objnew);
             *
             *
             * retObj = execSelectSQL(finalQuery); ////////.println(""); for
             * (int i = 0; i < retObj.getRowCount(); i++) { String testQuery =
             * "select distinct sub_folder_tab_id from prg_user_all_info_details
             * where element_id in (" +
             * removeLastCommas(retObj.getFieldValueString(i, 5)) + ") and
             * sub_folder_tab_id=" + subFolderTabid; // //////.println("arun
             * testQuery "+testQuery); viewFormulaClass = "formulaViewMenu";
             * PbReturnObject testpbro = execSelectSQL(testQuery); if
             * (testpbro.getRowCount() > 0) { ElementId =
             * retObj.getFieldValueString(i, colNames[0]); ElementName =
             * retObj.getFieldValueString(i, colNames[1]); REFElementId =
             * retObj.getFieldValueString(i, colNames[2]); Formula =
             * retObj.getFieldValueString(i, colNames[7]); if
             * (ElementId.equalsIgnoreCase(REFElementId)) { if
             * (!(list.contains(ElementId))) { outerBuffer.append("<li
             * class='closed'"); outerBuffer.append("<img src='icons
             * pinvoke/document-attribute-f.png'></img>");
             * outerBuffer.append("<span class='" + viewFormulaClass + "' id='"
             * + ElementId + "' title='" + Formula + "'
             * style='font-family:verdana;'>" + ElementName + "</span>");
             *
             * outerBuffer.append("<ul >"); for (int j = 0; j <
             * retObj.getRowCount(); j++) { ElementId1 =
             * retObj.getFieldValueString(j, colNames[0]); REFElementId1 =
             * retObj.getFieldValueString(j, colNames[2]); ElementName1 =
             * retObj.getFieldValueString(j, colNames[1]); Formula =
             * retObj.getFieldValueString(j, colNames[7]); if
             * (ElementId.equalsIgnoreCase(REFElementId1) &&
             * !(ElementId.equalsIgnoreCase(ElementId1))) {
             * outerBuffer.append("<li class='closed' id='" + ElementId1 +
             * "'>"); outerBuffer.append("<img src='icons
             * pinvoke/document-attribute-f.png'></img>");
             * outerBuffer.append("<span id='" + ElementId1 + "' title='" +
             * Formula + "' style='font-family:verdana;'>" + ElementName1 +
             * "</span>"); outerBuffer.append("</li>"); } }
             * outerBuffer.append("</ul>"); outerBuffer.append("</li>");
             * list.add(ElementId); } } } }
             */
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String getMeasureElements1(String userFolderIds, String subFolderTabid, String contextPath, String oneviewversion) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        //obj[0] = userFolderIds;
        obj[0] = subFolderTabid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
        String colId = "";

        String sql = getResourceBundle().getString("getFactElements");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //   
            ////////.println("finalQuery in getmeasureelements are : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                REFElementId = retObj.getFieldValueString(i, colNames[2]);
                Formula = retObj.getFieldValueString(i, colNames[7]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                    outerBuffer.append("<li class='closed'>");
                    if (!Formula.equalsIgnoreCase("")) {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<a href=''><span  class='open' class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "' style='font-family:verdana;' draggable='true' ondragstart='drag(event)'  onmouseover=\"dragmeasure('" + ElementName + "','" + ElementId + "','" + userFolderIds + "')\">" + ElementName + "</span></a>"); //<a href=''</a>
                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<a href=''><span class='open' id='" + ElementId1 + "'   title='" + Formula + "' style='font-family:verdana;'draggable='true' ondragstart='drag(event)' onmouseover=\"dragmeasure('" + ElementName1 + "','" + ElementId1 + "','" + userFolderIds + "')\">" + ElementName1 + "</span></a>");//<a href=''></a>
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return outerBuffer.toString();
    }

    public String buildTable(String measures) {
        StringBuffer outerBuffer = new StringBuffer("");

        return outerBuffer.toString();
    }

    public String dispParameters(String parameters, HashMap ParametersHashMap) {
        DisplayParameters disp = new DisplayParameters();
        ArrayList dispParams = new ArrayList();
        String[] paramIds = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String temp = "";
        try {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");
            paramIds = parameters.split(",");
            HashMap paramValues = new HashMap();
            for (int i = 0; i < paramIds.length; i++) {
                dispParams.add(paramIds[i]);
                paramValues.put(paramIds[i], "All");
            }
            //temp = disp.displayParam(dispParams) + disp.displayTimeParams(TimeDimHashMap);
            temp = disp.displayParamwithTime(dispParams, TimeDimHashMap);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        logger.info("successful");
        return temp;
    }

    //added by susheela start
    public String dispParameters(String parameters, HashMap ParametersHashMap, String reportId) {
        DisplayParameters disp = new DisplayParameters();
        ArrayList dispParams = new ArrayList();
        String[] paramIds = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String temp = "";
        try {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");

            paramIds = parameters.split(",");
            HashMap paramValues = new HashMap();
            for (int i = 0; i < paramIds.length; i++) {
                dispParams.add(paramIds[i]);
                paramValues.put(paramIds[i], "All");
            }
            //temp = disp.displayParam(dispParams) + disp.displayTimeParams(TimeDimHashMap);
            temp = disp.displayParamwithTime(dispParams, TimeDimHashMap, reportId);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        logger.info("successful");
        return temp;
    }

    public ArrayList getReportQryAggregations(ArrayList<String> reportQryElementIds) {
        String sqlQuery = getResourceBundle().getString("getReportQryAggregations");
        ArrayList reportQryAggregations = new ArrayList();
        reportQryColNames = new ArrayList();
        reportQryColTypes = new ArrayList();
        originalReportQryColTypes = new ArrayList();
        //ArrayList reportQryColNames = new ArrayList();
//        String StrReportQryElementIds = "";
//        String StrReportQryElementIdsOrder = "";
        StringBuilder StrReportQryElementIds = new StringBuilder(300);
        StringBuilder StrReportQryElementIdsOrder = new StringBuilder(300);
//        String StrReportQryElementIdsOrderSqlSer = " case ";
        StringBuilder StrReportQryElementIdsOrderSqlSer = new StringBuilder(600);
        StrReportQryElementIdsOrderSqlSer.append(" case ");
        String finalQuery = "";
        PbReturnObject retObj = null;
        Object[] Obj = new Object[2];
        String[] dbColumns = null;
        boolean flag = false;
        try {

            for (int i = 0; i < reportQryElementIds.size(); i++) {
                //added by anitha for MTD,QTD,YTD in AO Report
                String RepQryId = reportQryElementIds.get(i).toString().replace("A_", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "")
                        .replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "")
                        .replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_PYtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                        .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "");
                //end of code by anitha for MTD,QTD,YTD in AO Report
//                if(!reportQryElementIds.get(i).toString().contains("_percentwise")){
//                StrReportQryElementIds += "," + String.valueOf(reportQryElementIds.get(i)).replace("A_", "");
                StrReportQryElementIds.append(",").append(RepQryId);
//                StrReportQryElementIdsOrder += "," + String.valueOf(reportQryElementIds.get(i)).replace("A_", "") + "," + (i + 1);
                StrReportQryElementIdsOrder.append(",").append(RepQryId).append(",").append((i + 1));
//                StrReportQryElementIdsOrderSqlSer += " when element_id = " + String.valueOf(reportQryElementIds.get(i)).replace("A_", "") + " then " + (i + 1) + " ";
                StrReportQryElementIdsOrderSqlSer.append(" when element_id = ").append(RepQryId).append(" then ").append((i + 1)).append(" ");
//                }

            }
//            if (!("".equalsIgnoreCase(StrReportQryElementIds))) {
//                StrReportQryElementIds = StrReportQryElementIds.substring(1);
//                StrReportQryElementIdsOrder = StrReportQryElementIdsOrder.substring(1);
//            }
            if (StrReportQryElementIds.length() > 0) {
                StrReportQryElementIds = new StringBuilder(StrReportQryElementIds.substring(1));
                StrReportQryElementIdsOrder = new StringBuilder(StrReportQryElementIdsOrder.substring(1));
            }
//            StrReportQryElementIdsOrderSqlSer += " else 1001 end ";
            StrReportQryElementIdsOrderSqlSer.append(" else 1001 end ");
            //StrReportQryElementIds = StrReportQryElementIds.replace("A_", "");
            //StrReportQryElementIdsOrder = StrReportQryElementIdsOrder.replace("A_", "");
//            //////.println("StrReportQryElementIds is : "+StrReportQryElementIds);
            Obj[0] = StrReportQryElementIds;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Obj[1] = StrReportQryElementIdsOrderSqlSer;
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Obj[1] = StrReportQryElementIdsOrderSqlSer;
            } else {
                Obj[1] = StrReportQryElementIdsOrder;
            }
            finalQuery = buildQuery(sqlQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();
            for (int j = 0; j < retObj.getRowCount(); j++) {
                if (retObj.getFieldValueString(j, dbColumns[1]) != null && !"".equalsIgnoreCase(retObj.getFieldValueString(j, dbColumns[1]))) {
//                        && !"null".equalsIgnoreCase(retObj.getFieldValueString(j, dbColumns[1]))) {
                    reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                } else {
                    reportQryAggregations.add("");
                }
//                else {
//                    reportQryAggregations.add("COUNT");
//                }
                if(flag==false){
                for (int i = 0; i < reportQryElementIds.size(); i++) {
                    String measureId = reportQryElementIds.get(i);
                    String type = "";
            if (measureId.contains("_MTD")) {
                type = "_MTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){     
                    flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } else if (measureId.contains("_PMTD")) {
                type = "_PMTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } else if (measureId.contains("_PYMTD")) {
                type = "_PYMTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){        
                    flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_QTD")) {
                type = "_QTD";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                    flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_PQTD")) {
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                type = "_PQTD";
                    flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_PYQTD")) {
                type = "_PYQTD";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                    flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_YTD")) {
                type = "_YTD";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_PYTD")) {
                type = "_PYTD";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_MOMPer")) {
                type = "_MOMPer";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");}
            } else if (measureId.contains("_MOYMPer")) {
                type = "_MOYMPer";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");}
            } else if (measureId.contains("_QOQPer")) {
                type = "_QOQPer";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");}
            } else if (measureId.contains("_QOYQPer")) {
                type = "_QOYQPer";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");}
            } else if (measureId.contains("_YOYPer")) {
                type = "_YOYPer";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");}
            } else if (measureId.contains("_MOM")) {
                type = "_MOM";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_MOYM")) {
                type = "_MOYM";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_QOQ")) {
                type = "_QOQ";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_QOYQ")) {
                type = "_QOYQ";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_YOY")) {
                type = "_YOY";
                if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){    
                flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));}
            } else if (measureId.contains("_WTD")) {
                type = "_WTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
            }
            } else if (measureId.contains("_PWTD")) {
                type = "_PWTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } else if (measureId.contains("_PYWTD")) {
                type = "_PYWTD";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } else if (measureId.contains("_WOWPer")) {
                type = "_WOWPer";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");
                    }
            } else if (measureId.contains("_WOYWPer")) {
                type = "_WOYWPer";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add("avg");
                    }
            } else if (measureId.contains("_WOW")) {
                type = "_WOW";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } else if (measureId.contains("_WOYW")) {
                type = "_WOYW";
                    if(retObj.getFieldValueString(j, dbColumns[4]).equals(reportQryElementIds.get(i).replace(type, ""))){           
                        flag = true;
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]).concat(type));
                reportQryAggregations.add(retObj.getFieldValueString(j, dbColumns[1]));
                    }
            } 
                    reportQryColTypes.add(retObj.getFieldValueString(j, dbColumns[0]));
                    originalReportQryColTypes.add(retObj.getFieldValueString(j, dbColumns[3]));
            }}
                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[2]));
                reportQryColTypes.add(retObj.getFieldValueString(j, dbColumns[0]));
                originalReportQryColTypes.add(retObj.getFieldValueString(j, dbColumns[3]));
                flag=false;
            }

            setReportQryColNames(reportQryColNames);
            setReportQryColTypes(reportQryColTypes);
            setOriginalReportQryColTypes(originalReportQryColTypes);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        logger.info("successful");
        return reportQryAggregations;
    }

    public String getGraphClassName(String grpTypeName) {
        String sqlQuery = getResourceBundle().getString("getGraphClassName");
        PbReturnObject retObj = null;
        String finalQuery = "";
        String[] dbColumns = null;
        String graphClassName = "";
        Object[] Obj = new Object[1];
        Obj[0] = grpTypeName;

        try {
            finalQuery = buildQuery(sqlQuery, Obj);
            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();

            graphClassName = retObj.getFieldValueString(0, dbColumns[0]);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return graphClassName;
    }

    public void insertReport(int reportId, String reportName, String reportDesc) {
        String query = getResourceBundle().getString("insertReport");
        String finalQuery = "";
        Object[] reportDetails = new Object[4];
        reportDetails[0] = reportId;
        reportDetails[1] = reportName;
        reportDetails[2] = reportDesc;
        reportDetails[3] = reportDesc;
        try {
            finalQuery = buildQuery(query, reportDetails);
            execModifySQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        logger.info("successful");
    }

    public ArrayList insertReportMaster(int reportId, String reportName, String reportDesc, String UserFolderIds, String mapEnabled, List<String> customSeq, HashMap<String, ArrayList<String>> transposeFormatMap, HashMap<String, HashMap<String, ArrayList<String>>> targValue, HashMap<String, ArrayList<String>> goalPercent, String userId, List<String> rowViewByValues, String groupName, List<String> ElemeIds, HashMap<String, HashMap<String, ArrayList<String>>> goalTimeIndiv, List<String> newProdlist, boolean overwrite, Container container, String Gtregion) {
        String insertReportMasterQuery = "";
        PbReportViewerBD bd = new PbReportViewerBD();
        if (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertReportMasterQuery = getResourceBundle().getString("UpdateReportMaster");
        } else if (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertReportMasterQuery = getResourceBundle().getString("UpdateReportMaster");
        } else {
            insertReportMasterQuery = getResourceBundle().getString("insertReportMaster");
        }

        String getusernameQuery = getResourceBundle().getString("getUserName");
        Object[] user = null;
        String query = "";
        PbReturnObject retobj = null;
        try {
            user = new Object[1];
            user[0] = userId;
            query = buildQuery(getusernameQuery, user);
            retobj = this.execSelectSQL(query);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
        logger.info("successful");
        String userName = "";
        if (retobj.rowCount != 0) {
            userName = retobj.getFieldValueString(0, 0);
        }
        //  
        ArrayList queries = new ArrayList();
        Object[] reportMaster = null;
        String finalQuery = "";
        Gson gson = new Gson();
        //
        reportName = reportName.replace("'", "''");
        reportDesc = reportDesc.replace("'", "''");

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                if (overwrite) {
                    reportMaster = new Object[21];
                } else {
                    reportMaster = new Object[21];
                }
                reportMaster[0] = reportName;
                reportMaster[1] = reportDesc;
                //   reportMaster[2] = reportDesc;
                reportMaster[3] = reportDesc;
                reportMaster[4] = mapEnabled;
                reportMaster[5] = gson.toJson(customSeq);
                reportMaster[6] = gson.toJson(transposeFormatMap);
                reportMaster[7] = gson.toJson(targValue);
                reportMaster[8] = gson.toJson(goalPercent);
                reportMaster[9] = userName;
                reportMaster[10] = Joiner.on(",").join(rowViewByValues);
                reportMaster[11] = groupName;
                reportMaster[12] = Joiner.on(",").join(ElemeIds);
                reportMaster[13] = gson.toJson(goalTimeIndiv);
                reportMaster[14] = gson.toJson(newProdlist);
                reportMaster[15] = container.isSplitBy();
                reportMaster[16] = container.getSplitBy();
                reportMaster[17] = container.getReportCollect().getCurrentRepVersion();

                if (overwrite) {

                    reportMaster[20] = reportId;
                    reportMaster[18] = Gtregion;
                    reportMaster[19] = container.ReportLayout;
                    if (container.isIskpidasboard()) {
                        if (container.IsTimedasboard()) {
                            reportMaster[2] = "T";
                        } else {
                            reportMaster[2] = "R";
                        }
                    } else {
                        reportMaster[2] = "R";
                    }
                } else {
                    if (container.isIskpidasboard()) {
                        reportMaster[18] = "true";
                        reportMaster[19] = container.IsTimedasboard();
                        if (container.IsTimedasboard()) {
                            reportMaster[2] = "T";
                        } else {
                            reportMaster[2] = "R";
                        }
                    } else {
                        reportMaster[18] = "false";
                        reportMaster[2] = "R";
                        reportMaster[19] = "false";
                    }
                    reportMaster[20] = container.ReportLayout;
                }
            } else {
                reportMaster = new Object[23];
                reportMaster[0] = reportId;
                reportMaster[1] = reportName;
                reportMaster[2] = reportDesc;
                reportMaster[4] = reportDesc;
                reportMaster[5] = mapEnabled;
                reportMaster[6] = gson.toJson(customSeq);
                reportMaster[7] = gson.toJson(transposeFormatMap);
                reportMaster[8] = gson.toJson(targValue);
                reportMaster[9] = gson.toJson(goalPercent);
                reportMaster[10] = userName;
                reportMaster[11] = Joiner.on(",").join(rowViewByValues);
                reportMaster[12] = groupName;
                reportMaster[13] = Joiner.on(",").join(ElemeIds);
                reportMaster[14] = gson.toJson(goalTimeIndiv);
                reportMaster[15] = gson.toJson(newProdlist);
                reportMaster[16] = container.isSplitBy();
                reportMaster[17] = container.getSplitBy();
                reportMaster[18] = container.getReportCollect().getCurrentRepVersion();
                reportMaster[19] = Gtregion;
                if (container.isIskpidasboard()) {
                    reportMaster[20] = "true";
                    if (container.IsTimedasboard()) {
                        reportMaster[3] = "T";
                    } else {
                        reportMaster[3] = "R";
                    }
                    reportMaster[21] = container.IsTimedasboard();
                } else {
                    reportMaster[20] = "false";
                    reportMaster[3] = "R";
                    reportMaster[21] = container.IsTimedasboard();
                }
                reportMaster[22] = container.ReportLayout;
            }

            finalQuery = buildQuery(insertReportMasterQuery, reportMaster);

            queries.add(finalQuery);//inserting into report master
            queries = insertReportDetails(reportId, container.getAOId(), UserFolderIds, queries, overwrite);//inserting into report details

            //boolean status = executeMultiple(queries);
            return queries;

        } catch (Exception e) {
            logger.error("Exception: ", e);

            return queries;
        }

    }

    public ArrayList insertReportDetails(int reportId, String aoId, String UserFolderIds, ArrayList queries, boolean overwrite) throws Exception {
        String insertReportDetailsQuery = "";
        if (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            insertReportDetailsQuery = getResourceBundle().getString("updateReportDetails");
        } else if (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            insertReportDetailsQuery = getResourceBundle().getString("updateReportDetails");
        } else {
            insertReportDetailsQuery = getResourceBundle().getString("insertReportDetails");
        }
        Object[] reportFolders = null;
        Object[] reportDetails = null;
        String finalQuery = "";
        String REP_CRETAED_ON = null;
        if ((aoId != null && !aoId.equalsIgnoreCase(""))) {
            REP_CRETAED_ON = "AO";
        }
        if (UserFolderIds != null && !UserFolderIds.isEmpty() && !UserFolderIds.trim().equalsIgnoreCase("null")) {
            reportFolders = UserFolderIds.split(",");

            for (int i = 0; i < reportFolders.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    if (!overwrite) {
                        reportDetails = new Object[3];
                        reportDetails[0] = reportFolders[i];
                        reportDetails[1] = REP_CRETAED_ON;
                        reportDetails[2] = aoId;
//                     reportDetails[2] = null;
                    } else {
                        reportDetails = new Object[4];
                        reportDetails[0] = reportFolders[i];
                        reportDetails[1] = REP_CRETAED_ON;
                        reportDetails[2] = aoId;
                        reportDetails[3] = reportId;
                    }
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    if (!overwrite) {
                        reportDetails = new Object[4];
                        String qry = "select LAST_INSERT_ID(REPORT_ID) from PRG_AR_REPORT_MASTER order by 1 desc limit 1";
                        PbReturnObject retobj = new PbReturnObject();
                        retobj = super.execSelectSQL(qry);
                        reportDetails[0] = String.valueOf(Integer.parseInt(retobj.getFieldValueString(0, 0)) + 1);
                        reportDetails[1] = reportFolders[i];
                        reportDetails[2] = REP_CRETAED_ON;
                        reportDetails[3] = aoId;
                    } else {
                        reportDetails = new Object[4];
                        reportDetails[0] = reportFolders[i];
                        reportDetails[1] = REP_CRETAED_ON;
                        reportDetails[2] = aoId;
                        reportDetails[3] = reportId;
                    }

                } else {
                    reportDetails = new Object[4];
                    reportDetails[0] = reportId;
                    reportDetails[1] = reportFolders[i];
                    reportDetails[2] = REP_CRETAED_ON;
                    reportDetails[3] = aoId;
                }

                finalQuery = buildQuery(insertReportDetailsQuery, reportDetails);//inserting into report details

                queries.add(finalQuery);
            }
        }
        return queries;
    }

    public ArrayList insertReportParamDetails(int reportId, String paramsString, String paramString1, ArrayList queries, Container container, boolean overwrite) {

        //String insertReportParamDetailsQuery = resBundle.getString("insertReportParamDetails");
        String getReportParamDetailsQuery = getResourceBundle().getString("getReportParamDetails");
        PbReportCollection collect = container.getReportCollect();
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        String[] paramIds = paramsString.split(",");
//       String datetimeoption=null;
//        String dataSubstrcol=null;
//        String dateFormat=null;
//        for(int i=0;i<paramIds.length;i++){
//         datetimeoption=container.getDateandTimeOptions("A_"+paramIds[i].replace("elmnt-", "").trim());
//        dataSubstrcol=container.getDateSubStringValues("A_"+paramIds[i].replace("elmnt-", "").trim());
//        dateFormat=container.getDateFormatt("A_"+paramIds[i].replace("elmnt-", "").trim());
//        }
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();
        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();

        } else {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = paramsString.toString().replaceAll("elmnt-", "").trim();
            Obj[2] = paramString1.toString().replaceAll("elmnt-", "").trim();
        }
        String addParameterSecurity = "";

        try {
            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);
//            //////.println("finalquery getreportparamdetails are : " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();
            //modified by bharathi reddy default value clob insertion
            Connection connection = null;
            PreparedStatement opstmt = null;
            ArrayList<String> repqueries = new ArrayList<String>();
            Gson gson = new Gson();
            Type targetType = new TypeToken<List<String>>() {
            }.getType();
            if ((overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) || (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL))) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    Object[] object = new Object[16];
                    addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,NOT_IN,LIKE_V,NOT_LIKE,GLOBAL_KPIPARAM) values('&','&','&','&','&','&','&'," + (i + 1) + ",'&','&','&','&','&','&','&','&')";
                    object[0] = reportId;
                    object[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    
                    if (retObj.getFieldValueString(i, dbColumns[2]) != null && !retObj.getFieldValueString(i, dbColumns[2]).isEmpty() ){
                    object[2] = retObj.getFieldValueString(i, dbColumns[2]);
                    }else{
                         object[2] = "0";}
                    
                    if (retObj.getFieldValueString(i, dbColumns[3]) != null && !retObj.getFieldValueString(i, dbColumns[3]).isEmpty() ){
                    object[3] =retObj.getFieldValueString(i, dbColumns[3]);
                    }else{object[3] = "0";}
                    
                     if (retObj.getFieldValueString(i, dbColumns[4]) != null && !retObj.getFieldValueString(i, dbColumns[4]).isEmpty() ){
                    object[4] = retObj.getFieldValueString(i, dbColumns[4]);
                       }else{object[4] = "0";}
                     
                     if (retObj.getFieldValueString(i, dbColumns[5]) != null && !retObj.getFieldValueString(i, dbColumns[5]).isEmpty() ){
                    object[5] = retObj.getFieldValueString(i, dbColumns[5]);
                    }else{object[5] = "0";}
                     
                    List<String> defultVlueList = collect.getInValList(paramIds[i]);

                    //Added by Ram For Removing Inintialize Default Filter.
                    HashMap defaultFilterMap = container.getInitilizeFilterElement();
                    if (defaultFilterMap.containsKey(retObj.getFieldValueString(i, dbColumns[2]))) {
                        ArrayList<String> viewByElementIds = new ArrayList();
                        for (Object keyVal : defaultFilterMap.keySet()) {
                            viewByElementIds.add(keyVal.toString());
                        }
                        ArrayList filterList = (ArrayList) defaultFilterMap.get(viewByElementIds.get(0));
                        String defaultFilterVal = filterList.get(0).toString();

                        if (defultVlueList.contains(defaultFilterVal)) {
                            defultVlueList.remove(defaultFilterVal);

                        }
                    }
                    //End Ram Code

                    String defaultVal = gson.toJson(defultVlueList, targetType);
                    if (defultVlueList != null && !defultVlueList.isEmpty()) {
                        object[6] = defaultVal;
                    } else {
                        object[6] = "[\"All\"]";
                    }
                    object[7] = collect.getParameterStatus(paramIds[i]);

                    if (container.getDateandTimeOptions("A_" + paramIds[i]) != null && !container.getDateandTimeOptions("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateandTimeOptions("A_" + paramIds[i]))) {
                        object[8] = container.getDateandTimeOptions("A_" + paramIds[i]);
                    } else {
                        object[8] = null;
                    }

                    if (container.getDateSubStringValues("A_" + paramIds[i]) != null && !container.getDateSubStringValues("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateSubStringValues("A_" + paramIds[i]))) {
                        object[9] = container.getDateSubStringValues("A_" + paramIds[i]);
                    } else {
                        object[9] = null;
                    }

                    if (container.getDateFormatt("A_" + paramIds[i]) != null && !container.getDateFormatt("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateFormatt("A_" + paramIds[i]))) {
                        object[10] = container.getDateFormatt("A_" + paramIds[i]);
                    } else {
                        object[10] = null;
                    }

//                     if(datetimeoption!=null && !datetimeoption.equalsIgnoreCase("null") && !"".equals(datetimeoption)){
//                    object[8]=datetimeoption;
//                    }else{
//                        object[8]=null;
//                    }
//                    if(dataSubstrcol!=null && !dataSubstrcol.equalsIgnoreCase("null") && !"".equals(dataSubstrcol)){
//                    object[9]=dataSubstrcol;
//                    }else{
//                        object[9]=null;
//                    }
//                    if(dateFormat!=null && !dateFormat.equalsIgnoreCase("null") && !"".equals(dateFormat)){
//                    object[10]=dateFormat;
//                    }else{
//                        object[10]=null;
//                    }
                    List<String> notInValueList = collect.getNotInValList(paramIds[i]) != null ? collect.getNotInValList(paramIds[i]) : new ArrayList<String>();
                    List<String> likeValueList = collect.getLikeValList(paramIds[i]) != null ? collect.getLikeValList(paramIds[i]) : new ArrayList<String>();
                    List<String> notLikeValueList = collect.getNotLikeValList(paramIds[i]) != null ? collect.getNotLikeValList(paramIds[i]) : new ArrayList<String>();
                    String notInVlue = gson.toJson(notInValueList, targetType);
                    String likeVal = gson.toJson(likeValueList, targetType);
                    String notLikeVal = gson.toJson(notLikeValueList, targetType);
                    object[11] = notInVlue;
                    object[12] = likeVal;
                    object[13] = notLikeVal;
                    //sandeep for global paramter in kpi dashboard on 17/11/2014
//                    Set<String> repViewByIds = collect.kpireportViewByMain.keySet();
                    String rowviewbys;
//
//            for( String viewById : repViewByIds )
                    if (collect.reportIncomingParameters.get("CBOVIEW_BY_1") != null) {

                        rowviewbys = (String) collect.reportIncomingParameters.get("CBOVIEW_BY_1");
//                   requestParamValues.put("CBOVIEW_BY" + viewById,rowviewbys);
                        if (rowviewbys == null ? retObj.getFieldValueString(i, dbColumns[2]) == null : rowviewbys.equals(retObj.getFieldValueString(i, dbColumns[2]))) {
                            object[14] = "true";
                        } else {
                            object[14] = "false";
                        }

                    } else {
                        object[14] = "false";
                    }

                    //end of sandeep code
                    String finalquery = "";
                    finalquery = addParameterSecurity;

                    finalquery = buildQuery(finalquery, object);
                    repqueries.add(finalquery);
                }
                String deleteReportParamDetailsQuery = "delete from PRG_AR_REPORT_PARAM_DETAILS where report_id='" + reportId + "'";
                execModifySQL(deleteReportParamDetailsQuery);
                this.saveReport(repqueries);
            } else {
                connection = ProgenConnection.getInstance().getConnection();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,NOT_IN,LIKE_V,NOT_LIKE,GLOBAL_KPIPARAM) values(?,?,?,?,?,?,?," + (i + 1) + ",?,?,?,?,?,?,?,?)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,NOT_IN,LIKE_V,NOT_LIKE,GLOBAL_KPIPARAM) values(?,?,?,?,?,?,?," + (i + 1) + ",?,?,?,?,?,?,?,?)";
                    } else {
                        addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (PARAM_DISP_ID,REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,NOT_IN,LIKE_V,NOT_LIKE,GLOBAL_KPIPARAM) values(PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval,?,?,?,?,?,?,?," + (i + 1) + ",?,?,?,?,?,?,?,?)";
                    }
                    opstmt = connection.prepareStatement(addParameterSecurity);
                    //(OraclePreparedStatement) connection.prepareStatement(addParameterSecurity);
//                //////.println("retObj.getFieldValueString(0,0) is : " + retObj.getFieldValueString(0, 0));
//                //////.println("retObj.getFieldValueInt(0,0) is : " + retObj.getFieldValueInt(0, 0));
//                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                    opstmt.setInt(1, retObj.getFieldValueInt(0, 0));
//                } else {
//                    opstmt.setString(1, retObj.getFieldValueString(0, 0));
//                }
                    opstmt.setInt(1, retObj.getFieldValueInt(0, 0));
                    opstmt.setString(2, retObj.getFieldValueString(i, dbColumns[1]));
                    if (retObj.getFieldValueString(i, dbColumns[2]) != null && !retObj.getFieldValueString(i, dbColumns[2]).isEmpty() ){
                    opstmt.setString(3, retObj.getFieldValueString(i, dbColumns[2]));
                    }else{
                        opstmt.setString(3,"0");
                    }
                     if (retObj.getFieldValueString(i, dbColumns[3]) != null && !retObj.getFieldValueString(i, dbColumns[3]).isEmpty() ){
                    opstmt.setString(4, retObj.getFieldValueString(i, dbColumns[3]));
                     }else{
                        opstmt.setString(4,"0");
                    }
                     if (retObj.getFieldValueString(i, dbColumns[4]) != null && !retObj.getFieldValueString(i, dbColumns[4]).isEmpty() ){ 
                    opstmt.setString(5, retObj.getFieldValueString(i, dbColumns[4]));
                     }else{
                        opstmt.setString(5,"0");
                    }
                     if (retObj.getFieldValueString(i, dbColumns[5]) != null && !retObj.getFieldValueString(i, dbColumns[5]).isEmpty() ){ 
                    opstmt.setString(6, retObj.getFieldValueString(i, dbColumns[5]));
                     }else{
                        opstmt.setString(6,"0");
                    }

//                opstmt.setString(7, datetimeoption);
//                opstmt.setString(8, dataSubstrcol);
                    //                if (paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2])) != null) {
//                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                        opstmt.setString(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
////                        String paramClob = (String) paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]));
////                        ((PreparedStatementProxy) opstmt).setCharacterStream(7, new StringReader(paramClob), paramClob.length()); //.setStringForClob(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
//                    } else {
//                        ((OraclePreparedStatement) opstmt).setStringForClob(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
//                    }
//                }
                    //  List<String> defultVlueList=collect.getInValList(paramIds[i]);
                    List<String> defultVlueList = collect.getInValList(paramIds[i]);
                    if (defultVlueList != null && !defultVlueList.isEmpty()) {

                        String defaultVal = gson.toJson(defultVlueList, targetType);
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            opstmt.setString(7, defaultVal);
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            opstmt.setString(7, defaultVal);
                        } else {
                            ((OraclePreparedStatement) opstmt).setStringForClob(7, defaultVal);
                        }

                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        opstmt.setString(7, "[\"All\"]");
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        opstmt.setString(7, "[\"All\"]");
                    } else {
                        ((OraclePreparedStatement) opstmt).setStringForClob(7, "[\"All\"]");
                    }

                    opstmt.setString(8, collect.getParameterStatus(paramIds[i]));

                    if (container.getDateandTimeOptions("A_" + paramIds[i]) != null && !container.getDateandTimeOptions("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateandTimeOptions("A_" + paramIds[i]))) {
                        opstmt.setString(9, container.getDateandTimeOptions("A_" + paramIds[i]));
                    } else {
                        opstmt.setString(9, null);
                    }

                    if (container.getDateSubStringValues("A_" + paramIds[i]) != null && !container.getDateSubStringValues("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateSubStringValues("A_" + paramIds[i]))) {
                        opstmt.setString(10, container.getDateSubStringValues("A_" + paramIds[i]));
                    } else {
                        opstmt.setString(10, null);
                    }

                    if (container.getDateFormatt("A_" + paramIds[i]) != null && !container.getDateFormatt("A_" + paramIds[i]).equalsIgnoreCase("null") && !"".equals(container.getDateFormatt("A_" + paramIds[i]))) {
                        opstmt.setString(11, container.getDateFormatt("A_" + paramIds[i]));
                    } else {
                        opstmt.setString(11, null);
                    }

//                 if(datetimeoption!=null && !datetimeoption.equalsIgnoreCase("null") && !"".equals(datetimeoption)){
//                    opstmt.setString(9, datetimeoption);
//                    }else{
//                        opstmt.setString(9,null);
//                    }
//                    if(dataSubstrcol!=null && !dataSubstrcol.equalsIgnoreCase("null") && !"".equals(dataSubstrcol)){
//                    opstmt.setString(10, dataSubstrcol);
//                    }else{
//                        opstmt.setString(10,null);
//                    }
//                     if(dateFormat!=null && !dateFormat.equalsIgnoreCase("null") && !"".equals(dateFormat)){
//                    opstmt.setString(11, dateFormat);
//                    }else{
//                        opstmt.setString(11,null);
//                    }
                    List<String> notInValueList = collect.getNotInValList(paramIds[i]) != null ? collect.getNotInValList(paramIds[i]) : new ArrayList<String>();
                    List<String> likeValueList = collect.getLikeValList(paramIds[i]) != null ? collect.getLikeValList(paramIds[i]) : new ArrayList<String>();
                    List<String> notLikeValueList = collect.getNotLikeValList(paramIds[i]) != null ? collect.getNotLikeValList(paramIds[i]) : new ArrayList<String>();
                    String notInVlue = gson.toJson(notInValueList, targetType);
                    String likeVal = gson.toJson(likeValueList, targetType);
                    String notLikeVal = gson.toJson(notLikeValueList, targetType);
                    opstmt.setString(12, notInVlue);
                    opstmt.setString(13, likeVal);
                    opstmt.setString(14, notLikeVal);
                    String rowviewbys;
//
//            for( String viewById : repViewByIds )
                    if (collect.reportIncomingParameters.get("CBOVIEW_BY_1") != null) {

                        rowviewbys = (String) collect.reportIncomingParameters.get("CBOVIEW_BY_1");
//                   requestParamValues.put("CBOVIEW_BY" + viewById,rowviewbys);
                        if (rowviewbys == null ? retObj.getFieldValueString(i, dbColumns[2]) == null : rowviewbys.equals(retObj.getFieldValueString(i, dbColumns[2]))) {
                            opstmt.setString(15, "true");
                        } else {
                            opstmt.setString(15, "false");
                        }

                    } else {
                        opstmt.setString(15, "false");
                    }
                    int rows = opstmt.executeUpdate();
                }
                if (connection != null) {
                    opstmt.close();
                    connection.close();
                }
            }

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        logger.info("successful");
        return queries;
    }

    public ArrayList insertReportTimeDimensions(ArrayList timeDetails, HashMap timeDimHashMap, int reportId, ArrayList queries, ArrayList timeParams, String date, LinkedHashMap currentTimeDetails, boolean overwrite) {
        String sqlQuery = "";
        if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            sqlQuery = getResourceBundle().getString("UpdateReportTimeDimensions");
        } else if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            sqlQuery = getResourceBundle().getString("UpdateReportTimeDimensions");
        } else {
            sqlQuery = getResourceBundle().getString("insertReportTimeDimensions");
        }
        String finalQuery = "";
        Object[] obj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            if (overwrite) {
                obj = new Object[3];
                obj[0] = timeDetails.get(1);
                obj[1] = timeDetails.get(0);
                obj[2] = reportId;
            } else {
                obj = new Object[2];
                obj[0] = timeDetails.get(1);
                obj[1] = timeDetails.get(0);
            }
        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            obj = new Object[3];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
            obj[2] = reportId;
        } else {
            obj = new Object[3];
            obj[0] = timeDetails.get(1);
            obj[1] = timeDetails.get(0);
            obj[2] = reportId;
        }

        finalQuery = buildQuery(sqlQuery, obj);
        if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            try {
                execUpdateSQL(finalQuery);
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        } else {
            queries.add(finalQuery);
        }
        if (timeDetails.get(1).equals("PRG_COHORT")) {
            queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, null, timeDetails.get(1).toString(), date, currentTimeDetails, overwrite, reportId);
        } else {
            queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams, timeDetails.get(3).toString(), timeDetails.get(1).toString(), date, currentTimeDetails, overwrite, reportId);
        }
        return queries;
    }

    public ArrayList insertReportTimeDimensionsDetails(HashMap timeDimHashMap, ArrayList queries, ArrayList timeParamsS, String duration, String timeType, String date, LinkedHashMap currentTimeDetails, boolean overwrite, int reportId) {

        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensionsDetails");
        String finalQuery = "";
        String column_type = "";
        String date1 = date;
        String date11 = date;
        String fromDate = null;
        String[] dateArray = null;
        String[] dateArray1 = null;
        String fromDatesign = null;
        String toDatesign = null;
        String finalTime = null;
        Object[] obj;
        if (date1 != null && date1.contains("@")) {
            dateArray = date1.split("@");
            // by pass
//        if(dateArray1[0].contains("fromSysDate")){
//        for(int j=0;j<dateArray1.length;j++){
//        if(dateArray1[j].contains("fromSysDate") ||dateArray1[j].contains("fromglobalDate")){
//             if(dateArray1[j].contains("-"))
//                      fromDatesign="-";
//                   else
//                       fromDatesign="+";
//        fromDate=dateArray1[j].replace(",", "").replace("fromSysDate","").replace("fromglobalDate","").replace("+", "").replace("-", "").trim();
//       // dateArray[j]=fromDatesign+fromDate;
//        finalTime="fromSysDate"+","+fromDatesign+","+fromDate;
//
//        }
//    else{
//               int fromDate1 = Integer.parseInt(fromDate);
//               if(dateArray1[j].contains("-"))
//                      toDatesign="-";
//                   else
//                       toDatesign="+";
//               int toDate = Integer.parseInt(dateArray1[j].replace(",", "").replace("fromSysDate","").replace("toSystDate","").replace("CmpFrmSysDate","").replace("cmptoSysDate","").replace("toglobalDdate","").replace("CmpFrmglobalDate","").replace("cmptoglobalDate","").replace("+", "").replace("-", "").trim());
//              if(fromDatesign.equalsIgnoreCase("-")){
//                   fromDate1=-fromDate1;}
//               else{
//                   fromDate1=fromDate1;
//                }
//                  if(toDatesign.equalsIgnoreCase("-"))
//                   {
//                   toDate=-toDate;}
//               else{
//                   toDate=toDate;
//                }
//               int toDate1 = toDate-(fromDate1);
//               String signof=null;
//               String tdate=null;
//               if(dateArray1[j].contains("toSystDate")){
//                   tdate="toSystDate";}
//               else if(dateArray1[j].contains("CmpFrmSysDate")){
//                   tdate="CmpFrmSysDate";}
//               else if(dateArray1[j].contains("cmptoSysDate")){
//                   tdate="cmptoSysDate";}
//               else if(dateArray1[j].contains("toglobalDdate")){
//                   tdate="toglobalDdate";}
//               else if(dateArray1[j].contains("CmpFrmglobalDate")){
//                   tdate="CmpFrmglobalDate";}
//               else {
//                   tdate="cmptoglobalDate";}
//               if(dateArray1[j].contains("+"))
//                   signof="+";
//               else
//                   signof="-";
//               String val=null;
//               if(toDate1<0){
//                   signof="-";
//                   val=String.valueOf(toDate1);
//                   val.replace("-", "");
//               }
//               else{
//                   signof="+";
//                   val=String.valueOf(toDate1);
//                }
//               finalTime=finalTime+"@"+tdate+","+signof+","+val.replace("-", "");
//               dateArray = finalTime.split("@");
//        }
//        }
//            }else{
            dateArray = date1.split("@");
            // }
        }
        String currDetails = null;
        if (date11 != null && date11.contains("currdetails")) {
            currDetails = "currdetails";
        }
        int i = 0;

        Set details = timeDimHashMap.keySet();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            if (dateArray != null && i < dateArray.length) {
                date11 = dateArray[i];
            }
            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                obj = new Object[7];
            } else {
                obj = new Object[6];
            }
            String key = (String) it.next();
            ArrayList timeDetails = (ArrayList) timeDimHashMap.get(key);
            String column_name = (String) timeDetails.get(2);
            column_type = key;

            /*
             * for (int time = 0; time < timeParams.size(); time++) { if
             * (timeParams.get(time).toString().equalsIgnoreCase(key)) {
             * column_type = key;//String.valueOf(timeParams.get(time)); } }
             */
            int sequence = Integer.parseInt((String) timeDetails.get(3));
            int from_sequence = Integer.parseInt((String) timeDetails.get(4));
            // date = (Date) repDateFormat.parse(timeDetails.get(0).toString());
            String dateinmysql = "";
            String[] dateformysql = ((String) timeDetails.get(0)).split("/");
            if (key.equalsIgnoreCase("AS_OF_DATE") || key.equalsIgnoreCase("AS_OF_DATE1") || key.equalsIgnoreCase("AS_OF_DATE2") || key.equalsIgnoreCase("CMP_AS_OF_DATE1") || key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                dateinmysql = dateformysql[2].trim() + "/" + dateformysql[0].trim() + "/" + dateformysql[1].trim();
            }
            if (key.equalsIgnoreCase("AS_OF_DATE")) {
                if (date != null && date11 != null) {
                    if (timeDetails.get(0) != null && date11 != null && date11.equals("reportDate")) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }

                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("yestrday")) {
//                    date = "YesterDay";
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.equals("tomorow")) {
                        date = date11;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("newSysDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } //added by mohit for fix date
                    else if (timeDetails.get(0) != null && date11 != null && date11.contains("fixeddate")) {
                        String newdate = date11.substring(date11.indexOf(",") + 1);
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            String[] formysql = newdate.split("/");
                            date = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) newdate) + "','dd/mm/yyyy')";
                        }
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("globalDate")) {
                        if (date11.contains(" ")) {
                            date = date11.replace(" ", "+");
                        } else {
                            date = date11;
                        }
                        date = date;
                    } else if (timeDetails.get(0) != null && date11 != null && date11.contains("currdetails")) {
                        String[] currVal = currentTimeDetails.get(key).toString().split("#");
                        if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                            date11 = currVal[0];
                            date = "null";
                        } else if (timeDetails.get(0) != null) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                            } else {
                                date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                            }
                        }
                    } else {
                        date = "null";
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("fromyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("fromtomorow")) {
                    date = date11;
                }//added by mohit for fix date
                else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromfixeddate")) {
                    String newdate = date11.substring(date11.indexOf(",") + 1);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        String[] formysql = newdate.split("/");
                        date = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) newdate) + "','dd/mm/yyyy')";
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("fromglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.equals("toyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.equals("totomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toSystDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("toglobalDdate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("tofixeddate")) {
                    String newdate = date11.substring(date11.indexOf(",") + 1);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        String[] formysql = newdate.split("/");
                        date = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) newdate) + "','dd/mm/yyyy')";
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmtomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("CmpFrmfixeddate")) {
                    String newdate = date11.substring(date11.indexOf(",") + 1);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        String[] formysql = newdate.split("/");
                        date = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) newdate) + "','dd/mm/yyyy')";
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoyestrday")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptotomorow")) {
                    date = date11;
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoSysDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("cmptoglobalDate")) {
                    if (date11.contains(" ")) {
                        date = date11.replace(" ", "+");
                    } else {
                        date = date11;
                    }
                } else if (timeDetails.get(0) != null && date11 != null && date11.contains("Cmptofixeddate")) {
                    String newdate = date11.substring(date11.indexOf(",") + 1);
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        String[] formysql = newdate.split("/");
                        date = "date_format('" + formysql[2].trim() + "/" + formysql[1].trim() + "/" + formysql[0].trim() + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) newdate) + "','dd/mm/yyyy')";
                    }
                } else if (timeDetails.get(0) != null && currDetails != null && currDetails.contains("currdetails")) {
                    String[] currVal = currentTimeDetails.get(key).toString().split("#");
                    if (currVal != null && currVal[1].equalsIgnoreCase("null")) {
                        date11 = currVal[0];
                        date = "null";
                    } else if (timeDetails.get(0) != null) {
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                        } else {
                            date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                        }
                    }
                } else if (timeDetails.get(0) != null) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        date = "convert(datetime,'" + ((String) timeDetails.get(0)) + "',101)";
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        date = "date_format('" + dateinmysql + "','%Y-%m-%d')";
                    } else {
                        date = "to_date('" + ((String) timeDetails.get(0)) + "','mm/dd/yyyy')";
                    }

                } else {
                    date = "null";
                }
            } else {
                date = "null";
            }
            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                String qry = " ";
                if (overwrite) {
                    qry = "select LAST_INSERT_ID(REP_TIME_ID) from PRG_AR_REPORT_TIME order by 1 desc limit 1";
                } else {
                    qry = "select LAST_INSERT_ID(REP_TIME_ID) from PRG_AR_REPORT_TIME order by 1 desc limit 1 ";
                }
                PbReturnObject retobj = new PbReturnObject();
                try {
                    retobj = super.execSelectSQL(qry);
                } catch (SQLException ex) {
                    logger.error("Exception", ex);
                }catch (Exception ex) {
                    logger.error("Exception", ex);
                }
                if (overwrite) {
                    obj[0] = Integer.parseInt(retobj.getFieldValueString(0, 0));
                } else {
                    obj[0] = Integer.parseInt(retobj.getFieldValueString(0, 0)) + 1;
                }

                obj[1] = column_name;
                obj[2] = column_type;
                obj[3] = sequence;
                obj[4] = from_sequence;
                obj[5] = date;
                if (timeType.equalsIgnoreCase("PRG_COHORT")) {
                    if (column_type.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                        obj[6] = "Month";
                    } else {
                        obj[6] = null;
                    }
                } else //added by mohit for new periods
                if (column_type.equalsIgnoreCase("PRG_COMPARE")) {
                    String comparewith = timeDetails.get(0).toString();
                    if (comparewith.equalsIgnoreCase("Last Day") || comparewith.equalsIgnoreCase("Last Week") || comparewith.equalsIgnoreCase("Last Month") || comparewith.equalsIgnoreCase("Last Period") || comparewith.equalsIgnoreCase("Last Year") || comparewith.equalsIgnoreCase("Period Complete") || comparewith.equalsIgnoreCase("Year Complete")
                            || comparewith.equalsIgnoreCase("Previous Day") || comparewith.equalsIgnoreCase("Same Day Last Week") || comparewith.equalsIgnoreCase("Same Day Last Month") || comparewith.equalsIgnoreCase("Same Day Last Year")
                            || comparewith.equalsIgnoreCase("Same Week Last Year") || comparewith.equalsIgnoreCase("Complete Last Week") || comparewith.equalsIgnoreCase("Complete Same Week Last Year") || comparewith.equalsIgnoreCase("Same Month Last Year")
                            || comparewith.equalsIgnoreCase("Complete Last Month") || comparewith.equalsIgnoreCase("Complete Same Month Last Year") || comparewith.equalsIgnoreCase("Last Qtr") || comparewith.equalsIgnoreCase("Same Qtr Last Year")
                            || comparewith.equalsIgnoreCase("Complete Last Qtr") || comparewith.equalsIgnoreCase("Complete Same Qtr Last Year") || comparewith.equalsIgnoreCase("Complete Last Year")) {
                        obj[6] = timeDetails.get(0);
                    } else {
                        obj[6] = duration;
                    }
                } else {
                    obj[6] = duration;
                }
                if (key.equalsIgnoreCase("AS_OF_DATE")) {
                    if (date11 != null && date11.equalsIgnoreCase("yestrday") || date11 != null && date11.equalsIgnoreCase("Yesterday")) {
                        obj[5] = "null";
                        obj[6] = "Yesterday";
                    } else if (date11 != null && date11.equalsIgnoreCase("tomorow") || date11 != null && date11.equalsIgnoreCase("Tomorrow")) {
                        obj[5] = "null";
                        obj[6] = "Tomorrow";
                    } else if (date11 != null && (date11.contains("newSysDate") || date11.contains("globalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[5] = "null";
                        obj[6] = date11;
                    } else if (date11 != null && date11.contains("newSysDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    } else if (date11 != null && date11.contains("fixeddate")) {
                        obj[5] = date;
                        obj[6] = "fixeddate";
                    } else if (date11 != null && date11.contains("globalDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    }
                } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                    if (date11 != null && date11.equalsIgnoreCase("fromyestrday")) {
                        obj[5] = "null";
                        obj[6] = "fromyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("fromtomorow")) {
                        obj[5] = "null";
                        obj[6] = "fromtomorow";
                    } else if (date11 != null && (date11.contains("fromSysDate") || date11.contains("fromglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[5] = "null";
                        obj[6] = date11;
                    } else if (date11 != null && date11.contains("fromSysDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    } else if (date11 != null && date11.contains("fromfixeddate")) {
                        obj[5] = date;
                        obj[6] = "fromfixeddate";
                    } else if (date11 != null && date11.contains("fromglobalDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    }
                } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                    if (date11 != null && date11.equalsIgnoreCase("toyestrday")) {
                        obj[5] = "null";
                        obj[6] = "toyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("totomorow")) {
                        obj[5] = "null";
                        obj[6] = "totomorow";
                    } else if (date11 != null && (date11.contains("toSystDate") || date11.contains("toglobalDdate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[5] = "null";
                        obj[6] = date11;
                    } else if (date11 != null && date11.contains("toSystDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    } else if (date11 != null && date11.contains("tofixeddate")) {
                        obj[5] = date;
                        obj[6] = "tofixeddate";
                    } else if (date11 != null && date11.contains("toglobalDdate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    }
                } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    if (date11 != null && date11.equalsIgnoreCase("CmpFrmyestrday")) {
                        obj[5] = "null";
                        obj[6] = "CmpFrmyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("CmpFrmtomorow")) {
                        obj[5] = "null";
                        obj[6] = "CmpFrmtomorow";
                    } else if (date11 != null && (date11.contains("CmpFrmSysDate") || date11.contains("CmpFrmglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[5] = "null";
                        obj[6] = date11;
                    } else if (date11 != null && date11.contains("CmpFrmSysDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    } else if (date11 != null && date11.contains("CmpFrmfixeddate")) {
                        obj[5] = date;
                        obj[6] = "CmpFrmfixeddate";
                    } else if (date11 != null && date11.contains("CmpFrmglobalDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    }
                } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    if (date11 != null && date11.equalsIgnoreCase("cmptoyestrday")) {
                        obj[5] = "null";
                        obj[6] = "cmptoyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("cmptotomorow")) {
                        obj[5] = "null";
                        obj[6] = "cmptotomorow";
                    } else if (date11 != null && (date11.contains("cmptoSysDate") || date11.contains("cmptoglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[5] = "null";
                        obj[6] = date11;
                    } else if (date11 != null && date11.contains("cmptoSysDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    } else if (date11 != null && date11.contains("Cmptofixeddate")) {
                        obj[5] = date;
                        obj[6] = "Cmptofixeddate";
                    } else if (date11 != null && date11.contains("cmptoglobalDate")) {
                        obj[5] = "null";
                        obj[6] = date;
                    }
                }
            } else {

                obj[0] = column_name;
                obj[1] = column_type;
                obj[2] = sequence;
                obj[3] = from_sequence;
                obj[4] = date;
                if (timeType.equalsIgnoreCase("PRG_COHORT")) {
                    if (column_type.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                        obj[5] = "Month";
                    } else {
                        obj[5] = null;
                    }
                } else//added by mohit for new periods
                if (column_type.equalsIgnoreCase("PRG_COMPARE")) {
                    String comparewith = timeDetails.get(0).toString();
                    if (comparewith.equalsIgnoreCase("Last Day") || comparewith.equalsIgnoreCase("Last Week") || comparewith.equalsIgnoreCase("Last Month") || comparewith.equalsIgnoreCase("Last Period") || comparewith.equalsIgnoreCase("Last Year") || comparewith.equalsIgnoreCase("Period Complete") || comparewith.equalsIgnoreCase("Year Complete")
                            || comparewith.equalsIgnoreCase("Previous Day") || comparewith.equalsIgnoreCase("Same Day Last Week") || comparewith.equalsIgnoreCase("Same Day Last Month") || comparewith.equalsIgnoreCase("Same Day Last Year")
                            || comparewith.equalsIgnoreCase("Same Week Last Year") || comparewith.equalsIgnoreCase("Complete Last Week") || comparewith.equalsIgnoreCase("Complete Same Week Last Year") || comparewith.equalsIgnoreCase("Same Month Last Year")
                            || comparewith.equalsIgnoreCase("Complete Last Month") || comparewith.equalsIgnoreCase("Complete Same Month Last Year") || comparewith.equalsIgnoreCase("Last Qtr") || comparewith.equalsIgnoreCase("Same Qtr Last Year")
                            || comparewith.equalsIgnoreCase("Complete Last Qtr") || comparewith.equalsIgnoreCase("Complete Same Qtr Last Year") || comparewith.equalsIgnoreCase("Complete Last Year")) {
                        obj[5] = timeDetails.get(0);
                    } else {
                        obj[5] = duration;
                    }
                } else {
                    obj[5] = duration;
                }
                if (key.equalsIgnoreCase("AS_OF_DATE")) {
                    if (date11 != null && date11.equalsIgnoreCase("yestrday") || date11 != null && date11.equalsIgnoreCase("Yesterday")) {
                        obj[4] = "null";
                        obj[5] = "Yesterday";
                    } else if (date11 != null && date11.equalsIgnoreCase("tomorow") || date11 != null && date11.equalsIgnoreCase("Tomorrow")) {
                        obj[4] = "null";
                        obj[5] = "Tomorrow";
                    } else if (date11 != null && (date11.contains("newSysDate") || date11.contains("globalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[4] = "null";
                        obj[5] = date11;
                    } else if (date11 != null && date11.contains("fixeddate")) {
                        obj[4] = date;
                        obj[5] = "fixeddate";
                    } else if (date11 != null && date11.contains("newSysDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    } else if (date11 != null && date11.contains("globalDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    }
                } else if (key.equalsIgnoreCase("AS_OF_DATE1")) {
                    if (date11 != null && date11.equalsIgnoreCase("fromyestrday")) {
                        obj[4] = "null";
                        obj[5] = "fromyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("fromtomorow")) {
                        obj[4] = "null";
                        obj[5] = "fromtomorow";
                    } else if (date11 != null && (date11.contains("fromSysDate") || date11.contains("fromglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[4] = "null";
                        obj[5] = date11;
                    } else if (date11 != null && date11.contains("fromfixeddate")) {
                        obj[4] = date;
                        obj[5] = "fromfixeddate";
                    } else if (date11 != null && date11.contains("fromSysDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    } else if (date11 != null && date11.contains("fromglobalDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    }
                } else if (key.equalsIgnoreCase("AS_OF_DATE2")) {
                    if (date11 != null && date11.equalsIgnoreCase("toyestrday")) {
                        obj[4] = "null";
                        obj[5] = "toyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("totomorow")) {
                        obj[4] = "null";
                        obj[5] = "totomorow";
                    } else if (date11 != null && (date11.contains("toSystDate") || date11.contains("toglobalDdate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[4] = "null";
                        obj[5] = date11;
                    } else if (date11 != null && date11.contains("tofixeddate")) {
                        obj[4] = date;
                        obj[5] = "tofixeddate";
                    } else if (date11 != null && date11.contains("toSystDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    } else if (date11 != null && date11.contains("toglobalDdate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    }
                } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE1")) {
                    if (date11 != null && date11.equalsIgnoreCase("CmpFrmyestrday")) {
                        obj[4] = "null";
                        obj[5] = "CmpFrmyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("CmpFrmtomorow")) {
                        obj[4] = "null";
                        obj[5] = "CmpFrmtomorow";
                    } else if (date11 != null && (date11.contains("CmpFrmSysDate") || date11.contains("CmpFrmglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[4] = "null";
                        obj[5] = date11;
                    } else if (date11 != null && date11.contains("CmpFrmfixeddate")) {
                        obj[4] = date;
                        obj[5] = "CmpFrmfixeddate";
                    } else if (date11 != null && date11.contains("CmpFrmSysDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    } else if (date11 != null && date11.contains("CmpFrmglobalDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    }
                } else if (key.equalsIgnoreCase("CMP_AS_OF_DATE2")) {
                    if (date11 != null && date11.equalsIgnoreCase("cmptoyestrday")) {
                        obj[4] = "null";
                        obj[5] = "cmptoyestrday";
                    } else if (date11 != null && date11.equalsIgnoreCase("cmptotomorow")) {
                        obj[4] = "null";
                        obj[5] = "cmptotomorow";
                    } else if (date11 != null && (date11.contains("cmptoSysDate") || date11.contains("cmptoglobalDate")) && currDetails != null && currDetails.equalsIgnoreCase("currdetails")) {
                        obj[4] = "null";
                        obj[5] = date11;
                    } else if (date11 != null && date11.contains("Cmptofixeddate")) {
                        obj[4] = date;
                        obj[5] = "Cmptofixeddate";
                    } else if (date11 != null && date11.contains("cmptoSysDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    } else if (date11 != null && date11.contains("cmptoglobalDate")) {
                        obj[4] = "null";
                        obj[5] = date;
                    }
                }
            }
            finalQuery = buildQuery(sqlQuery, obj);

            queries.add(finalQuery);

            timeDetails = null;
            obj = null;
            i++;
        }
        return queries;
    }

    public ArrayList insertReportViewByMaster(ArrayList REP_Elements, ArrayList CEP_Elements, int reportId, ArrayList queries, ArrayList repExclude, ArrayList cepExclude, boolean overwrite, Container container) throws Exception {
        String insertReportViewByMasterForREPQuery = getResourceBundle().getString("insertReportViewByMasterForREP");
        String insertReportViewByMasterForCEPQuery = getResourceBundle().getString("insertReportViewByMasterForCEP");
        Object[] masterObj = null;
        String finalQuery = "";
        if (REP_Elements != null) {
            for (int i = 0; i < REP_Elements.size(); i++) {
//                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                    masterObj = new Object[3];
//                    masterObj[0] = (i + 1);
//                    masterObj[1] = (i + 1);
//                    masterObj[2] = REP_Elements.get(i).toString().replace("A_", "");
//                } else {
                masterObj = new Object[6];
                masterObj[0] = reportId;
                masterObj[1] = (i + 1);
                masterObj[2] = (i + 1);
                masterObj[3] = REP_Elements.get(i).toString().replace("A_", "");
                if (container.getReportCollect().crosstabelements != null && !container.getReportCollect().crosstabelements.isEmpty() && container.getReportCollect().crosstabelements.containsKey(REP_Elements.get(i).toString().replace("A_", ""))) {
                    masterObj[4] = Joiner.on(",").join(container.getReportCollect().getCrosstabelements(REP_Elements.get(i).toString().replace("A_", "")));
                } else {
                    masterObj[4] = null;
                }
//                }
                if (container.getReportCollect().getHideViewbys() != null && !container.getReportCollect().getHideViewbys().isEmpty() && container.getReportCollect().getHideViewbys().contains(REP_Elements.get(i).toString().replace("A_", ""))) {
                    masterObj[5] = true;
                } else {
                    masterObj[5] = false;
                }
                finalQuery = buildQuery(insertReportViewByMasterForREPQuery, masterObj);
                if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    execUpdateSQL(finalQuery);
                } else {
                    queries.add(finalQuery);
                }

                queries = insertReportViewByDetails(reportId, queries, repExclude, overwrite, (i + 1));
            }
        }
        if (CEP_Elements != null) {
            for (int i = REP_Elements.size(), j = 0; j < CEP_Elements.size(); i++, j++) {
//                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                    masterObj = new Object[3];
//                    masterObj[0] = (i + 1);
//                    masterObj[1] = (j + 1);
//                    masterObj[2] = CEP_Elements.get(j).toString().replace("A_", "");
//                } else {
                ArrayList crosstabname = new ArrayList();
                Object[] obj;
                masterObj = new Object[5];
                masterObj[0] = reportId;
                masterObj[1] = (i + 1);
                masterObj[2] = (j + 1);
                masterObj[3] = CEP_Elements.get(j).toString().replace("A_", "");
                if (container.getReportCollect().crosstabelements != null && !container.getReportCollect().crosstabelements.isEmpty() && container.getReportCollect().crosstabelements.containsKey(CEP_Elements.get(j).toString().replace("A_", ""))) {
                    masterObj[4] = Joiner.on(",").join(container.getReportCollect().getCrosstabelements(CEP_Elements.get(j).toString().replace("A_", "")));
                } else {
                    masterObj[4] = null;
                }
//                }
                HashMap TableHashMap = container.getTableHashMap();
                finalQuery = buildQuery(insertReportViewByMasterForCEPQuery, masterObj);
                if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    execUpdateSQL(finalQuery);
                } else {
                    queries.add(finalQuery);
                }
                queries = insertReportViewByDetails(reportId, queries, cepExclude, overwrite, (i + 1));
                //added by sruthi for crosstab rename
                String qry = "update PRG_AR_REPORT_PARAM_DETAILS set PARAM_DISP_NAME='&' where ELEMENT_ID='" + CEP_Elements.get(j).toString() + "' and REPORT_ID='" + reportId + "'";
                obj = new Object[1];
                crosstabname = (ArrayList) container.getTableHashMap().get("CEPNames");
                obj[0] = crosstabname.get(j);
                String finalQuery1 = buildQuery(qry, obj);
                execUpdateSQL(finalQuery1);
                //ended by sruthi
            }
        }
        return queries;
    }

    public ArrayList insertReportViewByDetails(int reportId, ArrayList queries, ArrayList Exclude, boolean overwrite, int count) throws Exception {

        String insertReportViewByDetailsQuery = getResourceBundle().getString("insertReportViewByDetails");
        String getReportViewByDetailsQuery = "";
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] detailsObj;
        detailsObj = new Object[1];
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            if (overwrite) {
                detailsObj[0] = reportId;
                getReportViewByDetailsQuery = getResourceBundle().getString("getupdatedReportViewByDetails");
                finalQuery = buildQuery(getReportViewByDetailsQuery, detailsObj);
            } else {
                getReportViewByDetailsQuery = getResourceBundle().getString("getReportViewByDetails");
                finalQuery = getReportViewByDetailsQuery;
            }

        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            if (overwrite) {
                detailsObj[0] = reportId;
                getReportViewByDetailsQuery = getResourceBundle().getString("getupdatedReportViewByDetails");
                finalQuery = buildQuery(getReportViewByDetailsQuery, detailsObj);
            } else {
                getReportViewByDetailsQuery = getResourceBundle().getString("getReportViewByDetails");
                detailsObj[0] = reportId;
                finalQuery = buildQuery(getReportViewByDetailsQuery, detailsObj);
            }
        } else {
            getReportViewByDetailsQuery = getResourceBundle().getString("getReportViewByDetails");
            detailsObj[0] = reportId;
            finalQuery = buildQuery(getReportViewByDetailsQuery, detailsObj);
        }
//        //////.println("insertReportViewByDetails finalquery is : "+finalQuery);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();

        for (int i = 0; i < retObj.getRowCount(); i++) {
            if (!(Exclude == null) && !(Exclude.equals(null) && !("null".equalsIgnoreCase(String.valueOf(Exclude))))) {
                if (Exclude.contains(String.valueOf(retObj.getFieldValueInt(i, dbColumns[2])))) {
                } else if (!overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    detailsObj = new Object[3];
                    detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    String qry = "SELECT LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER  order by 1 desc limit 1";
                    PbReturnObject retobj = new PbReturnObject();
                    retobj = super.execSelectSQL(qry);
                    detailsObj[1] = Integer.parseInt(retobj.getFieldValueString(0, 0)) + 1;
                    detailsObj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                    finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                    queries.add(finalQuery);
                } else if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    detailsObj = new Object[3];
                    detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    String qry = "SELECT LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER  order by 1 desc limit 1";
                    PbReturnObject retobj = new PbReturnObject();
                    retobj = super.execSelectSQL(qry);
                    detailsObj[1] = Integer.parseInt(retobj.getFieldValueString(0, 0));
                    detailsObj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                    finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                    queries.add(finalQuery);
                } else {
                    detailsObj = new Object[2];
                    detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                    detailsObj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                    finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                    queries.add(finalQuery);
                }
            } else if (overwrite && ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                detailsObj = new Object[3];
                detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                String qry = "SELECT LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER  order by 1 desc limit 1";
                PbReturnObject retobj = new PbReturnObject();
                retobj = super.execSelectSQL(qry);
                detailsObj[1] = Integer.parseInt(retobj.getFieldValueString(0, 0));
                detailsObj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                queries.add(finalQuery);
            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                detailsObj = new Object[3];
                detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                String qry = "SELECT LAST_INSERT_ID(VIEW_BY_ID) from PRG_AR_REPORT_VIEW_BY_MASTER  order by 1 desc limit 1";
                PbReturnObject retobj = new PbReturnObject();
                retobj = super.execSelectSQL(qry);
                detailsObj[1] = Integer.parseInt(retobj.getFieldValueString(0, 0)) + count;
                detailsObj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                queries.add(finalQuery);
            } else {
                detailsObj = new Object[2];
                detailsObj[0] = retObj.getFieldValueString(i, dbColumns[0]);
                detailsObj[1] = retObj.getFieldValueString(i, dbColumns[1]);
                finalQuery = buildQuery(insertReportViewByDetailsQuery, detailsObj);
                queries.add(finalQuery);
            }
        }
        return queries;
    }

    public ArrayList insertReportQueryDetailsForTimeDB(String timePeriods, int reportId, String measuress, String measuresOrder, ArrayList queries, boolean overwrite, Container container) throws Exception {
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportQueryDetailsT", "Enter");
        logger.info("Enter ");
        String getReportQueryInfoQuery = getResourceBundle().getString("getReportQueryInfo");
        String insertReportQueryDetailsQuery = getResourceBundle().getString("insertReportQueryDetailsT");
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String finalQuery = "";
        String[] dbColumns = null;
        Obj = new Object[2];
        Obj[0] = measuress;
        Obj[1] = measuresOrder;
        finalQuery = buildQuery(getReportQueryInfoQuery, Obj);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();
        HashMap<String, ArrayList<String>> summerizedTableHashMap = container.getSummerizedTableHashMap();
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                Obj = new Object[11];
                Obj[0] = (i + 1);
                Obj[1] = retObj.getFieldValueString(i, dbColumns[0]);
                Obj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                Obj[3] = retObj.getFieldValueString(i, dbColumns[2]);
                Obj[4] = retObj.getFieldValueString(i, dbColumns[3]);
                Obj[5] = retObj.getFieldValueString(i, dbColumns[4]);
                Obj[6] = retObj.getFieldValueString(i, dbColumns[5]);
                Obj[7] = reportId;
                Obj[8] = retObj.getFieldValueString(i, dbColumns[6]);
                if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summerizedTableHashMap.get("summerizedQryeIds")) != null && ((List<String>) summerizedTableHashMap.get("summerizedQryeIds")).contains(retObj.getFieldValueString(i, dbColumns[0]))) {
                    Obj[9] = true;
                } else {
                    Obj[9] = false;
                }
//            }
                if (container.IsTimedasboard()) {//added by Bhargavi

                    Obj[10] = timePeriods;
                } else {
                    Obj[10] = "";
                }

                finalQuery = buildQuery(insertReportQueryDetailsQuery, Obj);
                queries.add(finalQuery);
            }
        }
        if ((overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) || (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))) {
            String deleteReportQueryDetailsQuery = "delete from PRG_AR_QUERY_DETAIL where report_id ='" + reportId + "'";
            execModifySQL(deleteReportQueryDetailsQuery);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportQueryDetailsT", "Exit");
        logger.info("Exit ");
        return queries;
    }

    public ArrayList insertReportQueryDetails(int reportId, String measuress, String measuresOrder, ArrayList queries, boolean overwrite, Container container) throws Exception {
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportQueryDetails", "Enter");
        logger.info("Enter ");
        String getReportQueryInfoQuery = getResourceBundle().getString("getReportQueryInfo");
        String insertReportQueryDetailsQuery = getResourceBundle().getString("insertReportQueryDetails");
        PbReturnObject retObj = null;
        Object[] Obj = null;
        String finalQuery = "";
        String[] dbColumns = null;
        Obj = new Object[2];
        Obj[0] = measuress;
        Obj[1] = measuresOrder;
        finalQuery = buildQuery(getReportQueryInfoQuery, Obj);
        retObj = execSelectSQL(finalQuery);
        dbColumns = retObj.getColumnNames();
        HashMap<String, ArrayList<String>> summerizedTableHashMap = container.getSummerizedTableHashMap();
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
//            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                Obj = new Object[8];
//                Obj[0] = (i + 1);
//                Obj[1] = retObj.getFieldValueString(i, dbColumns[0]);
//                Obj[2] = retObj.getFieldValueString(i, dbColumns[1]);
//                Obj[3] = retObj.getFieldValueString(i, dbColumns[2]);
//                Obj[4] = retObj.getFieldValueString(i, dbColumns[3]);
//                Obj[5] = retObj.getFieldValueString(i, dbColumns[4]);
//                Obj[6] = retObj.getFieldValueString(i, dbColumns[5]);
//                Obj[7] = retObj.getFieldValueString(i, dbColumns[6]);
//            } else {
                Obj = new Object[10];
                Obj[0] = (i + 1);
                Obj[1] = retObj.getFieldValueString(i, dbColumns[0]);
                Obj[2] = retObj.getFieldValueString(i, dbColumns[1]);
                Obj[3] = retObj.getFieldValueString(i, dbColumns[2]);
                Obj[4] = retObj.getFieldValueString(i, dbColumns[3]);
                Obj[5] = retObj.getFieldValueString(i, dbColumns[4]);
                Obj[6] = retObj.getFieldValueString(i, dbColumns[5]);
                Obj[7] = reportId;
                Obj[8] = retObj.getFieldValueString(i, dbColumns[6]);
                if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null && ((List<String>) summerizedTableHashMap.get("summerizedQryeIds")) != null && ((List<String>) summerizedTableHashMap.get("summerizedQryeIds")).contains(retObj.getFieldValueString(i, dbColumns[0]))) {
                    Obj[9] = true;
                } else {
                    Obj[9] = false;
                }
//            }
                finalQuery = buildQuery(insertReportQueryDetailsQuery, Obj);
                queries.add(finalQuery);
            }
        }
        if ((overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) || (overwrite && ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL))) {
            String deleteReportQueryDetailsQuery = "delete from PRG_AR_QUERY_DETAIL where report_id ='" + reportId + "'";
            execModifySQL(deleteReportQueryDetailsQuery);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportQueryDetails", "Exit");
        logger.info("Exit ");
        return queries;
    }

    public ArrayList insertReportTableMaster(int reportId, String reportName, Container container, ArrayList queries, boolean overwrit) {

        String insertReportTableMasterQuery = getResourceBundle().getString("insertReportTableMaster");
        String finalQuery = "";
        String sql = "";
        StringBuilder ElementIds = new StringBuilder();
        StringBuilder ElementIdsOrder = new StringBuilder();

        HashMap TableProperties = null;
        HashMap ColumnProperties = null;
        ArrayList CEP = null;
        ArrayList<String> measList = new ArrayList<String>();

        HashMap ColorCodeMap = null;
        HashMap tempHashMap = null;
        Object[] Obj = null;
        //Set keySet=null;

//        ProgenLog.log(ProgenLog.FINE, this, "insertReportTableMaster", "Enter");
        logger.info("Enter ");

        if (getTableHashMap() != null) {
            if (getTableHashMap().get("ColorCodeMap") != null) {
                ColorCodeMap = (HashMap) getTableHashMap().get("ColorCodeMap");
            }
            if (getTableHashMap().get("TableProperties") != null) {
                TableProperties = (HashMap) getTableHashMap().get("TableProperties");
            }
            if (container.getColumnProperties() != null) {
                ColumnProperties = container.getColumnProperties();
            } else {
                ColumnProperties = new HashMap();
            }
            CEP = (ArrayList) getTableHashMap().get("CEP");
            if (CEP == null) {
                CEP = new ArrayList();
            }
        }

        TableHashMap = container.getTableHashMap();
        if (TableHashMap.containsKey("Measures")) {
            measList = (ArrayList) TableHashMap.get("Measures");
//           for(int i=0;i<newmeasList.size();i++){
//               if(newmeasList!=null && !newmeasList.get(i).toString().contains("_percentwise")){
//               measList.add(newmeasList.get(i).toString());
//               }
//           }

        }
//        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//            Obj = new Object[11];
//            Obj[0] = reportName;
//
//            String defaultSortedCol = "";
//            String chkTtlValues = null;
//            String chkSubTtlValues = null;
//            String chkAvgValues = null;
//            String chkOvrAllMaxValues = null;
//            String chkOvrAllMinValues = null;
//            String chkCatMaxValues = null;
//            String chkCatMinValues = null;
//            String selSymbolsValues = null;
//            String tableDisplayRows = null;
//
//            if (TableProperties != null && TableProperties.size() != 0) {
//                defaultSortedCol = (String) TableProperties.get("DefaultSortedColumn");
//                chkTtlValues = (String) TableProperties.get("ShowTotalValues");
//                chkSubTtlValues = (String) TableProperties.get("ShowSubTotalValues");
//                chkAvgValues = (String) TableProperties.get("ShowAvgValues");
//                chkOvrAllMaxValues = (String) TableProperties.get("ShowOvrAllMaxValues");
//                chkOvrAllMinValues = (String) TableProperties.get("ShowOvrAllMinValues");
//                chkCatMaxValues = (String) TableProperties.get("ShowCatMaxValues");
//                chkCatMinValues = (String) TableProperties.get("ShowCatMinValues");
//                selSymbolsValues = (String) TableProperties.get("ColumnSymbols");
//                tableDisplayRows = (String) TableProperties.get("tableDisplayRows");
////            ////////.println("tableDisplayRows====" + tableDisplayRows);
//            }
//
//            Obj[1] = chkSubTtlValues;
//            Obj[2] = chkTtlValues;
//            Obj[3] = chkAvgValues;
//            Obj[4] = chkOvrAllMinValues;
//            Obj[5] = chkOvrAllMaxValues;
//            Obj[6] = chkCatMaxValues;
//            Obj[7] = chkCatMinValues;
//            Obj[8] = selSymbolsValues;
//            Obj[9] = defaultSortedCol;
//            Obj[10] = tableDisplayRows;
//        } else {
        reportName = reportName.replace("'", "''");
        Obj = new Object[24]; //2+9
        Obj[0] = reportId;
        Obj[1] = reportName;

        String defaultSortedCol = "";
        String chkTtlValues = null;
        String chkSubTtlValues = null;
        String chkAvgValues = null;
        String chkOvrAllMaxValues = null;
        String chkOvrAllMinValues = null;
        String chkCatMaxValues = null;
        String chkCatMinValues = null;
        String selSymbolsValues = null;
        String tableDisplayRows = null;
        String catAvgReq = null;

        if (TableProperties != null && TableProperties.size() != 0) {
            defaultSortedCol = (String) TableProperties.get("DefaultSortedColumn");
            chkTtlValues = (String) TableProperties.get("ShowTotalValues");
            chkSubTtlValues = (String) TableProperties.get("ShowSubTotalValues");
            chkAvgValues = (String) TableProperties.get("ShowAvgValues");
            chkOvrAllMaxValues = (String) TableProperties.get("ShowOvrAllMaxValues");
            chkOvrAllMinValues = (String) TableProperties.get("ShowOvrAllMinValues");
            chkCatMaxValues = (String) TableProperties.get("ShowCatMaxValues");
            chkCatMinValues = (String) TableProperties.get("ShowCatMinValues");
            selSymbolsValues = (String) TableProperties.get("ColumnSymbols");
            catAvgReq = (String) TableProperties.get("ShowcatAvg");
//            ////////.println("tableDisplayRows====" + tableDisplayRows);
        }
        tableDisplayRows = container.getPagesPerSlide();
        String tabletype = "table";
        if (container.isTopBottomTableEnable()) {
            tabletype = "TopBottomTable";
        }
        Obj[2] = tabletype;
        Obj[3] = chkSubTtlValues;
        Obj[4] = chkTtlValues;
        Obj[5] = chkAvgValues;
        Obj[6] = chkOvrAllMinValues;
        Obj[7] = chkOvrAllMaxValues;
        Obj[8] = chkCatMaxValues;
        Obj[9] = chkCatMinValues;
        Obj[10] = selSymbolsValues;
        Obj[11] = defaultSortedCol;
        Obj[12] = tableDisplayRows;
        Obj[13] = container.tablePropertiesXml();
        if (container.getSearchReq()) {
            Obj[14] = "Y";
        } else {
            Obj[14] = "N";
        }

        if (container.isDrillAcrossSupported()) {
            Obj[15] = "Y";
        } else {
            Obj[15] = "N";
        }
        if (container.isMeasDrill()) {
            Obj[16] = "Y";
        } else {
            Obj[16] = "N";
        }
        if (container.getCatAvgTotalReq()) {
            Obj[17] = "Y";
        } else {
            Obj[17] = "N";
        }
//        }
        Gson gson = new Gson();
//        if(container.isSummarizedMeasuresEnabled())
//            Obj[18]=gson.toJson(container.getSummerizedTableHashMap());
//          else
        Obj[18] = null;
        if (container.isReportCrosstab()) {
            Obj[19] = gson.toJson(container.Displaylabelmap);
        } else {
            Obj[19] = null;
        }
        if (Boolean.valueOf(container.getgTAverage())) {
            Obj[20] = "true";
        } else {
            Obj[20] = "false";
        }
        if (container.isTopBottomTableEnable()) {
            Obj[21] = gson.toJson(container.getTopBottomTableHashMap());
        } else {
            Obj[21] = null;
        }
        if (container.getReportCollect().isIsExcelimportEnable()) {
            Obj[22] = "fromExcel";
        } else {
            Obj[22] = null;
        }
        if (container.getReportCollect().isIsExcelimportEnable()) {
            Obj[23] = this.writeExcelRetunObjecttoFile(reportId, container);
        } else {
            Obj[23] = null;
        }

        try {
//           //start of code by bhargavi to prevent the display of base measure if it is percentwise or pwst
//            for (int i = 0; i < measList.size(); i++) {
//                if (measList.get(i).toString().contains("_pwst") || measList.get(i).toString().contains("_percentwise") && !container.getIsPercentColumnwithAbsolute(measList.get(i)).equalsIgnoreCase("true")) {
//                    String orgCol = measList.get(i).toString().replace("_percentwise", "").replace("_pwst", "");
//                    if (measList.contains(orgCol)) {
//                        int index = measList.indexOf(orgCol);
//                        measList.remove(index);
//                    }
//                }
//            }
//             //end of code by bhargavi
            finalQuery = buildQuery(insertReportTableMasterQuery, Obj);
            ////////////////.println("finalQuery is "+finalQuery);
            execUpdateSQL(finalQuery);
            //queries.add(finalQuery);
            int j = 0;
            String ElementIdsOrder2 = " case ";
            for (String measure : measList) {
                if (!RTMeasureElement.isRunTimeMeasure(measure) && !measure.contains("_rt")) //if pattern is not matching it's a regular measure
                {
                    ElementIds.append("," + measure.replace("A_", ""));
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        ElementIdsOrder2 += " when element_id =" + measure.replace("A_", "") + " then " + (j + 1) + " ";
                    } else {
                        ElementIdsOrder.append("," + measure.replace("A_", "") + "," + (j + 1));
                    }
                } //start of code by Govardhan for percentwise without absolute....
                else if (!measList.contains(measure.replace("A_", "").replace("_percentwise", "").replace("_pwst", "").replace("_rt", "").replace("_rankST", "").replace("_rank", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "").replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                        .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", ""))) {
                    ElementIds.append("," + measure.replace("A_", "").replace("_percentwise", "").replace("_pwst", "").replace("_rt", "").replace("_rankST", "").replace("_rank", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "").replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                            .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", ""));
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                        ElementIdsOrder2 += " when element_id =" + measure.replace("A_", "").replace("_percentwise", "").replace("_pwst", "").replace("_rt", "").replace("_rankST", "").replace("_rank", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "").replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                                .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "")+ " then " + (j + 1) + " ";
                    } else {
                        ElementIdsOrder.append("," + measure.replace("A_", "").replace("_percentwise", "").replace("_pwst", "").replace("_rt", "").replace("_rankST", "").replace("_rank", "").replace("_MTD", "").replace("_QTD", "").replace("_YTD", "").replace("_PMTD", "").replace("_PQTD", "").replace("_PYTD", "").replace("_MOMPer", "").replace("_QOQPer", "").replace("_YOYPer", "").replace("_MOYMPer", "").replace("_QOYQPer", "").replace("_MOM", "").replace("_QOQ", "").replace("_YOY", "").replace("_MOYM", "").replace("_QOYQ", "").replace("_Qtdrank", "").replace("_Ytdrank", "").replace("_PMtdrank", "").replace("_PQtdrank", "").replace("_PYtdrank", "").replace("_PYMTD", "").replace("_PYQTD", "")
                                .replace("_WTD", "").replace("_PWTD", "").replace("_PYWTD", "").replace("_WOWPer", "").replace("_WOYWPer", "").replace("_WOW", "").replace("_WOYW", "")+ "," + (j + 1));
                    }
                }
                //end of code by Govardhan for percentwise without absolute....

                j++;
            }
            if (container.isReportCrosstab()) {
                HashMap<String, ArrayList<String>> summerizedTableHashMap = container.getSummerizedTableHashMap();
                if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled() && summerizedTableHashMap != null && summerizedTableHashMap.get("summerizedQryeIds") != null) {
                    List<String> summerizedqryEIDs = (List<String>) summerizedTableHashMap.get("summerizedQryeIds");
                    for (int i = 0; i < summerizedqryEIDs.size(); i++) {
                        j = j + 1;
                        ElementIds.append("," + summerizedqryEIDs.get(i).toString().replace("A_", ""));
                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                            ElementIdsOrder2 += " when element_id = " + summerizedqryEIDs.get(i).toString().replace("A_", "") + " then " + (j) + " ";
                        } else {
                            ElementIdsOrder.append("," + summerizedqryEIDs.get(i).toString().replace("A_", "") + "," + (j));
                        }
                    }
                }
            }
            ElementIdsOrder2 += " else 10000 end";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                if (overwrit) {
                    sql = "select qry_col_id,col_disp_name, col_seq,element_id from prg_ar_query_detail where element_id in (" + ElementIds.substring(1) + ")  and report_id=" + reportId + " order by " + ElementIdsOrder2;
                } else {
                    sql = "select qry_col_id,col_disp_name, col_seq,element_id from prg_ar_query_detail where element_id in (" + ElementIds.substring(1) + ")  and report_id= ident_current('PRG_AR_REPORT_MASTER') order by " + ElementIdsOrder2;
                }
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                sql = "select qry_col_id,col_disp_name, col_seq,element_id from prg_ar_query_detail where element_id in (" + ElementIds.substring(1) + ")  and report_id=" + reportId + " order by " + ElementIdsOrder2;
            } else {
                sql = "select qry_col_id,col_disp_name, col_seq,element_id from prg_ar_query_detail where element_id in (" + ElementIds.substring(1) + ")  and report_id='" + reportId + "' order by decode (element_id," + ElementIdsOrder.substring(1) + ") ";
            }

            logger.info("sql-----------" + sql);
            PbReturnObject queryObj = execSelectSQL(sql);
            String query;
            //String columnSequence = "0";
            String columnDisplayName = null;
            String qry_col_id;
            String getColorGroupXML = null;
            String elementId = null;
            int i = 0;
            int columnSequence = 0;
            String tabmstrId = "";
            int viewCount = container.getViewByCount();
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                String qry = "select LAST_INSERT_ID(REP_TAB_ID) from PRG_AR_REPORT_TABLE_MASTER order by 1 desc limit 1";
                PbReturnObject tabdtlobj = super.execSelectSQL(qry);
                tabmstrId = String.valueOf(Integer.parseInt(tabdtlobj.getFieldValueString(0, 0)));
            }
            ArrayList<String> hideMesureList = container.getReportCollect().getHideMeasures();
            for (String measure : measList) {
                columnSequence++;
                if (!RTMeasureElement.isRunTimeMeasure(measure) && !measure.contains("_rt")) { //if pattern is not matching it's a regular measure
                    columnDisplayName = queryObj.getFieldValueString(i, 1);
                    qry_col_id = queryObj.getFieldValueString(i, 0);
                    elementId = queryObj.getFieldValueString(i, 3);
                    //String updateQuery = "";
                    if (container.getMeasureName("A_" + elementId) != null) {
                        columnDisplayName = container.getMeasureName("A_" + elementId);
                    }
                    getColorGroupXML = buildColorCodeXML(ColorCodeMap, "A_" + elementId);
                    ArrayList singleColPropList = null;
                    if (measure.contains("A_")) {
                        singleColPropList = ColumnProperties.get(measure) == null ? null : (ArrayList) ColumnProperties.get(measure);
                    } else {
                        singleColPropList = ColumnProperties.get("A_" + measure) == null ? null : (ArrayList) ColumnProperties.get("A_" + measure);
                    }

                    if (singleColPropList != null && singleColPropList.size() != 0) {

                        boolean indicatorEnabled = container.isIndicatorEnabled(measure);
                        String indicatorStr = "N";
                        if (indicatorEnabled) {
                            indicatorStr = "Y";
                        }
                        String scriptIndicator = container.getscriptIndicator("A_" + elementId);
                        String scriptAlign = container.getTextAlign("A_" + elementId);
                        String measureAlign = container.getMeasureAlign("A_" + elementId);
                        String measureType = container.getmeasureType("A_" + elementId);
                        String timeConversion = container.gettimeConversion("A_" + elementId);
                        //start of code by Nazneen for sub total deviation
                        String subTotalDeviation = container.getSubTotalDeviation("A_" + elementId);
                        String ctGtAggType = container.getCTGtAggType("A_" + elementId);

                        String numFormat = getNumberSymbol("A_" + elementId);
                        int roundPrecision = container.getRoundPrecisionForMeasure("A_" + elementId);
                        String datetimeoption = container.getDateandTimeOptions("A_" + elementId);
                        String dataSubstrcol = container.getDateSubStringValues("A_" + elementId);
                        String dateFormat = container.getDateFormatt("A_" + elementId);
                        String repTabId = null;
                        if (overwrit) {
                            String qry1 = "select REP_TAB_ID from PRG_AR_REPORT_TABLE_MASTER where report_id=" + reportId;
                            PbReturnObject retObj = executeSelectSQL(qry1);
                            repTabId = retObj.getFieldValueString(0, 0);
                        }

                        String reportDrillAssignRepId = null;
                        String multireportDrillAssignRepId = null;
                        // 
                        if (container.getMsrDrillReportSelection() != null && container.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
                            multireportDrillAssignRepId = container.getReportDrillMap("A_" + elementId);
                        } else {
                            reportDrillAssignRepId = container.getReportDrillMap("A_" + elementId);
                        }

                        boolean hideMsrenable = false;
                        if (hideMesureList != null && !hideMesureList.isEmpty() && hideMesureList.contains(elementId)) {
                            hideMsrenable = true;
                        }

                        avgcalmap = container.getReportCollect().crosscolmap1;
                        avgvalcount = avgcalmap.keySet();
                        Iterator itcalcross = avgvalcount.iterator();
                        while (itcalcross.hasNext()) {
                            String keys = itcalcross.next().toString();
                            if (keys.equalsIgnoreCase(measure)) {
                                avgvaltype = avgcalmap.get(keys).toString();
                                break;
                            } else {
                                avgvaltype = "Include 0";
                            }
                        }
                        //changed by sruthi for numberformate 
                        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            if (overwrit) {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                        + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE,CT_GT_AGG_TYPE,NUM_FORMATE) "
                                        + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                        + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','false','" + avgvaltype + "','" + ctGtAggType + "','" + singleColPropList.get(9) + "')";

                            } else {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                        + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE,CT_GT_AGG_TYPE,NUM_FORMATE) "
                                        + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                        + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','false','" + avgvaltype + "','" + ctGtAggType + "','" + singleColPropList.get(9) + "')";
                            }
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            if (overwrit) {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                        + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE,CT_GT_AGG_TYPE,NUM_FORMATE) "
                                        + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                        + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','false','" + avgvaltype + "','" + ctGtAggType + "','" + singleColPropList.get(9) + "')";

                            } else {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                        + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE,CT_GT_AGG_TYPE,NUM_FORMATE) "
                                        + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                        + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','false','" + avgvaltype + "','" + ctGtAggType + "','" + singleColPropList.get(9) + "')";
                            }
                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                    + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE,CT_GT_AGG_TYPE,NUM_FORMATE) "
                                    + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "'," + repTabId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                    + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',TO_CLOB('" + getColorGroupXML + "') ,'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','false','" + avgvaltype + "','" + ctGtAggType + "','" + singleColPropList.get(9) + "')";
                        }

                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        if (overwrit) {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'false','" + avgvaltype + "')";

                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'false','" + avgvaltype + "')";
                        }
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        if (overwrit) {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'false','" + avgvaltype + "')";

                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'false','" + avgvaltype + "')";
                        }
                    } else {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "',PRG_AR_REPORT_TABLE_MASTER_SEQ.currval,'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',TO_CLOB('" + getColorGroupXML + "'),'false','" + avgvaltype + "')";
                    }
                    i++;
                    queries.add(query);
                } else {
                    //code to insert formula measure
                    String measure1 = measList.get(i).toString().replace("_percentwise", "").replace("_pwst", "");
                    if (!measList.contains(measure1)) {
                        i++;
                    }
                    String columnType;
                    columnType = RTMeasureElement.getMeasureType(measure).getColumnType();
                    //columnDisplayName = columnDisplayName + RTMeasureElement.getMeasureType(measure).getColumnDisplay();
//                  modified by Nazneen for %wise columns
//                    columnDisplayName = container.getDisplayLabels().get(viewCount).toString();
                    if (container.getMeasureName(measure) != null) {
                        columnDisplayName = container.getMeasureName(measure);
                    } else {
                        columnDisplayName = container.getDisplayLabels().get(viewCount).toString();
                    }

                    String columnId = container.getDisplayColumns().get(viewCount).toString();
                    elementId = RTMeasureElement.getOriginalColumn(measure).replace("A_", "");
                    ArrayList singleColPropList = null;
                    if (measure.contains("A_")) {
                        singleColPropList = ColumnProperties.get(measure) == null ? null : (ArrayList) ColumnProperties.get(measure);
                    } else {
                        singleColPropList = ColumnProperties.get("A_" + measure) == null ? null : (ArrayList) ColumnProperties.get("A_" + measure);
                    }
                    if (RTMeasureElement.getMeasureType(measure).isCommitted()) {
                        if (RTMeasureElement.isRunTimeExcelColumn(measure)) {
                            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                                if (overwrit) {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols, runtime_measure_id,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','" + columnId + "','false','" + avgvaltype + "')";

                                } else {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols, runtime_measure_id,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','" + columnId + "','false','" + avgvaltype + "')";
                                }
                            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                                if (overwrit) {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols, runtime_measure_id,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','" + columnId + "','false','" + avgvaltype + "')";

                                } else {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols, runtime_measure_id,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','" + columnId + "','false','" + avgvaltype + "')";
                                }
                            } else {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols, runtime_measure_id,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                        + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "',PRG_AR_REPORT_TABLE_MASTER_SEQ.currval,'" + columnDisplayName + "','" + columnSequence + "',TO_CLOB('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','" + columnId + "','false','" + avgvaltype + "')";
                            }
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                            if (overwrit) {
                                if (singleColPropList != null && singleColPropList.size() != 0) {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "', '" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                            + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','false','" + avgvaltype + "')";
                                } else {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','false','" + avgvaltype + "')";
                                }

                            } else if (singleColPropList != null && singleColPropList.size() != 0) {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                        //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                        + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "', '" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','false','" + avgvaltype + "')";
                            } else {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                        //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                        + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','false','" + avgvaltype + "')";
                            }
                        } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                            if (overwrit) {
                                if (singleColPropList != null && singleColPropList.size() != 0) {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "', '" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                            + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','false','" + avgvaltype + "')";
                                } else {
                                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                            //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                            + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','false','" + avgvaltype + "')";
                                }
                            } else if (singleColPropList != null && singleColPropList.size() != 0) {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                        //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                        + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "', '" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                        + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','false','" + avgvaltype + "')";
                            } else {
                                query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                        //+ " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), 'percentwise'," + elementId + ")";
                                        + " values (" + reportId + "," + tabmstrId + ",'" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','false','" + avgvaltype + "')";
                            }
                        } else if (singleColPropList != null && singleColPropList.size() != 0) {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "',PRG_AR_REPORT_TABLE_MASTER_SEQ.currval,'" + columnDisplayName + "','" + columnSequence + "',TO_CLOB('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "', '" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','false','" + avgvaltype + "')";

                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,color_group,run_time_formula,dependent_cols,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "',PRG_AR_REPORT_TABLE_MASTER_SEQ.currval,'" + columnDisplayName + "','" + columnSequence + "',TO_CLOB('" + getColorGroupXML + "'), '" + columnType + "','" + elementId + "','false','" + avgvaltype + "')";

                        }
                        queries.add(query);
                    }

                }
                viewCount++;

                //}
            }
            if (container.isReportCrosstab() && container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null) {
                queries = this.saveHybridReportMeasureProperties(reportId, tabmstrId, container, queryObj, i, columnSequence, overwrit, queries);
            }
            this.insertColorGroupDetails(container, reportId);
            this.insertExcelCellProperties(container, reportId);
            this.insertExcelColumns(container, reportId, false);
        }  catch (Exception ex) {
           logger.error("Exception", ex);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "insertReportTableMaster", "Exit");
        logger.info("Exit ");
        return queries;
    }

    public void insertColorGroupDetails(Container container, int reportId) {
        //code modified by Dinanath
        ColorGroup colorGroup = container.getColorGroup();
        int no0fDays = container.getNoOfDays();
        String insertReportColorQuery = "";
        String nextQry = "";
        ColorCodeTransferObject[] colorTransObj = null;
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = new PbReturnObject();
        Object[] objColor = null;
        String query = "";
        if (colorGroup != null) {
            try {
                colorTransObj = colorGroup.getColorCodeTransObject();
                for (int i = 0; i < colorTransObj.length; i++) {

                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
//                    nextQry="SELECT IDENT_CURRENT('PRG_AR_REPORT_COLORS')";
                        insertReportColorQuery = getResourceBundle().getString("insertReportColorQuery");
//                    returnObj=pbdb.execSelectSQL(nextQry);
                        objColor = new Object[13];           //Modified By ram
                        objColor[0] = reportId;
                        objColor[1] = colorTransObj[i].getMeasEleId();
                        objColor[2] = colorTransObj[i].getRowViewByValues();
                        objColor[3] = colorTransObj[i].getColViewByValues();
                        objColor[4] = colorTransObj[i].getParameter();
                        objColor[5] = colorTransObj[i].getRule();
                        if (container.isReportCrosstab()) {
                            objColor[6] = colorTransObj[i].getCrossTabMes();
                        } else {
                            objColor[6] = "";
                        }
                        if (colorTransObj[i].isGradientBased()) {
                            objColor[7] = "Y";
                        } else {
                            objColor[7] = "N";
                        }
                        objColor[8] = String.valueOf(colorTransObj[i].isAvgBased());
                        if (container.isReportCrosstab()) {
                            objColor[9] = colorTransObj[i].isMinMaxBased();
                        } else {
                            objColor[9] = "false";
                        }
                        objColor[10] = String.valueOf(colorTransObj[i].isPercentBased());
                        objColor[11] = String.valueOf(Container.isColorAppyForAllParameters()); //Added by Ram for Color Apply on All parameters.
                        objColor[12] = String.valueOf(Container.isColorAppyAcrossCurrentData()); //Added by Ram for Color Apply on CrossTab Across Current Data.

                        query = pbdb.buildQuery(insertReportColorQuery, objColor);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.ORACLE)) {
                        nextQry = "SELECT PRG_AR_REPORT_COLORS_SEQ.nextval Rep_Color_id FROM dual ";
                        insertReportColorQuery = getResourceBundle().getString("insertReportColorQueryForOracle");
                        //
                        returnObj = pbdb.execSelectSQL(nextQry);
                        objColor = new Object[14];
                        objColor[0] = returnObj.getFieldValueInt(0, 0);
                        objColor[1] = reportId;
                        objColor[2] = colorTransObj[i].getMeasEleId();
                        objColor[3] = colorTransObj[i].getRowViewByValues();
                        objColor[4] = colorTransObj[i].getColViewByValues();
                        objColor[5] = colorTransObj[i].getParameter();
                        objColor[6] = colorTransObj[i].getRule();
                        if (container.isReportCrosstab()) {
                            objColor[7] = colorTransObj[i].getCrossTabMes();
                        } else {
                            objColor[7] = "";
                        }
                        if (colorTransObj[i].isGradientBased()) {
                            objColor[8] = "Y";
                        } else {
                            objColor[8] = "N";
                        }
                        objColor[9] = String.valueOf(colorTransObj[i].isAvgBased());
                        if (container.isReportCrosstab()) {
                            objColor[10] = colorTransObj[i].isMinMaxBased();
                        } else {
                            objColor[10] = "false";
                        }
                        objColor[11] = String.valueOf(colorTransObj[i].isPercentBased());
                        objColor[12] = String.valueOf(Container.isColorAppyForAllParameters()); //Added by Ram for Color Apply on All parameters.
                        objColor[13] = String.valueOf(Container.isColorAppyAcrossCurrentData()); //Added by Ram for Color Apply on CrossTab Across Current Data.
                        query = pbdb.buildQuery(insertReportColorQuery, objColor);

                    }

                    //
                    pbdb.execUpdateSQL(query);

                }
            } catch (Exception e) {
                logger.error("Exception: ", e);

            }
        }
    }

    public void insertExcelCellProperties(Container container, int reportId) {
        ExcelCellFormatGroup excelCellGroup = container.getExcelCellGroup();
        String insertExcelCellQuery = "";
        String nextQry = "";
        ColorCodeTransferObject[] colorTransObj = null;
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = new PbReturnObject();
        Object[] objCell = null;
        String query = "";

        if (excelCellGroup != null) {
            ExcelCellFormatTransferObject[] transObjArr = excelCellGroup.getExcelCellsTransObject();
            for (ExcelCellFormatTransferObject transObj : transObjArr) {
                try {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        insertExcelCellQuery = getResourceBundle().getString("insertExcelCellQuery");
                        objCell = new Object[5];
                        objCell[0] = reportId;
                        objCell[1] = transObj.getRowViewByValues();
                        objCell[2] = transObj.getColViewByValues();
                        objCell[3] = transObj.getParameter();
                        objCell[4] = transObj.getExcelCellProps();
                        query = pbdb.buildQuery(insertExcelCellQuery, objCell);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.ORACLE)) {
                        nextQry = "SELECT prg_ar_excel_cell_props_seq.nextval Excel_Cell_id FROM dual ";
                        insertExcelCellQuery = getResourceBundle().getString("insertExcelCellQueryForOracle");
                        //
                        returnObj = pbdb.execSelectSQL(nextQry);
                        objCell = new Object[6];

                        objCell[0] = returnObj.getFieldValueInt(0, 0);
                        objCell[1] = reportId;
                        objCell[2] = transObj.getRowViewByValues();
                        objCell[3] = transObj.getColViewByValues();
                        objCell[4] = transObj.getParameter();
                        objCell[5] = transObj.getExcelCellProps();
                        query = pbdb.buildQuery(insertExcelCellQuery, objCell);
                    }
                    //
                    pbdb.execUpdateSQL(query);
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                }
                logger.info("successful");
            }
        }

    }

    public void insertExcelColumns(Container container, int reportId, boolean delete) {
        ExcelColumnGroup excelColGroup = container.getExcelColumnGroup();
        String insertExcelColQuery = "";
        String nextQry = "";
        ColorCodeTransferObject[] colorTransObj = null;
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj = new PbReturnObject();
        Object[] objCol = null;
        String query = "";

        if (delete) {
            ArrayList queries = new ArrayList();
            String deleteExcelColQuery = "delete from PRG_AR_EXCEL_COLUMNS where report_id=" + reportId;
            queries.add(deleteExcelColQuery);
            try {
                executeMultiple(queries);
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
            logger.info(" successful");
        }

        if (excelColGroup != null) {
            List<ExcelColumnTransferObject> transObjLst = excelColGroup.getExcelColumnsTransObject();
            for (ExcelColumnTransferObject transObj : transObjLst) {
                try {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        insertExcelColQuery = getResourceBundle().getString("insertExcelColQuery");
                        objCol = new Object[6];
                        objCol[0] = reportId;
                        objCol[1] = transObj.getMeasureId();
                        objCol[2] = transObj.getRowViewByValues();
                        objCol[3] = transObj.getColViewByValues();
                        objCol[4] = transObj.getParameter();
                        objCol[5] = transObj.getExcelColumnData();
                        query = pbdb.buildQuery(insertExcelColQuery, objCol);
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.ORACLE)) {
                        nextQry = "SELECT prg_ar_excel_cols_seq.nextval Excel_Col_id FROM dual ";
                        insertExcelColQuery = getResourceBundle().getString("insertExcelColQueryForOracle");
                        //
                        returnObj = pbdb.execSelectSQL(nextQry);
                        objCol = new Object[7];

                        objCol[0] = returnObj.getFieldValueInt(0, 0);
                        objCol[1] = reportId;
                        objCol[2] = transObj.getMeasureId();
                        objCol[3] = transObj.getRowViewByValues();
                        objCol[4] = transObj.getColViewByValues();
                        objCol[5] = transObj.getParameter();
                        objCol[6] = transObj.getExcelColumnData();
                        query = pbdb.buildQuery(insertExcelColQuery, objCol);
                    }

                    pbdb.execUpdateSQL(query);
                } catch (SQLException ex) {
                   logger.error("Exception", ex);
                } catch (Exception ex) {
                   logger.error("Exception", ex);
                }
            }
        }
    }

    public void insertRunTimeExcelColumn(Container container, RunTimeExcelColumn excelCol) {
        String reportId = container.getReportId();
        String colId = excelCol.getMeasureId();
        String colName = excelCol.getMeasureName();
        int seq = container.getDisplayColumns().indexOf(colId);
        String colType = RTMeasureElement.EXCELCOLUMN.getColumnType();
        ArrayList queries = new ArrayList();
        PbDb pbdb = new PbDb();
        PbReturnObject returnObj;

        try {
            String selQuery = "select rep_tab_id from PRG_AR_REPORT_TABLE_DETAILS where report_id=" + reportId;
            returnObj = pbdb.execSelectSQL(selQuery);
            int tableId = returnObj.getFieldValueInt(0, 0);

            String updateQuery = "update PRG_AR_REPORT_TABLE_DETAILS set disp_seq=disp_seq+1 where report_id=" + reportId + " and disp_seq>=" + seq;
            String insertQuery;
            queries.add(updateQuery);

            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                insertQuery = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,run_time_formula, runtime_measure_id) "
                        + " values (" + reportId + "," + tableId + ",'" + colName + "','" + seq + "', '" + colType + "','" + colId + "')";
            } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                insertQuery = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,run_time_formula, runtime_measure_id) "
                        + " values (" + reportId + "," + tableId + ",'" + colName + "','" + seq + "', '" + colType + "','" + colId + "')";
            } else {
                insertQuery = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,COL_NAME,DISP_SEQ,run_time_formula,runtime_measure_id) "
                        + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'"
                        + reportId + "'," + tableId + ",'" + colName + "','" + seq + "','" + colType + "','" + colId + "')";
            }
            queries.add(insertQuery);

            executeMultiple(queries);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
    }

    public boolean saveReport(ArrayList alist) {
        boolean result = false;
        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                result = executeMultiple(alist);
            } else {
                result = execMultiple(alist);
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return result;
    }

    public void saveWhatifDetails(int reportId, String whatIfxml) {
        String insertPrgArWhatifQuery = getResourceBundle().getString("insertPrgArWhatifQuery");
        Object object[] = new Object[2];
        String finalQuery = "";
        try {
            object[0] = reportId;
            object[1] = whatIfxml;
            finalQuery = super.buildQuery(insertPrgArWhatifQuery, object);
            //
            execModifySQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    /**
     * Get the Report Id of the Last Inserted Report in PRG_AR_REPORT_MASTER
     *
     * @return
     */
    public int getLastInsertedReportId() {
//        ProgenLog.log(ProgenLog.FINE, this, "getLastInsertedReportId", "Enter");
        logger.info("Enter ");
        String sqlQuery = getResourceBundle().getString("getLastReportId");
        PbReturnObject retObj = null;
        int reportId = -1;
        try {
            retObj = execSelectSQL(sqlQuery);
            if (retObj.getRowCount() > 0) {
                reportId = retObj.getFieldValueInt(0, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getLastInsertedReportId", "Exit reportId " + reportId);
        logger.info("Exit reportId " + reportId);
        return reportId;
    }

    public int getSequence(String sequence) {
        String sqlQuery = getResourceBundle().getString("getSequenceNumber");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] seq = new Object[1];
        int seqnum = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            seq[0] = sequence;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            seq[0] = sequence;
        } else {
            seq[0] = sequence + ".nextval";
        }
        try {

            finalQuery = buildQuery(sqlQuery, seq);

            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        logger.info(" successful");
        seqnum = retObj.getFieldValueInt(0, 0);
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            seqnum = seqnum + 1;
        }
        return seqnum;
    }

    public PbReturnObject getParameters(String str1, String str2) {
        String sqlQuery = getResourceBundle().getString("getParameters");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] params = new Object[2];
        params[0] = str1;
        params[1] = str2;
        try {
            finalQuery = buildQuery(sqlQuery, params);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        logger.info(" successful");
        return retObj;
    }

    public PbReturnObject getParamDetails(int reportId) {
        String sqlQuery = getResourceBundle().getString("getParamDetails");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] params = new Object[1];
        params[0] = reportId;
        try {
            finalQuery = buildQuery(sqlQuery, params);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        logger.info("successful");
        return retObj;

    }

    public void insertReportParamDetails(ArrayList queries) {
        try {
            executeMultiple(queries);
        } catch (Exception e) {

            logger.error("Exception: ", e);
        }
        logger.info("successful");
    }

    public void insertReportViews(ArrayList views) {
        try {
            executeMultiple(views);
        } catch (Exception e) {
        }
    }

    public void insertReportTableMaster(String query) {
        try {
            execModifySQL(query);
        } catch (Exception e) {
            logger.error("Exception: ", e);

        }
    }

    public PbReturnObject getQueryDetails(int reportId) {
        String sqlQuery = getResourceBundle().getString("getQueryDetails");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] params = new Object[1];
        params[0] = reportId;
        try {
            finalQuery = buildQuery(sqlQuery, params);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        logger.info("successful");
        return retObj;

    }

    public void insertReportTableDetails(ArrayList a) {
        try {
            executeMultiple(a);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void insertGraphDetails(ArrayList a) {
        try {
            executeMultiple(a);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

//    public PbReturnObject checkReportName(String PbUserId, String roleid) {
//        String query = getResourceBundle().getString("getReportNames");
//        Object obj[] = new Object[2];
//        obj[0] = PbUserId;
//        obj[1] = roleid;
//        PbReturnObject retObj = null;
//        query = buildQuery(query, obj);
//        try {
//            retObj = execSelectSQL(query);
//        } catch (Exception e) {
//            logger.error("Exception:", e);
//        }
//
//        return retObj;
//    }
     public boolean checkReportNameExists(String reportName, String PbUserId, String roleid) {
        String query = getResourceBundle().getString("checkReportName");
        boolean exists = false;
        Object obj[] = new Object[3];
        obj[0] = PbUserId;
        obj[1] = roleid;
        obj[2] = reportName;
        PbReturnObject retObj = null;
        query = buildQuery(query, obj);
        try {
            retObj = execSelectSQL(query);
            if (retObj.getRowCount() > 0) {
                exists = true;
            }else{
               String Query1 = "select * from prg_ar_report_master where report_name = '&'";
               Object obj1[] = new Object[1];
               obj1[0] = reportName;
               query = buildQuery(Query1, obj1);
                try {
                    retObj = null;
                  retObj = execSelectSQL(query);  
                  if (retObj.getRowCount() > 0) {
                exists = true;
                   }
                }catch (Exception e) {
            e.printStackTrace();
        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    public PbReturnObject getGraphTypes() {
        String sqlQuery = getResourceBundle().getString("getGraphTypes");
        PbReturnObject retObj = null;

        try {

            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }

        return retObj;

    }

    public PbReturnObject getGraphSizes() {
        String sqlQuery = getResourceBundle().getString("getGraphSizes");
        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getGraphTypeClass() {
        String sqlQuery = getResourceBundle().getString("getGraphTypeClass");
        PbReturnObject retObj = null;
        try {

            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getGraphSizeAxis() {
        String sqlQuery = getResourceBundle().getString("getGraphSizeAxis");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }
    //added by santhosh.kumar on 06-02-2010

    public PbReturnObject getGraphSizeAxisFx() {
        String sqlQuery = getResourceBundle().getString("getGraphSizeAxisFx");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }
    //getGraphSizeAxisFx

    public PbReturnObject getAllReports() {
        String sqlQuery = getResourceBundle().getString("getAllReports");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getAllReportshome(String userId) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = null;
        if (PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "DASHBOARDVIEWER", Integer.parseInt(userId))) {
            sqlQuery = getResourceBundle().getString("getAllRepsAndDashboards");
        } else {
            sqlQuery = getResourceBundle().getString("getAllReportshome");
        }

        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder(200);

        PbReturnObject retObj = null;
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, "FOLDER_ID");
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, "FOLDER_ID"));
                }
//                folderString = folderString.substring(1);
                folderString = new StringBuilder(folderString.substring(1));
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            String finalQuery = buildQuery(sqlQuery, values);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getAllDashs() {
        String sqlQuery = getResourceBundle().getString("getAllDashs");

        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public PbReturnObject getAllUsers() {
        String sqlQuery = getResourceBundle().getString("getAllUsers");

        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public PbReturnObject getAllreps() {
        String sqlQuery = getResourceBundle().getString("getAllreps");

        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public PbReturnObject getAllDashsNavi(String userId) {
        String sqlQuery = getResourceBundle().getString("getAllDashsNavi");
        Object obj[] = new Object[1];
        obj[0] = userId;
        String finalQuery = buildQuery(sqlQuery, obj);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public PbReturnObject getAllOneViewBys(String userId) {
//          String finalQuery = "SELECT * FROM ONEVIEW_NAME_ID order by viewid";
        Object obj[] = new Object[1];
        obj[0] = userId;
        String finalQuery = getResourceBundle().getString("getOneviews");
        String finalQuery1 = buildQuery(finalQuery, obj);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery1);

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return retObj;
    }

    public PbReturnObject getAllIcals(String userId) {
//          String finalQuery = "SELECT * FROM ONEVIEW_NAME_ID order by viewid";
        String finalQuery = getResourceBundle().getString("getAllICals");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return retObj;
    }

    public PbReturnObject getAllrepsNavi(String userId) {
        String sqlQuery = getResourceBundle().getString("getAllrepsNavi");
        Object obj[] = new Object[1];
        obj[0] = userId;

        String finalQuery = buildQuery(sqlQuery, obj);
        ////////.println("finalQuery"+finalQuery);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public void insertTimeDimensions(int rep_time_id, ArrayList list, int reportId) {
        String sqlQuery = getResourceBundle().getString("insertReportTime");
        String finalQuery = "";
        Object[] obj = new Object[4];
        obj[0] = rep_time_id;
        obj[1] = list.get(1);
        obj[2] = list.get(0);
        obj[3] = reportId;
        finalQuery = buildQuery(sqlQuery, obj);

        try {
            execModifySQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public void insertTimeDimDetails(int rep_time_id, HashMap map) {
        String sqlQuery = getResourceBundle().getString("insertReportTimeDetails");
        String finalQuery = "";
        ArrayList queries = new ArrayList();

        Set details = map.keySet();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            Object[] obj = new Object[5];
            String key = (String) it.next();
            ArrayList timeDetails = (ArrayList) map.get(key);
            String column_name = (String) timeDetails.get(2);
            String column_type = key;
            int sequence = Integer.parseInt((String) timeDetails.get(3));
            int from_sequence = Integer.parseInt((String) timeDetails.get(4));
            obj[0] = rep_time_id;
            obj[1] = column_name;
            obj[2] = column_type;
            obj[3] = sequence;
            obj[4] = from_sequence;
            finalQuery = buildQuery(sqlQuery, obj);
            queries.add(finalQuery);

            timeDetails = null;
            obj = null;
        }

        try {
            executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }

    public String getUserFolderMinTimeLevel(String foldersIds) throws Exception {

        String minTimeLevel = "";

        String qry = getResourceBundle().getString("getUserFolderMinTimeLevel");

        Object obj[] = new Object[1];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "null";
        }

        String finalQry = buildQuery(qry, obj);
        //      //////.println("finalQry++"+finalQry);
        PbReturnObject pbro = execSelectSQL(finalQry);

        minTimeLevel = String.valueOf(pbro.getFieldValueInt(0, 0));

        return minTimeLevel;
    }

    public String getUserFoldersByReportId(String reportId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = reportId;
        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByReportId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                FolderId = retObj.getFieldValueString(i, colNames[0]);
                FolderName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                outerBuffer.append("<input type='checkbox' checked disabled name='userfldsList' id='" + FolderId + "' onclick='javascript:getUserDims()' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getReportUserFolders(String reportId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = reportId;

//        String FolderId = "";
        StringBuilder FolderId = new StringBuilder(200);
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByReportId");

        try {

            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
//                FolderId = FolderId + "," + retObj.getFieldValueString(i, colNames[0]);
                FolderId.append(",").append(retObj.getFieldValueString(i, colNames[0]));
            }
//            if (!(FolderId.equalsIgnoreCase(""))) {
//                FolderId = FolderId.substring(1);
//            }
            if (FolderId.length() > 0) {
                FolderId = new StringBuilder(FolderId.substring(1));
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return FolderId.toString();
    }

    public String getBuckets(String foldersIds) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object[] obj = null;

        String bucketId = null;
        String bucketName = null;

        String bucketColName = null;
        String bucketColId = null;
        String getBucketsQuery = getResourceBundle().getString("getBucketsInfo");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            obj = new Object[1];
            if (foldersIds != null && !foldersIds.equalsIgnoreCase("")) {
                obj[0] = foldersIds;
            } else {
                obj[0] = "''";
            }
            finalQuery = buildQuery(getBucketsQuery, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            if (retObj != null && retObj.getRowCount() != 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {

                    bucketColId = retObj.getFieldValueString(i, colNames[2]);
                    bucketColName = retObj.getFieldValueString(i, colNames[3]);

                    if (bucketId == null) {

                        bucketId = retObj.getFieldValueString(i, colNames[0]);
                        bucketName = retObj.getFieldValueString(i, colNames[1]);

                        outerBuffer.append("<li class='closed' id='" + bucketId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                        outerBuffer.append("<span style='font-family:verdana;'>" + bucketName + "</span>");
                        outerBuffer.append("<ul id='dimName-" + bucketName + "'>");

                        outerBuffer.append("<li class='closed' id='" + bucketColId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");

                        outerBuffer.append("<span id='elmnt-" + bucketColId + "' style='font-family:verdana;'>" + bucketColName + "</span>");

                        outerBuffer.append("</li>");

                    } else if (bucketId.equalsIgnoreCase(retObj.getFieldValueString(i, colNames[0]))) {

                        outerBuffer.append("<li class='closed' id='" + bucketColId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                        outerBuffer.append("<span id='elmnt-" + bucketColId + "' style='font-family:verdana;'>" + bucketColName + "</span>");
                        outerBuffer.append("</li>");

                    } else {

                        outerBuffer.append("</ul>");
                        outerBuffer.append("</li>");

                        bucketId = retObj.getFieldValueString(i, colNames[0]);
                        bucketName = retObj.getFieldValueString(i, colNames[1]);

                        outerBuffer.append("<li class='closed' id='" + bucketId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                        outerBuffer.append("<span style='font-family:verdana;'>" + bucketName + "</span>");
                        outerBuffer.append("<ul id='dimName-" + bucketName + "'>");

                        outerBuffer.append("<li class='closed' id='" + bucketColId + "'>");
                        outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                        outerBuffer.append("<span id='elmnt-" + bucketColId + "' style='font-family:verdana;'>" + bucketColName + "</span>");
                        outerBuffer.append("</li>");

                    }
                }

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
           logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }

    //code written by praveen on 18/11/2009
    public PbReturnObject getUserReportsAndDashboards(String userId) {
        //String sqlQuery = resBundle.getString("getAllReports");
        String sqlQuery = getResourceBundle().getString("getUserReportsAndDashboards");
        PbReturnObject retObj = null;
        Object[] values = new Object[1];
        values[0] = userId;
        try {
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getUserDashboards(String userId) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserDashboards");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder(300);
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, "FOLDER_ID");
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, "FOLDER_ID"));
                }
                folderString = new StringBuilder(folderString.substring(1));
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        retObj.writeString();
        return retObj;
    }

    public PbReturnObject getUserReports(String userId) {
//        ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "Enter userId " + userId);
        logger.info("Enter userId " + userId);
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserReports");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder();
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "foldersQuery " + foldersQuery);
            logger.info("foldersQuery " + foldersQuery);
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, 0));
                }
                folderString = new StringBuilder(folderString.substring(1));
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
//            ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "sqlQuery " + sqlQuery);
            logger.info("sqlQuery " + sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "Exit");
        logger.info("Exit");
        return retObj;
    }

    public PbReturnObject getrepStudioSort(String userId, String sortOption, String selectValue) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getrepStudioSort");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder();
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, 0));
                }
//                folderString = folderString.substring(1);
                folderString = new StringBuilder(folderString.substring(1));
            }

            Object[] values = new Object[5];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            values[3] = selectValue;
            values[4] = sortOption;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) { 
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getrepPurgeSort(String userId, String sortOption, String selectValue) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getrepPurgeSort");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder();
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, 0));
                }
//                folderString = folderString.substring(1);
                folderString = new StringBuilder(folderString.substring(1));
            }

            Object[] values = new Object[5];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            values[3] = selectValue;
            values[4] = sortOption;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getallReportSort(String userId, String sortOption, String selectValue) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getallReportSort");

        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder();

        PbReturnObject retObj = null;
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, 0));
                }
//                folderString = folderString.substring(1);
                folderString = new StringBuilder(folderString.substring(1));
            }
            Object[] values = new Object[5];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            values[3] = selectValue;
            values[4] = sortOption;
            String finalQuery = buildQuery(sqlQuery, values);

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getDashSort(String userId, String sortOption, String selectValue) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getDashSort");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
//        String folderString = "";
        StringBuilder folderString = new StringBuilder();
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
//                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                    folderString.append(",").append(retFolderObj.getFieldValueString(i, 0));
                }
//                folderString = folderString.substring(1);
                folderString = new StringBuilder(folderString.substring(1));
            }
            Object[] values = new Object[5];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            values[3] = selectValue;
            values[4] = sortOption;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public void DeleteUserdashboards(String userId, String deleteids) {
        //String sqlQuery = resBundle.getString("getAllDashs");
        String sqlQuery = getResourceBundle().getString("DeleteUserdashboards");
        String deletedashquery = getResourceBundle().getString("DeleteUserdashboardsdetails");

        PbReturnObject retObj = null;
        Object[] values = new Object[1];
        values[0] = deleteids;
//        values[1] = deleteids;
        try {
            sqlQuery = buildQuery(sqlQuery, values);
            deletedashquery = buildQuery(deletedashquery, values);
            execModifySQL(sqlQuery);
            execModifySQL(deletedashquery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
    }

    public void DeleteUserReports(String userId, String deleterepids) {
        //String sqlQuery = resBundle.getString("getAllreps");
        String sqlQuery = getResourceBundle().getString("DeleteUserReports");
        String DelRepUserQry = getResourceBundle().getString("getUsersbyReportId");
        String delSnapshotQry = getResourceBundle().getString("deleteSnapshot");

        PbReturnObject retObj = null;
        String DelRepUserSql = "";
        String[] delRepIds = deleterepids.split(",");
        Object[] delVal = new Object[1];
        Object[] values = new Object[2];
        values[0] = deleterepids;
        values[1] = userId;
        try {
            for (int i = 0; i < delRepIds.length; i++) {
                delVal[0] = delRepIds[i];
                DelRepUserSql = buildQuery(DelRepUserQry, delVal);
                retObj = execSelectSQL(DelRepUserSql);
                if (retObj.getRowCount() > 1) {
                    sqlQuery = buildQuery(sqlQuery, values);
                    execModifySQL(sqlQuery);
                    delSnapshotQry = buildQuery(delSnapshotQry, values);
                    execModifySQL(delSnapshotQry);
                } else {
                    PurgeReportsDAO(userId, delRepIds[i]);
                }
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        } catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
    }

    public void clearCache(String[] reportIds) {
        for (String reportId : reportIds) {
            ReportCacheManager.MANAGER.clearReportQueryCache(reportId);
        }
    }

    public void PurgeReportsDAO(String userId, String purgerepids) {
        String[] purgeReps = purgerepids.split(",");
        ArrayList delList = new ArrayList();
        PbReturnObject retObj = null;
//        Object[] values = new Object[2];
//        values[1] = purgerepids;
        try {
            delList.add("delete from prg_ar_report_master where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_details where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_param_details where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_view_by_details where view_by_id in(select view_by_id from prg_ar_report_view_by_master where report_id in(" + purgerepids + "))");
            delList.add("delete from prg_ar_report_view_by_master where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_time_detail where rep_time_id in(select rep_time_id from prg_ar_report_time where report_id in(" + purgerepids + "))");
            delList.add("delete from prg_ar_report_time where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_table_details where rep_tab_id in(select rep_tab_id from prg_ar_report_table_master where report_id in(" + purgerepids + "))");
            delList.add("delete from prg_ar_report_table_master where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_query_detail where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_graph_details where graph_id in(select graph_id from prg_ar_graph_master where report_id in(" + purgerepids + "))");
            delList.add("delete from prg_ar_graph_master where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_parents where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_user_stickynote where report_id in(" + purgerepids + ") and user_id = " + userId);
            delList.add("delete from prg_ar_parameter_security where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_report_map_details where report_id in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_WHATIFS where report_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_dimension_segment where report_id in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_EXCEL_CELL_PROPS where report_id in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_EXCEL_COLUMNS where report_id in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_USER_REPORTS where report_id in(" + purgerepids + ")");

            delList.add("delete from prg_ar_kpi_details where  kpi_master_id in(select kpi_master_id from prg_ar_dashboard_details where  dashboard_id in(" + purgerepids + "))");
            delList.add("delete from prg_ar_kpi_graph_details where dashboard_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_dashboard_details where dashboard_id in(" + purgerepids + ")");
            delList.add("delete from dashboard_target_kpi_value where dashboard_id in(" + purgerepids + ")");
            delList.add("delete from prg_ar_user_reports where report_id in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_PERSONALIZED_REPORTS where PRG_REPORT_ID in(" + purgerepids + ")");
            delList.add("delete from ACCOUNT_REPORT where REPORT_ID in(" + purgerepids + ")");
            delList.add("delete from PR_AR_REPORT_MESSAGE where REPORT_ID in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_REPORT_BUCKETS where REPORT_ID in(" + purgerepids + ")");
            delList.add("delete from PRG_AR_DATA_SNAPSHOTS where REPORT_ID in(" + purgerepids + ")");

            executeMultiple(delList);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        clearCache(purgeReps);
    }

    //added by chiranjeevi for delete sticky from list
    public void DeleteSticky(String userId, String deleteStickId) {
        String sqlQuery = getResourceBundle().getString("DeleteSticky");

        PbReturnObject retObj = null;
        Object[] values = new Object[2];
        values[0] = deleteStickId;
        values[1] = userId;
        try {
            sqlQuery = buildQuery(sqlQuery, values);

            execModifySQL(sqlQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
    }

    public void saveParameterSecurity(String reportId, ReportParameterSecurity security) {
        //String sqlQuery = resBundle.getString("getAllreps");
        // String sqlQuery = resBundle.getString("addParamSecurity");
        try {
            OraclePreparedStatement opstmt = null;
            Connection connection = null;

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                //String addParameterSecurity = "INSERT INTO PRG_AR_PARAMETER_SECURITY(SECURITY_ID, ELEMENT_ID, MEMBER_VALUE, REPORT_ID,MEMBER_ID) values(PR_AR_PARAMETER_SECURITY_SEQ.nextval,?,?,?,?)";
                String addParamSecurityQry = getResourceBundle().getString("addParamSecurity");
                Object[] paramVal = new Object[4];
                paramVal[0] = security.getParameteElementId();
                paramVal[1] = Joiner.on(",").join(security.getParamSecureValues());
                paramVal[2] = reportId;
                paramVal[3] = security.getMemberId();
                try {
                    String finalQuery = buildQuery(addParamSecurityQry, paramVal);
                    execModifySQL(finalQuery);
                } catch (Exception exp) {
                    logger.error("Exception: ", exp);
                }
            } else {
                connection = ProgenConnection.getInstance().getConnection();
                String addParameterSecurity = "INSERT INTO PRG_AR_PARAMETER_SECURITY(SECURITY_ID, ELEMENT_ID, MEMBER_VALUE, REPORT_ID,MEMBER_ID) values(PR_AR_PARAMETER_SECURITY_SEQ.nextval,?,?,?,?)";
                opstmt = (OraclePreparedStatement) connection.prepareStatement(addParameterSecurity);
                opstmt.setString(1, security.getParameteElementId());
                opstmt.setStringForClob(2, Joiner.on(",").join(security.getParamSecureValues()));
                opstmt.setString(3, reportId);
                opstmt.setString(4, security.getMemberId());

                int rows = opstmt.executeUpdate();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    //added by Mahesh for Favourite Parameters on 29/12/2009
    public PbReturnObject checkParamName() {
        String query = getResourceBundle().getString("getParamNames");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
           logger.error("Exception", ex);
        }

        return retObj;
    }

    public void insertFavParams(String favname, String favdesc, String folderids, String reportId, String userid, String paramswithTime) {
        try {
            String paramsTime[] = paramswithTime.split(",");
            ArrayList favqueries = new ArrayList();

            String insertFavParamsquery = getResourceBundle().getString("insertFavParams");
            String finalQuery = "";

            Object[] favParam = new Object[6];
            for (int i = 0; i < paramsTime.length; i++) {
                favParam[0] = userid;
                favParam[1] = reportId;
                favParam[2] = folderids;
                favParam[3] = favname;
                favParam[4] = favdesc;
                favParam[5] = paramsTime[i];
                finalQuery = buildQuery(insertFavParamsquery, favParam);
                favqueries.add(finalQuery);
            }
            executeMultiple(favqueries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    public String getFavParams(String foldersIds, String usrId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        PbReturnObject retObj1 = null;
        String finalQuery1 = null;
        String[] colNames1 = null;
        Object obj[] = new Object[2];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        obj[1] = usrId;
        String userId = "";
        String favName = "";
        String subFolderId = "";
        String elementId = "";
        String userId1 = "";
        String favName1 = "";
        String subFolderId1 = "";
        String sql = getResourceBundle().getString("getFavParams");
        String getFavParamNamessql = getResourceBundle().getString("getFavParamNames");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            // 
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            finalQuery1 = buildQuery(getFavParamNamessql, obj);
            retObj1 = execSelectSQL(finalQuery1);
            colNames1 = retObj1.getColumnNames();

            /*
             * String minTimeLevel = getUserFolderMinTimeLevel(foldersIds); if
             * (minTimeLevel.equals("5")) { outerBuffer.append("<li
             * class='closed' id='timeDimension-Period Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Period Basis'
             * style='font-family:verdana;'>Time-Period Basis</span>");
             * outerBuffer.append("</li>"); outerBuffer.append("<li
             * class='closed' id='timeDimension-Range Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Range Basis'
             * style='font-family:verdana;'>Time-Range Basis</span>");
             * outerBuffer.append("</li>"); } else if (minTimeLevel.equals("4"))
             * { outerBuffer.append("<li class='closed' id='timeDimension-Week
             * Basis'>"); outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Week Basis'
             * style='font-family:verdana;'>Time-Week Basis</span>");
             * outerBuffer.append("</li>"); } else if (minTimeLevel.equals("3"))
             * { outerBuffer.append("<li class='closed' id='timeDimension-Month
             * Basis'>"); outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Month Basis'
             * style='font-family:verdana;'>Time-Month Basis</span>");
             * outerBuffer.append("</li>"); outerBuffer.append("<li
             * class='closed' id='timeDimension-Compare Month Basis'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Compare Month Basis'
             * style='font-family:verdana;'>Time-Compare Month Basis</span>");
             * outerBuffer.append("</li>"); } else if (minTimeLevel.equals("2"))
             * { outerBuffer.append("<li class='closed'
             * id='timeDimension-Quarter Basis'>"); outerBuffer.append("<img
             * src='icons pinvoke/Timesetup.gif'></img>");
             * outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis'
             * style='font-family:verdana;'>Time-Quarter Basis</span>");
             * outerBuffer.append("</li>"); } else if (minTimeLevel.equals("1"))
             * { outerBuffer.append("<li class='closed' id='timeDimension-Year
             * Basis'>"); outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='elmnt-Time Dimension-Year Basis'
             * style='font-family:verdana;'>Time-Year Basis</span>");
             * outerBuffer.append("</li>"); }
             */
            for (int j = 0; j < retObj1.getRowCount(); j++) {
                userId = retObj1.getFieldValueString(j, colNames1[0]);
                subFolderId = retObj1.getFieldValueString(j, colNames1[1]);
                favName = retObj1.getFieldValueString(j, colNames1[2]);

                outerBuffer.append("<li class='closed' id='" + favName + "' onmouseup=\"delfavparam('" + favName + "')\">");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;' id='" + favName + "favouriteSpan' onmouseover=\"openContextMenu(this.id,'" + favName + "')\" >" + favName + "</span>");
                outerBuffer.append("<ul id='dimName-" + favName + "' >");

                for (int i = 0; i < retObj.getRowCount(); i++) {
                    userId1 = retObj.getFieldValueString(i, colNames[0]);
                    subFolderId1 = retObj.getFieldValueString(i, colNames[1]);
                    favName1 = retObj.getFieldValueString(i, colNames[2]);
                    elementId = retObj.getFieldValueString(i, colNames[3]);

                    if (favName.equalsIgnoreCase(favName1)) {

                        /*
                         * if (elementId.equalsIgnoreCase("AS_OF_DATE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Date</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("PRG_PERIOD_TYPE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Aggregation</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("PRG_COMPARE")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Compare With</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("AS_OF_DATE1")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>From Date</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id='" +
                         * elementId + "'>"); outerBuffer.append("<img
                         * src='icons pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>To Date</span>");
                         * outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE1")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Compare From
                         * Date</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Compare To
                         * Date</span>"); outerBuffer.append("</li>"); }else
                         * if(elementId.equalsIgnoreCase("CMP_AS_OF_DATE2")){
                         * outerBuffer.append("<li class='closed' id=" +
                         * elementId + ">"); outerBuffer.append("<img src='icons
                         * pinvoke/Timesetup.gif'></img>");
                         * outerBuffer.append("<span id=elmnt-" + elementId + "
                         * style='font-family:verdana;'>Compare To
                         * Date</span>"); outerBuffer.append("</li>"); }
                         */
                        outerBuffer.append(getFavParamMbrs(subFolderId, elementId));
                    }
                }
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getFavParamMbrs(String subFolderId, String elementId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = subFolderId;
        obj[1] = elementId;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getFavParamMbrs");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");

                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getNumberSymbol(String columnName) {
        HashMap NFMap = null;
        String nbrSymbol = "";
        if (this.getTableHashMap() != null) {
            if (this.getTableHashMap().get("NFMap") != null) {
                NFMap = (HashMap) this.getTableHashMap().get("NFMap");

                if (NFMap.get(columnName) != null) {
                    nbrSymbol = String.valueOf(NFMap.get(columnName));
                }
            }
        }
        return nbrSymbol;
    }

    public HashMap getTableHashMap() {
        return TableHashMap;
    }

    public void setTableHashMap(HashMap TableHashMap) {
        this.TableHashMap = TableHashMap;
    }

    public ArrayList getReportQryColNames() {
        return reportQryColNames;
    }

    public void setReportQryColNames(ArrayList reportQryColNames) {
        this.reportQryColNames = reportQryColNames;
    }

    public ArrayList getReportQryColTypes() {
        return reportQryColTypes;
    }

    public void setReportQryColTypes(ArrayList reportQryColTypes) {
        this.reportQryColTypes = reportQryColTypes;
    }

    public PbReturnObject getGraphTypeClassFx() {
        String sqlQuery = getResourceBundle().getString("getGraphTypeClassFx");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            retObj = null;
        }catch (Exception ex) {
           logger.error("Exception", ex);
            retObj = null;
        }
        return retObj;
    }

    public void editReportName(String reportId, String reportName, String reportDesc) {

        String updateReportNameQuery = getResourceBundle().getString("updateReportName");
        String updateReportNameQueryschedule = getResourceBundle().getString("updateReportNameforschedule");

        ArrayList queries = new ArrayList();
        Object[] repMaster = null;
        Object[] repMaster2 = null;
        String finalQuery = "";
        String finalQuery1 = "";
        String[] repNm = null;
        if (reportName.contains("&")) {
            repNm = reportName.split("&");
            reportName = repNm[0] + "&'||'" + repNm[1];
        } else if (reportName.contains("+")) {
            repNm = reportName.split("+");
            reportName = repNm[0] + "+'||'" + repNm[1];
        } else if (reportName.contains("#")) {
            repNm = reportName.split("#");
            reportName = repNm[0] + "#'||'" + repNm[1];
        } else if (reportName.contains("%")) {
            repNm = reportName.split("%");
            reportName = repNm[0] + "%'||'" + repNm[1];
        } else if (reportName.contains("@")) {
            repNm = reportName.split("@");
            reportName = repNm[0] + "@'||'" + repNm[1];
        }
        try {
            repMaster2 = new Object[2];
            repMaster = new Object[4];
            repMaster[0] = reportName;
            repMaster[1] = reportDesc;
            repMaster[2] = reportDesc;
            repMaster[3] = reportId;
            repMaster2[0] = reportName;
            repMaster2[1] = reportId;
            finalQuery = buildQuery(updateReportNameQuery, repMaster);
            finalQuery1 = buildQuery(updateReportNameQueryschedule, repMaster2);
            execModifySQL(finalQuery);
            execModifySQL(finalQuery1);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    ///insert the reports for account and the users added on 28jan 10 by susheela
    public void addReportForCompany(String repId, String orgIds, String loginuserId) throws Exception {
        ArrayList insertList = new ArrayList();
        String accounts[] = orgIds.split(",");
        String userAccQuery = "select distinct pu_id from prg_ar_users where account_type in(" + orgIds + ") and pu_Id!=" + loginuserId;
        PbReturnObject userObj = new PbReturnObject();
        String insertQ = "insert into prg_ar_user_reports(USER_REP_ID,USER_ID,REPORT_ID,PUR_REPORT_SEQUENCE,PUR_FAV_REPORT,PUR_CUST_REPORT_NAME)"
                + " values(PRG_AR_USER_REPORTS_SEQ.nextval,'&','&','','N','')";

        String orgReportQ = "insert into account_report(org_id,report_id) values('&','&')";
        if (orgIds.length() > 0) {
            userObj = execSelectSQL(userAccQuery);
        }

// to insert for company
        for (int m = 0; m < accounts.length; m++) {
            Object orgObj[] = new Object[2];
            orgObj[0] = accounts[m];
            orgObj[1] = repId;
            String finorgReportQ = buildQuery(orgReportQ, orgObj);

            insertList.add(finorgReportQ);
        }
// to insert for company's users
        for (int n = 0; n < userObj.getRowCount(); n++) {
            String userId = userObj.getFieldValueString(n, "PU_ID");
            Object userInserOb[] = new Object[2];
            userInserOb[0] = userId;
            userInserOb[1] = repId;
            String fininsertQ = buildQuery(insertQ, userInserOb);

            insertList.add(fininsertQ);
        }
        String userId = loginuserId;
        Object userInserOb[] = new Object[2];
        userInserOb[0] = loginuserId;
        userInserOb[1] = repId;
        String fininsertQ = buildQuery(insertQ, userInserOb);

        insertList.add(fininsertQ);

        try {
            executeMultiple(insertList);
        } catch (Exception d) {
            logger.error("Exception: ", d);
        }
    }

    public String removeLastCommas(String str) {

        ////////.println("str"+str);
        if (str.length() > 0) {
            str = str.replaceAll(" ", "");
        }
        if (str.length() > 0) {
            while (str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }

        }

        return str;
    }

    public boolean deleteReport(ArrayList alist) {
        boolean result = false;
        try {
            result = executeMultiple(alist);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return result;
    }

    public boolean DeleteReport(Integer reportId) throws Exception {
        String[] reportIds = new String[1];
        reportIds[0] = reportId.toString();
        ArrayList queries = new ArrayList();
        boolean result = false;
        String deleteReportGraphDetailsQuery = "delete  from PRG_AR_GRAPH_DETAILS  where graph_id in ( select DISTINCT a.graph_id from PRG_AR_GRAPH_DETAILS a ,PRG_AR_GRAPH_MASTER b where a.graph_id= b.graph_id and b.report_id ='" + reportId + "' ) ";
        String deleteReportGraphMasterQuery = "delete from PRG_AR_GRAPH_MASTER where report_id ='" + reportId + "' ";
        String deleteReportTableDetailsQuery = "delete from PRG_AR_REPORT_TABLE_DETAILS where report_id='" + reportId + "'";
        String deleteReportTableMasterQuery = "delete from PRG_AR_REPORT_TABLE_MASTER where report_id='" + reportId + "'";
        String deleteReportQueryDetailsQuery = "delete from PRG_AR_QUERY_DETAIL where report_id ='" + reportId + "'";
        String deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in (select DISTINCT a.view_by_id from PRG_AR_REPORT_VIEW_BY_DETAILS a,PRG_AR_REPORT_VIEW_BY_MASTER b where a.view_by_id= b.view_by_id and b.report_id=" + reportId + ")";
        String deleteReportViewByMasterQuery = "delete from PRG_AR_REPORT_VIEW_BY_MASTER where report_id ='" + reportId + "'";
        //ned to delete from viewby details aslo
        String deleteReportTimeDetailsQuery = "delete from PRG_AR_REPORT_TIME_DETAIL where rep_time_id in ( select rep_time_id from PRG_AR_REPORT_TIME where report_id='" + reportId + "')";
        String deleteReportTimeDimensionsQuery = "delete from PRG_AR_REPORT_TIME where report_id='" + reportId + "'";
        String deleteReportParamDetailsQuery = "delete from PRG_AR_REPORT_PARAM_DETAILS where report_id='" + reportId + "'";
        String deleteReportDetailsQuery = "delete from PRG_AR_REPORT_DETAILS where report_id='" + reportId + "'";
        String deleteReportMasterQuery = "delete from PRG_AR_REPORT_MASTER where report_id='" + reportId + "'";
        String deletewhatifsQuery = "delete from PRG_AR_WHATIFS where report_id='" + reportId + "'";
        String deleteSegmentValues = "delete from PRG_AR_DIMENSION_SEGMENT where report_id=" + reportId + "";
        String deleteExcelCellQuery = "delete from PRG_AR_EXCEL_CELL_PROPS where report_id=" + reportId;
        String deleteExcelColQuery = "delete from PRG_AR_EXCEL_COLUMNS where report_id=" + reportId;
//        String deleteReportAssg = "delete from PRG_AR_USER_REPORTS where REPORT_ID=" + reportId;
        String deleteReportParents = "delete from PRG_AR_REPORT_PARENTS where REPORT_ID=" + reportId;
        String deleteMapDetails = "delete from prg_ar_report_map_details where REPORT_ID=" + reportId;
        String deleteParamSecurity = "delete from PRG_AR_PARAMETER_SECURITY where REPORT_ID=" + reportId;
        String deleteReportMapDetails = "delete from PRG_AR_REPORT_MAP_DETAILS where REPORT_ID=" + reportId;
        String deleteReportColors = "delete from PRG_AR_REPORT_COLORS where REPORT_ID=" + reportId;
        try {
            queries.add(deleteReportGraphDetailsQuery);
            queries.add(deleteReportGraphMasterQuery);
            queries.add(deleteReportTableDetailsQuery);
            queries.add(deleteReportTableMasterQuery);
            queries.add(deleteReportQueryDetailsQuery);
            queries.add(deleteReportViewByDetailsQuery);
            queries.add(deleteReportViewByMasterQuery);
            queries.add(deleteReportTimeDimensionsQuery);
            queries.add(deleteReportTimeDetailsQuery);
            queries.add(deleteReportParamDetailsQuery);
            queries.add(deleteReportDetailsQuery);
            queries.add(deleteReportMasterQuery);
            queries.add(deletewhatifsQuery);
            queries.add(deleteSegmentValues);
            queries.add(deleteExcelCellQuery);
            queries.add(deleteExcelColQuery);
            // queries.add(deleteReportAssg);
            queries.add(deleteReportParents);
            queries.add(deleteMapDetails);
            queries.add(deleteReportColors);
            result = executeMultiple(queries);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        clearCache(reportIds);
        return result;
    }

    public String buildColorCodeXML(HashMap ColorCodeMap, String columnDisplayName) {
        HashMap tempHashMap = null;
        String[] colorCodes = null;
        String[] operators = null;
        String[] sValues = null;
        String[] eValues = null;
        StringBuffer SBColorCodeXML = new StringBuffer("");
        String[] strOperators = {"<", ">", "<=", ">=", "=", "!=", "<>"};
        String[] strOperatorsInWords = {"less than", "greater than", "less than equal to", "greater than equal to", "equal to", "not equal to", "between"};

        if (ColorCodeMap != null && ColorCodeMap.get(columnDisplayName) != null) {
            tempHashMap = (HashMap) ColorCodeMap.get(columnDisplayName);
            colorCodes = (String[]) tempHashMap.get("colorCodes");
            operators = (String[]) tempHashMap.get("operators");
            sValues = (String[]) tempHashMap.get("sValues");
            eValues = (String[]) tempHashMap.get("eValues");
            SBColorCodeXML.append("<color-grouping>");

            if (colorCodes != null) {
                SBColorCodeXML.append("<color-codes>");
                for (int i = 0; i < colorCodes.length; i++) {
                    SBColorCodeXML.append("<color-code>");
                    SBColorCodeXML.append(colorCodes[i]);
                    SBColorCodeXML.append("</color-code>");
                }
                SBColorCodeXML.append("</color-codes>");
            }
            if (operators != null) {
                SBColorCodeXML.append("<operators>");
                for (int i = 0; i < operators.length; i++) {
                    SBColorCodeXML.append("<operator>");
                    for (int j = 0; j < strOperators.length; j++) {
                        if (operators[i].equalsIgnoreCase(strOperators[j])) {
                            SBColorCodeXML.append(strOperatorsInWords[j]);
                            break;
                        }
                    }

                    SBColorCodeXML.append("</operator>");
                }
                SBColorCodeXML.append("</operators>");
            }
            if (sValues != null) {
                SBColorCodeXML.append("<sValues>");
                for (int i = 0; i < sValues.length; i++) {
                    SBColorCodeXML.append("<sValue>");
                    SBColorCodeXML.append(sValues[i]);
                    SBColorCodeXML.append("</sValue>");
                }
                SBColorCodeXML.append("</sValues>");
            }
            if (eValues != null) {
                SBColorCodeXML.append("<eValues>");
                for (int i = 0; i < eValues.length; i++) {
                    SBColorCodeXML.append("<eValue>");
                    SBColorCodeXML.append(eValues[i]);
                    SBColorCodeXML.append("</eValue>");
                }
                SBColorCodeXML.append("</eValues>");
            }

            SBColorCodeXML.append("</color-grouping>");
        }
        return SBColorCodeXML.toString();
    }

    public HashMap getParameterGroupMap() {
        return ParameterGroupMap;
    }

    public void setParameterGroupMap(HashMap ParameterGroupMap) {
        this.ParameterGroupMap = ParameterGroupMap;
    }

    //added by uday on 20-mar-2010
    public PbReturnObject getAllWhatIfReports(String userId) {
        String query = "select distinct wr.report_id,wr.report_name,wr.report_desc,wr.report_type from prg_ar_watif_reports wr, prg_ar_user_reports ur "
                + "where wr.report_id= ur.report_id and ur.user_id=" + userId + " and ur.is_what_if_report='Y' order by 2 ASC";
        ////////.println("query is: :" + query);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return retObj;
    }

    public PbReturnObject checkWhatIfScenarioName() {
        String query = "select * from prg_ar_watif_reports";
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
           logger.error("Exception", ex);
        }
        return retObj;
    }

    public String getWhatIfUserFoldersByUserId(String pbUserId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = null;

        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            obj = new Object[3];
            obj[0] = pbUserId;
            obj[1] = pbUserId;
            obj[2] = pbUserId;
            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() != 0) {
                colNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    FolderId = retObj.getFieldValueString(i, colNames[0]);
                    FolderName = retObj.getFieldValueString(i, colNames[1]);
                    outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                    outerBuffer.append("<input type='checkbox' name='userfldsList' id='" + FolderId + "' onclick='javascript:getWhatIfUserDims();' ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                    outerBuffer.append("</li>");
                }
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getWhatIfUserDims(String foldersIds, String userId) {
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        String finalQuery1 = null;
        String[] colNames1 = null;

        Object obj[] = new Object[1];
        Object obj1[] = new Object[2];
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj[0] = foldersIds;
        } else {
            obj[0] = "''";
        }
        if (foldersIds != null && !"".equalsIgnoreCase(foldersIds)) {
            obj1[0] = foldersIds;
        } else {
            obj1[0] = "''";
        }
        obj1[1] = userId;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        String busFolderId = "";
        String elementId = "";
        String sql = getResourceBundle().getString("getUserDims");
        String sql1 = getResourceBundle().getString("getFavParams");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
//added for Favourite parameters on 29-12-2009
            finalQuery1 = buildQuery(sql1, obj1);

            retObj1 = execSelectSQL(finalQuery1);
            colNames1 = retObj1.getColumnNames();

            /*
             * userId = retObj1.getFieldValueString(0, colNames1[0]);
             * busFolderId = retObj1.getFieldValueString(0, colNames1[1]);
             * favName = retObj1.getFieldValueString(0, colNames1[2]);
             * if(retObj1.getRowCount()>0){ outerBuffer.append("<li
             * class='closed' id='" + favName + "'>"); outerBuffer.append("<img
             * src='icons pinvoke/Dimension.png'></img>");
             * outerBuffer.append("<span style='font-family:verdana;'>" +
             * favName + "</span>"); outerBuffer.append("<ul
             * id='dimName-'"+favName+"'>"); for (int i = 0; i <
             * retObj1.getRowCount(); i++) { elementId =
             * retObj1.getFieldValueString(i, colNames1[3]); if
             * (elementId.equalsIgnoreCase("AS_OF_DATE") ||
             * elementId.equalsIgnoreCase("PRG_PERIOD_TYPE") ||
             * elementId.equalsIgnoreCase("PRG_COMPARE")) {
             *
             * outerBuffer.append("<li class='closed' id='"+elementId+"'>");
             * outerBuffer.append("<img src='icons
             * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
             * id='"+elementId+"'
             * style='font-family:verdana;'>"+elementId+"</span>");
             * outerBuffer.append("</li>"); }
             * outerBuffer.append(getFavParamMbrs(busFolderId, elementId)); }
             * outerBuffer.append("</ul>"); outerBuffer.append("</li>"); }
             */
//end
            String minTimeLevel = getUserFolderMinTimeLevel(foldersIds);
            if (minTimeLevel.equals("5")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Period Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Period Basis' style='font-family:verdana;'>Time-Period Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Range Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Range Basis' style='font-family:verdana;'>Time-Range Basis</span>");
                outerBuffer.append("</li>");

//commented this code only for Nerolac

                /*
                 * outerBuffer.append("<li class='closed'
                 * id='timeDimension-Month Range Basis'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Month Range Basis'
                 * style='font-family:verdana;'>Time-Month Range Basis</span>");
                 * outerBuffer.append("</li>"); outerBuffer.append("<li
                 * class='closed' id='timeDimension-Quarter Range Basis'>");
                 * outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Quarter Range Basis'
                 * style='font-family:verdana;'>Time-Quarter Range
                 * Basis</span>"); outerBuffer.append("</li>");
                 * outerBuffer.append("<li class='closed' id='timeDimension-Year
                 * Range Basis'>"); outerBuffer.append("<img src='icons
                 * pinvoke/Timesetup.gif'></img>"); outerBuffer.append("<span
                 * id='elmnt-Time Dimension-Year Range Basis'
                 * style='font-family:verdana;'>Time-Year Range Basis</span>");
                 * outerBuffer.append("</li>");
                 */
            } else if (minTimeLevel.equals("4")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Week Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Week Basis' style='font-family:verdana;'>Time-Week Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("3")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Month Basis' style='font-family:verdana;'>Time-Month Basis</span>");
                outerBuffer.append("</li>");
                outerBuffer.append("<li class='closed' id='timeDimension-Compare Month Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Compare Month Basis' style='font-family:verdana;'>Time-Compare Month Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("2")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Quarter Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Quarter Basis' style='font-family:verdana;'>Time-Quarter Basis</span>");
                outerBuffer.append("</li>");
            } else if (minTimeLevel.equals("1")) {
                outerBuffer.append("<li class='closed' id='timeDimension-Year Basis'>");
                outerBuffer.append("<img src='icons pinvoke/Timesetup.gif'></img>");
                outerBuffer.append("<span id='elmnt-Time Dimension-Year Basis' style='font-family:verdana;'>Time-Year Basis</span>");
                outerBuffer.append("</li>");
            }
//added for favparams
            if (retObj1.getRowCount() > 0) {
                outerBuffer.append("<li class='closed'>");
                outerBuffer.append("<img src='icons pinvoke/folder-horizontal.png'>&nbsp;<span>Favourite Params</span>");
                outerBuffer.append("<ul id='favourParams' class='background'>");
                outerBuffer.append(getFavParams(foldersIds, userId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrs(subFolderId, dimId));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String dispWhatIfParameters(String parameters, HashMap ParametersHashMap) {
        DisplayParameters disp = new DisplayParameters();
        ArrayList dispParams = new ArrayList();
        String[] paramIds = null;
        HashMap TimeDimHashMap = null;
        ArrayList TimeDimList = new ArrayList();
        String temp = "";
        try {
            TimeDimHashMap = (HashMap) ParametersHashMap.get("TimeDimHashMap");
            TimeDimList = (ArrayList) ParametersHashMap.get("timeParameters");
            ////////.println("TimeDimHashMap uday is: " + TimeDimHashMap);
            ////////.println("TimeDimList uday is:: " + TimeDimList);
            paramIds = parameters.split(",");
            HashMap paramValues = new HashMap();
            for (int i = 0; i < paramIds.length; i++) {
                dispParams.add(paramIds[i]);
                paramValues.put(paramIds[i], "All");
            }

//            temp = disp.displayWhatIfParamWithTime(dispParams, TimeDimHashMap);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return temp;
    }

    public String getWhatIfMeasures(String foldersIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = foldersIds;
        String factName = "";
        String subFolderTabid = "";
        String userFolderIds = foldersIds;
        String sql = getResourceBundle().getString("getFactsNew");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");

                outerBuffer.append(getMeasureElements(userFolderIds, subFolderTabid, "udayBatta"));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public boolean deleteUserWhatIfScenarios(String userId, String deleteids) {
        ArrayList queries = new ArrayList();
        String sqlQuery = getResourceBundle().getString("deleteWhatIfScenarios");
        String qry = getResourceBundle().getString("deleteUserWhatIfScenarios");
        boolean b = false;
        try {
            Object[] obj = new Object[1];
            obj[0] = deleteids;

            sqlQuery = buildQuery(sqlQuery, obj);
            queries.add(sqlQuery);
            ////////.println("sqlQuery is:: " + sqlQuery);

            qry = buildQuery(qry, obj);
            queries.add(qry);
            b = executeMultiple(queries);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return b;
    }

//    public ArrayList getWhatIfReportQryAggregations(ArrayList reportQryElementIds) {
//        String sqlQuery = getResourceBundle().getString("getWhatIfReportQryAggregations");
//        ArrayList reportQryAggregations = new ArrayList();
//        reportQryColNames = new ArrayList();
//        reportQryColTypes = new ArrayList();
//        //ArrayList reportQryColNames = new ArrayList();
////        String StrReportQryElementIds = "";
//        StringBuilder StrReportQryElementIds = new StringBuilder(300);
//        StringBuilder StrReportQryElementIdsOrder = new StringBuilder(300);
//        String finalQuery = "";
//        PbReturnObject retObj = null;
//        Object[] Obj = new Object[2];
//        String[] dbColumns = null;
//
//        HashMap aggrMap = new HashMap();
//
//
//        try {
//            for (int i = 0; i < reportQryElementIds.size(); i++) {
////                StrReportQryElementIds += "," + String.valueOf(reportQryElementIds.get(i));
////                StrReportQryElementIdsOrder += "," + String.valueOf(reportQryElementIds.get(i)) + "," + (i + 1);
//                 StrReportQryElementIds.append( ",").append( String.valueOf(reportQryElementIds.get(i)));
//                StrReportQryElementIdsOrder.append(",").append( String.valueOf(reportQryElementIds.get(i)) + "," + (i + 1));
//
//            }
////            if (!("".equalsIgnoreCase(StrReportQryElementIds))) {
////                StrReportQryElementIds = StrReportQryElementIds.substring(1);
////                StrReportQryElementIdsOrder = StrReportQryElementIdsOrder.substring(1);
////            }
//             if (StrReportQryElementIds.length()>0) {
//                StrReportQryElementIds =new StringBuilder( StrReportQryElementIds.substring(1));
//                StrReportQryElementIdsOrder =new StringBuilder( StrReportQryElementIdsOrder.substring(1));
//            }
//
//            //StrReportQryElementIds = StrReportQryElementIds.replace("A_", "");
//            //StrReportQryElementIdsOrder = StrReportQryElementIdsOrder.replace("A_", "");
//            StrReportQryElementIds =new StringBuilder( StrReportQryElementIds.toString().replaceAll("_P", ""));
//            StrReportQryElementIdsOrder =new StringBuilder( StrReportQryElementIdsOrder.toString().replaceAll("_P", ""));
//            Obj[0] = StrReportQryElementIds;
//            Obj[1] = StrReportQryElementIdsOrder;
//
//            finalQuery = buildQuery(sqlQuery, Obj);
//           
//            retObj = execSelectSQL(finalQuery);
//            dbColumns = retObj.getColumnNames();
//
//            for (int j = 0; j < retObj.getRowCount(); j++) {
//                if (retObj.getFieldValueString(j, dbColumns[2]) != null && !"".equalsIgnoreCase(retObj.getFieldValueString(j, dbColumns[2]))) {
//                    aggrMap.put(retObj.getFieldValueString(j, dbColumns[0]), retObj.getFieldValueString(j, dbColumns[2]) + "~" + retObj.getFieldValueString(j, dbColumns[3]));
//                } else {
//                    aggrMap.put(retObj.getFieldValueString(j, dbColumns[0]), "COUNT" + "~" + retObj.getFieldValueString(j, dbColumns[3]));
//                }
//                reportQryColNames.add(retObj.getFieldValueString(j, dbColumns[3]));
//            }
//            String QueryColId = "";
//            String val = "";
//            for (int i = 0; i < reportQryElementIds.size(); i++) {
//                QueryColId = String.valueOf(reportQryElementIds.get(i));
//                if (QueryColId.endsWith("_P")) {
//                    val = String.valueOf(aggrMap.get(QueryColId.replaceAll("_P", "")));
//                    String vallist[] = val.split("~");
//                    reportQryColNames.add(vallist[1]);
//                    reportQryAggregations.add(vallist[0]);
//                } else {
//                    val = String.valueOf(aggrMap.get(QueryColId));
//                    String vallist[] = val.split("~");
//                    reportQryColNames.add(vallist[1]);
//                    reportQryAggregations.add(vallist[0]);
//                }
//
//
//            }
//
//            for (int i = 0; i < reportQryElementIds.size(); i++) {
//                QueryColId = String.valueOf(reportQryElementIds.get(i));
//                val = String.valueOf(aggrMap.get(QueryColId));
//                String vallist[] = val.split("~");
//                reportQryColNames.add(vallist[1] + "(Projected Value)");
//                reportQryAggregations.add(vallist[0]);
//
//            }
//          setReportQryColNames(reportQryColNames);
//        } catch (Exception exp) {
//            
//        }
//        return reportQryAggregations;
//    }   
    public void delParameterSecurity(String elementId, String REPORTID) {
        try {
//            Connection connection = ProgenConnection.getInstance().getConnection();

            String delParameterSecurityQry = getResourceBundle().getString("delParameterSecurity");
            Object[] paramVal = new Object[2];
            paramVal[0] = elementId;
            paramVal[1] = REPORTID;
//            try {
            String finalQuery = buildQuery(delParameterSecurityQry, paramVal);
            execModifySQL(finalQuery);
//            } catch (Exception exp) {
//                
//            }

//            if (connection != null) {
//                connection.close();
//            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateParameterSecurity(String elementId, String paramValues, String REPORTID) {
        try {
            OraclePreparedStatement opstmt = null;
            Connection connection = null;

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                String updateParameterSecurityQry = getResourceBundle().getString("updateParameterSecurity");
                Object[] paramVal = new Object[3];
                paramVal[0] = paramValues;
                paramVal[1] = elementId;
                paramVal[2] = REPORTID;
                try {
                    String finalQuery = buildQuery(updateParameterSecurityQry, paramVal);
                    execModifySQL(finalQuery);
                } catch (Exception exp) {
                    logger.error("Exception: ", exp);
                }
            } else {
                connection = ProgenConnection.getInstance().getConnection();
                String updateParameterSecurity = "update prg_ar_parameter_security set member_value=? where element_id=? and report_id=?";
                opstmt = (OraclePreparedStatement) connection.prepareStatement(updateParameterSecurity);
                opstmt.setStringForClob(1, paramValues);
                opstmt.setString(2, elementId);
                opstmt.setString(3, REPORTID);
                opstmt.executeUpdate();
//                int rows = opstmt.executeUpdate();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }
    }

    public void saveDimensionSegmentValues(Container container, String reportId) {

        String insertDimensionValues = getResourceBundle().getString("insertSegmentValues");
        Object[] segmentDetails = new Object[6];
        ArrayList<Segment> dimensionSegment = null;
        String xmlSegment = "";

        Segment segment = null;
        dimensionSegment = container.getDimensionSegment();
        for (int i = 0; i < dimensionSegment.size(); i++) {
            try {

                segmentDetails[0] = reportId;
                segment = dimensionSegment.get(i);
//                if (segment instanceof MeasureBasedSegment) {
//                    segmentDetails[1] = segment.getDimension();
//                    //segmentDetails[2] = ((MeasureBasedSegment) seg).getMeasureElementId();
//                    segmentDetails[2] = "null";
//                    segmentDetails[3] = ((MeasureBasedSegment) segment).toXml();
//                    segmentDetails[4] = "MeasureBased";
//                } else {
                segmentDetails[1] = segment.getDimension();
                //segmentDetails[2] = ((ValueBasedSegment) seg).getDimensionElementId();
                segmentDetails[2] = "null";
                segmentDetails[3] = segment.toXml();
                segmentDetails[4] = segment.getSegmentType();
                segmentDetails[5] = segment.getSegName();

//                }
                String finalQuery = buildQuery(insertDimensionValues, segmentDetails);
                //
                execModifySQL(finalQuery);

            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
    }

    public void updateFavouriteParams(String reportId, String userId, String favName, String favDesc, String folderIds, String elementIds) {

        try {
            String paramsTime[] = elementIds.split(",");
            ArrayList favqueries = new ArrayList();
            String deleteFavParamsquery = getResourceBundle().getString("deleteFavParams");
            Object[] delObj = new Object[1];
            delObj[0] = favName;
            execModifySQL(buildQuery(deleteFavParamsquery, delObj));

            String insertFavParamsquery = getResourceBundle().getString("insertFavParams");
            String finalQuery = "";

            Object[] favParam = new Object[6];
            for (int i = 0; i < paramsTime.length; i++) {
                favParam[0] = userId;
                favParam[1] = reportId;
                favParam[2] = folderIds;
                favParam[3] = favName;
                favParam[4] = favDesc;
                favParam[5] = paramsTime[i];
                finalQuery = buildQuery(insertFavParamsquery, favParam);
                favqueries.add(finalQuery);
            }
            executeMultiple(favqueries);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    //added for std progen time
    public ArrayList insertNonStdReportTimeDimensions(int reportId, ArrayList queries) {
        String sqlQuery = getResourceBundle().getString("insertReportTimeDimensions");
        String finalQuery = "";
        Object[] obj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            obj = new Object[2];
            obj[0] = "Non_Standard";
            obj[1] = "Day";
        } else {
            obj = new Object[3];
            obj[0] = "Non_Standard";
            obj[1] = "Day";
            obj[2] = reportId;
        }

        finalQuery = buildQuery(sqlQuery, obj);
        queries.add(finalQuery);
        //for(int time=0;time<timeParams.size();time++){
//        queries = insertReportTimeDimensionsDetails(timeDimHashMap, queries, timeParams);
        //}
        return queries;
    }

    public void assignReportToUsers(String reportId, String[] userIds) {
        String addUserReports = getResourceBundle().getString("addUserReports");
        String finalQuery;
        PbDb pbdb = new PbDb();
        ArrayList<String> assignQueryLst = new ArrayList<String>();
        Object obj[] = null;
        boolean assigned = false;
        try {
            for (int i = 0; i < userIds.length; i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    obj = new Object[2];
                    obj[0] = userIds[i];
                    obj[1] = reportId;

                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                    obj = new Object[2];
                    obj[0] = userIds[i];
                    obj[1] = reportId;
                } else {
                    PbReturnObject pbro;
                    pbro = pbdb.execSelectSQL("select PRG_AR_USER_REPORTS_SEQ.nextval from dual");
                    obj = new Object[3];
                    String seqNum = String.valueOf(pbro.getFieldValueInt(0, 0));
                    obj[0] = seqNum;
                    obj[1] = userIds[i];
                    obj[2] = reportId;
                }

                finalQuery = pbdb.buildQuery(addUserReports, obj);
                assignQueryLst.add(finalQuery);
            }
            assigned = pbdb.executeMultiple(assignQueryLst);
        } catch (SQLException exp) {
            logger.error("Exception: ", exp);
            assigned = false;
        }catch (Exception exp) {
            logger.error("Exception: ", exp);
            assigned = false;
        }
        if (!assigned) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "assignReportToUsers", "Assigned of Report " + reportId + " failed");
            logger.error("Assigned of Report " + reportId + " failed");
        }
    }

    public String getRoleIdByReportId(String reportId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        Object obj[] = null;
        String RoleId = "";
        String getFolderByIdQuery = getResourceBundle().getString("getRoleIdByReportId");
        try {
            obj = new Object[1];
            obj[0] = reportId;
            finalQuery = buildQuery(getFolderByIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            RoleId = retObj.getFieldValueString(0, 0);

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        } catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return RoleId;
    }

    public void saveMapMeasures(Container container, String newReportId) {
        try {
            List<String> mainMeasures = container.getMapMainMeasure();
            String finalquery = "";
            List<String> suppMeasures = container.getMapSupportingMeasures();
            StringBuilder suppMeasSB = new StringBuilder();
            StringBuilder mainMeasSB = new StringBuilder();
            for (String meas : mainMeasures) {
                if (meas.startsWith("A_")) {
                    meas = meas.replace("A_", "");
                }
                mainMeasSB.append(",").append(meas);
            }
            for (String meas : suppMeasures) {
                if (meas.startsWith("A_")) {
                    meas = meas.replace("A_", "");
                }
                suppMeasSB.append(",").append(meas);
            }
            mainMeasSB.replace(0, 1, "");
            if (suppMeasSB.length() > 0) {
                suppMeasSB.replace(0, 1, "");
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                finalquery = "insert into prg_ar_report_map_details values(" + newReportId + ",'" + mainMeasSB.toString() + "','" + suppMeasSB.toString() + "')";
            } else {
                finalquery = "insert into prg_ar_report_map_details values(prg_ar_report_map_det_id_seq.nextval," + newReportId + ",'" + mainMeasSB.toString() + "','" + suppMeasSB.toString() + "')";
            }
            execModifySQL(finalquery);
        } catch (Exception ex) {
            logger.error("Exception", ex);

        }
    }

    public boolean copyReport(String reportName, String reportDesc, String oldReportId) {
        String[] reportIds = null;
        ArrayList queries = new ArrayList();
        boolean result = false;
        String copyReportId = "";
        String finalQuery = null;

        String copyReportMasterQry = getResourceBundle().getString("copyReportMaster");
        String copyReportDetailsQry = getResourceBundle().getString("copyReportDetails");
        String copyReportParamsQry = getResourceBundle().getString("copyReportParams");
        String copyReportViewbyMasterQry = getResourceBundle().getString("copyReportViewbyMaster");
        String copyReportViewbyDetailsQry = getResourceBundle().getString("copyReportViewbyDetails");
        String copyReportTimeQry = getResourceBundle().getString("copyReportTime");
        String copyReportTimeDetailsQry = getResourceBundle().getString("copyReportTimeDetails");
        String copyReportQueryDetailsQry = getResourceBundle().getString("copyReportQueryDetails");
        String copyReportTableMasterQry = getResourceBundle().getString("copyReportTableMaster");
        String copyReportTableDetailsQry = getResourceBundle().getString("copyReportTableDetails");
        String copyReportGraphMasterQry = getResourceBundle().getString("copyReportGraphMaster");
        String copyReportGraphDetailsQry = getResourceBundle().getString("copyReportGraphDetails");
        String copyUserReportsQry = getResourceBundle().getString("copyUserReports");
        String copyWhatifReportsQry = getResourceBundle().getString("copyWhatifReports");
        String copyReportDimSegmentQry = getResourceBundle().getString("copyReportDimSegment");
        String copyReportExcelCellPropsQry = getResourceBundle().getString("copyReportExcelCellProps");
        String copyReportExcelColsQry = getResourceBundle().getString("copyReportExcelCols");
        String copyReportParamSecurityQry = getResourceBundle().getString("copyReportParamSecurity");
        String copyReportColorsQry = getResourceBundle().getString("copyReportColors");
        String copyReportMapDetailsQry = getResourceBundle().getString("copyReportMapDetails");

        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//                    copyReportId = "select IDENT_CURRENT('PRG_AR_REPORT_MASTER')";
                reportIds = new String[4];
                reportIds[0] = reportName.replace("'", "''");
                reportIds[1] = reportDesc.replace("'", "''");
                reportIds[2] = reportDesc.replace("'", "''");
                reportIds[3] = oldReportId;
                finalQuery = buildQuery(copyReportMasterQry, reportIds);
                int cpyRepId = insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_AR_REPORT_MASTER");
                copyReportId = String.valueOf(cpyRepId);
//                    queries.add(finalQuery);
            } else {
                copyReportId = String.valueOf(getSequenceNumber("select PRG_AR_REPORT_MASTER_SEQ.nextval from dual"));
                reportIds = new String[5];
                reportIds[0] = copyReportId;
                reportIds[1] = reportName.replace("'", "''");
                reportIds[2] = reportDesc.replace("'", "''");
                reportIds[3] = reportDesc.replace("'", "''");
                reportIds[4] = oldReportId;
                finalQuery = buildQuery(copyReportMasterQry, reportIds);
                queries.add(finalQuery);
            }
            reportIds = new String[1];
            reportIds[0] = oldReportId;
            finalQuery = buildQuery(copyReportDetailsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportParamsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportViewbyMasterQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[2];
            reportIds[0] = copyReportId;
            reportIds[1] = copyReportId;
            finalQuery = buildQuery(copyReportViewbyDetailsQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[1];
            reportIds[0] = oldReportId;
            finalQuery = buildQuery(copyReportTimeQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportTimeDetailsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportQueryDetailsQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[2];
            reportIds[0] = reportName;
            reportIds[1] = oldReportId;
            finalQuery = buildQuery(copyReportTableMasterQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[4];
            reportIds[0] = oldReportId;
            reportIds[1] = oldReportId;
            reportIds[2] = copyReportId;
            reportIds[3] = copyReportId;
            finalQuery = buildQuery(copyReportTableDetailsQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[2];
            reportIds[0] = reportName;
            reportIds[1] = oldReportId;
            finalQuery = buildQuery(copyReportGraphMasterQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[3];
            reportIds[0] = oldReportId;
            reportIds[1] = copyReportId;
            reportIds[2] = copyReportId;
            finalQuery = buildQuery(copyReportGraphDetailsQry, reportIds);
            queries.add(finalQuery);

            reportIds = new String[1];
            reportIds[0] = oldReportId;
            finalQuery = buildQuery(copyUserReportsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyWhatifReportsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportDimSegmentQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportExcelCellPropsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportExcelColsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportParamSecurityQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportColorsQry, reportIds);
            queries.add(finalQuery);

            finalQuery = buildQuery(copyReportMapDetailsQry, reportIds);
            queries.add(finalQuery);

            result = executeMultiple(queries);

        } catch (Exception e) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "copyReport", "Copy Report Failed " + e.getMessage());
            logger.error("Exception", e);
            result = false;
        }
        return result;
    }

    public Map<String, String> getElementsNames(String elementIds) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        Object obj[] = null;
        Map<String, String> elementNameMap = new HashMap<String, String>();
        String getElementNameQry = getResourceBundle().getString("getElementName");
        try {
            obj = new Object[1];
            obj[0] = elementIds;
            finalQuery = buildQuery(getElementNameQry, obj);
            retObj = execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String elementName = retObj.getFieldValueString(i, 1);
                String elementId = retObj.getFieldValueString(i, 0);
                elementNameMap.put(elementId, elementName);
            }
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) { 
           logger.error("Exception", ex);
        }
        return elementNameMap;
    }

    public PbReturnObject getSchedulerDetails() {
        PbReturnObject retObj = null;
        try {
            String finalQry = getResourceBundle().getString("getSchedulerDetails");
            retObj = execSelectSQL(finalQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }

    public ArrayList getOriginalReportQryColTypes() {
        return originalReportQryColTypes;
    }

    public void setOriginalReportQryColTypes(ArrayList originalReportQryColTypes) {
        this.originalReportQryColTypes = originalReportQryColTypes;
    }

    public HashMap<String, ArrayList<String>> getRoleIds(String userId) {
        String roleQry = getResourceBundle().getString("getRoleDetails");
        String availRoleQry = getResourceBundle().getString("getAvailableRoleDetails");
        Object[] availableobj = new Object[3];
        Object[] obj = new Object[1];
        availableobj[0] = userId;
        availableobj[1] = userId;
        availableobj[2] = userId;
        obj[0] = userId;
        String finalRoleQry = buildQuery(roleQry, obj);
        String fianlQry = buildQuery(availRoleQry, availableobj);
        PbReturnObject roleObj = new PbReturnObject();
        HashMap<String, ArrayList<String>> roleMap = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> availableRoleMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> roleIdArr = new ArrayList<String>();
        ArrayList<String> roleNameArr = new ArrayList<String>();
        try {
            roleObj = execSelectSQL(finalRoleQry);
            for (int i = 0; i < roleObj.getRowCount(); i++) {
                roleIdArr.add(roleObj.getFieldValueString(i, 0));
                roleNameArr.add(roleObj.getFieldValueString(i, 1));
            }
            roleMap.put("roleId", roleIdArr);
            roleMap.put("roleName", roleNameArr);

            roleIdArr = new ArrayList<String>();
            roleNameArr = new ArrayList<String>();

            roleObj = execSelectSQL(fianlQry);
            for (int i = 0; i < roleObj.getRowCount(); i++) {
                roleIdArr.add(roleObj.getFieldValueString(i, 0));
                roleNameArr.add(roleObj.getFieldValueString(i, 1));
            }
            availableRoleMap.put("roleId", roleIdArr);
            availableRoleMap.put("roleName", roleNameArr);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        if (roleMap.size() > 0) {
            return roleMap;
        } else {
            return availableRoleMap;
        }
    }

    public List<ReportDetails> getReportDetails(String userId, String roleId) {
        String reportQry = getResourceBundle().getString("getReportDetails");
        Object[] obj = new Object[2];
        obj[0] = userId;
        obj[1] = roleId;
        String finalReportQry = buildQuery(reportQry, obj);
        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = null;
        try {
            reportObj = execSelectSQL(finalReportQry);
            reportJson = getReportJson(reportObj);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }

    public List<ReportDetails> getReportsForDrillAcross(String userId, String roleId, ArrayList<String> rowViewBys) {
        String reportQry = getResourceBundle().getString("getReportsForDrillAcross");
        Object[] obj = new Object[3];
        obj[0] = userId;
        obj[1] = roleId;
        obj[2] = rowViewBys.get(0);
        String finalReportQry = buildQuery(reportQry, obj);
        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = null;
        try {
            reportObj = execSelectSQL(finalReportQry);
            reportJson = getReportJson(reportObj);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }

    public List<ReportDetails> getReportsForComparison(String userId, String roleId, ArrayList<String> rowViewBys) {
        String reportQry = getResourceBundle().getString("getReportsForComparision");
        Object[] obj = new Object[2];
        obj[0] = userId;
        obj[1] = roleId;
//        obj[2]=rowViewBys.get(0);
        String finalReportQry = buildQuery(reportQry, obj);
        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = null;
        try {
            reportObj = execSelectSQL(finalReportQry);
            reportJson = getReportJson(reportObj);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }

    public List<ReportDetails> getFavouriteRept(String userId) {
        String qry = getResourceBundle().getString("getFavReport");
        Object[] obj = new Object[2];
        obj[0] = userId;
        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = null;
        String finalFavReportQry = buildQuery(qry, obj);
        try {
            reportObj = execSelectSQL(finalFavReportQry);
            reportJson = getReportJson(reportObj);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }

    public List<ReportDetails> getReportJson(PbReturnObject reportObj) {
        List<ReportDetails> reportDetailsList = new ArrayList<ReportDetails>();
        for (int i = 0; i < reportObj.getRowCount(); i++) {
            ReportDetails reportDetails = new ReportDetails();
            reportDetails.setReportId(reportObj.getFieldValueString(i, 0));
            reportDetails.setReportName(reportObj.getFieldValueString(i, 1));
            reportDetails.setReportType(reportObj.getFieldValueString(i, 2));
            reportDetails.setReportDesc(reportObj.getFieldValueString(i, 3));
            reportDetailsList.add(reportDetails);
        }
        return reportDetailsList;
    }

    public ArrayList getReportViewBy(String repId) {
        ArrayList<String> viewBy = new ArrayList<String>();
        String ViewByQuery = getResourceBundle().getString("getViewByForReport");
        Object[] obj = new Object[1];
        obj[0] = repId;
        String finalReportQry = buildQuery(ViewByQuery, obj);
        PbReturnObject reportObj = new PbReturnObject();
        try {
            reportObj = execSelectSQL(finalReportQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        for (int i = 0; i < reportObj.getRowCount(); i++) {
            viewBy.add(reportObj.getFieldValueString(i, 0));
        }
        return viewBy;
    }

    public ArrayList<String> getElementIdListForReport(String reportId) {
        String sqlQuery = getResourceBundle().getString("getElementIdListForReport");
        PbReturnObject retObj = new PbReturnObject();
        ArrayList<String> elementIdList = new ArrayList<String>();
        Object[] obj = new Object[1];
        obj[0] = reportId;
        String finalQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    elementIdList.add(retObj.getFieldValueString(i, "ELEMENT_ID"));
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return elementIdList;
    }

    public boolean IsComparableReport(ArrayList<String> elementIdList, String reportId) {
        boolean flag = false;
        String sqlQuery = getResourceBundle().getString("getComparableReports");
        PbReturnObject retObj = new PbReturnObject();
        Object[] obj = new Object[1];
        obj[0] = reportId;
        int count = 0;
        String finalQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                count = retObj.getFieldValueInt(0, "COUNT");
                if (count == 0) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
//        String sqlQuery=getResourceBundle().getString("getElementIdListForReport");
//        PbReturnObject retObj=new PbReturnObject();
//        ArrayList<String> comparableElementIdList=new ArrayList<String>();
//        Object[] obj=new Object[1];
//        obj[0]=reportId;
//        int count=0;
//        boolean flag=false;
//        String finalQuery=buildQuery(sqlQuery, obj);
//        try {
//            retObj = execSelectSQL(finalQuery);
//             if(retObj.getRowCount()>0)
//            {
//                for(int i=0;i<retObj.getRowCount();i++)
//                {
//                    comparableElementIdList.add(retObj.getFieldValueString(i, "ELEMENT_ID"));
//                }
//            }
//
//                if(!comparableElementIdList.containsAll(elementIdList))
//                    count=count+1;
//
//            if(count==0)
//                flag=true;
//            else
//                flag=false;
//
//        } catch (SQLException ex) {
//            logger.error("Exception",ex);
//        }
        return flag;
    }

    public String buildPbReturnObject(List<Container> containerList) {
        StringBuilder tableBuilder = new StringBuilder();
        Container firstContainer = containerList.get(0);
        List<String> mergeColIds = new ArrayList<String>();
        List<String> mergeColNames = new ArrayList<String>();
        List<Integer> mergeSequence = new ArrayList<Integer>();
        List<String> tempList = new ArrayList<String>();
        //StringBuilder tableBuilder=new StringBuilder();
        List<ProgenDataSet> retObjList = new ArrayList<ProgenDataSet>();
        List<Integer> mergeIndex = new ArrayList<Integer>();
        List<Integer> emptyReturnObjList = new ArrayList<Integer>();
        ProgenDataSet mainRetObj = null;

        ArrayList<String> dimIdList = new ArrayList<String>();
        ArrayList<String> dimNameList = new ArrayList<String>();

        ArrayList<String> rowViewBys = firstContainer.getReportCollect().reportRowViewbyValues;
        for (String rowViewby : rowViewBys) {
            mergeColIds.add(rowViewby);
            if (rowViewby.equalsIgnoreCase("Time")) {
                mergeColNames.add("Time");
            } else {
                mergeColNames.add(firstContainer.getReportCollect().getParameterDispName(rowViewby));
            }
            mergeSequence.add(0);
        }

        char[] sortType = new char[rowViewBys.size()];
        char[] sortDataType = new char[rowViewBys.size()];
        for (int p = 0; p < rowViewBys.size(); p++) {
            sortType[p] = '0';
            sortDataType[p] = 'C';
        }

        for (int i = 0; i < rowViewBys.size(); i++) {
            String dimId = rowViewBys.get(i);

            if (!dimId.equals("TIME")) {
                if (!dimId.startsWith("A_")) {
                    dimIdList.add("A_" + dimId);
                } else {
                    dimIdList.add(dimId);
                }
            } else {
                dimIdList.add(dimId);
            }
            dimNameList.add((String) firstContainer.getDisplayLabels().get(i));

        }
        ArrayList dispColslist = new ArrayList();
        for (int i = 0; i < containerList.size(); i++) {

            Container mainContainer = containerList.get(i);
            WhatIfScenario whatIfScenario = mainContainer.getWhatIfScenario();
            List<String> dispCols = mainContainer.getDisplayColumns();
            List dispLabels = mainContainer.getDisplayLabels();
            if (i != 0) {
                mergeSequence.add(i);
            }
            ArrayList<Integer> seq = new ArrayList<Integer>();
            mainRetObj = mainContainer.getRetObj();
            // seq = firstRetObj.sortDataSet(rowViewByFirstCont, sortType, sortDataType);
            if (mainRetObj.getRowCount() > 0) {
                seq = mainRetObj.sortDataSet(dimIdList, sortType, sortDataType);
            }
            mainRetObj.setViewSequence(seq);
            retObjList.add(mainRetObj);
            mergeIndex.add(0);

//            }
//            else
//            {
//            emptyReturnObjList.add(i);
//            }
//            retObjList.add(mainContainer.getRetObj());
            for (int x = rowViewBys.size(); x < dispCols.size(); x++) {
                String colId = dispCols.get(x);
                String tempColId = i + "_" + colId;
                if (tempList.contains(tempColId)) {
                    continue;
                }
                tempList.add(tempColId);
                mergeColIds.add(i + "_" + colId);
                if (mainContainer.isReportCrosstab()) {
                    dispColslist = (ArrayList) dispLabels.get(x);
                    mergeColNames.add(dispColslist.toString() + "-" + mainContainer.getReportName());
                } else {
                    mergeColNames.add(dispLabels.get(x) + "-" + mainContainer.getReportName());
                }

                for (int j = i + 1; j < containerList.size(); j++) {
                    Container secContainer = containerList.get(j);
                    List<String> secDispCols = secContainer.getDisplayColumns();
                    List<String> secDispLabels = secContainer.getDisplayLabels();
                    if (secDispCols != null) {
                        if (secDispCols.contains(colId)) {
                            mergeColIds.add(j + "_" + colId);
//                        mergeSequence.add(j);
                            mergeColNames.add(dispLabels.get(x) + "-" + secContainer.getReportName());

                            tempList.add(j + "_" + colId);
//                         tempList.add(i+"_"+colId);
                        }
                    }
                }
            }
        }

        PbReturnObject newRetObj = new PbReturnObject();
        List<String> retObjColNames = new ArrayList<String>();

//        retObjColNames.addAll(dimIdList);
        retObjColNames.addAll(dimNameList);

        for (int i = rowViewBys.size(); i < mergeColIds.size(); i++) {

//            retObjColNames.add(mergeSequence.get(i)+"_"+mergeColIds.get(i));
            //retObjColNames.add(mergeColIds.get(i));
            retObjColNames.add(mergeColNames.get(i));
        }

        String[] colNameArr = new String[retObjColNames.size()];
        for (int i = 0; i < retObjColNames.size(); i++) {
            colNameArr[i] = retObjColNames.get(i);
        }

        newRetObj.setColumnNames(colNameArr);

        int count = 0;
        ArrayList<String> colViewBy = null;
        for (int m = 0; m < containerList.size(); m++) {
            colViewBy = new ArrayList<String>();
            colViewBy = containerList.get(m).getReportCollect().reportColViewbyValues;
            if (!colViewBy.isEmpty()) {
                count = count + 1;
            }
        }

        // if(count==0){
        while (!isValsExhausted(retObjList, mergeIndex)) {
            List<Integer> indexList = getLeastValRetObjects(retObjList, mergeIndex, dimIdList);
            int tempIndex = indexList.get(0);

            for (int l = 0; l < dimIdList.size(); l++) {
                String value = retObjList.get(tempIndex).getFieldValueStringBasedOnViewSeq(mergeIndex.get(tempIndex), dimIdList.get(l));
                newRetObj.setFieldValue(dimNameList.get(l), retObjList.get(tempIndex).getFieldValueStringBasedOnViewSeq(mergeIndex.get(tempIndex), dimIdList.get(l)));
            }

            for (int i = rowViewBys.size(); i < mergeColIds.size(); i++) {
                String colId = mergeColIds.get(i);
                String colName = mergeColNames.get(i);
                String value = null;
                //int index = mergeSequence.get(i);
                int index = Integer.parseInt(colId.substring(0, 1));
                //String value= retObjList.get(index).getFieldValueString(mergeIndex.get(index), colId.substring(2));
//                if(!retObjList.get(index).getViewSequence().isEmpty()){
                if (indexList.contains(index)) {
                    BigDecimal val = null;
                    if (RTMeasureElement.isRunTimeMeasure(colId.substring(2))) {
                        val = retObjList.get(index).getFieldValueRuntimeMeasureBasedOnViewSeq(mergeIndex.get(index), colId.substring(2));
                    } else {
                        val = retObjList.get(index).getFieldValueBigDecimalBasedOnViewSeq(mergeIndex.get(index), colId.substring(2));
                    }
                    value = NumberFormatter.getModifiedNumber(val, "", -1);
                    //newRetObj.setFieldValue(index+"_"+colId, retObjList.get(index).getFieldValueString(mergeIndex.get(index), colId.substring(2)));
                    newRetObj.setFieldValue(colName, value);
                } //            }
                else {
                    newRetObj.setFieldValue(colName, "-");
                }
            }

            newRetObj.addRow();
            for (int index : indexList) {
                mergeIndex.set(index, mergeIndex.get(index) + 1);
            }
        }
        // }

        if (newRetObj != null && newRetObj.getRowCount() > 0) {
            tableBuilder = tableBuilder.append("<table class=\"tablesorter\"> <tr>");
            for (int k = 0; k < newRetObj.getColumnNames().length; k++) {
                String color = "#95CEF9";
                if (k == 0) {
                    color = "#95CEF9";
                } else {
                    int index = Integer.parseInt(mergeColIds.get(k).substring(0, 1));
                    if (index == 0) {
                        color = "#95CEF9";
                    } else if (index == 1) {
                        color = "cadetblue";
                    } else if (index == 2) {
                        color = "cyan";
                    } else if (index == 3) {
                        color = "grey";
                    }
                }
                tableBuilder = tableBuilder.append("<th class=\"header\" style=\"background-color:" + color + "\">").append(newRetObj.getColumnNames()[k]).append("</th>");
            }
            tableBuilder.append("</tr>");
            for (int i = 0; i < newRetObj.getRowCount(); i++) {
                tableBuilder.append("<tr>");
                for (int j = 0; j < newRetObj.getColumnCount(); j++) {
                    tableBuilder.append("<td>").append(newRetObj.getFieldValueString(i, j)).append("</td>");
                }
                tableBuilder.append("</tr>");
            }
            tableBuilder.append("</table>");
        }
        return tableBuilder.toString();
    }

    private boolean isValsExhausted(List<ProgenDataSet> dataSets, List<Integer> index) {
        int i = 0;
        int count = 0;
        for (i = 0; i < dataSets.size(); i++) {
            if (index.get(i) >= dataSets.get(i).getViewSequence().size()) {
                count = count + 1;
            }
        }
        if (count == dataSets.size()) {
            return true;
        } else {
            return false;
        }
//        if (i==dataSets.size())
//            return false;
//        else
//            return true;
    }

    private int getReturnObjectIndex(List<ProgenDataSet> dataSets, List<Integer> index) {
        int retObjIndex = -1;
        for (int i = 0; i < dataSets.size(); i++) {
            if (index.get(i) < dataSets.get(i).getViewSequence().size()) {
                retObjIndex = i;
                break;
            }
        }
        return retObjIndex;
    }

    private List<Integer> getLeastValRetObjects(List<ProgenDataSet> dataSets, List<Integer> index, List<String> dimIds) {
        List<Integer> result = new ArrayList<Integer>();
//        int rowCount=dataSets.get(0).getRowCount();
//        int maxDatasetObj=0;
//        int retObjIndex=-1;
//        for(int k=1;k<dataSets.size();k++)
//        {
//        if(rowCount<dataSets.get(k).getRowCount())
//        {
//           rowCount=dataSets.get(k).getRowCount();
//           maxDatasetObj=k;
//        }
//        }
        List<String> dimVals = new ArrayList<String>();
        int in = getReturnObjectIndex(dataSets, index);
        for (int i = 0; i < dimIds.size(); i++) {

//            if(index.get(0)<dataSets.get(0).getRowCount())
////            dimVals.add(dataSets.get(0).getFieldValueStringBasedOnViewSeq(index.get(0), dimIds.get(i)));
//            for(int k=0;k<dataSets.size();k++){
//
//            retObjIndex=isValExists(dataSets,index);
//            if(retObjIndex!=-1)
//                break;
//            }
            dimVals.add(dataSets.get(in).getFieldValueStringBasedOnViewSeq(index.get(in), dimIds.get(i)));

        }
        for (int i = 0; i < dataSets.size(); i++) {
//            if(i!=retObjIndex){
            if (index.get(i) < dataSets.get(i).getViewSequence().size()) {
                for (int j = 0; j < dimIds.size(); j++) {

                    String temp = dataSets.get(i).getFieldValueStringBasedOnViewSeq(index.get(i), dimIds.get(j));
                    if (dimVals.get(j).trim().compareTo(temp.trim()) > 0) {
                        dimVals.clear();
                        for (int x = 0; x < dimIds.size(); x++) {
                            dimVals.add(dataSets.get(i).getFieldValueStringBasedOnViewSeq(index.get(i), dimIds.get(x)));
                        }
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < dataSets.size(); i++) {
            if (index.get(i) < dataSets.get(i).getViewSequence().size()) {
                int j = 0;
                for (j = 0; j < dimIds.size(); j++) {

                    String val = dataSets.get(i).getFieldValueStringBasedOnViewSeq(index.get(i), dimIds.get(j));
                    if (!dimVals.get(j).equals(val)) {
                        break;
                    }

                }
                if (j == dimIds.size()) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public void setOverWriteExistingReport(boolean overWriteExistingReport) {
        this.overWriteExistingReport = overWriteExistingReport;
    }

//    public void saveReportComparisionDetails( ReportComparisionHelper comparisionHelper, String[] repIdList,String viewBy) throws RuntimeException{
//        String sqlQuery=getResourceBundle().getString("InsertReportComparisionDetails");
//        Object[] values=new Object[4];
//       Gson gson = new GsonBuilder().serializeNulls().create();
//        String otherReportIds="";
//        for(int i=1;i<repIdList.length;i++)
//        {
//            otherReportIds=otherReportIds+repIdList[i];
//            if(i!=repIdList.length-1)
//                otherReportIds=otherReportIds+",";
//        }
//         List<Container> containerList=comparisionHelper.getContainerList();
//        String tempJson=gson.toJson(comparisionHelper);
////        
//        values[0]=repIdList[0];
//        values[1]=otherReportIds;
//        values[2]=containerList.get(0).getReportName();
//        values[3]=viewBy;
////        values[2]=tempJson;
//        String finalQuery=buildQuery(sqlQuery, values);
//        try {
//            execModifySQL(finalQuery);
//        } catch (Exception ex) {
//            logger.error("Exception",ex);
//        }
//    }
    public String getMeasuresForCrosstab(Container container) {
        StringBuffer graphBuffer = new StringBuffer("");
        int measCnt = container.getReportMeasureCount();
        if (container.isSummarizedMeasuresEnabled() && container.getSummerizedTableHashMap() != null) {
            ArrayList summmeas = new ArrayList();
            HashMap summarizedmMesMap = container.getSummerizedTableHashMap();
            summmeas.addAll((List<String>) summarizedmMesMap.get("summerizedQryeIds"));
            measCnt += summmeas.size();
        }

        ArrayList tempStr = null;
        for (int i = container.getViewByCount(); i < measCnt + container.getViewByCount(); i++) {
            tempStr = new ArrayList();
            tempStr = (ArrayList) container.getDisplayLabels().get(i);
            graphBuffer.append(" <li  style='width:auto;height:auto;color:white' >");
            graphBuffer.append("<ul>");
            graphBuffer.append("<li id=\"eleName" + container.getDisplayColumns().get(i) + "\" style=\"color:black\"><span id='" + container.getDisplayColumns().get(i) + "'>" + tempStr.get(tempStr.size() - 1) + "</span></li>");
            graphBuffer.append("</ul>");
            graphBuffer.append("</li>");
        }

        return graphBuffer.toString();
    }

    public ArrayList viewByValuesList(String repId) {
        ArrayList viewBy = new ArrayList();
        String ViewByQuery = getResourceBundle().getString("getViewByValues");
        Object[] obj = new Object[1];
        obj[0] = repId;
        String finalReportQry = buildQuery(ViewByQuery, obj);
        PbReturnObject reportObj = new PbReturnObject();
        try {
            reportObj = execSelectSQL(finalReportQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        String values = reportObj.getFieldValueString(0, "VIEWBY_VALUES");
        String groupName = reportObj.getFieldValueString(0, "GROUP_NAME");
        String[] viewByvalues = values.split(",");
        if (!values.equals("")) {
            viewBy.addAll(Arrays.asList(viewByvalues));
            viewBy.add(groupName);
        }
        return viewBy;
    }

    public List<String> getCreatedDateForOverwriteReport(String reportId) throws SQLException {

        String getCreatedDateQuery = getResourceBundle().getString("getCreatedDateandUserName");
        Object obj[] = null;
        String query = "";
        List<String> list = new ArrayList<String>();
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                obj = new Object[1];
                obj[0] = reportId;
            } else {
                obj = new Object[1];
                obj[0] = reportId;
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        query = buildQuery(getCreatedDateQuery, obj);
        PbReturnObject retobj = this.execSelectSQL(query);
        if (retobj.rowCount != 0) {
            list.add(retobj.getFieldValueDateString(0, 0));
            list.add(retobj.getFieldValueString(0, 1));
        }
        return list;
    }

    public void updateCurrentDateAnduserName(int reportId, List<String> createDateAndUser, String userId) throws SQLException, Exception {
        String getusernameQuery = getResourceBundle().getString("getUserName");
        Object[] user = null;
        String query = "";
        boolean flag = false;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                user = new Object[1];
                user[0] = userId;
            } else {
                user = new Object[1];
                user[0] = userId;
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        query = buildQuery(getusernameQuery, user);
        PbReturnObject retobj = this.execSelectSQL(query);
        String userName = "";
        if (retobj.rowCount != 0) {
            userName = retobj.getFieldValueString(0, 0);
        }
        if (!createDateAndUser.isEmpty()) {
            String updateDateQuery = getResourceBundle().getString("updateDateAndUserName");
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Object obj[] = new Object[3];
                obj[0] = createDateAndUser.get(1);
                obj[1] = userName;
                obj[2] = reportId;
                query = buildQuery(updateDateQuery, obj);
                this.execUpdateSQL(query);
            } else {
                Object obj[] = new Object[4];
                obj[0] = createDateAndUser.get(0);

                obj[1] = createDateAndUser.get(1);
                obj[2] = userName;
                obj[3] = reportId;
                query = buildQuery(updateDateQuery, obj);
                this.execUpdateSQL(query);
            }
        }

    }

    public PbReturnObject getReportDetailsList(String reportId) {
        String query = "";
        String getReportDetailsList = getResourceBundle().getString("getReportDetailsList");
        PbReturnObject retobj = null;
        try {
            Object obj[] = new Object[1];
            obj[0] = reportId;
            query = buildQuery(getReportDetailsList, obj);
            retobj = this.execSelectSQL(query);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retobj;
    }

    public PbReturnObject getMeasureValues(String measureId) {

        String ViewByQuery = getResourceBundle().getString("getMeasureValues");
        Object[] obj = new Object[2];
        obj[0] = measureId;
        obj[1] = measureId;
        String finalReportQry = buildQuery(ViewByQuery, obj);
        PbReturnObject reportObj = new PbReturnObject();
        try {
            reportObj = execSelectSQL(finalReportQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return reportObj;
    }

    public String getSchedulerID(int reportId, String sName) {
        String qry = getResourceBundle().getString("getSchedulerId");
        Object[] obj = new Object[2];
        obj[0] = reportId;
        obj[1] = sName;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        try {
            retobj = execSelectSQL(finalqry);
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        String schedulerId = retobj.getFieldValueString(0, 0).toString();
        return schedulerId;
    }

    public void updateSchedulerDetails(String SchedulerId, ReportSchedule schedule) {
        String qry = getResourceBundle().getString("updateSchedulerdetails");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(schedule.getStartDate());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(schedule.getEndDate());
        Date sdate = schedule.getStartDate();
        Date edate = schedule.getEndDate();
        schedule.setStartDate(null);
        schedule.setEndDate(null);
        //Start of code by sandeep on 15/10/14 for schedule// update local files in oneview
        HashMap GraphSizesDtlsHashMap = null;
        HashMap GraphSizesDtlsHashMap1 = null;
        String advHtmlFileProps = "";
        String advHtmlFileProps1 = "";
        String filename = "";
        String filename1 = "";
        String contextpath = "";
        String contextpath1 = "";
        contextpath1 = schedule.getContextPath();
        schedule.setContextPath(contextpath);
        filename1 = schedule.getfilename();
        schedule.setfilename(filename);
        advHtmlFileProps1 = schedule.getadvHtmlFileProps();
        schedule.setadvHtmlFileProps(advHtmlFileProps);
        GraphSizesDtlsHashMap = schedule.getGraphSizesDtlsHashMap();
        schedule.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap1);
        //End of code by sandeep on 15/10/14 for schedule// update local files in oneview
        List<ReportSchedule> scheduleList = new ArrayList<ReportSchedule>();
        scheduleList.add(schedule);
        Gson gson = new Gson();
        String gsonString = gson.toJson(scheduleList);
        Object[] obj = new Object[4];
        obj[0] = gsonString;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            obj[1] = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else {
            obj[2] = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        obj[3] = SchedulerId;
        String finalqry = buildQuery(qry, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        //Start of code by sandeep on 15/10/14 for schedule// update local files in oneview
        schedule.setStartDate(sdate);
        schedule.setEndDate(edate);
        schedule.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
        schedule.setadvHtmlFileProps(advHtmlFileProps1);
        schedule.setfilename(filename1);
        schedule.setContextPath(contextpath1);
        //End of code by sandeep on 15/10/14 for schedule// update local files in oneview
    }
    //Added By Ram 03Nov15 for update Dashboard schedule

    public void updateDashboardSchedulerDetails(String SchedulerId, ReportSchedule schedule) {
        String qry = getResourceBundle().getString("updateDashboardSchedulerdetails");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(schedule.getStartDate());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(schedule.getEndDate());
        Date sdate = schedule.getStartDate();
        Date edate = schedule.getEndDate();
        schedule.setStartDate(null);
        schedule.setEndDate(null);
        //Start of code by sandeep on 15/10/14 for schedule// update local files in oneview
        HashMap GraphSizesDtlsHashMap = null;
        HashMap GraphSizesDtlsHashMap1 = null;
        String advHtmlFileProps = "";
        String advHtmlFileProps1 = "";
        String filename = "";
        String filename1 = "";
        String contextpath = "";
        String contextpath1 = "";
        contextpath1 = schedule.getContextPath();
        schedule.setContextPath(contextpath);
        filename1 = schedule.getfilename();
        schedule.setfilename(filename);
        advHtmlFileProps1 = schedule.getadvHtmlFileProps();
        schedule.setadvHtmlFileProps(advHtmlFileProps);
        GraphSizesDtlsHashMap = schedule.getGraphSizesDtlsHashMap();
        schedule.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap1);
        //End of code by sandeep on 15/10/14 for schedule// update local files in oneview
        List<ReportSchedule> scheduleList = new ArrayList<ReportSchedule>();
        scheduleList.add(schedule);
        Gson gson = new Gson();
        String gsonString = gson.toJson(scheduleList);
        Object[] obj = new Object[4];
        obj[0] = gsonString;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[1] = startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.DATE);
        } else {
            obj[1] = startCalendar.get(Calendar.DATE) + "-" + (startCalendar.get(Calendar.MONTH) + 1) + "-" + startCalendar.get(Calendar.YEAR);
        }

        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            obj[2] = endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.DATE);
        } else {
            obj[2] = endCalendar.get(Calendar.DATE) + "-" + (endCalendar.get(Calendar.MONTH) + 1) + "-" + endCalendar.get(Calendar.YEAR);
        }

        obj[3] = SchedulerId;
        String finalqry = buildQuery(qry, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        //Start of code by sandeep on 15/10/14 for schedule// update local files in oneview
        schedule.setStartDate(sdate);
        schedule.setEndDate(edate);
        schedule.setGraphSizesDtlsHashMap(GraphSizesDtlsHashMap);
        schedule.setadvHtmlFileProps(advHtmlFileProps1);
        schedule.setfilename(filename1);
        schedule.setContextPath(contextpath1);
        //End of code by sandeep on 15/10/14 for schedule// update local files in oneview
    }

    public String getURL(String reportId, String schedulerId) {
        String qry = getResourceBundle().getString("getUrl");
        Object[] obj = new Object[2];
        obj[0] = schedulerId;
        obj[1] = reportId;
        String finalqry = buildQuery(qry, obj);
        PbReturnObject retobj = new PbReturnObject();
        try {
            retobj = execSelectSQL(finalqry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            String snapurl = retobj.getFieldUnknown(0, 11);

            return snapurl;
        } // String snapurl = retobj.getFieldValueClobString(0, "SNAP_URL").replace(";", "&");
        else {
            String snapurl = retobj.getFieldValueClobString(0, "SNAP_URL");
            return snapurl;
        }
    }
////swathi written by 17Apr

    public void updateSchedulerStatusAndDate(String schedulerId, String reportId, String userId) {
        String qry = getResourceBundle().getString("updateSchedulerStatusAndDate");
        Object[] obj = new Object[3];
        obj[0] = schedulerId;
        obj[1] = reportId;
        obj[2] = userId;
        String finalqry = buildQuery(qry, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public ArrayList viewTableData(String tableIds, Container container, String dimName) throws SQLException {

        logger.info("dimName******" + dimName);
        int viewByCnt = container.getViewByCount();
        String[] dimvals;
        HashMap<String, String> dimValMap = new HashMap<String, String>();
        if (viewByCnt > 1) {
            dimvals = dimName.split(",");
            for (int i = 0; i < dimvals.length; i++) {
                String[] a = dimvals[i].split("=");
                dimValMap.put(a[0], a[1]);
            }
        } else {
            String[] a = dimName.split("=");
            dimValMap.put(a[0], a[1]);
        }

        ArrayList alist = new ArrayList();

//        PbReportQueryResourceBundle resBundle = new PbReportQueryResourceBundle();
        PbReturnObject retObj = null;
        String measureId = null;
        Object[] Obj = null;
        String d_clu = null;
        PbTimeRanges pbTime = new PbTimeRanges();
        ArrayList<String> timedetailsar = new ArrayList<String>();
        timedetailsar = container.getReportCollect().timeDetailsArray;
        // String dislayLable=(String) container.getDisplayLabels().get(0);
        PbReportQuery pbrepQuery = new PbReportQuery();

//        ArrayList<String> measures=(ArrayList<String>) container.getTableHashMap().get("Measures");
//        if(measures.contains(tableIds)){
//            measureId=tableIds;
//        }
//        else{
//            measureId=measures.get(0).replace("A_", "");
//        }
//        pbTime.elementID=measureId;
        measureId = tableIds;
        pbTime.elementID = tableIds;
        if (timedetailsar.get(4).equalsIgnoreCase("Last Period")) {
            pbTime.setRange(timedetailsar.get(3).toString(), timedetailsar.get(4).toString(), timedetailsar.get(2).toString());
            d_clu = pbTime.d_clu;
        } else {
            pbTime.setRange(timedetailsar);
            d_clu = pbTime.d_clu;
        }
//        String dimId=container.getDisplayColumns().get(0);
        ArrayList<String> rowViewbyVals = new ArrayList<String>();
        rowViewbyVals = container.getReportCollect().reportRowViewbyValues;

//        String getALlLayerTableNamesQuery = getResourceBundle().getString("getALlLayerTableNames");
//        String getALlLayerColNamesQuery = getResourceBundle().getString("getALlLayerColNames");
        String tableId = getResourceBundle().getString("getTableId");
//         String dimElTabId = getResourceBundle().getString("getDimeEleTableId");
//         String dimTabelName = getResourceBundle().getString("getDimeTableName");
//         String alldimColValues=getResourceBundle().getString("allDimValuesList");

//        String dimTableName = "";
        String factTabName = "";
        //       String dbTabName = "";
//        String dimCol="";
//        Object[] Obj1 = null;
//        Object[] Obj2 = null;
//        Object[] Obj3 = null;
//        String finalQuery = "";
//        PbReturnObject retObj1 = null;
//        PbReturnObject retObj2 = null;
//        PbReturnObject retObj3 = null;
        String bussTableId = null;
//        ArrayList<String>dimTableIds=new ArrayList<String>();
        String finalQuery = "";
        String qryTable = null;
        Vector<String> bussTableIds = new Vector<String>();

        String[] colNames = null;

        Connection connection = null;
        try {

            Obj = new Object[1];
            Obj[0] = measureId;
            finalQuery = buildQuery(tableId, Obj);
            retObj = super.execSelectSQL(finalQuery);
            for (int i = 0; i < retObj.getRowCount(); i++) {
                bussTableId = retObj.getFieldValueString(i, 0);
                factTabName = retObj.getFieldValueString(i, 1);
                qryTable = bussTableId;
                bussTableIds.add(bussTableId);
            }
            String timeCol = pbrepQuery.getTimeColforFact(bussTableId);
//            Obj = new Object[1];
//            Obj[0] = bussTableId;
//            finalQuery = buildQuery(getALlLayerTableNamesQuery, Obj);
//
//            retObj = execSelectSQL(finalQuery);
//            colNames = retObj.getColumnNames();
//
//            if (retObj != null && retObj.getRowCount() != 0) {
//                factTabName = retObj.getFieldValueString(0, colNames[0]);
//
//                dbTabName = retObj.getFieldValueString(0, colNames[2]);
//
//            }

//            Obj = new Object[1];
//            Obj[0] = bussTableId;
//            finalQuery = buildQuery(getALlLayerColNamesQuery, Obj);
//
//            retObj = execSelectSQL(finalQuery);
//            colNames = retObj.getColumnNames();
            finalQuery = "Select Distinct Buss_Table_Id,Buss_Table_Name,Buss_Col_Name,Element_Id,User_Col_Desc,User_Col_Type,Ref_Element_Id, Ref_Element_Type From Prg_User_All_Info_Details  Where Element_Id In (" + Joiner.on(",").join(rowViewbyVals) + ") Order By Buss_Table_Id, Ref_Element_Id, Ref_Element_Type";
            logger.info("finalQuery" + finalQuery);
            retObj = execSelectSQL(finalQuery);
            int size = retObj.getRowCount();
            HashMap<String, String> viewbyFacts = new HashMap<String, String>();
            HashMap<String, String> viewbybusColNames = new HashMap<String, String>();
            if (retObj != null && size > 0) {
                for (int i = 0; i < size; i++) {
//                  dbTabName=retObj.getFieldValueString(i, 0);
                    qryTable += "," + retObj.getFieldValueString(i, 0);
                    bussTableIds.add(retObj.getFieldValueString(i, 0));
                    viewbyFacts.put(retObj.getFieldValueString(i, 3), retObj.getFieldValueString(i, 1));
                    viewbybusColNames.put(retObj.getFieldValueString(i, 3), retObj.getFieldValueString(i, 2));
                }
            }
            String fromClause = null;
            fromClause = factTabName + " " + factTabName;
            String[] viewbyIds = (String[]) viewbyFacts.keySet().toArray(new String[0]);
            for (int i = 0; i < viewbyIds.length; i++) {
                if (!fromClause.contains(viewbyFacts.get(viewbyIds[i]))) {
                    fromClause += "," + viewbyFacts.get(viewbyIds[i]) + " " + viewbyFacts.get(viewbyIds[i]);
                }
            }
            String filterClause = null;
            viewbyIds = (String[]) dimValMap.keySet().toArray(new String[0]);
            for (int i = 0; i < viewbyIds.length; i++) {
                if (i == 0) {
                    filterClause = viewbyFacts.get(viewbyIds[i]) + "." + viewbybusColNames.get(viewbyIds[i]) + "='" + dimValMap.get(viewbyIds[i]) + "'";
                } else {
                    filterClause += "  AND  " + viewbyFacts.get(viewbyIds[i]) + "." + viewbybusColNames.get(viewbyIds[i]) + "='" + dimValMap.get(viewbyIds[i]) + "'";
                }
            }

//            Obj2 = new Object[1];
//            Obj2[0]=dimId.replace("A_", "");
//            finalQuery = buildQuery(dimElTabId, Obj2);
//            retObj = execSelectSQL(finalQuery);
//            if(retObj.getRowCount()>0){
//            dbTabName=retObj.getFieldValueString(0, 0);
//            qryTable+=","+dbTabName;
//            bussTableIds.add(dbTabName);
//            }
//
//             Obj2 = new Object[1];
//            Obj2[0]=dbTabName;
//
//            finalQuery = buildQuery(dimTabelName, Obj2);
//            retObj = execSelectSQL(finalQuery);
//            dimTableName=retObj.getFieldValueString(0, 0);
//             Obj2 = new Object[1];
//            Obj2[0]=dbTabName;
//            finalQuery = buildQuery(alldimColValues, Obj2);
//            retObj2 = execSelectSQL(finalQuery);
            //  String timeCol = pbrepQuery.getTimeColforFact(bussTableId);
//            finalQuery = resBundle.getString("generateViewByQry1");
//            Obj = new Object[1];
//            Obj[0] = dimId.replace("A_", "");
//
//            //
//            finalQuery = buildQuery(finalQuery, Obj);
//            ////.println(" generateViewByQry1 "+ sqlstr);
//            retObj = execSelectSQL(finalQuery);
//
//            colNames = retObj.getColumnNames();
//
//            if (retObj.getRowCount() > 0) {
//
//                for (int i = 0; i < retObj.getRowCount(); i++) {
//
//                   dimCol=retObj.getFieldValueString(i, colNames[1]) + "." + retObj.getFieldValueString(i, colNames[2]);
//
//
//                }
//            }
            String whereClause = null;
            finalQuery = "Select ACTUAL_CLAUSE,BUSS_TABLE_ID,BUSS_TABLE_ID2 from PRG_GRP_BUSS_TABLE_RLT_MASTER where BUSS_TABLE_ID in (" + qryTable + ") OR BUSS_TABLE_ID2 in (" + qryTable + ") ";
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            int psize = retObj.getRowCount();
            if (psize > 0) {
                for (int looper = 0; looper < psize; looper++) {
                    if (bussTableIds.contains(retObj.getFieldValueString(looper, colNames[1])) && bussTableIds.contains(retObj.getFieldValueString(looper, colNames[2]))) {
                        whereClause = retObj.getFieldValue(looper, colNames[0]).toString();
                    }
                }
            }
            String insql = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

                insql = "SELECT DISTINCT top 100 " + factTabName + ".*  FROM " + fromClause + " WHERE " + whereClause + "  AND  " + timeCol + " " + d_clu + " AND  " + filterClause;
                //String insql = "select top 100 "+factTabName+".*  FROM "+factTabName+" "+factTabName+" ,"+dimTableName+"  "+dimTableName+" WHERE " +whereClause+"  AND  "+timeCol+" "+ d_clu+" AND  "+dimCol+"='"+dimName+"' ";
                //String insql = "select top 100 "+factTabName+".*  FROM "+factTabName+" "+factTabName+" ,"+dimTableName+"  "+dimTableName+" WHERE " +whereClause+"  AND  "+timeCol+" "+ d_clu+" AND  "+dimCol+"='"+dimName+"' ";
                logger.info("insql****" + insql);
                connection = getTableConnection(bussTableId);
                retObj = execSelectSQL(insql, connection);
            } else {
                insql = "SELECT DISTINCT " + factTabName + ".*  FROM " + fromClause + " WHERE " + whereClause + "  AND  " + timeCol + " " + d_clu + " AND  " + filterClause;
                // String insql = "SELECT "+factTabName+".*  FROM "+factTabName+" "+factTabName+" ,"+dimTableName+"  "+dimTableName+" WHERE " +whereClause+"  AND  "+timeCol+" "+ d_clu+" AND  "+dimCol+"='"+dimName+"' ";
                logger.info("insql****" + insql);
                connection = getTableConnection(bussTableId);
                retObj = execSelectSQL(insql, connection);

            }

            alist.add(retObj);
            if (connection != null) {
                connection.close();
            }
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }

        return alist;

    }

    public Connection getTableConnection(String bussTableId) {
        Connection conn = null;
        try {
            String masterTableId = this.getMasterTableId(bussTableId);
            conn = ProgenConnection.getInstance().getConnectionByTable(masterTableId);
            return conn;
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
            return conn;
        }
    }

    public String getMasterTableId(String bussTableIds) {
        String masterTableId = null;
        PbReturnObject retObj = null;
        String masterTableQry = getResourceBundle().getString("getMasterTableIdDetails");
        String finalQuery = "";

        Object[] Obj = new Object[1];
        Obj[0] = bussTableIds;

        try {
            finalQuery = buildQuery(masterTableQry, Obj);

            retObj = execSelectSQL(finalQuery);

            masterTableId = retObj.getFieldValueString(0, 0);
        } catch (SQLException ex) {
           logger.error("Exception", ex);
            masterTableId = "";
        }catch (Exception ex) {
           logger.error("Exception", ex);
            masterTableId = "";
        }
        return masterTableId;

    }

    public TreeMap getMeasureNames(String userFolderIds, String subFolderTabid, String contextPath) {

        String finalQuery = null;
        PbReturnObject retObj = null;
        String[] colNames = null;
        String ElementId = "";
        String ElementName = "";
        //HashMap map=new HashMap();
        TreeMap map = new TreeMap();
        Object obj[] = new Object[1];
        obj[0] = subFolderTabid;
        String sql = getResourceBundle().getString("getFactRelatedMeasures");
        finalQuery = buildQuery(sql, obj);
        //  
        try {
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                // map.put(ElementId, ElementName);
                map.put(ElementName, ElementId);
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return map;

    }

    public String getBussTableNameandId(String folderId, String subfolderId) {
        PbReturnObject retObj = null;
        String sql = "select distinct buss_table_id,buss_table_name from prg_user_all_info_details where folder_id=& and sub_folder_tab_id=& and disp_name is not null and user_col_type not in('calculated','summarised','summarized','TIMECALUCULATED')";
        Object obj[] = new Object[2];
        obj[0] = folderId;
        obj[1] = subfolderId;
        StringBuffer sb = new StringBuffer();
        String finalqry = buildQuery(sql, obj);
        //
        try {
            retObj = execSelectSQL(finalqry);
            if (retObj.getRowCount() != 0) {
                sb.append(retObj.getFieldValueString(0, 0));
                sb.append(",").append(retObj.getFieldValueString(0, 1));
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        // 
        return sb.toString();
    }

    public String fetchFactName(String elementId) {
        String qry = "select buss_table_name,sub_folder_tab_id,table_disp_name from prg_user_all_info_details where element_id=&";
        PbReturnObject retObj = null;
        String factName = "";
        String subFolderTabid = "";
        String dispName = "";
        String factInfo = null;
        Object[] obj = new Object[1];
        obj[0] = elementId;
        String finalQuery = buildQuery(qry, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() != 0) {
                StringBuffer allFactNames = new StringBuffer("");
                StringBuffer allsubFolderIds = new StringBuffer("");
                StringBuffer allDispNames = new StringBuffer("");
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    factName = retObj.getFieldValueString(i, 0);
                    allFactNames.append(",").append(factName);
                    subFolderTabid = retObj.getFieldValueString(i, 1);
                    allsubFolderIds.append(",").append(subFolderTabid);
                    dispName = retObj.getFieldValueString(i, 2);
                    allDispNames.append(",").append(dispName);
                }
                factInfo = allFactNames + "&" + allsubFolderIds + "&" + allDispNames;
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return factInfo;
    }

    public String getBussTabledetails(String measEleId) {
        PbReturnObject retObj = null;
        String sql = getResourceBundle().getString("getBussTabledetails");
        Object obj[] = new Object[1];
        if (measEleId.contains("A_")) {
            obj[0] = measEleId.replace("A_", "");
        } else {
            obj[0] = measEleId;
        }
        StringBuilder sb = new StringBuilder();
        String finalqry = buildQuery(sql, obj);

        try {
            retObj = execSelectSQL(finalqry);
            if (retObj.getRowCount() != 0) {
                sb.append(retObj.getFieldValueString(0, 0));
                sb.append(",").append(retObj.getFieldValueString(0, 1));
                sb.append(",").append(retObj.getFieldValueString(0, 2));
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        // 
        return sb.toString();
    }

    public PbReturnObject getReportNamesforFolderId(String folderId) {
        String reportsQuery = getResourceBundle().getString("getReportNamesforFolderId");
        PbReturnObject retObj = null;
        String finalQuery = "";
        Object[] reps = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            reps = new Object[2];
            reps[0] = folderId;
            reps[1] = folderId;

        } else {
            reps = new Object[1];
            reps[0] = folderId;
        }

        try {
            finalQuery = buildQuery(reportsQuery, reps);
//           

            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return retObj;
    }

    public String getBuscolName(String elementId) {
        String sqlqry = "select buss_table_name,buss_col_name,user_col_type,actual_col_formula from prg_user_all_info_details where element_id=" + elementId;
        PbReturnObject retObj = null;
        String buscolName = null;
        try {
            retObj = execSelectSQL(sqlqry);
            if (retObj.getRowCount() != 0) {
                if (retObj.getFieldValueString(0, 2).equalsIgnoreCase("calculated")) {
                    buscolName = '(' + retObj.getFieldValueString(0, 3) + ')';
                } else {
                    buscolName = retObj.getFieldValueString(0, 0) + "." + retObj.getFieldValueString(0, 1);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return buscolName;
    }

    public String getCurrentViewbysofRep(String repId) {
//            String currentviewby = "";
        StringBuilder currentviewby = new StringBuilder(200);
        String getReportCurrentViewby = getResourceBundle().getString("getReportCurrentViewby");
        PbReturnObject retObject = null;
        String finalqry = "";
        Object[] Obj = null;
        Obj = new Object[1];
        Obj[0] = repId;
        finalqry = buildQuery(getReportCurrentViewby, Obj);
        try {
            retObject = execSelectSQL(finalqry);
            if (retObject.getRowCount() > 0) {
//                currentviewby = retObject.getFieldValueString(0,"VIEW_BY_ID");
                currentviewby = new StringBuilder(retObject.getFieldValueString(0, "VIEW_BY_ID"));
                for (int i = 1; i < retObject.getRowCount(); i++) {
                    currentviewby.append(",").append(retObject.getFieldValueString(i, "VIEW_BY_ID"));
//                    currentviewby = currentviewby+","+retObject.getFieldValueString(i,"VIEW_BY_ID");
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return currentviewby.toString();
    }

    public String getMeasuresforGrpInsights(String foldersIds, ArrayList Parameters, String contextPath) {
        PbReturnObject retObj = null;
        String finalQuery = null;
//        String factName = "";
//        String subFolderTabid = "";
//        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        if (Parameters != null && !Parameters.isEmpty()) {

            obj = new Object[2];
            obj[0] = foldersIds;
            try {
                obj[1] = getdimmap.getFact(Parameters);
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
            logger.error("Exception", ex);
            }
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            outerBuffer.append("<li class='closed' id=''>");
            outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
            outerBuffer.append("<span style='font-family:verdana;' title='Groups'>Groups</span>");
            outerBuffer.append("<ul >");
            outerBuffer.append(getMeasureElementsForGrpInsights(userFolderIds, contextPath));
            outerBuffer.append("</ul>");
            outerBuffer.append("</li>");
//                 outerBuffer.append("<li class='closed' id=''>");
//                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
//                outerBuffer.append("<span style='font-family:verdana;' title='Facts'>Facts</span>");
//                outerBuffer.append("<ul >");
//            for (int i = 0; i < retObj.getRowCount(); i++) {
//                factName = retObj.getFieldValueString(i, colNames[1]);
//                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
//                facttooltip = retObj.getFieldValueString(i, colNames[2]);
//
//                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
//                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
//                outerBuffer.append("<span style='font-family:verdana;' title='" + facttooltip + "'>" + factName + "</span>");
//                outerBuffer.append("<ul id='factName-" + factName + "'>");
            outerBuffer.append(getMeasures(userFolderIds, Parameters, contextPath));
//                outerBuffer.append("</ul>");
//                outerBuffer.append("</li>");
//            }
//            outerBuffer.append("</ul>");
//            outerBuffer.append("</li>");
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }

    public String getMeasureElementsForGrpInsights(String userFolderIds, String contextPath) {
        PbReturnObject retObj = null;
//        PbReturnObject retObjforParents = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        obj[0] = userFolderIds;
//        obj[0] = subFolderTabid;
        String grpId = "";
//        String REFElementId = "";
//        String ElementId1 = "";
//        String REFElementId1 = "";
        String grpName = "";
//        String ElementName1 = "";
        String Formula = "";
        String colId = "";
//        String queryforParents="";
        ArrayList parentIds = new ArrayList();
        String[] parentColNames = null;

//        String sql = getResourceBundle().getString("getFactElements");
        String queryGroups = getResourceBundle().getString("getGroups");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(queryGroups, obj);
            retObj = execSelectSQL(finalQuery);
//            retObjforParents=execSelectSQL(queryGroups);
            colNames = retObj.getColumnNames();

            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                grpId = retObj.getFieldValueString(i, colNames[0]);
                grpName = retObj.getFieldValueString(i, colNames[1]);
                outerBuffer.append("<li class='closed'>");
                if (!Formula.equalsIgnoreCase("")) {
                    outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                    viewFormulaClass = "formulaViewMenu";
                } else {
                    outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                }
                outerBuffer.append("<span class='" + viewFormulaClass + "' id='G_" + grpId + "' ' style='font-family:verdana;'>" + grpName + "</span>");
                outerBuffer.append("</li>");
//                }
            }

        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();

    }

    public String getUserDimDetails(String foldersIds, String userId) {

//        ProgenLog.log(ProgenLog.FINE, this, "getUserDimDetails", "Enter foldersIds--" + foldersIds );
        logger.info("Enter foldersIds--" + foldersIds);
        PbReturnObject retObj = null;
//        PbReturnObject retObj1 = null;
//        String finalQuery = null;
        String[] colNames = null;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            retObj = this.getUserDimensions(foldersIds, userId, favName);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");
                outerBuffer.append(getUserDimsMbrs(subFolderId, dimId));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getUserDims", "Exit");
        logger.info("Exit");
        return outerBuffer.toString();

    }

    public void saveNewDimensions(String reportId, String dimIds, Container container) {
        String getReportParamDetailsQuery = getResourceBundle().getString("getReportParamDetails1");
//        PbReportCollection collect = container.getReportCollect();
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        String[] paramIds = dimIds.split(",");
        if ((dimIds.equalsIgnoreCase("") || dimIds == null) && container.ReportLayout.equalsIgnoreCase("KPI") || container.ReportLayout.equalsIgnoreCase("None")) //added by mohit for kpi and none
        {
            dimIds = "0000";
        }
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = dimIds;
//            Obj[2] = dimIds;
        } else {
            Obj = new Object[3];
            Obj[0] = reportId;
            Obj[1] = dimIds;
            //       Obj[2] = dimIds;
        }
        String addParameterSecurity = "";

        try {
            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);
//            //////.println("finalquery getreportparamdetails are : " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            dbColumns = retObj.getColumnNames();
            //modified by bharathi reddy default value clob insertion
            Connection connection = ProgenConnection.getInstance().getConnection();
            PreparedStatement opstmt = null;
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE) values(?,?,?,?,?,?,?," + (i + 1) + ",?)";
                } else {
                    addParameterSecurity = "insert into PRG_AR_REPORT_PARAM_DETAILS (PARAM_DISP_ID,REPORT_ID,PARAM_DISP_NAME,ELEMENT_ID,DIM_ID,DIM_TAB_ID,BUSS_TABLE,DEFAULT_VALUE,DISP_SEQ_NO,PARAMETER_TYPE) values(PRG_AR_REPORT_PARAM_DETLS_SEQ.nextval,?,?,?,?,?,?,?," + (i + 1) + ",?)";
                }
                opstmt = connection.prepareStatement(addParameterSecurity);
                //(OraclePreparedStatement) connection.prepareStatement(addParameterSecurity);
//                //////.println("retObj.getFieldValueString(0,0) is : " + retObj.getFieldValueString(0, 0));
//                //////.println("retObj.getFieldValueInt(0,0) is : " + retObj.getFieldValueInt(0, 0));
//                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                    opstmt.setInt(1, retObj.getFieldValueInt(0, 0));
//                } else {
//                    opstmt.setString(1, retObj.getFieldValueString(0, 0));
//                }
                opstmt.setInt(1, retObj.getFieldValueInt(0, 0));
                opstmt.setString(2, retObj.getFieldValueString(i, dbColumns[1]));
                opstmt.setString(3, retObj.getFieldValueString(i, dbColumns[2]));
                opstmt.setString(4, retObj.getFieldValueString(i, dbColumns[3]));
                opstmt.setString(5, retObj.getFieldValueString(i, dbColumns[4]));
                opstmt.setString(6, retObj.getFieldValueString(i, dbColumns[5]));
                //                if (paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2])) != null) {
//                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                        opstmt.setString(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
////                        String paramClob = (String) paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]));
////                        ((PreparedStatementProxy) opstmt).setCharacterStream(7, new StringReader(paramClob), paramClob.length()); //.setStringForClob(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
//                    } else {
//                        ((OraclePreparedStatement) opstmt).setStringForClob(7, String.valueOf(paramDefaultValuesHashMap.get(retObj.getFieldValueString(i, dbColumns[2]))));
//                    }
//                }
//                if (!(collect.getDefaultValue(paramIds[i]).equals(""))) {
//                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
//                        opstmt.setString(7, collect.getDefaultValue(paramIds[i]));
//                    } else {
//                        ((OraclePreparedStatement) opstmt).setStringForClob(7, collect.getDefaultValue(paramIds[i]));
//                    }
//
//                } else {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    opstmt.setString(7, "All");
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    opstmt.setString(7, "All");
                } else {
                    ((OraclePreparedStatement) opstmt).setStringForClob(7, "All");
                }
//                }

                opstmt.setString(8, "NOT_SELECTED");
                int rows = opstmt.executeUpdate();
            }
            if (connection != null) {
                if (opstmt != null) {
                    opstmt.close();
                }
                connection.close();  //added by mohit for kpi and none
            }
        } catch (SQLException ex) {
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

    }

    public List<ReportDetails> getFavouriteRept1(String userId) {
        String finalFavReportQry = "SELECT  report_id,report_name,report_type FROM PRG_HOME_TABS where USER_ID = " + userId;

        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = null;
        try {
            reportObj = execSelectSQL(finalFavReportQry);
            reportJson = getReportJson(reportObj);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }

    public void getreportdetails(String Ids, String userId) {
        String Id = Ids;
        String userid = userId;
        HashMap map = new HashMap();
        ArrayList id = new ArrayList();
        ArrayList names = new ArrayList();
        ArrayList types = new ArrayList();
        Gson json = new Gson();
        String jsonString = null;
        try {
            String query = " select REPORT_NAME,REPORT_TYPE,REPORT_DESC from  PRG_AR_REPORT_MASTER  where REPORT_ID=&";
            String query2 = " select REPORT_ID from  PRG_HOME_TABS  where REPORT_ID=&";
            String query1 = "Insert into PRG_HOME_TABS(user_id,report_id,report_name,report_type,report_desc) values(&,'&','&','&','&')";
            PbReturnObject retob = new PbReturnObject();
            PbReturnObject retobj = new PbReturnObject();
            PbReturnObject retob1 = new PbReturnObject();
            Object[] obj = new Object[1];
            Object[] values = new Object[5];
            Object[] obj1 = new Object[1];
            obj[0] = Id;
            obj1[0] = Id;
            PbDb pbdb = new PbDb();
            String finalquery = pbdb.buildQuery(query, obj);
            String finalquery2 = pbdb.buildQuery(query2, obj1);
            retob = pbdb.execSelectSQL(finalquery);
            retobj = pbdb.execSelectSQL(finalquery2);
//        for(int i=0;i<retob.rowCount;i++)
            if (retob != null && retob.getRowCount() > 0) {
                if (retobj == null || retobj.getRowCount() < 1) {
                    values[0] = userid;
                    values[1] = Id;
                    values[2] = retob.getFieldValueString(0, "REPORT_NAME");
                    values[3] = retob.getFieldValueString(0, "REPORT_TYPE");
//           values[3]="R";
                    values[4] = retob.getFieldValueString(0, "REPORT_DESC");
                    String finalquery1 = pbdb.buildQuery(query1, values);
                    pbdb.execUpdateSQL(finalquery1);
                }
            }

        } catch (SQLException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
           logger.error("Exception", ex);
        }

    }
//Surender

    public List<ReportDetails> viewByRepors(String userId) {
        String reportQry = getResourceBundle().getString("viewByReports");
        Object[] obj = new Object[1];
        obj[0] = userId;
        String finalReportQry = buildQuery(reportQry, obj);
        PbReturnObject reportObj = new PbReturnObject();
        List<ReportDetails> reportJson = new ArrayList<ReportDetails>();
        try {
            reportObj = execSelectSQL(finalReportQry);
            for (int i = 0; i < reportObj.getRowCount(); i++) {
                ReportDetails reportDetails = new ReportDetails();
                reportDetails.setReportId(reportObj.getFieldValueString(i, 0));
                reportDetails.setReportName(reportObj.getFieldValueString(i, 1));
                reportDetails.setReportType(reportObj.getFieldValueString(i, 2));
                reportJson.add(reportDetails);
            }

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return reportJson;
    }
//Surender

    public String getAllReportsWithGrap(String buzRoles, String busrolename, String userid) {
        PbReturnObject retObj = null;
        String[] colNames = null;
        String repName = "";
        String repId = "";
        String graphId = "";
        String graphName = "";
        Object[] dataValues = null;
        String getReportssql = getResourceBundle().getString("getReportsByBuzRoles");
        StudioDao studioDao = new StudioDao();

        StringBuilder outerBuffer = new StringBuilder();
        String finalQuery = "";
        StringBuilder repIds = new StringBuilder();
        StringBuilder repNames = new StringBuilder();
        StringBuilder grapIds = new StringBuilder();
        StringBuilder grapNames = new StringBuilder();
        FileReadWrite fileReadWrite = new FileReadWrite();
        repIds.append("{ ReportIds: [");
        repNames.append(" ReportNames: [");
        grapIds.append(" GraphIds: [");
        grapNames.append(" GraphNames: [");

        try {

            dataValues = new Object[1];
            dataValues[0] = buzRoles;
            finalQuery = buildQuery(getReportssql, dataValues);
            retObj = execSelectSQL(finalQuery);
// PbReturnObject retObj = studioDao.getUserReports(userid);
            if (retObj != null) {
                colNames = retObj.getColumnNames();
                String tempReportId = null;
                String data = "";
                String repidold = "null";
//int j=0;
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    repId = retObj.getFieldValueString(i, colNames[0]);
                    repName = retObj.getFieldValueString(i, colNames[1]);

                    graphId = retObj.getFieldValueString(i, colNames[2]);
                    graphName = retObj.getFieldValueString(i, colNames[3]);

//  if( repId == null ? "80" == null : repId.equals("80")){
                    String filePath = "/usr/local/cache/" + busrolename.replaceAll("\\W", "").trim() + "/" + repName.replaceAll("\\W", "").trim() + "_" + repId + "/" + repName.replaceAll("\\W", "").trim() + "_" + repId + ".json";
                    String filePath1 = "/usr/local/cache/" + busrolename.replaceAll("\\W", "").trim() + "/" + repName.replaceAll("\\W", "").trim() + "_" + repId + "/" + repName.replaceAll("\\W", "").trim() + "_" + repId + "_data.json";
                    File file = new File(filePath);
                    File datafile = new File(filePath1);
                    String data1 = "";
                    XtendReportMeta reportMeta = new XtendReportMeta();
                    Type metaType = new TypeToken<XtendReportMeta>() {
                    }.getType();
                    Gson gson = new Gson();
                    if (repidold == null ? repId != null : !repidold.equals(repId)) {
//           repidold=repId;
                        if (file.exists()) {
                            if (datafile.exists()) {
                                data1 = fileReadWrite.loadJSON(filePath1);

                                HashMap<String, List<Map<String, String>>> chartData123 = new HashMap<String, List<Map<String, String>>>();
                                Type mapTarType = new TypeToken<HashMap<String, List<Map<String, String>>>>() {
                                }.getType();

//   Map<String,DashboardChartData> chartlist= chartData2.
//           chartData123 = gson.fromJson(chartData2, mapTarType);
                                repidold = repId;
                                HashMap<String, List<Map<String, String>>> chartData12 = new HashMap<String, List<Map<String, String>>>();

                                String chartData1 = fileReadWrite.loadJSON(filePath);

//                  Type mapTarType = new TypeToken <HashMap<String,List<Map<String, String>>>>() {
//                }.getType();
                                if ((chartData1 == null ? "" != null : !chartData1.equals("")) && chartData1 != null) {
                                    chartData12 = gson.fromJson(chartData1, mapTarType);
                                    List<String> charts = new ArrayList();
                                    if ((data1 == null ? "" != null : !data1.equals("")) && data1 != null) {
                                        reportMeta = gson.fromJson(data1, metaType);
                                        Map<String, DashboardChartData> chartData2 = (Map<String, DashboardChartData>) reportMeta.getChartData();

//                                     for(int h=0; h<chartData2.size();h++){
                                        if (chartData2 != null) {
                                            Set<String> mapData = chartData2.keySet();
                                            Iterator itcalcross = mapData.iterator();
                                            while (itcalcross.hasNext()) {
                                                String keys = itcalcross.next().toString();
                                                charts.add(keys);
                                            }
                                        }
                                    }
//
// j++;
//                    }
//                    }
                                    for (int j = 0; j < charts.size(); j++) {
//             HashMap<String,List<Map<String, String>>> mapData = new HashMap<String,List<Map<String, String>>>();
                                        Type tarType1 = new TypeToken<Map<String, List<Map<String, String>>>>() {
                                        }.getType();
                                        if ((data1 == null ? "" != null : !data1.equals("")) && data1 != null) {
                                            reportMeta = gson.fromJson(data1, metaType);
                                            Map<String, DashboardChartData> chartData2 = (Map<String, DashboardChartData>) reportMeta.getChartData();

//                                     for(int h=0; h<chartData2.size();h++){
                                            Set<String> mapData = chartData2.keySet();
                                            Iterator itcalcross = mapData.iterator();
                                            while (itcalcross.hasNext()) {
                                                String keys = itcalcross.next().toString();
                                                if (charts.get(j).equalsIgnoreCase(keys)) {
                                                    DashboardChartData chartData21 = chartData2.get(keys);
                                                    String name = chartData21.getName();
                                                    String charttype = chartData21.getChartType();
                                                    if (charttype != null && charttype.equalsIgnoreCase("TileChart") || charttype.equalsIgnoreCase("RadialProgress") || charttype.equalsIgnoreCase("LiquidFilledGauge") || charttype.equalsIgnoreCase("Dial-Gauge")) {
                                                        graphName = chartData21.getKPIName();
                                                    } else if (name != null) {
                                                        graphName = name;
                                                    } else {
                                                        graphName = charts.get(j);
                                                    }
                                                }
                                            }
                                            repIds.append("\"").append(repId).append("\"");
                                            repNames.append("\"").append(repName).append("\"");
                                            grapIds.append("\"").append(repId).append("\"");
                                            grapNames.append("\"").append(graphName).append("\"");

                                            if (i != retObj.getRowCount() - 1) {
                                                repIds.append(",");
                                                repNames.append(",");
                                                grapIds.append(",");
                                                grapNames.append(",");
                                            }
//                                         }
                                        }
//                    j++;
                                    }
                                }
                            }
                        }
//else{
//
//       repIds.append("\"").append(repId).append("\"");
//                    repNames.append("\"").append(repName).append("\"");
//                    grapIds.append("\"").append(graphId).append("\"");
//                    grapNames.append("\"").append(graphName).append("\"");
//
//                     if (i != retObj.getRowCount() - 1) {
//                            repIds.append(",");
//                            repNames.append(",");
//                            grapIds.append(",");
//                            grapNames.append(",");
//                }
//                }
                    }
                }
                repIds.append("],").append(repNames).append("],").append(grapIds).append("],").append(grapNames).append("] }");
            }
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return repIds.toString();

    }
//Surender

    public String getMeasuresForOneView(String foldersIds, String contextPath) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object[] obj = new Object[1];
        String sql = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        sql = getResourceBundle().getString("getFactsNewforOneView");

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            obj[0] = foldersIds;
            String finalqe = buildQuery(sql, obj);
            retObj = execSelectSQL(finalqe);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;' title='" + facttooltip + "'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");
                outerBuffer.append(getMeasureElements(userFolderIds, subFolderTabid, contextPath));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }
    //Surender

    public String insertOneViewName(String oneviewName) {

        Calendar date = Calendar.getInstance();
        SimpleDateFormat datfrmt = new SimpleDateFormat();
        datfrmt.applyPattern("dd/MM/yyyy");
        String dat = datfrmt.format(date.getTime());
        String finalquery = getResourceBundle().getString("insertOneviewnameId");
        int oneId = 0;
        Object[] obj = new Object[3];
        int snapShotId = 1;
        String finalQuery = null;

        PbReturnObject reportObj = new PbReturnObject();
        String oneViewId = null;
        try {

            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                Object[] object = new Object[2];
                object[0] = oneviewName;
                object[1] = dat;
                finalQuery = super.buildQuery(finalquery, object);
                snapShotId = super.insertAndGetSequenceInSQLSERVER(finalQuery, "PRG_AR_ONEVIEW_NAME_ID");
                oneViewId = Integer.toString(snapShotId);
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                Object[] object = new Object[2];
                object[0] = oneviewName;
                object[1] = dat;
                finalQuery = super.buildQuery(finalquery, object);
                snapShotId = super.insertAndGetSequenceInMySql(finalQuery, "ONEVIEW_NAME_ID", "VIEWID");
                oneViewId = Integer.toString(snapShotId);
            } else {
                String finalqr = getResourceBundle().getString("getOneviewIdName");
                reportObj = execSelectSQL(finalqr);
                oneId = reportObj.getFieldValueInt(0, 0);
                oneViewId = Integer.toString(oneId);
                obj[0] = oneId;
                obj[1] = oneviewName;
                obj[2] = dat;
                String finalqry = buildQuery(finalquery, obj);
                execUpdateSQL(finalqry);
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return oneViewId;
    }
    //Surender

    public PbReturnObject getAllOneViewBys() {
//          String finalQuery = "SELECT * FROM ONEVIEW_NAME_ID order by viewid";
        String finalQuery = getResourceBundle().getString("getOneviews");
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }
    //Surender

    public void insertOneViewData(OnceViewContainer onecontainer, String oneviewID) throws IOException {
//          String finalqry = "insert into ONEVIEW_DATA (VIEW_ID,VIEW_DATA) values(?,?)";
        String finalqry = getResourceBundle().getString("insertOneviewData");
        Object[] obj = new Object[2];
        Connection connection = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objOstream = new ObjectOutputStream(baos);
            objOstream.writeObject(onecontainer);

            objOstream.flush();
            objOstream.close();
            baos.close();
            connection = getConnection();
            byte[] bArray = baos.toByteArray();
            PreparedStatement objStatement = connection.prepareStatement(finalqry);
            objStatement.setString(1, oneviewID);
            objStatement.setBytes(2, bArray);

            objStatement.execute();
            connection.close();
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
    //Surendre

    public OnceViewContainer getOneViewData(String oneviewID) throws SQLException {
//          String finalqry = "SELECT * FROM ONEVIEW_DATA WHERE VIEW_ID='"+oneviewID+"'";
        String finalQuery = null;
        String finalqry = getResourceBundle().getString("getOneviewData");
        Connection con = null;
        OnceViewContainer onecontainer = null;
        Object[] obj = new Object[1];
        obj[0] = oneviewID;
        finalQuery = buildQuery(finalqry, obj);
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(finalQuery);
            rs = ps.executeQuery();
            while (rs.next()) {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes("VIEW_DATA")));
                onecontainer = (OnceViewContainer) ois.readObject();
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            con.close();
            con = null;
        } catch (IOException ex) {
           logger.error("Exception", ex);
        } catch (ClassNotFoundException ex) {
           logger.error("Exception", ex);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }

        }
        return onecontainer;
    }
//Surender

    public PbReturnObject getBurolsByUserId(String pbUserId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = null;

        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            obj = new Object[1];
            obj[0] = pbUserId;
            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }

    public void updateOneviewData(OnceViewContainer onecontainer, String oneviewID) throws Exception {
//          String finalqry = "insert into ONEVIEW_DATA (VIEW_ID,VIEW_DATA) values(?,?)";
        String finalqry = getResourceBundle().getString("updateOneviewData");
        Object[] obj = new Object[2];
        Connection connection = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objOstream = new ObjectOutputStream(baos);
            objOstream.writeObject(onecontainer);

            objOstream.flush();
            objOstream.close();
            baos.close();
            connection = getConnection();
            byte[] bArray = baos.toByteArray();
            PreparedStatement objStatement = connection.prepareStatement(finalqry);

            objStatement.setBytes(1, bArray);
            objStatement.setString(2, oneviewID);

            objStatement.execute();
        } catch (IOException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        } finally {
            connection.close();
        }
    }

    public String getUserName(String userId) {
        PbReturnObject retObj = null;
        String userName = null;
        String finalString = getResourceBundle().getString("getUserName");
        Object[] obj = null;
        String finalQuery = null;
        try {
            obj = new Object[1];
            obj[0] = userId;
            finalQuery = buildQuery(finalString, obj);
            retObj = execSelectSQL(finalQuery);
            userName = retObj.getFieldValueString(0, "PU_LOGIN_ID");
        } catch (SQLException ex) { 
           logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return userName;
    }

    public void oneviewRenaming(String oneviewId, String oneviewName) throws IOException {
//          String finalqry = "insert into ONEVIEW_DATA (VIEW_ID,VIEW_DATA) values(?,?)";
        String finalqry = getResourceBundle().getString("upDateOneviewname");
        Object[] obj = new Object[2];
        String finalQuery = null;
        try {
            obj[0] = oneviewName;
            obj[1] = Integer.parseInt(oneviewId);
            finalQuery = buildQuery(finalqry, obj);
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public void updateOneviewData(String oneviewDeatilsFile, String oneviewId, String oneViewVersion, String filePath) {
        String finalquery = getResourceBundle().getString("insertOneviewFileandName");
        Object[] obj = new Object[4];
        obj[0] = oneviewId;
        obj[1] = oneviewDeatilsFile;
        obj[2] = oneViewVersion;
        obj[3] = filePath;
        String finalqry = buildQuery(finalquery, obj);
        try {
            execUpdateSQL(finalqry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public PbReturnObject getOneviewFileNam(String oneviewId) {
        PbReturnObject retObj = null;
        String userName = null;
        String finalString = getResourceBundle().getString("getOneviewFileName");
        Object[] obj = null;
        String finalQuery = null;
        try {
            obj = new Object[1];
            obj[0] = oneviewId;
            finalQuery = buildQuery(finalString, obj);
            retObj = execSelectSQL(finalQuery);
//            userName=retObj.getFieldValueString(0, "ONEVIEW_FILE");
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }

    public PbReturnObject testForExisting() {
        PbReturnObject retObj = null;
        String finalString = getResourceBundle().getString("getFileDetailsData");
        try {
            retObj = execSelectSQL(finalString);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }

    public String getOneviewFileName(String oneviewId) {
        PbReturnObject retObj = null;
        String userName = null;
        String finalString = getResourceBundle().getString("getOneviewFileName");
        Object[] obj = null;
        String finalQuery = null;
        try {
            obj = new Object[1];
            obj[0] = oneviewId;
            finalQuery = buildQuery(finalString, obj);
            retObj = execSelectSQL(finalQuery);
            userName = retObj.getFieldValueString(0, 1);
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return userName;
    }

    public String getRegionOneview(String oneviewId) {
        PbReturnObject retObj = null;
        String userName = null;
        String finalString = getResourceBundle().getString("getOneviewFileName");
        Object[] obj = null;
        String finalQuery = null;
        try {
            obj = new Object[1];
            obj[0] = oneviewId;
            finalQuery = buildQuery(finalString, obj);
            retObj = execSelectSQL(finalQuery);
            userName = retObj.getFieldValueString(0, 2);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return userName;
    }

    public void upDateOneviewRegionData(String oneviewdata, String oneviewId, String oneviewName, String userId, String roleIds, String filePath) throws IOException {
//          String finalqry = "insert into ONEVIEW_DATA (VIEW_ID,VIEW_DATA) values(?,?)";
        Calendar date = Calendar.getInstance();
        SimpleDateFormat datfrmt = new SimpleDateFormat();
        datfrmt.applyPattern("dd/MM/yyyy");
        String dat = datfrmt.format(date.getTime());
        String finalqry = getResourceBundle().getString("upDateOneviewRegiondata");
        Object[] obj = new Object[8];
        String finalQuery = null;
        try {
            obj[0] = oneviewdata;
            obj[1] = oneviewName;
            obj[2] = dat;
            obj[3] = userId;
            obj[4] = dat;
            obj[5] = userId;
            obj[6] = roleIds;
            obj[7] = oneviewId;
            finalQuery = buildQuery(finalqry, obj);
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        String qry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = null where USER_ID = '" + userId + "'";
        } else {
            qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = '' where USER_ID = '" + userId + "'";
        }
        try {
            execModifySQL(qry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        String insertTMDetQuery = getResourceBundle().getString("insertOneViewAssignDetail");
        Object[] reportDets;
        String result = "";
        int seq = 0;
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) && !ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            try {
                seq = super.getSequenceNumber("select PRG_AR_ONEVIEW_ASSIGNMENT_SEQ.NEXTVAL FROM DUAL");
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        if (!ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER) && !ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            reportDets = new Object[4];
            reportDets[0] = seq;
            reportDets[1] = oneviewId;
            reportDets[2] = userId;
            reportDets[3] = filePath;
        } else {
            reportDets = new Object[3];
            reportDets[0] = oneviewId;
            reportDets[1] = userId;
            reportDets[2] = filePath;
        }
        String finalQuery1 = "";

        try {
            finalQuery1 = buildQuery(insertTMDetQuery, reportDets);
            execModifySQL(finalQuery1);
            result = "success";
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
            result = "failure";
        }
    }

    public PbReturnObject getJqtoJfRetObj() {
        PbReturnObject retObj = null;
        String sqlQuery = "SELECT JqpGraph_Id,JqpGraph_Name,JfGraph_Id,JfGraph_Name FROM PRG_AR_GRAPHSMAP";
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retObj;
    }

    public ResourceBundle getResourceBundle1() {

        return this.getResourceBundle();
    }

    public boolean DeleteReportwhenOverwriteRep(Integer reportId) {
        String[] reportIds = new String[1];
        reportIds[0] = reportId.toString();
        ArrayList queries = new ArrayList();
        String deleteReportViewByDetailsQuery = "";
        boolean result = false;

//        String deleteReportGraphMasterQuery="";
        String deleteReportGraphDetailsQuery = "";
        try {

            String deleteReportTimeDetailsQuery = "delete from PRG_AR_REPORT_TIME_DETAIL where rep_time_id in ( select rep_time_id from PRG_AR_REPORT_TIME where report_id='" + reportId + "')";
            String deleteReportTimeDimensionsQuery = "delete from PRG_AR_REPORT_TIME where report_id='" + reportId + "'";
            String deleteReportViewByMasterQuery = "delete from PRG_AR_REPORT_VIEW_BY_MASTER where report_id ='" + reportId + "'";
            String qry = "select DISTINCT a.view_by_id from PRG_AR_REPORT_VIEW_BY_DETAILS a,PRG_AR_REPORT_VIEW_BY_MASTER b where a.view_by_id= b.view_by_id and b.report_id=" + reportId;
            PbReturnObject retObj = super.execSelectSQL(qry);
            String viewbyIds = "";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i == 0) {
                    viewbyIds = retObj.getFieldValueString(i, 0);
                } else {
                    viewbyIds = viewbyIds + "," + retObj.getFieldValueString(i, 0);
                }

            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in ('" + viewbyIds + "')";
            } else {
                deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in (select DISTINCT a.view_by_id from PRG_AR_REPORT_VIEW_BY_DETAILS a,PRG_AR_REPORT_VIEW_BY_MASTER b where a.view_by_id= b.view_by_id and b.report_id=" + reportId + ")";
            }
            String deleteReportTableMasterQuery = "delete from PRG_AR_REPORT_TABLE_MASTER where report_id='" + reportId + "'";
            String deleteReportTableDetailsQuery = "delete from PRG_AR_REPORT_TABLE_DETAILS where report_id='" + reportId + "'";
            String deleteReportColors = "delete from PRG_AR_REPORT_COLORS where REPORT_ID=" + reportId;
            String deleteExcelCellQuery = "delete from PRG_AR_EXCEL_CELL_PROPS where report_id=" + reportId;
//        if(!overWriteExistingReport){
//                deleteReportGraphMasterQuery = "delete from PRG_AR_GRAPH_MASTER where report_id ='" + reportId + "' ";
            String grqry = "select DISTINCT a.graph_id from PRG_AR_GRAPH_DETAILS a ,PRG_AR_GRAPH_MASTER b where a.graph_id= b.graph_id and b.report_id ='" + reportId + "'";
            retObj = super.execSelectSQL(grqry);
            String graphIds = "";
            for (int j = 0; j < retObj.getRowCount(); j++) {
                if (j == 0) {
                    graphIds = retObj.getFieldValueString(j, 0);
                } else {
                    graphIds = graphIds + "," + retObj.getFieldValueString(j, 0);
                }
            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                deleteReportGraphDetailsQuery = "delete  from PRG_AR_GRAPH_DETAILS  where graph_id in ( '" + graphIds + "' ) ";
            } else {
                deleteReportGraphDetailsQuery = "delete  from PRG_AR_GRAPH_DETAILS  where graph_id in ( select DISTINCT a.graph_id from PRG_AR_GRAPH_DETAILS a ,PRG_AR_GRAPH_MASTER b where a.graph_id= b.graph_id and b.report_id ='" + reportId + "' ) ";
            }
//            }
            String deletewhatifsQuery = "delete from PRG_AR_WHATIFS where report_id='" + reportId + "'";
            String deleteSegmentValues = "delete from PRG_AR_DIMENSION_SEGMENT where report_id=" + reportId + "";
            String deleteParamSecurity = "delete from PRG_AR_PARAMETER_SECURITY where REPORT_ID=" + reportId;
            String deleteMapDetails = "delete from prg_ar_report_map_details where REPORT_ID=" + reportId;
            queries.add(deleteReportTimeDetailsQuery);
            queries.add(deleteReportTimeDimensionsQuery);
            queries.add(deleteReportViewByMasterQuery);
            queries.add(deleteReportViewByDetailsQuery);
            queries.add(deleteReportTableMasterQuery);
            queries.add(deleteReportTableDetailsQuery);
            queries.add(deleteReportColors);
            queries.add(deleteExcelCellQuery);
//        queries.add(deleteReportGraphMasterQuery);
            queries.add(deleteReportGraphDetailsQuery);
            queries.add(deleteSegmentValues);
            queries.add(deletewhatifsQuery);
            queries.add(deleteParamSecurity);
            queries.add(deleteMapDetails);
            result = executeMultiple(queries);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return result;
    }

    public void updateMOdifiedUserDetails(String oneviewID, String userId, String roleIds) {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat datfrmt = new SimpleDateFormat();
        datfrmt.applyPattern("dd/MM/yyyy");
        String dat = datfrmt.format(date.getTime());
        String finalqry = getResourceBundle().getString("updateModifiedDetails");
        Object[] obj = new Object[4];
        String finalQuery = null;
        try {
            obj[0] = dat;
            obj[1] = userId;
            obj[2] = roleIds;
            obj[3] = oneviewID;
            finalQuery = buildQuery(finalqry, obj);
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public String getRoleNames(String viewroleIds, String userId) {
        String finalQuery = null;
        PbReturnObject retObj = null;
        String result = "";
        Object[] obj = new Object[1];
        if (viewroleIds != null && !viewroleIds.equalsIgnoreCase("")) {
            String roleNameqry = "select distinct FOLDER_NAME from PRG_USER_ALL_INFO_DETAILS where FOLDER_ID in(" + viewroleIds + ")";
            try {
                retObj = execSelectSQL(roleNameqry);
                if (retObj != null) {
                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        if (!result.equalsIgnoreCase("")) {
                            result += "," + retObj.getFieldValueString(i, "FOLDER_NAME");
                        } else {
                            result = "," + retObj.getFieldValueString(i, "FOLDER_NAME");
                        }
                    }
                }
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
            logger.error("Exception", ex);
            }
        }
        if (result.equalsIgnoreCase("")) {
            return result;
        } else {
            return result.substring(1);
        }
    }

    public String getUserNames(String userId) {
        String finalQuery = null;
        PbReturnObject retObj = null;
        String result = "";
        Object[] obj = new Object[1];
        if (userId != null && !userId.equalsIgnoreCase("")) {
            String roleNameqry = "select PU_FIRSTNAME from PRG_AR_USERS where PU_ID = " + userId;
            try {
                retObj = execSelectSQL(roleNameqry);
                if (retObj != null) {
                    result = retObj.getFieldValueString(0, "PU_FIRSTNAME");
                }
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
            logger.error("Exception", ex);
            }
        }
        return result;
    }

    public ArrayList getOneViewAssignment(String roleIds, int count) {
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String result = "";
        String result1 = "";
        ArrayList alist = new ArrayList();
        String qry = "SELECT PU_ID,PU_LOGIN_ID FROM PRG_AR_USERS WHERE PU_ID IN(SELECT USER_ID FROM PRG_GRP_USER_FOLDER_ASSIGNMENT WHERE USER_FOLDER_ID IN(" + roleIds + ") GROUP BY USER_ID HAVING COUNT(USER_FOLDER_ID)  = " + count + ")";

        try {
            retObj = execSelectSQL(qry);
            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (!result.equalsIgnoreCase("")) {
                        result += "," + retObj.getFieldValueString(i, "PU_ID");
                        result1 += "," + retObj.getFieldValueString(i, "PU_LOGIN_ID");
                    } else {
                        result = "," + retObj.getFieldValueString(i, "PU_ID");
                        result1 = "," + retObj.getFieldValueString(i, "PU_LOGIN_ID");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (result != "" && result1 != "") {
            alist.add(result.substring(1));
            alist.add(result1.substring(1));
        }
        return alist;
    }

    public String assignOneView(String oneviewId, String userIdArray, String fileName, String filePath) {
        String insertTMDetQuery = getResourceBundle().getString("insertOneViewAssignDetail");
        Object[] reportDets;
        String result = "";
        int seq = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            try {
                seq = super.getSequenceNumber("select PRG_AR_ONEVIEW_ASSIGNMENT_SEQ.NEXTVAL FROM DUAL");
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            reportDets = new Object[5];
            reportDets[0] = seq;
            reportDets[1] = oneviewId;
            reportDets[2] = userIdArray;
            reportDets[3] = fileName;
            reportDets[4] = filePath;

        } else {
            reportDets = new Object[4];
            reportDets[0] = oneviewId;
            reportDets[1] = userIdArray;
            reportDets[2] = fileName;
            reportDets[3] = filePath;
        }
        String finalQuery = "";

        try {
            finalQuery = buildQuery(insertTMDetQuery, reportDets);
            execModifySQL(finalQuery);
            result = "success";
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
            result = "failure";
        }
        return result;
    }

    public ArrayList getAssignedUsersforView(String oneviewId) {
        String assignQry = "select PU_ID,PU_FIRSTNAME from PRG_AR_USERS where PU_ID in(select distinct USER_ID from PRG_AR_ONEVIEW_ASSIGNMENT where ONEVIEW_ID = " + oneviewId + ")";
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            retObj = execSelectSQL(assignQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                retObj.getFieldValueString(i, "PU_ID");
                alist.add(retObj.getFieldValueString(i, "PU_FIRSTNAME"));
            }
        }
        return alist;
    }

    public ArrayList getAssignedUsersforViewIds(String oneviewId) {
        String assignQry = "select PU_ID,PU_FIRSTNAME from PRG_AR_USERS where PU_ID in(select distinct USER_ID from PRG_AR_ONEVIEW_ASSIGNMENT where ONEVIEW_ID = " + oneviewId + ")";
        PbReturnObject retObj = null;
        ArrayList alist = new ArrayList();
        try {
            retObj = execSelectSQL(assignQry);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                retObj.getFieldValueString(i, "PU_ID");
                alist.add(retObj.getFieldValueString(i, "PU_ID"));
            }
        }
        return alist;
    }

    public String saveAsNewOneView(String oldIneViewId, String newOneViewName, HttpSession session, HttpServletRequest request, String userId) {

        String oldContainerFile = null;//= getOneviewFileName(oldIneViewId);
        String oldRegionFile = null;//= getRegionOneview(oldIneViewId);
        OnceViewContainer oldOneContainer = null;
        String oldRegionFileDetails = "";
        HashSet roleIdSet = new HashSet();
        StringBuilder roleIdString = new StringBuilder();
        String roleIdsString = "";
        String result = "OneView successfully created.";
        String oneVersion = "1.1";
        String filePath = "";
        String newOneViewId = insertOneViewName(newOneViewName);
        OneViewBD viewBd = new OneViewBD();
        String advFileProps = "";
        String changedFileProps = advFileProps;
        PbReturnObject retObj = getOneviewFileNam(oldIneViewId);
        if (retObj != null && retObj.rowCount > 0) {
            oldContainerFile = retObj.getFieldValueString(0, "ONEVIEW_FILE");
            oldRegionFile = retObj.getFieldValueString(0, "ONEVIEW_REGIONSDATA");
            oneVersion = retObj.getFieldValueString(0, "ONE_VERSION");
            filePath = retObj.getFieldValueString(0, "FILEPATH");
        }

        try {
            InputStream servletStream = session.getServletContext().getResourceAsStream("/WEB-INF/classes/cache.ccf");
            if (servletStream != null) {
                try {
                    Properties fileProps = new Properties();
                    fileProps.load(servletStream);
                    advFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                    changedFileProps = fileProps.getProperty("jcs.auxiliary.DC.attributes.DiskPath");
                } catch (Exception e) {
                    logger.error("Exception: ", e);
                }
            }
            if (Float.parseFloat(oneVersion) <= 2.0f) {
            } else {
                if (Float.parseFloat(oneVersion) >= 2.0f) {
                    /*
                     * This block is to migrate 2.0 to 2.1
                     */
 /*
                     * belowe code is create the directory for oneview to plavce
                     * the inner files
                     */
                    String folderName = newOneViewName + "_" + newOneViewId + "_" + new SimpleDateFormat("mm-dd-yy").format(new Date());
                    String folderPath = advFileProps + "/" + folderName;
                    File folderDir = new File(folderPath);
                    if (!folderDir.exists()) {
                        folderDir.mkdir();
                        changedFileProps = folderPath;
                    }
                    /*
                     * dirctory created
                     */
                }
                if (Float.parseFloat(oneVersion) > 2.0f) {
                    advFileProps = filePath;
                }

            }
            /*
             * FileInputStream fis2 = new
             * FileInputStream(advFileProps+"/"+oldContainerFile);
             * ObjectInputStream ois2 = new ObjectInputStream(fis2);
             * oldOneContainer = (OnceViewContainer) ois2.readObject();
             * ois2.close();
             */
            oldOneContainer = (OnceViewContainer) viewBd.readFileDetails(advFileProps, oldContainerFile);
            List<OneViewLetDetails> dashletDetails1 = oldOneContainer.onviewLetdetails;
            for (OneViewLetDetails viewlet : dashletDetails1) {
                roleIdSet.add(viewlet.getRoleId());
                viewlet.setOneviewId(newOneViewId);
            }
            Iterator roleIter = roleIdSet.iterator();
            while (roleIter.hasNext()) {
                roleIdString.append(",").append(roleIter.next());
            }
            if (roleIdString.length() >= 1) {
                roleIdsString = roleIdString.substring(1);
            }

            if (Float.parseFloat(oneVersion) < 2.0f) {

                File file = new File(advFileProps + "/" + oldRegionFile);
                if (file.exists()) {
                    FileInputStream fis3 = new FileInputStream(advFileProps + "/" + oldRegionFile);
                    ObjectInputStream ois3 = new ObjectInputStream(fis3);
                    oldRegionFileDetails = (String) ois3.readObject();
                    ois3.close();
                } else {
                    result = " OneView your trying to copy is not exists in system..!!";
                }

                String newOneviewFileName = "OneviewDetails" + newOneViewId + "_" + System.currentTimeMillis() + ".txt";
                String newOneviewRegionFileName = "OneviewRegionDetails" + newOneViewId + "_" + System.currentTimeMillis() + ".txt";
                //inserting Onecontainer into database and file System
                FileOutputStream fos1 = new FileOutputStream(advFileProps + "/" + newOneviewFileName);
                ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
                oos1.writeObject(oldOneContainer);
                oos1.flush();
                oos1.close();
                updateOneviewData(newOneviewFileName, newOneViewId, "1.1", "null");

                //updating Region File Data
                FileOutputStream fos = new FileOutputStream(advFileProps + "/" + newOneviewRegionFileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(oldRegionFileDetails);
                oos.flush();
                oos.close();
                upDateOneviewRegionData(newOneviewRegionFileName, newOneViewId, newOneViewName, userId, roleIdsString, filePath);

            }
            if (Float.parseFloat(oneVersion) == 2.0f || Float.parseFloat(oneVersion) > 2.0f) {
                String finalqry = getResourceBundle().getString("insertOneviewRegiondata");

                ArrayList queryList = new ArrayList();
                String query = "";
                HashMap oldfileMap = oldOneContainer.getRegHashMap();
                HashMap newFileMap = new HashMap();
                Object[] obj = new Object[3];

                for (int i = 0; i < oldfileMap.size(); i++) {
                    String oldRegFile = oldfileMap.get(i).toString();
                    String newRegFile = "InnerRegionDetails" + newOneViewId + "_" + i + "_" + System.currentTimeMillis() + ".txt";
                    newFileMap.put(i, newRegFile);
                    obj[0] = newOneViewId;
                    obj[1] = i;
                    obj[2] = newRegFile;
                    query = buildQuery(finalqry, obj);
                    queryList.add(query);
                    if (Float.parseFloat(oneVersion) == 2.5f) {
                    } else {
                        String innerRegFile = (String) viewBd.readFileDetails(advFileProps, oldRegFile);
                        viewBd.writeFileDetails(changedFileProps, newRegFile, innerRegFile);
                    }
                }
                new PbDb().executeMultiple(queryList);

                oldOneContainer.SetRegHashMap(newFileMap);
                //sandeep
                if (Float.parseFloat(oneVersion) == 2.5f) {
                    String oneviewtypedate = (String) session.getAttribute("oneviewdatetype");
                    Map<String, List<String>> allFilters = (Map<String, List<String>>) session.getAttribute("allFilters");
                    Map<String, List<String>> allFiltersnames = (Map<String, List<String>>) session.getAttribute("allFiltersnames");
                    ArrayList viewbygblname = (ArrayList) session.getAttribute("viewbynames");
                    ArrayList parameterlist = (ArrayList) session.getAttribute("parameterlist");
                    if (allFilters != null) {
                        oldOneContainer.setallFilters(allFilters);
                        oldOneContainer.setallFiltersnames(allFiltersnames);
                    }
                    if (viewbygblname != null) {

                        oldOneContainer.setviewbygblname(viewbygblname);
                        oldOneContainer.setparameterlist(parameterlist);
                    }
                    Gson gson = new Gson();
                    if (request.getParameter("filters1") != null) {
                        boolean flag = false;
                        if ((request.getParameter("isNewReport") != null && request.getParameter("isNewReport").equalsIgnoreCase("true"))) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                        if (!flag) {
                            Type tarType1111 = new TypeToken<Map<String, List<String>>>() {
                            }.getType();
                            Map<String, List<String>> map1 = gson.fromJson(request.getParameter("filters1"), tarType1111);
                            oldOneContainer.setFilterMap(map1);
                        }
                    }
                }

//end of sandeep code
                String newOneviewFileName = "OneviewDetails" + newOneViewId + "_" + System.currentTimeMillis() + ".txt";
                viewBd.writeFileDetails(changedFileProps, newOneviewFileName, oldOneContainer);
                /*
                 * Writing New Container
                 */
                String newOneviewRegionFileName = "OneviewRegionDetails" + newOneViewId + "_" + System.currentTimeMillis() + ".txt";
                String oldMainRegFile = (String) viewBd.readFileDetails(advFileProps, oldRegionFile);
                viewBd.writeFileDetails(changedFileProps, newOneviewRegionFileName, oldMainRegFile);
                /*
                 * Writing DataRegionFile
                 */
                if (Float.parseFloat(oneVersion) == 2.5f) {
                    updateOneviewData(newOneviewFileName, newOneViewId, oneVersion, changedFileProps);
                } else {
                    updateOneviewData(newOneviewFileName, newOneViewId, session.getAttribute("OneViewVersion").toString(), changedFileProps);
                }

                upDateOneviewRegionData(newOneviewRegionFileName, newOneViewId, newOneViewName, userId, roleIdsString, filePath);
            }
            //sandeep
            PbReportViewerDAO dao = new PbReportViewerDAO();
            XtendAdapter adapter = new XtendAdapter();
            dashletDetails1 = oldOneContainer.onviewLetdetails;
            for (OneViewLetDetails detail : dashletDetails1) {
                String repname = detail.getRepName();
                String repid = detail.getRepId();
                String regid = detail.getNoOfViewLets();
                String chartname = detail.getchartname();
                Container container = null;
                String report = dao.geroneviewcharts(repid, repname, container, oldIneViewId, detail.getNoOfViewLets());
                Map map = new HashMap();
                Gson gson = new Gson();
                Type tarType1 = new TypeToken<Map<String, String>>() {
                }.getType();
                map = gson.fromJson(report, tarType1);
                String data = (String) map.get("data");
                String meta = (String) map.get("meta");
                Map<String, String> dataMapgblsave = new HashMap<String, String>();
                if (request.getSession(false).getAttribute("dataMapgblsave") != null) {
                    dataMapgblsave = (Map<String, String>) request.getSession(false).getAttribute("dataMapgblsave");
//              }
                    String data1 = dataMapgblsave.get(regid);
                    FileReadWrite fileReadWrite = new FileReadWrite();
                    File datafile = new File("/usr/local/cache/OneviewGO/oneview_" + oldIneViewId + "/oneview_" + oldIneViewId + "_" + regid + "/" + repname.replaceAll("\\W", "").trim() + "_" + repid + ".json");
                    if (datafile.exists() && data1 != null) {
//           fileReadWrite.writeToFile("/usr/local/cache/OneviewGO/oneview_"+oneviewid+"/oneview_"+oneviewid+"_"+regid+"/"+reportName.replaceAll("\\W", "").trim()+"_"+reportId+".json", data1);
                        data = data1;
                    }
                    adapter.saveoneviewChartMeta(repname, repid, meta, data, newOneViewId, regid, "null", "null", chartname);
//                }
                } else {
                    adapter.saveoneviewChartMeta(repname, repid, meta, data, newOneViewId, regid, "null", "null", chartname);
                }
            }
            //end of sandeep code for save as new oneview with newoneview
        } catch (IOException ex) {
            result = "oneView cannot be created Problem Occured..!!";
            logger.error("Exception: ", ex);
        } catch (Exception ex) {
             result = "oneView cannot be created Problem Occured..!!";
            logger.error("Exception: ", ex);
        }
        return newOneViewId;
    }

    public void deletePrevioueUsers(String oneviewId) {
        String qry = "delete from PRG_AR_ONEVIEW_ASSIGNMENT where ONEVIEW_ID = " + oneviewId + "";
        try {
            execModifySQL(qry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public boolean saveOneViewSequence(String[] idArray, String userId) {
        String qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = '&' where ONEVIEW_ID = '&' and USER_ID = '&'";
        boolean flag = false;
        ArrayList queries = new ArrayList();
        for (int i = 0; i < idArray.length; i++) {
            qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = '" + (i + 1) + "' where ONEVIEW_ID = '" + idArray[i] + "' and USER_ID='" + userId + "'";
            queries.add(qry);
        }
        flag = executeMultiple(queries);
        return flag;
    }

    public PbReturnObject getOneViewsBasedOnSequence(String userId) {
        PbReturnObject retOjb = null;
        Object obj[] = new Object[1];
        obj[0] = userId;
        String finalQuery = getResourceBundle().getString("getOneviewsBasedOnSequence");
        String finalQuery1 = buildQuery(finalQuery, obj);
        try {
            //
            retOjb = execSelectSQL(finalQuery1);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retOjb;
    }

    public PbReturnObject getOneViewsBasedOnSequence1(String userId) {
        PbReturnObject retOjb = null;
        Object obj[] = new Object[1];
        obj[0] = userId;
        String finalQuery = getResourceBundle().getString("getOneviewsBasedOnSequence1");
        String finalQuery1 = buildQuery(finalQuery, obj);
        try {
            //
            retOjb = execSelectSQL(finalQuery1);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retOjb;
    }

    public PbReturnObject getOneViewsBasedOnSequencemysql(String userId) {
        PbReturnObject retOjb = null;
        Object obj[] = new Object[1];
        obj[0] = userId;
        String finalQuery = "SELECT ONEVIEW_ID FROM PRG_AR_ONEVIEW_ASSIGNMENT WHERE user_id IS NOT NULL and user_id = '&' and ASSIGN_TAB_SEQUENCE IS NOT NULL ORDER BY ASSIGN_TAB_SEQUENCE";
        String finalQuery1 = buildQuery(finalQuery, obj);
        try {
            //
            retOjb = execSelectSQL(finalQuery1);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return retOjb;
    }

    public void updateTabSequences(String userId) {
        String qry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER) || ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
            qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = null where USER_ID = '" + userId + "'";
        } else {
            qry = "update PRG_AR_ONEVIEW_ASSIGNMENT set ASSIGN_TAB_SEQUENCE = '' where USER_ID = '" + userId + "'";
        }
        try {
            execModifySQL(qry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public boolean deleteParameterRegionQuerys(int reportId) {
        ArrayList queries = new ArrayList();
        boolean result = false;
        String deleteReportGraphDetailsQuery = "";
        String deleteReportViewByDetailsQuery = "";
        try {
//        String deleteReportTimeDetailsQuery = "delete from PRG_AR_REPORT_TIME_DETAIL where rep_time_id in ( select rep_time_id from PRG_AR_REPORT_TIME where report_id='" + reportId + "')";
//        String deleteReportTimeDimensionsQuery = "delete from PRG_AR_REPORT_TIME where report_id='" + reportId + "'";
            String deleteReportViewByMasterQuery = "delete from PRG_AR_REPORT_VIEW_BY_MASTER where report_id ='" + reportId + "'";
            String qry = "select DISTINCT a.view_by_id from PRG_AR_REPORT_VIEW_BY_DETAILS a,PRG_AR_REPORT_VIEW_BY_MASTER b where a.view_by_id= b.view_by_id and b.report_id=" + reportId;
            PbReturnObject retObj = super.execSelectSQL(qry);
            String viewbyIds = "";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i == 0) {
                    viewbyIds = retObj.getFieldValueString(i, 0);
                } else {
                    viewbyIds = viewbyIds + "," + retObj.getFieldValueString(i, 0);
                }

            }
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in ('" + viewbyIds + "')";
            } else {
                deleteReportViewByDetailsQuery = "delete from PRG_AR_REPORT_VIEW_BY_DETAILS where VIEW_BY_ID in (select DISTINCT a.view_by_id from PRG_AR_REPORT_VIEW_BY_DETAILS a,PRG_AR_REPORT_VIEW_BY_MASTER b where a.view_by_id= b.view_by_id and b.report_id=" + reportId + ")";
            }
//        queries.add(deleteReportTimeDetailsQuery);
//        queries.add(deleteReportTimeDimensionsQuery);
            queries.add(deleteReportViewByMasterQuery);
            queries.add(deleteReportViewByDetailsQuery);
            result = executeMultiple(queries);
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return result;
    }

    public void updateReportParamDetails(int reportId, String paramsString, Container container) {
        String[] paramIds = paramsString.split(",");
        PbReportCollection collect = container.getReportCollect();
        OraclePreparedStatement opstmt = null;
        PreparedStatement sqlstmt = null;
        Connection conn = null;
        try {
            conn = ProgenConnection.getInstance().getConnection();
            Gson gson = new Gson();
            /*
             * this code is added by srikanth.p
             */
            Type tarType = new TypeToken<List<String>>() {
            }.getType();
            for (int i = 0; i < paramIds.length; i++) {
                String updateReportParamDetailsqry = "UPDATE PRG_AR_REPORT_PARAM_DETAILS SET DEFAULT_VALUE=?,PARAMETER_TYPE=? where REPORT_ID=? and ELEMENT_ID=?";
                List<String> defaultList = collect.getDefaultValue(paramIds[i]);
                String defaultValStr = "[\"All\"]";
                if (defaultList != null && !defaultList.isEmpty()) {
                    defaultValStr = gson.toJson(defaultList, tarType);
                    // opstmt.setStringForClob(1, defaultValStr);
                }
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.ORACLE)) {
                    opstmt = (OraclePreparedStatement) conn.prepareStatement(updateReportParamDetailsqry);
                    opstmt.setStringForClob(1, defaultValStr);
                    opstmt.setString(2, collect.getParameterStatus(paramIds[i]));
                    opstmt.setInt(3, reportId);
                    opstmt.setInt(4, Integer.parseInt(paramIds[i]));
                    opstmt.executeUpdate();
                } else {
                    // conn = ProgenConnection.getInstance().getConnection();
                    sqlstmt = (PreparedStatement) conn.prepareStatement(updateReportParamDetailsqry);

                    sqlstmt.setString(1, defaultValStr);

                    sqlstmt.setString(2, collect.getParameterStatus(paramIds[i]));
                    sqlstmt.setInt(3, reportId);
                    sqlstmt.setInt(4, Integer.parseInt(paramIds[i]));
                    sqlstmt.executeUpdate();
                }
            }
            if (conn != null) {
                if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.ORACLE)) {
                    conn.commit();
                    opstmt.close();
                    opstmt = null;
                    conn.close();
                    conn = null;
                } else {
                    sqlstmt.close();
                    sqlstmt = null;
                    conn.close();
                    conn = null;
                }
            }

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

    }

    public ArrayList<String> getQryColumnsFromDB(int reportId) {
        ArrayList<String> elementIds = new ArrayList<String>();
        try {
            PbReturnObject retObj = null;
            String qry = "select element_id from PRG_AR_QUERY_DETAIL where report_id=" + reportId;
            retObj = executeSelectSQL(qry);
            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    elementIds.add(retObj.getFieldValueString(i, 0));
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return elementIds;
    }

    public void updateQryColSequence(int reportId, ArrayList<String> QryColumns) {
        String qry = null;
        ArrayList qryList = new ArrayList();
        if (QryColumns != null && !QryColumns.isEmpty()) {
            try {
                for (int i = 0; i < QryColumns.size(); i++) {
                    qry = "update PRG_AR_QUERY_DETAIL set col_seq=" + (i + 1) + "where element_id=" + QryColumns.get(i) + "and report_id=" + reportId;
                    qryList.add(qry);
//          execUpdateSQL(qry);
                }
                executeMultiple(qryList);
            } catch (Exception ex) {
                logger.error("Exception", ex);
            }
        }
    }

    public void removeQryColumns(int reportId, ArrayList<String> QryColumns) {
        try {
            String qry = null;
            qry = "delete from PRG_AR_QUERY_DETAIL where element_id in(" + Joiner.on(",").join(QryColumns.toArray(new String[QryColumns.size()])) + ") and report_id=" + reportId;
            execModifySQL(qry);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    public void removeTableDetails(int reportId) {
        ArrayList queries = new ArrayList();
        String deleteReportTableMasterQuery = "delete from PRG_AR_REPORT_TABLE_MASTER where report_id='" + reportId + "'";
        String deleteReportTableDetailsQuery = "delete from PRG_AR_REPORT_TABLE_DETAILS where report_id='" + reportId + "'";
        String deleteReportColors = "delete from PRG_AR_REPORT_COLORS where REPORT_ID=" + reportId;
        String deleteExcelCellQuery = "delete from PRG_AR_EXCEL_CELL_PROPS where report_id=" + reportId;
        String deletewhatifsQuery = "delete from PRG_AR_WHATIFS where report_id='" + reportId + "'";
        String deleteSegmentValues = "delete from PRG_AR_DIMENSION_SEGMENT where report_id=" + reportId + "";
        String deleteParamSecurity = "delete from PRG_AR_PARAMETER_SECURITY where REPORT_ID=" + reportId;
        //sString deleteExcelColQuery = "delete from PRG_AR_EXCEL_COLUMNS where report_id=" + reportId;
        queries.add(deleteReportTableMasterQuery);
        queries.add(deleteReportTableDetailsQuery);
        queries.add(deleteReportColors);
        queries.add(deleteExcelCellQuery);
        queries.add(deletewhatifsQuery);
        queries.add(deleteSegmentValues);
        queries.add(deleteParamSecurity);
        executeMultiple(queries);

    }

    public void updateReportMaster(int reportId, Container container) {
        String upadteReportMasterqry = getResourceBundle().getString("UpdateReportMasterforLocalSave");
        String finalQuery;
        List<String> customSeq = container.getReportCollect().getCustomSequence();
        HashMap<String, ArrayList<String>> transposeFormatMap = container.getReportCollect().getTransposeFormatMap();
        HashMap<String, HashMap<String, ArrayList<String>>> targValue = container.getReportCollect().getGoalSeekBasicandAdhoc();
        HashMap<String, ArrayList<String>> goalPercent = container.getReportCollect().getGoalandPercent();
        List<String> rowViewByValues = container.getReportCollect().getViewByValues();
        String groupName = container.getReportCollect().getGroupName();
        List<String> ElemeIds = container.getReportCollect().getGlobalValues();
        HashMap<String, HashMap<String, ArrayList<String>>> goalTimeIndiv = container.getReportCollect().getGoalSeekTimeIndvidual();
        List<String> newProdlist = container.getReportCollect().getProdAndViewName();
        Gson gson = new Gson();
        Object[] reportMaster = new Object[10];
        reportMaster[0] = gson.toJson(customSeq);
        reportMaster[1] = gson.toJson(transposeFormatMap);
        reportMaster[2] = gson.toJson(targValue);
        reportMaster[3] = gson.toJson(goalPercent);
        reportMaster[4] = Joiner.on(",").join(rowViewByValues);
        reportMaster[5] = groupName;
        reportMaster[6] = Joiner.on(",").join(ElemeIds);
        reportMaster[7] = gson.toJson(goalTimeIndiv);
        reportMaster[8] = gson.toJson(newProdlist);
        reportMaster[9] = reportId;
        finalQuery = buildQuery(upadteReportMasterqry, reportMaster);
        try {
            execUpdateSQL(finalQuery);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        ArrayList paramIds = (ArrayList) container.getParametersHashMap().get("Parameters");
        if ((container.getReportCollect().dateoptions != null && !container.getReportCollect().dateoptions.isEmpty()) || (container.getReportCollect().dateSubStringValues != null && !container.getReportCollect().dateSubStringValues.isEmpty()) || (container.getReportCollect().dateFormatt != null && !container.getReportCollect().dateFormatt.isEmpty())) {
            String[] params;
            Object[] Ids = paramIds.toArray();
            params = Arrays.copyOf(Ids, Ids.length, String[].class);
            String qry = "update PRG_AR_REPORT_PARAM_DETAILS set DATE_TIME_OPTION='&',DATA_SUBSTRING='&',DATE_FORMAT='&' where ELEMENT_ID='&' and REPORT_ID='&'";
            Object[] obj = new Object[5];
            ArrayList querys = new ArrayList();
            for (int i = 0; i < params.length; i++) {
                if (container.getDateandTimeOptions("A_" + params[i]) != null && !container.getDateandTimeOptions("A_" + params[i]).equalsIgnoreCase("null") && !"".equals(container.getDateandTimeOptions("A_" + params[i]))) {
                    obj[0] = container.getDateandTimeOptions("A_" + params[i]);
                } else {
                    obj[0] = null;
                }
                if (container.getDateSubStringValues("A_" + params[i]) != null && !container.getDateSubStringValues("A_" + params[i]).equalsIgnoreCase("null") && !"".equals(container.getDateSubStringValues("A_" + params[i]))) {
                    obj[1] = container.getDateSubStringValues("A_" + params[i]);
                } else {
                    obj[1] = null;
                }
                if (container.getDateFormatt("A_" + params[i]) != null && !container.getDateFormatt("A_" + params[i]).equalsIgnoreCase("null") && !"".equals(container.getDateFormatt("A_" + params[i]))) {
                    obj[2] = container.getDateFormatt("A_" + params[i]);
                } else {
                    obj[2] = null;
                }
                obj[3] = params[i];
                obj[4] = reportId;
                finalQuery = buildQuery(qry, obj);
                querys.add(finalQuery);
            }
            executeMultiple(querys);

        }
        try {
            String qry = "update PRG_AR_REPORT_PARAM_DETAILS set PARAM_DISP_NAME='&' where ELEMENT_ID='&' and REPORT_ID='&'";
            Object[] obj;
            ArrayList RepElementIds = (ArrayList) container.getTableHashMap().get("REP");
            ArrayList RepNames = new ArrayList();
            for (int i = 0; i < RepElementIds.size(); i++) {
                RepNames.add(container.getDisplayLabels().get(i));
            }

            if (RepElementIds != null && RepElementIds.size() > 0) {
                obj = new Object[3];
                String finalQuery1 = "";
                for (int i = 0; i < RepElementIds.size(); i++) {
                    obj[0] = RepNames.get(i);
                    obj[1] = RepElementIds.get(i);
                    obj[2] = reportId;
                    finalQuery1 = buildQuery(qry, obj);
                    execUpdateSQL(finalQuery1);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }

    //Added By Ram 22Desc2015 for getting UnMeasure ViewBys.
    public String getUnMeasureViewBys(String repId, String paramslist) {
        String sql = null;
        Object[] obj = null;
        String finalQuery = "";
        PbReturnObject retObj = new PbReturnObject();
        if (!paramslist.isEmpty()) {
            sql = getResourceBundle().getString("getUnMeasureViewBys");
            obj = new Object[2];
            obj[0] = repId;
            obj[1] = paramslist;
        }
        finalQuery = buildQuery(sql, obj);
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        String UnMeasureViewBys = "";
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i == 0) {
                    UnMeasureViewBys = retObj.getFieldValueString(i, 0);
                } else {
                    UnMeasureViewBys += "," + retObj.getFieldValueString(i, 0);
                }
            }
        }
        return UnMeasureViewBys;
    }

    public ArrayList getMeasureAsViewBys(String repId, String paramslist) {
        String sql = null;
        Object[] obj = null;
        String finalQuery = "";
        PbReturnObject retObj = new PbReturnObject();
        if (!paramslist.isEmpty()) {
            sql = getResourceBundle().getString("getMeasureAsViewBys");
            obj = new Object[2];
            obj[0] = repId;
            obj[1] = paramslist;
        }
        finalQuery = buildQuery(sql, obj);
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        ArrayList MeasureViewBys = new ArrayList();
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MeasureViewBys.add(retObj.getFieldValueString(i, 0));

            }
        }
        return MeasureViewBys;
    }

    public HashMap getTableNames(String currentBizRoles, String tabList) {
        HashMap namesList = new HashMap();
        String sql = null;
        Object[] obj = null;
        String finalQuery = "";
        String[] colNames = null;
        PbReturnObject retObj = new PbReturnObject();
        if (!tabList.isEmpty()) {
            sql = getResourceBundle().getString("getFactsNew1");
            //String qry = "select distinct BUSS_TABLE_ID, TABLE_DISP_NAME from  PRG_USER_ALL_INFO_DETAILS where BUSS_TABLE_ID in ("+tableList+") and TABLE_DISP_NAME is not null";
            obj = new Object[2];
            obj[0] = currentBizRoles;
            obj[1] = tabList;
        } else {
            sql = getResourceBundle().getString("getAllFactsNew1");
            obj = new Object[1];
            obj[0] = currentBizRoles;
        }
        finalQuery = buildQuery(sql, obj);
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        colNames = retObj.getColumnNames();
        String tabNameList = "";
        String tabIdList = "";
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i == 0) {
                    tabNameList = retObj.getFieldValueString(i, 1);
                    tabIdList = retObj.getFieldValueString(i, 0);
                } else {
                    tabNameList += "," + retObj.getFieldValueString(i, 1);
                    tabIdList += "," + retObj.getFieldValueString(i, 0);
                }
            }
        }
        namesList.put("tabIdList", tabIdList);
        namesList.put("tabNameList", tabNameList);
        return namesList;
    }

    public HashMap getTableNamesForAO(String aoid, String tabList) {
        HashMap namesList = new HashMap();
        String sql = null;
        Object[] obj = null;
        String finalQuery = "";
        String[] colNames = null;
        PbReturnObject retObj = new PbReturnObject();
        if (!tabList.isEmpty()) {
            sql = getResourceBundle().getString("getFactsForAO");
            //String qry = "select distinct BUSS_TABLE_ID, TABLE_DISP_NAME from  PRG_USER_ALL_INFO_DETAILS where BUSS_TABLE_ID in ("+tableList+") and TABLE_DISP_NAME is not null";
            obj = new Object[1];
            obj[0] = aoid;

        } else {
            sql = getResourceBundle().getString("getFactsForAO");
            obj = new Object[1];
            obj[0] = aoid;
        }
        finalQuery = buildQuery(sql, obj);
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        colNames = retObj.getColumnNames();
        String tabNameList = "";
        String tabIdList = "";
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                if (i == 0) {
                    tabNameList = retObj.getFieldValueString(i, 1);
                    tabIdList = retObj.getFieldValueString(i, 0);
                } else {
                    tabNameList += "," + retObj.getFieldValueString(i, 1);
                    tabIdList += "," + retObj.getFieldValueString(i, 0);
                }
            }
        }
        namesList.put("tabIdList", tabIdList);
        namesList.put("tabNameList", tabNameList);
        return namesList;
    }

    public Boolean saveSelectedParamDetails(String reportId, String paramIdsArray) {
        String qry1 = "update PRG_AR_REPORT_PARAM_DETAILS set SELECTED_PARAM_DETAILS = 'N' where REPORT_ID = '&'";
        String qry = "update PRG_AR_REPORT_PARAM_DETAILS set SELECTED_PARAM_DETAILS = '&' where ELEMENT_ID = '&' and REPORT_ID = '&'";
        String[] arrays = null;
        String finalQuery = "";
        String finalQuery1 = "";
        boolean flag = false;
        Object obj1[] = new Object[1];
        obj1[0] = reportId;
        finalQuery1 = buildQuery(qry1, obj1);
        try {
            execUpdateSQL(finalQuery1);
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
        Object obj[] = new Object[3];
        ArrayList queries = new ArrayList();
        if (paramIdsArray != null && !paramIdsArray.isEmpty()) {
            arrays = paramIdsArray.split(",");
            for (int i = 0; i < arrays.length; i++) {
                obj[0] = 'Y';
                obj[1] = arrays[i];
                obj[2] = reportId;
                finalQuery = buildQuery(qry, obj);
                queries.add(finalQuery);
            }
            flag = executeMultiple(queries);
        }
        return flag;
    }

    public String getSelectedParamDetails(String reportId) {
        String query = "select ELEMENT_ID from PRG_AR_REPORT_PARAM_DETAILS where SELECTED_PARAM_DETAILS = 'Y' and REPORT_ID = '" + reportId + "'";
        PbReturnObject retObj = new PbReturnObject();
        PbDb pbdb = new PbDb();
        String selectedParams = "";
        try {
            retObj = pbdb.execSelectSQL(query);
            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    if (i == 0) {
                        selectedParams = retObj.getFieldValueString(i, "ELEMENT_ID");
                    } else {
                        selectedParams = selectedParams + "," + retObj.getFieldValueString(i, "ELEMENT_ID");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return selectedParams;
    }
    //public void deleteDimensions(String reportId , String dimIds , Container container)

    public void deleteDimensions(String reportId, String dimIds) {
        String getReportParamDetailsQuery = getResourceBundle().getString("deleteReportParamaters");
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        String[] paramIds = dimIds.split(",");

        if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
            Obj = new Object[2];
            Obj[0] = reportId;
            Obj[1] = dimIds;
        } else {
            Obj = new Object[2];
            Obj[0] = reportId;
            Obj[1] = dimIds;
        }

        try {

            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);

            execUpdateSQL(finalQuery);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }

    }

    public ArrayList getSplitValuesForTranspoce(String reportId) {
        ArrayList resultList = new ArrayList();
        String query = "SELECT IS_SPLITBY,SPLIT_VALUE FROM PRG_AR_REPORT_MASTER WHERE REPORT_ID=" + reportId;
        PbReturnObject retObj = new PbReturnObject();
        PbDb pbdb = new PbDb();
        try {
            retObj = pbdb.execSelectSQL(query);
            if (retObj != null) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    String isSplitby = retObj.getFieldValueString(i, "IS_SPLITBY");
                    String splitVal = retObj.getFieldValueString(i, "SPLIT_VALUE");
                    if (isSplitby != null && !isSplitby.equalsIgnoreCase("null") && Boolean.parseBoolean(isSplitby)) {
                        resultList.add(true);
                        resultList.add(splitVal);
                    } else {
                        resultList.add(false);
                    }
                }

            }
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return resultList;
    }

    public void updateDimensions(String reportId, String dimIds) {
        String getReportParamDetailsQuery = getResourceBundle().getString("updateReportParamaters");
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        ArrayList queries = new ArrayList();
        String[] paramIds = dimIds.split(",");

        for (int i = 0; i < paramIds.length; i++) {
            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                Obj = new Object[3];
                Obj[0] = i + 1;
                Obj[1] = reportId;
                Obj[2] = paramIds[i];
            } else {
                Obj = new Object[3];
                Obj[0] = i + 1;
                Obj[1] = reportId;
                Obj[2] = paramIds[i];
            }
            finalQuery = buildQuery(getReportParamDetailsQuery, Obj);
            queries.add(finalQuery);
        }

        try {

            if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                executeMultiple(queries);
            } else {
                execMultiple(queries);
            }
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }

    }

    public String getUserFoldersByUserIdInDesigner(String pbUserId) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = null;

        String FolderId = "";
        String FolderName = "";
        String getUserFoldersByUserIdQuery = getResourceBundle().getString("getUserFoldersByUserId");

        StringBuffer outerBuffer = new StringBuffer("");

        try {
            obj = new Object[1];
            obj[0] = pbUserId;
            finalQuery = buildQuery(getUserFoldersByUserIdQuery, obj);
            retObj = execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() != 0) {
                colNames = retObj.getColumnNames();
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    FolderId = retObj.getFieldValueString(i, colNames[0]);
                    FolderName = retObj.getFieldValueString(i, colNames[1]);
                    outerBuffer.append("<li class='closed' id='" + FolderId + "'>");
                    outerBuffer.append("<input type='radio' name='userfldsList' id='" + FolderId + "'  ><span><font size='1px' face='verdana'><b>" + FolderName + "</b></font></span>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public Boolean getAutometicDate(String reportId) throws SQLException {
        PbReturnObject pbro1 = null;
        PbDb pbdb = new PbDb();
        boolean autometicDate = false;
        String query1 = "select AUTOMETIC_DATE from PRG_AR_REPORT_TIME where report_id=" + reportId;
        pbro1 = pbdb.execSelectSQL(query1);
        if (pbro1 != null && pbro1.rowCount > 0) {

            autometicDate = Boolean.parseBoolean(pbro1.getFieldValueString(0, 0));
        }
        return autometicDate;
    }

    public ArrayList getDefaultDate(String reportId, String isopen) throws SQLException, ParseException {
        PbReturnObject pbro = null;
        PbReturnObject pbro1 = null;
        PbDb pbdb = new PbDb();
        String query = "select REP_TIME_ID from PRG_AR_REPORT_TIME where report_id=" + reportId;
        pbro = pbdb.execSelectSQL(query);
        int repTimeId = pbro.getFieldValueInt(0, 0);
        String query1 = "select DEFAULT_VALUE from PRG_AR_REPORT_TIME_DETAIL where REP_TIME_ID=" + repTimeId;
        pbro1 = pbdb.execSelectSQL(query1);
        ArrayList al = new ArrayList();
        String fromd1 = "";
        for (int i = 0; i < pbro1.getRowCount(); i++) {
            if (pbro1.getFieldValueString(i, 0).contains("+") || pbro1.getFieldValueString(i, 0).contains("-")) {

                if (i == 0) {
                    fromd1 = pbro1.getFieldValueString(i, 0).replace(",", "").replace("fromSysDate", "").replace("toSystDate", "").replace("CmpFrmSysDate", "").replace("cmptoSysDate", "").replace("toglobalDdate", "").replace("CmpFrmglobalDate", "").replace("cmptoglobalDate", "").replace("fromglobalDate", "").replace("+", "");
                    al.add(pbro1.getFieldValueString(i, 0).replace(",", "").replace("fromSysDate", "").replace("toSystDate", "").replace("CmpFrmSysDate", "").replace("cmptoSysDate", "").replace("toglobalDdate", "").replace("CmpFrmglobalDate", "").replace("cmptoglobalDate", "").replace("fromglobalDate", ""));
                } else {
                    int fromdate = Integer.parseInt(fromd1);

                    String attr = pbro1.getFieldValueString(i, 0).replace(",", "").replace("fromSysDate", "").replace("toSystDate", "").replace("CmpFrmSysDate", "").replace("cmptoSysDate", "").replace("toglobalDdate", "").replace("CmpFrmglobalDate", "").replace("cmptoglobalDate", "").replace("fromglobalDate", "").replace("+", "");
                    int toDate1 = Integer.parseInt(attr) - (fromdate);
                    // al.add(pbro1.getFieldValueString(i, 0).replace(",", "").replace("fromSysDate","").replace("toSystDate","").replace("CmpFrmSysDate","").replace("cmptoSysDate","").replace("toglobalDdate","").replace("CmpFrmglobalDate","").replace("cmptoglobalDate","").replace("fromglobalDate", ""));
                    String timie = String.valueOf(toDate1);
                    if (timie.contains("-")) {
                        al.add(toDate1);
                    } else {
                        al.add("+" + toDate1);
                    }
                }
            }
        }
        return al;

    }

    public void saveDesignerDimensions(String reportId, String dimIds, String dimName, Container container) {
        String getReportParamDetailsQuery = getResourceBundle().getString("getReportParamDetails1");
        PbReportCollection collect = container.getReportCollect();
        PbReturnObject retObj = null;
        String[] dbColumns = null;
        String finalQuery = "";
        Object[] Obj = null;
        String[] paramIds = dimIds.split(",");
        String[] paramName = dimName.split(",");
        ArrayList ParamId = new ArrayList();
        ArrayList ParamName = new ArrayList();
        for (int i = 0; i < paramIds.length; i++) {
            ParamId.add(paramIds[i]);
        }
        for (int i = 0; i < paramName.length; i++) {
            ParamName.add(paramName[i]);
        }
        HashMap ParametersHashMap = container.getParametersHashMap();
        ParametersHashMap.put("Parameters", ParamId);
        ParametersHashMap.put("ParametersNames", ParamName);

    }

    public String getFavLiksRegion(HttpSession session, String ctxpath) {

        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReturnObject links = new ProgenWidgetsDAO().getFavReports(Integer.parseInt(userId));
        StringBuilder result = new StringBuilder();

        result.append("<form name='myFormLink' method='post' action='>");
        if (links != null) {
            if (links.getRowCount() <= 10) {
                result.append("<div style='height:auto;'><script>parent.document.getElementById('favFrame').style.height='100%';</script>");
            } else if (links.getRowCount() <= 15) {
                result.append("<script>parent.document.getElementById('favFrame').style.height='100%';</script><div style='height:auto;'>");

            } else if (links.getRowCount() <= 20) {
                result.append("<script>parent.document.getElementById('favFrame').style.height='100%';</script><div style='height:100%;'>");
            }
            result.append("<table width=\"100%\" align='left'>");
            for (int i = 0; i < links.getRowCount(); i++) {
                String message1 = "";
                if (links.getFieldValueString(i, 3) != null && links.getFieldValueString(i, 3).equalsIgnoreCase("R")) {
                    message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";

                    result.append("<tr><td align='left'>");
                    result.append("<a href='javascript:void(0)' title=" + links.getFieldValueString(i, 0) + " onclick=\"viewReportDrill('reportViewer.do?reportBy=viewReport&REPORTID=" + links.getFieldValueInt(i, 1) + "&action=open')\" style='text-decoration:none;font-family:Verdana;font-size:10px;cursor:pointer;'><span style='font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;'>" + message1 + "</span></a></td>");
                    result.append("<td align='right'><a href=" + ctxpath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + links.getFieldValueInt(i, 1) + "&reportDrill=Y class='ui-icon ui-icon-gear' title='Report Drill' ></a></td></tr>");
                } else {
                    message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";
                    result.append("<tr><td align='left'><a href='javascript:void(0)'  title=" + links.getFieldValueString(i, 0) + " onclick=\"viewReportDrill('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + links.getFieldValueInt(i, 1) + "&pagename=" + links.getFieldValueString(i, 0) + "&reportDrill=Y')\" style='text-decoration:none;font-family:Verdana;font-size:10px;cursor:pointer;'><span style='font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;'>" + message1 + "</span></a></td>");
                    result.append("<td align='right'><a href=" + ctxpath + "/dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + links.getFieldValueInt(i, 1) + " class='ui-icon ui-icon-gear' title='Report Drill' ></a></td>");
                }
            }
        }

        result.append("</table></div></form>");
        return result.toString();
    }

//added by krishan pratap
    public String getFavLiksRegionkpi(HttpSession session, String ctxpath) {

        String userId = String.valueOf(session.getAttribute("USERID"));
        PbReturnObject links = new ProgenWidgetsDAO().getFavReports(Integer.parseInt(userId));
        StringBuilder result = new StringBuilder();

        result.append("<form name='myFormLink' method='post' action='>");
        if (links != null) {
            if (links.getRowCount() <= 10) {
                result.append("<div style='height:auto;'><script>parent.document.getElementById('favFrame').style.height='100%';</script>");
            } else if (links.getRowCount() <= 15) {
                result.append("<script>parent.document.getElementById('favFrame').style.height='100%';</script><div style='height:auto;'>");

            } else if (links.getRowCount() <= 20) {
                result.append("<script>parent.document.getElementById('favFrame').style.height='100%';</script><div style='height:100%;'>");
            }
            result.append("<table width=\"100%\" align='left'>");
            for (int i = 0; i < links.getRowCount(); i++) {
                String message1 = "";
                if (links.getFieldValueString(i, 3) != null && links.getFieldValueString(i, 3).equalsIgnoreCase("R")) {
                    message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";

                    result.append("<tr><td style='padding-left:40px;>");
                    result.append("<a href='javascript:void(0)' title=" + links.getFieldValueString(i, 0) + " onclick=\"viewReportDrill('reportViewer.do?reportBy=viewReport&REPORTID=" + links.getFieldValueInt(i, 1) + "&action=open')\" style='text-decoration:none;font-family:Verdana;font-size:10px;cursor:pointer;'><span style='font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;'>" + message1 + "</span></a></td>");
                    result.append("<td align='right'><a href=" + ctxpath + "/reportViewer.do?reportBy=viewReport&REPORTID=" + links.getFieldValueInt(i, 1) + "&reportDrill=Y class='ui-icon ui-icon-gear' title='Report Drill' ></a></td></tr>");
                } else {
                    message1 = (links.getFieldValueString(i, 0).length() <= 25) ? links.getFieldValueString(i, 0) : links.getFieldValueString(i, 0).substring(0, 25) + "..";
                    result.append("<tr><td  style='padding-left:40px;'><a href='javascript:void(0)'  title=" + links.getFieldValueString(i, 0) + " onclick=\"viewReportDrill('dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + links.getFieldValueInt(i, 1) + "&pagename=" + links.getFieldValueString(i, 0) + "&reportDrill=Y')\" style='text-decoration:none;font-family:Verdana;font-size:10px;cursor:pointer;'><span style='font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;'>" + message1 + "</span></a></td>");
                    result.append("<td align='right'><a href=" + ctxpath + "/dashboardViewer.do?reportBy=viewDashboard&REPORTID=" + links.getFieldValueInt(i, 1) + " class='ui-icon ui-icon-gear' title='Report Drill' ></a></td>");
                }
            }
        }

        result.append("</table></div></form>");
        return result.toString();
    }

    public String getDataCall(String userId) throws Exception {
        PbDb pbdb = new PbDb();
        StringBuilder result = new StringBuilder();
//String userId = request.getParameter("userId");
// String tagQuery = "select distinct TAG_ID,TAG_NAME,TAG_DESC from PRG_TAG_MASTER order by TAG_ID";
        String tagQuery = "select distinct TAG_ID,TAG_NAME from PRG_TAG_MASTER "
                + " where user_id = " + userId;
        PbReturnObject tagObj = pbdb.execSelectSQL(tagQuery);
        ArrayList al = new ArrayList();
        result.append("<table  width=\"100%\"  align='left'style='border : 1px solid gray;'>");
        int count = 0;
        if (tagObj != null && tagObj.rowCount > 0) {
//for (int i = 0; i < tagObj.rowCount; i++) {
            for (int i = 0; i < tagObj.rowCount / 2; i++) {
                result.append("<tr bgcolor='#F0F5F8' height='40px'>");
                for (int j = 0; j < 2; j++) {
//al.add(tagObj.getFieldValueString(i, 0));
//map.put("Id" , tagObj.getFieldValueString(i, 0));
                    result.append("<td id='" + tagObj.getFieldValueString(count, 0) + "' onclick='tagreport(this.id)'  align='center' style='border : 1px solid gray '>").append(tagObj.getFieldValueString(count++, 1)).append("</td>");

                }
                result.append("</tr>");

            }
        }
        result.append("</table>");
//return al;

        return result.toString();
    }

    public String getParameterDisplayData(String[] paramIds, String[] paramNames) {
        StringBuffer buffer = new StringBuffer("");
        int j = 0;
        if (paramIds != null) {
            for (int i = 0; i < paramIds.length; i++) {
                buffer.append("<li id='" + paramNames[i] + "^elmnt-" + paramIds[i] + "' class=\"navtitle-hover\" style=\"width: auto; height: auto; color: white;\">");
                buffer.append("<table id='" + paramNames[i] + j + "'>");
                buffer.append("<tbody>");
                buffer.append("<tr><td><a class=\"ui-icon ui-icon-close\" href=\"javascript:deleteDim('" + paramNames[i] + "^elmnt-" + paramIds[i] + "')\"> a </a>");
                buffer.append("</td><td style=\"color: black;\">" + paramNames[i] + "</td></tr>");
                buffer.append("</tbody></table></li>");
            }
        }

        return buffer.toString();
    }

    public String getParameterForOneView(String oneViewId, HttpSession session) throws Exception {
        StringBuffer buffer = new StringBuffer("");
        int j = 0;
        String advHtmlFileProps = (String) session.getAttribute("advHtmlFileProps");
        String oldadvHtmlFileProps = (String) session.getAttribute("oldAdvHtmlFileProps");
        String oneviewglobal = (String) session.getAttribute("oneviewglobal");
        String ctxPath = (String) session.getAttribute("ctxPath");
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        String fileName = reportTemplateDAO.getOneviewFileName(oneViewId);

        OneViewBD oneBd = new OneViewBD();
        StringBuilder imgtest = new StringBuilder();
        OnceViewContainer oneContainer = (OnceViewContainer) oneBd.readFileDetails(oldadvHtmlFileProps, fileName);

        ArrayList measIdList = new ArrayList();
        ArrayList measNameList = new ArrayList();
        ArrayList selectedParam = new ArrayList();
        ArrayList selectedParamVAl = new ArrayList();
        if (oneviewglobal != null && oneviewglobal.equalsIgnoreCase("true")) {
            measIdList = oneContainer.getparameterlist();
            measNameList = oneContainer.getviewbygblname();
            session.setAttribute("rowViewIdList", measIdList);
            session.setAttribute("rowNamesLst", measNameList);
            if (measIdList != null) {
                for (int i = 0; i < measIdList.size(); i++) {
                    buffer.append(" <li id='ViewBy" + measIdList.get(i) + "' style='width:auto;height:auto;color:white' >");
                    buffer.append("<table id='viewTab" + measIdList.get(i) + "'>");
                    buffer.append(" <tr>");
                    buffer.append(" <td >");
                    buffer.append("<img src=\"" + ctxPath + "/icons pinvoke/cross-small.png\" style=\"cursor:pointer\" onclick=\"javascript:deleteColumn('ViewBy" + measIdList.get(i) + "','RowViewBy','" + measNameList.get(i) + "')\" />");
                    buffer.append("</td>");
                    buffer.append("<td class=\"gFontFamily gFontSize12\" style=\"color:black\">" + measNameList.get(i) + "</td>");
                    buffer.append("</tr>");
                    buffer.append("</table>");
                    buffer.append("</li>");
                }
            }
        } else {
            measIdList = oneContainer.getParamIds();
            measNameList = oneContainer.getParamNames();

            if (measIdList != null) {
                for (int i = 0; i < measIdList.size(); i++) {
                    buffer.append("<li id='" + measNameList.get(i) + "^elmnt-" + measIdList.get(i) + "' class=\"navtitle-hover\" style=\"width: auto; height: auto; color: white;\">");
                    buffer.append("<table id='" + measNameList.get(i) + j + "'>");
                    buffer.append("<tbody>");
                    buffer.append("<tr><td><a class=\"ui-icon ui-icon-close\" href=\"javascript:deleteDim('" + measNameList.get(i) + "^elmnt-" + measIdList.get(i) + "')\"> a </a>");
                    buffer.append("</td><td class=\"gFontFamily gFontSize12\" style=\"color: black;\">" + measNameList.get(i) + "</td></tr>");
                    buffer.append("</tbody></table></li>");
                }
            }
        }

        return buffer.toString();
    }
//added by Nazneen for getting dims excluding already added dimensions in the report

    public String getUserDimDetailsForRep(String foldersIds, String userId, String reportParamIdsVal) {

//        ProgenLog.log(ProgenLog.FINE, this, "getUserDimDetails", "Enter foldersIds--" + foldersIds );
        logger.info("Enter foldersIds--" + foldersIds);
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            retObj = this.getUserDimensions(foldersIds, userId, favName);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrsForRep(subFolderId, dimId, reportParamIdsVal));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getUserDims", "Exit");
        logger.info("Exit");
        return outerBuffer.toString();

    }

    public String getUserDimsMbrsForRep(String subFolderId, String dimId, String reportParamIdsVal) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[3];
        obj[0] = subFolderId;
        obj[1] = dimId;
        obj[2] = reportParamIdsVal;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrsForRep");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            ////////.println("getUserDimsMbrs query is : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }
    //added by Mohit

    public String getUserDimsMbrsForRepForAO(String subFolderId, String dimId, String reportParamIdsVal, String aoid) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[4];
        obj[0] = subFolderId;
        obj[1] = dimId;
        obj[2] = reportParamIdsVal;
        obj[3] = aoid;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrsForRepForAO");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
//            System.out.println("getUserDimsMbrsForRepForAO query is : "+finalQuery);
            logger.info("getUserDimsMbrsForRepForAO query is : " + finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = retObj.getFieldValueString(i, colNames[1]);
                elementid = retObj.getFieldValueString(i, colNames[0]);
                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getUserDimsMbrsForOneview(String subFolderId, String dimId, String reportParamIdsVal, ArrayList viewbyisnames, ArrayList viewbyids) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[3];
        obj[0] = subFolderId;
        obj[1] = dimId;
        obj[2] = reportParamIdsVal;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String sql = getResourceBundle().getString("getUserDimsMbrsForRep");
        StringBuffer outerBuffer = new StringBuffer("");
        try {

            finalQuery = buildQuery(sql, obj);
            ////////.println("getUserDimsMbrs query is : "+finalQuery);
//            retObj = execSelectSQL(finalQuery);
//            colNames = retObj.getColumnNames();
            for (int i = 0; i < viewbyids.size(); i++) {
//                MbrId = retObj.getFieldValueString(i, colNames[2]);
                MbrName = (String) viewbyisnames.get(i);
                elementid = (String) viewbyids.get(i);
                outerBuffer.append("<li class='closed'>");
//                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return outerBuffer.toString();
    }
    //kiran

    public void clearWhatIf(String reportId) {
        try {
            String qry = null;
            qry = "delete from PRG_AR_WHATIFS where REPORT_ID=" + reportId;
            execModifySQL(qry);

        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
    //added by Nazneen to get Dimension Members for Dimension Dependent

    public TreeMap getDimensionMembersForDimDep(String measEleId) {

        String finalQuery = null;
        PbReturnObject retObj = null;
        String[] colNames = null;
        String ElementId = "";
        String ElementName = "";
        String connId = "";
        String folderID = "";
        //HashMap map=new HashMap();
        TreeMap map = new TreeMap();
        measEleId = measEleId.replace("A_", "");

        String getMeasureDetails = "select FOLDER_ID,CONNECTION_ID from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID=" + measEleId;
        try {
            retObj = execSelectSQL(getMeasureDetails);
            if (retObj.getRowCount() > 0) {
                folderID = retObj.getFieldValueString(0, 0);
                connId = retObj.getFieldValueString(0, 1);
            }
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        String query1 = getResourceBundle().getString("getUserDimsMbrsForDimDep");
        Object obj[] = new Object[2];
        obj[0] = connId;
        obj[1] = folderID;
        finalQuery = buildQuery(query1, obj);
        try {
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                map.put(ElementName, ElementId);
            }
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return map;

    }

    public ArrayList saveHybridReportMeasureProperties(int reportId, String tabmstrId, Container container, PbReturnObject queryObj, int i, int columnSequence, boolean overwrit, ArrayList queries) {
        String query;
        String columnDisplayName = null;
        String qry_col_id;
        String getColorGroupXML = null;
        String elementId = null;
        HashMap ColorCodeMap = null;
        HashMap ColumnProperties = null;
        if (getTableHashMap() != null) {
            if (getTableHashMap().get("ColorCodeMap") != null) {
                ColorCodeMap = (HashMap) getTableHashMap().get("ColorCodeMap");
            }
            if (container.getColumnProperties() != null) {
                ColumnProperties = container.getColumnProperties();
            } else {
                ColumnProperties = new HashMap();
            }
        }
        ArrayList<String> hideMesureList = container.getReportCollect().getHideMeasures();
        HashMap<String, ArrayList<String>> summerizedTableHashMap = container.getSummerizedTableHashMap();
        boolean summerizedMsrEnable = false;
        if (summerizedTableHashMap != null && summerizedTableHashMap.get("summerizedQryeIds") != null) {
            summerizedMsrEnable = true;
            List<String> summerizedqryEIDs = (List<String>) summerizedTableHashMap.get("summerizedQryeIds");
            for (String measure : summerizedqryEIDs) {
                columnDisplayName = queryObj.getFieldValueString(i, 1);
                qry_col_id = queryObj.getFieldValueString(i, 0);
                elementId = queryObj.getFieldValueString(i, 3);
                //String updateQuery = "";
                if (container.getMeasureName("A_" + elementId) != null) {
                    columnDisplayName = container.getMeasureName("A_" + elementId);
                }
                getColorGroupXML = buildColorCodeXML(ColorCodeMap, "A_" + elementId);
                ArrayList singleColPropList = null;
                if (measure.contains("A_")) {
                    singleColPropList = ColumnProperties.get(measure) == null ? null : (ArrayList) ColumnProperties.get(measure);
                } else {
                    singleColPropList = ColumnProperties.get("A_" + measure) == null ? null : (ArrayList) ColumnProperties.get("A_" + measure);
                }
                boolean indicatorEnabled = container.isIndicatorEnabled(measure);
                String indicatorStr = "N";
                if (indicatorEnabled) {
                    indicatorStr = "Y";
                }
                String scriptIndicator = container.getscriptIndicator("A_" + elementId);
                String scriptAlign = container.getTextAlign("A_" + elementId);
                String measureAlign = container.getMeasureAlign("A_" + elementId);
                String measureType = container.getmeasureType("A_" + elementId);
                String timeConversion = container.gettimeConversion("A_" + elementId);
                //start of code by Nazneen for sub total deviation
                String subTotalDeviation = container.getSubTotalDeviation("A_" + elementId);

                String numFormat = getNumberSymbol("A_" + elementId);
                int roundPrecision = container.getRoundPrecisionForMeasure("A_" + elementId);
                String datetimeoption = container.getDateandTimeOptions("A_" + elementId);
                String dataSubstrcol = container.getDateSubStringValues("A_" + elementId);
                String dateFormat = container.getDateFormatt("A_" + elementId);
                String repTabId = null;
                if (overwrit) {
                    try {
                        String qry1 = "select REP_TAB_ID from PRG_AR_REPORT_TABLE_MASTER where report_id=" + reportId;
                        PbReturnObject retObj = executeSelectSQL(qry1);
                        repTabId = retObj.getFieldValueString(0, 0);
                    } catch (SQLException ex) {
                        logger.error("Exception", ex);
                    }catch (Exception ex) {
            logger.error("Exception", ex);
                    }
                }

                String reportDrillAssignRepId = null;
                String multireportDrillAssignRepId = null;
                // 
                if (container.getMsrDrillReportSelection() != null && container.getMsrDrillReportSelection().equalsIgnoreCase("multi report")) {
                    multireportDrillAssignRepId = container.getReportDrillMap("A_" + elementId);
                } else {
                    reportDrillAssignRepId = container.getReportDrillMap("A_" + elementId);
                }

                boolean hideMsrenable = false;
                if (hideMesureList != null && !hideMesureList.isEmpty() && hideMesureList.contains(elementId)) {
                    hideMsrenable = true;
                }

                if (singleColPropList != null && singleColPropList.size() != 0) {
                    if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                        if (overwrit) {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                    + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                    + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";

                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                    + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                    + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                        }
                    } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                        if (overwrit) {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                    + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                    + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";

                        } else {
                            query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                    + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                    + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                    + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                    + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                        }
                    } else {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,"
                                + "SHOW_SUB_TOTAL, SHOW_GRD_TOTAL, SHOW_AVG, SHOW_MIN, SHOW_MAX, SHOW_CAT_MAX, SHOW_CAT_MIN, APPEND_SYMBOL,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "'," + repTabId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',"
                                + "'" + singleColPropList.get(1) + "','" + singleColPropList.get(0) + "','" + singleColPropList.get(2) + "','" + singleColPropList.get(4) + "','" + singleColPropList.get(3)
                                + "','" + singleColPropList.get(5) + "','" + singleColPropList.get(6) + "','" + singleColPropList.get(7) + "',TO_CLOB('" + getColorGroupXML + "') ,'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                    }

                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.SQL_SERVER)) {
                    if (overwrit) {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (" + reportId + ",ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                    } else {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (ident_current('PRG_AR_REPORT_MASTER'),ident_current('PRG_AR_REPORT_TABLE_MASTER'),'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                    }
                } else if (ProgenConnection.getInstance().getDatabaseType().equalsIgnoreCase(ProgenConnection.MYSQL)) {
                    if (overwrit) {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";

                    } else {
                        query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                                + " values (" + reportId + "," + tabmstrId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                    }
                } else {
                    query = "insert into PRG_AR_REPORT_TABLE_DETAILS (REP_TAB_DTL_ID,REPORT_ID,REP_TAB_ID,QRY_COL_ID,COL_NAME,DISP_SEQ,color_group,NUMBER_FORMAT,ROUNDING_PRECISION, SHOW_INDICATOR, SCRIPT_INDICATOR,SCRIPT_ALIGN,MEASURE_TYPE,MEASURE_ALIGN,TIME_CONVERSION,DATE_TIME_OPTION,DATA_SUBSTRING,DATE_FORMAT,REPORT_DRILL_ASSIGN_REPORT,MULTI_REPORT_DRILL_ASSIGN,HIDE_MSR,SUBTOTAL_DEVIATION,SUMMERIZED_MSR_ENABLE,AVGZEROCOUNTTYPE) "
                            + " values (PRG_AR_REPORT_TABLE_DETLS_SEQ.nextval,'" + reportId + "'," + repTabId + ",'" + qry_col_id + "','" + columnDisplayName + "','" + columnSequence + "',TO_CLOB('" + getColorGroupXML + "'),'" + numFormat + "','" + roundPrecision + "','" + indicatorStr + "','" + scriptIndicator + "','" + scriptAlign + "','" + measureType + "','" + measureAlign + "','" + timeConversion + "','" + datetimeoption + "','" + dataSubstrcol + "','" + dateFormat + "'," + reportDrillAssignRepId + ",'" + multireportDrillAssignRepId + "','" + hideMsrenable + "'" + ",'" + subTotalDeviation + "','" + summerizedMsrEnable + "','" + avgvaltype + "')";
                }
                i++;
                queries.add(query);
            }
        }
        return queries;
    }

    public String writeExcelRetunObjecttoFile(int reportId, Container container) {
        String folderPath = container.getReportCollect().getReportAdvHtmlFileProps() + "/ImportExcel";
        //
        File folderDir = new File(folderPath);
        if (!folderDir.exists()) {
            folderDir.mkdir();
        }
        String filePath = folderPath + "/" + container.getReportName() + ".txt";
        FileOutputStream fos = null;
        ObjectOutputStream oos1 = null;
        try {
            fos = new FileOutputStream(filePath);
            oos1 = new ObjectOutputStream(fos);
            oos1.writeObject((ImportExcelDetail) container.importExcelDetails);
            oos1.flush();
        } catch (FileNotFoundException ex) {
            logger.error("Exception", ex);
        } catch (IOException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        } finally {
            try {
                oos1.close();
                fos.close();
            } catch (IOException ex) {
                logger.error("Exception", ex);
            }
        }
        return filePath;
    }

    public String trendAnalysisAction(String reportId, Container container, String PbUserId, String[] rowviewBy, String[] rowviewId) {
        JsonGenerator JsonGenerator = new JsonGenerator();
        String JsonList = JsonGenerator.trendAnalysisAction(reportId, container, PbUserId, rowviewBy, rowviewId);
        return JsonList;
    }
    //modified by krishan pratap

    public int SaveDefaultTab(String reportId, String defaulttab, String showIcon) {
        String Showall = "";
        if (showIcon.contains("selecctall")) {
            Showall = "showall";
        } else {
            Showall = "Notshowall";
        }

        PbDb pbdb = new PbDb();
        String query = "UPDATE PRG_AR_REPORT_MASTER SET DEFAULT_TAB='&',SHOW_ICONS='&',SHOW_ALL='&' WHERE REPORT_ID=&";
        Object obj[] = new Object[4];
        obj[0] = defaulttab;
        obj[3] = reportId;
        obj[1] = showIcon;
        obj[2] = Showall;
        int i1 = 0;
        query = super.buildQuery(query, obj);
        try {
            i1 = pbdb.execUpdateSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return i1;
    }
    //  added by mohit for kpi and none

    public String GetDefaultTab(String reportId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String query = "SELECT DEFAULT_TAB,REPORT_LAYOUT FROM PRG_AR_REPORT_MASTER WHERE REPORT_ID=&";
        Object obj[] = new Object[1];
        obj[0] = reportId;
        String DEFAULT_TAB = "";
        query = super.buildQuery(query, obj);
        try {
            pbro = pbdb.execSelectSQL(query);
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (pbro != null && !pbro.getFieldValueString(0, 1).equalsIgnoreCase("")) {
            DEFAULT_TAB = pbro.getFieldValueString(0, 0) + "::" + pbro.getFieldValueString(0, 1);
        } else if (pbro != null) {
            DEFAULT_TAB = pbro.getFieldValueString(0, 0) + "::ViewBy";
        }
        return DEFAULT_TAB;
    }

    public HashMap<String, ArrayList> GetAllFilterValues(String[] Paramsids) {
        PbDb pbdb = new PbDb();
        HashMap<String, ArrayList> map = new HashMap();
        ArrayList<String> parameterlist = new ArrayList<String>();
//            ArrayList<String> a = new ArrayList<String>();
        try {
//            String[] split = Paramsids.split(",");
            for (int i = 0; i < Paramsids.length; i++) {
                ArrayList<String> a = new ArrayList<String>();
                PbReturnObject retObj1 = null;
                String query1 = "select buss_col_name,BUSS_TABLE_NAME from prg_user_all_info_details where ELEMENT_ID= " + Paramsids[i];
                retObj1 = pbdb.execSelectSQL(query1);
                Object[] ObjArray = new Object[3];
                ObjArray[0] = retObj1.getFieldUnknown(0, 0);
                ObjArray[1] = retObj1.getFieldUnknown(0, 1);
                ObjArray[2] = retObj1.getFieldUnknown(0, 0);
                String query = "select distinct  &   from  &  where & is not null ";

                Statement st = null;
                ResultSet rs = null;
                Connection conn = ProgenConnection.getInstance().getConnectionForElement(Paramsids[i]);
                st = conn.createStatement();
                String finalQuery = pbdb.buildQuery(query, ObjArray);
                rs = st.executeQuery(finalQuery);
                // a.add("All");
                while (rs.next()) {
                    a.add(rs.getString(1).replace("'", ""));
                }
                map.put(Paramsids[i], a);

//for (String s : a)
//{
//    listString += s + "\t";
//}
                rs.close();
                rs = null;
                st.close();
                st = null;
                conn.close();

                conn = null;

            }
        } catch (SQLException ex) { 
            logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return map;
    }

    public String GetAllfavReports(String userId) {
//         PbDb pbdb = new PbDb();
        HashMap<String, String> map = new HashMap();
//          ArrayList<String> parameterlist = new ArrayList<String>();
        PbReturnObject links = null;
        try {
            CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal  
            if (cacheLayer.getdata("GetAllfavReports" + userId) == null) {
                links = new ProgenWidgetsDAO().getFavReports(Integer.parseInt(userId));
                cacheLayer.setdata("GetAllfavReports" + userId, links);
            } else {
                links = (PbReturnObject) cacheLayer.getdata("GetAllfavReports" + userId);
            }
            if (links != null) {
                for (int i = 0; i < links.getRowCount(); i++) {
                    map.put(links.getFieldValue(i, 1).toString(), links.getFieldValue(i, 0).toString());
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return new Gson().toJson(map);
    }

    // Added By prabal
    public String GetAllfavReportsLandingPage(String userId) {
        logger.info("@@@@@@@@@@@@@@@@@@  Dao  GetAllfavReportsLandingPage  userId=" + userId);
//         PbDb pbdb = new PbDb();
        ArrayList arylist = new ArrayList();
        CacheLayer cacheLayer = CacheLayer.getCacheInstance();// ByPrabal
        PbReturnObject links = null;
        try {

//            if (cacheLayer.getdata("GetAllfavReportsLandingPage" + userId) == null) {
                links = new ProgenWidgetsDAO().getFavReports(Integer.parseInt(userId));
//            if (cacheLayer.getdata("GetAllfavReportsLandingPage" + userId) == null) {
//                cacheLayer.setdata("GetAllfavReportsLandingPage" + userId, links);
//            } else {
//                links = (PbReturnObject) cacheLayer.getdata("GetAllfavReportsLandingPage" + userId);
//            }
            if (links != null) {
                for (int i = 0; i < links.getRowCount(); i++) {
                    HashMap<String, String> map = new HashMap();
                    map.put("ID", links.getFieldValue(i, 1).toString());
                    map.put("NAME", links.getFieldValue(i, 0).toString());
                    map.put("REPORTDESC"  ,links.getFieldValue(i, 4).toString());
                    arylist.add(map);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return new Gson().toJson(arylist);
    }

    public String GetAllDB(String userId) {
//         PbDb pbdb = new PbDb();
        HashMap<String, String> map = new HashMap();
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbroKpi = studioDao.getUserKPIDashboards(userId);
        PbReturnObject pbroOld = studioDao.getUserDashboards(userId);
        try {
            if (pbroKpi != null) {
                for (int i = 0; i < pbroKpi.getRowCount(); i++) {
                    map.put(pbroKpi.getFieldValue(i, 0).toString(), pbroKpi.getFieldValue(i, 1).toString() + "::" + pbroKpi.getFieldValue(i, 5).toString());
                }

            }

            if (pbroOld != null) {
                for (int i = 0; i < pbroOld.getRowCount(); i++) {
                    map.put(pbroOld.getFieldValue(i, 0).toString(), pbroOld.getFieldValue(i, 1).toString() + "::" + pbroOld.getFieldValue(i, 5).toString());

                }

            }

        } catch (Exception ex) {

            logger.error("Exception: ", ex);

        }
        return new Gson().toJson(map);

    }

    public String GetAllOneView(String userId) {
//         PbDb pbdb = new PbDb();
        HashMap<String, String> map = new HashMap();
        PbReturnObject retOjb = this.getAllOneViewBys(userId);
        try {
            if (retOjb != null) {
                for (int i = 0; i < retOjb.getRowCount(); i++) {
                    map.put(retOjb.getFieldValue(i, 0).toString(), retOjb.getFieldValue(i, 4).toString());
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return new Gson().toJson(map);
    }
    // add by mayank

    public int saveResetGraph(String reportId, String graphGO, String garphRefresh) {
        PbDb pbdb = new PbDb();
        String query = "UPDATE PRG_AR_REPORT_MASTER SET ISRESETGO='&', ISREFRESHGO='&' WHERE REPORT_ID=&";
        Object obj[] = new Object[3];
        obj[0] = graphGO;
        obj[1] = garphRefresh;
        obj[2] = reportId;
        int i1 = 0;
        query = super.buildQuery(query, obj);
        try {
            i1 = pbdb.execUpdateSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return i1;
    }

    public String getResetGraph(String reportId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String query = "SELECT ISRESETGO,ISREFRESHGO FROM PRG_AR_REPORT_MASTER WHERE REPORT_ID=&";
        Object obj[] = new Object[1];
        obj[0] = reportId;
        String list = "";
        query = super.buildQuery(query, obj);
        try {
            pbro = pbdb.execSelectSQL(query);
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (pbro != null) {
            list = pbro.getFieldValueString(0, 0) + "::" + pbro.getFieldValueString(0, 1);
        }
        return list;
    }
    //added by krishan 

    public String getUserDimDetailsForRepCustom(String foldersIds, String userId, String reportParamIdsVal) {

//        ProgenLog.log(ProgenLog.FINE, this, "getUserDimDetails", "Enter foldersIds--" + foldersIds );
        logger.info("Enter foldersIds--" + foldersIds);
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String favName = "";
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            retObj = this.getUserDimensions(foldersIds, userId, favName);
            colNames = retObj.getColumnNames();

            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li onClick='measureSelect(" + dimId + ")' class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension1.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrsForRep(subFolderId, dimId, reportParamIdsVal));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (Exception exception) {
            logger.info("Exception: ", exception);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getUserDims", "Exit");
        logger.info("Exit");
        return outerBuffer.toString();

    }

    public String getUserDimDetailsForRepAO(String aoIds, String userId, String reportParamIdsVal) {

//        ProgenLog.log(ProgenLog.FINE, this, "getUserAODimsMbrsForRep", "Enter AoIds--" + aoIds );
        logger.info("Enter AoIds--" + aoIds);
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        obj[0] = aoIds;
        obj[1] = aoIds;
        String MbrId = "";
        String MbrName = "";
        String elementid = "";
        String dimId = "";
        String dimName = "";
        String subFolderId = "";
        String sql = getResourceBundle().getString("getUserAODimsMbrsForRep");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
//             System.out.println("getUserDimDetailsForRepAO::"+finalQuery);
            logger.info("getUserDimDetailsForRepAO::" + finalQuery);
            retObj = execSelectSQL(finalQuery);
//       colNames = retObj.getColumnNames();
//         retObj = this.getUserDimensionsForAO(aoIds);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                dimId = retObj.getFieldValueString(i, colNames[0]);
                dimName = retObj.getFieldValueString(i, colNames[1]);
                subFolderId = retObj.getFieldValueString(i, colNames[2]);
                outerBuffer.append("<li onClick='measureSelect(" + dimId + ")' class='closed' id='" + dimId + "'>");
                outerBuffer.append("<img src='icons pinvoke/Dimension1.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;'>" + dimName + "</span>");
                outerBuffer.append("<ul id='dimName-" + dimName + "'>");

                outerBuffer.append(getUserDimsMbrsForRepForAO(subFolderId, dimId, reportParamIdsVal, aoIds));

                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }

//            for (int i = 0; i < retObj.getRowCount(); i++) {
//         MbrId = retObj.getFieldValueString(i, colNames[2]);
//                MbrName = retObj.getFieldValueString(i, colNames[1]);
//                elementid = retObj.getFieldValueString(i, colNames[0]);
//                outerBuffer.append("<li class='closed' id='" + MbrId + "'>");
//                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
//                outerBuffer.append("<span id='elmnt-" + elementid + "' style='font-family:verdana;'>" + MbrName + "</span>");
//                outerBuffer.append("</li>");
//}
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
//        ProgenLog.log(ProgenLog.FINE, this, "getUserAODimsMbrsForRep", "Exit");
        logger.info("Exit");
        return outerBuffer.toString();

    }

    public String getMeasurescustom(String foldersIds, ArrayList Parameters, String contextPath, String createAO) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        if (Parameters != null && !Parameters.isEmpty()) {

            obj = new Object[2];
            obj[0] = foldersIds;

            try {
                obj[1] = getdimmap.getFact(Parameters);
            } catch (SQLException ex) {
                logger.error("Exception", ex);
            }catch (Exception ex) {
            logger.error("Exception", ex);
            }
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);

            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;' title='" + facttooltip + "'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");
                outerBuffer.append(getMeasureElementscustom(userFolderIds, subFolderTabid, contextPath, createAO));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }

    public String getMeasurescustomForA0(String aoid, ArrayList Parameters, String contextPath) {

        PbReturnObject retObj = null;
        PbReturnObject retObj2 = null;
        String finalQuery = null;
        String finalQuery2 = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = aoid;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        String getAOMasterMap = null;
        GetDimFactMapping getdimmap = new GetDimFactMapping();
        obj = new Object[2];
        obj[0] = aoid;
        obj[1] = aoid;
        sql = getResourceBundle().getString("getFactsNewForAO");
        getAOMasterMap = getResourceBundle().getString("getAOMasterMap");
        Object obj2[] = new Object[1];
        obj2[0] = aoid;
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            finalQuery2 = buildQuery(getAOMasterMap, obj2);
            retObj = execSelectSQL(finalQuery);
            retObj2 = execSelectSQL(finalQuery2);

            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
//                factName = retObj.getFieldValueString(i, colNames[1]);
                factName = retObj2.getFieldValueString(0, "AO_NAME");
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);
                if (i == 0) {
                    outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                    outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                    outerBuffer.append("<span style='font-family:verdana;' title='" + facttooltip + "'>" + factName + "</span>");
                    outerBuffer.append("<ul id='factName-" + factName + "'>");
                }
                outerBuffer.append(getMeasureElementscustomForAO(userFolderIds, subFolderTabid, contextPath, aoid));
                if (i == retObj.getRowCount() - 1) {
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }

    public String getMeasureElementscustom(String userFolderIds, String subFolderTabid, String contextPath, String createAO) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[1];
        //obj[0] = userFolderIds;
        obj[0] = subFolderTabid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
        String colId = "";
        String sql = "";
        //added by Mohit
        if (createAO != null && !createAO.equalsIgnoreCase("") && createAO.equalsIgnoreCase("true")) {
            sql = getResourceBundle().getString("getFactElementsWithOutTC");
        } else {
            sql = getResourceBundle().getString("getFactElements1");
        }
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //   
            ////////.println("finalQuery in getmeasureelements are : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                REFElementId = retObj.getFieldValueString(i, colNames[2]);
                Formula = retObj.getFieldValueString(i, colNames[7]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                    outerBuffer.append("<li class='closed'>");
                    if (!Formula.equalsIgnoreCase("")) {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<span  class='open' class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "' style='font-family:verdana;' )\">" + ElementName + "</span>"); //<a href=''</a>

                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<span class='open' id='" + ElementId1 + "'   title='" + Formula + "' style='font-family:verdana' )\">" + ElementName1 + "</span>");//<a href=''></a>
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }
    //added by mohit

    public String getMeasureElementscustomForAO(String userFolderIds, String subFolderTabid, String contextPath, String aoid) {
        PbReturnObject retObj = null;
        String finalQuery = null;
        String[] colNames = null;
        Object obj[] = new Object[2];
        //obj[0] = userFolderIds;
        obj[0] = subFolderTabid;
        obj[1] = aoid;
        String ElementId = "";
        String REFElementId = "";
        String ElementId1 = "";
        String REFElementId1 = "";
        String ElementName = "";
        String ElementName1 = "";
        String Formula = "";
        String colId = "";

        String sql = getResourceBundle().getString("getFactElementsForAO");
        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //   System.out.println("getMeasureElements"+finalQuery+""+userFolderIds+",,,"+subFolderTabid);
            ////////.println("finalQuery in getmeasureelements are : "+finalQuery);
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            ArrayList list = new ArrayList();
            String viewFormulaClass = "formulaViewMenu";
            for (int i = 0; i < retObj.getRowCount(); i++) {
                viewFormulaClass = "";
                ElementId = retObj.getFieldValueString(i, colNames[0]);
                ElementName = retObj.getFieldValueString(i, colNames[1]);
                REFElementId = retObj.getFieldValueString(i, colNames[2]);
                Formula = retObj.getFieldValueString(i, colNames[7]);
                if (ElementId.equalsIgnoreCase(REFElementId)) {
                    list.add(ElementId);
                    outerBuffer.append("<li class='closed'>");
                    if (!Formula.equalsIgnoreCase("")) {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                        viewFormulaClass = "formulaViewMenu";
                    } else {
                        outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                    }
                    outerBuffer.append("<span  class='open' class='" + viewFormulaClass + "' id='" + ElementId + "'  title='" + Formula + "' style='font-family:verdana;' )\">" + ElementName + "</span>"); //<a href=''</a>

                    outerBuffer.append("<ul >");
                    for (int j = 0; j < retObj.getRowCount(); j++) {
                        ElementId1 = retObj.getFieldValueString(j, colNames[0]);
                        REFElementId1 = retObj.getFieldValueString(j, colNames[2]);
                        ElementName1 = retObj.getFieldValueString(j, colNames[1]);
                        Formula = retObj.getFieldValueString(j, colNames[7]);
                        if (ElementId.equalsIgnoreCase(REFElementId1) && !(ElementId.equalsIgnoreCase(ElementId1))) {
                            outerBuffer.append("<li class='closed' id='" + ElementId1 + "'>");
                            if (!Formula.equalsIgnoreCase("")) {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/document-attribute-f.png'></img>");
                            } else {
                                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/report.png'></img>");
                            }
                            outerBuffer.append("<span class='open' id='" + ElementId1 + "'   title='" + Formula + "' style='font-family:verdana' )\">" + ElementName1 + "</span>");//<a href=''></a>
                            outerBuffer.append("</li>");
                        }
                    }
                    outerBuffer.append("</ul>");
                    outerBuffer.append("</li>");
                }
            }

        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return outerBuffer.toString();
    }

    public String getMeasuresForReportCustom(String foldersIds, ArrayList Parameters, String contextPath, ArrayList tabIds, String createAO) {

        PbReturnObject retObj = null;
        String finalQuery = null;
        String factName = "";
        String subFolderTabid = "";
        String facttooltip = "";
        String userFolderIds = foldersIds;
        String[] colNames = null;
        Object obj[] = null;
        String sql = null;
        String tableIds = "";
        GetDimFactMapping getdimmap = new GetDimFactMapping();

        if (Parameters != null && !Parameters.isEmpty()) {
//            String dimFactRel = "select fact_id from ( "
//                    + "SELECT info.DIM_ID,info.BUSS_TABLE_ID,rlt.buss_table_id2 fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id "
//                    + "and rlt.buss_table_id2=isfact.buss_table_id and isfact.is_fact='Y' "
//                    + "union "
//                    + "SELECT info.ELEMENT_ID,info.BUSS_TABLE_ID,rlt.buss_table_id fact_id "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS info,PRG_GRP_BUSS_TABLE_RLT_MASTER rlt,PRG_USER_SUB_FOLDER_TABLES isfact "
//                    + "where info.element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and info.buss_table_id =rlt.buss_table_id2 "
//                    + "and rlt.buss_table_id=isfact.buss_table_id and isfact.is_fact='Y')b1 "
//                    + "group by fact_id having count(distinct DIM_ID) >=(select count (distinct DIM_ID) "
//                    + "FROM PRG_USER_ALL_INFO_DETAILS where element_id in (" + Parameters.toString().replace("[", "").replace("]", "") + ") and buss_table_id = fact_id) ";

            obj = new Object[2];
            obj[0] = foldersIds;
//            obj[1] = dimFactRel;
//                    try {
//           obj[1] = getdimmap.getFact(Parameters);
//        } catch (SQLException ex) {
//            logger.error("Exception",ex);
//        }

//            obj[1] = dimFactRel;
            if (tabIds != null && !tabIds.isEmpty()) {
                for (int i = 0; i < tabIds.size(); i++) {
                    if (i == 0) {
                        tableIds = (String) tabIds.get(i);
                    } else {
                        tableIds = tableIds + "," + (String) tabIds.get(i);
                    }
                }
                obj[1] = tableIds;
            }
//           obj[1] = getdimmap.getFact(Parameters);
            sql = getResourceBundle().getString("getFactsNew");
        } else if (tabIds != null && !tabIds.isEmpty()) {
            obj = new Object[2];
            obj[0] = foldersIds;
            for (int i = 0; i < tabIds.size(); i++) {
                if (i == 0) {
                    tableIds = (String) tabIds.get(i);
                } else {
                    tableIds = tableIds + "," + (String) tabIds.get(i);
                }
            }
            obj[1] = tableIds;
            sql = getResourceBundle().getString("getFactsNew");
        } else {
            obj = new Object[1];
            obj[0] = foldersIds;
            sql = getResourceBundle().getString("getAllFactsNew");
        }

        StringBuffer outerBuffer = new StringBuffer("");
        try {
            finalQuery = buildQuery(sql, obj);
            //
            retObj = execSelectSQL(finalQuery);
            colNames = retObj.getColumnNames();
            for (int i = 0; i < retObj.getRowCount(); i++) {
                factName = retObj.getFieldValueString(i, colNames[1]);
                subFolderTabid = retObj.getFieldValueString(i, colNames[0]);
                facttooltip = retObj.getFieldValueString(i, colNames[2]);

                outerBuffer.append("<li class='closed' id='" + factName + i + "'>");
                outerBuffer.append("<img src='" + contextPath + "/icons pinvoke/table.png'></img>");
                outerBuffer.append("<span style='font-family:verdana;' title='" + facttooltip + "'>" + factName + "</span>");
                outerBuffer.append("<ul id='factName-" + factName + "'>");
                outerBuffer.append(getMeasureElementscustom(userFolderIds, subFolderTabid, contextPath, createAO));
                outerBuffer.append("</ul>");
                outerBuffer.append("</li>");
            }
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return outerBuffer.toString();
    }
// added by krishan Pratap

    public String comparisonDate(String reportId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String query = "SELECT COMPARISON_DATE FROM PRG_AR_REPORT_TIME WHERE REPORT_ID=&";
        Object obj[] = new Object[1];
        obj[0] = reportId;
        String COMPARISON_DATE = "";
        query = super.buildQuery(query, obj);
        try {
            pbro = pbdb.execSelectSQL(query);
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        if (pbro != null && pbro.rowCount > 0) {
            COMPARISON_DATE = pbro.getFieldValueString(0, 0);
        }
        return COMPARISON_DATE;
    }
    //added by krishan pratap

    public void saveComparisonDate(String reportId, String comparisonDate) {
        PbDb pbdb = new PbDb();
        String query = "UPDATE PRG_AR_REPORT_TIME SET COMPARISON_DATE='&' WHERE REPORT_ID=&";
        Object obj[] = new Object[2];
        obj[0] = comparisonDate;
        obj[1] = reportId;
        int i1 = 0;
        query = super.buildQuery(query, obj);
        try {
            i1 = pbdb.execUpdateSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }

    }

// end 
    // Added By prabal    
    public void saveCurrentTabAndReportIdDao(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, Exception {
        String current_reportId = request.getParameter("current_reportId");
        String defTab = request.getParameter("current_defaulttab");
        HashMap<String, String> temp = new HashMap<String, String>();
        temp.put("current_repId" + current_reportId, defTab);
        session.setAttribute("current_reptId" + current_reportId, "current_repId" + current_reportId);
        session.setAttribute("current_repId" + current_reportId, temp);
    }
    // Ended By prabal 

    //added by sruthi for multi calendar
    public void saveMultiCalendar(Container container, int reportId) throws Exception {
        HashMap multicalendar = new HashMap();
        PbReturnObject pbro = null;
        PbDb pbdb1 = new PbDb();
        HashMap<String, ArrayList<String>> multicalendardata = new HashMap<String, ArrayList<String>>();
        ArrayList<String> tableids = new ArrayList<String>();
        multicalendardata = container.getMultiCalendarHashMap();
        if (multicalendardata != null && !multicalendardata.isEmpty()) {
            //String strI = "" + reportId;
            //multicalendardata=  (HashMap<String,ArrayList<String>>) multicalendar.get(strI);
            for (String key : multicalendardata.keySet()) {
                tableids.add(key);
            }
            for (int j = 0; j < tableids.size(); j++) {

                ArrayList<String> listofdata = new ArrayList<String>();
                listofdata = multicalendardata.get(tableids.get(j));
                String subquery = "select BUSS_TABLE_ID from PRG_AR_ALTERNATE_DATA where REPORT_ID='" + reportId + "' and BUSS_TABLE_ID=" + tableids.get(j);
                pbro = pbdb1.execSelectSQL(subquery);
                if (pbro.getRowCount() != 0) {
                    Object[] updateDetails = new Object[11];
                    updateDetails[0] = listofdata.get(0);
                    updateDetails[1] = listofdata.get(1);
                    updateDetails[2] = listofdata.get(2);
                    updateDetails[3] = listofdata.get(3);
                    updateDetails[4] = listofdata.get(4);
                    updateDetails[5] = listofdata.get(5);
                    updateDetails[6] = listofdata.get(6);
                    updateDetails[7] = listofdata.get(7);
                    updateDetails[8] = listofdata.get(8);            //Added by Mohit
                    updateDetails[9] = reportId;
                    updateDetails[10] = tableids.get(j);
                    String updateMultiCalendar = getResourceBundle().getString("updateMultiCalendar");
                    String updatefinalQuery = buildQuery(updateMultiCalendar, updateDetails);
                    this.execUpdateSQL(updatefinalQuery);
                } else {
                    Object[] segmentDetails = new Object[11];
                    segmentDetails[0] = reportId;
                    segmentDetails[1] = tableids.get(j);
                    segmentDetails[2] = listofdata.get(0);
                    segmentDetails[3] = listofdata.get(1);
                    segmentDetails[4] = listofdata.get(2);
                    segmentDetails[5] = listofdata.get(3);
                    segmentDetails[6] = listofdata.get(4);
                    segmentDetails[7] = listofdata.get(5);
                    segmentDetails[8] = listofdata.get(6);
                    segmentDetails[9] = listofdata.get(7);
                    segmentDetails[10] = listofdata.get(8);
                    String insertDimensionValues = getResourceBundle().getString("insertMultiCalendar");
                    String finalQuery = buildQuery(insertDimensionValues, segmentDetails);
                    execModifySQL(finalQuery);
                }
            }
        }
    }
//ended by sruthi

//added by krishan
    public int saveShowHeader(String reportId, String defaulttab) {
        PbDb pbdb = new PbDb();
        String query = "UPDATE PRG_AR_REPORT_MASTER SET SHOW_HEADER='&' WHERE REPORT_ID=&";
        Object obj[] = new Object[2];
        obj[0] = defaulttab;
        obj[1] = reportId;
        int i1 = 0;
        query = super.buildQuery(query, obj);
        try {
            i1 = pbdb.execUpdateSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return i1;
    }
    
    //added by Ashutosh
    public int isDraggable(String reportId, String dragValue) {
        PbDb pbdb = new PbDb();
        String query = "UPDATE PRG_AR_REPORT_MASTER SET isDraggable='&' WHERE REPORT_ID=&";
        Object obj[] = new Object[2];
        obj[0] = dragValue;
        obj[1] = reportId;
        int i1 = 0;
        query = super.buildQuery(query, obj);
        try {
            i1 = pbdb.execUpdateSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        return i1;
    }

    //added by Dinanath

    public String[] getCurrentLocalSavePointPath(String userId, String reportId) {
        String finalqry;
        String result[] = new String[2];
        Object obj[] = null;
        String qry1 = "select collect_filename, container_filename  from prg_report_savepoint_details where is_enabled='&' and user_id='&' and report_id='&'";
        obj = new Object[3];
        obj[0] = "true";
        obj[1] = userId;
        obj[2] = reportId;
        finalqry = buildQuery(qry1, obj);
        PbReturnObject returnObject = new PbReturnObject();
        try {
            returnObject = execSelectSQL(finalqry);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    result[0] = returnObject.getFieldValueString(i, 0);
                    result[1] = returnObject.getFieldValueString(i, 1);
                }
            }
        } catch (SQLException ex) { 
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return result;
    }

    //Added by Mohit for ao master
    public void getAOMasterMap(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, Exception {
        //System.out.println("###################@@@@@@@@@@@@@@@   inside dao saveCurrentTabAndReportIdDAO     @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        HashMap AOId = new HashMap();
        HashMap AODetail = new HashMap();
        String aoId = request.getParameter("aoid");
        Object[] getAOMaster = new Object[1];
        getAOMaster[0] = aoId;
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReturnObject pbro = null;
        PbDb pbdb1 = new PbDb();
        String getAOMasterMap = getResourceBundle().getString("getAOMasterMap");
        getAOMasterMap = buildQuery(getAOMasterMap, getAOMaster);
        pbro = pbdb1.execSelectSQL(getAOMasterMap);
        if (pbro != null) {
            AODetail.put("AO_NAME", pbro.getFieldValueString(0, "AO_NAME"));
            AODetail.put("AO_DESC", pbro.getFieldValueString(0, "AO_DESC"));//added by Dinanath
            AODetail.put("AO_TABLE_NAME", pbro.getFieldValueString(0, "AO_TABLE_NAME"));
            AODetail.put("AO_MAIN_DATE", pbro.getFieldValueString(0, "AO_MAIN_DATE"));
            AODetail.put("AO_IS_MULTIDATE", pbro.getFieldValueString(0, "AO_IS_MULTIDATE"));
            AODetail.put("FOLDER_ID", pbro.getFieldValueString(0, "FOLDER_ID"));
            AODetail.put("IS_VIRTUAL_AO", pbro.getFieldValueString(0, "IS_VIRTUAL_AO"));
            AODetail.put("USER_ID", pbro.getFieldValueString(0, "USER_ID"));
            AODetail.put("A0_CREATED_DATE", pbro.getFieldValueString(0, "A0_CREATED_DATE"));
            AOId.put(aoId, AODetail);
            session.setAttribute("AOMasterMap", AOId);
        }

//           session.setAttribute("current_repId", current_reportId);
//           session.setAttribute(current_reportId,temp);
        //
    }
    //  added by mohit for kpi and none

    public String GetaoAsGoId(String reportId) {
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String query = "SELECT AO_ID_FOR_GRAPH FROM PRG_AR_REPORT_MASTER WHERE REPORT_ID=&";
        Object obj[] = new Object[1];
        obj[0] = reportId;
        String DEFAULT_TAB = "";
        query = super.buildQuery(query, obj);
        try {
            pbro = pbdb.execSelectSQL(query);
        } catch (Exception exp) {
            logger.error("Exception: ", exp);
        }
        if (pbro != null) {
            DEFAULT_TAB = pbro.getFieldValueString(0, 0);
        }
        return DEFAULT_TAB;
    }

    /**
     * *********************************************************************************
     * @Created By : Prabal Pratap Singh
     *
     * @since : 23/01/2015 @reason : for recently used reports for user specific
     * @param : HttpServletRequest request, HttpServletResponse
     * response,HttpSession session
     * @see : com.progen.reportdesigner.action.ReportTemplateAction
     * @return : JSON String
     * ***********************************************************************************
     */
    public String getRecentalyUsedUserReports(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, Exception {
        Gson json = new Gson();
        List<String> reportName = new ArrayList<String>();
        List<String> reportDesc = new ArrayList<String>();
        List<String> reportId = new ArrayList<String>();
        List<String> createdDate = new ArrayList<String>();
        List<String> updatedDate = new ArrayList<String>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        java.sql.Date lastUpdate = null;
        Map dimmap = new HashMap();
//     Timestamp st;
        CacheLayer cacheMap = CacheLayer.getCacheInstance();
        java.sql.Date d;
        String userId = request.getParameter("userId");
        boolean customReportFlag = false;
        customReportFlag = Boolean.parseBoolean(request.getParameter("customReports"));
        PbReturnObject returnObject = new PbReturnObject();
        PbDb pbdb = new PbDb();
        if (!customReportFlag) {
            if (cacheMap.getdata("customReportFlag" + userId) == null) {
                String query = "SELECT  R.REPORT_NAME,R.REPORT_ID, R.REPORT_DESC,  R.CREATED_DATE,  R.last_update_on,  count(*) TOTAL_COUNT FROM prg_user_hitcalc H, prg_ar_report_master R WHERE  R.report_id = H.report_id and h.user_id = (&) GROUP BY   R.REPORT_NAME,  R.REPORT_ID, R.REPORT_DESC, R.CREATED_DATE,  R.last_update_on order by 6 desc";
                Object obj[] = new Object[1];
                obj[0] = userId;
                query = pbdb.buildQuery(query, obj);
                returnObject = pbdb.execSelectSQL(query);
                cacheMap.setdata("customReportFlag" + userId, returnObject);
            } else {
                returnObject = (PbReturnObject) cacheMap.getdata("customReportFlag" + userId);
            }
        } else if (cacheMap.getdata("getRecentalyUsedUserReports" + userId) == null) {
            String query = "select  REPORT_NAME,REPORT_ID,REPORT_DESC,CREATED_DATE,last_update_on from PRG_AR_REPORT_MASTER where created_by=(select PU_LOGIN_ID from PRG_AR_USERS where PU_ID= (&) ) order by REPORT_ID desc";
            Object obj[] = new Object[1];
            obj[0] = userId;
            query = pbdb.buildQuery(query, obj);
            returnObject = pbdb.execSelectSQL(query);
            cacheMap.setdata("getRecentalyUsedUserReports" + userId, returnObject);
        } else {
            returnObject = (PbReturnObject) cacheMap.getdata("getRecentalyUsedUserReports" + userId);
        }
        try {

            int length = returnObject.rowCount;
            if (length >= 27) {
                length = 27;
            }
            if (returnObject != null) {
                for (int i = 0; i < length; i++) {
                    reportName.add(returnObject.getFieldValueString(i, 0));
                    reportId.add(returnObject.getFieldValueString(i, 1));
                    reportDesc.add(returnObject.getFieldValueString(i, 2));
                    //  new Date(returnObject.getFieldValueDateString(i, 3)).getTime();
                    //   
//                st = new Date(returnObject.getFieldValueDateString(i, 2)).getTime();
                    d = new java.sql.Date(new Date(returnObject.getFieldValueDateString(i, 3)).getTime());
                    createdDate.add(df.format(d));
                    String dateStr = returnObject.getFieldValueString(i, 4);
                    if (dateStr == null || dateStr.equalsIgnoreCase("")) {
                        updatedDate.add("");
                    } else {
//                        lastUpdate = new java.sql.Date(formatter.parse(dateStr).getTime());
                        updatedDate.add(df.format(formatter.parse(dateStr)));
                    }
                    dimmap.put("REPORT_NAME", reportName);
                    dimmap.put("REPORT_ID", reportId);
                    dimmap.put("REPORT_DESC", reportDesc);
                    dimmap.put("CREATED_DATE", createdDate);
                    dimmap.put("UPDATED_DATE", updatedDate);
                }//end of for loop
            }//end of if of return object
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return json.toJson(dimmap);
    }
    //Added By Ram for Initialize default filter

    public HashMap getDefaultFilterElementId(HttpServletRequest request, HttpServletResponse response, String reportid) throws IOException, Exception {
        GetDefaultParameterLov gpl = new GetDefaultParameterLov();
        PbReturnObject returnObject = new PbReturnObject();
        ArrayList elementIdList = new ArrayList();
        ArrayList totalElementIdList = new ArrayList();
        HashMap parMapVals = new HashMap();
        HashMap<String, ArrayList<String>> finalMapVals = new HashMap<String, ArrayList<String>>();
        String qq = "%40";
        String query = "";
        String tabtype = "table";
        String fromajaxtype = "true";
        String startValue = "1";
        String parArrVals = "";
        String reportID = reportid;
        String fromglobal = "true";
        String scrollFlag = "1";

        String EleQuery = "select distinct element_id from prg_ar_report_param_details where report_id='" + reportid + "'";
//        
        try {
            returnObject = execSelectSQL(EleQuery);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    totalElementIdList.add(returnObject.getFieldValueString(i, 0));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        for (int k = 0; k < totalElementIdList.size(); k++) {
            parMapVals.put(totalElementIdList.get(k), "");
            if (k == 0) {
                parArrVals = "All";
            } else {
                parArrVals = parArrVals + ";" + "All";
            }
        }

        String qry = "select distinct element_id from prg_ar_report_param_details where report_id='" + reportid + "' and IS_INITIALIZE_DEFAULT_FILTER='Y'";
//         
        try {
            returnObject = execSelectSQL(qry);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    elementIdList.add(returnObject.getFieldValueString(i, 0));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);

        }

        for (int i = 0; i < elementIdList.size(); i++) {

            ArrayList<String> viewByElementIds = new ArrayList();
            for (Object key : parMapVals.keySet()) {
                viewByElementIds.add(key.toString());
            }
//    String listString = "";
            StringBuilder listString = new StringBuilder();
            viewByElementIds.size();
            for (String s : viewByElementIds) {
                if (!s.equalsIgnoreCase("TIME") && !s.equalsIgnoreCase("none") && !s.equalsIgnoreCase("KPI")) {
//        listString += s + ",";
                    listString.append(s).append(",");
                }
            }
            String allParamIds = "";
//    if(listString != null && !listString.isEmpty()){
            if (listString.length() > 0) {
                allParamIds = listString.substring(0, listString.length() - 1);
            }

            query = elementIdList.get(i).toString();
            ArrayList getData = gpl.processRequest(request, qq, query, tabtype, fromajaxtype, startValue, allParamIds, parArrVals, reportID, fromglobal, scrollFlag);
            finalMapVals.put(elementIdList.get(i).toString(), getData);
//             
        }
        return finalMapVals;
    }

    //added by anitha
    public ArrayList getSessionQueriesForOracle() {
        ArrayList sessionQueryLst = new ArrayList();
        PbReturnObject returnObject = new PbReturnObject();
        String query = "select session_text from SESSION_QUERIES";
        try {
            returnObject = execSelectSQL(query);
            if (returnObject != null) {
                for (int i = 0; i < returnObject.getRowCount(); i++) {
                    sessionQueryLst.add(returnObject.getFieldValueString(i, 0));
                }
            }
        } catch (SQLException ex) {
             logger.error("Exception", ex);
        }catch (Exception ex) {
            logger.error("Exception", ex);
        }
        return sessionQueryLst;
    }
    //added by bhargavi
    public void DeleteUserAOs(String userId, String deleteAOIds) {
        String[] deleteAOs = deleteAOIds.split(",");
        ArrayList delList = new ArrayList();
        PbReturnObject retObj = null;
        //  Connection conn = null;
        String query = "";
        String reportIds = "";
        String elementid = "";

        try {
            // conn = getConnection();
            for (int i = 0; i < deleteAOs.length; i++) {
                PreparedStatement ps = null;
                File datafile = new File("/usr/local/cache/analyticalobject/M_AO_" + deleteAOs[i] + ".json");
                if (datafile.exists()) {
                    datafile.delete();
                }

                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                    query = "SELECT TOP 1 element_id FROM prg_ar_AO_details where AO_ID=&";
                } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    query = "select element_id FROM prg_ar_AO_details where AO_ID=& and ROWNUM = 1 ";
                } else {
                    query = "select element_id FROM prg_ar_AO_details where AO_ID=& limit 1 ";
                }
                Object[] obj = new Object[1];
                obj[0] = deleteAOs[i];
                query = buildQuery(query, obj);
                PbReturnObject returnobject = execSelectSQL(query);
                if (returnobject != null) {
                    elementid = returnobject.getFieldValueString(0, 0);
                    query = "select report_id from prg_ar_report_details where AO_ID in(" + deleteAOIds + ")";
                    retObj = executeSelectSQL(query);
                    if (retObj != null && retObj.getRowCount() != 0) {
                        for (int j = 0; j < retObj.rowCount; j++) {

                            reportIds = "," + retObj.getFieldValueString(j, 0);

                            DeleteUserReports(userId, reportIds.substring(1));
                        }
                    }
                    delList.add("delete from prg_ar_AO_master where AO_ID in(" + deleteAOIds + ")");
                    delList.add("delete from prg_ar_AO_details where AO_ID in(" + deleteAOIds + ")");

                    executeMultiple(delList);
                    execUpdateSQL("DROP table M_AO_" + deleteAOs[i], ProgenConnection.getInstance().getConnectionForElement(elementid));
                }

            }

        } catch (SQLException ex) { 
            logger.error("Exception: ", ex);
            retObj = null;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            retObj = null;
        }
        clearCache(deleteAOs);
    }
    //Added by Ram For checking color apply for all viewbys
    public String isColorApplyForAllViewBys(String reportId) {
        PbReturnObject pbro = null;
        String   applyColorProperty="";
        String   IsColorForAllViewBys="false";
        String   isColorAcrossCurrentData="false";
            String sql = "select IsColorForAllViewBys, isColorAcrossCurrentData from PRG_AR_REPORT_COLORS where report_id="+reportId+"";
            try {
                pbro = execSelectSQL(sql);
                 if (pbro.getRowCount()>0 ) {
                IsColorForAllViewBys=pbro.getFieldValue(0, 0).toString();
                isColorAcrossCurrentData=pbro.getFieldValue(0, 1).toString();
                 }
                applyColorProperty=IsColorForAllViewBys+"_"+isColorAcrossCurrentData;
            } catch (SQLException ex) {
                applyColorProperty=IsColorForAllViewBys+"_"+isColorAcrossCurrentData;
               logger.error("Exception", ex);
            }catch (Exception ex) {
                applyColorProperty=IsColorForAllViewBys+"_"+isColorAcrossCurrentData;
               logger.error("Exception", ex);
}
        logger.info("successful");
        return applyColorProperty;
    }
    
    public String loadAOMeasures(String userId) {
        PbReturnObject retObj = null;
        String query = "select distinct rd.report_id report_id, rm.report_name report_name, qd.col_disp_name col_disp_name,"
                + "qd.element_id element_id from PRG_AR_REPORT_DETAILS rd join prg_ar_report_master rm"
                + " on rd.report_id = rm.report_id and rd.ao_id !='' join PRG_AR_QUERY_DETAIL qd on rd.report_id = qd.REPORT_ID "
                + "join PRG_AR_USER_REPORTS ur on ur.report_id = rd.REPORT_ID where ur.user_id = "+userId;
        try {
            retObj=executeSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
}
        Map<String,List<String>> reportMeasureMapping=new HashMap();
        Map<String,String> reportNameIdMap=new HashMap();
        Map<String,String> measureNameIdMap=new HashMap();
        StringBuilder outerBuffer=new StringBuilder();
        if(retObj!=null && retObj.getRowCount()!=0){
            for(int i = 0;i<retObj.getRowCount();i++){
                String report_id = retObj.getFieldValueString(i, "report_id");
                String report_name= retObj.getFieldValueString(i, "report_name");
                Set<String> reports = reportMeasureMapping.keySet();
                if(!reports.contains(report_id)){
                    reportMeasureMapping.put(report_id, new ArrayList<String>());
                }
                List<String> measures = reportMeasureMapping.get(report_id);
                String measure_name=retObj.getFieldValueString(i, "col_disp_name");
                String measure_id=retObj.getFieldValueString(i, "element_id");
                measures.add(measure_id);
                reportMeasureMapping.put(report_id,measures);
                reportNameIdMap.put(report_id,report_name);
                measureNameIdMap.put(measure_id,measure_name);
            }
        }
        Set<String> reportIds = reportMeasureMapping.keySet();
        for(String repId:reportIds){
            outerBuffer.append("<li class='closed' id='" + repId + "'>");
            outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
            outerBuffer.append("<span style='font-family:verdana;'>" + reportNameIdMap.get(repId) + "</span>");
            outerBuffer.append("<ul id='rep_" + repId + "'>");
            
            List<String> measures=reportMeasureMapping.get(repId);
            for(int i=0;i<measures.size();i++){
                outerBuffer.append("<li class='closed' id='" + measures.get(i) + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='"+repId+":"+measureNameIdMap.get(measures.get(i))+":" + measures.get(i) + "' style='font-family:verdana;'>" +measureNameIdMap.get(measures.get(i)) + "</span>");
                outerBuffer.append("</li>");
            }
            outerBuffer.append("</ul>");
            outerBuffer.append("</li>");
        }
        return outerBuffer.toString();
    }

    public String loadPages(String userId) {
        PbReturnObject retObj = null;
        String query = "select rm.report_id,rm.report_name,rpm.page_id,rpm.page_label,rpm.page_sequence from prg_ar_report_master rm join prg_report_page_mapping_18may rpm on rm.report_id=rpm.report_id and rpm.user_id='"+userId+"'";
        try {
            retObj=executeSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        Map<String,List<String>> reportPageMapping=new HashMap();
        Map<String,String> reportNameIdMap=new HashMap();
        Map<String,String> pageNameIdMap=new HashMap();
        StringBuilder outerBuffer=new StringBuilder();
        if(retObj!=null && retObj.getRowCount()!=0){
            for(int i = 0;i<retObj.getRowCount();i++){
                String report_id = retObj.getFieldValueString(i, "report_id");
                String report_name= retObj.getFieldValueString(i, "report_name");
                Set<String> reports = reportPageMapping.keySet();
                if(!reports.contains(report_id)){
                    reportPageMapping.put(report_id, new ArrayList<String>());
                }
                List<String> pages = reportPageMapping.get(report_id);
                String page_name=retObj.getFieldValueString(i, "page_label");
                String page_id=retObj.getFieldValueString(i, "page_id");
                pages.add(page_id);
                reportPageMapping.put(report_id,pages);
                reportNameIdMap.put(report_id,report_name);
                pageNameIdMap.put(page_id,page_name);
            }
        }
        Set<String> reportIds = reportPageMapping.keySet();
        for(String repId:reportIds){
            outerBuffer.append("<li class='closed' id='" + repId + "'>");
            outerBuffer.append("<img src='icons pinvoke/Dimension.png'></img>");
            outerBuffer.append("<span style='font-family:verdana;'>" + reportNameIdMap.get(repId) + "</span>");
            outerBuffer.append("<ul id='rep_" + repId + "'>");
            
            List<String> pages=reportPageMapping.get(repId);
            for(int i=0;i<pages.size();i++){
                outerBuffer.append("<li class='closed' id='" + pages.get(i) + "'>");
                outerBuffer.append("<img src='icons pinvoke/hirarechy.png'></img>");
                outerBuffer.append("<span id='"+repId+":"+pageNameIdMap.get(pages.get(i))+":" + pages.get(i) + "' style='font-family:verdana;'>" +pageNameIdMap.get(pages.get(i)) + "</span>");
                outerBuffer.append("</li>");
            }
            outerBuffer.append("</ul>");
            outerBuffer.append("</li>");
        }
        return outerBuffer.toString();
    }
}
