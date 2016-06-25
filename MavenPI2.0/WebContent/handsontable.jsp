<%--
    Document   : handsontable
    Created on : 18 Mar, 2013, 2:20:02 PM
    Author     : surender.maddi@progenbusiness.com
--%>


<%@page import="com.progen.handsontable.bd.HandsontableDetails,java.io.FileInputStream,java.io.ObjectInputStream,java.util.logging.Level,java.util.Map,java.math.BigDecimal,prg.db.ContainerConstants,com.progen.report.PbReportCollection,java.io.FileOutputStream,java.io.ObjectOutputStream,com.progen.reportview.db.PbReportViewerDAO,com.progen.db.POIDataSet,java.io.File,com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="java.util.ArrayList,prg.db.PbReturnObject,com.progen.db.ProgenDataSet,com.progen.report.display.util.NumberFormatter,java.util.List,java.util.Arrays,com.progen.handsontable.bd.HandsonTableBD"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.google.gson.reflect.TypeToken,java.util.HashMap,prg.db.Container,com.google.gson.Gson"%>



<!DOCTYPE html>
<%
    String PbReportId = (String) request.getParameter("reportId");
    String fromHomePage = (String) request.getParameter("fromHomePage");
    String homeFile = (String) request.getParameter("fileName");
    String hotFileName = (String) request.getParameter("hotFileName");
