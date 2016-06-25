package com.progen.report.util.sort;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.progen.db.ProgenDataSet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 * Extracts data from Resultset and Sort the data After sorting, caller can
 * retrieve the view Sequence and use that view Sequence to display the rows
 * Also includes functionality for Top5/Bottom5 which is essentially sort and
 * retrieve that many rows
 *
 * @author arun
 */
public class DataSetSorter {

    public static Logger logger = Logger.getLogger(DataSetSorter.class);
    ArrayList<RowData> rowDataList;
    ArrayList<RowData> rowMeasureDataList;
    int noOfColumns;
public String groupColumns=null;
public String search=null;
    public DataSetSorter() {
    }

    public void setData(ProgenDataSet retObj, ArrayList<String> columns) {
        Object[][] data = retObj.retrieveDataBasedOnViewSeq(columns);
        setData(data, retObj.getViewSequence());
    }

    public void setData(Object[][] data, ArrayList<Integer> viewSequence) {
        this.rowDataList = new ArrayList<RowData>(data.length);
        if (data.length > 0) {
            this.noOfColumns = data[0].length;
            RowData rowData;

            for (int i = 0; i < data.length; i++) {
                rowData = new RowData();
                rowData.setRowSequence(viewSequence.get(i));
                for (int j = 0; j < data[i].length; j++) {
                    if (j == 0) {
                        rowData.setData1(data[i][j]);
                    }
                    if (j == 1) {
                        rowData.setData2(data[i][j]);
                    }
                    if (j == 2) {
                        rowData.setData3(data[i][j]);
                    }
                    if (j == 3) {
                        rowData.setData4(data[i][j]);
                    }
                    if (j == 4) {
                        rowData.setData5(data[i][j]);
                    }
                    if (j == 5) {
                        rowData.setData6(data[i][j]);
                    }
                    if (j == 7) {
                        rowData.setData7(data[i][j]);
                    }
                    if (j == 6) {
                        rowData.setData8(data[i][j]);
                    }
                    if (j == 8) {
                        rowData.setData9(data[i][j]);
                    }
                    if (j == 9) {
                        rowData.setData10(data[i][j]);
                    }
                }
                rowDataList.add(i, rowData);
            }
        }
    }

    //added by Amar
    public void setMeasuresData(Object[][] data, ArrayList<Integer> viewSequence) {
        this.rowMeasureDataList = new ArrayList<RowData>(data.length);
        if (data.length > 0) {
            this.noOfColumns = data[0].length;
            RowData rowData;

            for (int i = 0; i < data.length; i++) {
                rowData = new RowData();
                rowData.setRowSequence(viewSequence.get(i));
                for (int j = 0; j < data[i].length; j++) {
                    if (j == 0) {
                        rowData.setData1(data[i][j]);
                    }
                    if (j == 1) {
                        rowData.setData2(data[i][j]);
                    }
                    if (j == 2) {
                        rowData.setData3(data[i][j]);
                    }
                    if (j == 3) {
                        rowData.setData4(data[i][j]);
                    }
                    if (j == 4) {
                        rowData.setData5(data[i][j]);
                    }
                    if (j == 5) {
                        rowData.setData6(data[i][j]);
                    }
                    if (j == 7) {
                        rowData.setData7(data[i][j]);
                    }
                    if (j == 6) {
                        rowData.setData8(data[i][j]);
                    }
                    if (j == 8) {
                        rowData.setData9(data[i][j]);
                    }
                    if (j == 9) {
                        rowData.setData10(data[i][j]);
                    }
                    if (j == 10) {
                        rowData.setData11(data[i][j]);
                    }
                    if (j == 11) {
                        rowData.setData12(data[i][j]);
                    }
                    if (j == 12) {
                        rowData.setData13(data[i][j]);
                    }
                    if (j == 13) {
                        rowData.setData14(data[i][j]);
                    }
                    if (j == 14) {
                        rowData.setData15(data[i][j]);
                    }
                    if (j == 15) {
                        rowData.setData16(data[i][j]);
                    }
                    if (j == 16) {
                        rowData.setData17(data[i][j]);
                    }
                    if (j == 17) {
                        rowData.setData18(data[i][j]);
                    }
                    if (j == 18) {
                        rowData.setData19(data[i][j]);
                    }
                    if (j == 19) {
                        rowData.setData20(data[i][j]);
                    }
                }
                rowMeasureDataList.add(i, rowData);
            }
        }
    }

    /**
     * Prepare the dataset for sorting From the Object[][] array we will make
     * the RowData ArrayList this list will be further used for sorting or
     * top/bottom operations
     *
     * @param dataTypes
     * @param data
     */
//    public void setData(char[] dataTypes,Object[][] data, ArrayList<Integer> viewSequence)
//    {
//        this.setData(data, viewSequence);
//    }
//    public ArrayList<Integer> sortData(char[] sortOrder)
//    {
//         ArrayList<RowData> sortedList = this.sortRowData(sortOrder);
//         ArrayList<Integer> rowSeq = new ArrayList<Integer>();
//         for ( RowData data : sortedList )
//         {
//             rowSeq.add(data.getRowSequence());
//         }
//         return rowSeq;
//    }
    public ArrayList<Integer> sortData(ArrayList<SortOrder> sortOrder) {
        assert (sortOrder != null);
        assert (sortOrder.size() == noOfColumns);
        ArrayList<RowData> sortedList = this.sortRowData(sortOrder);
        ArrayList<Integer> rowSeq = new ArrayList<Integer>();
        for (RowData data : sortedList) {
            rowSeq.add(data.getRowSequence());
        }
        return rowSeq;
    }

    /**
     * Returns the row sequence for the top/bottom set of rows First it will
     * sort the data, then based on the topBtmCount it will fetch that many
     * records sortOrder determines top or bottom if Asc then its bottom if Dsc
     * then its Top If there are more than one column then first sort all
     * Dimension cols and finally sort the measure column There will only be one
     * Measure column coming
     *
     * @param topBtmCount
     * @param sortOrder Sort order of the individual data elements
     * @return
     */
    public ArrayList<Integer> findTopBottom(int topBtmCount, ArrayList<SortOrder> sortOrderLst) {
        char[] sortOrder = new char[sortOrderLst.size()];
        int index = 0;
        ArrayList<RowData> sortedList = this.sortRowData(sortOrderLst);
        ArrayList<Integer> rowSeq = new ArrayList<Integer>();

        if (sortedList.size() < topBtmCount) {
            topBtmCount = sortedList.size();
        }
        //across all the data find Top/Bottom 5
        if (sortOrder.length == 1) {
            for (int i = 0; i < topBtmCount; i++) {
                rowSeq.add(sortedList.get(i).getRowSequence());
            }
        } else {
            rowSeq = this.getTopBottomRows(sortedList, topBtmCount, sortOrder.length);
        }
        return rowSeq;
    }

