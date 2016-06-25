/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.display.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author progen
 */
public class NumberFormatter {

    public static String getModifiedNumber(double bd, String nbrSymbol) {
        String temp = "";
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                bd = (bd / getPowerOfTen(3));
                temp = nFormat.format(bd) + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                bd = (bd / getPowerOfTen(6));
                temp = nFormat.format(bd) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                bd = (bd / getPowerOfTen(6));
                temp = nFormat.format(bd) + " M";
            }
        } else {
            temp = nFormat.format(bd);
        }
        return temp;
    }

    public static String getModifiedNumber(BigDecimal bd) {
        String str = "";
        String temp = "";
        str =
                bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;

        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(1);
        nFormat.setMinimumFractionDigits(1);

        /*
         * if ((num / getPowerOfTen(12)) > 0.99) // check for Trillion {
         * doubleVal = (num / getPowerOfTen(12)); temp =
         * String.valueOf(truncate(doubleVal)) + "Tn"; } else if ((num /
         * getPowerOfTen(9)) > 0.99) // check for Billion { doubleVal = (num /
         * getPowerOfTen(9)); temp = String.valueOf(truncate(doubleVal)) + "Bn";
         * } else
         */

        if ((num / getPowerOfTen(6)) > 0.99) // check for Million
        {
            doubleVal = (num / getPowerOfTen(6));
            //temp = String.valueOf(truncate(doubleVal)) + "M";
            temp =
                    nFormat.format(doubleVal) + " M";
        } else if ((num / getPowerOfTen(3)) > 0.99) // check for K
        {
            doubleVal = (num / getPowerOfTen(3));
            //temp = String.valueOf(truncate(doubleVal)) + "K";
            temp =
                    nFormat.format(doubleVal) + " K";
        } else {
            //temp = String.valueOf(num);
            temp = nFormat.format(num);
        }
        // ////.println("temp===="+temp);
        return temp;
    }

    public static String getModifidNumber(BigDecimal bd) {       //target Basis
        String str = "";
        String temp = "";
        str = bd.toString();

        double num = Double.parseDouble(str);
        BigDecimal decimal = new BigDecimal(num);
        bd = decimal.setScale(2, RoundingMode.HALF_EVEN);
        temp = bd.toString();
        return temp;
    }

    public static String getModifiedNumber(BigDecimal bd, String nbrSymbol) {
        //  ////.println("nbrSymbol=="+nbrSymbol);
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
       //  NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
       //   NumberFormat nFormat = NumberFormat.getCurrencyInstance();


        /*
         * if ((num / getPowerOfTen(12)) > 0.99) // check for Trillion {
         * doubleVal = (num / getPowerOfTen(12)); temp =
         * String.valueOf(truncate(doubleVal)) + "Tn"; } else if ((num /
         * getPowerOfTen(9)) > 0.99) // check for Billion { doubleVal = (num /
         * getPowerOfTen(9)); temp = String.valueOf(truncate(doubleVal)) + "Bn";
         * } else
         */

        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
                temp = nFormat.format(bd) + " %";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Bn")) {
                doubleVal = (num / getPowerOfTen(9));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "Bn";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " Cr";
            }

        } else {
            if (Math.round(bd.doubleValue()) == bd.doubleValue()) {
                nFormat.setMaximumFractionDigits(0);
                nFormat.setMinimumFractionDigits(0);
            } else {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
            }
            temp = nFormat.format(bd);
          //  temp=bd.toString();
        }

        //  ////.println("temp1=="+temp);
        return temp;
    }

    public static String getModifiedNumber(double num, String nbrSymbol, int precision) {
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        String temp = "";

        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(num);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(nFormat, num, precision);
                temp = nFormat.format(num) + " %";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " Cr";
            } else {
                setFormat(nFormat, num, precision);
                temp = nFormat.format(num);
            }
        } else {
            setFormat(nFormat, num, precision);
            temp = nFormat.format(num);
        }

        return temp;
    }
    //added by sruthi for inidan rs for dashboard

    public static String indianFormate(Double value, String symbol) {
        DecimalFormat formatter = new DecimalFormat("##,###");
        boolean negFlag = value < 0 ? true : false;
        Double val1 = null;
        String temp = "";
        int flag = 0;
        value = Math.abs(value);
        if (value.intValue() > 9999) {
            formatter.applyPattern("#,##");
            temp = formatter.format((int) (value.intValue() / 1000)) + ",";
            formatter.applyPattern("#,###");
            if ((value.intValue() - (int) (value.intValue() / 1000) * 1000) < 10 && !((value.intValue() - (int) (value.intValue() / 1000) * 1000) == 0)) {
                temp += "00";
            } else if ((value.intValue() - (int) (value.intValue() / 1000) * 1000) < 100 && !((value.intValue() - (int) (value.intValue() / 1000) * 1000) == 0)) {
                temp += "0";
            }
            temp += formatter.format(value.intValue() - (int) (value.intValue() / 1000) * 1000);
            if ((value.intValue() - (int) (value.intValue() / 1000) * 1000) == 0) {
                temp += "00";
            }
        } else if (value.intValue() >= 1000 && value.intValue() <= 9999) {
            formatter.applyPattern("#,###");
            temp = formatter.format(value.intValue());
        } else {
            temp += value.intValue();
        }
        if (negFlag == true) {
            return "-" + temp;
        } else {
            return temp;
        }
    }
    //     added by sruthi for inidan rs for dashboard
