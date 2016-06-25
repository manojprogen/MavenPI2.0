package com.progen.report.display;

import com.progen.report.data.TableBuilder;
import com.progen.report.data.TableHeaderRow;
import com.progen.report.display.table.TableHeaderDisplay;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;

public class CSVTableHeaderDisplay extends TableHeaderDisplay {

    public static Logger logger = Logger.getLogger(CSVTableHeaderDisplay.class);
    TableBuilder builder;
    ArrayList<String> timedetailArray;
    ArrayList<String> parameterDertails;

    public CSVTableHeaderDisplay(TableBuilder builder, ArrayList values, ArrayList parameterDertails) {
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
        //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
        Date date = new Date();
        String DATE_FORMAT = "MM/dd/yyyy";
        String DATE_FORMAT1 = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT1);
        Date FromDate = null;
        Date ToDate = null;
        Date CmpFromDate = null;
        Date CmpToDate = null;
        Date CreatedDate = null;
//            try {
//                FromDate = sdf.parse(timedetailArray.get(2).toString());
//                ToDate = sdf.parse(timedetailArray.get(3).toString());
//                CmpFromDate = sdf.parse(timedetailArray.get(4).toString());
//                CmpToDate = sdf.parse(timedetailArray.get(5).toString());
//                CreatedDate = sdf.parse(timedetailArray.get(6).toString());

//            } catch (ParseException ex) {
//            logger.error("Exception:",ex);
//            }
        //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
        //[Day, PRG_DATE_RANGE, 08/31/2011, 09/30/2011, 08/01/2011, 08/30/2011, 0018JanReport, 02/27/2013]
        if (!timedetailArray.isEmpty() && !timedetailArray.get(0).equalsIgnoreCase("wihtoutParam")) {
            if (!timedetailArray.isEmpty() && timedetailArray.get(1).toString().equalsIgnoreCase("PRG_STD")) {
                try {
                    header.append("Progen Business Solutions").append("\n").append("\n");
                    header.append("Report Title ").append(":").append(timedetailArray.get(5)).append("\n");
                    //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                    //                    header.append("Created Date :").append(timedetailArray.get(6)).append("\n");
                    //                    header.append("Date         :").append(timedetailArray.get(2)).append("\n");
                    //                    header.append("Duration     :").append(timedetailArray.get(3)).append("\n");
                    //                    header.append("Compare With :").append(timedetailArray.get(4)).append("\n");
                    //                    header.append("Created Date :").append(sdf1.format(CreatedDate)).append("\n");
                    FromDate = sdf.parse(timedetailArray.get(2).toString());
                    //ToDate = sdf.parse(timedetailArray.get(3).toString());
//                    CmpFromDate = sdf.parse(timedetailArray.get(4).toString());
//                    CmpToDate = sdf.parse(timedetailArray.get(5).toString());
                    header.append("Created Date :").append(sdf1.format(date)).append("\n");
                    header.append("Date         :").append(sdf1.format(FromDate)).append("\n");
                    header.append("Duration     :").append(timedetailArray.get(3)).append("\n");
                    header.append("Compare With :").append(timedetailArray.get(4)).append("\n");
                    //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                    if (!parameterDertails.isEmpty()) {
                        for (int i = 0; i < parameterDertails.size(); i++) {
                            header.append("Filter Applyed On  ").append(parameterDertails.get(i)).append("\n");
                        }
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }
            } else if (!timedetailArray.isEmpty() && timedetailArray.get(1).toString().equalsIgnoreCase("PRG_DATE_RANGE")) {
                try {
                    header.append("Progen Business Solutions").append("\n").append("\n");
                    header.append("Report Title ").append(":").append(timedetailArray.get(6)).append("\n");
                    //Start of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                    //                    header.append("Created Date :").append(timedetailArray.get(7)).append("\n");
                    //                    header.append("FromDate         :").append(timedetailArray.get(2)).append("\n");
                    //                    header.append("ToDate     :").append(timedetailArray.get(3)).append("\n");
                    //                    header.append("CompareFromDate :").append(timedetailArray.get(4)).append("\n");
                    //                    header.append("CompareToDate :").append(timedetailArray.get(5)).append("\n");
                    //                    header.append("Created Date :").append(sdf1.format(CreatedDate)).append("\n");
                    FromDate = sdf.parse(timedetailArray.get(2).toString());
                    ToDate = sdf.parse(timedetailArray.get(3).toString());
                    CmpFromDate = sdf.parse(timedetailArray.get(4).toString());
                    CmpToDate = sdf.parse(timedetailArray.get(5).toString());
                    header.append("Created Date :").append(sdf1.format(date)).append("\n");
                    header.append("FromDate         :").append(sdf1.format(FromDate)).append("\n");
                    header.append("ToDate     :").append(sdf1.format(ToDate)).append("\n");
                    header.append("CompareFromDate :").append(sdf1.format(CmpFromDate)).append("\n");
                    header.append("CompareToDate :").append(sdf1.format(CmpToDate)).append("\n");
                    //end of code by Nazneen on 2 June 2014 for date format as DD/MM/YYYY
                    if (!parameterDertails.isEmpty()) {
                        for (int i = 0; i < parameterDertails.size(); i++) {
                            header.append("Filter Applyed On  ").append(parameterDertails.get(i)).append("\n");
                        }
                    }
                } catch (ParseException ex) {
                    logger.error("Exception:", ex);
                }
            }
        } else {
            header.append("Report Title ").append(":").append(timedetailArray.get(1)).append("\n");
//                header.append("Created Date :").append(timedetailArray.get(2)).append("\n");
            header.append("Created Date :").append(sdf1.format(CreatedDate)).append("\n");
        }
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
