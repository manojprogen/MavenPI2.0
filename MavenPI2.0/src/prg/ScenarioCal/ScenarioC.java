package prg.ScenarioCal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ScenarioC {

    public float getScenarioVal(float tempDev) {
        float d = tempDev;//deviation[i];
        float p = (float) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        float tmp = Math.round(Rval2);
        float finalDev = tmp / p;
        return finalDev;
    }

    public String getScenarioValDouble(double tempDev) {
        double d = tempDev;//deviation[i];
        double p = (double) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        double tmp = Math.round(Rval2);
        double finalDev = tmp / p;

        double calValue = tempDev;
        String modifiedFinalValue = "";

        /*
         * if ((finalDev / getPowerOfTen(12)) > 0.99) {
         * ////////////////////////////////////////.println.println(" ifoidf ");
         * calValue = (finalDev / getPowerOfTen(12)); modifiedFinalValue =
         * String.valueOf(truncate(calValue)); } else if ((finalDev /
         * getPowerOfTen(9)) > 0.99) {
         * ////////////////////////////////////////.println.println(" in tt ");
         * calValue = (finalDev / getPowerOfTen(9)); modifiedFinalValue =
         * String.valueOf(truncate(calValue)); } else if ((finalDev /
         * getPowerOfTen(6)) > 0.99) { calValue = (finalDev / getPowerOfTen(6));
         * modifiedFinalValue = String.valueOf(truncate(calValue)); } else if
         * ((finalDev / getPowerOfTen(3)) > 0.99) { calValue = (finalDev /
         * getPowerOfTen(3)); modifiedFinalValue =
         * String.valueOf(truncate(calValue)); } else { modifiedFinalValue =
         * String.valueOf(calValue); }
         */
        modifiedFinalValue = String.valueOf(calValue);

        return modifiedFinalValue;
    }

    public String getScenarioValDouble(String tempDev) {
        double d = Double.parseDouble(tempDev);//deviation[i];
        double p = (double) Math.pow(10, 2);
        double Rval2 = 0;
        Rval2 = d * p;
        double tmp = Math.round(Rval2);
        double finalDev = tmp / p;

        double calValue = Double.parseDouble(tempDev);
        String modifiedFinalValue = "";

        if ((finalDev / getPowerOfTen(12)) > 0.99) {
            calValue = (finalDev / getPowerOfTen(12));
            modifiedFinalValue = String.valueOf(truncate(calValue)) + "T";
        } else if ((finalDev / getPowerOfTen(9)) > 0.99) {
            calValue = (finalDev / getPowerOfTen(9));
            modifiedFinalValue = String.valueOf(truncate(calValue)) + "B";
        } else if ((finalDev / getPowerOfTen(6)) > 0.99) {
            calValue = (finalDev / getPowerOfTen(6));
            modifiedFinalValue = String.valueOf(truncate(calValue)) + "M";
        } else if ((finalDev / getPowerOfTen(3)) > 0.99) {
            calValue = (finalDev / getPowerOfTen(3));
            modifiedFinalValue = String.valueOf(truncate(calValue)) + "K";
        } else {
            modifiedFinalValue = String.valueOf(calValue);
        }

        return modifiedFinalValue;
    }

    public long getPowerOfTen(int num) {
        long bd = 1;
        for (int i = 0; i < num; i++) {
            bd = bd * 10;
        }
        return bd;
    }

    private double truncate(double x) {
        DecimalFormat df = new DecimalFormat("0.##");
        String d = df.format(x);
        d = d.replaceAll(",", ".");
//        Double dbl = new Double(d);
        Double dbl = Double.parseDouble(d);
        return dbl;
    }

    public String getModifiedNumber(double bd, String nbrSymbol) {

        String temp = "";
        NumberFormat nFormat = NumberFormat.getInstance(Locale.US);
        nFormat.setMaximumFractionDigits(2);
        nFormat.setMinimumFractionDigits(2);
        if (nbrSymbol != null && !nbrSymbol.equalsIgnoreCase("")) {
            if (nbrSymbol.equalsIgnoreCase("K")) {
                bd = (bd / getPowerOfTen(3));
                //temp = String.valueOf(truncate(bd)) + "K";
                temp= nFormat.format(bd) + "K";
            } else if (nbrSymbol.equalsIgnoreCase("Mn")) {
                bd = (bd / getPowerOfTen(6));
                //temp = String.valueOf(truncate(bd)) + "M";
                temp= nFormat.format(bd) + "M";
            } else if (nbrSymbol.equalsIgnoreCase("Abs")) {
                //temp = String.valueOf(bd);
                temp = nFormat.format(bd);
            } else if (nbrSymbol.equalsIgnoreCase("M")) {
                bd = (bd / getPowerOfTen(6));
                //temp = String.valueOf(truncate(bd)) + "M";
                temp= nFormat.format(bd) + "M";
            }

        } else {
            //temp = String.valueOf(bd);
            temp = nFormat.format(bd);
        }
        //.out.println("temp2=="+temp);
        return temp;
        /*
         * if ((bd / getPowerOfTen(12)) > 0.99) // check for Trillion { bd = (bd
         * / getPowerOfTen(12)); temp = String.valueOf(truncate(bd)) + "Tn"; }
         * else if ((bd / getPowerOfTen(9)) > 0.99) // check for Billion { bd =
         * (bd / getPowerOfTen(9)); temp = String.valueOf(truncate(bd)) + "Bn";
         * }
         *
         * if ((bd / getPowerOfTen(6)) > 0.99) // check for Million { bd = (bd /
         * getPowerOfTen(6)); temp = String.valueOf(truncate(bd)) + "Mn"; } else
         * if ((bd / getPowerOfTen(3)) > 0.99) // check for K { bd = (bd /
         * getPowerOfTen(3)); temp = String.valueOf(truncate(bd)) + "K"; } else
         * { temp = String.valueOf(bd); }
         */

    }
}