//        public static String indianFormate1(String value,String symbol){
//            String temp=null;
//             value=value.replace(",","");
//            //  char lastDigit=value.charAt(value.length()-1);
//              String result = "";
//            int len = value.length()-1;
//            int nDigits = 0;
//            for (int i = len - 1; i >= 0; i--)
//            {
//                 result = value.charAt(i) + result;
//                nDigits++;
//                if (((nDigits % 2) == 0) && (i > 0))
//                {
//                    result = "," + result;
//                }
//            }
//                    return (result);
//        }

    public static String getDecimalIndianFormate(String numberformate) {
        String part1 = "";
        String part2 = "";
        String[] parts = numberformate.split("[.]");
        part1 = parts[0];
        part2 = "." + parts[1];
        Double val1 = new Double(part1.replace(",", ""));
        return (val1 + "," + part2);
    }

    public static String getModifiedNumberDashboard(double num, String nbrSymbol, int precision) {
        double doubleVal;
        Double val = new Double(num);
        String normaltemp = "";
        String part2 = "";
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        String temp = "";
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            String value2 = null;
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                    value2 = value.split(",")[0];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                //val=num;
                setFormat(nFormat, num, precision);
                nbrSymbol = "";
                normaltemp = nFormat.format(val);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                val = num;
                setFormat(nFormat, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "%";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " M";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];

                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " Cr";
            } else {
                setFormat(nFormat, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            }
        } else {
            setFormat(nFormat, num, precision);
            normaltemp = nFormat.format(num);
            if (normaltemp.contains(".")) {
                String value = getDecimalIndianFormate(normaltemp);
                val = Double.parseDouble(value.split(",")[0]);
                part2 = value.split(",")[1];
            } else {
                val = Double.parseDouble(normaltemp.replace(",", ""));
            }
            temp = indianFormate(val, nbrSymbol) + part2;
        }

        return temp;
    }

    public static String getModifiedNumberDashboard(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        String last_three_digits = "";
        String remaining_units = "";
        String split_rupee[];
        String explore_remaining_units = "";
        str = bd.toString();
        String normaltemp = "";
        String part2 = "";
        double num = Double.parseDouble(str);
        double doubleVal;
        Double val = new Double(num);
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        DecimalFormat formatter = new DecimalFormat("##,###");
        boolean negFlag = val < 0 ? true : false;
        val = Math.abs(val);
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                setFormat(formatter, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(formatter, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "%";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " M";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2 + " Cr";
            } else {
                setFormat(nFormat, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = Double.parseDouble(normaltemp.replace(",", ""));
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            }
        } else {
            setFormat(nFormat, num, precision);
            normaltemp = nFormat.format(num);
            if (normaltemp.contains(".")) {
                String value = getDecimalIndianFormate(normaltemp);
                val = Double.parseDouble(value.split(",")[0]);
                part2 = value.split(",")[1];
            } else {
                val = Double.parseDouble(normaltemp.replace(",", ""));
            }
            temp = indianFormate(val, nbrSymbol) + part2;
        }
        return temp;
    }
//ended by sruthi

    public static String getModifiedNumber(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);

        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd) + "%";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal) + " Cr";
            } else {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd);
            }
        } else {
            setFormat(nFormat, bd.doubleValue(), precision);
            temp = nFormat.format(bd);
        }

        //  ////.println("temp1=="+temp);
        return temp;
    }
    //added by sruthi for numberformate
    public static String getModifiedNumberFormate(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);

        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd);
            }
        } else {
            setFormat(nFormat, bd.doubleValue(), precision);
            temp = nFormat.format(bd);
        }

        //  ////.println("temp1=="+temp);
        return temp;
    }