//    String[] columnnames = request.getParameterValues("columnnames");
//    String[] graphcolumns = request.getParameterValues("graphcolumns");

    boolean testFrom = false;
    boolean fromHomeFile = false;
    boolean testForHOT = false;
    boolean hotFileTest = false;
    boolean fileExistornot = false;
    if(fromHomePage!=null && !fromHomePage.equalsIgnoreCase("null") && !fromHomePage.equalsIgnoreCase("")){
        testFrom = true;
    }
    if(homeFile!=null && !homeFile.equalsIgnoreCase("null") && !homeFile.equalsIgnoreCase("")){
        fromHomeFile = true;
    }
    if(hotFileName!=null && !hotFileName.equalsIgnoreCase("null") && !hotFileName.equalsIgnoreCase("")){
        hotFileTest = true;
    }
    String ctxPath = request.getContextPath();
    HashMap map = null;
    Container container = null;
    ProgenDataSet rtObj = null;
    ProgenDataSet pbrtObj = new PbReturnObject();
    String[] colHeasders = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    List data = new ArrayList();
    ArrayList tableHeasders = new ArrayList();
    List showDeaders = new ArrayList();
    List grandTotal = new ArrayList();
    int displaysize = 0;
    String colName = "";
    String colheadersNames = "";
    String tableDataValues = "";
    HandsontableDetails hotDetails = null;
    Map hotMap = null;
    hotMap = (HashMap) session.getAttribute("HOTFORMULA");
    HandsonTableBD hotBd = new HandsonTableBD();
    String hotFilePath=(String) request.getSession(false).getAttribute("reportAdvHtmlFileProps");
    String useriId = request.getSession().getAttribute("USERID").toString();
    if(!fromHomeFile && !testFrom && request.getParameter("headerDta")==null && !hotFileTest){
        pbrtObj = hotBd.getHandsonTableData(PbReportId, useriId);
     }
    if(!fromHomeFile && !hotFileTest){
    map = (HashMap) session.getAttribute("PROGENTABLES");
    container = (Container) map.get(PbReportId);
    }
    boolean testforGrand = false;
    if (hotMap != null) {
        hotDetails = (HandsontableDetails) hotMap.get(PbReportId);
    }
    if(hotFileTest){
        testForHOT = true;
        try {
            String fileName = "";
            PbReportViewerDAO dao = new PbReportViewerDAO();
            fileName = dao.getGraphHotFile(hotFileName);
//            FileInputStream fis2 = new FileInputStream(hotFilePath + "/" + "welcomePage16491_1366960834055.txt");
            FileInputStream fis2 = new FileInputStream(hotFilePath + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            hotDetails = (HandsontableDetails) ois2.readObject();
            ois2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tableHeasders.addAll(hotDetails.measureNames);
//        data.addAll(hotDetails.measureNames);
        data.addAll(hotDetails.reportTableData);
        displaysize = tableHeasders.size();
//        testforGrand = true;
    } else if(request.getParameter("headerDta")!=null && !testFrom){
        testForHOT = true;

        tableHeasders.addAll(Arrays.asList((request.getParameter("headerDta").split(","))));
        data.addAll(Arrays.asList((request.getParameter("finalGrapData").split(","))));
        displaysize = tableHeasders.size();
        hotDetails = new HandsontableDetails();
        PbReportId=(String)request.getParameter("reportId");
            hotDetails.setUserId(useriId);
            hotDetails.setReportId(PbReportId);
            hotDetails.measureNames=tableHeasders;
            hotDetails.reportTableData=data;
            hotDetails.setReportName((String)request.getParameter("repNameVal"));
           String homeFileName="";
           homeFileName = "welcomePage"+PbReportId+"_"+System.currentTimeMillis()+".txt";
           FileOutputStream fos1 = new FileOutputStream(hotFilePath+"/"+homeFileName);
           ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
           oos1.writeObject(hotDetails);
           oos1.flush();
           oos1.close();
           PbReportViewerDAO repDao = new PbReportViewerDAO();
           repDao.updateGraphType(PbReportId,useriId,homeFileName);
//        testforGrand = true;
    } else if(fromHomeFile){
        try {
            FileInputStream fis2 = new FileInputStream(hotFilePath + "/" + homeFile);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            hotDetails = (HandsontableDetails) ois2.readObject();
            ois2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tableHeasders.addAll(hotDetails.measureNames);
//        data.addAll(hotDetails.measureNames);
        data.addAll(hotDetails.reportTableData);
        displaysize = tableHeasders.size();
        testforGrand = true;

    } else if (hotDetails != null) {
        tableHeasders.addAll(hotDetails.measureNames);
        showDeaders.addAll(container.getDisplayLabels());
        data.addAll(hotDetails.reportTableData);
        displaysize = tableHeasders.size();
        testforGrand = true;
    } else if ((pbrtObj==null || pbrtObj.rowCount == 0)) {
        PbReportCollection collect = container.getReportCollect();
        ArrayList<String> sortCols = null;
        char[] sortTypes = null;//ArrayList sortTypes = null;
        char[] sortDataTypes = null;

        if (collect.reportRowViewbyValues.get(0) != null && collect.reportRowViewbyValues.get(0).equalsIgnoreCase("TIME")) {
        } else {
            sortCols = container.getSortColumns();
            if (sortCols != null && !sortCols.isEmpty()) {
                sortCols = container.getSortColumns();
                if (!sortCols.isEmpty()) {
                    sortTypes = container.getSortTypes();
                    sortDataTypes = container.getSortDataTypes();
                    ProgenDataSet retObj = container.getRetObj();
                    ArrayList rowSequence = new ArrayList();
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

        displaysize = container.getDisplayLabels().size();
        rtObj = container.getRetObj();
        tableHeasders = container.getDisplayLabels();
        showDeaders = container.getDisplayLabels();

        if (container.isReportCrosstab()) {
            String[] displayLabels = new String[tableHeasders.size()];
            for (int i = 0; i < tableHeasders.size(); i++) {
                displayLabels[i] = String.valueOf(tableHeasders.get(i)).replace(",", "  ");
            }
            tableHeasders.clear();
            tableHeasders.addAll(Arrays.asList(displayLabels));
        }
         int fromRow = container.getFromRow();
         int toRow = container.getRetObj().getViewSequence().size();
        data.addAll(tableHeasders);
        for (int i = fromRow; i < toRow; i++) {
            for (int j = 0; j < displaysize; j++) {
                colName = container.getDisplayColumns().get(j);
                if (container.getDataTypes().get(j).toString().equalsIgnoreCase("N")) {
                    data.add(NumberFormatter.getModifidNumber(rtObj.getFieldValueBigDecimal(Integer.parseInt(container.getRetObj().getViewSequence().get(i).toString()), colName)));
                } else {
                    data.add(rtObj.getFieldValueString(i, colName));
                }
            }
        }
        testforGrand = true;
//        ArrayList displayValues = new ArrayList();
//        ArrayList viewBys = new ArrayList();
//        displayValues = container.getDisplayColumns();
//        viewBys = container.getDisplayTypes();
//       for (int i = 0; i < 1; i++) {
//        for (int col = 0; col < displayValues.size(); col++) {
//            if(viewBys.get(col).toString().equalsIgnoreCase("T")){
//                 String formattedData = "";
////                            String element = container.getDisplayColumns().get(col);
////                            String nbrSymbol = container.getNumberSymbol(element);
////                             int precision = container.getRoundPrecisionForMeasure(element);
////                            if(nbrSymbol!=null && nbrSymbol.equalsIgnoreCase("nf") ){
//                             formattedData=NumberFormatter.getModifidNumber(rtObj.getColumnGrandTotalValue(displayValues.get(col).toString()));
////                         }
////                          else {
////                          formattedData = NumberFormatter.getModifiedNumberFormat(rtObj.getColumnGrandTotalValue(displayValues.get(col).toString()), nbrSymbol, precision);
////                       }
//                      grandTotal.add(formattedData);
//              }else{
//               grandTotal.add("Grand Total");
//            }
//           }
//         }
//        data.addAll(grandTotal);
        if(testFrom){
            hotDetails = new HandsontableDetails();
            hotDetails.setUserId(useriId);
            hotDetails.setReportId(PbReportId);
            hotDetails.measureNames=tableHeasders;
            hotDetails.reportTableData=data;
            hotDetails.setReportName(container.getReportDesc());
           String homeFileName="";
           String graphType="tableType";
           homeFileName = "welcomePage"+PbReportId+"_"+System.currentTimeMillis()+".txt";
           FileOutputStream fos1 = new FileOutputStream(hotFilePath+"/"+homeFileName);
           ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
           oos1.writeObject(hotDetails);
           oos1.flush();
           oos1.close();
           PbReportViewerDAO repDao = new PbReportViewerDAO();
           repDao.insertHomePageGraphs(PbReportId,homeFileName,PbReportId,container.getReportDesc(),graphType);
        }
    } else {
        String filePath = pbrtObj.getFieldValueString(0, "FILEPATH_FOLDER");
        String fileName = pbrtObj.getFieldValueString(0, "FILE_NAME");
        try {
            FileInputStream fis2 = new FileInputStream(filePath + "/" + fileName);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            hotDetails = (HandsontableDetails) ois2.readObject();
            ois2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showDeaders.addAll(container.getDisplayLabels());
        tableHeasders.addAll(hotDetails.measureNames);
//        data.addAll(hotDetails.measureNames);
        data.addAll(hotDetails.reportTableData);
        displaysize = tableHeasders.size();
        testforGrand = true;
//        if (colheadersNames != null && tableDataValues != null) {
//            tableHeasders.addAll(Arrays.asList(colheadersNames.split(",")));
//            data.addAll(Arrays.asList(tableDataValues.split(",")));
//            displaysize = tableHeasders.size();
////              tableHeasders=gson.fromJson(colheadersNames, new TypeToken<List>(){}.getType());
////              data=gson.fromJson(tableDataValues, new TypeToken<List>(){}.getType());
////              displaysize = tableHeasders.size();
//        }
    }
    String contextpath=request.getContextPath();
%>
<html>
    <head>
        <!--<script src="http://handsontable.com/lib/jquery.min.js"></script>
        <script src="http://handsontable.com/dist/jquery.handsontable.full.js"></script>
        <link rel="stylesheet" media="screen" href="http://handsontable.com/dist/jquery.handsontable.full.css">
        <link rel="stylesheet" media="screen" href="http://handsontable.com/demo/css/samples.css">
        <script src="http://handsontable.com/lib/jquery-ui/js/jquery-ui.custom.min.js"></script>
        <link rel="stylesheet" media="screen" href="http://handsontable.com/lib/jquery-ui/css/ui-bootstrap/jquery-ui.custom.css">-->

        <script type="text/javascript" src="<%=contextpath%>/javascript/handsontable/jquery.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/handsontable/jquery.handsontable.full.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/handsontable/jquery-ui.custom.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/handsontable/custonDialoginHandson.js"></script>-->

<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->


        <link type="text/css" rel="stylesheet" media="screen"  href="<%=contextpath%>/javascript/handsontable/jquery.handsontable.full.css">
        <link type="text/css" rel="stylesheet" media="screen"  href="<%=contextpath%>/javascript/handsontable/jquery-ui.custom.css" />
      <link type="text/css" rel="stylesheet" media="screen"  href="<%=contextpath%>/javascript/handsontable/samples.css">



<!--        <script src="http://handsontable.com/lib/jquery.min.js"></script>-->
<!--<script src="http://handsontable.com/dist/jquery.handsontable.full.js"></script>-->
<!--<link rel="stylesheet" media="screen" href="http://handsontable.com/dist/jquery.handsontable.full.css">-->
<!--<link rel="stylesheet" media="screen" href="http://handsontable.com/demo/css/samples.css">-->

        <style type="text/css">
            body {background: white; margin: 20px;}
            h2 {margin: 20px 0;}
        </style>
        <script type="text/javascript">
            var calheaders='';

            $(document).ready(function () {

                function createBigData(){
                    var arr = [];
                    var first = 0.0;
                    var second = 0.0;
                    var third = 0.0;
                    var forth = 0.0;
                    var fifth = 0.0;
                    var sixth = 0.0;
                    var seventh = 0.0;
                    var eighth = 0.0;
                    var ninth = 0.0;
                    var tenth = 0.0;
                    var leventh = 0.0;
                    var twelth = 0.0;
                    var thirteen = 0.0;
                    var forteen = 0.0;
                    var fifteen = 0.0;
                    var sixteen = 0.0;
                    var seventen = 0.0;
                    var eighteen = 0.0;
                    var ninteen = 0.0;
                    var twenty = 0.0;

                    var parsdata = '<%=data%>';
                    //                    var test = parsdata.replace("[", "").replace("]", "");
                    var test = parsdata.substring(1, parsdata.length-1);
                    var finalval = test.split(",");
                    
                    var i=0;
                    var finalLength = 0;
                    var testVal = '<%=testforGrand%>'
                    var fromHome = '<%=testFrom%>'

                      if(testVal=='true'){
                        finalLength = finalval.length-parseInt('<%=displaysize%>');
                        
                      }else{
                        finalLength = finalval.length
                      }
                      if(fromHome =='true'){
                         finalLength = '<%=displaysize%>'*9;
                      }
                      if('<%=testForHOT%>'=='true'){
                          finalLength = '<%=displaysize%>'*7;
                      }
                    while(i<finalLength){
                        if('<%=displaysize%>'==2){
                            arr.push([
                                finalval[i],
                                finalval[i+1]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                            }
                            i=i+2;
                        }else if('<%=displaysize%>'==3){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                            }
                            i=i+3;
                        }else if('<%=displaysize%>'==4){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3]

                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                            }
                            i=i+4;
//                            alert(i)
//                            alert(arr)
                        }else if('<%=displaysize%>'==5){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                            }
                            i=i+5;
                        }else if('<%=displaysize%>'==6){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                            }
                            i=i+6;
                        }else if('<%=displaysize%>'==7){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                            }
                            i=i+7;
                        }else if('<%=displaysize%>'==8){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                            }
                            i=i+8;
                        }else if('<%=displaysize%>'==9){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                            }
                            i=i+9;
                        }else if('<%=displaysize%>'==10){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                            }
                            i=i+10;
                        }else if('<%=displaysize%>'==11){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                            }
                            i=i+11;
                        }else if('<%=displaysize%>'==12){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                            }
                            i=i+12;
                        }else if('<%=displaysize%>'==13){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                            }
                            i=i+13;
                        }else if('<%=displaysize%>'==14){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                            }
                            i=i+14;
                        }else if('<%=displaysize%>'==15){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                            }
                            i=i+15;
                        }else if('<%=displaysize%>'==16){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                            }
                            i=i+16;
                        }else if('<%=displaysize%>'==17){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                               sixteen=sixteen+parseFloat(finalval[i+16]);
                            }
                            i=i+17;
                        }else if('<%=displaysize%>'==18){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                               sixteen=sixteen+parseFloat(finalval[i+16]);
                               seventen=seventen+parseFloat(finalval[i+17]);
                            }
                            i=i+18;
                        }else if('<%=displaysize%>'==19){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                               sixteen=sixteen+parseFloat(finalval[i+16]);
                               seventen=seventen+parseFloat(finalval[i+17]);
                               eighteen=eighteen+parseFloat(finalval[i+18]);
                            }
                            i=i+19;
                        }else if('<%=displaysize%>'==20){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19]
                            ])
                            if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                               sixteen=sixteen+parseFloat(finalval[i+16]);
                               seventen=seventen+parseFloat(finalval[i+17]);
                               eighteen=eighteen+parseFloat(finalval[i+18]);
                               ninteen=ninteen+parseFloat(finalval[i+19]);
                            }
                            i=i+20;
                        }else if('<%=displaysize%>'==21){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19],
                                finalval[i+20]
                            ])
                             if(i!=0){
                               first=first+parseFloat(finalval[i+1]);
                               second=second+parseFloat(finalval[i+2]);
                               third=third+parseFloat(finalval[i+3]);
                               forth=forth+parseFloat(finalval[i+4]);
                               fifth=fifth+parseFloat(finalval[i+5]);
                               sixth=sixth+parseFloat(finalval[i+6]);
                               seventh=seventh+parseFloat(finalval[i+7]);
                               eighth=eighth+parseFloat(finalval[i+8]);
                               ninth=ninth+parseFloat(finalval[i+9]);
                               tenth=tenth+parseFloat(finalval[i+10]);
                               leventh=leventh+parseFloat(finalval[i+11]);
                               twelth=twelth+parseFloat(finalval[i+12]);
                               thirteen=thirteen+parseFloat(finalval[i+13]);
                               forteen=forteen+parseFloat(finalval[i+14]);
                               fifteen=fifteen+parseFloat(finalval[i+15]);
                               sixteen=sixteen+parseFloat(finalval[i+16]);
                               seventen=seventen+parseFloat(finalval[i+17]);
                               eighteen=eighteen+parseFloat(finalval[i+18]);
                               twenty=twenty+parseFloat(finalval[i+20]);
                            }
                            i=i+21;
                        }else if('<%=displaysize%>'==22){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19],
                                finalval[i+20],
                                finalval[i+21]
                            ])
                            i=i+22;
                        }else if('<%=displaysize%>'==23){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19],
                                finalval[i+20],
                                finalval[i+21],
                                finalval[i+22]
                            ])
                            i=i+23;
                        }else if('<%=displaysize%>'==24){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19],
                                finalval[i+20],
                                finalval[i+21],
                                finalval[i+22],
                                finalval[i+23]
                            ])
                            i=i+24;
                        }else if('<%=displaysize%>'==25){
                            arr.push([
                                finalval[i],
                                finalval[i+1],
                                finalval[i+2],
                                finalval[i+3],
                                finalval[i+4],
                                finalval[i+5],
                                finalval[i+6],
                                finalval[i+7],
                                finalval[i+8],
                                finalval[i+9],
                                finalval[i+10],
                                finalval[i+11],
                                finalval[i+12],
                                finalval[i+13],
                                finalval[i+14],
                                finalval[i+15],
                                finalval[i+16],
                                finalval[i+17],
                                finalval[i+18],
                                finalval[i+19],
                                finalval[i+20],
                                finalval[i+21],
                                finalval[i+22],
                                finalval[i+23],
                                finalval[i+24]
                            ])
                            i=i+25;
                        }
                    }
                    if(testVal=='true'){
                    if('<%=displaysize%>'==2){
                      arr.push([
                          'Grand Total',
                          Math.round(first*100)/100
                      ])
                    }else if('<%=displaysize%>'==3){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100
                      ])
                    }else if('<%=displaysize%>'==4){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100
                      ])
                    }else if('<%=displaysize%>'==5){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100
                      ])
                    }else if('<%=displaysize%>'==6){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100
                      ])
                    }else if('<%=displaysize%>'==7){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100
                      ])
                    }else if('<%=displaysize%>'==8){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100
                      ])
                    }else if('<%=displaysize%>'==9){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100
                      ])
                    }else if('<%=displaysize%>'==10){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100
                      ])
                    }else if('<%=displaysize%>'==11){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100
                      ])
                     }else if('<%=displaysize%>'==12){
                      arr.push([
                          'Grand Total',
                            Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100
                      ])
                   }else if('<%=displaysize%>'==13){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100
                      ])
                     }else if('<%=displaysize%>'==14){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100
                      ])
                    }else if('<%=displaysize%>'==15){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100
                      ])
                   }else if('<%=displaysize%>'==16){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100
                      ])
                    }else if('<%=displaysize%>'==17){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100,
                           Math.round(sixteen*100)/100
                      ])
                   }else if('<%=displaysize%>'==18){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100,
                           Math.round(sixteen*100)/100,
                           Math.round(seventen*100)/100
                      ])
                     }else if('<%=displaysize%>'==19){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100,
                           Math.round(sixteen*100)/100,
                           Math.round(seventen*100)/100,
                           Math.round(eighteen*100)/100
                      ])
                    }else if('<%=displaysize%>'==20){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100,
                           Math.round(sixteen*100)/100,
                           Math.round(seventen*100)/100,
                           Math.round(eighteen*100)/100,
                           Math.round(ninteen*100)/100
                      ])
                   }else if('<%=displaysize%>'==21){
                      arr.push([
                          'Grand Total',
                           Math.round(first*100)/100,
                           Math.round(second*100)/100,
                           Math.round(third*100)/100,
                           Math.round(forth*100)/100,
                           Math.round(fifth*100)/100,
                           Math.round(sixth*100)/100,
                           Math.round(seventh*100)/100,
                           Math.round(eighth*100)/100,
                           Math.round(ninth*100)/100,
                           Math.round(tenth*100)/100,
                           Math.round(leventh*100)/100,
                           Math.round(twelth*100)/100,
                           Math.round(thirteen*100)/100,
                           Math.round(forteen*100)/100,
                           Math.round(fifteen*100)/100,
                           Math.round(sixteen*100)/100,
                           Math.round(seventen*100)/100,
                           Math.round(eighteen*100)/100,
                           Math.round(ninteen*100)/100,
                           Math.round(twenty*100)/100
                      ])
                    }
                    }
                  
                    return arr;
                }
                function widths(){
                    var innerWidth=parseInt(window.innerWidth)-58;
                    var size = '<%=displaysize%>';
                    var intialwidth = '';
                    var fromHome = '<%=testFrom%>';
                    if(fromHome =='true'){
                      intialwidth = '110'
                    }else if('<%=testForHOT%>'=='true'){
                      intialwidth ="75";
                    }else{
                        if(Math.round(innerWidth/size)>200){
                           intialwidth=200;
                        }else{
                       intialwidth =parseInt(Math.round(innerWidth/size));
                    }
                    }
                    var columns = [];
                    for(var i=0 ;i<size;i++){
                        columns.push([
                            intialwidth
                        ])
                    }
                    return columns
                }
                function colHeadersdata(){
                    calheaders = '<%=tableHeasders%>';
                    var finalva = calheaders.substring(1, calheaders.length-1)
                    var names = finalva.split(",");
                    return names;
                }
                function columnTypes(){
                    var caltypes = new Array();
                    if('<%=displaysize%>'==2){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                    }else if('<%=displaysize%>'==3){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                    }else if('<%=displaysize%>'==4){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                    }else if('<%=displaysize%>'==5){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                    }else if('<%=displaysize%>'==6){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                    }else if('<%=displaysize%>'==7){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                    }else if('<%=displaysize%>'==8){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                    }else if('<%=displaysize%>'==9){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                    }else if('<%=displaysize%>'==10){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                    }else if('<%=displaysize%>'==11){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                    }else if('<%=displaysize%>'==12){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                    }else if('<%=displaysize%>'==13){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                    }else if('<%=displaysize%>'==14){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                    }else if('<%=displaysize%>'==15){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                    }else if('<%=displaysize%>'==16){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        var valeu15 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                        caltypes.push(valeu15);
                    }else if('<%=displaysize%>'==17){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        var valeu15 = {type:'numeric',readOnly:true}
                        var valeu16 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                        caltypes.push(valeu15);
                        caltypes.push(valeu16);
                    }else if('<%=displaysize%>'==18){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        var valeu15 = {type:'numeric',readOnly:true}
                        var valeu16 = {type:'numeric',readOnly:true}
                        var valeu17 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                        caltypes.push(valeu15);
                        caltypes.push(valeu16);
                        caltypes.push(valeu17);
                    }else if('<%=displaysize%>'==19){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        var valeu15 = {type:'numeric',readOnly:true}
                        var valeu16 = {type:'numeric',readOnly:true}
                        var valeu17 = {type:'numeric',readOnly:true}
                        var valeu18 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                        caltypes.push(valeu15);
                        caltypes.push(valeu16);
                        caltypes.push(valeu17);
                        caltypes.push(valeu18);
                    }else if('<%=displaysize%>'==20){
                        var valeu = {type:'text',readOnly:true}
                        var valeu1 = {type:'numeric',readOnly:true}
                        var valeu2 = {type:'numeric',readOnly:true}
                        var valeu3 = {type:'numeric',readOnly:true}
                        var valeu4 = {type:'numeric',readOnly:true}
                        var valeu5 = {type:'numeric',readOnly:true}
                        var valeu6 = {type:'numeric',readOnly:true}
                        var valeu7 = {type:'numeric',readOnly:true}
                        var valeu8 = {type:'numeric',readOnly:true}
                        var valeu9 = {type:'numeric',readOnly:true}
                        var valeu10 = {type:'numeric',readOnly:true}
                        var valeu11 = {type:'numeric',readOnly:true}
                        var valeu12 = {type:'numeric',readOnly:true}
                        var valeu13 = {type:'numeric',readOnly:true}
                        var valeu14 = {type:'numeric',readOnly:true}
                        var valeu15 = {type:'numeric',readOnly:true}
                        var valeu16 = {type:'numeric',readOnly:true}
                        var valeu17 = {type:'numeric',readOnly:true}
                        var valeu18 = {type:'numeric',readOnly:true}
                        var valeu19 = {type:'numeric',readOnly:true}
                        caltypes.push(valeu);
                        caltypes.push(valeu1);
                        caltypes.push(valeu2);
                        caltypes.push(valeu3);
                        caltypes.push(valeu4);
                        caltypes.push(valeu5);
                        caltypes.push(valeu6);
                        caltypes.push(valeu7);
                        caltypes.push(valeu8);
                        caltypes.push(valeu9);
                        caltypes.push(valeu10);
                        caltypes.push(valeu11);
                        caltypes.push(valeu12);
                        caltypes.push(valeu13);
                        caltypes.push(valeu14);
                        caltypes.push(valeu15);
                        caltypes.push(valeu16);
                        caltypes.push(valeu17);
                        caltypes.push(valeu18);
                        caltypes.push(valeu19);
                    }
                    return caltypes;
                }
                var greenRenderer = function (instance, td, row, col, prop, value, cellProperties) {
                    Handsontable.TextCell.renderer.apply(this, arguments);
//                    $(td).css({
                        td.style.fontWeight = 'bold';
//                        td.style.color = 'blue';
//                    });
                };
                var fontsize = function (instance, td, row, col, prop, value, cellProperties) {
                    Handsontable.TextCell.renderer.apply(this, arguments);
//                    $(td).css({
                        td.style.fontSize='8pt';
//                        td.style.color = 'blue';
//                    });
                };
                 function contextmenu(){
                    if('<%=testFrom%>'!='true'){
                    return ['row_below'];

                 }
                 else{
                     return [];
                 }
               }
             function showFormula(index){
                 parent.showFormulaHandsOntable('<%=PbReportId%>',index);
             }

                $("#handsontablId").handsontable({
                    data: createBigData(),
                    rowHeaders: true,
//                    colWidths: widths(),
//                    colWidths:[200, 200, 200, 200, 200, 200, 200, 200, 200, 200],
//                    columnSorting: true,
                    manualColumnResize: true,
                    //                    manualColumnMove: true,
                    //                    currentRowClassName: 'currentRow',
                    //                    currentColClassName: 'currentCol',
                    //                    autoWrapRow: true,
                    //                    colHeaders: colHeadersdata(),
                    colHeaders: true,
//                    minSpareRows: 1,
                   fixedRowsTop: 1,
                   fixedColumnsLeft: 1,
                    stretchH: 'all',
                      
                    

//                    //                    multiSelect: true,
                    //                    columns: columnTypes(),
                    //
                    //                    minSpareRows: 1
                    //    minSpareCols: 1,
                    //                    contextMenu: ['row_below', 'col_right','remove_row','remove_col','undo','redo']

                    contextMenu:{

                        callback: function (key, options) {
                    if (key === 'about') {
        //          setTimeout(function () {
                    //timeout is used to make sure the menu collapsed before alert is shown
                   // alert($("#handsontablId").handsontable('getSelected'))
                    var splitVal=$("#handsontablId").handsontable('getSelected');
                    //alert(splitVal[1])

                    showFormula(splitVal[1]);
        //          }, 100);
                }
                if('<%=testFrom%>'!='true'){

                    return ['row_below'];

                        }
                else{
                     return [];
                }
              },
               items: {
                   "about": {name: 'View Formula'}

               }
                    },

                                        cells: function (row, col, prop) {
                                       if('<%=fromHomeFile%>'!='true'){
                                        if (row === 0) {
                                          return {type: {renderer: greenRenderer}};
                                              }
                                         }else{
                                            return {type: {renderer: fontsize}};
                                         }
                                       }

                    //

                    //    contextMenu: true
                });
                


                $("#handsontablId").handsontable('selectCell', 3, 3);
            });

            function saveHandsonTable(){
//                var rowList = $("#handsontablId").handsontable("getData");
                var rowList = $("#handsontablId").data('handsontable');

                var finalValu = rowList.getData().toString().split(",");
                calheaders = '<%=tableHeasders%>';

                var finalva = calheaders.substring(1, calheaders.length-1)
//                var disize = '<%=displaysize%>';
//                var colCount = $("#handsontablId").handsontable('countCols')
//                var finalv = finalva.split(",")
//                if(disize<colCount){
//                    var diff = parseInt(colCount)-parseInt(disize)
//                    for(var i=0;i<diff;i++){
//                        finalv.push(finalValu[disize]);
//                    }
//                }

                //                               alert(JSON.stringify(rowList))
                //                               $('#example').text(JSON.stringify(rowList));
                //                               console.log(rowList);
                //var handsontable =  $("#example1").data('handsontable');
                var tableval = "";
                for(var i=0;i<finalValu.length;i++){
                    tableval+="<tr><td><input type='text' name='handsonData' value='"+finalValu[i]+"'></td><tr>";
                }
                $("#handsontableId").html(tableval);
                $.post(
                'handsontableaction.do?handsonParam=getReportTableHeaders&reportId='+'<%=PbReportId%>'+"&calheaders="+encodeURIComponent(finalva), $("#handsonTableFormId").serialize(),
                function (res){
                    alert("Successfully Saved")
//                    var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+'<%=PbReportId%>';
//                        var dSrc = parent.document.getElementById("handsonTableFrame");
//                        dSrc.src = source;
                }
               );
            }
            function movetoReportTable(){
                parent.document.getElementById("progenTableDiv").style.display=''
                parent.document.getElementById("paramHeaderId").style.display=''
                parent.document.getElementById("handsonTableId").style.display='none'
            }
            function changeCellType(){
                calheaders = '<%=tableHeasders%>';
//                var headers = '<%=showDeaders%>';
                var finalva = calheaders.substring(1, calheaders.length-1)
                var finalalue = finalva.split(",")
                var displaysize = '<%=displaysize%>';

                var htmlval = "";
                htmlval+="<table><tr><td>Enter Name</td><td><input id='ColumnName' type='text' name='headerName' value='' ></td></tr></table><br>";
                htmlval+="<table>";
                for(var i=1;i<displaysize;i++){
                    htmlval+="<tr><td style='font-size:10pt;color:'>"+finalalue[i]+"</td><td><input id='checkeValues"+i+"' type='checkbox' name='' value='"+i+"'></td></tr>";
                }
                htmlval+="</table><br>";
                htmlval+="<table>";
                htmlval+="<tr><td >Operators:</td><td><select id='handsonTableOperatorId' style='width: 120px;' name='' onchange=\"selectMeasure()\">";
                htmlval+="<option value=''>--Select--</option>";
                htmlval+="<option value='+'>Sum(+)</option>";
                htmlval+="<option value='-'>Difference(-)</option>";
                htmlval+="<option value='*'>Multiplication(*)</option>";
                htmlval+="<option value='/'>Division(/)</option>";
                htmlval+="</select></td></tr>";
                htmlval+="</table><br>";
                htmlval+="<table>";
                htmlval+="<tr><td><input type='button' class='navtitle-hover' name='' value='Clear Formula' onclick=\"clearFormula()\" style='width: 100px;'></td></tr>";
                htmlval+="<tr><td><textarea id='formulaId' rows='3' style='width: 99%; height: 100%;' onkeyup=\"textarea()\" cols='60' readonly='' name=''></textarea></td></tr>";
                htmlval+="</table>";
                htmlval+="<table>";
                htmlval+="<tr><td >Number:</td><td><input type='text' id='numberValueId' name='' value='' onkeypress=\"return isNumberKey(event)\" size='15'></td></tr>";
                htmlval+="<tr><td style='display:none'><input type='text' id='hiddenNumberValue' name='' value='' ></td></tr>";
                htmlval+="</table>";
                htmlval+="<center><table><tr><td width='40px'><input class='navtitle-hover' type='button' name='' value='+' style='width: 20px;' onclick=\"numberValue('+')\"><td><td width='40px'><input class='navtitle-hover' type='button' name='' value='-' style='width: 20px;' onclick=\"numberValue('-')\"><td></tr>";
                htmlval+="<tr><td width='40px'><input class='navtitle-hover' type='button' name='' value='*' style='width: 20px;' onclick=\"numberValue('*')\"><td><td width='40px'><input class='navtitle-hover' type='button' name='' value='/' style='width: 20px;' onclick=\"numberValue('/')\"><td></tr>";
                htmlval+="<tr><td width='40px' style='display:none'><input id='operatorValueId' type='button' name='' value='' ><td></tr><table></center>";

                htmlval+="<br><br><center><table >";
                htmlval+="<tr><td><input type='button' name='' value='Done' onclick='handsontableFormula()'></td></tr>";
                htmlval+="</table></center>";

                parent.$("#colmunFormulaId").html(htmlval);
                parent.$("#colmunFormulaId").dialog('open')
                parent.rowList="";

                parent.rowList=$("#handsontablId").handsontable("getData");
//                parent.headerSize = headerSize;
                parent.disize = '<%=displaysize%>';
                parent.colCount = $("#handsontablId").handsontable('countCols')

                parent.finalva = calheaders.substring(1, calheaders.length-1)
                parent.pbReportId = '<%=PbReportId%>';
          }
           function resetHot(){
            $.ajax({
               url:'<%=request.getContextPath()%>/handsontableaction.do?handsonParam=removeHandsontableSession',
               success:function(){
                   var source = '<%=request.getContextPath()%>/handsontable.jsp?reportId='+'<%=PbReportId%>';
                   var dSrc = parent.document.getElementById("handsonTableFrame");
                   dSrc.src = source;
              }
          })
            }
            function applyFilter(){
               calheaders = '<%=tableHeasders%>';
//                var headers = '<%=showDeaders%>';
                var finalva = calheaders.substring(1, calheaders.length-1)
                var finalalue = finalva.split(",")
                var displaysize = '<%=displaysize%>';

                var htmlval = "";
                htmlval+="<table><tr><td>Please Select Measure to apply filters</td></tr></table><br>";
                htmlval+="<table>";
                for(var i=1;i<displaysize;i++){
                    htmlval+="<tr><td style='font-size:10pt;color:'>"+finalalue[i]+"</td><td><input id='checkFilterVal"+i+"' type='checkbox' name='' value='"+finalalue[i]+"'></td></tr>";
                }
                htmlval+="</table>";
                htmlval+="<br><br><center><table >";
                htmlval+="<tr><td><input type='button' name='' value='Next' onclick='handsontableFilters()'></td></tr>";
                htmlval+="</table></center>";

                parent.$("#columnFilterId").html(htmlval);
                parent.$("#columnFilterId").dialog('open')

                parent.rowList="";
                parent.rowList=$("#handsontablId").handsontable("getData");
//                parent.headerSize = headerSize;
                parent.disize = '<%=displaysize%>';
                parent.colCount = $("#handsontablId").handsontable('countCols')

                parent.finalva = calheaders.substring(1, calheaders.length-1)
                parent.pbReportId = '<%=PbReportId%>';
            }
        </script>
    </head>
    <body>
        <%if(!testFrom && !testForHOT && !hotFileTest){%>
        <table >
            <tr>
                <td width="100%" ></td>
                <%if (!container.isReportCrosstab() && showDeaders.size() > 2 && showDeaders.size() < 8) {%>
                <td><input type="image" title="Cell Formula" value="" onclick="changeCellType()" src="<%=ctxPath%>/icons pinvoke/user.png"></td>
                <%}%>
                <td><input type="image" style="width:30px;height:20px;"  title="ApplyFilter" value="" onclick="applyFilter()" src="<%=ctxPath%>/icons pinvoke/filter.png"></td>
                <td><input type="image" style="width:30px;height:20px;" title="Reset" value="" onclick="resetHot()" src="<%=ctxPath%>/icons pinvoke/refresh.png"></td>
                <td><input type="image" style="width:40px;height:20px;" title="SaveHandsonTable" value="" onclick="saveHandsonTable()" src="<%=ctxPath%>/icons pinvoke/save.png"></td>
                <td><input type="image" style="width:30px;height:20px;" title="MovetoTable" value="" onclick="movetoReportTable()" src="<%=ctxPath%>/icons pinvoke/table.png"></td>
            </tr>
        </table>
                 <%}%>
<!--            <center><img id="handsonimgId" src="<%=request.getContextPath()%>/images/ajax1.gif" align="left"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>-->
<!--        <img id="handsonimgId" src="<%=request.getContextPath()%>/images/ajax1.gif" align="left"  width="100px" height="100px"  style="top:100px; position:absolute" />-->
         <%if(testFrom ){%>
         <p align='center' style='color:#d0d0d0;font-size:35pt;font-family:verdana;padding-top:40px'> Template</p>
                 <%}else{%>
        <div class="handsontable" id="handsontablId" Style=" width: 100%; height: 100%; overflow: scroll"></div>
                 <%}%>
        <form id="handsonTableFormId" name="" action="">
         <table id="handsontableId" style="display: none" ></table>
        </form>
    </body>
</html>
