/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.colorgroup;

import com.google.common.collect.ArrayListMultimap;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author progen
 */
public class ColorConstants {

    static ArrayListMultimap<String, String> colorMap;
    static ColorConstants colorConst;

    public ColorConstants() {
        colorMap = ArrayListMultimap.create();
        //Color codes for red
//        colorMap.put("Red", "#330000");
//        colorMap.put("Red", "#4C0000");
//        colorMap.put("Red", "#660000");
//        colorMap.put("Red", "#800000");
//        colorMap.put("Red", "#990000");
//        colorMap.put("Red", "#CC0000");
//        colorMap.put("Red", "#FF0000");
//        colorMap.put("Red", "#FF3333");
//        colorMap.put("Red", "#FF6666");
//        colorMap.put("Red", "#FF9999");
//        colorMap.put("Red", "#FFCCCC");

        colorMap.put("Red", "#E60000");
        colorMap.put("Red", "#FF0000");
        colorMap.put("Red", "#FF1919");
        colorMap.put("Red", "#FF3333");
        colorMap.put("Red", "#FF4D4D");
        colorMap.put("Red", "#FF6666");
        colorMap.put("Red", "#FF8080");
        colorMap.put("Red", "#FF9999");
        colorMap.put("Red", "#FFB2B2");
        colorMap.put("Red", "#FFCCCC");
        colorMap.put("Red", "#FFE6E6");
        colorMap.put("Red", "#E60000");


        //Color codes for green
//        colorMap.put("Green", "#003D00");
//        colorMap.put("Green", "#005200");
//        colorMap.put("Green", "#006600");
//        colorMap.put("Green", "#007A00");
//        colorMap.put("Green", "#008F00");
//        colorMap.put("Green", "#00A300");
//        colorMap.put("Green", "#00CC00");
//        colorMap.put("Green", "#33D633");
//        colorMap.put("Green", "#66E066");
//        colorMap.put("Green", "#99EB99");
//        colorMap.put("Green", "#CCF5CC");

        colorMap.put("Green", "#00E600");
        colorMap.put("Green", "#00ff00");
        colorMap.put("Green", "#19FF19");
        colorMap.put("Green", "#33FF33");
        colorMap.put("Green", "#4DFF4D");
        colorMap.put("Green", "#66FF66");
        colorMap.put("Green", "#80FF80");
        colorMap.put("Green", "#99FF99");
        colorMap.put("Green", "#B2FFB2");
        colorMap.put("Green", "#CCFFCC");
        colorMap.put("Green", "#E6FFE6");


        //Color codes for blue
//        colorMap.put("Blue", "#141466");
//        colorMap.put("Blue", "#1A1A80");
//        colorMap.put("Blue", "#1F1F99");
//        colorMap.put("Blue", "#2929CC");
//        colorMap.put("Blue", "#2E2EE6");
//        colorMap.put("Blue", "#3333FF");
//        colorMap.put("Blue", "#4747FF");
//        colorMap.put("Blue", "#7070FF");
//        colorMap.put("Blue", "#ADADFF");
//        colorMap.put("Blue", "#D6D6FF");
//        colorMap.put("Blue", "#EBEBFF");

        colorMap.put("Blue", "#0000E6");
        colorMap.put("Blue", "#0000ff");
        colorMap.put("Blue", "#1919FF");
        colorMap.put("Blue", "#3333FF");
        colorMap.put("Blue", "#4D4DFF");
        colorMap.put("Blue", "#6666FF");
        colorMap.put("Blue", "#8080FF");
        colorMap.put("Blue", "#9999FF");
        colorMap.put("Blue", "#B2B2FF");
        colorMap.put("Blue", "#CCCCFF");
        colorMap.put("Blue", "#E6E6FF");


        //color codes for orange
//        colorMap.put("Orange", "#4C2E00");
//        colorMap.put("Orange", "#663D00");
//        colorMap.put("Orange", "#804C00");
//        colorMap.put("Orange", "#995C00");
//        colorMap.put("Orange", "#CC7A00");
//        colorMap.put("Orange", "#FF9900");
//        colorMap.put("Orange", "#FFA319");
//        colorMap.put("Orange", "#FFB84D");
//        colorMap.put("Orange", "#FFCC80");
//        colorMap.put("Orange", "#FFE0B2");
//        colorMap.put("Orange", "#FFF5E6");
        colorMap.put("Orange", "#E68A2E");
        colorMap.put("Orange", "#FF9933");
        colorMap.put("Orange", "#FFA347");
        colorMap.put("Orange", "#FFAD5C");
        colorMap.put("Orange", "#FFB870");
        colorMap.put("Orange", "#FFC285");
        colorMap.put("Orange", "#FFCC99");
        colorMap.put("Orange", "#FFD6AD");
        colorMap.put("Orange", "#FFE0C2");
        colorMap.put("Orange", "#FFEBD6");
        colorMap.put("Orange", "#FFF5EB");
    }

