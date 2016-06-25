package com.progen.report;

import java.util.Comparator;
import java.util.Map;

public class JsonComperator implements Comparator<Map<String, String>> {

    public String sortingMeasure;
    public String sortType;
    public String sortColumns;
    int i = 0;

    public JsonComperator(String sortingMeasure, String sortType, String sortColumns) {
        this.sortingMeasure = sortingMeasure;
        this.sortType = sortType;
        this.sortColumns = sortColumns;
    }

    @Override
//    public int compare(
//            
//        Map<String,String> o1, Map<String,String> o2) {
//         if(!o2.get(sortingMeasure).equalsIgnoreCase("") && !o1.get(sortingMeasure).equalsIgnoreCase("")){
//            if(sortColumns!=null && "A".equals(sortColumns)){
//                if(sortType.equalsIgnoreCase("asc")){
//                    return o1.get(sortingMeasure).compareTo(o2.get(sortingMeasure));
//                }else{
//                    return o2.get(sortingMeasure).compareTo(o1.get(sortingMeasure));
//                }
//            
//            }else{
//         i=Double.compare(Double.parseDouble(o1.get(sortingMeasure)),Double.parseDouble(o2.get(sortingMeasure)));
//            
//               if(sortType.equalsIgnoreCase("desc")){
//            return -i;
//        }else{
//            return i;
//        }
//            }
//        
//         
//         }
//         else{
//         return  0;
//         }
//    }
    public int compare(
            Map<String, String> o1, Map<String, String> o2) {
        if (o2.get(sortingMeasure) != null && o1.get(sortingMeasure) != null) {

            if (!o2.get(sortingMeasure).equalsIgnoreCase("") && !o1.get(sortingMeasure).equalsIgnoreCase("")) {

                int i = 0;
                if (sortColumns != null && "A".equals(sortColumns)) {
                    i = o2.get(sortingMeasure).compareToIgnoreCase(o1.get(sortingMeasure));
                } else {
                    i = Double.compare(Double.parseDouble(o1.get(sortingMeasure)), Double.parseDouble(o2.get(sortingMeasure)));
                }
                if (sortType.equalsIgnoreCase("desc")) {
                    return -i;
                } else {
                    return i;
                }
            } else {
                if (o2.get(sortingMeasure).equalsIgnoreCase("")) {
                    return -1;
                }
                if (o1.get(sortingMeasure).equalsIgnoreCase("")) {
                    return 1;
                }
            }
        }
        return 0;
    }
}