//ended by sruthi

    public static long getPowerOfTen(int num) {
        long bd = 1;
        for (int i = 0; i
                < num; i++) {
            bd = bd * 10;
        }

        return bd;
    }

    private static void setFormat(NumberFormat nFormat, double value, int precision) {
        if (precision < 0) {
            if (Math.round(value) == value) {
                nFormat.setMaximumFractionDigits(0);
                nFormat.setMinimumFractionDigits(0);
            } else {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
            }
        } else {
            nFormat.setMaximumFractionDigits(precision);
            nFormat.setMinimumFractionDigits(precision);
        }
    }

    private static void setFormat(NumberFormat nFormat, double value) {
        if (Math.round(value) == value) {
            nFormat.setMaximumFractionDigits(0);
            nFormat.setMinimumFractionDigits(0);
        } else {
            nFormat.setMaximumFractionDigits(2);
            nFormat.setMinimumFractionDigits(2);
        }
    }

    public static String getModifiedNumberFormat(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);

        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd) + "%";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                temp = nFormat.format(doubleVal);
            } else {
                setFormat(nFormat, bd.doubleValue(), precision);
                temp = nFormat.format(bd);
            }
        } else {
            setFormat(nFormat, bd.doubleValue(), precision);
            temp = nFormat.format(bd);
        }

        //  ////.println("temp1=="+temp);
        return temp;
    }

    public static String getModifiedNumberFormatSymbol(String nbrSymbol) {
//changed by sruthi
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                return (" In '000");
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                return ("In Millions");
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                return ("In Lakhs");
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                return ("In Crores");
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                return ("");
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                return ("");
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                return ("In Millions");
            }
        }

        return "";

    }

//added by krishan Pratap
    public static String getModifiedNumberReport(BigDecimal bd, String nbrSymbol, int precision) {
        String str = "";
        String temp = "";
        String last_three_digits = "";
        String remaining_units = "";
        String split_rupee[];
        String explore_remaining_units = "";
        str = bd.toString();
        String normaltemp = "";
        String part2 = "";
        double num = Double.parseDouble(str);
        double doubleVal;
        Double val = new Double(num);
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        DecimalFormat formatter = new DecimalFormat("##,###");
        boolean negFlag = val < 0 ? true : false;
        val = Math.abs(val);
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = doubleVal;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "K";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = doubleVal;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")) {
                setFormat(formatter, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = num;
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                setFormat(formatter, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = num;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "%";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = doubleVal;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "M";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = doubleVal;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "L";
            } else if (nbrSymbol.equalsIgnoreCase("C")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal, precision);
                normaltemp = nFormat.format(doubleVal);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = doubleVal;
                }
                temp = indianFormate(val, nbrSymbol) + part2 + "C";
            } else {
                setFormat(nFormat, num, precision);
                normaltemp = nFormat.format(num);
                if (normaltemp.contains(".")) {
                    String value = getDecimalIndianFormate(normaltemp);
                    val = Double.parseDouble(value.split(",")[0]);
                    part2 = value.split(",")[1];
                } else {
                    val = num;
                }
                temp = indianFormate(val, nbrSymbol) + part2;
            }
        } else {
            setFormat(nFormat, num, precision);
            normaltemp = nFormat.format(num);
            if (normaltemp.contains(".")) {
                String value = getDecimalIndianFormate(normaltemp);
                val = Double.parseDouble(value.split(",")[0]);
                part2 = value.split(",")[1];
            } else {
                val = num;
            }
            temp = indianFormate(val, nbrSymbol) + part2;
        }
        return temp;
    }
    
    //added by krishan for append suffix is applied 
     public static String getModifiedNumberColor(BigDecimal bd, String nbrSymbol) {
        //  ////.println("nbrSymbol=="+nbrSymbol);
        String str = "";
        String temp = "";
        str = bd.toString();
        double num = Double.parseDouble(str);
        double doubleVal;
   
         NumberFormat nFormat = NumberFormat.getInstance(Locale.US);        
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                doubleVal = (num / getPowerOfTen(3));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs") || nbrSymbol.equalsIgnoreCase("A")|| nbrSymbol.equalsIgnoreCase("Ab") ) {
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("%")) {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
                temp = nFormat.format(bd) + " %";
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                doubleVal = (num / getPowerOfTen(6));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " M";
            } else if (nbrSymbol.equalsIgnoreCase("Bn")) {
                doubleVal = (num / getPowerOfTen(9));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + "Bn";
            } else if (nbrSymbol.equalsIgnoreCase("L")) {
                doubleVal = (num / getPowerOfTen(5));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " L";
            } else if (nbrSymbol.equalsIgnoreCase("Cr")) {
                doubleVal = (num / getPowerOfTen(7));
                setFormat(nFormat, doubleVal);
                temp = nFormat.format(doubleVal) + " Cr";
            }

        } else {
            if (Math.round(bd.doubleValue()) == bd.doubleValue()) {
                nFormat.setMaximumFractionDigits(0);
                nFormat.setMinimumFractionDigits(0);
            } else {
                nFormat.setMaximumFractionDigits(2);
                nFormat.setMinimumFractionDigits(2);
            }
            temp = nFormat.format(bd);
          //  temp=bd.toString();
        }

        //  ////.println("temp1=="+temp);
        return temp;
    }

}
