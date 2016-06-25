package com.progen.datasnapshots;

import com.progen.report.SearchFilter;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.util.sort.DataSetFilter;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import prg.db.Container;
import prg.db.PbReturnObject;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Administrator
 */
public class XMLReturnObject {

    private PbReturnObject retObj = null;
    private Container container = null;
    private HashMap<String, String> drillMap = null;
    private LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
    private HashMap<String, ArrayList<String>> dimensionList = new HashMap<String, ArrayList<String>>();
    public static Logger logger = Logger.getLogger(XMLReturnObject.class);

    public XMLReturnObject() {
        retObj = new PbReturnObject();
        container = new Container();
        drillMap = new HashMap<String, String>();
    }

//    public PbReturnObject getNewReturnObject(String viewby){
//
//       PbReturnObject newRetObj=new PbReturnObject();
//       char[] sortTypes = new char[] {'D'};
//       char[] sortDataTypes = new char[] {'C'};
//       ArrayList<String> columnNames=new ArrayList<String>();
//       ArrayList<String> displayColumns=new ArrayList<String>();
//       ArrayList<Integer> sortList=new ArrayList<Integer>();
//       displayColumns=container.getDisplayColumns();
//       String[] newColNames = null;
//       for(int i=0;i<displayColumns.size();i++)
//       {
//           if(viewby.equalsIgnoreCase(displayColumns.get(i).trim()))
//           {
//               columnNames.add(viewby);
//           }
//       }
//        sortList=retObj.sortDataSet(columnNames, sortTypes, sortDataTypes);
//        ArrayList measList = container.getTableMeasure();
//        for(int k=0;k<measList.size();k++)
//        {
//            columnNames.add((String) measList.get(k));
//        }
//      newColNames=new String[columnNames.size()];
//      for(int i=0;i<columnNames.size();i++)
//      {
//
//          newColNames[i]=columnNames.get(i);
//      }
//
//
//        newRetObj.setColumnNames(newColNames);
//         BigDecimal[] currentValues=new BigDecimal[container.getTableMeasure().size()];
//
//         for(int m=0;m<currentValues.length;m++)
//         {
//             currentValues[m]=BigDecimal.ZERO;
//         }
//
//         String currDimValue = null;
//         for(int n=0;n<sortList.size();n++)
//         {
//             int actualRow = sortList.get(n);
//             String temp=retObj.getFieldValueString(actualRow, viewby);
//
//             if (currDimValue == null)
//                 currDimValue = temp;
//
//             if (temp.equalsIgnoreCase(currDimValue)){
//                 for (int i=0;i<measList.size();i++){
//                     BigDecimal bd = retObj.getFieldValueBigDecimal(actualRow, (String)measList.get(i));
//                     if (bd != null)
//                         currentValues[i] = currentValues[i].add(bd);
//
//                 }
//
//             }
//            else{
//                 //Add to the new return object
//                 newRetObj.setFieldValue(viewby, currDimValue);
//                for (int i=0;i<measList.size();i++){
//                     newRetObj.setFieldValue((String)measList.get(i), currentValues[i]);
//
//                     BigDecimal bd = retObj.getFieldValueBigDecimal(actualRow, (String)measList.get(i));
//                     currentValues[i] = bd;
//                 }
//
//                 newRetObj.addRow();
//
//                 //update the current dim value and clear the current value array
//                currDimValue = temp;
//            }
//
//         }
//         newRetObj.resetViewSequence();
//         getHtml(newRetObj);
//        return newRetObj;
//    }
    public String getNewReturnObject(String[] viewbys, AdvancedHtmlData advancedHtmlData) {


        PbReturnObject newRetObj = new PbReturnObject();
        char[] sortTypes = new char[viewbys.length];
        char[] sortDataTypes = new char[viewbys.length];
        for (int p = 0; p < viewbys.length; p++) {
            sortTypes[p] = 'D';
            sortDataTypes[p] = 'C';
        }
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList dispLabels = new ArrayList();
        ArrayList<String> displayColumns = new ArrayList<String>();
        ArrayList<Integer> sortList = new ArrayList<Integer>();
        displayColumns = advancedHtmlData.getColumnIds();
        String[] newColNames = null;
        for (int i = 0; i < displayColumns.size(); i++) {
            for (int j = 0; j < viewbys.length; j++) {
                if (viewbys[j].equalsIgnoreCase(displayColumns.get(i).trim())) {
                    columnNames.add(viewbys[j]);
                    dispLabels.add(advancedHtmlData.getColumnNames().get(i));
                }
            }

        }
        sortList = retObj.sortDataSet(columnNames, sortTypes, sortDataTypes);
        ArrayList measList = advancedHtmlData.getMeasureIds();
        for (int k = 0; k < measList.size(); k++) {
            columnNames.add((String) measList.get(k));
            dispLabels.add(advancedHtmlData.getMeasureNames().get(k));
        }
        newColNames = new String[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {

            newColNames[i] = columnNames.get(i);
            //newColNames[i] = (String) dispLabels.get(i);
        }


        newRetObj.setColumnNames(newColNames);
        BigDecimal[] currentValues = new BigDecimal[advancedHtmlData.getMeasureIds().size()];

        for (int m = 0; m < currentValues.length; m++) {
            currentValues[m] = BigDecimal.ZERO;
        }

        String[] currDimValue = null;
        for (int n = 0; n < sortList.size(); n++) {
            int actualRow = sortList.get(n);
            String[] temp = new String[viewbys.length];
            for (int k = 0; k < viewbys.length; k++) {
                temp[k] = retObj.getFieldValueString(actualRow, viewbys[k]);
            }


            if (currDimValue == null) {
                currDimValue = new String[temp.length];
                for (int l = 0; l < temp.length; l++) {
                    currDimValue[l] = temp[l];
                }
            }
            boolean flag = compareStringArray(temp, currDimValue);
            if (flag) {
                for (int i = 0; i < measList.size(); i++) {
                    BigDecimal bd = retObj.getFieldValueBigDecimal(actualRow, (String) measList.get(i));
                    if (bd != null) {
                        currentValues[i] = currentValues[i].add(bd);
                    }

                }

            } else {
                //Add to the new return object
                for (int k = 0; k < viewbys.length; k++) {
                    newRetObj.setFieldValue(viewbys[k], currDimValue[k]);
                }

                for (int i = 0; i < measList.size(); i++) {
                    newRetObj.setFieldValue((String) measList.get(i), currentValues[i]);

                    BigDecimal bd = retObj.getFieldValueBigDecimal(actualRow, (String) measList.get(i));
                    currentValues[i] = bd;
                }

                newRetObj.addRow();

                //update the current dim value and clear the current value array
                for (int l = 0; l < temp.length; l++) {
                    currDimValue[l] = temp[l];
                }
            }
            if (n == sortList.size() - 1) {
                for (int k = 0; k < viewbys.length; k++) {
                    newRetObj.setFieldValue(viewbys[k], currDimValue[k]);
                }

                for (int i = 0; i < measList.size(); i++) {
                    newRetObj.setFieldValue((String) measList.get(i), currentValues[i]);

                    BigDecimal bd = retObj.getFieldValueBigDecimal(actualRow, (String) measList.get(i));
                    currentValues[i] = bd;
                }

                newRetObj.addRow();
            }

        }
        newRetObj.resetViewSequence();
        int recordCount = newRetObj.getRowCount();
        String data = getHtml(newRetObj, dispLabels, viewbys);
        data = data + "||" + Integer.toString(recordCount);
        return data;
    }

    public String getHtml(PbReturnObject retObj, ArrayList dispLabels, String[] viewbys) {

        StringBuilder tableBuilder = new StringBuilder();
//        String[] colNameArray=new String[dispLabels.size()];
//        for(int i=0;i<dispLabels.size();i++)
//        {
//            colNameArray[i]=(String) dispLabels.get(i);
//        }
//         retObj.setColumnNames(colNameArray);
        boolean flag = false;
//        String drillValue=drillMap.get(viewbys[0].replaceAll("A_", ""));
//        if(drillValue.equalsIgnoreCase(viewbys[0].replaceAll("A_", "")))
//         flag=false;

        try {


            if (retObj != null && retObj.getRowCount() > 0) {
                //tableBuilder = tableBuilder.append("<table border='1' style='border-collapse:separate;border-spacing:0px;-moz-border-radius: 6px 6px 6px 6px;' CELLPADDING=\"0\" CELLSPACING=\"1\"> <tr>");
                tableBuilder.append("<thead><tr>");
                for (int k = 0; k < retObj.getColumnNames().length; k++) {
                    tableBuilder = tableBuilder.append("<th align=\"center\">").append(dispLabels.get(k)).append("</th>");
                }
                tableBuilder.append("</tr></thead><tbody>");
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    tableBuilder.append("<tr>");
                    for (int j = 0; j < retObj.getColumnCount(); j++) {
                        if (j < viewbys.length) {
                            String drillValue = drillMap.get(viewbys[j].replaceAll("A_", ""));
                            if (!drillValue.equalsIgnoreCase(viewbys[j].replaceAll("A_", "")) && this.container.getReportParameterIds().contains("A_" + drillValue)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                            if (flag == true) {
                                tableBuilder.append("<td><a href=\"javascript:void(0)\" onclick=\"drilldown('").append(retObj.getFieldValueString(i, j)).append("','").append(retObj.getColumnNames()[j]).append("')\" >").append(retObj.getFieldValueString(i, j)).append("</a></td>");
                            } else {
                                tableBuilder.append("<td>").append(retObj.getFieldValueString(i, j)).append("</td>");
                            }

                        } else if (j >= viewbys.length) {
                            String measData = NumberFormatter.getModifiedNumber(retObj.getFieldValueBigDecimal(i, j), "", -1);
                            tableBuilder.append("<td align=\"right\">").append(measData).append("</td>");
                        } else {
                            tableBuilder.append("<td>").append(retObj.getFieldValueString(i, j)).append("</td>");
                        }

                    }
                    tableBuilder.append("</tr>");
                }
                tableBuilder.append("</tbody>");
            }
        } catch (Exception ex) {
//            
        }
        // 

        return tableBuilder.toString();
    }

    public PbReturnObject createObjects(Document paramDocument) {


        Element root = null;
        String[] columnNames = null;

        try {
            root = paramDocument.getRootElement();
            List columnNamesList = root.getChildren("COLUMN_DETAILS");

            Element colNames = (Element) columnNamesList.get(0);
            List columnList = colNames.getChildren("COLUMN");

            //List colNameList=colNames.getChildren("COLUMN_NAME");
            columnNames = new String[columnList.size()];
            for (int k = 0; k < columnList.size(); k++) {

                Element column = (Element) columnList.get(k);
                List columnNameList = column.getChildren("COLUMN_ID");
                Element columnName = (Element) columnNameList.get(0);
                columnNames[k] = columnName.getText();
            }


            retObj.setColumnNames(columnNames);
            // data.setColumnTypesInt(new int[]{Types.VARCHAR, Types.NUMERIC});
            List bodyList = root.getChildren("BODY");
//                  for(int l=0;l<bodyList.size();l++)
//                  {
            Element body = (Element) bodyList.get(0);
            List rowList = body.getChildren("ROW");
            for (int l = 0; l < rowList.size(); l++) {
                Element row = (Element) rowList.get(l);
                List colDataList = row.getChildren("COLUMN_DATA");
                for (int m = 0; m < colDataList.size(); m++) {
                    Element colData = (Element) colDataList.get(m);
                    retObj.setFieldValue(retObj.getColumnNames()[m], colData.getText());
                }
                retObj.addRow();
            }
//                  }
            retObj.resetViewSequence();
            // getHtml(retObj);


        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return retObj;
    }

    public Container createContainer(Document paramDocument) {
        Element root = null;
        String[] columnNames = null;
        ArrayList displayLabels = new ArrayList();
        ArrayList<String> displayColumns = new ArrayList<String>();
        ArrayList tableMeasureNames = new ArrayList();
        ArrayList tableMeasures = new ArrayList();
        ArrayList ReportParameterNames = new ArrayList();
        ArrayList reportParameterIds = new ArrayList();

        try {
            root = paramDocument.getRootElement();
            List dimensionList = root.getChildren("DIMENSION");
            for (int i = 0; i < dimensionList.size(); i++) {
                Element dimParam = (Element) dimensionList.get(i);
                List dimIdList = dimParam.getChildren("DIMENSION_ID");
                List dimNameList = dimParam.getChildren("DIMENSION_NAME");
                Element dimId = (Element) dimIdList.get(0);
                Element dimName = (Element) dimNameList.get(0);
                displayColumns.add(dimId.getText());
                displayLabels.add(dimName.getText());
                ReportParameterNames.add(dimName.getText());
                reportParameterIds.add(dimId.getText());

            }
            List measureList = root.getChildren("MEASURE");
            for (int j = 0; j < measureList.size(); j++) {
                Element measure = (Element) measureList.get(j);
                List measIdList = measure.getChildren("MEASURE_ID");
                List measNameList = measure.getChildren("MEASURE_NAME");
                Element measId = (Element) measIdList.get(0);
                Element measName = (Element) measNameList.get(0);
                displayColumns.add(measId.getText());
                displayLabels.add(measName.getText());
                tableMeasureNames.add(measName.getText());
                tableMeasures.add(measId.getText());
            }
            container.setDisplayColumns(displayColumns);
            container.setDisplayLabels(displayLabels);
            container.setTableMeasure(tableMeasures);
            container.setTableMeasureNames(tableMeasureNames);
            container.setReportParameterNames(ReportParameterNames);
            container.setReportParameterIds(reportParameterIds);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return container;
    }

    public static void main(String[] args) throws Exception {
        XMLReturnObject test = new XMLReturnObject();
        HashMap<String, String> paramMap = new HashMap<String, String>();
        //File file = new File("D:\\Documents and Settings\\Administrator\\Desktop\\xmltestreport.xml");


        //retObj.setViewSequence(null);
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

    public String clearFilters(String[] viewBys, AdvancedHtmlData advancedHtmlData) {
        this.getFilters().clear();
        retObj.resetViewSequence();
        String data = this.getNewReturnObject(viewBys, advancedHtmlData);
        return data;
    }

    public void addFilter(String dimId, String dimValue) {
        this.getFilters().put(dimId, dimValue);
    }

    public String getNewReturnObject(String[] viewbys, HashMap<String, String> paramMap, AdvancedHtmlData advancedHtmlData) {
        SearchFilter filter = new SearchFilter();
        ArrayList<Integer> sortList = new ArrayList<Integer>();
        ArrayList<Integer> intvals = new ArrayList<Integer>();
        ArrayList<String> colTypes = new ArrayList<String>();
        ArrayList<String> columnList = new ArrayList<String>();
        //retObj=(PbReturnObject) advancedHtmlData.getReturnObject();

        int[] columnTypes = null;
        for (int i = 0; i < container.getReportParameterIds().size(); i++) {
            colTypes.add("C");
        }
        for (int j = 0; j < container.getTableMeasure().size(); j++) {
            colTypes.add("N");
        }
        columnTypes = new int[colTypes.size()];
        for (int k = 0; k < colTypes.size(); k++) {
            if (colTypes.get(k).equalsIgnoreCase("C")) {
                columnTypes[k] = Types.VARCHAR;
            } else {
                columnTypes[k] = Types.NUMERIC;
            }
        }
        retObj.setColumnTypesInt(columnTypes);
//                   if (!(dimId.startsWith("A_")))
//                       dimId = "A_"+dimId;

        List searchValList = null;

        //List searchValueList = new ArrayList();
//        searchValList.add("WB1");
//        searchValList.add("UP2");
//        searchValueList.add("SWT");
//        searchValueList.add("HLT");

        Set<String> paramSet = paramMap.keySet();
        for (String str : paramSet) {
            searchValList = new ArrayList();
            searchValList.add(paramMap.get(str));
            filter.add(str, "IN", searchValList);
            retObj.resetViewSequence();
            columnList.add(str);
        }

        //filter.add("A_75770", "IN", searchValList);



//        columnList.add("A_75770");
//        columnList.add("A_75777");
        //retObj.resetViewSequence();

        // filter.add("A_75778", "IN", searchValueList);
        //retObj.resetViewSequence();
        DataSetFilter dataSetFilter = new DataSetFilter();
        dataSetFilter.setData(retObj, columnList);
        dataSetFilter.setSearchFilter(filter);
        intvals = dataSetFilter.searchDataSet();
        retObj.setViewSequence(intvals);

        String filterRetObj = this.getNewReturnObject(viewbys, advancedHtmlData);
        return filterRetObj;

    }

    public PbReturnObject getRetObj() {
        return retObj;
    }

    public void setRetObj(PbReturnObject retObj) {
        this.retObj = retObj;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public HashMap<String, String> getDrillMap() {
        return drillMap;
    }

    public void setDrillMap(HashMap<String, String> drillMap) {
        this.drillMap = drillMap;
    }

    public ArrayList<String> uniqueDimValues(String elementId) {
        Set<String> dimValueSet = new HashSet<String>();
        ArrayList<String> valueList = new ArrayList<String>();
        for (int i = 0; i < this.retObj.getRowCount(); i++) {
            dimValueSet.add(retObj.getFieldValueString(i, elementId).trim());
        }
        for (String str : dimValueSet) {
            valueList.add(str);
        }

        return valueList;
    }

    public HashMap<String, ArrayList<String>> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(HashMap<String, ArrayList<String>> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public LinkedHashMap<String, String> getFilters() {
        return filters;
    }

    public void setFilters(LinkedHashMap<String, String> filters) {
        this.filters = filters;
    }
}
