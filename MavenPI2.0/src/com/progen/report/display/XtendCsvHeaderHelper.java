package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;
import java.util.ArrayList;

public class XtendCsvHeaderHelper extends TableHeaderDisplay {

    TableBuilder builder;
    ArrayList<String> timedetailArray;
    ArrayList<String> parameterDertails;

    public XtendCsvHeaderHelper(TableBuilder builder, ArrayList values, ArrayList parameterDertails) {
        super(builder);
        this.timedetailArray = values;
        this.parameterDertails = parameterDertails;
    }

    @Override
    public StringBuilder generateHTML() {
        TableHeaderRow[] headerRows;
        headerRows = tableBldr.getHeaderRowData();
        int colCount = tableBldr.getColumnCount();
        StringBuilder header = new StringBuilder();

        String heading;
        String id;
        String sortImage;
        int rowSpan;
        int colSpan;
        String dispStyle;

        //[Day, PRG_DATE_RANGE, 08/31/2011, 09/30/2011, 08/01/2011, 08/30/2011, 0018JanReport, 02/27/2013]

//        if(!timedetailArray.isEmpty() && !timedetailArray.get(0).equalsIgnoreCase("wihtoutParam")){
//         if (!timedetailArray.isEmpty() && timedetailArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
//        header.append("Progen Business Solutions").append("\n").append("\n");
//            header.append("Report Title ").append(":").append(timedetailArray.get(5)).append("\n");
//            header.append("Created Date :").append(timedetailArray.get(6)).append("\n");
//        header.append("Date         :").append(timedetailArray.get(2)).append("\n");
//        header.append("Duration     :").append(timedetailArray.get(3)).append("\n");
//        header.append("Compare With :").append(timedetailArray.get(4)).append("\n");
//
//                    if(!parameterDertails.isEmpty()){
//                        for(int i=0;i<parameterDertails.size();i++){
//                           header.append("Filter Applyed On  ").append(parameterDertails.get(i)).append("\n");
//              }
//            }
//
//                }else if(!timedetailArray.isEmpty() &&  timedetailArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")){
//                    header.append("Progen Business Solutions").append("\n").append("\n");
//                    header.append("Report Title ").append(":").append(timedetailArray.get(6)).append("\n");
//                    header.append("Created Date :").append(timedetailArray.get(7)).append("\n");
//                    header.append("FromDate         :").append(timedetailArray.get(2)).append("\n");
//                    header.append("ToDate     :").append(timedetailArray.get(3)).append("\n");
//                    header.append("CompareFromDate :").append(timedetailArray.get(4)).append("\n");
//                    header.append("CompareToDate :").append(timedetailArray.get(5)).append("\n");
//
//                    if(!parameterDertails.isEmpty()){
//                        for(int i=0;i<parameterDertails.size();i++){
//                           header.append("Filter Applyed On  ").append(parameterDertails.get(i)).append("\n");
//        }
//                    }
//                }
//        }else{
//            header.append("Report Title ").append(":").append(timedetailArray.get(1)).append("\n");
//            header.append("Created Date :").append(timedetailArray.get(2)).append("\n");
//        }

        for (TableHeaderRow headerRow : headerRows) {

            for (int i = tableBldr.getFromColumn(); i < colCount; i++) {
                heading = headerRow.getRowData(i);

                header.append(heading);
                if (i != colCount - 1) {
                    header.append(",");
                }

            }
            header.append("\n");
        }

        return super.parentHtml.append(header);
    }
}