    public String getColorCode(String color, int index) {
        return colorMap.get("Red").get(index);
    }

    public static ColorConstants getInstance() {
        if (colorConst == null) {
            colorConst = new ColorConstants();
        }
        return colorConst;
    }

    public static String getGradientColor1(BigDecimal divisor, BigDecimal divident, String color) {
        BigDecimal result = BigDecimal.ZERO;
        result = divident.divide(divisor, 2, RoundingMode.HALF_DOWN);
        result = result.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_DOWN);
        int index = 0;
        if (result.intValue() > 0 && result.intValue() <= 10) {
            index = 10;
        } else if (result.intValue() > 0 && result.intValue() <= 10) {
            index = 9;
        } else if (result.intValue() > 10 && result.intValue() <= 20) {
            index = 8;
        } else if (result.intValue() > 20 && result.intValue() <= 30) {
            index = 7;
        } else if (result.intValue() > 30 && result.intValue() <= 40) {
            index = 6;
        } else if (result.intValue() > 40 && result.intValue() <= 50) {
            index = 5;
        } else if (result.intValue() > 50 && result.intValue() <= 60) {
            index = 4;
        } else if (result.intValue() > 60 && result.intValue() <= 70) {
            index = 3;
        } else if (result.intValue() > 70 && result.intValue() <= 80) {
            index = 2;
        } else if (result.intValue() > 80 && result.intValue() <= 90) {
            index = 1;
        } else if (result.intValue() > 90 && result.intValue() <= 100) {
            index = 0;
        } else {
            index = 0;
        }
        return colorMap.get(color).get(index);
    }

    public static String getGradientColor(BigDecimal actualVal, BigDecimal diffVal, String color, MathOperator operator) {

        int index = 0;
        BigDecimal interval = diffVal.divide(new BigDecimal(10), 2, RoundingMode.HALF_DOWN);

        int originalIndex = 0;

        double actualDouble = actualVal.doubleValue();
        double intervalDouble = interval.doubleValue();
        double temp;
        double temp1 = intervalDouble;
        if (actualDouble > 0 && actualDouble <= intervalDouble) {
            index = 10;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;
        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 9;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 8;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 7;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + intervalDouble;
        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 6;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 5;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 4;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 3;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 2;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 1;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (actualDouble > temp && actualDouble <= intervalDouble) {
            index = 0;
        }
        temp = intervalDouble;
        intervalDouble = intervalDouble + temp1;

        if (operator == MathOperator.LESS_THAN || operator == MathOperator.LESS_THAN_EQUAL_TO) {
            if (index == 0) {
                index = 10;
            } else if (index == 1) {
                index = 9;
            } else if (index == 2) {
                index = 8;
            } else if (index == 3) {
                index = 7;
            } else if (index == 4) {
                index = 6;
            } else if (index == 5) {
                index = 5;
            } else if (index == 6) {
                index = 4;
            } else if (index == 7) {
                index = 3;
            } else if (index == 8) {
                index = 2;
            } else if (index == 9) {
                index = 1;
            } else if (index == 10) {
                index = 0;
            }
        }
        if (colorMap.get(color) != null) {
            return colorMap.get(color).get(index);
        } else {
            return "";
        }

    }

    public static void main(String a[]) {
        ColorConstants cc = new ColorConstants();
//        
//        
//        
//        
    }
}
