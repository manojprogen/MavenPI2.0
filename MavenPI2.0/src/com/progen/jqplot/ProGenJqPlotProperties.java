/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.jqplot;

/**
 *
 * @author LAPTOP11
 */
public class ProGenJqPlotProperties {

    public static String xaxisfontSize = "7.35pt";
    public static String yaxisfontSize = "7pt";
    public static String xaxisfontcolor = "black";
    public static String yaxisfontcolor = "black";
    public static String fontFamily = "Verdana";
    public static String xaxisangle = "-30";
    public static String yaxisangle = "-30";
    public static String legendfontSize = "10pt";
    public static String[] seriescolors1 = {"#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF", "#737CA1", "#7E354D", "#E66C2C", "#A74AC7", "#307D7E"};
    public static String[] seriescolors = {"#ADA96E", "#2F4F4F", "#A9A9A9", "#808000", "#B0171F", "#DC143C", "#b8860b", "#f5deb3", "#9932cc", "#d8bfd8", "#66cdaa", "#FF00FF"};
    private static String GrpFrameHeight;
    private static String GrpdivHeight;
    private static String Grpdivwidth;
    private static boolean labels = false;

    /**
     * @return the GrpFrameHeight
     */
    public static String getGrpFrameHeight() {
        return GrpFrameHeight;
    }

    /**
     * @param aGrpFrameHeight the GrpFrameHeight to set
     */
    public static void setGrpFrameHeight(String aGrpFrameHeight) {
        GrpFrameHeight = aGrpFrameHeight;
    }

    /**
     * @return the GrpdivHeight
     */
    public static String getGrpdivHeight() {
        return GrpdivHeight;
    }

    /**
     * @param aGrpdivHeight the GrpdivHeight to set
     */
    public static void setGrpdivHeight(String aGrpdivHeight) {
        GrpdivHeight = aGrpdivHeight;
    }

    /**
     * @return the Grpdivwidth
     */
    public static String getGrpdivwidth() {
        return Grpdivwidth;
    }

    /**
     * @param aGrpdivwidth the Grpdivwidth to set
     */
    public static void setGrpdivwidth(String aGrpdivwidth) {
        Grpdivwidth = aGrpdivwidth;
    }

    /**
     * @return the labels
     */
    public static boolean isLabels() {
        return labels;
    }

    /**
     * @param aLabels the labels to set
     */
    public static void setLabels(boolean aLabels) {
        labels = aLabels;
    }
}