    /**
     * If there are more than one sort columns then its actually finding
     * Top/Bottom within a group We will take the last - 1 column (ie column
     * preceding the measure column) Then for each element in that we find
     * Top/Bottom eg AP Mahesh Scooter 10000 AP Xyz Scooter 1000 AP Msh Scooter
     * 1000 AP sh Scooter 10 AP asdh Scooter 1000 TN Mahasdfash Scooter 20000 TN
     * Maheasdf Scooter 30000 TN Masdf Scooter 30000 TN aaash Scooter 30000 TN
     * AAh Scooter 33 TN hesh Scooter 300 TN asdfasahesh Scooter 10400
     *
     * User will first sort by State then Say Sort of 3rd column which is Sales
     * (say) So we will take State Names in a LinkedHashSet Then for each
     * element in the Set we will find the Top or Bottom 5. For each of those
     * records retrieved we get the Row Sequence This row Sequence will be
     * returned back
     *
     * @param sortedList
     * @param topBtmCount
     * @param sortColCount
     * @return
     */
    private ArrayList<Integer> getTopBottomRows(ArrayList<RowData> sortedList, int topBtmCount, int sortColCount) {
        ArrayList<Integer> rowSequence = new ArrayList<Integer>();
        LinkedHashSet<String> lastColEdge = new LinkedHashSet<String>();
        int count;

        for (RowData row : sortedList) {
            if (sortColCount == 2) {
                lastColEdge.add(row.getData1().toString());
            } else if (sortColCount == 3) {
                lastColEdge.add(row.getData2().toString());
            } else if (sortColCount == 4) {
                lastColEdge.add(row.getData3().toString());
            } else if (sortColCount == 5) {
                lastColEdge.add(row.getData4().toString());
            } else if (sortColCount == 6) {
                lastColEdge.add(row.getData5().toString());
            } else if (sortColCount == 7) {
                lastColEdge.add(row.getData6().toString());
            } else if (sortColCount == 8) {
                lastColEdge.add(row.getData7().toString());
            } else if (sortColCount == 9) {
                lastColEdge.add(row.getData8().toString());
            } else if (sortColCount == 10) {
                lastColEdge.add(row.getData9().toString());
            }

        }

        Iterable<RowData> iterList;
        for (String edgeVal : lastColEdge) {
            //////.println("Set: "+edgeVal);
            count = 0;
            iterList = this.filterList(sortedList, edgeVal, sortColCount - 1);
            for (RowData rowData : iterList) {
                rowSequence.add(rowData.getRowSequence());
                count++;
                if (topBtmCount == count) {
                    break;
                }
            }
        }
        return rowSequence;
    }

