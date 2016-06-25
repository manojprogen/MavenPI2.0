/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report;

/**
 *
 * @author Progen
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

public class DateComperator implements Comparator<Map<String, String>> {

    public static Logger logger = Logger.getLogger(DateComperator.class);
    public String sortingView;
    public String sortType;
    public ArrayList<String> timeDetails;

    public DateComperator(String sortingView, String sortType, ArrayList<String> timeDetails) {
        this.sortingView = sortingView;
        this.sortType = sortType;
        this.timeDetails = timeDetails;
    }

    private int getWeight(String day) {
        if (day.trim().equalsIgnoreCase("SUNDAY")) {
            return 0;
        } else if (day.trim().equalsIgnoreCase("MONDAY")) {
            return 1;
        } else if (day.trim().equalsIgnoreCase("TUESDAY")) {
            return 2;
        } else if (day.trim().equalsIgnoreCase("WEDNESDAY")) {
            return 3;
        } else if (day.trim().equalsIgnoreCase("THURSDAY")) {
            return 4;
        } else if (day.trim().equalsIgnoreCase("FRIDAY")) {
            return 5;
        } else {
            return 6;
        }
    }

    @Override
    public int compare(
            Map<String, String> o1, Map<String, String> o2) {
        int i = 0;
        if (sortingView.toString().toLowerCase().contains("week")) {
            i = getWeight(o1.get(sortingView)) - getWeight(o2.get(sortingView));
        } else if (sortingView.toString().replaceAll("\\W", "").trim().toLowerCase().equalsIgnoreCase("qtryear") || (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Qtr") && sortingView.trim().equalsIgnoreCase("TIME"))) {
            Set<String> keySet1 = o1.keySet();
            Iterator<String> iterator1 = keySet1.iterator();
            String key1 = "";
            if (iterator1.hasNext()) {
                key1 = iterator1.next();
            }
            String value1 = o1.get(key1);
            Set<String> keySet2 = o2.keySet();
            Iterator<String> iterator2 = keySet2.iterator();
            String key2 = "";
            if (iterator2.hasNext()) {
                key2 = iterator2.next();
            }
            String value2 = o2.get(key2);
            value1 = value1.replaceAll(" ", "").trim();
            value2 = value2.replaceAll(" ", "").trim();
            String[] split1 = value1.split("-");
            String[] split2 = value2.split("-");
            if(split1!=null && split1.length>1){
            if (timeDetails != null && timeDetails.get(3).equalsIgnoreCase("Qtr") && sortingView.trim().equalsIgnoreCase("TIME")) {
                if (Integer.valueOf(split1[1]) > Integer.valueOf(split2[1])) {
                    i = -1;
                } else if (Integer.valueOf(split1[1]) < Integer.valueOf(split2[1])) {
                    i = 1;
                } else {
//                    i=split1[0].compareTo(split2[0]);
                    i = split2[0].compareTo(split1[0]);
                }
            } else {
                if (Integer.valueOf(split1[1]) > Integer.valueOf(split2[1])) {
                    i = 1;
                } else if (Integer.valueOf(split1[1]) < Integer.valueOf(split2[1])) {
                    i = -1;
                } else {
                    i = split1[0].compareTo(split2[0]);
                }
            }
            }
        } else {

            Date date1 = null, date2 = null;

            String[] formats = {
                "MMM-yyyy", "MMM-yyyy",
                "MM-dd-yyyy", "MM-dd-yyyy",
                "MMM-d-yyyy", "MMM-d-yyyy",
                "MM/dd/yyyy", "MM/dd/yyyy",
                "MMM/d/yyyy", "MMM/d/yyyy",
                "MMM-yy", "MMM-yy",
                "MMMM", "MMMM",
                "EEEE", "EEEE",
                "yyyy", "yyyy",};
            String format = "";
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(o1.get(sortingView).replaceAll(" ", ""));
                    format = parse;
//                    
                    break;
                } catch (ParseException e) {
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                if (sortingView.equalsIgnoreCase("month")) {
                    date1 = sdf.parse(o1.get(sortingView).replaceAll(" ", ""));
                    date2 = sdf.parse(o2.get(sortingView).replaceAll(" ", ""));
                } else {
                    date1 = sdf.parse(o1.get(sortingView).replaceAll(" ", ""));
                    date2 = sdf.parse(o2.get(sortingView).replaceAll(" ", ""));
                }

            } catch (ParseException ex) {
                logger.error("Exception:", ex);
            }
            if (timeDetails != null && (timeDetails.get(3).equalsIgnoreCase("Month") || timeDetails.get(3).equalsIgnoreCase("Year")) && sortingView.equalsIgnoreCase("TIME")) {
                i = date2.compareTo(date1);
            } else {

                i = date1.compareTo(date2);
            }
        }
        if (sortType.equalsIgnoreCase("desc")) {
            return -i;
        } else {
            return i;
        }
    }
}