    /**
     * For each value retrieve from the list matching RowData sets eg. AP is
     * value Then from the sortedList retrieve all elements which has RowData
     * for AP AP is stored in column of the RowData. Uses Google Collections
     * Predicate to retreive the record
     *
     * @param sortedList
     * @param value
     * @param column
     * @return List of all RowData matching the value in the RowData
     */
    private Iterable<RowData> filterList(ArrayList<RowData> sortedList, final String value, final int column) {
        //ArrayList<RowData> filteredLst = new ArrayList<RowData>();

        Predicate<RowData> predicate = new Predicate<RowData>() {

            public boolean apply(RowData input) {
                if (column == 1) {
                    if (input.getData1() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData1().toString());
                    }
                } else if (column == 2) {
                    if (input.getData2() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData2().toString());
                    }
                } else if (column == 3) {
                    if (input.getData3() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData3().toString());
                    }
                } else if (column == 4) {
                    if (input.getData4() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData4().toString());
                    }
                } else if (column == 5) {
                    if (input.getData5() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData5().toString());
                    }
                } else if (column == 6) {
                    if (input.getData6() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData6().toString());
                    }
                } else if (column == 7) {
                    if (input.getData7() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData7().toString());
                    }
                } else if (column == 8) {
                    if (input.getData8() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData8().toString());
                    }
                } else if (column == 9) {
                    if (input.getData9() == null) {
                        return false;
                    } else {
                        return value.equals(input.getData9().toString());
                    }
                } else {
                    return false;
                }
            }
        };

        Iterable<RowData> filtered = Iterables.filter(sortedList, predicate);
        return filtered;
    }

    private ArrayList<RowData> sortRowData(ArrayList<SortOrder> sortOrder) {
        Function[] dataFunctions = new Function[this.noOfColumns + 1];
        Ordering<RowData> compoundOrdering = null;
        Ordering<RowData> singleOrdering;

        int i = 0;
        if (!rowDataList.isEmpty()) {
            RowData firstRow = rowDataList.get(0);
            for (i = 0; i < noOfColumns; i++) {
                //Code added by mayank
                String str = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\-\\d{4}";
                String str1 = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\ - \\d{4}";
                Pattern pattern;
                pattern = Pattern.compile(str);
                Pattern pattern1 = Pattern.compile(str1);
                //end of code..
                Object data = null;
                if (i == 0) {
                    data = firstRow.getData1();
                }
                if (i == 1) {
                    data = firstRow.getData2();
                }
                if (i == 2) {
                    data = firstRow.getData3();
                }
                if (i == 3) {
                    data = firstRow.getData4();
                }
                if (i == 4) {
                    data = firstRow.getData5();
                }
                if (i == 5) {
                    data = firstRow.getData6();
                }
                if (i == 6) {
                    data = firstRow.getData7();
                }
                if (i == 7) {
                    data = firstRow.getData8();
                }
                if (i == 8) {
                    data = firstRow.getData9();
                }
                if (i == 9) {
                    data = firstRow.getData10();
                }
                //code added by mayank..
                Matcher match = pattern.matcher(data.toString().trim());
                Matcher match1 = pattern1.matcher(data.toString().trim());
//            
//            
//            
                if (match.matches() || match1.matches()) {
                    dataFunctions[i] = getDateDataFunction(i);
                } //end of code..
                else if (data instanceof Number) {
                    dataFunctions[i] = getBigDecimalDataFunction(i);
                } else {
                    dataFunctions[i] = getStringDataFunction(i);
                }
            }
        }
        dataFunctions[i] = getRowSequenceFunction();


        for (i = 0; i < sortOrder.size() && i < dataFunctions.length; i++) {
            if (sortOrder.get(i) == SortOrder.ASCENDING) {
                singleOrdering = Ordering.natural().onResultOf(dataFunctions[i]);
            } else {
                singleOrdering = Ordering.natural().onResultOf(dataFunctions[i]).reverse();
            }
            if (compoundOrdering == null) {
                compoundOrdering = singleOrdering;
            } else {
                compoundOrdering = compoundOrdering.compound(singleOrdering);
            }
        }
        if (i >= dataFunctions.length) {
            i = dataFunctions.length - 1;
        }
        compoundOrdering = compoundOrdering.compound(Ordering.natural().onResultOf(dataFunctions[i]));

        //for ( RowData data : this.rowDataList )
        //    ////.println(data);
       ArrayList<RowData> sortedList=null;
         //added by sruthi for grouping subtotal 
       if (groupColumns!= null && !groupColumns.toString().isEmpty()) {
            sortedList=this.rowDataList;
      }else
          sortedList = (ArrayList<RowData>) compoundOrdering.nullsLast().sortedCopy(this.rowDataList);//

        return sortedList;

    }

    /**
     * Get Function Object for retrieving BigDecimal or numeric data from
     * RowData While sorting if data is null Comparable throws Null Pointer
     * Exception So have to handle NPR here
     *
     * @param index - index of the fuction or the column index
     * @return Function for getting Numeric data of the RowData for this index
     */
    // Added by Mayank..
    private Function<RowData, Date> getDateDataFunction(final int index) {

        Function<RowData, Date> function = new Function<RowData, Date>() {

            DateFormat df = new SimpleDateFormat("MMM-yyyy");

            public Date apply(RowData from) {
                if (index == 0) {
                    if (from.getData1() == null) {
                        return null;
                    } else if (from.getData1().toString().contains("-")) {
                        String str = from.getData1().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                } else if (index == 1) {
                    if (from.getData2() == null) {
                        return null;
                    } else if (from.getData2().toString().contains("-")) {
                        String str = from.getData2().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                } else if (index == 2) {
                    if (from.getData3() == null) {
                        return null;
                    } else if (from.getData3().toString().contains("-")) {
                        String str = from.getData3().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }

                } else if (index == 3) {
                    if (from.getData4() == null) {
                        return null;
                    } else if (from.getData4().toString().contains("-")) {
                        String str = from.getData4().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                } else if (index == 4) {
                    if (from.getData5() == null) {
                        return null;
                    } else if (from.getData5().toString().contains("-")) {
                        String str = from.getData5().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }
                } else if (index == 5) {
                    if (from.getData6() == null) {
                        return null;
                    } else if (from.getData6().toString().contains("-")) {
                        String str = from.getData6().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }

                } else if (index == 6) {
                    if (from.getData7() == null) {
                        return null;
                    } else if (from.getData7().toString().contains("-")) {
                        String str = from.getData7().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }

                } else if (index == 7) {
                    if (from.getData8() == null) {
                        return null;
                    } else if (from.getData8().toString().contains("-")) {
                        String str = from.getData8().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }
                    }

                } else if (index == 8) {
                    if (from.getData9() == null) {
                        return null;
                    } else if (from.getData9().toString().contains("-")) {
                        String str = from.getData9().toString().replaceAll(" ", "");
                        try {
                            return df.parse(str);
                        } catch (ParseException ex) {
                            logger.error("Exception:", ex);
                        }

                    }

                } else if (from.getData10() == null) {
                    return null;
                } else if (from.getData10().toString().contains("-")) {
                    String str = from.getData10().toString().replaceAll(" ", "");
                    try {
                        return df.parse(str);
                    } catch (ParseException ex) {
                        logger.error("Exception:", ex);
                    }
                }
                return new Date();
            }
        };
        return function;

    }
    //End of code....(20-june-14)

    private Function<RowData, BigDecimal> getBigDecimalDataFunction(final int index) {
        Function<RowData, BigDecimal> function = new Function<RowData, BigDecimal>() {

            public BigDecimal apply(RowData from) {
                if (index == 0) {
                    if (from.getData1() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData1() instanceof Number) {
                        return (BigDecimal) from.getData1();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 1) {
                    if (from.getData2() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData2() instanceof Number) {
                        return (BigDecimal) from.getData2();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 2) {
                    if (from.getData3() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData3() instanceof Number) {
                        return (BigDecimal) from.getData3();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 3) {
                    if (from.getData4() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData4() instanceof Number) {
                        return (BigDecimal) from.getData4();
                    } else {
                        return new BigDecimal(0);
                    }
                } else if (index == 4) {
                    if (from.getData5() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData5() instanceof Number) {
                        return (BigDecimal) from.getData5();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 5) {
                    if (from.getData6() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData6() instanceof Number) {
                        return (BigDecimal) from.getData6();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 6) {
                    if (from.getData7() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData7() instanceof Number) {
                        return (BigDecimal) from.getData7();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 7) {
                    if (from.getData8() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData8() instanceof Number) {
                        return (BigDecimal) from.getData8();
                    } else {
                        return new BigDecimal(0);
                    }

                } else if (index == 8) {
                    if (from.getData9() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData9() instanceof Number) {
                        return (BigDecimal) from.getData9();
                    } else {
                        return new BigDecimal(0);
                    }

                } else {
                    if (from.getData10() == null) {
                        return new BigDecimal(0);
                    } else if (from.getData10() instanceof Number) {
                        return (BigDecimal) from.getData10();
                    } else {
                        return new BigDecimal(0);
                    }

                }
            }
        };
        return function;
    }

    /**
     * Get Function Object for retrieving String data from RowData While sorting
     * if data is null Comparable throws Null Pointer Exception So have to
     * handle NPR here
     *
     * @param index - index of the fuction or the column index
     * @return Function for getting String data of the RowData for this index
     */
    private Function<RowData, String> getStringDataFunction(final int index) {
        Function<RowData, String> function = new Function<RowData, String>() {

            public String apply(RowData from) {
                if (index == 0) {
                    if (from.getData1() == null) {
                        return "";
                    } else {
                        return from.getData1().toString();
                    }
                } else if (index == 1) {
                    if (from.getData2() == null) {
                        return "";
                    } else {
                        return from.getData2().toString();
                    }
                } else if (index == 2) {
                    if (from.getData3() == null) {
                        return "";
                    } else {
                        return from.getData3().toString();
                    }
                } else if (index == 3) {
                    if (from.getData4() == null) {
                        return "";
                    } else {
                        return from.getData4().toString();
                    }
                } else if (index == 4) {
                    if (from.getData5() == null) {
                        return "";
                    } else {
                        return from.getData5().toString();
                    }
                } else if (index == 5) {
                    if (from.getData6() == null) {
                        return "";
                    } else {
                        return from.getData6().toString();
                    }
                } else if (index == 6) {
                    if (from.getData7() == null) {
                        return "";
                    } else {
                        return from.getData7().toString();
                    }
                } else if (index == 7) {
                    if (from.getData8() == null) {
                        return "";
                    } else {
                        return from.getData8().toString();
                    }
                } else if (index == 8) {
                    if (from.getData9() == null) {
                        return "";
                    } else {
                        return from.getData9().toString();
                    }
                } else if (from.getData10() == null) {
                    return "";
                } else {
                    return from.getData10().toString();
                }

            }
        };
        return function;
    }

    /**
     * Get Function Object for retrieving Row Sequence After sorting the new row
     * sequence to display the data will be got from this row sequence Row
     * Sequence is always the last function to be added in the Compound Ordering
     * Row Sequence can never be null
     *
     * @return Function for getting data of the rowSequence of a RowData
     */
    private Function<RowData, Integer> getRowSequenceFunction() {
        Function<RowData, Integer> function = new Function<RowData, Integer>() {

            public Integer apply(RowData from) {
                return from.getRowSequence();
            }
        };
        return function;
    }

    public ArrayList<Integer> groupData() {

        ArrayList<RowData> sortedList = this.groupRowData();
        ArrayList<Integer> rowSeq = new ArrayList<Integer>();
        for (RowData data : sortedList) {
            rowSeq.add(data.getRowSequence());
        }
        return rowSeq;
    }

    private ArrayList<RowData> groupRowData() {
        Function[] dataFunctions = new Function[this.noOfColumns + 1];
        ArrayList<RowData> duplicatelist = new ArrayList();
        ArrayList<RowData> duplicatelist1 = new ArrayList();
        duplicatelist1 = (ArrayList<RowData>) rowDataList.clone();
        int i = 0;
        if (!duplicatelist1.isEmpty()) {
            for (int j = 0; j < rowDataList.size(); j++) {
                RowData firstRow1 = rowDataList.get(j);
                RowData temp = firstRow1;
                for (int k = 0; k < duplicatelist1.size(); k++) {
                    RowData firstRow2 = duplicatelist1.get(k);
                    if (firstRow1.getData1().toString().equalsIgnoreCase(firstRow2.getData1().toString())) {
                        duplicatelist.add(duplicatelist1.get(k));
                        duplicatelist1.remove(k);
                        k--;
                    }
                }


            }
        }


        return duplicatelist;

    }

    private ArrayList<RowData> processRowData(ArrayList<RowData> dataList, int index, int colCount) {
        LinkedHashSet<String> lastColEdge = new LinkedHashSet<String>();
        Iterable<RowData> iter;

        int count;



        if (index == colCount) {
            ArrayList<RowData> internRowData = new ArrayList<RowData>();
            for (RowData data : dataList) {
                internRowData.add(data);
            }
            return internRowData;
        } else {
            for (RowData row : dataList) {
                if (index == 1) {
                    lastColEdge.add(row.getData1().toString());
                } else if (index == 2) {
                    lastColEdge.add(row.getData2().toString());
                } else if (index == 3) {
                    lastColEdge.add(row.getData3().toString());
                } else if (index == 4) {
                    lastColEdge.add(row.getData4().toString());
                } else if (index == 5) {
                    lastColEdge.add(row.getData5().toString());
                } else if (index == 6) {
                    lastColEdge.add(row.getData6().toString());
                } else if (index == 7) {
                    lastColEdge.add(row.getData7().toString());
                } else if (index == 8) {
                    lastColEdge.add(row.getData8().toString());
                } else if (index == 9) {
                    lastColEdge.add(row.getData9().toString());
                } else if (index == 10) {
                    lastColEdge.add(row.getData10().toString());
                }
            }
            ArrayList<RowData> rootRowData = new ArrayList<RowData>();
            for (String colEdge : lastColEdge) {
                ArrayList<RowData> caryDataList = new ArrayList<RowData>();
//               if((index+1) == colCount){
                iter = this.filterList(dataList, colEdge, (index));
//               }else{
//                   iter=this.filterList(dataList,colEdge,(index+1));
//               }
                for (RowData data1 : iter) {
                    caryDataList.add(data1);
                }
                ArrayList<RowData> innerDataList = processRowData(caryDataList, (index + 1), colCount);
                rootRowData.addAll(innerDataList);
            }
            return rootRowData;
        }
    }
    /*
     * @author srikanth.p This method will process the row data to sort
     * according to subtotal It first fetches all the distinct root level view
     * bys for each viewby all its child view bys fetched and will be Subtotal
     * SOrt Helperlist where first level view bys will be treated at root
     * level(zero level) second column viewbys will be at first level so on..
     * leaf level view bys will contains viewsequence. Sort::root level will be
     * sorted first and by iterating through the sorted root elments all childs
     * will be sorted and the respected sequence nums will be taken from leaf
     * level
     *
     */

    public ArrayList<Integer> sortOnSubTotal(char sortType, int sortColCount, String element, Container container) {
        Iterable<RowData> iter;
        Iterable<RowData> iterNew;
        ArrayList<RowData> sortedList = this.rowDataList;
        // code added by Amar
        String aggType = "";
        String refferedElements = "";
        String userColType = "";
        String refElementType = "";
        String tempFormula = "";
        String formulaStd = "";
        int rowCnt = 0;
        BigDecimal rootLeafSubTotalTemp;

        PbDb pbdb = new PbDb();
        PbReturnObject retobj = null;
        String eleId = element.replace("A_", "");
        String qry = "select  ACTUAL_COL_FORMULA,REFFERED_ELEMENTS,USER_COL_TYPE,REF_ELEMENT_TYPE,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + eleId;
        try {
            retobj = pbdb.execSelectSQL(qry);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retobj != null && retobj.getRowCount() > 0) {
            aggType = retobj.getFieldValueString(0, 4);
            refferedElements = retobj.getFieldValueString(0, 1);
            userColType = retobj.getFieldValueString(0, 2);
            refElementType = retobj.getFieldValueString(0, 3);
            formulaStd = retobj.getFieldValueString(0, 0);
        }
        // End of code by Amar
        ArrayList<SubTotalSortHelper> subTotalSortHelperList = new ArrayList<SubTotalSortHelper>();
        for (int i = 0; i < (sortColCount - 1); i++) {
            SubTotalSortHelper levelSortHelper = new SubTotalSortHelper();
            levelSortHelper.setLevel(i);
            subTotalSortHelperList.add(levelSortHelper);
        }
        if (subTotalSortHelperList.size() > 0) {
            subTotalSortHelperList.get((subTotalSortHelperList.size() - 1)).setLeaf(true);
            subTotalSortHelperList.get(0).setRoot(true);
        }

        LinkedHashSet<String> rootCol = new LinkedHashSet<String>();
        for (RowData row : sortedList) {
            rootCol.add(row.getData1().toString());
        }
        int rootIndex = 1;
        SubTotalSortHelper subTotHelper = subTotalSortHelperList.get(rootIndex - 1);
        if (!userColType.equalsIgnoreCase("SUMMARIZED")) {
            for (String rootElem : rootCol) {
                Double rootLeafSubTotal = 0.0;
                LinkedList rootLeafViewSeq = new LinkedList();
                ArrayList<Double> dataList = new ArrayList<Double>();
                ArrayList<RowData> finalDataForRootElem = new ArrayList<RowData>();
                ArrayList<RowData> rootRowData = new ArrayList<RowData>();
                iter = this.filterList(sortedList, rootElem, rootIndex);
                for (RowData rowdata : iter) {
                    rootRowData.add(rowdata);
                    if (subTotHelper.isLeaf()) {
                        rootLeafSubTotal += ((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue();
                        rootLeafViewSeq.add(rowdata.rowSequence);
                        dataList.add(((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue());
                        //code added by Amar
                        if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                            rowCnt++;
                        }
                        //end of code by Amar
                    }
                }
                finalDataForRootElem = this.processRowData(rootRowData, (rootIndex + 1), sortColCount);
//                processRowDataForSubTotals(finalDataForRootElem, sortColCount,subTotalSortHelperList);
                // code added by Amar
                if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                    rootLeafSubTotalTemp = new BigDecimal(rootLeafSubTotal);
                    if (rowCnt != 0) {
                        rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                        rootLeafSubTotal = rootLeafSubTotalTemp.doubleValue();
                        rowCnt = 0;
                    }
                }
                //End of code by Amar
                int passRootIndx = rootIndex;
                if (subTotHelper.isLeaf() && subTotHelper.isRoot()) {
                    int parentIndex = subTotHelper.addToNodeSet(rootElem);
                    subTotHelper.addSubTotal(rootLeafSubTotal);
                    subTotHelper.addViewSequence(rootLeafViewSeq);
                    subTotHelper.addDataList(dataList);
                } else {
                    int parentIndex = subTotHelper.addToNodeSet(rootElem);
                    Double subTotal = arrangeSubTotals(finalDataForRootElem, passRootIndx, sortColCount, subTotalSortHelperList, parentIndex, (rootIndex + 1), sortType);
                    //code added by Amar
                    for (RowData rowdata : iter) {
                        if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                            rowCnt++;
                        }
                    }
                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                        rootLeafSubTotalTemp = new BigDecimal(subTotal);
                        if (rowCnt != 0) {
                            rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                            subTotal = rootLeafSubTotalTemp.doubleValue();
                            rowCnt = 0;
                        }
                    }
                    //End of code by Amar
                    subTotHelper.addSubTotal(subTotal);
                }


            }
        } else {  // code added by Amar
            if (aggType.equalsIgnoreCase("AVG") || refElementType.equalsIgnoreCase("4")) {
                String[] tempStore = refferedElements.split("\\s*,\\s*");
                ArrayList<String> items = new ArrayList<String>();
                String temp;
                for (int i = 0; i < tempStore.length; i++) {
                    temp = "A_" + tempStore[i];
                    items.add(temp);
                }
                ArrayList<String> allMeasure = container.getTableDisplayMeasures();
                char[] sortDataTypes = null;
                sortDataTypes = container.getColumnDataTypes(items);
                int viewByCount = container.getViewByCount();
                if (allMeasure.containsAll(items)) {
                    //int viewByCount=container.getViewByCount();
                    if (viewByCount > 3) {
                        if (subTotalSortHelperList.size() > 0) {
                            subTotalSortHelperList.get(0).setLeaf(true);
                            subTotalSortHelperList.get(0).setRoot(true);
                        }
                    }
                    ArrayList<RowData> sortedMeasuresList = this.rowMeasureDataList;
                    Double rootLeafSubTotal = 0.0;
//                  if (subTotalSortHelperList.size() > 0) {
//                      subTotalSortHelperList.get(0).setLeaf(true);
//                      subTotalSortHelperList.get(0).setRoot(true);
//                  }
                    for (String rootElem : rootCol) {
                        String refEleArray[] = refferedElements.split(",");
                        int len = refEleArray.length;
                        int flag = 1;
                        String mysqlString = "";
                        tempFormula = formulaStd;
                        PbReturnObject retobj1 = null;
                        LinkedList rootLeafViewSeq = new LinkedList();
                        ArrayList<RowData> rootRowData = new ArrayList<RowData>();
                        ArrayList<RowData> finalDataForRootElem = new ArrayList<RowData>();
                        ArrayList<Double> dataList = new ArrayList<Double>();
                        //if(subTotHelper.isLeaf()){
                        for (int i = 0; i < len; i++) {
                            String elementId = refEleArray[i];
                            String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;

                            try {
                                retobj1 = pbdb.execSelectSQL(getBussColName);
                            } catch (SQLException ex) {
                                logger.error("Exception:", ex);
                            }
                            if (retobj1 != null && retobj1.getRowCount() > 0) {
                                String bussColName = retobj1.getFieldValueString(0, 0);
                                String aggrType = retobj1.getFieldValueString(0, 1);
                                if (tempFormula.contains(bussColName)) {
                                    String newEleID = "A_" + elementId;
                                    tempFormula = tempFormula.replace(bussColName, newEleID);
                                    BigDecimal subTotalValueForEle = null;
                                    int rowCnt1 = 0;

                                    Double rootLeafSubTotal1 = 0.0;
                                    BigDecimal rootLeafSubTotalTempNew = null;
                                    iter = this.filterList(sortedMeasuresList, rootElem, rootIndex);

                                    for (RowData rowdata : iter) {
                                        if (getRowDataAtIndex(rowdata, viewByCount + i).toString() == null || getRowDataAtIndex(rowdata, viewByCount + i).toString().isEmpty()) {
                                            rootLeafSubTotal1 += BigDecimal.ZERO.doubleValue();
                                        } else {
                                            rootLeafSubTotal1 += Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString());
                                            if (Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString()) != 0) {
                                                rowCnt1++;
                                            }
                                        }
                                    }
                                    subTotalValueForEle = new BigDecimal(rootLeafSubTotal1);
                                    if (subTotalValueForEle == null) {
                                        flag = 0;
                                    }
                                    if (rowCnt1 == 0) {
                                        rowCnt1 = 1;
                                    }
                                    if (flag == 1) {
                                        subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                        if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                            subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(rowCnt1), RoundingMode.HALF_UP);
                                        }
                                        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                            mysqlString = mysqlString + "," + subTotalValueForEle + " AS " + newEleID;
                                        } else {
                                            tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
                                        }

                                    }
                                }
                            }
                        }
                        // }
                        iterNew = this.filterList(sortedList, rootElem, rootIndex);
                        for (RowData rowdata : iterNew) {
                            rootRowData.add(rowdata);
                            // if(subTotHelper.isLeaf()){
                            dataList.add(((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue());
                            rootLeafViewSeq.add(rowdata.rowSequence);
                            //}
                        }

                        finalDataForRootElem = this.processRowData(rootRowData, (rootIndex + 1), sortColCount);
                        //Calculate formula
                        //if(!tempFormula.equalsIgnoreCase("")){
                        //facade.container.summarizedHashmap.put(facade.getColumnId(column), "OTHERS");
                        //rootLeafSubTotal = getComputeFormulaVal(tempFormula,mysqlString,"ST");
                        int passRootIndx = rootIndex;
                        if (!tempFormula.equalsIgnoreCase("")) {
                            rootLeafSubTotal = getComputeFormulaVal(tempFormula, mysqlString, "ST");
                        }
                        if (subTotHelper.isLeaf() && subTotHelper.isRoot()) {
//                                     if(!tempFormula.equalsIgnoreCase("")){
//                                         rootLeafSubTotal = getComputeFormulaVal(tempFormula,mysqlString,"ST");
//                                     }
                            int parentIndex = subTotHelper.addToNodeSet(rootElem);
                            subTotHelper.addSubTotal(rootLeafSubTotal);
                            subTotHelper.addViewSequence(rootLeafViewSeq);
                            subTotHelper.addDataList(dataList);
                        } else {
                            int parentIndex = subTotHelper.addToNodeSet(rootElem);
                            Double subTotalTemp = this.arrangeSubTotalsOnSummMeasures(finalDataForRootElem, passRootIndx, sortColCount, subTotalSortHelperList, rootElem, parentIndex, (rootIndex + 1), sortType, refferedElements, formulaStd, viewByCount);
                            Double subTotal = rootLeafSubTotal;
                            subTotHelper.addSubTotal(subTotal);
                        }
                    }
                } else {
                    for (String rootElem : rootCol) {
                        Double rootLeafSubTotal = 0.0;
                        LinkedList rootLeafViewSeq = new LinkedList();
                        ArrayList<Double> dataList = new ArrayList<Double>();
                        ArrayList<RowData> finalDataForRootElem = new ArrayList<RowData>();
                        ArrayList<RowData> rootRowData = new ArrayList<RowData>();
                        iter = this.filterList(sortedList, rootElem, rootIndex);
                        for (RowData rowdata : iter) {
                            rootRowData.add(rowdata);
                            if (subTotHelper.isLeaf()) {
                                rootLeafSubTotal += ((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue();
                                rootLeafViewSeq.add(rowdata.rowSequence);
                                dataList.add(((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue());
                                //code added by Amar
                                if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                                    rowCnt++;
                                }
                                //end of code by Amar
                            }
                        }
                        finalDataForRootElem = this.processRowData(rootRowData, (rootIndex + 1), sortColCount);
//                processRowDataForSubTotals(finalDataForRootElem, sortColCount,subTotalSortHelperList);
                        // code added by Amar
                        if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                            rootLeafSubTotalTemp = new BigDecimal(rootLeafSubTotal);
                            if (rowCnt != 0) {
                                rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                rootLeafSubTotal = rootLeafSubTotalTemp.doubleValue();
                                rowCnt = 0;
                            }
                        }

                        int passRootIndx = rootIndex;
                        if (subTotHelper.isLeaf() && subTotHelper.isRoot()) {
                            int parentIndex = subTotHelper.addToNodeSet(rootElem);
                            subTotHelper.addSubTotal(rootLeafSubTotal);
                            subTotHelper.addViewSequence(rootLeafViewSeq);
                            subTotHelper.addDataList(dataList);
                        } else {
                            int parentIndex = subTotHelper.addToNodeSet(rootElem);
                            Double subTotal = arrangeSubTotals(finalDataForRootElem, passRootIndx, sortColCount, subTotalSortHelperList, parentIndex, (rootIndex + 1), sortType);

                            for (RowData rowdata : iter) {
                                if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                                    rowCnt++;
                                }
                            }
                            if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                                rootLeafSubTotalTemp = new BigDecimal(subTotal);
                                if (rowCnt != 0) {
                                    rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                    subTotal = rootLeafSubTotalTemp.doubleValue();
                                    rowCnt = 0;
                                }
                            }
                            //End of code by Amar
                            subTotHelper.addSubTotal(subTotal);
                        }
                    }
                }
            } else {
                for (String rootElem : rootCol) {
                    Double rootLeafSubTotal = 0.0;
                    LinkedList rootLeafViewSeq = new LinkedList();
                    ArrayList<Double> dataList = new ArrayList<Double>();
                    ArrayList<RowData> finalDataForRootElem = new ArrayList<RowData>();
                    ArrayList<RowData> rootRowData = new ArrayList<RowData>();
                    iter = this.filterList(sortedList, rootElem, rootIndex);
                    for (RowData rowdata : iter) {
                        rootRowData.add(rowdata);
                        if (subTotHelper.isLeaf()) {
                            rootLeafSubTotal += ((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue();
                            rootLeafViewSeq.add(rowdata.rowSequence);
                            dataList.add(((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue());
                            //code added by Amar
                            if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                                rowCnt++;
                            }
                            //end of code by Amar
                        }
                    }
                    finalDataForRootElem = this.processRowData(rootRowData, (rootIndex + 1), sortColCount);
//                processRowDataForSubTotals(finalDataForRootElem, sortColCount,subTotalSortHelperList);
                    // code added by Amar
                    if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                        rootLeafSubTotalTemp = new BigDecimal(rootLeafSubTotal);
                        if (rowCnt != 0) {
                            rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                            rootLeafSubTotal = rootLeafSubTotalTemp.doubleValue();
                            rowCnt = 0;
                        }
                    }
                    //End of code by Amar
                    int passRootIndx = rootIndex;
                    if (subTotHelper.isLeaf() && subTotHelper.isRoot()) {
                        int parentIndex = subTotHelper.addToNodeSet(rootElem);
                        subTotHelper.addSubTotal(rootLeafSubTotal);
                        subTotHelper.addViewSequence(rootLeafViewSeq);
                        subTotHelper.addDataList(dataList);
                    } else {
                        int parentIndex = subTotHelper.addToNodeSet(rootElem);
                        Double subTotal = arrangeSubTotals(finalDataForRootElem, passRootIndx, sortColCount, subTotalSortHelperList, parentIndex, (rootIndex + 1), sortType);
                        //code added by Amar
                        for (RowData rowdata : iter) {
                            if (((BigDecimal) getRowDataAtIndex(rowdata, sortColCount)).doubleValue() != 0) {
                                rowCnt++;
                            }
                        }
                        if (aggType.equalsIgnoreCase("AVG") || aggType.toUpperCase().contains("AVG")) {

                            rootLeafSubTotalTemp = new BigDecimal(subTotal);
                            if (rowCnt != 0) {
                                rootLeafSubTotalTemp = rootLeafSubTotalTemp.divide(new BigDecimal(rowCnt), RoundingMode.HALF_UP);
                                subTotal = rootLeafSubTotalTemp.doubleValue();
                                rowCnt = 0;
                            }
                        }
                        //End of code by Amar
                        subTotHelper.addSubTotal(subTotal);
                    }


                }
            }
        }
        ArrayList<Integer> rowSequence = new ArrayList<Integer>();
        rowSequence = sortAllLevels(subTotalSortHelperList, sortType);
        return rowSequence;
    }

    private Double arrangeSubTotals(ArrayList<RowData> filteredList, int index, int colCount, ArrayList<SubTotalSortHelper> subTotalSortHelperList, int parentPoss, int nextlevel, char sortType) {
        LinkedHashSet<String> lastColEdge = new LinkedHashSet<String>();
        Iterable<RowData> iter;
        SubTotalSortHelper subTotSortHelper = subTotalSortHelperList.get(index);
        for (RowData row : filteredList) {
            if (nextlevel == 1) {
                lastColEdge.add(row.getData1().toString());
            } else if (nextlevel == 2) {
                lastColEdge.add(row.getData2().toString());
            } else if (nextlevel == 3) {
                lastColEdge.add(row.getData3().toString());
            } else if (nextlevel == 4) {
                lastColEdge.add(row.getData4().toString());
            } else if (nextlevel == 5) {
                lastColEdge.add(row.getData5().toString());
            } else if (nextlevel == 6) {
                lastColEdge.add(row.getData6().toString());
            } else if (nextlevel == 7) {
                lastColEdge.add(row.getData7().toString());
            } else if (nextlevel == 8) {
                lastColEdge.add(row.getData8().toString());
            } else if (nextlevel == 9) {
                lastColEdge.add(row.getData9().toString());
            } else if (nextlevel == 10) {
                lastColEdge.add(row.getData10().toString());
            }
        }


        if (subTotSortHelper.isLeaf()) {
            Double tot = 0.0;
            for (String colEdge : lastColEdge) {
                ArrayList subTotalList = new ArrayList();
                ArrayList viewSequence = new ArrayList();
                iter = this.filterList(filteredList, colEdge, (index + 1));
                Double subTotal = 0.0;
                for (RowData data1 : iter) {
                    subTotal += ((BigDecimal) getRowDataAtIndex(data1, colCount)).doubleValue();
                    subTotalList.add(((BigDecimal) getRowDataAtIndex(data1, colCount)).doubleValue());
                    viewSequence.add(data1.rowSequence);
                }
                int parentIndex = subTotSortHelper.addToNodeSet(colEdge);
                subTotSortHelper.setParentNode(parentIndex, parentPoss);
                subTotSortHelper.addSubTotal(subTotal);
                ArrayList changedSeq = sortSubElements(subTotalList, sortType);
                LinkedList<Integer> modifiedIndex = new LinkedList<Integer>();
                for (int i = 0; i < changedSeq.size(); i++) {
                    int tempIndx = (Integer) changedSeq.get(i);
                    tot += (Double) subTotalList.get(tempIndx);
                    modifiedIndex.add((Integer) viewSequence.get(tempIndx));
                }
                subTotSortHelper.addViewSequence(modifiedIndex);
            }
            return tot;
        } else {
            Double tot = 0.0;
            for (String colEdge : lastColEdge) {
                ArrayList<RowData> caryDataList = new ArrayList<RowData>();
                Double subTotal = 0.0;
                int parentIndex = subTotSortHelper.addToNodeSet(colEdge);
                subTotSortHelper.setParentNode(parentIndex, parentPoss);
                iter = this.filterList(filteredList, colEdge, (index + 1));
                for (RowData data1 : iter) {
                    caryDataList.add(data1);
                }
                subTotal += arrangeSubTotals(caryDataList, (index + 1), colCount, subTotalSortHelperList, parentIndex, (nextlevel + 1), sortType);
                tot += subTotal;
                subTotSortHelper.addSubTotal(subTotal);
            }
            return tot;
        }
    }

    private Object getRowDataAtIndex(RowData rowData, int colCount) {
        Object data = null;
        switch (colCount) {
            case 1:
                data = rowData.getData1();
                break;
            case 2:
                data = rowData.getData2();
                break;
            case 3:
                data = rowData.getData3();
                break;
            case 4:
                data = rowData.getData4();
                break;
            case 5:
                data = rowData.getData5();
                break;
            case 6:
                data = rowData.getData6();
                break;
            case 7:
                data = rowData.getData7();
                break;
            case 8:
                data = rowData.getData8();
                break;
            case 9:
                data = rowData.getData9();
                break;
            case 10:
                data = rowData.getData10();
                break;
            case 11:
                data = rowData.getData11();
                break;
            case 12:
                data = rowData.getData12();
                break;
            case 13:
                data = rowData.getData13();
                break;
            case 14:
                data = rowData.getData14();
                break;
            case 15:
                data = rowData.getData15();
                break;
            case 16:
                data = rowData.getData16();
                break;
            case 17:
                data = rowData.getData17();
                break;
            case 18:
                data = rowData.getData18();
                break;
            case 19:
                data = rowData.getData19();
                break;
            case 20:
                data = rowData.getData20();
                break;
        }
        return data;
    }

    private ArrayList sortAllLevels(ArrayList<SubTotalSortHelper> helperList, char sortType) {
        ArrayList finalViewSequence = new ArrayList();
        if (!helperList.isEmpty()) {
            SubTotalSortHelper rootHelper = helperList.get(0);
            rootHelper.setSortType(sortType);
            rootHelper.sortThisLevel();
            LinkedList rootNodeList = rootHelper.getNodeSet();

            LinkedList<Integer> subViewSequence = rootHelper.getSubViewSequence();
            for (int j = 0; j < subViewSequence.size(); j++) {
                int index = subViewSequence.get(j);
                String rootParent = rootNodeList.get(index).toString();
                int nextindex = 1;
                if (rootHelper.isRoot() && rootHelper.isLeaf()) {
                    nextindex = 0;
                }
                if (rootHelper.isLeaf() && rootHelper.isRoot()) {
                    List internSeq = (LinkedList) rootHelper.getViewSequence().get((Integer) subViewSequence.get(j));
                    ArrayList<Double> dataList = (ArrayList<Double>) rootHelper.getDataList().get((Integer) subViewSequence.get(j));
                    ArrayList internelViewSeq = sortSubElements(dataList, sortType);
                      if (this.groupColumns!= null && !this.groupColumns.toString().isEmpty()) {
                          if(this.search!=null && !this.search.isEmpty() && this.search.equalsIgnoreCase("BTM")){
                              if(sortType=='D')
                          internelViewSeq=sortSubElements(dataList, 'A');
                              else 
                             internelViewSeq=sortSubElements(dataList, 'D');     
                          }
                   } 
                    LinkedList sequence = new LinkedList();
                    for (int k = 0; k < internelViewSeq.size(); k++) {
                        sequence.add(internSeq.get((Integer) internelViewSeq.get(k)));
                    }
                    finalViewSequence.add(sequence);
                } else {
                    getViewSequenceFromChilds(helperList, nextindex, index, finalViewSequence, sortType);
                }


            }
        }
        ArrayList viewSequence = new ArrayList();
        for (int i = 0; i < finalViewSequence.size(); i++) {
            LinkedList internelList = (LinkedList) finalViewSequence.get(i);
            for (int k = 0; k < internelList.size(); k++) {
                viewSequence.add(internelList.get(k));
            }
        }
        return viewSequence;
    }

    private void getViewSequenceFromChilds(ArrayList<SubTotalSortHelper> helperList, int index, int parentIndex, ArrayList viewSequence, char sortType) {
        if (helperList.size() >= index) {
            SubTotalSortHelper helper = helperList.get(index);
            LinkedList nodeList = helper.getNodeSet();
            ArrayList matchedIndeces = new ArrayList();
            ArrayList nextLevelParents = new ArrayList();
            ArrayList matchedSubTots = new ArrayList();
            for (int i = 0; i < nodeList.size(); i++) {
                String node = nodeList.get(i).toString();
                int parent = (Integer) helper.getParentMap().get(i);
                if (parent == parentIndex) {
                    matchedIndeces.add(i);
                    nextLevelParents.add(i);
                    matchedSubTots.add(helper.getSubTotalList().get(i));
                }
            }
            ArrayList internelViewSeq = sortSubElements(matchedSubTots, sortType);

            if (helper.isLeaf()) {
                for (int k = 0; k < internelViewSeq.size(); k++) {
                    int seqPoss = (Integer) matchedIndeces.get((Integer) internelViewSeq.get(k));
                    viewSequence.add(helper.getViewSequence().get(seqPoss));
                }
                return;
            } else {
                for (int l = 0; l < internelViewSeq.size(); l++) {
                    int caryParent = (Integer) nextLevelParents.get((Integer) internelViewSeq.get(l));
                    getViewSequenceFromChilds(helperList, index + 1, caryParent, viewSequence, sortType);
                }
            }
        }
    }

    private ArrayList sortSubElements(ArrayList subTotalList, char sortType) {
        ArrayList viewSequence = new ArrayList();
        Ordering<Double> ordering = Ordering.natural().nullsLast().onResultOf(new Function<Double, Double>() {

            public Double apply(Double data) {
                return data;
            }
        });
        ArrayList<Double> sortedList = null;
        if (sortType == 'A') {
            sortedList = (ArrayList<Double>) ordering.nullsLast().sortedCopy(subTotalList);
        } else {
            sortedList = (ArrayList<Double>) ordering.reverse().nullsLast().sortedCopy(subTotalList);
        }
        Object tempObject = subTotalList.clone();
        ArrayList tempSubTotal = (ArrayList) tempObject;
        for (Double value : sortedList) {
            int foundIndex = tempSubTotal.indexOf(value);
            tempSubTotal.add(foundIndex, null);
            tempSubTotal.remove(foundIndex + 1);
            viewSequence.add(foundIndex);
        }
        return viewSequence;
    }

    public Double getComputeFormulaVal(String tempFormula, String mysqlString, String stType) {
        PbDb pbdb = new PbDb();
        String formula = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            tempFormula = "SELECT " + tempFormula;
        } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            mysqlString = mysqlString.substring(1);
            tempFormula = "SELECT " + tempFormula + " FROM (SELECT " + mysqlString + ") A";
        } else {
            tempFormula = "SELECT " + tempFormula + " FROM DUAL";
        }
        PbReturnObject retobj2 = null;
        try {

            retobj2 = pbdb.execSelectSQL(tempFormula);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retobj2.getRowCount() > 0) {
            formula = retobj2.getFieldValueString(0, 0);
            if (formula.equalsIgnoreCase("")) {
                formula = "0";
            }
            BigDecimal subTotalVal = new BigDecimal(formula);
            subTotalVal = subTotalVal.setScale(2, RoundingMode.CEILING);
            return subTotalVal.doubleValue();
        }
        return 0.0;
    }

    private Double arrangeSubTotalsOnSummMeasures(ArrayList<RowData> filteredList, int index, int colCount, ArrayList<SubTotalSortHelper> subTotalSortHelperList, String rootElm, int parentPoss, int nextlevel, char sortType, String refferedElements, String formulaStd, int viewByCount) {
        LinkedHashSet<String> lastColEdge = new LinkedHashSet<String>();
        ArrayList<RowData> sortedMeasuresList = this.rowMeasureDataList;
        Iterable<RowData> iter;
        Iterable<RowData> iterNew;
        Iterable<RowData> iterTmp;
        SubTotalSortHelper subTotSortHelper = subTotalSortHelperList.get(index);
        for (RowData row : filteredList) {
            if (nextlevel == 1) {
                lastColEdge.add(row.getData1().toString());
            } else if (nextlevel == 2) {
                lastColEdge.add(row.getData2().toString());
            } else if (nextlevel == 3) {
                lastColEdge.add(row.getData3().toString());
            } else if (nextlevel == 4) {
                lastColEdge.add(row.getData4().toString());
            } else if (nextlevel == 5) {
                lastColEdge.add(row.getData5().toString());
            } else if (nextlevel == 6) {
                lastColEdge.add(row.getData6().toString());
            } else if (nextlevel == 7) {
                lastColEdge.add(row.getData7().toString());
            } else if (nextlevel == 8) {
                lastColEdge.add(row.getData8().toString());
            } else if (nextlevel == 9) {
                lastColEdge.add(row.getData9().toString());
            } else if (nextlevel == 10) {
                lastColEdge.add(row.getData10().toString());
            }
        }


        if (subTotSortHelper.isLeaf()) {
            PbDb pbdb = new PbDb();
            Double tot = 0.0;
            ArrayList<RowData> finalTempRootRowData = new ArrayList<RowData>();
            ArrayList<RowData> tempRootRowData = new ArrayList<RowData>();
            iterTmp = this.filterList(sortedMeasuresList, rootElm, index);
            for (RowData rowdata : iterTmp) {
                tempRootRowData.add(rowdata);
            }
            finalTempRootRowData = this.processRowData(tempRootRowData, index, colCount);
            String refEleArray[] = refferedElements.split(",");
            int len = refEleArray.length;
            PbReturnObject retobj2 = null;
            for (String colEdge : lastColEdge) {
                int flag = 1;
                int flag1 = 0;
                int flag2 = 0;
                String mysqlString = "";
                String tempFormula = formulaStd;
                PbReturnObject retobj1 = null;
                LinkedList rootLeafViewSeq = new LinkedList();
                ArrayList<RowData> rootRowData = new ArrayList<RowData>();
                ArrayList<RowData> finalDataForRootElem = new ArrayList<RowData>();
                ArrayList<Double> dataList = new ArrayList<Double>();
                ArrayList subTotalList = new ArrayList();
                ArrayList viewSequence = new ArrayList();
                Double subTotal = 0.0;
                for (int i = 0; i < len; i++) {
                    String elementId = refEleArray[i];
                    String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                    try {
                        retobj1 = pbdb.execSelectSQL(getBussColName);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                    if (retobj1 != null && retobj1.getRowCount() > 0) {
                        flag1++;
                        String bussColName = retobj1.getFieldValueString(0, 0);
                        String aggrType = retobj1.getFieldValueString(0, 1);
                        if (tempFormula.contains(bussColName)) {
                            flag2++;
                            String newEleID = "A_" + elementId;
                            tempFormula = tempFormula.replace(bussColName, newEleID);
                            BigDecimal subTotalValueForEle = null;
                            int rowCnt1 = 0;

                            Double rootLeafSubTotal1 = 0.0;
                            BigDecimal rootLeafSubTotalTempNew = null;
                            //iter = this.filterList(sortedMeasuresList, colEdge, (index+1));
                            iter = this.filterList(finalTempRootRowData, colEdge, (index + 1));
                            for (RowData rowdata : iter) {
                                if (getRowDataAtIndex(rowdata, viewByCount + i).toString() == null || getRowDataAtIndex(rowdata, viewByCount + i).toString().isEmpty()) {
                                    rootLeafSubTotal1 += BigDecimal.ZERO.doubleValue();
                                } else {
                                    rootLeafSubTotal1 += Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString());
                                    if (Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString()) != 0) {
                                        rowCnt1++;
                                    }
                                }
                            }
                            subTotalValueForEle = new BigDecimal(rootLeafSubTotal1);
                            if (subTotalValueForEle == null) {
                                flag = 0;
                            }
                            if (rowCnt1 == 0) {
                                rowCnt1 = 1;
                            }
                            if (flag == 1) {
                                subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
                                if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                    subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(rowCnt1), RoundingMode.HALF_UP);
                                }
                                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                    mysqlString = mysqlString + "," + subTotalValueForEle + " AS " + newEleID;
                                } else {
                                    tempFormula = tempFormula.replace(newEleID, subTotalValueForEle.toString());
                                }
                            }
                        }
                    }
                }
                iterNew = this.filterList(filteredList, colEdge, index + 1);
                for (RowData rowdata : iterNew) {
                    subTotalList.add(((BigDecimal) getRowDataAtIndex(rowdata, colCount)).doubleValue());
                    viewSequence.add(rowdata.rowSequence);
                }

                if (!tempFormula.equalsIgnoreCase("")) {
                    tot = getComputeFormulaVal(tempFormula, mysqlString, "ST");
                }
                int parentIndex = subTotSortHelper.addToNodeSet(colEdge);
                subTotSortHelper.setParentNode(parentIndex, parentPoss);
                subTotSortHelper.addSubTotal(tot);
                ArrayList changedSeq = sortSubElements(subTotalList, sortType);
                LinkedList<Integer> modifiedIndex = new LinkedList<Integer>();


                for (int i = 0; i < changedSeq.size(); i++) {
                    int tempIndx = (Integer) changedSeq.get(i);
//                        tot += (Double)subTotalList.get(tempIndx);
                    modifiedIndex.add((Integer) viewSequence.get(tempIndx));
                }
                subTotSortHelper.addViewSequence(modifiedIndex);
            }
            String mysqlString = "";
            String tempFormula1 = formulaStd;
            for (int i = 0; i < len; i++) {
                int flag = 1;
                String elementId = refEleArray[i];
                String getBussColName = "select USER_COL_NAME,AGGREGATION_TYPE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID =" + elementId;
                try {
                    retobj2 = pbdb.execSelectSQL(getBussColName);
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
                if (retobj2 != null && retobj2.getRowCount() > 0) {
                    String bussColName = retobj2.getFieldValueString(0, 0);
                    String aggrType = retobj2.getFieldValueString(0, 1);
                    if (tempFormula1.contains(bussColName)) {
                        String newEleID = "A_" + elementId;
                        tempFormula1 = tempFormula1.replace(bussColName, newEleID);
                        BigDecimal subTotalValueForEle = null;
                        int rowCnt1 = 0;

                        Double rootLeafSubTotal1 = 0.0;
                        BigDecimal rootLeafSubTotalTempNew = null;

                        for (RowData rowdata : iterTmp) {
                            if (getRowDataAtIndex(rowdata, viewByCount + i).toString() == null || getRowDataAtIndex(rowdata, viewByCount + i).toString().isEmpty()) {
                                rootLeafSubTotal1 += BigDecimal.ZERO.doubleValue();
                            } else {
                                rootLeafSubTotal1 += Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString());
                                if (Double.parseDouble(getRowDataAtIndex(rowdata, viewByCount + i).toString()) != 0) {
                                    rowCnt1++;
                                }
                            }
                        }
                        if (rowCnt1 == 0) {
                            rowCnt1 = 1;

                        }
                        subTotalValueForEle = new BigDecimal(rootLeafSubTotal1);
                        if (subTotalValueForEle == null) {
                            flag = 0;
                        }
                        if (flag == 1) {
                            subTotalValueForEle = subTotalValueForEle.setScale(2, RoundingMode.CEILING);
                            if (aggrType.equalsIgnoreCase("AVG") || aggrType.toUpperCase().contains("AVG")) {
                                subTotalValueForEle = subTotalValueForEle.divide(new BigDecimal(rowCnt1), RoundingMode.HALF_UP);
                            }
                            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                                mysqlString = mysqlString + "," + subTotalValueForEle + " AS " + newEleID;
                            } else {
                                tempFormula1 = tempFormula1.replace(newEleID, subTotalValueForEle.toString());
                            }
                        }
                    }
                }
            }
            if (!tempFormula1.equalsIgnoreCase("")) {
                tot = getComputeFormulaVal(tempFormula1, mysqlString, "ST");
            }
            return tot;
        } else {
            Double tot = 0.0;
            for (String colEdge : lastColEdge) {
                ArrayList<RowData> caryDataList = new ArrayList<RowData>();
                Double subTotal = 0.0;
                int parentIndex = subTotSortHelper.addToNodeSet(colEdge);
                subTotSortHelper.setParentNode(parentIndex, parentPoss);
                iter = this.filterList(filteredList, colEdge, (index + 1));
                for (RowData data1 : iter) {
                    caryDataList.add(data1);
                }
                //subTotal += arrangeSubTotals(caryDataList, (index+1), colCount, subTotalSortHelperList,parentIndex,(nextlevel+1),sortType);
                subTotal += this.arrangeSubTotalsOnSummMeasures(caryDataList, (index + 1), colCount, subTotalSortHelperList, colEdge, parentIndex, (nextlevel + 1), sortType, refferedElements, formulaStd, viewByCount);
                tot += subTotal;
                subTotSortHelper.addSubTotal(subTotal);
            }
            return tot;
        }
    }
}